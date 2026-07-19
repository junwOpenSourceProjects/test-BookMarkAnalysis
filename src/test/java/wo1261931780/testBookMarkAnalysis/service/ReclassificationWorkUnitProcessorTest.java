package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cn.hutool.json.JSONUtil;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wo1261931780.testBookMarkAnalysis.config.BookmarkConfig;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationTask;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationDomainGroup;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationWorkUnit;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationTaskMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationDomainGroupMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationWorkUnitMapper;

@ExtendWith(MockitoExtension.class)
class ReclassificationWorkUnitProcessorTest {

    @Mock private AiClassificationTaskMapper taskMapper;
    @Mock private AiReclassificationDomainGroupMapper domainGroupMapper;
    @Mock private ReclassificationAiService aiService;
    @Mock private ReclassificationResultPersistenceService persistenceService;
    @Mock private AiReclassificationWorkUnitMapper workUnitMapper;
    @Mock private ReclassificationApplicationService applicationService;

    @Test
    void processesLargeDomainFolderNamingUsingTaskEndpointAndConfiguredSecret() throws Exception {
        BookmarkConfig config = new BookmarkConfig();
        config.setAiApiKey("test-secret");
        AiClassificationTask task = new AiClassificationTask();
        task.setId(1L);
        task.setApiBaseUrl("https://example.test");
        task.setModelName("test-model");
        when(taskMapper.selectById(1L)).thenReturn(task);

        AiReclassificationWorkUnit unit = new AiReclassificationWorkUnit();
        unit.setId(10L);
        unit.setTaskId(1L);
        unit.setDomainGroupId(100L);
        unit.setUnitKind(ReclassificationConstants.UNIT_LARGE_DOMAIN_FOLDER_NAME);
        unit.setInputJson("{\"bookmarks\":[]}");
        AiClientService.AiJsonReply reply =
                new AiClientService.AiJsonReply("{}", "[]", JSONUtil.createArray());
        when(aiService.requestLargeDomainFolderName(
                        anyString(), anyString(), anyString(), anyString()))
                .thenReturn(reply);
        ReclassificationAiService.FolderNaming naming =
                new ReclassificationAiService.FolderNaming("GitHub 项目", "代码协作");
        when(aiService.parseLargeDomainFolder(reply.array())).thenReturn(naming);
        when(persistenceService.persistFolderNaming(unit, reply, naming)).thenReturn(2);

        ReclassificationWorkUnitProcessor processor = new ReclassificationWorkUnitProcessor(
                config, taskMapper, domainGroupMapper, aiService, persistenceService, workUnitMapper, applicationService);

        assertEquals(2, processor.process(unit));
        verify(aiService).requestLargeDomainFolderName(
                "{\"bookmarks\":[]}", "https://example.test", "test-secret", "test-model");
        verify(persistenceService).persistFolderNaming(unit, reply, naming);
    }

