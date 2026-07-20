# 书签解析项目 - 开发优化计划

> 文档创建时间：2026年1月4日
> 项目名称：test-BookMarkAnalysis
> 当前状态：**第四阶段优化完成** ✅

---

## 一、项目概述

### 1.1 项目功能

这是一个浏览器书签解析项目，主要功能包括：

- 解析浏览器导出的 HTML 书签文件（Chrome/Firefox/Edge）和 Safari plist 文件
- 提取书签链接（`<A>` 标签）和文件夹（`<H3>` 标签）
- 将解析结果存储到 MySQL 数据库
- 提供书签去重、导出、搜索、仪表盘统计、AI 分类、资源管理等功能
- 通过 REST API 提供服务

### 1.2 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.4.13 | Web 框架 |
| MyBatis-Plus | 3.5.9 | ORM 框架 |
| MySQL | 8.x | 数据库 |
| Hutool | 5.8.34 | 工具库 |
| springdoc-openapi | 2.7.0 | API 文档 |
| Lombok | 1.18.46 | 代码简化 |
| Jsoup | 1.18.3 | HTML 解析 |
| dd-plist | 1.28 | Safari plist 解析 |
| Caffeine | - | 本地缓存 |

---

## 二、依赖升级记录 ✅

### 2.1 升级详情

| 依赖 | 旧版本 | 新版本 | 备注 |
|------|--------|--------|------|
| Spring Boot Parent | 3.1.6 | **3.4.13** | 统一版本管理 |
| spring-boot-starter | 3.2.0（独立指定） | 由 parent 管理 | 移除独立版本号 |
| spring-boot-starter-web | 3.2.0（独立指定） | 由 parent 管理 | 移除独立版本号 |
| spring-boot-starter-jdbc | 3.2.0（独立指定） | 由 parent 管理 | 移除独立版本号 |
| MyBatis-Plus | mybatis-plus-boot-starter 3.5.4.1 | **mybatis-plus-spring-boot3-starter 3.5.9** | Spring Boot 3 专用版 |
| MyBatis-Plus Generator | 3.5.4 | **3.5.9** | 代码生成器（已注释） |
| Dynamic Datasource | dynamic-datasource-spring-boot-starter 4.2.0 | **dynamic-datasource-spring-boot3-starter 4.3.1** | 多数据源（已注释） |
| Lombok | 1.18.30 | **1.18.46** | 最新稳定版 |
| Hutool | 5.8.23 | **5.8.34** | 最新稳定版 |
| MySQL Connector | mysql:mysql-connector-java 8.0.33 | **com.mysql:mysql-connector-j** | 新 GAV 坐标 |
| Swagger | springfox-swagger2 3.0.0 | **springdoc-openapi-starter-webmvc-ui 2.7.0** | Spring Boot 3 兼容 |

### 2.2 解决的冲突

1. **版本不一致问题**
   - 问题：parent 是 3.1.6，但 starter 是 3.2.0
   - 解决：统一升级到 3.4.13，由 parent 管理版本

2. **Swagger 不兼容 Spring Boot 3**
   - 问题：springfox 不支持 Spring Boot 3
   - 解决：迁移到 springdoc-openapi，更新所有注解

3. **MyBatis-Plus 分页插件找不到类**
   - 问题：`PaginationInnerInterceptor` 类找不到
   - 解决：添加 `mybatis-plus-jsqlparser` 依赖，指定 `DbType.MYSQL`

4. **insertOrUpdate 方法冲突**
   - 问题：自定义方法与 BaseMapper 冲突（返回类型不同）
   - 解决：重命名为 `customInsertOrUpdate`

5. **MySQL 驱动坐标过时**
   - 问题：`mysql:mysql-connector-java` 已废弃
   - 解决：更新为 `com.mysql:mysql-connector-j`

---

## 三、现存问题分析

### 3.1 高优先级问题 🔴

| 序号 | 问题 | 位置 | 影响 | 状态 |
|------|------|------|------|------|
| 1 | 硬编码 Windows 文件路径 | `BookmarksParserServiceImpl.java` | 跨平台兼容性 | ✅ 已解决 |
| 2 | 硬编码输出文件路径 | `ShowMeListController.java` | 跨平台兼容性 | ✅ 已解决 |
| 3 | 包结构混乱 | `BookMarks2Service` 在 controller 包 | 代码可维护性 | ✅ 已解决 |
| 4 | 重复实体类 | `BookMarks` 和 `BookMarks2` 几乎相同 | 代码冗余 | ✅ 已解决（抽取 BaseBookmark） |
| 5 | 异常处理简陋 | 仅使用 `e.printStackTrace()` | 生产环境不可用 | ✅ 已解决 |

