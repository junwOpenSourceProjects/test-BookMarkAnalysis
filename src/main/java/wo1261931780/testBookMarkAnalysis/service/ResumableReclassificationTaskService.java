package wo1261931780.testBookMarkAnalysis.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import wo1261931780.testBookMarkAnalysis.config.BookmarkConfig;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationTask;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationDomainGroup;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationWorkUnit;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationTaskMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationDomainGroupMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationWorkUnitMapper;

/**
 * Database-backed runner for the full bookmark rebuild. The executor only executes work: task
 * status, progress, retryability and results always live in MySQL.
 */
@Service
public class ResumableReclassificationTaskService {
    private final BookmarkConfig bookmarkConfig;
    private final BookmarkReclassificationPreparationService preparationService;
    private final ReclassificationWorkUnitPlanner planner;
    private final ReclassificationWorkUnitClaimService claimService;
    private final ReclassificationWorkUnitProcessor processor;
    private final ReclassificationTaskControlService controlService;
    private final ReclassificationApplicationService applicationService;
    private final SmallPoolClusteringService smallPoolClusteringService;
    private final AiClassificationTaskMapper taskMapper;
    private final AiReclassificationWorkUnitMapper workUnitMapper;
    private final AiReclassificationDomainGroupMapper domainGroupMapper;
    private final ExecutorService executor;

    public ResumableReclassificationTaskService(
            BookmarkConfig bookmarkConfig,
            BookmarkReclassificationPreparationService preparationService,
            ReclassificationWorkUnitPlanner planner,
            ReclassificationWorkUnitClaimService claimService,
            ReclassificationWorkUnitProcessor processor,
            ReclassificationTaskControlService controlService,
            ReclassificationApplicationService applicationService,
            SmallPoolClusteringService smallPoolClusteringService,
            AiClassificationTaskMapper taskMapper,
            AiReclassificationWorkUnitMapper workUnitMapper,
            AiReclassificationDomainGroupMapper domainGroupMapper) {
        this.bookmarkConfig = bookmarkConfig;
        this.preparationService = preparationService;
        this.planner = planner;
        this.claimService = claimService;
        this.processor = processor;
        this.controlService = controlService;
        this.applicationService = applicationService;
        this.smallPoolClusteringService = smallPoolClusteringService;
        this.taskMapper = taskMapper;
        this.workUnitMapper = workUnitMapper;
        this.domainGroupMapper = domainGroupMapper;
        this.executor = Executors.newSingleThreadExecutor(new ReclassificationThreadFactory());
    }

    @Transactional(rollbackFor = Exception.class)
    public AiClassificationTask start(String apiBaseUrl, String modelName) {
        if (taskMapper.existsActiveTask()) {
            throw new IllegalStateException("已有正在准备或运行中的重分类任务，请先等待其完成或暂停后再开始");
        }
        String resolvedApiBaseUrl = nonBlankOrDefault(apiBaseUrl, bookmarkConfig.getAiApiBaseUrl());
        String resolvedModelName = nonBlankOrDefault(modelName, bookmarkConfig.getAiModelName());
        if (bookmarkConfig.getAiApiKey() == null || bookmarkConfig.getAiApiKey().isBlank()) {
            throw new IllegalStateException("未配置 AI API Key，无法启动重分类任务");
        }

        BookmarkReclassificationPreparationService.PreparedTask prepared =
                preparationService.prepareNewTask(resolvedApiBaseUrl, resolvedModelName);
        planner.planInitialWorkUnits(prepared.taskId());
        if (taskMapper.markTaskRunningIfQueued(prepared.taskId()) != 1) {
            throw new IllegalStateException("重分类任务无法进入运行状态");
        }
        AiClassificationTask task = requireTask(prepared.taskId());
        submitAfterCommit(task.getId());
        return task;
    }

    public AiClassificationTask pause(Long taskId) {
        return controlService.pause(taskId);
    }

    public AiClassificationTask resume(Long taskId) {
        AiClassificationTask task = controlService.resume(taskId);
        submitRunner(task.getId());
        return task;
    }

    @Transactional(rollbackFor = Exception.class)
    public AiClassificationTask continueSmallPool(Long taskId) {
        AiClassificationTask task = requireTask(taskId);
        if (!ReclassificationConstants.TASK_STATUS_COMPLETED.equals(task.getStatus())) {
            throw new IllegalStateException("只有已完成但尚未应用零散书签的任务可以继续小域名池处理");
        }
        int plannedDrafts = smallPoolClusteringService.planClusterDrafts(taskId);
        if (plannedDrafts == 0) {
            throw new IllegalStateException("没有可继续处理的零散书签分析结果");
        }
        if (taskMapper.markTaskRunningForSmallPoolContinuation(taskId) != 1) {
            throw new IllegalStateException("重分类任务状态已变化，请刷新后重试");
        }
        task.setStatus(ReclassificationConstants.TASK_STATUS_RUNNING);
        task.setPhase(ReclassificationConstants.TASK_PHASE_SMALL_CLUSTER_DRAFTS);
        task.setCompletedAt(null);
        submitAfterCommit(taskId);
        return task;
    }

    public AiClassificationTask getTask(Long taskId) {
        return requireTask(taskId);
    }

    public List<AiClassificationTask> listRecoverable() {
        return taskMapper.selectList(
                new LambdaQueryWrapper<AiClassificationTask>()
                        .in(
                                AiClassificationTask::getStatus,
                                ReclassificationConstants.TASK_STATUS_PAUSED,
                                ReclassificationConstants.TASK_STATUS_RECOVERABLE)
                        .orderByDesc(AiClassificationTask::getId));
    }

