package wo1261931780.testBookMarkAnalysis.service;

import cn.hutool.core.util.IdUtil;
import jakarta.annotation.PreDestroy;
import cn.hutool.json.JSONUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationResult;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationTask;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationResultMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationTaskMapper;

/**
 * 智能分类后台任务服务。
 *
 * <p>分类任务按顺序进入单一任务队列；单个任务内部最多并行 3 个 AI 批次，
 * 从而在提升吞吐的同时避免多个大任务叠加压垮远程 AI API。</p>
 */
@Service
public class SmartClassificationTaskService {

    private static final int PREVIEW_LIMIT_DEFAULT = 200;
    private static final long TASK_RETENTION_MS = 60 * 60 * 1000L;

    private final Map<String, ClassificationTask> tasks = new ConcurrentHashMap<>();
    private final ExecutorService taskExecutor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "smart-classification-task-worker");
        thread.setDaemon(true);
        return thread;
    });

    @Autowired private SmartClassificationService smartClassificationService;
    @Autowired private AiClassificationTaskMapper taskMapper;
    @Autowired private AiClassificationResultMapper resultMapper;

    public Map<String, Object> startTask(
            String strategy,
            List<Long> bookmarkIds,
            boolean useAI,
            String apiBaseUrl,
            String apiKey,
            String modelName) {
        cleanupExpiredTasks();
        Long persistedTaskId = IdUtil.getSnowflakeNextId();
        String taskId = persistedTaskId.toString();
        ClassificationTask task = new ClassificationTask(taskId, persistedTaskId);
        tasks.put(taskId, task);
        AiClassificationTask persistentTask = new AiClassificationTask();
        persistentTask.setId(persistedTaskId);
        persistentTask.setStrategy(strategy);
        persistentTask.setModelName(modelName);
        persistentTask.setStatus("QUEUED");
        persistentTask.setTotalCount(0);
        persistentTask.setRuleMatchedCount(0);
        persistentTask.setAiMatchedCount(0);
        persistentTask.setFailedCount(0);
        taskMapper.insert(persistentTask);

        taskExecutor.submit(() -> runTask(task, strategy, bookmarkIds, useAI, apiBaseUrl, apiKey, modelName));
        return snapshot(task, false, 0);
    }

    public Map<String, Object> getTaskStatus(String taskId) {
        cleanupExpiredTasks();
        ClassificationTask task = tasks.get(taskId);
        return task == null ? null : snapshot(task, false, 0);
    }

    public Map<String, Object> getTaskResult(String taskId, int previewLimit) {
        cleanupExpiredTasks();
        ClassificationTask task = tasks.get(taskId);
        if (task == null) {
            return null;
        }
        int limit = previewLimit > 0 ? Math.min(previewLimit, 500) : PREVIEW_LIMIT_DEFAULT;
        return snapshot(task, true, limit);
    }

    public Map<String, Object> applyTaskResults(String taskId) {
        ClassificationTask task = tasks.get(taskId);
        if (task == null) {
            return null;
        }
        if (!"COMPLETED".equals(task.status)) {
            throw new IllegalStateException("分类任务尚未完成");
        }
        return smartClassificationService.applyResults(task.results);
    }

    private void runTask(
            ClassificationTask task,
            String strategy,
            List<Long> bookmarkIds,
            boolean useAI,
            String apiBaseUrl,
            String apiKey,
            String modelName) {
        task.status = "RUNNING";
        task.startedAt = System.currentTimeMillis();
        updatePersistentTask(task, "RUNNING", null);
        try {
            Map<String, Object> result = smartClassificationService.classify(
                    strategy,
                    bookmarkIds,
                    useAI,
                    apiBaseUrl,
                    apiKey,
                    modelName,
                    progress -> updateProgress(task, progress));

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> results = (List<Map<String, Object>>) result.getOrDefault("results", List.of());
            task.results = List.copyOf(results);
            task.total = ((Number) result.getOrDefault("total", 0)).intValue();
            task.ruleMatched = ((Number) result.getOrDefault("ruleMatched", 0)).intValue();
            task.aiMatched = ((Number) result.getOrDefault("aiMatched", 0)).intValue();
            task.unmatched = ((Number) result.getOrDefault("unmatched", 0)).intValue();
            persistResults(task.persistedTaskId, results);
            Map<String, Object> applyStats = smartClassificationService.applyResults(results);
            markResultsApplied(task.persistedTaskId);
            task.status = "COMPLETED";
            task.completedAt = System.currentTimeMillis();
            updatePersistentTask(task, "COMPLETED", null);
        } catch (Exception e) {
            task.status = "FAILED";
            task.errorMessage = e.getMessage();
            task.completedAt = System.currentTimeMillis();
            updatePersistentTask(task, "FAILED", e.getMessage());
        }
    }

    private void persistResults(Long taskId, List<Map<String, Object>> results) {
        for (Map<String, Object> result : results) {
            AiClassificationResult entity = new AiClassificationResult();
            entity.setId(IdUtil.getSnowflakeNextId());
            entity.setTaskId(taskId);
            entity.setBookmarkId(Long.valueOf(String.valueOf(result.get("bookmarkId"))));
            entity.setOriginalTitle((String) result.get("originalTitle"));
            entity.setSuggestedTitle((String) result.get("suggestedTitle"));
            entity.setSuggestedFolder((String) result.get("suggestedFolder"));
            entity.setKeywords(result.get("keywords") == null ? null : JSONUtil.toJsonStr(result.get("keywords")));
            entity.setPageType((String) result.get("pageType"));
            entity.setConfidence((Integer) result.get("confidence"));
            entity.setReason((String) result.get("aiReason"));
            entity.setSource((String) result.get("source"));
            entity.setStatus("PENDING");
            resultMapper.insert(entity);
        }
    }

    private void markResultsApplied(Long taskId) {
        List<AiClassificationResult> results = resultMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AiClassificationResult>()
                        .eq(AiClassificationResult::getTaskId, taskId));
        for (AiClassificationResult result : results) {
            result.setStatus("APPLIED");
            result.setAppliedAt(LocalDateTime.now());
            resultMapper.updateById(result);
        }
    }

    private void updatePersistentTask(ClassificationTask task, String status, String errorMessage) {
        AiClassificationTask persistentTask = new AiClassificationTask();
        persistentTask.setId(task.persistedTaskId);
        persistentTask.setStatus(status);
        persistentTask.setTotalCount(task.total);
        persistentTask.setRuleMatchedCount(task.ruleMatched);
        persistentTask.setAiMatchedCount(task.aiMatched);
        persistentTask.setFailedCount(Math.max(0, task.unmatched));
        persistentTask.setStartedAt(task.startedAt == 0 ? null : LocalDateTime.now());
        if (task.completedAt > 0) persistentTask.setCompletedAt(LocalDateTime.now());
        persistentTask.setErrorMessage(errorMessage);
        taskMapper.updateById(persistentTask);
    }

    private void updateProgress(
            ClassificationTask task, SmartClassificationService.ClassificationProgress progress) {
        task.total = progress.total();
        task.ruleMatched = progress.ruleMatched();
        task.aiMatched = progress.aiMatched();
        task.unmatched = progress.unmatched();
        task.completedBatches = progress.completedBatches();
        task.totalBatches = progress.totalBatches();
        updatePersistentTask(task, "RUNNING", null);
    }

    private Map<String, Object> snapshot(ClassificationTask task, boolean includeResults, int previewLimit) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("taskId", task.taskId);
        result.put("status", task.status);
        result.put("total", task.total);
        result.put("ruleMatched", task.ruleMatched);
        result.put("aiMatched", task.aiMatched);
        result.put("unmatched", task.unmatched);
        result.put("completedBatches", task.completedBatches);
        result.put("totalBatches", task.totalBatches);
        result.put("createdAt", task.createdAt);
        result.put("startedAt", task.startedAt);
        result.put("completedAt", task.completedAt);
        result.put("errorMessage", task.errorMessage);
        result.put("resultTotal", task.results.size());
        if (includeResults && "COMPLETED".equals(task.status)) {
            result.put("results", new ArrayList<>(task.results.subList(0, Math.min(previewLimit, task.results.size()))));
        }
        return result;
    }

    private void cleanupExpiredTasks() {
        long now = System.currentTimeMillis();
        tasks.entrySet().removeIf(entry -> {
            ClassificationTask task = entry.getValue();
            return task.completedAt > 0 && now - task.completedAt > TASK_RETENTION_MS;
        });
    }

    @PreDestroy
    void shutdown() {
        taskExecutor.shutdownNow();
    }

    private static final class ClassificationTask {
        private final String taskId;
        private final Long persistedTaskId;
        private final long createdAt = System.currentTimeMillis();
        private volatile String status = "QUEUED";
        private volatile int total;
        private volatile int ruleMatched;
        private volatile int aiMatched;
        private volatile int unmatched;
        private volatile int completedBatches;
        private volatile int totalBatches;
        private volatile long startedAt;
        private volatile long completedAt;
        private volatile String errorMessage;
        private volatile List<Map<String, Object>> results = List.of();

        private ClassificationTask(String taskId, Long persistedTaskId) {
            this.taskId = taskId;
            this.persistedTaskId = persistedTaskId;
        }
    }
}
