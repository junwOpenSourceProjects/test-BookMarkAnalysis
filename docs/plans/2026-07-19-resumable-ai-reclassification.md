# Resumable AI Bookmark Reclassification Implementation Plan

> **For Hermes:** Use subagent-driven-development skill to implement this plan task-by-task.

**Goal:** Replace the current in-memory smart-classification flow with a database-backed, pause/resume full bookmark-directory rebuild that groups large registrable domains, clusters small domains by AI-derived topic, and automatically applies each durable result.

**Architecture:** Keep `bookmark_ai_classification_task` and `bookmark_ai_classification_result` as the task/result backbone, extend them with rebuild state, and add snapshot, domain-group, work-unit, and folder-application tables. A new database-driven orchestration service owns state transitions; a single worker executes one persisted AI work unit at a time so pause/restart/error recovery is exact. The frontend replaces strategy/preview/apply controls with start, pause, resume, durable progress, and recoverable-task views.

**Tech Stack:** Java 25, Spring Boot 3.4.13, MyBatis-Plus 3.5.9, MySQL 8, Hutool JSON/HTTP, Guava `InternetDomainName` for public-suffix-aware registrable domains, JUnit 5 + Mockito, Nuxt 4, Vue 3, Nuxt UI v4, TypeScript.

**Design source:** `docs/superpowers/specs/2026-07-19-resumable-ai-reclassification-design.md`

---

## Execution conventions

- Work in the backend repository: `/Users/junw/Documents/GitHub/BookMarkAnalysis/test-BookMarkAnalysis` unless a task explicitly names the sibling frontend repository `../test-BookMarkAnalysisVue3`.
- Do **not** modify or commit the pre-existing uncommitted change to `src/main/resources/application.yml`.
- Use `Long` as Java IDs internally; serialize IDs as strings in API maps/DTOs so Snowflake IDs retain precision in Vue.
- Do not store the AI API key in any task table, prompt record, log, or response. On every worker execution resolve the key from `BookmarkConfig`; persist non-secret endpoint/model metadata plus AI input/output only.
- The new rebuild flow is always AI-enabled. Remove the old `strategy` and `useAI` choice from the new UI/API; keep legacy non-rebuild endpoints only if another page still calls them, but the toolbox must use only the new routes.
- A user-triggered pause is cooperative at a persisted-work-unit boundary. A worker may finish the one AI HTTP request already in flight, persist its whole result transactionally, then stop before claiming another unit.
- Use one active work unit per running rebuild task. This deliberately trades prior three-way AI concurrency for safe database recovery and the requirement that a failure pauses the whole task before further work starts.
- Run `./mvnw spotless:apply` before backend test/compile commands. Frontend verification is `pnpm exec nuxi typecheck` followed by `pnpm build` in `../test-BookMarkAnalysisVue3`.

## Durable data model and workflow constants

Implement these constants in one place, not as duplicated literals:

```java
public final class ReclassificationConstants {
    public static final int LARGE_DOMAIN_MIN_BOOKMARKS = 5;
    public static final int BOOKMARK_ANALYSIS_BATCH_SIZE = 25;
    public static final int SMALL_POOL_CLUSTER_DRAFT_SIZE = 80;
    public static final String TASK_PHASE_PREPARING = "PREPARING";
    public static final String TASK_PHASE_LARGE_DOMAINS = "LARGE_DOMAINS";
    public static final String TASK_PHASE_SMALL_ANALYSIS = "SMALL_ANALYSIS";
    public static final String TASK_PHASE_SMALL_CLUSTER_DRAFTS = "SMALL_CLUSTER_DRAFTS";
    public static final String TASK_PHASE_SMALL_CANONICALIZATION = "SMALL_CANONICALIZATION";
    public static final String TASK_PHASE_APPLYING = "APPLYING";
}
```

Use the following state values exactly:

```text
Task:      QUEUED, RUNNING, PAUSED, RECOVERABLE, COMPLETED, FAILED
Work unit: PENDING, RUNNING, SUCCEEDED, RETRYABLE_FAILED, PAUSED
Result:    PENDING, APPLIED
```

Use the following work-unit kinds exactly:

```text
LARGE_DOMAIN_FOLDER_NAME
LARGE_DOMAIN_BOOKMARK_ANALYSIS
SMALL_POOL_BOOKMARK_ANALYSIS
SMALL_POOL_CLUSTER_DRAFT
SMALL_POOL_CANONICALIZE_FOLDERS
```

The work-unit input/output JSON must contain only task snapshot data and AI model output. Never store HTTP headers or the API key.

---

### Task 1: Establish a safe baseline and record the replacement boundary

**Objective:** Verify the existing old classification behavior and make the migration boundary explicit before changing code.

**Files:**
- Modify: `docs/superpowers/specs/2026-07-19-resumable-ai-reclassification-design.md` only if the implementation exploration reveals a contradiction; otherwise no source change.
- Inspect: `src/main/java/wo1261931780/testBookMarkAnalysis/service/SmartClassificationTaskService.java`
- Inspect: `src/main/java/wo1261931780/testBookMarkAnalysis/service/SmartClassificationService.java`
- Inspect: `src/main/java/wo1261931780/testBookMarkAnalysis/controller/ShowMeListController.java:430-518`

**Step 1: Run current focused backend tests.**

Run:

```bash
./mvnw -Dtest=SmartClassificationServiceTest,SmartClassificationTaskServiceTest test
```

Expected: current tests pass before refactoring.

**Step 2: Record the old endpoints that the new toolbox must stop using.**

The old toolbox routes are:

```text
POST /BookMarks/toolbox/classify/start
GET  /BookMarks/toolbox/classify/task/{taskId}
GET  /BookMarks/toolbox/classify/task/{taskId}/result
POST /BookMarks/toolbox/classify/task/{taskId}/apply
POST /BookMarks/toolbox/applyClassify
```

Do not delete the old code in this task. Later tasks replace the toolbox consumer and then remove duplicate “manual apply” behavior from the rebuild path.

