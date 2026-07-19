package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AiClientServiceTest {

    private HttpServer server;
    private String apiBaseUrl;
    private final AtomicReference<String> authorization = new AtomicReference<>();
    private final AtomicReference<String> legacyApiKey = new AtomicReference<>();
    private final AtomicReference<String> requestBody = new AtomicReference<>();

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/v1/chat/completions", this::handleChatCompletion);
        server.start();
        apiBaseUrl = "http://127.0.0.1:" + server.getAddress().getPort();
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    @Test
    void sendsOpenAiCompatibleRequestAndParsesChatCompletion() throws Exception {
        AiClientService client = new AiClientService();

        var result = client.chat(
                "system prompt", "user prompt", 0.3,
                apiBaseUrl, "test-api-key", "gpt-5.6-terra");

        assertEquals(1, result.size());
        assertEquals("1", result.getJSONObject(0).getStr("bookmarkId"));
        assertEquals("Bearer test-api-key", authorization.get());
        assertNull(legacyApiKey.get());
        org.junit.jupiter.api.Assertions.assertTrue(requestBody.get().contains("\"model\":\"gpt-5.6-terra\""));
    }

    @Test
    void returnsAuditableRequestAndRawModelContentWithoutAuthorizationHeader() throws Exception {
        AiClientService client = new AiClientService();

        AiClientService.AiJsonReply reply = client.chatForJsonArray(
                "system prompt", "user prompt", 0.3,
                apiBaseUrl, "test-api-key", "gpt-5.6-terra");

        assertEquals("[{\"bookmarkId\":\"1\"}]", reply.rawContent());
        org.junit.jupiter.api.Assertions.assertTrue(reply.requestJson().contains("user prompt"));
        org.junit.jupiter.api.Assertions.assertFalse(reply.requestJson().contains("test-api-key"));
        assertEquals("1", reply.array().getJSONObject(0).getStr("bookmarkId"));
    }

    private void handleChatCompletion(HttpExchange exchange) throws IOException {
        authorization.set(exchange.getRequestHeaders().getFirst("Authorization"));
        legacyApiKey.set(exchange.getRequestHeaders().getFirst("x-api-key"));
        requestBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
        byte[] response = "{\"choices\":[{\"message\":{\"content\":\"[{\\\"bookmarkId\\\":\\\"1\\\"}]\"}}]}"
                .getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }
}
