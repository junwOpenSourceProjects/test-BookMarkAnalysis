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

@ExtendWith(MockitoExtension.class)
class ReclassificationWorkUnitProcessorTest {

    @Mock private AiClassificationTaskMapper taskMapper;
    @Mock private AiReclassificationDomainGroupMapper domainGroupMapper;
    @Mock private ReclassificationAiService aiService;
    @Mock private ReclassificationResultPersistenceService persistenceService;

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
                config, taskMapper, domainGroupMapper, aiService, persistenceService);

        assertEquals(2, processor.process(unit));
        verify(aiService).requestLargeDomainFolderName(
                "{\"bookmarks\":[]}", "https://example.test", "test-secret", "test-model");
        verify(persistenceService).persistFolderNaming(unit, reply, naming);
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
                config, taskMapper, domainGroupMapper, aiService, persistenceService);

        assertEquals(0, processor.process(unit));
        verify(persistenceService).markUnitRetryableFailed(any(), any());
        verify(taskMapper).markTaskRecoverable(1L, "offline");
    }
}