    @Test
    void appliesLargeDomainFolderAfterItsFinalAnalysisUnitSucceeds() throws Exception {
        BookmarkConfig config = new BookmarkConfig();
        config.setAiApiKey("test-secret");
        AiClassificationTask task = new AiClassificationTask();
        task.setId(1L);
        task.setApiBaseUrl("https://example.test");
        task.setModelName("test-model");
        when(taskMapper.selectById(1L)).thenReturn(task);

        AiReclassificationDomainGroup group = new AiReclassificationDomainGroup();
        group.setId(100L);
        group.setLogicalFolderKey("large:100");
        group.setFolderName("GitHub 项目");
        when(domainGroupMapper.selectById(100L)).thenReturn(group);

        AiReclassificationWorkUnit unit = new AiReclassificationWorkUnit();
        unit.setId(10L);
        unit.setTaskId(1L);
        unit.setDomainGroupId(100L);
        unit.setUnitKind(ReclassificationConstants.UNIT_LARGE_DOMAIN_BOOKMARK_ANALYSIS);
        unit.setInputJson("{\"bookmarks\":[{\"bookmarkId\":\"1\"}]}");
        AiClientService.AiJsonReply reply = new AiClientService.AiJsonReply("{}", "[]", JSONUtil.createArray());
        when(aiService.requestBookmarkAnalyses(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(reply);
        List<ReclassificationAiService.BookmarkAnalysis> analyses = List.of(
                new ReclassificationAiService.BookmarkAnalysis("1", "GitHub 文档", null, "文档", "开发", 90, "官方"));
        when(aiService.parseBookmarkAnalyses(reply.array(), java.util.Set.of("1"))).thenReturn(analyses);
        when(workUnitMapper.countIncompleteAnalysisUnitsForGroup(1L, 100L)).thenReturn(0);
        when(applicationService.applyFolder(1L, "large:100", "GitHub 项目", "LARGE_DOMAINS"))
                .thenReturn(new ReclassificationApplicationService.ApplicationStats(1, 1, 1));

        ReclassificationWorkUnitProcessor processor = new ReclassificationWorkUnitProcessor(
                config, taskMapper, domainGroupMapper, aiService, persistenceService, workUnitMapper, applicationService);

        processor.process(unit);

        verify(applicationService).applyFolder(1L, "large:100", "GitHub 项目", "LARGE_DOMAINS");
    }

    @Test
    void processesSmallPoolDraftAndPersistsTemporaryTopicAssignments() throws Exception {
        BookmarkConfig config = new BookmarkConfig();
        config.setAiApiKey("test-secret");
        AiClassificationTask task = new AiClassificationTask();
        task.setId(1L);
        task.setApiBaseUrl("https://example.test");
        task.setModelName("test-model");
        when(taskMapper.selectById(1L)).thenReturn(task);
        AiReclassificationWorkUnit unit = new AiReclassificationWorkUnit();
        unit.setId(20L);
        unit.setTaskId(1L);
        unit.setUnitKind(ReclassificationConstants.UNIT_SMALL_POOL_CLUSTER_DRAFT);
        unit.setInputJson("{\"bookmarks\":[{\"bookmarkId\":\"1\"}]}");
        AiClientService.AiJsonReply reply = new AiClientService.AiJsonReply("{}", "[]", JSONUtil.createArray());
        when(aiService.requestSmallPoolClusterDraft(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(reply);
        List<ReclassificationAiService.ClusterDraftAssignment> assignments = List.of(
                new ReclassificationAiService.ClusterDraftAssignment(
                        "1", "draft:frontend-tools", "前端开发工具"));
        when(aiService.parseSmallPoolClusterDraft(reply.array(), java.util.Set.of("1")))
                .thenReturn(assignments);

        ReclassificationWorkUnitProcessor processor = new ReclassificationWorkUnitProcessor(
                config, taskMapper, domainGroupMapper, aiService, persistenceService, workUnitMapper, applicationService);

        processor.process(unit);

        verify(persistenceService).persistSmallPoolDraftAssignments(unit, assignments, reply);
    }

    @Test
    void processesCanonicalizationAndAppliesEachFinalSmallPoolFolder() throws Exception {
        BookmarkConfig config = new BookmarkConfig();
        config.setAiApiKey("test-secret");
        AiClassificationTask task = new AiClassificationTask();
        task.setId(1L);
        task.setApiBaseUrl("https://example.test");
        task.setModelName("test-model");
        when(taskMapper.selectById(1L)).thenReturn(task);
        AiReclassificationWorkUnit unit = new AiReclassificationWorkUnit();
        unit.setId(30L);
        unit.setTaskId(1L);
        unit.setUnitKind(ReclassificationConstants.UNIT_SMALL_POOL_CANONICALIZE_FOLDERS);
        unit.setInputJson("{\"draftFolders\":{\"draft:frontend-tools\":\"前端工具\"}}");
        AiClientService.AiJsonReply reply = new AiClientService.AiJsonReply("{}", "[]", JSONUtil.createArray());
        when(aiService.requestSmallPoolCanonicalization(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(reply);
        List<ReclassificationAiService.CanonicalFolderAssignment> assignments = List.of(
                new ReclassificationAiService.CanonicalFolderAssignment(
                        "draft:frontend-tools", "small:frontend-development", "前端开发与工具"));
        when(aiService.parseSmallPoolCanonicalization(reply.array(), java.util.Set.of("draft:frontend-tools")))
                .thenReturn(assignments);

        ReclassificationWorkUnitProcessor processor = new ReclassificationWorkUnitProcessor(
                config, taskMapper, domainGroupMapper, aiService, persistenceService, workUnitMapper, applicationService);

        processor.process(unit);

        verify(aiService).requestSmallPoolCanonicalization(
                unit.getInputJson(), "https://example.test", "test-secret", "test-model");
        verify(persistenceService).persistSmallPoolCanonicalAssignments(unit, assignments, reply);
        verify(applicationService).applyFolder(
                1L,
                "small:frontend-development",
                "前端开发与工具",
                ReclassificationConstants.TASK_PHASE_SMALL_CANONICALIZATION);
    }

    @Test
    void turnsAiFailureIntoRecoverableTaskAndRetryableUnit() throws Exception {
        BookmarkConfig config = new BookmarkConfig();
        config.setAiApiKey("test-secret");
        AiClassificationTask task = new AiClassificationTask();
        task.setId(1L);
        task.setApiBaseUrl("https://example.test");
        task.setModelName("test-model");
        when(taskMapper.selectById(1L)).thenReturn(task);
        AiReclassificationWorkUnit unit = new AiReclassificationWorkUnit();
        unit.setId(10L);
        unit.setTaskId(1L);
        unit.setDomainGroupId(100L);
        unit.setUnitKind(ReclassificationConstants.UNIT_LARGE_DOMAIN_FOLDER_NAME);
        unit.setInputJson("{}");
        when(aiService.requestLargeDomainFolderName(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("offline"));

        ReclassificationWorkUnitProcessor processor = new ReclassificationWorkUnitProcessor(
                config, taskMapper, domainGroupMapper, aiService, persistenceService, workUnitMapper, applicationService);

        assertEquals(0, processor.process(unit));
        verify(persistenceService).markUnitRetryableFailed(any(), any());
        verify(taskMapper).markTaskRecoverable(1L, "offline");
    }
}
