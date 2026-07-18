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

        // 构建 OpenAI Chat Completions API 请求体
        JSONObject requestBody = new JSONObject();
        requestBody.set("model", modelName);
        requestBody.set("max_tokens", 8000);
        requestBody.set("temperature", temperature);

        JSONArray messages = new JSONArray();
        if (systemPrompt != null && !systemPrompt.isBlank()) {
            JSONObject systemMsg = new JSONObject();
            systemMsg.set("role", "system");
            systemMsg.set("content", systemPrompt);
            messages.add(systemMsg);
        }
        JSONObject userMsg = new JSONObject();
        userMsg.set("role", "user");
        userMsg.set("content", userContent);
        messages.add(userMsg);
        requestBody.set("messages", messages);

        // API 端点
        String apiEndpoint = apiBaseUrl;
        if (!apiEndpoint.endsWith("/")) apiEndpoint += "/";
        apiEndpoint += "v1/chat/completions";

        // 发送请求
        cn.hutool.http.HttpRequest request =
                cn.hutool.http.HttpUtil.createPost(apiEndpoint)
                        .header("Content-Type", "application/json; charset=utf-8")
                        .header("Authorization", "Bearer " + apiKey)
                        .timeout(300000)
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

        String responseBodyStr = null;
        int maxRetries = 3;
        Exception lastError = null;
        for (int retry = 0; retry < maxRetries; retry++) {
            try (cn.hutool.http.HttpResponse response = request.execute()) {
                if (!response.isOk()) {
                    throw new RuntimeException(
                            "AI API 请求失败: HTTP " + response.getStatus() + " - " + response.body());
                }
                responseBodyStr = response.body();
                lastError = null;
                break;
            } catch (Exception e) {
                lastError = e;
                if (retry < maxRetries - 1) {
                    Thread.sleep(2000);
                }
            }
        }
        if (lastError != null) {
            throw new RuntimeException("AI API 请求失败(重试" + maxRetries + "次): " + lastError.getMessage(), lastError);
        }

        // 解析 OpenAI Chat Completions 响应格式
        JSONObject responseJson = JSONUtil.parseObj(responseBodyStr);
        JSONArray choices = responseJson.getJSONArray("choices");
        if (choices == null || choices.isEmpty()) {
            throw new RuntimeException("AI 返回内容为空");
        }
        JSONObject message = choices.getJSONObject(0).getJSONObject("message");
        String textReply = message == null ? null : message.getStr("content");
        if (textReply == null || textReply.isBlank()) {
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