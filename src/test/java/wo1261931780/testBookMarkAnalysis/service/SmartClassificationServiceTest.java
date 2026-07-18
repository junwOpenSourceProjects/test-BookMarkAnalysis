package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import cn.hutool.json.JSONUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;

@ExtendWith(MockitoExtension.class)
class SmartClassificationServiceTest {

    @Mock private BookMarksService bookMarksService;
    @Mock private AiClientService aiClient;
    @InjectMocks private SmartClassificationService smartClassificationService;

    @Test
    void classifiesAiBatchesConcurrently() throws Exception {
        List<BookMarks> bookmarks = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (long id = 1; id <= 75; id++) {
            BookMarks bookmark = new BookMarks();
            bookmark.setId(id);
            bookmark.setHref("https://unmatched-" + id + ".invalid/article");
            bookmark.setTitle("待补全标题");
            bookmark.setType("a");
            bookmarks.add(bookmark);
            ids.add(id);
        }

        BookMarks folder = new BookMarks();
        folder.setId(1000L);
        folder.setTitle("开发工具");
        folder.setType("h3");

        when(bookMarksService.listByIds(any())).thenReturn(bookmarks);
        when(bookMarksService.list(any(Wrapper.class))).thenReturn(List.of(folder));

        AtomicInteger inFlight = new AtomicInteger();
        AtomicInteger maxInFlight = new AtomicInteger();
        CountDownLatch startedBatches = new CountDownLatch(3);
        CountDownLatch releaseBatches = new CountDownLatch(1);

        when(aiClient.chat(anyString(), anyString(), anyDouble(), anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> {
                    int active = inFlight.incrementAndGet();
                    maxInFlight.accumulateAndGet(active, Math::max);
                    startedBatches.countDown();
                    if (startedBatches.getCount() == 0) {
                        releaseBatches.countDown();
                    }

                    try {
                        releaseBatches.await(1, TimeUnit.SECONDS);
                        String userContent = invocation.getArgument(1, String.class);
                        String bookmarkJson = userContent.substring(userContent.indexOf('['));
                        JSONArray requestBookmarks = JSONUtil.parseArray(bookmarkJson);
                        JSONArray reply = new JSONArray();
                        for (int i = 0; i < requestBookmarks.size(); i++) {
                            String bookmarkId = requestBookmarks.getJSONObject(i).getStr("id");
                            reply.add(JSONUtil.createObj()
                                    .set("bookmarkId", bookmarkId)
                                    .set("suggestedTitle", "AI 标题 " + bookmarkId)
                                    .set("suggestedFolder", "开发工具")
                                    .set("confidence", 90));
                        }
                        return reply;
                    } finally {
                        inFlight.decrementAndGet();
                    }
                });

        Map<String, Object> result = smartClassificationService.classify(
                "function", ids, true, "https://example.test", "test-key", "gpt-5.6-terra");

        assertEquals(75, result.get("aiMatched"));
        assertEquals(0, result.get("unmatched"));
        assertTrue(maxInFlight.get() >= 3, "AI 批次应并行执行以降低总等待时间");
        verify(bookMarksService, times(1)).list(any(Wrapper.class));
    }
}
