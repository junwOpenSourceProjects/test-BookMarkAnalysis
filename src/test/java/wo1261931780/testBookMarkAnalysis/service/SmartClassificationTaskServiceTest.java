package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationResultMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationTaskMapper;

@ExtendWith(MockitoExtension.class)
class SmartClassificationTaskServiceTest {

    @Mock private SmartClassificationService smartClassificationService;
    @Mock private AiClassificationTaskMapper taskMapper;
    @Mock private AiClassificationResultMapper resultMapper;
    @InjectMocks private SmartClassificationTaskService taskService;

    @AfterEach
    void tearDown() {
        taskService.shutdown();
    }

    @Test
    void runsClassificationInBackgroundAndReturnsLimitedPreview() throws Exception {
        List<Map<String, Object>> results = List.of(
                Map.of("bookmarkId", "1", "suggestedFolder", "开发工具"),
                Map.of("bookmarkId", "2", "suggestedFolder", "新闻资讯"));
        doAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            Consumer<SmartClassificationService.ClassificationProgress> progressConsumer =
                    invocation.getArgument(6, Consumer.class);
            progressConsumer.accept(new SmartClassificationService.ClassificationProgress(2, 1, 0, 1, 0, 1));
            progressConsumer.accept(new SmartClassificationService.ClassificationProgress(2, 1, 1, 0, 1, 1));
            return Map.of(
                    "total", 2,
                    "ruleMatched", 1,
                    "aiMatched", 1,
                    "unmatched", 0,
                    "results", results);
        }).when(smartClassificationService).classify(
                anyString(), anyList(), anyBoolean(), anyString(), anyString(), anyString(), any());

        Map<String, Object> initial = taskService.startTask(
                "function", List.of(1L, 2L), true, "https://example.test", "test-key", "gpt-5.6-terra");
        String taskId = (String) initial.get("taskId");
        assertEquals("QUEUED", initial.get("status"));

        Map<String, Object> status = waitForCompletion(taskId);
        assertEquals("COMPLETED", status.get("status"));
        assertEquals(1, status.get("completedBatches"));
        assertEquals(2, status.get("resultTotal"));

        Map<String, Object> preview = taskService.getTaskResult(taskId, 1);
        assertNotNull(preview);
        assertEquals(2, preview.get("resultTotal"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> previewItems = (List<Map<String, Object>>) preview.get("results");
        assertEquals(1, previewItems.size());
    }

    private Map<String, Object> waitForCompletion(String taskId) throws InterruptedException {
        for (int attempt = 0; attempt < 100; attempt++) {
            Map<String, Object> status = taskService.getTaskStatus(taskId);
            assertNotNull(status);
            if ("COMPLETED".equals(status.get("status"))) {
                return status;
            }
            Thread.sleep(10);
        }
        throw new AssertionError("classification task did not complete in time");
    }
}