### 3.2 中优先级问题 🟡

| 序号 | 问题 | 位置 | 影响 | 状态 |
|------|------|------|------|------|
| 1 | 正则表达式每次重新编译 | `BookmarksParserServiceImpl.java` | 性能损耗 | ✅ 已解决 |
| 2 | 排序实现冗余 | `selectByUrl` 方法中的 Comparator | 代码简洁性 | ✅ 已解决 |
| 3 | 无参数校验 | Controller 层 | 数据安全性 | ✅ 已解决 |
| 4 | 无事务管理 | 批量操作无 @Transactional | 数据一致性 | ✅ 已解决 |
| 5 | 未使用的依赖 | dynamic-datasource（实际单数据源） | 依赖臃肿 | ✅ 已解决 |

### 3.3 低优先级问题 🟢

| 序号 | 问题 | 位置 | 影响 |
|------|------|------|------|
| 1 | 无单元测试 | test 目录为空 | 代码质量保障 |
| 2 | 无统一响应处理 | ShowResult 手动包装 | 开发效率 |
| 3 | 命名不规范 | BookMarks 应为 Bookmark | 代码规范 |
| 4 | 静态常量定义在实体类 | `COL_*` 常量 | 代码设计 |

---

## 四、开发优化计划

### 第一阶段：代码重构（预计 1-2 天）

#### 4.1.1 消除硬编码路径 ✅ 已完成

**目标**：支持跨平台运行，路径可配置

**已实现**：

- 创建了 `BookmarkConfig.java` 配置类
- 在 `application.yml` 中添加了配置项
- `BookmarksParserServiceImpl.java` 使用 `ResourceLoader` 加载文件
- `ShowMeListController.java` 注入配置读取路径

```yaml
bookmark:
  input-path: classpath:wo1261931780/testBookMarkAnalysis/bookmarks/bookmarks.html
  output-path: ${user.home}/bookmark-output/result.txt
```

#### 4.1.2 重构包结构 ✅ 已完成

**目标**：符合 Spring Boot 最佳实践

**已实现**：

- [x] 移动 `BookMarks2Service.java` 到 service 包
- [x] 添加全局异常处理类
- [x] 添加自定义业务异常类
- [x] 创建 `BaseBookmark.java` 抽取公共字段

#### 4.1.3 优化正则解析性能 ✅ 已完成

**目标**：预编译 Pattern，提升解析效率

**已实现**：所有正则表达式已预编译为静态常量。

---

### 第二阶段：功能增强（预计 2-3 天）✅ 已完成

#### 4.2.1 添加文件上传接口 ✅ 已完成

**目标**：支持通过 API 上传书签文件

**已实现**：

- 在 `ShowMeListController` 中添加了 `POST /BookMarks/upload` 接口
- 创建了 `ParseResult` DTO 类返回解析结果
- 支持解析上传的 HTML 书签文件并保存到数据库
- 扩展支持 Jsoup 解析 `/upload/jsoup`、Safari plist `/upload/safari`、自动识别 `/upload/auto`

#### 4.2.2 支持多种浏览器书签格式 ✅ 已完成

| 浏览器 | 格式特征 | 支持状态 |
|--------|----------|----------|
| Chrome | Netscape Bookmark File Format | ✅ 已支持 |
| Firefox | Netscape Bookmark File Format | ✅ 已支持 |
| Edge | Netscape Bookmark File Format | ✅ 已支持 |
| Safari | plist 格式 | ✅ 已支持 |

#### 4.2.3 添加书签分析功能 ✅ 已完成

**目标**：提供书签统计和分析

**已实现**：

- 创建了 `BookmarkAnalysis` DTO 类
- 添加了 `GET /BookMarks/analyze` 接口
- 支持统计总数、链接数、文件夹数、重复 URL、域名分布、最早/最近添加时间等

```java
@Data
@Schema(description = "书签分析结果")
public class BookmarkAnalysis {
    private int totalCount;
    private int folderCount;
    private int linkCount;
    private int duplicateCount;
    private Map<String, Integer> domainDistribution;
    private List<String> duplicateUrls;
    private Long earliestAddDate;
    private Long latestAddDate;
}
```

