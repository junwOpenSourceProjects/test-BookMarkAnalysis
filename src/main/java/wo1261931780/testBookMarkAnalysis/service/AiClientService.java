package wo1261931780.testBookMarkAnalysis.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wo1261931780.testBookMarkAnalysis.config.BookmarkConfig;

/**
 * AI API 客户端 — 统一封装 OpenAI Chat Completions API 调用
 * <p>
 * 使用 VoltAPI 的 OpenAI 兼容端点：
 * POST /v1/chat/completions，Authorization Bearer 认证。
 */
@Service
public class AiClientService {
    private static final int MAX_RETRIES = 3;
    private static final int REQUEST_TIMEOUT_MILLIS = 300000;

    @Autowired private BookmarkConfig bookmarkConfig;

    public record AiJsonReply(String requestJson, String rawContent, JSONArray array) {}

    /**
     * 调用 AI，返回解析后的 JSON 数组。
     */
    public JSONArray chat(String systemPrompt, String userContent, double temperature) throws Exception {
        return chatForJsonArray(
                        systemPrompt,
                        userContent,
                        temperature,
                        bookmarkConfig.getAiApiBaseUrl(),
                        bookmarkConfig.getAiApiKey(),
                        bookmarkConfig.getAiModelName())
                .array();
    }

    /**
     * 调用 AI（指定 API 参数），保留旧调用方需要的数组返回值。
     */
    public JSONArray chat(
            String systemPrompt,
            String userContent,
            double temperature,
            String apiBaseUrl,
            String apiKey,
            String modelName)
            throws Exception {
        return chatForJsonArray(systemPrompt, userContent, temperature, apiBaseUrl, apiKey, modelName)
                .array();
    }

    /**
     * 调用 AI 并返回可持久化审计信息。requestJson 不含 Authorization 头或 API Key。
     */
    public AiJsonReply chatForJsonArray(
            String systemPrompt,
            String userContent,
            double temperature,
            String apiBaseUrl,
            String apiKey,
            String modelName)
            throws Exception {
        JSONObject requestBody = buildRequestBody(systemPrompt, userContent, temperature, modelName);
        String responseBody = executeRequest(apiBaseUrl, apiKey, requestBody.toString());
        String rawContent = extractModelContent(responseBody);
        return new AiJsonReply(requestBody.toString(), rawContent, JSONUtil.parseArray(stripMarkdownFence(rawContent)));
    }

    private JSONObject buildRequestBody(
            String systemPrompt, String userContent, double temperature, String modelName) {
        JSONObject requestBody = new JSONObject();
        requestBody.set("model", modelName);
        requestBody.set("max_tokens", 8000);
        requestBody.set("temperature", temperature);

        JSONArray messages = new JSONArray();
        if (systemPrompt != null && !systemPrompt.isBlank()) {
            JSONObject systemMessage = new JSONObject();
            systemMessage.set("role", "system");
            systemMessage.set("content", systemPrompt);
            messages.add(systemMessage);
        }
        JSONObject userMessage = new JSONObject();
        userMessage.set("role", "user");
        userMessage.set("content", userContent);
        messages.add(userMessage);
        requestBody.set("messages", messages);
        return requestBody;
    }

    private String executeRequest(String apiBaseUrl, String apiKey, String requestJson) throws Exception {
        String apiEndpoint = apiBaseUrl.endsWith("/")
                ? apiBaseUrl + "v1/chat/completions"
                : apiBaseUrl + "/v1/chat/completions";
        cn.hutool.http.HttpRequest request = cn.hutool.http.HttpUtil.createPost(apiEndpoint)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Authorization", "Bearer " + apiKey)
                .timeout(REQUEST_TIMEOUT_MILLIS)
                .body(requestJson);
        configureProxy(request);

        Exception lastError = null;
        for (int retry = 0; retry < MAX_RETRIES; retry++) {
            try (cn.hutool.http.HttpResponse response = request.execute()) {
                if (!response.isOk()) {
                    throw new RuntimeException(
                            "AI API 请求失败: HTTP " + response.getStatus() + " - " + response.body());
                }
                return response.body();
            } catch (Exception exception) {
                lastError = exception;
                if (retry < MAX_RETRIES - 1) {
                    Thread.sleep(2000);
                }
            }
        }
        throw new RuntimeException(
                "AI API 请求失败(重试" + MAX_RETRIES + "次): " + lastError.getMessage(), lastError);
    }

    private void configureProxy(cn.hutool.http.HttpRequest request) {
        String proxyHost = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");
        if (proxyHost == null || proxyPort == null || proxyHost.isEmpty()) {
            return;
        }
        try {
            request.setProxy(
                    new java.net.Proxy(
                            java.net.Proxy.Type.HTTP,
                            new java.net.InetSocketAddress(proxyHost, Integer.parseInt(proxyPort))));
        } catch (Exception ignored) {
            // A malformed optional proxy must not hide the actual AI request error.
        }
    }

    private String extractModelContent(String responseBody) {
        JSONObject responseJson = JSONUtil.parseObj(responseBody);
        JSONArray choices = responseJson.getJSONArray("choices");
        if (choices == null || choices.isEmpty()) {
            throw new RuntimeException("AI 返回内容为空");
        }
        JSONObject message = choices.getJSONObject(0).getJSONObject("message");
        String content = message == null ? null : message.getStr("content");
        if (content == null || content.isBlank()) {
            throw new RuntimeException("AI 返回格式异常");
        }
        return content;
    }

    private String stripMarkdownFence(String rawContent) {
        String content = rawContent.trim();
        if (content.startsWith("```json")) {
            content = content.substring(7);
        } else if (content.startsWith("```")) {
            content = content.substring(3);
        }
        if (content.endsWith("```")) {
            content = content.substring(0, content.length() - 3);
        }
        return content.trim();
    }
}
