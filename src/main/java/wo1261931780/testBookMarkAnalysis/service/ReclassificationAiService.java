package wo1261931780.testBookMarkAnalysis.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class ReclassificationAiService {
    private final AiClientService aiClient;

    public ReclassificationAiService(AiClientService aiClient) {
        this.aiClient = aiClient;
    }

    public record FolderNaming(String folderName, String reason) {}

    public record BookmarkAnalysis(
            String bookmarkId,
            String suggestedTitle,
            String keywordsJson,
            String pageType,
            String topicHint,
            Integer confidence,
            String reason) {}

    public AiClientService.AiJsonReply requestLargeDomainFolderName(
            String unitInputJson, String apiBaseUrl, String apiKey, String modelName) throws Exception {
        return aiClient.chatForJsonArray(
                """
                你正在重建个人书签目录。输入是一组属于同一主域名的书签。
                只为这一组生成一个中文文件夹名，名称应描述内容而不是直接复述域名。
                严格返回 JSON 数组，不含 Markdown：
                [{"folderName":"15到40字中文目录名","reason":"一句话理由"}]
                """,
                unitInputJson,
                0.2,
                apiBaseUrl,
                apiKey,
                modelName);
    }

    public AiClientService.AiJsonReply requestBookmarkAnalyses(
            String unitInputJson, String apiBaseUrl, String apiKey, String modelName) throws Exception {
        return aiClient.chatForJsonArray(
                """
                你正在重建个人书签目录。请为输入中的每条书签生成结构化内容理解。
                每个输入 bookmarkId 必须且只能返回一次。suggestedTitle 和 topicHint 必须非空。
                严格返回 JSON 数组，不含 Markdown：
                [{
                  "bookmarkId":"原输入ID",
                  "suggestedTitle":"15到80字中文标题",
                  "keywords":["关键词"],
                  "pageType":"文档/工具/论文/项目/文章/讨论/其他",
                  "topicHint":"简短主题提示",
                  "confidence":60,
                  "reason":"一句话理由"
                }]
                """,
                unitInputJson,
                0.3,
                apiBaseUrl,
                apiKey,
                modelName);
    }

    public FolderNaming parseLargeDomainFolder(JSONArray reply) {
        if (reply == null || reply.size() != 1) {
            throw new IllegalArgumentException("大域名目录命名必须只返回一项");
        }
        JSONObject item = reply.getJSONObject(0);
        String folderName = requireText(item, "folderName");
        if (folderName.length() > 512) {
            throw new IllegalArgumentException("AI 文件夹名称过长");
        }
        return new FolderNaming(folderName, trimToNull(item.getStr("reason")));
    }

    public List<BookmarkAnalysis> parseBookmarkAnalyses(JSONArray reply, Set<String> expectedBookmarkIds) {
        if (reply == null) {
            throw new IllegalArgumentException("AI 书签分析结果为空");
        }
        Set<String> returnedIds = new LinkedHashSet<>();
        List<BookmarkAnalysis> analyses = new ArrayList<>();
        for (int index = 0; index < reply.size(); index++) {
            JSONObject item = reply.getJSONObject(index);
            String bookmarkId = requireText(item, "bookmarkId");
            if (!expectedBookmarkIds.contains(bookmarkId)) {
                throw new IllegalArgumentException("AI 返回了不属于当前工作单元的书签ID: " + bookmarkId);
            }
            if (!returnedIds.add(bookmarkId)) {
                throw new IllegalArgumentException("AI 返回了重复书签ID: " + bookmarkId);
            }
            String suggestedTitle = requireText(item, "suggestedTitle");
            String topicHint = requireText(item, "topicHint");
            if (suggestedTitle.length() > 512 || topicHint.length() > 256) {
                throw new IllegalArgumentException("AI 返回字段超过数据库长度限制");
            }
            analyses.add(
                    new BookmarkAnalysis(
                            bookmarkId,
                            suggestedTitle,
                            item.getJSONArray("keywords") == null
                                    ? null
                                    : item.getJSONArray("keywords").toString(),
                            trimToNull(item.getStr("pageType")),
                            topicHint,
                            item.getInt("confidence"),
                            trimToNull(item.getStr("reason"))));
        }
        if (!returnedIds.equals(expectedBookmarkIds)) {
            throw new IllegalArgumentException("AI 未返回当前工作单元的全部书签分析结果");
        }
        return analyses;
    }

    private String requireText(JSONObject item, String fieldName) {
        String value = trimToNull(item.getStr(fieldName));
        if (value == null) {
            throw new IllegalArgumentException("AI 返回缺少必填字段: " + fieldName);
        }
        return value;
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