#### 4.2.4 添加导出功能 ✅ 已完成

**目标**：支持多种格式导出

**已实现**：

- 添加了 `GET /BookMarks/export` 接口
- 支持 HTML、Markdown、JSON 三种格式

#### 4.2.5 新增搜索与仪表盘统计接口 ✅ 已完成

**目标**：支持前端检索和仪表盘展示

**已实现**：

- `GET /BookMarks/search?keyword=&page=&limit=` — 按关键词分页搜索书签标题和链接
- `GET /BookMarks/stats` — 返回仪表盘统计 `{ total, folders, tags, recent }`

---

### 第三阶段：工程化提升（预计 1-2 天）✅ 已完成

#### 4.3.1 添加全局异常处理 ✅ 已完成

**目标**：统一异常处理和错误响应

**已实现**：

- 创建了 `GlobalExceptionHandler.java`
- 创建了 `BusinessException.java`

#### 4.3.2 添加参数校验 ✅ 已完成

**目标**：确保接口入参合法

**已实现**：

- 添加了 `spring-boot-starter-validation` 依赖
- Controller 添加了 `@Validated` 注解
- 分页参数添加了 `@Min`、`@Max` 校验

#### 4.3.3 添加事务管理 ✅ 已完成

**目标**：保证批量操作的数据一致性

**已实现**：所有批量操作方法都添加了 `@Transactional` 注解。

#### 4.3.4 添加单元测试 ✅ 已完成

**目标**：覆盖核心业务逻辑

**已实现**：

- 创建了 `BookmarksParserServiceTest.java` - Service 层测试
- 创建了 `ShowMeListControllerTest.java` - Controller 集成测试

#### 4.3.5 完善 API 文档 ✅ 已完成

**目标**：提供完整的接口文档

**已实现**：

- 创建了 `OpenApiConfig.java` 配置类
- 所有 Controller 接口添加了 `@Operation`、`@Parameter`、`@Tag` 注解

---

### 第四阶段：可选优化（未来迭代）✅ 已完成

#### 4.4.1 使用 Jsoup 替代正则解析 ✅ 已完成

**目标**：更健壮的 HTML 解析

**已实现**：

- 添加 `jsoup 1.18.3` 依赖
- 创建 `BookmarkParser` 解析器接口
- 创建 `JsoupBookmarkParser` 实现类
- 在 Controller 中添加 `POST /BookMarks/upload/jsoup` 接口

#### 4.4.2 添加缓存机制 ✅ 已完成

**目标**：减少数据库查询，提升性能

**已实现**：

- 添加 `spring-boot-starter-cache` 和 `caffeine` 依赖
- 创建 `CacheConfig` 配置类
- 在 `analyzeBookmarks()`、`selectByUrl()` 方法添加 `@Cacheable` 注解
- 在数据变更方法添加 `@CacheEvict` 注解

#### 4.4.3 添加前端界面 ✅ 已完成

**目标**：提供可视化管理

**技术选型**：

- Nuxt 4.4.8 + Vue 3
- @nuxt/ui v4
- TypeScript
- pnpm

**已实现页面**：

- 仪表盘统计（`/dashboard`）
- 书签导入（`/import`）
- 高级资源管理器（`/manager`）
- 树状文件夹浏览（`/tree`）
- 全量数据检索（`/list`）
- 管家工具箱（`/toolbox`）

前端开发服务器默认端口 `3000`，通过 `composables/useBookmarkApi.ts` 调用后端 `/BookMarks/*` 接口。

#### 4.4.4 Docker 化部署 ✅ 已完成

**目标**：容器化部署

**已实现**：

- 创建多阶段构建 `Dockerfile`
- 创建 `docker-compose.yml`（MySQL + App + Redis 可选）
- 创建 `.env.example` 环境变量模板
- 创建 `.dockerignore` 忽略文件
- 创建 MySQL 初始化脚本
- 添加 `spring-boot-starter-actuator` 健康检查支持
- 更新 `application.yml` 支持环境变量配置

```yaml
services:
  mysql:      # MySQL 8.0 数据库
  app:        # Spring Boot 应用
  redis:      # Redis 缓存（可选，使用 --profile redis 启动）
```

**启动命令**：

