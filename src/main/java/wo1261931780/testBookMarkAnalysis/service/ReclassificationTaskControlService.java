package wo1261931780.testBookMarkAnalysis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationTask;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationTaskMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationWorkUnitMapper;

@Service
public class ReclassificationTaskControlService {
    private final AiClassificationTaskMapper taskMapper;
    private final AiReclassificationWorkUnitMapper workUnitMapper;

    public ReclassificationTaskControlService(
            AiClassificationTaskMapper taskMapper,
            AiReclassificationWorkUnitMapper workUnitMapper) {
        this.taskMapper = taskMapper;
        this.workUnitMapper = workUnitMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public AiClassificationTask pause(Long taskId) {
        AiClassificationTask task = requireTask(taskId);
        if (!ReclassificationConstants.TASK_STATUS_RUNNING.equals(task.getStatus())) {
            throw new IllegalStateException("只有运行中的重分类任务可以暂停");
        }
        if (taskMapper.markTaskPausedIfRunning(taskId) != 1) {
            throw new IllegalStateException("重分类任务状态已变化，请刷新后重试");
        }
        task.setStatus(ReclassificationConstants.TASK_STATUS_PAUSED);
        return task;
    }

    @Transactional(rollbackFor = Exception.class)
    public AiClassificationTask resume(Long taskId) {
        AiClassificationTask task = requireTask(taskId);
        if (!ReclassificationConstants.TASK_STATUS_PAUSED.equals(task.getStatus())
                && !ReclassificationConstants.TASK_STATUS_RECOVERABLE.equals(task.getStatus())) {
            throw new IllegalStateException("只有暂停或可恢复的重分类任务可以继续");
        }
        if (taskMapper.markTaskRunningIfResumable(taskId) != 1) {
            throw new IllegalStateException("重分类任务状态已变化，请刷新后重试");
        }
        task.setStatus(ReclassificationConstants.TASK_STATUS_RUNNING);
        return task;
    }

    @Transactional(rollbackFor = Exception.class)
    public RecoverySummary recoverInterruptedTasks() {
        int recoveredTasks = taskMapper.markRunningTasksRecoverable();
        int retryableUnits = workUnitMapper.markRunningUnitsRetryableFailed();
        return new RecoverySummary(recoveredTasks, retryableUnits);
    }

    private AiClassificationTask requireTask(Long taskId) {
        AiClassificationTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("重分类任务不存在: " + taskId);
        }
        return task;
    }

    public record RecoverySummary(int recoveredTasks, int retryableUnits) {}
}