**Step 3: Commit only if this task caused a documentation correction.**

```bash
git add docs/superpowers/specs/2026-07-19-resumable-ai-reclassification-design.md
git commit -m "docs(ai): clarify reclassification migration boundary"
```

If no documentation changed, do not create an empty commit.

---

### Task 2: Add the schema migration and provision new installations

**Objective:** Make every object required to recover a task and audit AI analysis durable in MySQL.

**Files:**
- Create: `sql/migrations/20260719_resumable_ai_reclassification.sql`
- Modify: `docker/mysql/init/01-init.sql`
- Modify: `sql/schema.sql`

**Step 1: Write the migration first.**

Extend the current task/result tables and create four new tables. Use `utf8mb4`, no foreign keys, Snowflake `BIGINT` IDs, and indexes/unique keys below.

```sql
ALTER TABLE bookmark_ai_classification_task
  ADD COLUMN phase VARCHAR(48) NOT NULL DEFAULT 'PREPARING' AFTER status,
  ADD COLUMN api_base_url VARCHAR(512) NULL AFTER model_name,
  ADD COLUMN tree_cleared_at DATETIME NULL AFTER completed_at,
  ADD COLUMN paused_at DATETIME NULL AFTER tree_cleared_at,
  ADD COLUMN resumed_at DATETIME NULL AFTER paused_at,
  ADD COLUMN recovery_count INT NOT NULL DEFAULT 0 AFTER failed_count,
  ADD COLUMN total_work_unit_count INT NOT NULL DEFAULT 0 AFTER recovery_count,
  ADD COLUMN completed_work_unit_count INT NOT NULL DEFAULT 0 AFTER total_work_unit_count,
  ADD COLUMN created_folder_count INT NOT NULL DEFAULT 0 AFTER completed_work_unit_count,
  ADD COLUMN moved_bookmark_count INT NOT NULL DEFAULT 0 AFTER created_folder_count,
  ADD COLUMN updated_title_count INT NOT NULL DEFAULT 0 AFTER moved_bookmark_count;

ALTER TABLE bookmark_ai_classification_result
  ADD COLUMN domain_group_id BIGINT NULL AFTER task_id,
  ADD COLUMN logical_folder_key VARCHAR(128) NULL AFTER suggested_folder,
  ADD COLUMN topic_hint VARCHAR(256) NULL AFTER page_type,
  ADD COLUMN analysis_json JSON NULL AFTER reason,
  ADD UNIQUE KEY uk_task_bookmark (task_id, bookmark_id),
  ADD KEY idx_task_folder (task_id, logical_folder_key);
```

Create the new tables with these required columns and keys:

```sql
CREATE TABLE bookmark_ai_reclassification_snapshot (
  id BIGINT NOT NULL,
  task_id BIGINT NOT NULL,
  bookmark_id BIGINT NOT NULL,
  href TEXT NULL,
  original_title TEXT NULL,
  registrable_domain VARCHAR(255) NULL,
  ordinal INT NOT NULL,
  pool_type VARCHAR(20) NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_task_bookmark (task_id, bookmark_id),
  KEY idx_task_domain (task_id, registrable_domain),
  KEY idx_task_pool_ordinal (task_id, pool_type, ordinal)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE bookmark_ai_reclassification_domain_group (
  id BIGINT NOT NULL,
  task_id BIGINT NOT NULL,
  registrable_domain VARCHAR(255) NOT NULL,
  bookmark_count INT NOT NULL,
  group_type VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL,
  logical_folder_key VARCHAR(128) NULL,
  folder_name VARCHAR(512) NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_task_domain (task_id, registrable_domain),
  KEY idx_task_type_status (task_id, group_type, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE bookmark_ai_reclassification_work_unit (
  id BIGINT NOT NULL,
  task_id BIGINT NOT NULL,
  domain_group_id BIGINT NULL,
  unit_kind VARCHAR(64) NOT NULL,
  unit_key VARCHAR(128) NOT NULL,
  status VARCHAR(20) NOT NULL,
  ordinal INT NOT NULL DEFAULT 0,
  attempt_count INT NOT NULL DEFAULT 0,
  prompt_version VARCHAR(32) NOT NULL,
  input_json JSON NOT NULL,
  request_json JSON NULL,
  raw_response MEDIUMTEXT NULL,
  parsed_output_json JSON NULL,
  error_message TEXT NULL,
  started_at DATETIME NULL,
  completed_at DATETIME NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_task_unit_key (task_id, unit_key),
  KEY idx_task_status_ordinal (task_id, status, ordinal),
  KEY idx_group_kind (domain_group_id, unit_kind)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE bookmark_ai_reclassification_folder_application (
  id BIGINT NOT NULL,
  task_id BIGINT NOT NULL,
  logical_folder_key VARCHAR(128) NOT NULL,
  folder_name VARCHAR(512) NOT NULL,
  folder_id BIGINT NULL,
  source_phase VARCHAR(48) NOT NULL,
  status VARCHAR(20) NOT NULL,
  applied_at DATETIME NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_task_folder_key (task_id, logical_folder_key),
  KEY idx_task_status (task_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

Use `ADD COLUMN IF NOT EXISTS` only if the target MySQL 8 deployment version supports it; otherwise document that this migration is a one-time forward migration and must not be re-run against an already altered database. Do not silently swallow DDL errors.

**Step 2: Add the equivalent create-table definitions to `docker/mysql/init/01-init.sql` and `sql/schema.sql`.**

The Docker init script is only for a fresh volume. It must contain the complete final table definitions (not `ALTER TABLE` statements that assume legacy tables), while `sql/migrations/...` upgrades existing databases.

**Step 3: Validate the migration on a disposable database.**

Run with Docker MySQL running:

```bash
docker compose up -d mysql
docker compose exec -T mysql mysql -uroot -p"${MYSQL_ROOT_PASSWORD:-root123456}" bookmarks \
  < sql/migrations/20260719_resumable_ai_reclassification.sql