```bash
# 构建并启动
docker-compose up -d --build

# 包含 Redis 缓存
docker-compose --profile redis up -d --build

# 查看日志
docker-compose logs -f app
```

---

## 五、里程碑计划

| 阶段 | 目标 | 预计工时 | 优先级 | 状态 |
|------|------|----------|--------|------|
| 第一阶段 | 代码重构 | 1-2 天 | P0 | ✅ 已完成 |
| 第二阶段 | 功能增强 | 2-3 天 | P1 | ✅ 已完成 |
| 第三阶段 | 工程化提升 | 1-2 天 | P1 | ✅ 已完成 |
| 第四阶段 | 可选优化 | 按需 | P2 | ✅ 已完成 |

---

## 六、已完成优化汇总

### 6.1 第一阶段完成项

| 序号 | 优化项 | 说明 |
|------|--------|------|
| 1 | 依赖升级 | Spring Boot 3.4.13，MyBatis-Plus 3.5.9，springdoc-openapi 2.7.0 |
| 2 | 消除硬编码路径 | 创建 `BookmarkConfig` 配置类 |
| 3 | 包结构重构 | 将 `BookMarks2Service` 移至 service 包 |
| 4 | 正则表达式优化 | 预编译所有 Pattern 为静态常量 |
| 5 | 实体类重构 | 创建 `BaseBookmark` 基类，抽取公共字段 |
| 6 | 全局异常处理 | 新增 `GlobalExceptionHandler` 和 `BusinessException` |
| 7 | 参数校验 | 添加 `@Validated`、`@Min`、`@Max` 注解 |
| 8 | 事务管理 | 所有批量操作添加 `@Transactional` |
| 9 | 移除未使用依赖 | 注释掉 `dynamic-datasource` 和 `mybatis-plus-generator` |

### 6.2 第二阶段完成项

| 序号 | 优化项 | 说明 |
|------|--------|------|
| 1 | 文件上传接口 | `POST /BookMarks/upload` 支持上传 HTML 书签文件并解析 |
| 2 | 多格式上传 | 新增 `/upload/jsoup`、`/upload/safari`、`/upload/auto` |
| 3 | 书签分析功能 | `GET /BookMarks/analyze` 统计分析 |
| 4 | 搜索与统计 | `GET /BookMarks/search`、`GET /BookMarks/stats` |
| 5 | 多格式导出 | `GET /BookMarks/export` 支持 HTML/Markdown/JSON |
| 6 | 链接检测 | `GET /BookMarks/checkLinks`、`POST /BookMarks/checkLinks/async` |
| 7 | OpenAPI 配置 | 创建 `OpenApiConfig.java` 完善 API 文档 |
| 8 | 单元测试 | 创建 Service 和 Controller 测试类 |

### 6.3 第三阶段完成项

| 序号 | 优化项 | 说明 |
|------|--------|------|
| 1 | 全局异常处理 | 统一错误响应 |
| 2 | 参数校验 | Controller 层入参校验 |
| 3 | 事务管理 | 批量操作事务化 |
| 4 | API 文档 | 接口注解完善 |

### 6.4 第四阶段完成项

| 序号 | 优化项 | 说明 |
|------|--------|------|
| 1 | Jsoup 解析器 | 创建 `BookmarkParser` 接口和 `JsoupBookmarkParser` 实现 |
| 2 | Caffeine 缓存 | 添加本地缓存配置 |
| 3 | Docker 化部署 | 多阶段构建 Dockerfile + docker-compose 编排 |
| 4 | 前端界面 | Nuxt 4.4.8 + Vue 3 + @nuxt/ui v4 管理界面 |
| 5 | Actuator 健康检查 | 添加 `/actuator/health` 端点支持 |
| 6 | 环境变量配置 | `application.yml` 支持环境变量覆盖 |

### 6.5 新增/修改文件清单

**第一阶段新增文件：**

- `src/main/java/.../config/BookmarkConfig.java`
- `src/main/java/.../common/exception/GlobalExceptionHandler.java`
- `src/main/java/.../common/exception/BusinessException.java`
- `src/main/java/.../entity/BaseBookmark.java`
- `DEVELOPMENT_PLAN.md`

**第二阶段新增文件：**

