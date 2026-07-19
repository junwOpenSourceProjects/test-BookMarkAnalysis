package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationTask;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationTaskMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationWorkUnitMapper;

@ExtendWith(MockitoExtension.class)
class ReclassificationTaskControlServiceTest {

    @Mock private AiClassificationTaskMapper taskMapper;
    @Mock private AiReclassificationWorkUnitMapper workUnitMapper;

    @Test
    void pausesOnlyRunningTasksAndResumesPausedOrRecoverableTasks() {
        AiClassificationTask running = task(1L, ReclassificationConstants.TASK_STATUS_RUNNING);
        when(taskMapper.selectById(1L)).thenReturn(running);
        when(taskMapper.markTaskPausedIfRunning(1L)).thenReturn(1);

        ReclassificationTaskControlService service =
                new ReclassificationTaskControlService(taskMapper, workUnitMapper);

        assertEquals(ReclassificationConstants.TASK_STATUS_PAUSED, service.pause(1L).getStatus());

        AiClassificationTask paused = task(2L, ReclassificationConstants.TASK_STATUS_PAUSED);
        when(taskMapper.selectById(2L)).thenReturn(paused);
        when(taskMapper.markTaskRunningIfResumable(2L)).thenReturn(1);
        assertEquals(ReclassificationConstants.TASK_STATUS_RUNNING, service.resume(2L).getStatus());
    }

    @Test
    void rejectsPauseOrResumeFromInvalidState() {
        when(taskMapper.selectById(3L)).thenReturn(task(3L, ReclassificationConstants.TASK_STATUS_COMPLETED));
        ReclassificationTaskControlService service =
                new ReclassificationTaskControlService(taskMapper, workUnitMapper);

        assertThrows(IllegalStateException.class, () -> service.pause(3L));
        assertThrows(IllegalStateException.class, () -> service.resume(3L));
    }

    @Test
    void marksInterruptedRunningStateRecoverableAtStartup() {
        when(taskMapper.markRunningTasksRecoverable()).thenReturn(2);
        when(workUnitMapper.markRunningUnitsRetryableFailed()).thenReturn(3);
        ReclassificationTaskControlService service =
                new ReclassificationTaskControlService(taskMapper, workUnitMapper);

        ReclassificationTaskControlService.RecoverySummary summary = service.recoverInterruptedTasks();

        assertEquals(2, summary.recoveredTasks());
        assertEquals(3, summary.retryableUnits());
    }

    private AiClassificationTask task(Long id, String status) {
        AiClassificationTask task = new AiClassificationTask();
        task.setId(id);
        task.setStatus(status);
        task.setPhase(ReclassificationConstants.TASK_PHASE_LARGE_DOMAINS);
        return task;
    }
}
