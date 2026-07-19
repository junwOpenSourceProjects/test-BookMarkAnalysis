package wo1261931780.testBookMarkAnalysis.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import wo1261931780.testBookMarkAnalysis.config.BookmarkConfig;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationTask;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationDomainGroup;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationWorkUnit;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationTaskMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationDomainGroupMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationWorkUnitMapper;

@Service
public class ReclassificationWorkUnitProcessor {
    private final BookmarkConfig bookmarkConfig;
    private final AiClassificationTaskMapper taskMapper;
    private final AiReclassificationDomainGroupMapper domainGroupMapper;
    private final ReclassificationAiService aiService;
    private final ReclassificationResultPersistenceService persistenceService;
    private final AiReclassificationWorkUnitMapper workUnitMapper;
    private final ReclassificationApplicationService applicationService;

    public ReclassificationWorkUnitProcessor(
            BookmarkConfig bookmarkConfig,
            AiClassificationTaskMapper taskMapper,
            AiReclassificationDomainGroupMapper domainGroupMapper,
            ReclassificationAiService aiService,
            ReclassificationResultPersistenceService persistenceService,
            AiReclassificationWorkUnitMapper workUnitMapper,
            ReclassificationApplicationService applicationService) {
        this.bookmarkConfig = bookmarkConfig;
        this.taskMapper = taskMapper;
        this.domainGroupMapper = domainGroupMapper;
        this.aiService = aiService;
        this.persistenceService = persistenceService;
        this.workUnitMapper = workUnitMapper;
        this.applicationService = applicationService;
    }

    /** Returns newly planned work-unit count; returns zero when no successor was planned. */
    public int process(AiReclassificationWorkUnit unit) {
        try {
            AiClassificationTask task = taskMapper.selectById(unit.getTaskId());
            if (task == null) {
                throw new IllegalArgumentException("重分类任务不存在: " + unit.getTaskId());
            }
            String apiKey = bookmarkConfig.getAiApiKey();
            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalStateException("未配置 AI API Key，无法继续重分类任务");
            }
            return switch (unit.getUnitKind()) {
                case ReclassificationConstants.UNIT_LARGE_DOMAIN_FOLDER_NAME -> processLargeFolderName(
                        unit, task, apiKey);
                case ReclassificationConstants.UNIT_LARGE_DOMAIN_BOOKMARK_ANALYSIS,
                                ReclassificationConstants.UNIT_SMALL_POOL_BOOKMARK_ANALYSIS ->
                        processBookmarkAnalysis(unit, task, apiKey);
                case ReclassificationConstants.UNIT_SMALL_POOL_CLUSTER_DRAFT ->
                        processSmallPoolClusterDraft(unit, task, apiKey);
                default -> throw new IllegalArgumentException("暂不支持的重分类工作单元: " + unit.getUnitKind());
            };
        } catch (Exception exception) {
            persistenceService.markUnitRetryableFailed(unit, exception);
            taskMapper.markTaskRecoverable(unit.getTaskId(), exception.getMessage());
            return 0;
        }
    }

    private int processLargeFolderName(
            AiReclassificationWorkUnit unit, AiClassificationTask task, String apiKey) throws Exception {
        AiClientService.AiJsonReply reply = aiService.requestLargeDomainFolderName(
                unit.getInputJson(), task.getApiBaseUrl(), apiKey, task.getModelName());
        ReclassificationAiService.FolderNaming naming = aiService.parseLargeDomainFolder(reply.array());
        return persistenceService.persistFolderNaming(unit, reply, naming);
    }

    private int processBookmarkAnalysis(
            AiReclassificationWorkUnit unit, AiClassificationTask task, String apiKey) throws Exception {
        AiClientService.AiJsonReply reply = aiService.requestBookmarkAnalyses(
                unit.getInputJson(), task.getApiBaseUrl(), apiKey, task.getModelName());
        List<ReclassificationAiService.BookmarkAnalysis> analyses = aiService.parseBookmarkAnalyses(
                reply.array(), expectedBookmarkIds(unit.getInputJson()));
        String logicalFolderKey = resolveLogicalFolderKey(unit);
        persistenceService.persistBookmarkAnalyses(unit, logicalFolderKey, analyses, reply);
        if (unit.getDomainGroupId() != null
                && workUnitMapper.countIncompleteAnalysisUnitsForGroup(
                                unit.getTaskId(), unit.getDomainGroupId())
                        == 0) {
            AiReclassificationDomainGroup group = domainGroupMapper.selectById(unit.getDomainGroupId());
            if (group != null && group.getFolderName() != null && group.getLogicalFolderKey() != null) {
                applicationService.applyFolder(
                        unit.getTaskId(),
                        group.getLogicalFolderKey(),
                        group.getFolderName(),
                        ReclassificationConstants.TASK_PHASE_LARGE_DOMAINS);
            }
        }
        return 0;
    }

    private int processSmallPoolClusterDraft(
            AiReclassificationWorkUnit unit, AiClassificationTask task, String apiKey) throws Exception {
        AiClientService.AiJsonReply reply = aiService.requestSmallPoolClusterDraft(
                unit.getInputJson(), task.getApiBaseUrl(), apiKey, task.getModelName());
        List<ReclassificationAiService.ClusterDraftAssignment> assignments =
                aiService.parseSmallPoolClusterDraft(reply.array(), expectedBookmarkIds(unit.getInputJson()));
        persistenceService.persistSmallPoolDraftAssignments(unit, assignments, reply);
        return 0;
    }

    private Set<String> expectedBookmarkIds(String inputJson) {
        JSONArray bookmarks = JSONUtil.parseObj(inputJson).getJSONArray("bookmarks");
        Set<String> ids = new LinkedHashSet<>();
        if (bookmarks == null) {
            return ids;
        }
        for (int index = 0; index < bookmarks.size(); index++) {
            ids.add(bookmarks.getJSONObject(index).getStr("bookmarkId"));
        }
        return ids;
    }

    private String resolveLogicalFolderKey(AiReclassificationWorkUnit unit) {
        if (unit.getDomainGroupId() == null) {
            return "small:pending";
        }
        AiReclassificationDomainGroup group = domainGroupMapper.selectById(unit.getDomainGroupId());
        if (group == null || group.getLogicalFolderKey() == null || group.getLogicalFolderKey().isBlank()) {
            throw new IllegalStateException("大域名组尚未生成稳定目录键: " + unit.getDomainGroupId());
        }
        return group.getLogicalFolderKey();
    }
}