- `src/main/java/.../config/OpenApiConfig.java`
- `src/main/java/.../entity/BookmarkAnalysis.java`
- `src/main/java/.../entity/ParseResult.java`
- `src/main/java/.../entity/LinkCheckResult.java`
- `src/main/java/.../entity/LinkCheckReport.java`
- `src/main/java/.../service/LinkCheckService.java`
- `src/main/java/.../service/impl/LinkCheckServiceImpl.java`
- `src/test/java/.../service/BookmarksParserServiceTest.java`
- `src/test/java/.../controller/ShowMeListControllerTest.java`

**第四阶段新增文件：**

- `src/main/java/.../parser/BookmarkParser.java`
- `src/main/java/.../parser/impl/JsoupBookmarkParser.java`
- `src/main/java/.../parser/impl/SafariBookmarkParser.java`
- `src/main/java/.../config/CacheConfig.java`
- `Dockerfile`
- `docker-compose.yml`
- `.env.example`
- `.dockerignore`
- `docker/mysql/init/01-init.sql`

**修改文件：**

- `pom.xml` - 依赖升级和清理，添加 dd-plist 依赖
- `application.yml` - 添加 bookmark 配置、端口 8000
- `BookMarksService.java` - 事务管理
- `BookMarks2Service.java` - 事务管理 + 移动包
- `BookmarksParserService.java` - 新增接口方法
- `BookmarksParserServiceImpl.java` - 性能优化 + 新功能实现
- `ShowMeListController.java` - 参数校验 + Swagger 注解 + 新接口
- `MybatisPlusConfig.java` - 添加 DbType.MYSQL
- `BookMarks.java`、`BookMarks2.java` - 继承 BaseBookmark
- `BookMarksMapper.java`、`BookMarks2Mapper.java` - 方法重命名
- `BookMarksMapper.xml`、`BookMarks2Mapper.xml` - 方法 ID 更新

---

## 七、注意事项

1. **数据库兼容性**
   - 升级后需要测试现有数据的兼容性
   - 建议在测试环境先行验证
   - 数据库名称为 `bookmarks`

2. **API 兼容性**
   - 所有接口根路径为 `/BookMarks`，无 `/api` 前缀
   - Swagger 访问地址：`http://localhost:8000/swagger-ui.html`
   - Docker 环境 Swagger 访问地址：`http://localhost:8080/swagger-ui.html`
   - 如有前端对接，需同步更新 `baseURL` 和接口路径

3. **端口说明**
   - 本地开发后端端口：`8000`（`application.yml`）
   - Docker 后端端口：`8080`（`docker-compose.yml`）
   - 前端开发服务器端口：`3000`

4. **回滚方案**
   - 保留原有 `pom.xml` 备份
   - 使用 Git 标签标记升级前版本

---

## 八、参考资料