docker compose exec -T mysql mysql -uroot -p"${MYSQL_ROOT_PASSWORD:-root123456}" -e \
  "SHOW TABLES FROM bookmarks LIKE 'bookmark_ai_reclassification%';"
```

Expected: four new table names are listed and `DESCRIBE bookmark_ai_classification_task;` includes all rebuild-state columns.

**Step 4: Commit.**

```bash
git add sql/migrations/20260719_resumable_ai_reclassification.sql docker/mysql/init/01-init.sql sql/schema.sql
git commit -m "feat(ai): add resumable reclassification schema"
```

---

### Task 3: Add persistent entities, mappers, and task status constants

**Objective:** Make all new tables and added columns available through typed MyBatis-Plus entities rather than `Map`-only database access.

**Files:**
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/entity/AiReclassificationSnapshot.java`
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/entity/AiReclassificationDomainGroup.java`
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/entity/AiReclassificationWorkUnit.java`
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/entity/AiReclassificationFolderApplication.java`
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/mapper/AiReclassificationSnapshotMapper.java`
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/mapper/AiReclassificationDomainGroupMapper.java`
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/mapper/AiReclassificationWorkUnitMapper.java`
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/mapper/AiReclassificationFolderApplicationMapper.java`
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationConstants.java`
- Modify: `src/main/java/wo1261931780/testBookMarkAnalysis/entity/AiClassificationTask.java`
- Modify: `src/main/java/wo1261931780/testBookMarkAnalysis/entity/AiClassificationResult.java`
- Test: `src/test/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationConstantsTest.java`

**Step 1: Write a failing constants test.**

```java
@Test
void usesTheConfirmedThresholdAndRecoverableStates() {
    assertEquals(5, ReclassificationConstants.LARGE_DOMAIN_MIN_BOOKMARKS);
    assertEquals("RECOVERABLE", ReclassificationConstants.TASK_STATUS_RECOVERABLE);
    assertEquals("SMALL_POOL_CANONICALIZE_FOLDERS", ReclassificationConstants.UNIT_SMALL_POOL_CANONICALIZE_FOLDERS);
}
```

Run:

```bash
./mvnw -Dtest=ReclassificationConstantsTest test
```

Expected: FAIL because the constants class does not yet exist.

**Step 2: Implement constants and entities.**

Use `@TableName`, `@TableId(type = IdType.ASSIGN_ID)`, Lombok `@Data`, and Java `LocalDateTime` exactly as current task/result entities do. Ensure JSON database columns are represented as `String`; conversion is performed in services with `JSONUtil`, keeping mapper configuration simple.

Add fields such as:

```java
// AiReclassificationWorkUnit
private Long taskId;
private Long domainGroupId;
private String unitKind;
private String unitKey;
private String status;
private Integer ordinal;
private Integer attemptCount;
private String promptVersion;
private String inputJson;
private String requestJson;
private String rawResponse;
private String parsedOutputJson;
private String errorMessage;
private LocalDateTime startedAt;
private LocalDateTime completedAt;
```

Add all Task 2 state/count/timestamp fields to `AiClassificationTask`, and domain-group/folder/topic/analysis fields to `AiClassificationResult`.

**Step 3: Run the focused test and compile.**

```bash
./mvnw spotless:apply
./mvnw -Dtest=ReclassificationConstantsTest test
./mvnw -DskipTests compile
```

Expected: PASS and `BUILD SUCCESS`.

**Step 4: Commit.**

```bash
git add src/main/java src/test/java
git commit -m "feat(ai): model durable reclassification state"
```

---

### Task 4: Add public-suffix-aware registrable-domain resolution

**Objective:** Correctly bucket `docs.github.com`, `gist.github.com`, and `github.com` together without incorrectly reducing domains such as `example.com.cn`.

**Files:**
- Modify: `pom.xml`
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/service/RegistrableDomainResolver.java`
- Test: `src/test/java/wo1261931780/testBookMarkAnalysis/service/RegistrableDomainResolverTest.java`

**Step 1: Add failing resolver tests.**

```java
@Test
void reducesSubdomainsToTheSameRegistrableDomain() {
    assertEquals("github.com", resolver.resolve("https://docs.github.com/en/rest"));
    assertEquals("github.com", resolver.resolve("https://gist.github.com/user/1"));
    assertEquals("github.com", resolver.resolve("https://github.com/openai"));
}

@Test
void preservesPublicSuffixAwareChineseDomains() {
    assertEquals("example.com.cn", resolver.resolve("https://a.b.example.com.cn/path"));
}

@Test
void fallsBackToNormalizedHostForIpAndInvalidUrls() {
    assertEquals("127.0.0.1", resolver.resolve("http://127.0.0.1:8080"));
    assertNull(resolver.resolve("not a valid url"));
}
```

**Step 2: Add Guava and implement the resolver.**

Add a pinned dependency:

```xml
<dependency>
  <groupId>com.google.guava</groupId>
  <artifactId>guava</artifactId>
  <version>33.4.8-jre</version>
</dependency>
```

Implement resolution as follows:

```java
String host = DomainCategoryMapper.extractDomain(url);
if (host == null) return null;
if (InetAddresses.isInetAddress(host)) return host;
try {
    InternetDomainName domain = InternetDomainName.from(host);
    return domain.hasPublicSuffix() && domain.isUnderPublicSuffix()
            ? domain.topPrivateDomain().toString()
            : host;
} catch (IllegalArgumentException ignored) {
    return host;
}
```

Use Guava `InetAddresses` to avoid treating IP literals as registrable domains. Keep the resolver separate from the legacy `DomainCategoryMapper` so existing rule-based features are not accidentally changed.

**Step 3: Verify.**

```bash
./mvnw spotless:apply
./mvnw -Dtest=RegistrableDomainResolverTest test
```

Expected: all three tests pass.

**Step 4: Commit.**

```bash
git add pom.xml src/main/java/wo1261931780/testBookMarkAnalysis/service/RegistrableDomainResolver.java src/test/java/wo1261931780/testBookMarkAnalysis/service/RegistrableDomainResolverTest.java
git commit -m "feat(ai): resolve registrable bookmark domains"
```

---

### Task 5: Build the transactional start/rebuild preparation service

**Objective:** On “start,” persist a fixed bookmark snapshot, group it by registrable domain, and immediately clear all existing folders without losing links.

**Files:**
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/service/BookmarkReclassificationPreparationService.java`
- Modify: `src/main/java/wo1261931780/testBookMarkAnalysis/service/BookMarksService.java`
- Modify: `src/main/java/wo1261931780/testBookMarkAnalysis/mapper/BookMarksMapper.java`
- Modify: `src/main/resources/wo1261931780/testBookMarkAnalysis/mapper/BookMarksMapper.xml`
- Test: `src/test/java/wo1261931780/testBookMarkAnalysis/service/BookmarkReclassificationPreparationServiceTest.java`

