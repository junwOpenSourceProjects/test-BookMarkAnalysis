package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import wo1261931780.testBookMarkAnalysis.config.BookmarkConfig;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationTask;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationTaskMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationDomainGroupMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationWorkUnitMapper;

class ResumableReclassificationTaskServiceTest {

    @Test
    void rejectsStartingAnotherTaskWhileAnExistingTaskIsActive() {
        AiClassificationTaskMapper taskMapper = mock(AiClassificationTaskMapper.class);
        when(taskMapper.existsActiveTask()).thenReturn(true);
        ResumableReclassificationTaskService service = service(taskMapper);

        assertThrows(IllegalStateException.class, () -> service.start(null, null));

        verify(taskMapper, never()).insert(any(AiClassificationTask.class));
    }

    @Test
    void continuesCompletedTaskFromPersistedSmallPoolAnalyses() {
        AiClassificationTaskMapper taskMapper = mock(AiClassificationTaskMapper.class);
        AiReclassificationWorkUnitMapper workUnitMapper =
                mock(AiReclassificationWorkUnitMapper.class);
        SmallPoolClusteringService smallPoolClusteringService = mock(SmallPoolClusteringService.class);
        AiClassificationTask task = new AiClassificationTask();
        task.setId(11L);
        task.setStatus(ReclassificationConstants.TASK_STATUS_COMPLETED);
        when(taskMapper.selectById(11L)).thenReturn(task).thenReturn((AiClassificationTask) null);
        when(smallPoolClusteringService.planClusterDrafts(11L)).thenReturn(62);
        when(taskMapper.markTaskRunningForSmallPoolContinuation(11L)).thenReturn(1);

        ResumableReclassificationTaskService service = service(
                taskMapper,
                workUnitMapper,
                mock(AiReclassificationDomainGroupMapper.class),
                smallPoolClusteringService);

        AiClassificationTask continued = service.continueSmallPool(11L);

        assertEquals(ReclassificationConstants.TASK_STATUS_RUNNING, continued.getStatus());
        verify(smallPoolClusteringService).planClusterDrafts(11L);
        verify(taskMapper).markTaskRunningForSmallPoolContinuation(11L);
    }

    @Test
    void describesDurableProgressFromPersistedRows() {
        AiClassificationTaskMapper taskMapper = mock(AiClassificationTaskMapper.class);
        AiReclassificationWorkUnitMapper workUnitMapper =
                mock(AiReclassificationWorkUnitMapper.class);
        AiReclassificationDomainGroupMapper domainGroupMapper =
                mock(AiReclassificationDomainGroupMapper.class);
        AiClassificationTask task = new AiClassificationTask();
        task.setId(11L);
        task.setStatus(ReclassificationConstants.TASK_STATUS_PAUSED);
        task.setTotalCount(12);
        task.setCreatedFolderCount(2);
        task.setMovedBookmarkCount(10);
        task.setUpdatedTitleCount(8);
        when(taskMapper.selectById(11L)).thenReturn(task);
        when(workUnitMapper.countByTask(11L)).thenReturn(7);
        when(workUnitMapper.countSucceededByTask(11L)).thenReturn(4);
        when(domainGroupMapper.selectCount(any())).thenReturn(2L);
        when(domainGroupMapper.selectList(any())).thenReturn(List.of());

        ResumableReclassificationTaskService service =
                service(taskMapper, workUnitMapper, domainGroupMapper);
        ResumableReclassificationTaskService.TaskDetails details = service.describe(11L);

        assertEquals(7, details.totalWorkUnits());
        assertEquals(4, details.completedWorkUnits());
        assertEquals(2, details.largeDomainGroups());
        assertEquals(0, details.smallPoolBookmarks());
    }

    private ResumableReclassificationTaskService service(AiClassificationTaskMapper taskMapper) {
        return service(
                taskMapper,
                mock(AiReclassificationWorkUnitMapper.class),
                mock(AiReclassificationDomainGroupMapper.class));
    }

    private ResumableReclassificationTaskService service(
            AiClassificationTaskMapper taskMapper,
            AiReclassificationWorkUnitMapper workUnitMapper,
            AiReclassificationDomainGroupMapper domainGroupMapper) {
        BookmarkConfig config = new BookmarkConfig();
        config.setAiApiKey("test-key");
        return service(
                taskMapper,
                workUnitMapper,
                domainGroupMapper,
                mock(SmallPoolClusteringService.class));
    }

    private ResumableReclassificationTaskService service(
            AiClassificationTaskMapper taskMapper,
            AiReclassificationWorkUnitMapper workUnitMapper,
            AiReclassificationDomainGroupMapper domainGroupMapper,
            SmallPoolClusteringService smallPoolClusteringService) {
        BookmarkConfig config = new BookmarkConfig();
        config.setAiApiKey("test-key");
        return new ResumableReclassificationTaskService(
                config,
                mock(BookmarkReclassificationPreparationService.class),
                mock(ReclassificationWorkUnitPlanner.class),
                mock(ReclassificationWorkUnitClaimService.class),
                mock(ReclassificationWorkUnitProcessor.class),
                mock(ReclassificationTaskControlService.class),
                mock(ReclassificationApplicationService.class),
                smallPoolClusteringService,
                taskMapper,
                workUnitMapper,
                domainGroupMapper);
    }
}
