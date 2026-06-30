package wo1261931780.testBookMarkAnalysis.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;

@Service
@Transactional(rollbackFor = Exception.class)
public class BookmarkCategorizationService {

    @Autowired private BookMarksService bookMarksService;

    private String extractDomain(String urlStr) {
        try {
            if (urlStr == null || urlStr.trim().isEmpty()) return "unknown";
            if (!urlStr.startsWith("http://") && !urlStr.startsWith("https://")) {
                urlStr = "http://" + urlStr;
            }
            URI uri = new URI(urlStr);
            String host = uri.getHost();
            if (host == null) return "unknown";
            host = host.toLowerCase();
            if (host.startsWith("www.")) {
                host = host.substring(4);
            }
            return host;
        } catch (Exception e) {
            return "unknown";
        }
    }

    /** 调用 Google Gemini 原生 API 进行书签分类 */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> categorizeBookmarks(
            String apiBaseUrl, String apiKey, String modelName, List<Long> bookmarkIds)
            throws Exception {
        // 1. 提取全库所有文件夹 (h3) 作为候选类别
        List<BookMarks> folders =
                bookMarksService.list(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<
                                        BookMarks>()
                                .eq(BookMarks::getType, "h3"));
        JSONArray folderList = new JSONArray();
        for (BookMarks folder : folders) {
            JSONObject obj = new JSONObject();
            obj.set("id", folder.getId() != null ? folder.getId().toString() : "0");
            obj.set("name", folder.getTitle());
            folderList.add(obj);
        }

        // 2. 提取需要分类的书签实体
        List<BookMarks> targetBookmarks = bookMarksService.listByIds(bookmarkIds);
        if (targetBookmarks.isEmpty()) return new ArrayList<>();

        JSONArray bookmarkList = new JSONArray();
        for (BookMarks bm : targetBookmarks) {
            JSONObject obj = new JSONObject();
            obj.set("id", bm.getId() != null ? bm.getId().toString() : "0");
            obj.set("title", bm.getTitle());
            obj.set("url", bm.getHref());
            bookmarkList.add(obj);
        }

        // 3. 构建 Prompt (按照 Gemini API format)
        String systemPrompt =
                "你是一个专业的文件分类整理专家。你的任务是根据提供的文件夹列表，为给定的一批书签分配最合适的归属地。如果你认为没有任何现存的文件夹适合这个书签，你可以向我们建议一个新的文件夹名称（将其作为 recommendedParentName 输出，并令 recommendedParentId 为空或null）。\n你必须严格输出唯一的 JSON 数组，不允许包裹在任何 markdown 代码块中！";
        String userContent =
                "【现存的文件夹ID和名称列表】：\n"
                        + folderList.toString()
                        + "\n\n"
                        + "【待分类书签列表】：\n"
                        + bookmarkList.toString()
                        + "\n\n"
                        + "请将每个书签归类，严格返回形如下方的纯 JSON 数组：\n"
                        + "[\n"
                        + "  {\n"
                        + "    \"bookmarkId\": \"书签的ID(字符串)\",\n"
                        + "    \"recommendedParentId\": \"文件夹ID(字符串，新建分类填 null)\",\n"
                        + "    \"recommendedParentName\": \"文件夹名称(如果是新建则给新名，旧的给原名)\",\n"
                        + "    \"reason\": \"一句话归因\",\n"
                        + "    \"confidence\": 95\n"
                        + "  }\n"
                        + "]";

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
        if (!apiEndpoint.endsWith("/")) {
            apiEndpoint += "/";
        }
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
                request.setProxy(
                        new java.net.Proxy(
                                java.net.Proxy.Type.HTTP,
                                new java.net.InetSocketAddress(
                                        proxyHost, Integer.parseInt(proxyPort))));
            } catch (Exception ignored) {
            }
        }

        try (cn.hutool.http.HttpResponse response = request.execute()) {
            if (!response.isOk()) {
                throw new RuntimeException(
                        "AI API 请求失败: HTTP " + response.getStatus() + " - " + response.body());
            }
            responseBodyStr = response.body();
        }
        JSONObject responseJson = JSONUtil.parseObj(responseBodyStr);
        String textReply =
                responseJson
                        .getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getStr("content");

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

    public static class ReconstructSession {
        public List<JSONObject> allDomainProfiles;
        public int currentBatchIndex;
        public int totalBatches;
        public int batchSize = 40;
        public List<BookMarks> allLinks;
        public Map<String, Long> folderDict = new HashMap<>();
        public Long fallbackFolderId;
    }

    private ReconstructSession currentSession = null;

    public Map<String, Object> initReconstructTree() {
        currentSession = new ReconstructSession();

        List<BookMarks> allLinks =
                bookMarksService.list(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<
                                        BookMarks>()
                                .eq(BookMarks::getType, "a")
                                .isNotNull(BookMarks::getHref));
        currentSession.allLinks = allLinks;

        Map<String, List<BookMarks>> groupedByDomain =
                allLinks.stream().collect(Collectors.groupingBy(bm -> extractDomain(bm.getHref())));

        List<JSONObject> allDomainProfiles = new ArrayList<>();
        for (Map.Entry<String, List<BookMarks>> entry : groupedByDomain.entrySet()) {
            String domain = entry.getKey();
            if ("unknown".equals(domain) || domain.isEmpty()) continue;
            List<BookMarks> bookmarks = entry.getValue();
            if (bookmarks.size() < 2) continue; // 过滤极端长尾数据

            JSONObject profile = new JSONObject();
            profile.set("domain", domain);
            profile.set("count", bookmarks.size());
            JSONArray samples = new JSONArray();
            int sampleSize = Math.min(2, bookmarks.size());
            for (int i = 0; i < sampleSize; i++) {
                samples.add(bookmarks.get(i).getTitle());
            }
            profile.set("sampleTitles", samples);
            allDomainProfiles.add(profile);
        }

        currentSession.allDomainProfiles = allDomainProfiles;
        currentSession.totalBatches =
                (int) Math.ceil((double) allDomainProfiles.size() / currentSession.batchSize);
        currentSession.currentBatchIndex = 0;

        // 焦土战术提前执行：删除所有老H3文件夹
        bookMarksService.remove(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BookMarks>()
                        .eq(BookMarks::getType, "h3"));

        BookMarks fallback = new BookMarks();
        fallback.setId(IdUtil.getSnowflakeNextId());
        fallback.setTitle("未分类/长尾杂项区");
        fallback.setType("h3");
        fallback.setAddDate(System.currentTimeMillis() / 1000);
        fallback.setLastModified(System.currentTimeMillis() / 1000);
        fallback.setParentId(null);
        bookMarksService.save(fallback);

        currentSession.fallbackFolderId = fallback.getId();
        currentSession.folderDict.put(fallback.getTitle(), fallback.getId());

        Map<String, Object> res = new HashMap<>();
        res.put("totalBatches", currentSession.totalBatches);
        res.put("currentBatchIndex", currentSession.currentBatchIndex);
        res.put("domainCount", allDomainProfiles.size());
        return res;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> fetchNextBatch(
            String apiBaseUrl, String apiKey, String modelName) throws Exception {
        if (currentSession == null
                || currentSession.currentBatchIndex >= currentSession.totalBatches) {
            return new ArrayList<>();
        }

        int fromIndex = currentSession.currentBatchIndex * currentSession.batchSize;
        int toIndex =
                Math.min(
                        fromIndex + currentSession.batchSize,
                        currentSession.allDomainProfiles.size());
        List<JSONObject> batchProfiles =
                currentSession.allDomainProfiles.subList(fromIndex, toIndex);

        JSONArray batchArray = new JSONArray();
        batchArray.addAll(batchProfiles);

        String systemPrompt =
                "你是一位资深的知识管理专家。\n"
                        + "任务：为你接收到的这些核心域名，构建高内聚的全新文件夹名称。\n"
                        + "要求：\n"
                        + "1. 相同或近似主题的网站务必归入同一个文件夹名，避免冗余。\n"
                        + "2. 仅能回复严格合法的 JSON 数组，不含 Markdown 引号或多余文本。\n"
                        + "3. 格式：`[{ \"domain\": \"xxx\", \"newFolderName\": \"xxx\" }]`。";

        String userContent = "【请对以下批次的网站执行归类】：\n" + batchArray.toString();

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
        requestBody.set("temperature", 0.1);

        String apiEndpoint = apiBaseUrl;
        if (!apiEndpoint.endsWith("/")) apiEndpoint += "/";
        apiEndpoint += "chat/completions";

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
                request.setProxy(
                        new java.net.Proxy(
                                java.net.Proxy.Type.HTTP,
                                new java.net.InetSocketAddress(
                                        proxyHost, Integer.parseInt(proxyPort))));
            } catch (Exception ignored) {
            }
        }

        String responseBodyStr = null;
        int retryCount = 0;
        int maxRetries = 3;
        while (retryCount < maxRetries) {
            try (cn.hutool.http.HttpResponse response = request.execute()) {
                if (!response.isOk()) {
                    throw new RuntimeException(
                            "HTTP " + response.getStatus() + " - " + response.body());
                }
                responseBodyStr = response.body();
                break;
            } catch (Exception e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw new Exception("获取AI建议失败: " + e.getMessage(), e);
                }
                Thread.sleep(3000);
            }
        }

        JSONObject responseJson = JSONUtil.parseObj(responseBodyStr);
        String textReply =
                responseJson
                        .getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getStr("content");

        textReply = textReply.trim();
        if (textReply.startsWith("```json")) textReply = textReply.substring(7);
        else if (textReply.startsWith("```")) textReply = textReply.substring(3);
        if (textReply.endsWith("```")) textReply = textReply.substring(0, textReply.length() - 3);
        textReply = textReply.trim();

        JSONArray mappings = JSONUtil.parseArray(textReply);
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (int j = 0; j < mappings.size(); j++) {
            resultList.add((Map<String, Object>) mappings.getJSONObject(j).toBean(Map.class));
        }

        for (Map<String, Object> map : resultList) {
            String domain = (String) map.get("domain");
            for (JSONObject prof : batchProfiles) {
                if (domain.equals(prof.getStr("domain"))) {
                    map.put("count", prof.getInt("count"));
                    break;
                }
            }
        }
        return resultList;
    }

    public Map<String, Object> confirmBatch(List<Map<String, Object>> mappings) {
        if (currentSession == null) return new HashMap<>();

        Set<String> newFolderNames = new HashSet<>();
        for (Map<String, Object> mapObj : mappings) {
            String folderName = (String) mapObj.get("newFolderName");
            if (folderName != null
                    && !folderName.isEmpty()
                    && !currentSession.folderDict.containsKey(folderName)) {
                newFolderNames.add(folderName);
            }
        }

        List<BookMarks> newFolderEntities = new ArrayList<>();
        for (String fName : newFolderNames) {
            BookMarks fItem = new BookMarks();
            fItem.setId(IdUtil.getSnowflakeNextId());
            fItem.setTitle(fName);
            fItem.setType("h3");
            fItem.setAddDate(System.currentTimeMillis() / 1000);
            fItem.setLastModified(System.currentTimeMillis() / 1000);
            fItem.setParentId(null);
            newFolderEntities.add(fItem);
            currentSession.folderDict.put(fName, fItem.getId());
        }
        if (!newFolderEntities.isEmpty()) {
            bookMarksService.saveBatch(newFolderEntities);
        }

        Map<String, Long> domainToFolderId = new HashMap<>();
        for (Map<String, Object> mapObj : mappings) {
            String domain = (String) mapObj.get("domain");
            String folderName = (String) mapObj.get("newFolderName");
            Long fId = currentSession.folderDict.get(folderName);
            if (fId != null) {
                domainToFolderId.put(domain, fId);
            }
        }

        List<BookMarks> linksToUpdate = new ArrayList<>();
        for (BookMarks link : currentSession.allLinks) {
            String domain = extractDomain(link.getHref());
            if (domainToFolderId.containsKey(domain)) {
                link.setParentId(domainToFolderId.get(domain));
                linksToUpdate.add(link);
            } else if (currentSession.currentBatchIndex == 0) {
                if (link.getParentId() == null
                        || !link.getParentId().equals(currentSession.fallbackFolderId)) {
                    link.setParentId(currentSession.fallbackFolderId);
                    linksToUpdate.add(link);
                }
            }
        }

        if (!linksToUpdate.isEmpty()) {
            bookMarksService.updateBatchById(linksToUpdate, 500);
        }

        currentSession.currentBatchIndex++;
        Map<String, Object> res = new HashMap<>();
        res.put("totalBatches", currentSession.totalBatches);
        res.put("currentBatchIndex", currentSession.currentBatchIndex);
        return res;
    }
}