**Step 1: Write failing preparation tests with mocked services/mappers.**

Cover exactly these cases:

1. A snapshot is created before any destructive database call.
2. Every link is detached using a set-based update; all `h3` folders are deleted.
3. `docs.github.com`, `gist.github.com`, and `github.com` form one `LARGE_DOMAIN` group when total count is 5.
4. A 4-bookmark domain is assigned `SMALL_POOL`, and it does not create a large-group folder candidate.
5. `treeClearedAt` is set only after detach/delete succeeds.

**Step 2: Add dedicated bookmark mapper operations.**

Avoid loading and individually updating every bookmark for the destructive step. Add mapper methods and XML statements equivalent to:

```xml
<update id="clearParentIdsForLinks">
  UPDATE book_marks SET parent_id = NULL WHERE type = 'a'
</update>

<delete id="deleteAllFolders">
  DELETE FROM book_marks WHERE type = 'h3'
</delete>
```

The flow does not use current folders as input. Snapshot only `type = 'a'` links, ordered stably by ID, and write `ordinal` in snapshot order.

**Step 3: Implement a single transactional `prepareNewTask(...)`.**

The public return type should provide the created task ID and counts, e.g.:

```java
public record PreparedTask(Long taskId, int totalBookmarks, int largeGroupCount, int smallPoolCount) {}
```

Within one `@Transactional` method:

1. insert task with `QUEUED` / `PREPARING`;
2. read current links once;
3. bulk-insert snapshots with the resolver output;
4. create one group row per non-null registrable domain; group invalid/no-domain URLs into the small pool with a deterministic pseudo-domain such as `__unknown__`;
5. mark each snapshot `LARGE_DOMAIN` or `SMALL_POOL` from the 5-bookmark threshold;
6. call `clearParentIdsForLinks()` and `deleteAllFolders()`;
7. set `treeClearedAt`, initialize phase `LARGE_DOMAINS`, and create the first persisted work units in a later task.

Do not submit an executor job in this preparation service.

**Step 4: Run focused tests.**

```bash
./mvnw spotless:apply
./mvnw -Dtest=BookmarkReclassificationPreparationServiceTest test
```

Expected: test proves old folders are removed only after snapshot persistence logic succeeds.

**Step 5: Commit.**

```bash
git add src/main/java/wo1261931780/testBookMarkAnalysis/service/BookmarkReclassificationPreparationService.java src/main/java/wo1261931780/testBookMarkAnalysis/service/BookMarksService.java src/main/java/wo1261931780/testBookMarkAnalysis/mapper/BookMarksMapper.java src/main/resources/wo1261931780/testBookMarkAnalysis/mapper/BookMarksMapper.xml src/test/java/wo1261931780/testBookMarkAnalysis/service/BookmarkReclassificationPreparationServiceTest.java
git commit -m "feat(ai): persist rebuild snapshot and clear folder tree"
```

---

### Task 6: Create deterministic persisted work units

**Objective:** Convert the snapshot/groups into idempotent, database-addressable units before any AI call begins.

**Files:**
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationWorkUnitPlanner.java`
- Test: `src/test/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationWorkUnitPlannerTest.java`

**Step 1: Write failing planner tests.**

Test that the planner creates:

- one `LARGE_DOMAIN_FOLDER_NAME` unit per large group;
- `LARGE_DOMAIN_BOOKMARK_ANALYSIS` units in stable 25-bookmark chunks after folder naming is known;
- `SMALL_POOL_BOOKMARK_ANALYSIS` units in stable 25-bookmark chunks;
- unique `unitKey` values such as `large-folder:<groupId>`, `large-analysis:<groupId>:0001`, `small-analysis:0001`;
- no duplicate rows when planning is invoked after resume.

**Step 2: Implement planning in phases.**

Do not pre-create large-domain title-analysis units until that group has a saved folder name. This keeps each unit input self-contained and stable.

Use JSON snapshots shaped like:

```json
{
  "groupId": "123",
  "registrableDomain": "github.com",
  "bookmarks": [
    {"bookmarkId":"1","url":"https://github.com/...","originalTitle":"..."}
  ]
}
```

Persist `promptVersion` (for example `reclassify-v1`) and `inputJson` at unit creation time. Build units with `PENDING`, not `RUNNING`.

**Step 3: Verify.**

```bash
./mvnw spotless:apply
./mvnw -Dtest=ReclassificationWorkUnitPlannerTest test
```

Expected: all unit-key and batching assertions pass.

**Step 4: Commit.**

```bash
git add src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationWorkUnitPlanner.java src/test/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationWorkUnitPlannerTest.java
git commit -m "feat(ai): plan durable reclassification work units"
```

---

### Task 7: Preserve auditable AI replies and add strict typed parsing

**Objective:** Return raw AI response content for durable storage and reject malformed output before any result is marked successful.

**Files:**
- Modify: `src/main/java/wo1261931780/testBookMarkAnalysis/service/AiClientService.java`
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationAiService.java`
- Test: `src/test/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationAiServiceTest.java`
- Test: `src/test/java/wo1261931780/testBookMarkAnalysis/service/AiClientServiceTest.java`