    public TaskDetails describe(Long taskId) {
        return describe(requireTask(taskId));
    }

    public TaskDetails describe(AiClassificationTask task) {
        int largeDomainGroups =
                Math.toIntExact(
                        domainGroupMapper.selectCount(
                                new LambdaQueryWrapper<AiReclassificationDomainGroup>()
                                        .eq(AiReclassificationDomainGroup::getTaskId, task.getId())
                                        .eq(
                                                AiReclassificationDomainGroup::getGroupType,
                                                ReclassificationConstants
                                                        .SNAPSHOT_POOL_LARGE_DOMAIN)));
        int smallPoolBookmarks =
                domainGroupMapper
                        .selectList(
                                new LambdaQueryWrapper<AiReclassificationDomainGroup>()
                                        .eq(AiReclassificationDomainGroup::getTaskId, task.getId())
                                        .eq(
                                                AiReclassificationDomainGroup::getGroupType,
                                                ReclassificationConstants.SNAPSHOT_POOL_SMALL_POOL))
                        .stream()
                        .map(AiReclassificationDomainGroup::getBookmarkCount)
                        .filter(java.util.Objects::nonNull)
                        .mapToInt(Integer::intValue)
                        .sum();
        int totalWorkUnits = workUnitMapper.countByTask(task.getId());
        int completedWorkUnits = workUnitMapper.countSucceededByTask(task.getId());
        return new TaskDetails(
                task, totalWorkUnits, completedWorkUnits, largeDomainGroups, smallPoolBookmarks);
    }

    private void submitAfterCommit(Long taskId) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            submitRunner(taskId);
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        submitRunner(taskId);
                    }
                });
    }

    private void submitRunner(Long taskId) {
        executor.submit(() -> runTask(taskId));
    }

    private void runTask(Long taskId) {
        while (!Thread.currentThread().isInterrupted()) {
            AiClassificationTask task = taskMapper.selectById(taskId);
            if (task == null
                    || !ReclassificationConstants.TASK_STATUS_RUNNING.equals(task.getStatus())) {
                return;
            }

            AiReclassificationWorkUnit unit = claimService.claimNext(taskId);
            if (unit == null) {
                completeIfNoPendingWork(task);
                return;
            }

            updateTaskPhase(task, phaseFor(unit.getUnitKind()));
            processor.process(unit);
            refreshProgress(taskId);

            AiClassificationTask afterProcessing = taskMapper.selectById(taskId);
            if (afterProcessing == null
                    || !ReclassificationConstants.TASK_STATUS_RUNNING.equals(
                            afterProcessing.getStatus())) {
                return;
            }
        }
    }

    private void completeIfNoPendingWork(AiClassificationTask task) {
        refreshProgress(task.getId());
        int total = workUnitMapper.countByTask(task.getId());
        int completed = workUnitMapper.countSucceededByTask(task.getId());
        if (total == completed) {
            taskMapper.markTaskCompletedIfRunning(task.getId());
        }
    }

    private void refreshProgress(Long taskId) {
        AiClassificationTask task = taskMapper.selectById(taskId);
        if (task == null) {
            return;
        }
        task.setTotalWorkUnitCount(workUnitMapper.countByTask(taskId));
        task.setCompletedWorkUnitCount(workUnitMapper.countSucceededByTask(taskId));
        ReclassificationApplicationService.ApplicationStats applicationStats =
                applicationService.totalsForTask(taskId);
        task.setCreatedFolderCount(applicationStats.createdFolders());
        task.setMovedBookmarkCount(applicationStats.movedBookmarks());
        task.setUpdatedTitleCount(applicationStats.updatedTitles());
        taskMapper.updateById(task);
    }

    private void updateTaskPhase(AiClassificationTask task, String phase) {
        if (phase.equals(task.getPhase())) {
            return;
        }
        task.setPhase(phase);
        taskMapper.updateById(task);
    }

    private String phaseFor(String unitKind) {
        return switch (unitKind) {
            case ReclassificationConstants.UNIT_LARGE_DOMAIN_FOLDER_NAME,
                    ReclassificationConstants.UNIT_LARGE_DOMAIN_BOOKMARK_ANALYSIS ->
                    ReclassificationConstants.TASK_PHASE_LARGE_DOMAINS;
            case ReclassificationConstants.UNIT_SMALL_POOL_BOOKMARK_ANALYSIS ->
                    ReclassificationConstants.TASK_PHASE_SMALL_ANALYSIS;
            case ReclassificationConstants.UNIT_SMALL_POOL_CLUSTER_DRAFT ->
                    ReclassificationConstants.TASK_PHASE_SMALL_CLUSTER_DRAFTS;
            case ReclassificationConstants.UNIT_SMALL_POOL_CANONICALIZE_FOLDERS ->
                    ReclassificationConstants.TASK_PHASE_SMALL_CANONICALIZATION;
            default -> ReclassificationConstants.TASK_PHASE_PREPARING;
        };
    }

    private AiClassificationTask requireTask(Long taskId) {
        AiClassificationTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("重分类任务不存在: " + taskId);
        }
        return task;
    }

    private String nonBlankOrDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    @PreDestroy
    void shutdown() {
        executor.shutdownNow();
    }

    public record TaskDetails(
            AiClassificationTask task,
            int totalWorkUnits,
            int completedWorkUnits,
            int largeDomainGroups,
            int smallPoolBookmarks) {}

    private static final class ReclassificationThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "bookmark-reclassification-worker");
            thread.setDaemon(true);
            return thread;
        }
    }
}
