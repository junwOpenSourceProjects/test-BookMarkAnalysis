package wo1261931780.testBookMarkAnalysis.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;

/**
 * 智能分类服务 — 规则引擎 + AI 回退
 * <p>
 * 流水线：规则引擎先分类 → 未匹配的调 AI（标题补全 + 分类一步完成）
 * 返回结果带置信度，低置信度标记供人工审核。
 */
@Service
public class SmartClassificationService {

    private static final int AI_BATCH_SIZE = 25;
    private static final int AI_MAX_CONCURRENCY = 3;

    @Autowired private BookMarksService bookMarksService;
    @Autowired private AiClientService aiClient;

    public record ClassificationProgress(
            int total,
            int ruleMatched,
            int aiMatched,
            int unmatched,
            int completedBatches,
            int totalBatches) {}

    /**
     * 执行完整分类流水线
     *
     * @param strategy    分类策略：function / domain / region
     * @param bookmarkIds 书签 ID 列表（null 则处理全部链接类型）
     * @param useAI       是否对未匹配项调用 AI
     * @param apiBaseUrl  AI API 地址
     * @param apiKey      API Key
     * @param modelName   模型名称
     * @return 分类结果列表
     */
    public Map<String, Object> classify(
            String strategy, List<Long> bookmarkIds, boolean useAI,
            String apiBaseUrl, String apiKey, String modelName) throws Exception {
        return classify(strategy, bookmarkIds, useAI, apiBaseUrl, apiKey, modelName, null);
    }

    /**
     * 执行分类并在规则和每个 AI 批次完成后发布进度。
     */
    public Map<String, Object> classify(
            String strategy, List<Long> bookmarkIds, boolean useAI,
            String apiBaseUrl, String apiKey, String modelName,
            Consumer<ClassificationProgress> progressConsumer) throws Exception {

        // 加载书签
        List<BookMarks> bookmarks;
        if (bookmarkIds != null && !bookmarkIds.isEmpty()) {
            bookmarks = bookMarksService.listByIds(bookmarkIds);
        } else {
            bookmarks = bookMarksService.list(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BookMarks>()
                            .eq(BookMarks::getType, "a"));
        }

        List<Map<String, Object>> results = new ArrayList<>();
        List<BookMarks> unmatched = new ArrayList<>();
        int ruleMatched = 0;

        // 第1层：规则引擎
        for (BookMarks bm : bookmarks) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("bookmarkId", bm.getId() != null ? bm.getId().toString() : "0");
            item.put("url", bm.getHref());
            item.put("originalTitle", bm.getTitle());
            item.put("needsTitle", DomainCategoryMapper.needsTitleGeneration(bm.getTitle()));

            String folderName = classifyByStrategy(strategy, bm);
            item.put("suggestedFolder", folderName);
            item.put("source", folderName != null ? "rule" : "unmatched");
            item.put("confidence", folderName != null ? 95 : 0);

            if (folderName != null) {
                ruleMatched++;
            } else {
                unmatched.add(bm);
            }
            results.add(item);
        }