**Step 1: Write failing tests.**

Verify that:

1. an audited client reply exposes `requestJson`, the raw model `content`, and parsed JSON;
2. an AI result with a bookmark ID outside the unit snapshot is rejected;
3. duplicate bookmark IDs, missing required `suggestedTitle`/`topicHint` fields, or invalid logical-folder keys are rejected;
4. valid large-domain folder naming returns exactly one nonblank `folderName` for the group;
5. prompt text and raw AI content contain no API key.

**Step 2: Add an audited client method while preserving legacy callers.**

Keep existing `chat(...)` as a compatibility wrapper. Add a record and method such as:

```java
public record AiJsonReply(String requestJson, String rawContent, JSONArray array) {}

public AiJsonReply chatForJsonArray(
        String systemPrompt, String userContent, double temperature,
        String apiBaseUrl, String apiKey, String modelName) throws Exception
```

`rawContent` is the model message content after HTTP response parsing but before removing Markdown fences. `requestJson` is the outgoing JSON body and must not include the Authorization header. Continue the existing timeout/retry behavior.

**Step 3: Implement phase-specific prompts and parsers.**

`ReclassificationAiService` must own prompt construction and parse-only methods for:

- `LARGE_DOMAIN_FOLDER_NAME`: output `{ "folderName": "...", "reason": "..." }`;
- bookmark analysis: one entry per input ID with `bookmarkId`, `suggestedTitle`, `keywords`, `pageType`, `topicHint`, `confidence`, and `reason`;
- small-pool cluster draft: stable provisional `logicalFolderKey`, `folderName`, and per-bookmark mapping;
- small-pool canonicalization: maps provisional keys to canonical `logicalFolderKey`/`folderName` only.

Use logical folder keys generated/validated by the service (e.g. `small:<slug>`), not arbitrary unsafe AI text. Folder display names may be AI text after trim and length validation.

**Step 4: Run tests.**

```bash
./mvnw spotless:apply
./mvnw -Dtest=AiClientServiceTest,ReclassificationAiServiceTest test
```

Expected: valid responses parse; malformed/mismatched responses fail with a domain exception that the orchestrator will convert to `RECOVERABLE`.

**Step 5: Commit.**

```bash
git add src/main/java/wo1261931780/testBookMarkAnalysis/service/AiClientService.java src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationAiService.java src/test/java/wo1261931780/testBookMarkAnalysis/service/AiClientServiceTest.java src/test/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationAiServiceTest.java
git commit -m "feat(ai): audit and validate reclassification responses"
```

---

### Task 8: Persist large-domain AI results and apply each completed group idempotently

**Objective:** Large groups of five or more bookmarks get one AI-named folder, with titles and moves committed exactly once.

**Files:**
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationResultPersistenceService.java`
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationApplicationService.java`
- Test: `src/test/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationApplicationServiceTest.java`

**Step 1: Write failing idempotency tests.**

Cover:

1. persisting the same `(taskId, bookmarkId)` updates/retains one result because of `uk_task_bookmark`;
2. applying a large group creates only one folder application row for `(taskId, logicalFolderKey)`;
3. a second apply call reuses the recorded folder ID rather than creating another folder;
4. titles only update when nonblank; each group bookmark gets the recorded folder `parentId`;
5. applied result rows are marked `APPLIED` only after bookmark update succeeds.

**Step 2: Implement result persistence.**

For a successful unit, in one transaction:

1. set the work unit `SUCCEEDED`, raw output, parsed output, and completed time;
2. upsert each `AiClassificationResult` with task/group/logical folder fields and `PENDING` application state;
3. update the domain group’s folder name after the folder-naming unit succeeds;
4. create the next group analysis units only after folder naming is durable.

**Step 3: Implement application.**

Use a unique logical key such as `large:<domainGroupId>` and `bookmark_ai_reclassification_folder_application` as the idempotency guard.

```java
@Transactional
public ApplicationStats applyFolder(Long taskId, String logicalFolderKey, String folderName) {
    // find or insert application row; create the h3 folder only if folderId is null
    // update all PENDING results with this logical key
    // mark those result rows APPLIED and update task aggregate counts
}
```

Do not consult legacy folders. The start transaction already removed all `h3` rows.

**Step 4: Verify.**

```bash
./mvnw spotless:apply
./mvnw -Dtest=ReclassificationApplicationServiceTest test
```

Expected: tests prove repeated application is side-effect-free.

**Step 5: Commit.**

```bash
git add src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationResultPersistenceService.java src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationApplicationService.java src/test/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationApplicationServiceTest.java
git commit -m "feat(ai): persist and apply large-domain folders"
```

---

### Task 9: Implement the small-domain analysis, draft, and canonicalization pipeline

**Objective:** Avoid separate folders for domains with fewer than five bookmarks while still grouping every small-pool bookmark into stable cross-domain topic folders.

