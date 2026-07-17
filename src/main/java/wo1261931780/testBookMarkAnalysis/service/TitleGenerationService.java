package wo1261931780.testBookMarkAnalysis.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wo1261931780.testBookMarkAnalysis.config.BookmarkConfig;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;

/**
 * AI 标题补全服务
 * <p>
 * 对空标题、过短标题或无意义标题的书签，调用 AI 生成完整标题。
 * 命名格式：[核心关键词] [页面类型] - [使用场景]
 */
@Service
public class TitleGenerationService {

    @Autowired private BookMarksService bookMarksService;
    @Autowired private BookmarkConfig bookmarkConfig;

    /**
     * 批量生成标题建议
     *
     * @param apiBaseUrl AI API 地址
     * @param apiKey     API Key
     * @param modelName  模型名称
     * @param bookmarkIds 书签 ID 列表（null 或空则处理全部）
     * @return 标题建议列表
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> generateTitles(
            String apiBaseUrl, String apiKey, String modelName, List<Long> bookmarkIds)
            throws Exception {

        // 获取需要补全的书签
        List<BookMarks> targets;
        if (bookmarkIds != null && !bookmarkIds.isEmpty()) {
            targets = bookMarksService.listByIds(bookmarkIds).stream()
                    .filter(bm -> DomainCategoryMapper.needsTitleGeneration(bm.getTitle()))
                    .collect(Collectors.toList());
        } else {
            targets = bookMarksService.list().stream()
                    .filter(bm -> DomainCategoryMapper.needsTitleGeneration(bm.getTitle()))
                    .collect(Collectors.toList());
        }

        if (targets.isEmpty()) return new ArrayList<>();

        // 构建请求数据
        JSONArray bookmarkList = new JSONArray();
        for (BookMarks bm : targets) {
            JSONObject obj = new JSONObject();
            obj.set("id", bm.getId() != null ? bm.getId().toString() : "0");
            obj.set("url", bm.getHref());
            obj.set("originalTitle", bm.getTitle() != null ? bm.getTitle() : "");
            obj.set("domain", DomainCategoryMapper.extractDomain(bm.getHref()));
            bookmarkList.add(obj);
        }

        String systemPrompt = """
                你是一个书签整理助手。请为以下书签生成完整、可搜索的标题。

                规则：
                1. 从 URL 路径中提取关键词（路径段、文件名、参数等）
                2. 从域名推断来源和可信度
                3. 标题格式：「核心内容」-「使用场景/备注」
                4. 长度：15-40 字，中文为主
                5. 如果原标题有价值，保留其核心信息并扩充
                6. 场景标注：官方文档 / 教程 / 工具 / 论文 / 讨论 / 博客 / 项目 / 新闻 / 其他

                示例：
                URL: https://github.com/facebook/react/issues/12345
                → "React 官方仓库 Issue #12345 - 组件渲染性能问题讨论"

                URL: https://jsonformatter.org (无标题)
                → "在线 JSON 格式化工具 - 开发者日常使用"

                URL: https://arxiv.org/abs/2301.12345
                → "论文: Attention Is All You Need - Transformer 架构原理"

                严格返回 JSON 数组，不含 markdown 标记：
                [{"bookmarkId":"...","suggestedTitle":"...","keywords":["...","..."],"pageType":"..."}]
                """;

        String userContent = "【待补全标题的书签列表】：\n" + bookmarkList.toString();

        // 调用 AI
        JSONObject requestBody = new JSONObject();
        requestBody.set("model", modelName);
        JSONArray messages = new JSONArray();

        JSONObject systemMsg = new JSONObject();
        systemMsg.set("role", "system");
        systemMsg.set("content", systemPrompt);
        messages.add(systemMsg);

        JSONObject userMsg = new JSONObject();
        userMsg.set("role", "user");
        userMsg.set("content", userContent);
        messages.add(userMsg);

        requestBody.set("messages", messages);
        requestBody.set("temperature", 0.3);

        String apiEndpoint = apiBaseUrl;
        if (!apiEndpoint.endsWith("/")) apiEndpoint += "/";
        apiEndpoint += "chat/completions";

        String responseBodyStr;
        cn.hutool.http.HttpRequest request =
                cn.hutool.http.HttpUtil.createPost(apiEndpoint)
                        .header("Content-Type", "application/json; charset=utf-8")
                        .header("Authorization", "Bearer " + apiKey)
                        .timeout(120000)
                        .body(requestBody.toString());

        String proxyHost = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");
        if (proxyHost != null && proxyPort != null && !proxyHost.isEmpty()) {
            try {
                request.setProxy(new java.net.Proxy(
                        java.net.Proxy.Type.HTTP,
                        new java.net.InetSocketAddress(proxyHost, Integer.parseInt(proxyPort))));
            } catch (Exception ignored) {}
        }

        try (cn.hutool.http.HttpResponse response = request.execute()) {
            if (!response.isOk()) {
                throw new RuntimeException(
                        "AI API 请求失败: HTTP " + response.getStatus() + " - " + response.body());
            }
            responseBodyStr = response.body();
        }

        JSONObject responseJson = JSONUtil.parseObj(responseBodyStr);
        String textReply = responseJson
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getStr("content");

        textReply = textReply.trim();
        if (textReply.startsWith("```json")) textReply = textReply.substring(7);
        else if (textReply.startsWith("```")) textReply = textReply.substring(3);
        if (textReply.endsWith("```")) textReply = textReply.substring(0, textReply.length() - 3);
        textReply = textReply.trim();

        JSONArray replyArray = JSONUtil.parseArray(textReply);
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (int i = 0; i < replyArray.size(); i++) {
            resultList.add((Map<String, Object>) replyArray.getJSONObject(i).toBean(Map.class));
        }

        return resultList;
    }

    /**
     * 应用标题建议到数据库
     */
    public int applyTitles(List<Map<String, Object>> suggestions) {
        int updated = 0;
        for (Map<String, Object> s : suggestions) {
            String idStr = (String) s.get("bookmarkId");
            String newTitle = (String) s.get("suggestedTitle");
            if (idStr == null || newTitle == null) continue;

            BookMarks bm = new BookMarks();
            bm.setId(Long.valueOf(idStr));
            bm.setTitle(newTitle);
            if (bookMarksService.updateById(bm)) {
                updated++;
            }
        }
        return updated;
    }
}