- [Spring Boot 3.4 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.4-Release-Notes)
- [MyBatis-Plus 官方文档](https://baomidou.com/guides/new-code-generator/)
- [springdoc-openapi 迁移指南](https://springdoc.org/v2/)
- [Jsoup 官方文档](https://jsoup.org/)

---

*文档版本：v1.6*
*最后更新：2026年6月*
*更新内容：同步 API 路径为 /BookMarks，补充 /stats、/search、前端集成与 AGPL-3.0 说明*

---

## 八点五、可恢复 AI 全量重分类（2026-07-19）🚧

> 目标：替换旧的“规则预览 + 手动应用”分类流程。新流程立即清空旧目录、以数据库快照为事实来源，支持暂停/恢复并自动、幂等地应用分类结果。

| 项目 | 状态 | 说明 |
|------|------|------|
| 持久化快照、域名组、工作单元与应用记录 | ✅ | 已落地迁移、实体、Mapper 与幂等结果持久化 |
| 主域名分桶、大域名与零散池 AI 聚类 | ✅ | 已实现大域名命名/分析、零散池草案与目录规范化 |
| 启动恢复、暂停/恢复与单线程数据库驱动执行 | ✅ | 后台执行器只负责运行；状态与结果完全存于数据库 |
| 重分类 REST 接口 | ✅ | `/toolbox/reclassification/start`、任务查询、暂停、继续、可恢复列表 |
| 工具箱任务控制界面 | ✅ | 已移除策略预览/手动应用入口，改为进度、暂停、继续、自动结果统计 |
| 完整 MySQL 端到端恢复冒烟测试 | ⏳ | 尚未加入 Testcontainers 或本地 MySQL fake-AI 集成测试 |
| 旧内存分类任务服务清理 | ✅ | 已移除旧 `/toolbox/classify/*`、手动应用与批量标题补全接口；前端统一使用可恢复重分类流程 |

**运行前提：** 已有数据库需一次性执行 `sql/migrations/20260719_resumable_ai_reclassification.sql`；AI Key 只通过 `BOOKMARK_AI_API_KEY` 配置。

### 8.5.1 已验证目录整理执行（2026-07-20）✅

- 已完成 AI 重分类结果的第四轮首批领域收拢：`车辆` 与 `成人相关内容`。
- 已迁移 157 条书签，新增 11 个目标目录、删除 17 个已迁空来源叶子目录；书签总数保持 7,497 条，目录数由 836 降至 830。
- 已创建迁移标识 `fourth-domain-consolidation-20260720-v1` 的数据库回滚快照；执行记录见 `docs/reviews/2026-07-20-fourth-round-domain-consolidation-execution.md`。
- 本记录只证明上述本地数据库目录迁移和完整性校验成功；自动化 MySQL/fake-AI 端到端恢复测试仍按上表待补。

## 九、第五阶段：智能书签分类

### 9.1 架构设计

```
书签输入
  │
  ├─ 步骤1：标题补全（AI 批量处理）
  │   ├── 原标题为空/过短/无意义 → 调用 AI 生成
  │   ├── 原标题已有足够信息 → 跳过
  │   └── 输出：完整标题 + 关键词列表
  │
  ├─ 步骤2：规则引擎（确定性，零成本）
  │   ├── 域名白名单匹配 → 60%+ 覆盖率
  │   ├── TLD + 语言检测 → 国内外区分
  │   └── 关键词匹配 → 辅助分类
  │
  └─ 步骤3：AI 分类（仅处理规则未覆盖的）
      └── 输入：完整标题 + URL + 关键词
```

### 9.2 命名公式

```
[来源/站点] [核心关键词] [页面类型] - [使用场景]

示例:
  github.com/facebook/react/issues/12345
  → "React 官方仓库 Issue #12345 - 组件渲染性能问题讨论"

  (空标题的某个工具站)
  → "在线 JSON 格式化工具 - 开发者日常使用"
```

### 9.3 三种分类策略

| 策略 | 规则层 | AI 层 | 文件夹数 | 适用场景 |
|------|--------|-------|---------|---------|
| 按域名 | 域名→文件夹名 | 未知域名 AI 分组 | 多 | 精确查找 |
| 按功能 | 域名白名单→功能类 | 未知网站 AI 推断功能 | 少 | 日常浏览 |
| 按国内外 | TLD+语言→国内/国外 | 几乎不需要 | 极少 | 语言偏好 |

### 9.4 开发任务

#### Phase 1：域名白名单规则引擎

- [x] 1.1 创建 `DomainCategoryMapper` 服务 — 维护域名→分类映射表
- [x] 1.2 覆盖 top 100 高频域名（开发工具、知识社区、视频娱乐、新闻资讯、购物、设计、AI、云服务、学术等）
- [x] 1.3 实现 `classifyByDomain()` 策略
- [x] 1.4 实现 `classifyByFunction()` 策略
- [x] 1.5 实现 `classifyByRegion()` 策略（TLD + 中文检测）
- [x] 1.6 新增 API 端点：`POST /BookMarks/toolbox/classify` — 按策略分类

#### Phase 2：标题补全

- [x] 2.1 实现 `generateTitle()` — AI 根据 URL 生成完整标题
- [x] 2.2 批量处理：仅对空标题/无意义标题调用 AI
- [x] 2.3 命名格式：「关键词 - 场景」+ 提取关键词列表
- [x] 2.4 新增 API 端点：`POST /BookMarks/toolbox/generateTitles`

#### Phase 3：前端策略选择 + 预览

- [x] 3.1 工具箱新增"智能分类"工具卡片
- [x] 3.2 策略选择界面：三种策略 + 预览按钮
- [x] 3.3 分类结果预览（标题 + 归属文件夹）
- [x] 3.4 确认应用 + 落库

#### Phase 4：AI 处理未知部分

- [x] 4.1 规则引擎覆盖不到的调用 AI 分类
- [x] 4.2 优化 AI Prompt（命名 + 分类一步完成）
- [x] 4.3 低置信度标记 + 人工审核入口

---

*文档版本：v1.7*
*最后更新：2026年7月*
*更新内容：新增第五阶段智能书签分类开发计划*