**Files:**
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/service/SmallPoolClusteringService.java`
- Modify: `src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationWorkUnitPlanner.java`
- Modify: `src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationResultPersistenceService.java`
- Test: `src/test/java/wo1261931780/testBookMarkAnalysis/service/SmallPoolClusteringServiceTest.java`

**Step 1: Write failing tests for both sizes.**

Test:

- a 30-item small pool creates two 25-item analysis units and one 30-item cluster draft (not a per-domain folder);
- a 160-item small pool creates two 80-item draft units, then one canonicalization unit over the provisional folder list;
- canonicalization rewrites all draft mappings to a single canonical key when AI says two draft labels are the same topic;
- no result remains without `logicalFolderKey` before small-pool application begins;
- all bookmarks from a 4-item `github.io` group may end in the same topic folder as other domains, but no `large:<groupId>` application exists for it.

**Step 2: Implement three persisted small-pool phases.**

1. **Analysis:** 25-bookmark batches persist per-bookmark title/keyword/page-type/topic-hint results only.
2. **Draft clusters:** one or more 80-record batches consume compact saved analysis fields and produce provisional folder mappings. A draft’s logical keys are namespaced, e.g. `draft:<unitId>:frontend`.
3. **Canonicalization:** once every draft succeeds, create exactly one unit with the unique provisional keys and folder names. It returns stable keys such as `small:frontend-development` and canonical display names. Rewrite every affected result from provisional to canonical key in one transaction.

For a small pool with no bookmarks, skip all three phases and advance task phase directly to `APPLYING`.

**Step 3: Apply canonical small folders.**

After canonicalization, group result rows by canonical logical key, create/reuse each recorded folder application, and auto-apply the group. Do not expose a user confirmation step.

**Step 4: Verify.**

```bash
./mvnw spotless:apply
./mvnw -Dtest=SmallPoolClusteringServiceTest test
```

Expected: both single-draft and multi-draft/canonicalization scenarios pass.

**Step 5: Commit.**

```bash
git add src/main/java/wo1261931780/testBookMarkAnalysis/service/SmallPoolClusteringService.java src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationWorkUnitPlanner.java src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationResultPersistenceService.java src/test/java/wo1261931780/testBookMarkAnalysis/service/SmallPoolClusteringServiceTest.java
git commit -m "feat(ai): cluster small-domain bookmarks by topic"
```

---

### Task 10: Replace in-memory orchestration with a database-driven pause/resume worker

**Objective:** Execute only persisted work units and make manual pause, AI failure, and process restart recover without duplicate work.

**Files:**
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/service/ResumableReclassificationTaskService.java`
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationRecoveryService.java`
- Modify: `src/main/java/wo1261931780/testBookMarkAnalysis/mapper/AiClassificationTaskMapper.java`
- Modify: `src/main/java/wo1261931780/testBookMarkAnalysis/mapper/AiReclassificationWorkUnitMapper.java`
- Test: `src/test/java/wo1261931780/testBookMarkAnalysis/service/ResumableReclassificationTaskServiceTest.java`
- Test: `src/test/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationRecoveryServiceTest.java`

**Step 1: Write failing state-transition tests.**

Cover these exact sequences:

```text
start -> RUNNING -> PAUSED -> RUNNING -> COMPLETED
RUNNING + AI failure -> RECOVERABLE -> RUNNING -> COMPLETED
startup: RUNNING task + RUNNING unit -> RECOVERABLE task + RETRYABLE_FAILED unit
resume after a SUCCEEDED unit -> only PENDING/RETRYABLE_FAILED unit is claimed
second start while one task is RUNNING -> rejected
```

Also verify pause at an AI boundary: when the pause flag is set while one unit finishes, its complete result persists, then no second unit is claimed.

**Step 2: Add mapper methods for conditional transitions and unit claim.**

Use database conditional updates, not a `ConcurrentHashMap`. Add custom mapper/XML methods along these lines:

```java
int markTaskRunningIfResumable(@Param("taskId") Long taskId);
int markTaskPausedIfRunning(@Param("taskId") Long taskId);
int markRunningTasksRecoverable();
int markRunningUnitsRetryableFailed();
AiReclassificationWorkUnit selectNextClaimableUnitForUpdate(@Param("taskId") Long taskId);
int claimUnit(@Param("unitId") Long unitId, @Param("startedAt") LocalDateTime startedAt);
boolean existsRunningTask();
```

`selectNextClaimableUnitForUpdate` must order by `ordinal`, select only `PENDING`/`RETRYABLE_FAILED`, and run within the same transaction as `claimUnit`. Do not use `SKIP LOCKED` unless the deployment MySQL version and test environment are verified to support it.

**Step 3: Implement the orchestration loop.**

The service must:

1. start preparation and planning, transition the task to `RUNNING`, then submit a background runnable;
2. on each loop iteration reload task status from DB;
3. exit immediately when `PAUSED`, `RECOVERABLE`, or terminal;
4. claim one unit transactionally;
5. invoke the correct phase service;
6. persist result, schedule successor units, apply completed folders, and update task counts;
7. on AI/network/parse exception, set the unit `RETRYABLE_FAILED`, task `RECOVERABLE`, persist error text, and stop the loop;
8. mark `COMPLETED` only after no uncompleted units remain and all persisted results are `APPLIED`.

Use a named single-thread executor with `@PreDestroy` shutdown. The executor is an execution mechanism only; no task/result state belongs in memory.

**Step 4: Implement explicit pause/resume methods.**

```java
public TaskSummary pause(Long taskId);
public TaskSummary resume(Long taskId);
public List<TaskSummary> listRecoverable();
public TaskSummary getTask(Long taskId);
```

`resume` accepts only `PAUSED` or `RECOVERABLE`, increments `recoveryCount` only for `RECOVERABLE`, sets `resumedAt`, and submits the loop. It must not recreate snapshot/group/unit/result rows.

**Step 5: Implement restart recovery.**

At `ApplicationReadyEvent`, execute database updates that turn stale `RUNNING` task rows into `RECOVERABLE` and `RUNNING` work units into `RETRYABLE_FAILED`. Do not call `resume` automatically.

**Step 6: Verify.**

```bash
./mvnw spotless:apply
./mvnw -Dtest=ResumableReclassificationTaskServiceTest,ReclassificationRecoveryServiceTest test
```

Expected: state sequences pass without a live database or live AI call because mappers/AI phase services are mocked.

**Step 7: Commit.**

```bash
git add src/main/java/wo1261931780/testBookMarkAnalysis/service/ResumableReclassificationTaskService.java src/main/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationRecoveryService.java src/main/java/wo1261931780/testBookMarkAnalysis/mapper/AiClassificationTaskMapper.java src/main/java/wo1261931780/testBookMarkAnalysis/mapper/AiReclassificationWorkUnitMapper.java src/main/resources/wo1261931780/testBookMarkAnalysis/mapper src/test/java/wo1261931780/testBookMarkAnalysis/service/ResumableReclassificationTaskServiceTest.java src/test/java/wo1261931780/testBookMarkAnalysis/service/ReclassificationRecoveryServiceTest.java
git commit -m "feat(ai): add durable pause and resume orchestration"
```

---

### Task 11: Add rebuild-specific REST APIs and retire duplicate manual-apply usage

**Objective:** Expose durable task status/control endpoints that the toolbox can use without memory-backed task IDs or a user “apply” click.

**Files:**
- Modify: `src/main/java/wo1261931780/testBookMarkAnalysis/controller/ShowMeListController.java:430-518`
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/controller/dto/ReclassificationStartRequest.java`
- Create: `src/main/java/wo1261931780/testBookMarkAnalysis/controller/dto/ReclassificationTaskResponse.java`
- Test: `src/test/java/wo1261931780/testBookMarkAnalysis/controller/ShowMeListControllerTest.java`

