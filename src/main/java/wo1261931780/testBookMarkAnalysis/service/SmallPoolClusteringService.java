package wo1261931780.testBookMarkAnalysis.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationResult;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationWorkUnit;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationResultMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationWorkUnitMapper;

@Service
public class SmallPoolClusteringService {
    private static final String PROMPT_VERSION = "reclassify-v1";

    private final AiClassificationResultMapper resultMapper;
    private final AiReclassificationWorkUnitMapper workUnitMapper;

    public SmallPoolClusteringService(
            AiClassificationResultMapper resultMapper,
            AiReclassificationWorkUnitMapper workUnitMapper) {
        this.resultMapper = resultMapper;
        this.workUnitMapper = workUnitMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public int planClusterDrafts(Long taskId) {
        List<AiClassificationResult> pendingSmallResults = resultMapper.selectList(
                new LambdaQueryWrapper<AiClassificationResult>()
                        .eq(AiClassificationResult::getTaskId, taskId)
                        .eq(AiClassificationResult::getLogicalFolderKey, "small:pending")
                        .eq(AiClassificationResult::getStatus, "PENDING")
                        .orderByAsc(AiClassificationResult::getId));
        Set<String> existingKeys = new HashSet<>();
        int nextOrdinal = 0;
        for (AiReclassificationWorkUnit existing : workUnitMapper.selectList(
                new LambdaQueryWrapper<AiReclassificationWorkUnit>()
                        .eq(AiReclassificationWorkUnit::getTaskId, taskId))) {
            existingKeys.add(existing.getUnitKey());
            nextOrdinal = Math.max(nextOrdinal, existing.getOrdinal() == null ? 0 : existing.getOrdinal() + 1);
        }

        int created = 0;
        for (int start = 0, batchNumber = 1;
                start < pendingSmallResults.size();
                start += ReclassificationConstants.SMALL_POOL_CLUSTER_DRAFT_SIZE, batchNumber++) {
            int end = Math.min(start + ReclassificationConstants.SMALL_POOL_CLUSTER_DRAFT_SIZE, pendingSmallResults.size());
            String unitKey = String.format("small-draft:%04d", batchNumber);
            if (!existingKeys.add(unitKey)) {
                continue;
            }
            AiReclassificationWorkUnit unit = new AiReclassificationWorkUnit();
            unit.setId(IdUtil.getSnowflakeNextId());
            unit.setTaskId(taskId);
            unit.setUnitKind(ReclassificationConstants.UNIT_SMALL_POOL_CLUSTER_DRAFT);
            unit.setUnitKey(unitKey);
            unit.setStatus(ReclassificationConstants.WORK_UNIT_STATUS_PENDING);
            unit.setOrdinal(nextOrdinal++);
            unit.setAttemptCount(0);
            unit.setPromptVersion(PROMPT_VERSION);
            unit.setInputJson(JSONUtil.toJsonStr(clusterInput(pendingSmallResults.subList(start, end))));
            workUnitMapper.insert(unit);
            created++;
        }
        return created;
    }

    @Transactional(rollbackFor = Exception.class)
    public int planCanonicalization(Long taskId) {
        List<AiClassificationResult> draftResults = resultMapper.selectList(
                new LambdaQueryWrapper<AiClassificationResult>()
                        .eq(AiClassificationResult::getTaskId, taskId)
                        .likeRight(AiClassificationResult::getLogicalFolderKey, "draft:")
                        .orderByAsc(AiClassificationResult::getLogicalFolderKey));
        if (draftResults.isEmpty()) {
            return 0;
        }
        for (AiReclassificationWorkUnit existing : workUnitMapper.selectList(
                new LambdaQueryWrapper<AiReclassificationWorkUnit>()
                        .eq(AiReclassificationWorkUnit::getTaskId, taskId)
                        .eq(
                                AiReclassificationWorkUnit::getUnitKey,
                                "small-canonicalize"))) {
            if ("small-canonicalize".equals(existing.getUnitKey())) {
                return 0;
            }
        }
        Map<String, String> folders = new LinkedHashMap<>();
        for (AiClassificationResult result : draftResults) {
            folders.putIfAbsent(result.getLogicalFolderKey(), result.getSuggestedFolder());
        }
        AiReclassificationWorkUnit unit = new AiReclassificationWorkUnit();
        unit.setId(IdUtil.getSnowflakeNextId());
        unit.setTaskId(taskId);
        unit.setUnitKind(ReclassificationConstants.UNIT_SMALL_POOL_CANONICALIZE_FOLDERS);
        unit.setUnitKey("small-canonicalize");
        unit.setStatus(ReclassificationConstants.WORK_UNIT_STATUS_PENDING);
        unit.setOrdinal(1000000);
        unit.setAttemptCount(0);
        unit.setPromptVersion(PROMPT_VERSION);
        unit.setInputJson(JSONUtil.toJsonStr(Map.of("draftFolders", folders)));
        workUnitMapper.insert(unit);
        return 1;
    }

    private Map<String, Object> clusterInput(List<AiClassificationResult> results) {
        List<Map<String, Object>> bookmarks = new ArrayList<>();
        for (AiClassificationResult result : results) {
            Map<String, Object> bookmark = new LinkedHashMap<>();
            bookmark.put("bookmarkId", result.getBookmarkId().toString());
            bookmark.put("suggestedTitle", result.getSuggestedTitle());
            bookmark.put("keywords", result.getKeywords());
            bookmark.put("pageType", result.getPageType());
            bookmark.put("topicHint", result.getTopicHint());
            bookmark.put("reason", result.getReason());
            bookmarks.add(bookmark);
        }
        Map<String, Object> input = new LinkedHashMap<>();
        input.put("bookmarks", bookmarks);
        return input;
    }
}