        // 第2层：AI 回退（每批 25 条，最多 3 个批次并行）
        int aiMatched = 0;
        int totalBatches = 0;
        if (useAI && !unmatched.isEmpty() && apiKey != null && !apiKey.isBlank()) {
            String folderListJson = loadFolderListJson();
            List<List<BookMarks>> batches = new ArrayList<>();
            for (int batchStart = 0; batchStart < unmatched.size(); batchStart += AI_BATCH_SIZE) {
                int batchEnd = Math.min(batchStart + AI_BATCH_SIZE, unmatched.size());
                batches.add(new ArrayList<>(unmatched.subList(batchStart, batchEnd)));
            }
            totalBatches = batches.size();
            publishProgress(progressConsumer, bookmarks.size(), ruleMatched, 0,
                    unmatched.size(), 0, totalBatches);

            ExecutorService executor = Executors.newFixedThreadPool(
                    Math.min(AI_MAX_CONCURRENCY, batches.size()));
            try {
                List<Future<List<Map<String, Object>>>> futures = new ArrayList<>();
                for (List<BookMarks> batch : batches) {
                    futures.add(executor.submit(() -> aiClassifyBatch(
                            strategy, batch, folderListJson, apiBaseUrl, apiKey, modelName)));
                }

                Map<String, Map<String, Object>> aiMap = new LinkedHashMap<>();
                int completedBatches = 0;
                for (Future<List<Map<String, Object>>> future : futures) {
                    for (Map<String, Object> aiResult : future.get()) {
                        aiMap.put((String) aiResult.get("bookmarkId"), aiResult);
                    }
                    completedBatches++;
                    publishProgress(progressConsumer, bookmarks.size(), ruleMatched, aiMap.size(),
                            Math.max(0, unmatched.size() - aiMap.size()), completedBatches, totalBatches);
                }

                for (Map<String, Object> item : results) {
                    if (!"unmatched".equals(item.get("source"))) {
                        continue;
                    }
                    Map<String, Object> aiResult = aiMap.get(item.get("bookmarkId"));
                    if (aiResult != null) {
                        item.put("suggestedTitle", aiResult.get("suggestedTitle"));
                        item.put("suggestedFolder", aiResult.get("suggestedFolder"));
                        item.put("keywords", aiResult.get("keywords"));
                        item.put("pageType", aiResult.get("pageType"));
                        item.put("confidence", aiResult.getOrDefault("confidence", 60));
                        item.put("source", "ai");
                        item.put("aiReason", aiResult.get("reason"));
                        aiMatched++;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("AI 批量分类被中断", e);
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                if (cause instanceof Exception exception) {
                    throw exception;
                }
                throw new RuntimeException("AI 批量分类失败", cause);
            } finally {
                executor.shutdown();
            }
        }
        publishProgress(progressConsumer, bookmarks.size(), ruleMatched, aiMatched,
                bookmarks.size() - ruleMatched - aiMatched, totalBatches, totalBatches);

        Map<String, Object> wrapper = new LinkedHashMap<>();
        wrapper.put("strategy", strategy);
        wrapper.put("total", bookmarks.size());
        wrapper.put("ruleMatched", ruleMatched);
        wrapper.put("aiMatched", aiMatched);
        wrapper.put("unmatched", bookmarks.size() - ruleMatched - aiMatched);
        wrapper.put("results", results);
        return wrapper;
    }

    private void publishProgress(
            Consumer<ClassificationProgress> progressConsumer,
            int total,
            int ruleMatched,
            int aiMatched,
            int unmatched,
            int completedBatches,
            int totalBatches) {
        if (progressConsumer != null) {
            progressConsumer.accept(new ClassificationProgress(
                    total, ruleMatched, aiMatched, unmatched, completedBatches, totalBatches));
        }
    }

    private String classifyByStrategy(String strategy, BookMarks bm) {
        switch (strategy) {
            case "domain":
                return DomainCategoryMapper.classifyByDomain(bm.getHref());
            case "region":
                return DomainCategoryMapper.classifyByRegion(bm.getHref(), bm.getTitle());
            case "function":
            default:
                return DomainCategoryMapper.classifyByFunction(bm.getHref());
        }
    }

    /**
     * AI 批量分类 — 标题补全 + 分类一步完成
     */
    private String loadFolderListJson() {
        List<BookMarks> folders = bookMarksService.list(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BookMarks>()
                        .eq(BookMarks::getType, "h3"));
        JSONArray folderList = new JSONArray();
        for (BookMarks folder : folders) {
            JSONObject item = new JSONObject();
            item.set("id", folder.getId() != null ? folder.getId().toString() : "0");
            item.set("name", folder.getTitle());
            folderList.add(item);
        }
        return folderList.toString();
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> aiClassifyBatch(
            String strategy, List<BookMarks> bookmarks, String folderListJson,
            String apiBaseUrl, String apiKey, String modelName) throws Exception {

        JSONArray bookmarkList = new JSONArray();
        for (BookMarks bm : bookmarks) {
            JSONObject obj = new JSONObject();
            obj.set("id", bm.getId() != null ? bm.getId().toString() : "0");
            obj.set("url", bm.getHref());
            obj.set("originalTitle", bm.getTitle() != null ? bm.getTitle() : "");
            obj.set("domain", DomainCategoryMapper.extractDomain(bm.getHref()));
            bookmarkList.add(obj);
        }

        String strategyDesc = switch (strategy) {
            case "domain" -> "按域名来源分组（如 github.com、zhihu.com 等）";
            case "region" -> "按国内外分组（国内 / 国外）";
            default -> "按网站功能分组（开发工具、视频娱乐、新闻资讯等）";
        };

        String systemPrompt = """
                你是一个书签整理助手。请同时完成两项任务：为书签生成完整标题，并归类到合适的文件夹。

                ## 分类策略
                %s

                ## 现有文件夹
                %s

                ## 标题命名规则
                1. 从 URL 路径提取关键词，从域名推断来源
                2. 格式：「核心内容」-「使用场景」
                3. 长度：15-40 字，中文为主
                4. 场景标注：官方文档/教程/工具/论文/讨论/博客/项目/新闻/其他

                ## 输出格式
                严格返回 JSON 数组，不含 markdown 标记：
                [{
                  "bookmarkId": "书签ID",
                  "suggestedTitle": "完整标题",
                  "keywords": ["关键词1", "关键词2"],
                  "pageType": "页面类型",
                  "suggestedFolder": "文件夹名（优先匹配现有文件夹，否则建议新名）",
                  "isNewFolder": true/false,
                  "confidence": 60-95,
                  "reason": "一句话归因"
                }]

                ## 置信度标准
                - 90-95：URL 和内容明确，归类清晰
                - 70-89：URL 可推断但不够明确
                - 50-69：信息不足，需人工确认
                """.formatted(strategyDesc, folderListJson);

        String userContent = "【待分类书签列表】：\n" + bookmarkList.toString();

        JSONArray replyArray = aiClient.chat(
                systemPrompt, userContent, 0.3, apiBaseUrl, apiKey, modelName);

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (int i = 0; i < replyArray.size(); i++) {
            resultList.add((Map<String, Object>) replyArray.getJSONObject(i).toBean(Map.class));
        }

        return resultList;
    }

    /**
     * 应用分类结果（创建文件夹 + 更新标题 + 设置 parentId）
     */
    public Map<String, Object> applyResults(List<Map<String, Object>> results) {
        int createdFolders = 0;
        int updatedTitles = 0;
        int movedBookmarks = 0;
        Map<String, Long> folderNameToId = new LinkedHashMap<>();

        // 先加载现有文件夹
        List<BookMarks> existingFolders = bookMarksService.list(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BookMarks>()
                        .eq(BookMarks::getType, "h3"));
        for (BookMarks f : existingFolders) {
            folderNameToId.put(f.getTitle(), f.getId());
        }

        // 处理每条结果
        for (Map<String, Object> r : results) {
            String idStr = (String) r.get("bookmarkId");
            if (idStr == null) continue;
            Long bookmarkId = Long.valueOf(idStr);

            // 更新标题
            String newTitle = (String) r.get("suggestedTitle");
            if (newTitle != null && !newTitle.isBlank()) {
                BookMarks bm = new BookMarks();
                bm.setId(bookmarkId);
                bm.setTitle(newTitle);
                if (bookMarksService.updateById(bm)) {
                    updatedTitles++;
                }
            }

            // 创建/查找文件夹
            String folderName = (String) r.get("suggestedFolder");
            if (folderName == null || folderName.isBlank()) continue;

            Long folderId = folderNameToId.get(folderName);
            if (folderId == null) {
                // 创建新文件夹
                BookMarks newFolder = new BookMarks();
                newFolder.setId(cn.hutool.core.util.IdUtil.getSnowflakeNextId());
                newFolder.setTitle(folderName);
                newFolder.setType("h3");
                newFolder.setAddDate(System.currentTimeMillis() / 1000);
                newFolder.setLastModified(System.currentTimeMillis() / 1000);
                bookMarksService.save(newFolder);
                folderId = newFolder.getId();
                folderNameToId.put(folderName, folderId);
                createdFolders++;
            }

            // 移动书签
            BookMarks bm = new BookMarks();
            bm.setId(bookmarkId);
            bm.setParentId(folderId);
            if (bookMarksService.updateById(bm)) {
                movedBookmarks++;
            }
        }

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("createdFolders", createdFolders);
        stats.put("updatedTitles", updatedTitles);
        stats.put("movedBookmarks", movedBookmarks);
        return stats;
    }
}