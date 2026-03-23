package wo1261931780.testBookMarkAnalysis.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BookmarkCategorizationService {

    @Autowired
    private BookMarksService bookMarksService;

    /**
     * 调用 Google Gemini 原生 API 进行书签分类
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> categorizeBookmarks(String apiBaseUrl, String apiKey, String modelName, List<Long> bookmarkIds) throws Exception {
        // 1. 提取全库所有文件夹 (h3) 作为候选类别
        List<BookMarks> folders = bookMarksService.list(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BookMarks>()
                        .eq(BookMarks::getType, "h3")
        );
        JSONArray folderList = new JSONArray();
        for (BookMarks folder : folders) {
            JSONObject obj = new JSONObject();
            obj.set("id", folder.getId() != null ? folder.getId().toString() : "0");
            obj.set("name", folder.getTitle());
            folderList.add(obj);
        }

        // 2. 提取需要分类的书签实体
        List<BookMarks> targetBookmarks = bookMarksService.listByIds(bookmarkIds);
        if(targetBookmarks.isEmpty()) return new ArrayList<>();

        JSONArray bookmarkList = new JSONArray();
        for (BookMarks bm : targetBookmarks) {
            JSONObject obj = new JSONObject();
            obj.set("id", bm.getId() != null ? bm.getId().toString() : "0");
            obj.set("title", bm.getTitle());
            obj.set("url", bm.getHref());
            bookmarkList.add(obj);
        }

        // 3. 构建 Prompt (按照 Gemini API format)
        String systemPrompt = "你是一个专业的文件分类整理专家。你的任务是根据提供的文件夹列表，为给定的一批书签分配最合适的归属地。如果你认为没有任何现存的文件夹适合这个书签，你可以向我们建议一个新的文件夹名称（将其作为 recommendedParentName 输出，并令 recommendedParentId 为空或null）。\n你必须严格输出唯一的 JSON 数组，不允许包裹在任何 markdown 代码块中！";
        String userContent = "【现存的文件夹ID和名称列表】：\n" + folderList.toString() + "\n\n" +
                "【待分类书签列表】：\n" + bookmarkList.toString() + "\n\n" +
                "请将每个书签归类，严格返回形如下方的纯 JSON 数组：\n" +
                "[\n" +
                "  {\n" +
                "    \"bookmarkId\": \"书签的ID(字符串)\",\n" +
                "    \"recommendedParentId\": \"文件夹ID(字符串，新建分类填 null)\",\n" +
                "    \"recommendedParentName\": \"文件夹名称(如果是新建则给新名，旧的给原名)\",\n" +
                "    \"reason\": \"一句话归因\",\n" +
                "    \"confidence\": 95\n" +
                "  }\n" +
                "]";

        JSONObject systemInstruction = new JSONObject();
        JSONArray sysParts = new JSONArray();
        JSONObject sysPart = new JSONObject();
        sysPart.set("text", systemPrompt);
        sysParts.add(sysPart);
        systemInstruction.set("parts", sysParts);

        JSONObject contentObj = new JSONObject();
        JSONArray userParts = new JSONArray();
        JSONObject userPart = new JSONObject();
        userPart.set("text", userContent);
        userParts.add(userPart);
        contentObj.set("parts", userParts);
        contentObj.set("role", "user");

        JSONArray contents = new JSONArray();
        contents.add(contentObj);
        
        JSONObject generationConfig = new JSONObject();
        generationConfig.set("temperature", 0.3);

        JSONObject requestBody = new JSONObject();
        requestBody.set("systemInstruction", systemInstruction);
        requestBody.set("contents", contents);
        requestBody.set("generationConfig", generationConfig);

        // 4. 发起 HTTP 调用
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();

        String apiEndpoint = apiBaseUrl;
        if (!apiEndpoint.endsWith("/")) {
            apiEndpoint += "/";
        }
        // Compose Gemini endpoint: https://.../v1beta/models/gemini-1.5-pro:generateContent?key=xxx
        apiEndpoint += "models/" + modelName + ":generateContent?key=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiEndpoint))
                .header("Content-Type", "application/json; charset=utf-8")
                .timeout(Duration.ofMinutes(2)) // LLM 推理可能较慢
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString(), java.nio.charset.StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() >= 400) {
            throw new RuntimeException("Gemini API 请求失败: HTTP " + response.statusCode() + " - " + response.body());
        }

        // 5. 解析返回并剥离 Markdown 符号
        String responseBodyStr = response.body();
        JSONObject responseJson = JSONUtil.parseObj(responseBodyStr);
        String textReply = responseJson.getJSONArray("candidates")
                                       .getJSONObject(0)
                                       .getJSONObject("content")
                                       .getJSONArray("parts")
                                       .getJSONObject(0)
                                       .getStr("text");
        
        // 尝试去除包裹的 ```json 和 ``` 
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
}