**Step 1: Write failing MVC tests for the new contract.**

Test:

```text
POST /BookMarks/toolbox/reclassification/start  -> 200 + taskId/status/phase/treeClearedAt
POST /BookMarks/toolbox/reclassification/task/{id}/pause -> 200 + PAUSED
POST /BookMarks/toolbox/reclassification/task/{id}/resume -> 200 + RUNNING
GET  /BookMarks/toolbox/reclassification/task/{id} -> durable counters
GET  /BookMarks/toolbox/reclassification/recoverable -> PAUSED and RECOVERABLE task list
```

Also test a conflicting start returns a business error when another task is `RUNNING`, and a resume of `COMPLETED` returns a business error.

**Step 2: Implement typed request/response DTOs.**

The start request should intentionally contain no `strategy`, `useAI`, `bookmarkIds`, or `apiKey`:

```java
public record ReclassificationStartRequest(
        String apiBaseUrl,
        String modelName) {}
```

Blank optional values resolve from `BookmarkConfig`. The API key always comes from configuration and is never accepted from the browser in this new flow.

A response must expose all Snowflake IDs as `String`, including work/folder stats needed by Vue:

```java
public record ReclassificationTaskResponse(
        String taskId, String status, String phase, int totalBookmarks,
        int totalWorkUnits, int completedWorkUnits, int largeDomainGroups,
        int smallPoolBookmarks, int createdFolders, int movedBookmarks,
        int updatedTitles, int recoveryCount, String errorMessage) {}
```

**Step 3: Keep old endpoints isolated.**

Do not route the new frontend through `/toolbox/classify/task/{id}/apply` or `/toolbox/applyClassify`. Either remove the old manual-apply endpoints only after a repository-wide call-site search proves they are unused, or retain them as legacy APIs with explicit `@Deprecated` documentation. They must never be invoked by the new reclassification service.

**Step 4: Verify.**

```bash
./mvnw spotless:apply
./mvnw -Dtest=ShowMeListControllerTest test
```

Expected: new routes return the typed durable task payload and legacy behavior is unaffected.

**Step 5: Commit.**

```bash
git add src/main/java/wo1261931780/testBookMarkAnalysis/controller/ShowMeListController.java src/main/java/wo1261931780/testBookMarkAnalysis/controller/dto src/test/java/wo1261931780/testBookMarkAnalysis/controller/ShowMeListControllerTest.java
git commit -m "feat(api): expose resumable reclassification controls"
```

---

### Task 12: Replace the toolbox classification UI with rebuild controls

**Objective:** Make the user-facing flow match the confirmed behavior: start immediately clears folders, running tasks can pause, and only database-backed paused/recoverable tasks can resume.

**Files (frontend repository `../test-BookMarkAnalysisVue3`):**
- Modify: `pages/toolbox.vue`
- Optionally create: `types/reclassification.ts` if interfaces become too large for the page.

**Step 1: Replace the existing strategy/AI toggle start panel.**

Delete the `strategies`, `selectedStrategy`, `useAI`, preview result list, and `applyClassify()` path. Replace with copy that is precise but does not add a confirmation modal:

```text
开始重新分类会立即清空现有文件夹结构；书签链接会保留。
系统先按主域名整理 5 条及以上的书签，再用 AI 将零散书签按主题归类。
```

The start button calls:

```ts
await bookmarkApi.post<ReclassificationTask>('/BookMarks/toolbox/reclassification/start', {})
```

**Step 2: Add durable task interfaces and status labels.**

Use a type like:

```ts
interface ReclassificationTask {
  taskId: string
  status: 'QUEUED' | 'RUNNING' | 'PAUSED' | 'RECOVERABLE' | 'COMPLETED' | 'FAILED'
  phase: string
  totalBookmarks: number
  totalWorkUnits: number
  completedWorkUnits: number
  largeDomainGroups: number
  smallPoolBookmarks: number
  createdFolders: number
  movedBookmarks: number
  updatedTitles: number
  recoveryCount: number
  errorMessage?: string
}
```

Progress is `completedWorkUnits / totalWorkUnits`, with a zero-denominator guard. Show phase/status text for preparing, large-domain analysis, small-pool analysis, clustering, applying, paused, and recoverable states.

**Step 3: Add pause and resume actions.**

- `RUNNING`: show **暂停** calling `POST /task/{id}/pause`.
- `PAUSED` or `RECOVERABLE`: show **继续** calling `POST /task/{id}/resume`.
- Poll every two seconds only for `QUEUED`/`RUNNING`; stop polling for `PAUSED`, `RECOVERABLE`, `COMPLETED`, and `FAILED`.
- On component mount, call `/reclassification/recoverable`, display all paused/recoverable tasks, and let the user select one to continue.
- Do not automatically resume after page reload or service restart.

**Step 4: Display automatic outcome rather than preview/apply.**

On `COMPLETED`, show the server counts:

```text
已创建 N 个文件夹
已移动 N 条书签
已更新 N 个标题
已恢复 N 次
```

There is no “确认应用” button because folder creation/moves/titles are already automatic and idempotent.

**Step 5: Verify frontend quality.**

Run from the frontend repository:

```bash
pnpm exec nuxi typecheck
pnpm build
```

