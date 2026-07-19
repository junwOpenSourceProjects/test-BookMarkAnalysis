package wo1261931780.testBookMarkAnalysis.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationDomainGroup;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationSnapshot;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationWorkUnit;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationDomainGroupMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationSnapshotMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationWorkUnitMapper;

@Service
public class ReclassificationWorkUnitPlanner {
    private static final String PROMPT_VERSION = "reclassify-v1";

    private final AiReclassificationDomainGroupMapper domainGroupMapper;
    private final AiReclassificationSnapshotMapper snapshotMapper;
    private final AiReclassificationWorkUnitMapper workUnitMapper;

    public ReclassificationWorkUnitPlanner(
            AiReclassificationDomainGroupMapper domainGroupMapper,
            AiReclassificationSnapshotMapper snapshotMapper,
            AiReclassificationWorkUnitMapper workUnitMapper) {
        this.domainGroupMapper = domainGroupMapper;
        this.snapshotMapper = snapshotMapper;
        this.workUnitMapper = workUnitMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public int planInitialWorkUnits(Long taskId) {
        Set<String> existingKeys = loadExistingUnitKeys(taskId);
        int nextOrdinal = existingKeys.size();
        int created = 0;

        List<AiReclassificationDomainGroup> largeGroups = new ArrayList<>(
                domainGroupMapper.selectList(
                        new LambdaQueryWrapper<AiReclassificationDomainGroup>()
                                .eq(AiReclassificationDomainGroup::getTaskId, taskId)
                                .eq(
                                        AiReclassificationDomainGroup::getGroupType,
                                        ReclassificationConstants.SNAPSHOT_POOL_LARGE_DOMAIN)));
        largeGroups.sort(Comparator.comparing(AiReclassificationDomainGroup::getId));
        for (AiReclassificationDomainGroup group : largeGroups) {
            List<AiReclassificationSnapshot> groupSnapshots = snapshotsForGroup(group);
            String unitKey = "large-folder:" + group.getId();
            if (createUnitIfMissing(
                    taskId,
                    group.getId(),
                    ReclassificationConstants.UNIT_LARGE_DOMAIN_FOLDER_NAME,
                    unitKey,
                    nextOrdinal,
                    largeDomainInput(group, groupSnapshots),
                    existingKeys)) {
                created++;
                nextOrdinal++;
            }
        }

        List<AiReclassificationSnapshot> smallPoolSnapshots = snapshotMapper.selectList(
                new LambdaQueryWrapper<AiReclassificationSnapshot>()
                        .eq(AiReclassificationSnapshot::getTaskId, taskId)
                        .eq(
                                AiReclassificationSnapshot::getPoolType,
                                ReclassificationConstants.SNAPSHOT_POOL_SMALL_POOL)
                        .orderByAsc(AiReclassificationSnapshot::getOrdinal));
        for (int start = 0, batchNumber = 1;
                start < smallPoolSnapshots.size();
                start += ReclassificationConstants.BOOKMARK_ANALYSIS_BATCH_SIZE, batchNumber++) {
            int end = Math.min(
                    start + ReclassificationConstants.BOOKMARK_ANALYSIS_BATCH_SIZE,
                    smallPoolSnapshots.size());
            String unitKey = String.format("small-analysis:%04d", batchNumber);
            if (createUnitIfMissing(
                    taskId,
                    null,
                    ReclassificationConstants.UNIT_SMALL_POOL_BOOKMARK_ANALYSIS,
                    unitKey,
                    nextOrdinal,
                    bookmarkAnalysisInput(smallPoolSnapshots.subList(start, end)),
                    existingKeys)) {
                created++;
                nextOrdinal++;
            }
        }
        return created;
    }

    @Transactional(rollbackFor = Exception.class)
    public int planLargeDomainBookmarkAnalysisUnits(AiReclassificationDomainGroup group) {
        Set<String> existingKeys = loadExistingUnitKeys(group.getTaskId());
        int nextOrdinal = existingKeys.size();
        int created = 0;
        List<AiReclassificationSnapshot> snapshots = snapshotsForGroup(group);
        for (int start = 0, batchNumber = 1;
                start < snapshots.size();
                start += ReclassificationConstants.BOOKMARK_ANALYSIS_BATCH_SIZE, batchNumber++) {
            int end = Math.min(start + ReclassificationConstants.BOOKMARK_ANALYSIS_BATCH_SIZE, snapshots.size());
            String unitKey = String.format("large-analysis:%d:%04d", group.getId(), batchNumber);
            if (createUnitIfMissing(
                    group.getTaskId(),
                    group.getId(),
                    ReclassificationConstants.UNIT_LARGE_DOMAIN_BOOKMARK_ANALYSIS,
                    unitKey,
                    nextOrdinal,
                    largeDomainInput(group, snapshots.subList(start, end)),
                    existingKeys)) {
                created++;
                nextOrdinal++;
            }
        }
        return created;
    }

    private List<AiReclassificationSnapshot> snapshotsForGroup(
            AiReclassificationDomainGroup group) {
        return snapshotMapper.selectList(
                new LambdaQueryWrapper<AiReclassificationSnapshot>()
                        .eq(AiReclassificationSnapshot::getTaskId, group.getTaskId())
                        .eq(
                                AiReclassificationSnapshot::getRegistrableDomain,
                                group.getRegistrableDomain())
                        .orderByAsc(AiReclassificationSnapshot::getOrdinal));
    }

    private Set<String> loadExistingUnitKeys(Long taskId) {
        Set<String> keys = new HashSet<>();
        for (AiReclassificationWorkUnit unit : workUnitMapper.selectList(
                new LambdaQueryWrapper<AiReclassificationWorkUnit>()
                        .eq(AiReclassificationWorkUnit::getTaskId, taskId))) {
            keys.add(unit.getUnitKey());
        }
        return keys;
    }

    private boolean createUnitIfMissing(
            Long taskId,
            Long domainGroupId,
            String unitKind,
            String unitKey,
            int ordinal,
            String inputJson,
            Set<String> existingKeys) {
        if (!existingKeys.add(unitKey)) {
            return false;
        }
        AiReclassificationWorkUnit unit = new AiReclassificationWorkUnit();
        unit.setId(IdUtil.getSnowflakeNextId());
        unit.setTaskId(taskId);
        unit.setDomainGroupId(domainGroupId);
        unit.setUnitKind(unitKind);
        unit.setUnitKey(unitKey);
        unit.setStatus(ReclassificationConstants.WORK_UNIT_STATUS_PENDING);
        unit.setOrdinal(ordinal);
        unit.setAttemptCount(0);
        unit.setPromptVersion(PROMPT_VERSION);
        unit.setInputJson(inputJson);
        workUnitMapper.insert(unit);
        return true;
    }

    private String largeDomainInput(
            AiReclassificationDomainGroup group, List<AiReclassificationSnapshot> snapshots) {
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("domainGroupId", group.getId().toString());
        input.put("registrableDomain", group.getRegistrableDomain());
        input.put("folderName", group.getFolderName());
        input.put("bookmarks", bookmarkPayload(snapshots));
        return JSONUtil.toJsonStr(input);
    }

    private String bookmarkAnalysisInput(List<AiReclassificationSnapshot> snapshots) {
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("bookmarks", bookmarkPayload(snapshots));
        return JSONUtil.toJsonStr(input);
    }

    private List<Map<String, Object>> bookmarkPayload(List<AiReclassificationSnapshot> snapshots) {
        List<Map<String, Object>> bookmarks = new ArrayList<>();
        for (AiReclassificationSnapshot snapshot : snapshots) {
            Map<String, Object> bookmark = new LinkedHashMap<>();
            bookmark.put("bookmarkId", snapshot.getBookmarkId().toString());
            bookmark.put("url", snapshot.getHref());
            bookmark.put("originalTitle", snapshot.getOriginalTitle());
            bookmark.put("registrableDomain", snapshot.getRegistrableDomain());
            bookmarks.add(bookmark);
        }
        return bookmarks;
    }
}
