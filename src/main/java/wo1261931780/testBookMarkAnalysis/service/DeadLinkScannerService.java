package wo1261931780.testBookMarkAnalysis.service;

import org.springframework.stereotype.Service;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DeadLinkScannerService {

    private final AtomicInteger totalLinks = new AtomicInteger(0);
    private final AtomicInteger processedLinks = new AtomicInteger(0);
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final List<Map<String, Object>> brokenLinks = Collections.synchronizedList(new ArrayList<>());

    public boolean startScan(List<BookMarks> links) {
        if (!isRunning.compareAndSet(false, true)) {
            return false; // Already running
        }

        totalLinks.set(links.size());
        processedLinks.set(0);
        brokenLinks.clear();

        // 核心：在 JDK 17 环境下，使用 FixedThreadPool 限制最多 40 个并发。
        // 其内部的 LinkedBlockingQueue 原生充当了排队缓冲，既保证了极高的吞吐上限，又杜绝了操作系统被海量 HTTP 连接同时压垮发生活锁。
        // 这完美替代了 Virtual Threads 与 Disruptor 带来的重型架构依赖，保持了轻量级。
        ExecutorService executor = Executors.newFixedThreadPool(40);
        
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        for (BookMarks link : links) {
            String href = link.getHref();
            if (href == null || href.trim().isEmpty() || !href.startsWith("http")) {
                processedLinks.incrementAndGet();
                continue;
            }

            executor.submit(() -> {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(href))
                            .timeout(Duration.ofSeconds(10))
                            // 使用 GET 测试，防止被部分服务器拦截 HEAD
                            .GET()
                            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                            .build();

                    HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
                    int status = response.statusCode();

                    // 403 Forbidden 往往是防爬虫拦截，401 是未授权，不代表死链。
                    if (status >= 400 && status < 600 && status != 403 && status != 401 && status != 405) {
                        addBrokenLink(link, "HTTP 状态码异常: " + status);
                    }
                } catch (IllegalArgumentException e) {
                    addBrokenLink(link, "URL 格式错误不可达");
                } catch (Exception e) {
                    addBrokenLink(link, "域名解析失败或连接超时");
                } finally {
                    processedLinks.incrementAndGet();
                }
            });
        }

        // 启动一个后台守护线程等待全部执行完毕并归位状态，防止阻塞主线程
        Thread watcherThread = new Thread(() -> {
            executor.shutdown();
            try {
                // 等待最多两个小时
                executor.awaitTermination(2, TimeUnit.HOURS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                isRunning.set(false);
            }
        });
        watcherThread.setName("DeadLinkWatcher-Thread");
        watcherThread.setDaemon(true);
        watcherThread.start();

        return true;
    }

    private void addBrokenLink(BookMarks link, String reason) {
        Map<String, Object> broken = new HashMap<>();
        // Note: Using toString() to map big snow-flake ID to string to prevent frontend JS truncation
        broken.put("id", link.getId() != null ? link.getId().toString() : "0");
        broken.put("title", link.getTitle());
        broken.put("href", link.getHref());
        broken.put("reason", reason);
        brokenLinks.add(broken);
    }

    public Map<String, Object> getProgress() {
        Map<String, Object> progress = new HashMap<>();
        progress.put("running", isRunning.get());
        progress.put("total", totalLinks.get());
        progress.put("processed", processedLinks.get());
        // Return a copy of the broken links list
        progress.put("brokenLinks", new ArrayList<>(brokenLinks));
        return progress;
    }
}