Expected: typecheck and Nuxt production build succeed.

**Step 6: Commit in the frontend repository.**

```bash
git -C ../test-BookMarkAnalysisVue3 add pages/toolbox.vue types/reclassification.ts
git -C ../test-BookMarkAnalysisVue3 commit -m "feat(toolbox): control resumable bookmark rebuilds"
```

Omit `types/reclassification.ts` from `git add` if interfaces remained inline.

---

### Task 13: Remove obsolete in-memory behavior and validate all backend tests

**Objective:** Ensure the new implementation has no hidden `ConcurrentHashMap`/one-hour cache dependency and no duplicate automatic/manual application path.

**Files:**
- Modify: `src/main/java/wo1261931780/testBookMarkAnalysis/service/SmartClassificationTaskService.java` or delete it if no callers remain.
- Modify: `src/test/java/wo1261931780/testBookMarkAnalysis/service/SmartClassificationTaskServiceTest.java` or delete it if the legacy service is removed.
- Modify: `src/main/java/wo1261931780/testBookMarkAnalysis/controller/ShowMeListController.java` if old endpoints are now proven unused.
- Modify: `README.md` and/or `DEVELOPMENT_PLAN.md` with the new task behavior and migration command.

**Step 1: Search for old in-memory/task apply call sites.**

Run:

```bash
rg -n 'ConcurrentHashMap|TASK_RETENTION_MS|applyTaskResults|/toolbox/classify|/toolbox/applyClassify|SmartClassificationTaskService' src/main src/test ../test-BookMarkAnalysisVue3
```

Expected: no new-flow caller relies on `SmartClassificationTaskService`, its in-memory `Map`, or a manual `apply` endpoint.

**Step 2: Remove or isolate legacy code.**

Preferred outcome: remove `SmartClassificationTaskService` and its tests after all callers are moved. Keep `SmartClassificationService` only if the legacy synchronous classify/title tools still genuinely use it. If keeping legacy code, document it as separate from reclassification and ensure it cannot manipulate an active rebuild task.

**Step 3: Document operations.**

Document:

1. first-time/fresh Docker initialization uses the updated init SQL;
2. existing MySQL needs `sql/migrations/20260719_resumable_ai_reclassification.sql` applied once;
3. a task interrupted by restart displays as recoverable and never auto-resumes;
4. a user click on “开始重新分类” immediately removes old folders.

**Step 4: Run formatting, all backend tests, and package build.**

```bash
./mvnw spotless:apply
./mvnw test
./mvnw -DskipTests package
```

Expected: all tests pass and the executable JAR packages successfully.

**Step 5: Commit.**

```bash
git add src/main/java src/test/java README.md DEVELOPMENT_PLAN.md
git commit -m "refactor(ai): retire in-memory classification workflow"
```

Only add documentation files that actually changed. Never add `src/main/resources/application.yml` unless its pre-existing change is intentionally reviewed in a separate commit.

---

### Task 14: Perform an end-to-end recovery smoke test against MySQL with a fake AI client

**Objective:** Verify the key acceptance criteria with a real database transaction boundary, without consuming a real AI API call.

**Files:**
- Create: `src/test/java/wo1261931780/testBookMarkAnalysis/service/ResumableReclassificationIntegrationTest.java`
- Optionally modify: `src/test/resources/application-test.yml` if a test profile does not already exist.

**Step 1: Add a Spring integration test configuration.**

Use Testcontainers MySQL if adding it is acceptable to the repository; otherwise gate this test behind an explicit local MySQL profile and document its command. Prefer Testcontainers for repeatability, with an `@TestConfiguration` fake `AiClientService`/`ReclassificationAiService` that returns deterministic valid output.

**Step 2: Implement this scenario.**

1. Insert existing `h3` folders and links, including 5 combined GitHub subdomain links and multiple `<5` domain links.
2. Start rebuild; assert old `h3` records are gone immediately and links remain.
3. Let exactly one work unit succeed; request pause; wait for `PAUSED`.
4. Instantiate/run recovery handling that converts an in-flight task/unit state into recoverable; assert no automatic executor action occurs.
5. Resume; assert only the remaining work units call the fake AI service.
6. Wait for `COMPLETED`; assert one GitHub AI-named folder, topic folders for small domains, no duplicate folder application keys, and all result rows `APPLIED`.

**Step 3: Run the integration test and full test suite.**

```bash
./mvnw -Dtest=ResumableReclassificationIntegrationTest test
./mvnw test
```

Expected: both pass; the integration test proves immediate tree clearing, pause/recovery, no duplicate AI work, threshold behavior, and automatic application.

**Step 4: Commit.**

```bash
git add src/test/java/wo1261931780/testBookMarkAnalysis/service/ResumableReclassificationIntegrationTest.java src/test/resources/application-test.yml pom.xml
git commit -m "test(ai): cover resumable rebuild recovery flow"
```

Only include the optional config/dependency files if they changed.

---

## Final acceptance checklist

Run this checklist before declaring the feature done:

```bash
# Backend repo
./mvnw spotless:apply
./mvnw test
./mvnw -DskipTests package
git status --short

# Frontend repo
cd ../test-BookMarkAnalysisVue3
pnpm exec nuxi typecheck
pnpm build
git status --short
```

Manual smoke test using the toolbox:

1. Start a rebuild with folders already present; confirm the old folders disappear immediately while links remain.
2. Verify GitHub subdomains form one group and a five-item group gets one AI-named folder.
3. Verify a four-item domain does not get its own domain folder and can merge with other small-domain bookmarks by topic.
4. Press pause during processing; refresh the page and confirm the task remains listed as paused.
5. Restart the backend during a work unit; confirm its task is listed as recoverable rather than auto-running.
6. Press continue and confirm prior persisted results are reused, unfinished/failed work resumes, and the task auto-applies all results at `COMPLETED`.
7. Verify there is no manual “确认应用” control in the new rebuild UI.
8. Verify no secrets/API key appear in any task, work-unit, result, exception response, or log output.
