package wo1261931780.testBookMarkAnalysis.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;
import wo1261931780.testBookMarkAnalysis.entity.LinkCheckReport;
import wo1261931780.testBookMarkAnalysis.entity.LinkCheckResult;
import wo1261931780.testBookMarkAnalysis.service.BookmarksParserService;
import wo1261931780.testBookMarkAnalysis.service.LinkCheckService;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.service.impl
 *
 * @author liujiajun_junw
 * @Date 2026-01-04
 * @Description 链接检测服务实现类
 */
@Slf4j
@Service
public class LinkCheckServiceImpl implements LinkCheckService {

    /**
     * 连接超时时间（毫秒）
     */
    private static final int CONNECT_TIMEOUT = 5000;

    /**
     * 读取超时时间（毫秒）
     */
    private static final int READ_TIMEOUT = 10000;

    /**
     * 并发检测线程数
     */
    private static final int THREAD_POOL_SIZE = 10;

    /**
     * 存储异步任务进度
     */
    private final Map<String, LinkCheckReport> taskProgressMap = new ConcurrentHashMap<>();

    @Autowired
    private BookmarksParserService bookmarksParserService;

    /**
     * 线程池用于并发检测
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    @Override
    public LinkCheckResult checkLink(String url) {
        if (StrUtil.isEmpty(url)) {
            return LinkCheckResult.builder()
                    .url(url)
                    .accessible(false)
                    .statusCode(-1)
                    .errorMessage("URL为空")
                    .build();
        }

        long startTime = System.currentTimeMillis();
        HttpURLConnection connection = null;

        try {
            URI uri = new URI(url);
            URL urlObj = uri.toURL();
            connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("HEAD"); // 使用HEAD请求，更快
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

            int statusCode = connection.getResponseCode();
            long responseTime = System.currentTimeMillis() - startTime;

            // 2xx和3xx状态码认为可访问
            boolean accessible = statusCode >= 200 && statusCode < 400;

            return LinkCheckResult.builder()
                    .url(url)
                    .statusCode(statusCode)
                    .accessible(accessible)
                    .responseTimeMs(responseTime)
                    .errorMessage(accessible ? null : "HTTP状态码: " + statusCode)
                    .build();

        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            log.debug("检测链接失败: {} - {}", url, e.getMessage());

            return LinkCheckResult.builder()
                    .url(url)
                    .statusCode(-1)
                    .accessible(false)
                    .responseTimeMs(responseTime)
                    .errorMessage(e.getClass().getSimpleName() + ": " + e.getMessage())
                    .build();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    public LinkCheckReport checkLinks(List<String> urls) {
        if (urls == null || urls.isEmpty()) {
            return LinkCheckReport.builder()
                    .totalChecked(0)
                    .accessibleCount(0)
                    .brokenCount(0)
                    .brokenLinks(new ArrayList<>())
                    .status("COMPLETED")
                    .build();
        }

        long startTime = System.currentTimeMillis();
        List<LinkCheckResult> brokenLinks = new ArrayList<>();
        int accessibleCount = 0;

        // 使用并发检测
        List<CompletableFuture<LinkCheckResult>> futures = urls.stream()
                .map(url -> CompletableFuture.supplyAsync(() -> checkLink(url), executorService))
                .collect(Collectors.toList());

        // 等待所有任务完成
        List<LinkCheckResult> results = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        for (LinkCheckResult result : results) {
            if (result.isAccessible()) {
                accessibleCount++;
            } else {
                brokenLinks.add(result);
            }
        }

        return LinkCheckReport.builder()
                .totalChecked(urls.size())
                .accessibleCount(accessibleCount)
                .brokenCount(brokenLinks.size())
                .brokenLinks(brokenLinks)
                .checkTimeMs(System.currentTimeMillis() - startTime)
                .status("COMPLETED")
                .progress(100)
                .build();
    }

    @Override
    @Async
    public CompletableFuture<LinkCheckReport> checkAllLinksAsync() {
        String taskId = IdUtil.fastSimpleUUID();

        // 初始化任务进度
        LinkCheckReport initialReport = LinkCheckReport.builder()
                .taskId(taskId)
                .status("RUNNING")
                .progress(0)
                .totalChecked(0)
                .accessibleCount(0)
                .brokenCount(0)
                .brokenLinks(new ArrayList<>())
                .build();
        taskProgressMap.put(taskId, initialReport);

        return CompletableFuture.supplyAsync(() -> {
            try {
                // 查询所有链接类型的书签
                List<BookMarks> bookmarks = bookmarksParserService.list().stream()
                        .filter(b -> "a".equals(b.getType()) && StrUtil.isNotEmpty(b.getHref()))
                        .collect(Collectors.toList());

                int total = bookmarks.size();
                List<LinkCheckResult> brokenLinks = new ArrayList<>();
                int accessibleCount = 0;
                long startTime = System.currentTimeMillis();

                for (int i = 0; i < bookmarks.size(); i++) {
                    BookMarks bookmark = bookmarks.get(i);
                    LinkCheckResult result = checkLink(bookmark.getHref());
                    result.setBookmarkId(bookmark.getId());
                    result.setTitle(bookmark.getTitle());

                    if (result.isAccessible()) {
                        accessibleCount++;
                    } else {
                        brokenLinks.add(result);
                    }

                    // 更新进度
                    int progress = (int) ((i + 1) * 100.0 / total);
                    LinkCheckReport progressReport = LinkCheckReport.builder()
                            .taskId(taskId)
                            .status("RUNNING")
                            .progress(progress)
                            .totalChecked(i + 1)
                            .accessibleCount(accessibleCount)
                            .brokenCount(brokenLinks.size())
                            .brokenLinks(new ArrayList<>(brokenLinks))
                            .checkTimeMs(System.currentTimeMillis() - startTime)
                            .build();
                    taskProgressMap.put(taskId, progressReport);
                }

                // 完成报告
                LinkCheckReport finalReport = LinkCheckReport.builder()
                        .taskId(taskId)
                        .status("COMPLETED")
                        .progress(100)
                        .totalChecked(total)
                        .accessibleCount(accessibleCount)
                        .brokenCount(brokenLinks.size())
                        .brokenLinks(brokenLinks)
                        .checkTimeMs(System.currentTimeMillis() - startTime)
                        .build();
                taskProgressMap.put(taskId, finalReport);

                log.info("异步链接检测完成，任务ID: {}, 总数: {}, 失效: {}",
                        taskId, total, brokenLinks.size());

                return finalReport;

            } catch (Exception e) {
                log.error("异步链接检测失败: {}", e.getMessage(), e);
                LinkCheckReport errorReport = LinkCheckReport.builder()
                        .taskId(taskId)
                        .status("FAILED")
                        .progress(0)
                        .build();
                taskProgressMap.put(taskId, errorReport);
                throw new RuntimeException("链接检测失败: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public LinkCheckReport checkAllLinks(int limit) {
        // 查询所有链接类型的书签
        List<BookMarks> bookmarks = bookmarksParserService.list().stream()
                .filter(b -> "a".equals(b.getType()) && StrUtil.isNotEmpty(b.getHref()))
                .collect(Collectors.toList());

        // 限制数量
        if (limit > 0 && bookmarks.size() > limit) {
            bookmarks = bookmarks.subList(0, limit);
        }

        List<String> urls = bookmarks.stream()
                .map(BookMarks::getHref)
                .collect(Collectors.toList());

        LinkCheckReport report = checkLinks(urls);

        // 补充书签ID和标题信息
        Map<String, BookMarks> urlToBookmark = bookmarks.stream()
                .collect(Collectors.toMap(BookMarks::getHref, b -> b, (a, b) -> a));

        report.getBrokenLinks().forEach(result -> {
            BookMarks bookmark = urlToBookmark.get(result.getUrl());
            if (bookmark != null) {
                result.setBookmarkId(bookmark.getId());
                result.setTitle(bookmark.getTitle());
            }
        });

        return report;
    }

    @Override
    public LinkCheckReport getCheckProgress(String taskId) {
        LinkCheckReport report = taskProgressMap.get(taskId);
        if (report == null) {
            return LinkCheckReport.builder()
                    .taskId(taskId)
                    .status("NOT_FOUND")
                    .build();
        }
        return report;
    }
}
