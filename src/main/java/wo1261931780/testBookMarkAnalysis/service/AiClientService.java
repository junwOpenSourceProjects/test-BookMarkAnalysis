package wo1261931780.testBookMarkAnalysis.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wo1261931780.testBookMarkAnalysis.config.BookmarkConfig;

/**
 * AI API 客户端 — 统一封装 Anthropic Messages API 调用
 * <p>
 * 使用 VoltAPI 的 Anthropic 兼容端点：
 * POST /v1/messages，x-api-key 认证，anthropic-version header
 */
@Service
public class AiClientService {

    @Autowired private BookmarkConfig bookmarkConfig;

    /**
     * 调用 AI，返回解析后的 JSON 数组
     *
     * @param systemPrompt 系统提示词
     * @param userContent  用户消息
     * @param temperature  温度（0-1）
     * @return AI 返回的 JSON 数组
     */
    public JSONArray chat(String systemPrompt, String userContent, double temperature) throws Exception {
        return chat(systemPrompt, userContent, temperature,
                bookmarkConfig.getAiApiBaseUrl(), bookmarkConfig.getAiApiKey(), bookmarkConfig.getAiModelName());
    }

    /**
     * 调用 AI（指定 API 参数）
     */
    public JSONArray chat(String systemPrompt, String userContent, double temperature,
                          String apiBaseUrl, String apiKey, String modelName) throws Exception {

        // 构建 Anthropic Messages API 请求体
        JSONObject requestBody = new JSONObject();
        requestBody.set("model", modelName);
        requestBody.set("max_tokens", 8000);
        requestBody.set("temperature", temperature);

        // system 是顶层字段
        if (systemPrompt != null && !systemPrompt.isBlank()) {
            requestBody.set("system", systemPrompt);
        }

        // messages 数组
        JSONArray messages = new JSONArray();
        JSONObject userMsg = new JSONObject();
        userMsg.set("role", "user");
        userMsg.set("content", userContent);
        messages.add(userMsg);
        requestBody.set("messages", messages);

        // API 端点
        String apiEndpoint = apiBaseUrl;
        if (!apiEndpoint.endsWith("/")) apiEndpoint += "/";
        apiEndpoint += "v1/messages";

        // 发送请求
        cn.hutool.http.HttpRequest request =
                cn.hutool.http.HttpUtil.createPost(apiEndpoint)
                        .header("Content-Type", "application/json; charset=utf-8")
                        .header("x-api-key", apiKey)
                        .header("anthropic-version", "2023-06-01")
                        .timeout(120000)
                        .body(requestBody.toString());

        // 代理支持
        String proxyHost = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");
        if (proxyHost != null && proxyPort != null && !proxyHost.isEmpty()) {
            try {
                request.setProxy(new java.net.Proxy(
                        java.net.Proxy.Type.HTTP,
                        new java.net.InetSocketAddress(proxyHost, Integer.parseInt(proxyPort))));
            } catch (Exception ignored) {}
        }

        String responseBodyStr;
        try (cn.hutool.http.HttpResponse response = request.execute()) {
            if (!response.isOk()) {
                throw new RuntimeException(
                        "AI API 请求失败: HTTP " + response.getStatus() + " - " + response.body());
            }
            responseBodyStr = response.body();
        }

        // 解析 Anthropic 响应格式（跳过 thinking 块，取 text 块）
        JSONObject responseJson = JSONUtil.parseObj(responseBodyStr);
        JSONArray content = responseJson.getJSONArray("content");
        if (content == null || content.isEmpty()) {
            throw new RuntimeException("AI 返回内容为空");
        }
        String textReply = null;
        for (int i = 0; i < content.size(); i++) {
            JSONObject block = content.getJSONObject(i);
            if ("text".equals(block.getStr("type"))) {
                textReply = block.getStr("text");
                break;
            }
        }
        if (textReply == null) {
            throw new RuntimeException("AI 返回格式异常");
        }

        // 清理 markdown 包裹
        textReply = textReply.trim();
        if (textReply.startsWith("```json")) textReply = textReply.substring(7);
        else if (textReply.startsWith("```")) textReply = textReply.substring(3);
        if (textReply.endsWith("```")) textReply = textReply.substring(0, textReply.length() - 3);
        textReply = textReply.trim();

        return JSONUtil.parseArray(textReply);
    }
}