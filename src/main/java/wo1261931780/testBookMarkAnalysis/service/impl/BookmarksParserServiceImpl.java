package wo1261931780.testBookMarkAnalysis.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wo1261931780.testBookMarkAnalysis.common.exception.BusinessException;
import wo1261931780.testBookMarkAnalysis.config.BookmarkConfig;
import wo1261931780.testBookMarkAnalysis.config.CacheConfig;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;
import wo1261931780.testBookMarkAnalysis.entity.BookmarkAnalysis;
import wo1261931780.testBookMarkAnalysis.entity.ParseResult;
import wo1261931780.testBookMarkAnalysis.mapper.BookMarksMapper;
import wo1261931780.testBookMarkAnalysis.parser.BookmarkParser;
import wo1261931780.testBookMarkAnalysis.service.BookMarksService;
import wo1261931780.testBookMarkAnalysis.service.BookmarksParserService;

/**
 * Created by Intellij IDEA. Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.service.impl
 *
 * @author liujiajun_junw @Date 2023-11-15-38 星期二 @Description 书签解析服务实现类
 */
@Slf4j
@Service
public class BookmarksParserServiceImpl extends ServiceImpl<BookMarksMapper, BookMarks>
        implements BookmarksParserService {

    @Autowired private BookMarksService bookMarksService;

    @Autowired private BookmarkConfig bookmarkConfig;

    @Autowired
    @Qualifier("jsoupBookmarkParser")
    private BookmarkParser jsoupParser;

    @Autowired
    @Qualifier("safariBookmarkParser")
    private BookmarkParser safariParser;

    @Autowired private ResourceLoader resourceLoader;

    @Override
    public List<BookMarks> parseBookMarks() {
        try {
            Resource resource = resourceLoader.getResource(bookmarkConfig.getInputPath());
            log.info("正在使用Jsoup解析书签文件: {}", bookmarkConfig.getInputPath());
            return jsoupParser.parse(resource.getInputStream());
        } catch (Exception e) {
            log.error("解析书签文件失败: {}", e.getMessage(), e);
            throw new RuntimeException("解析书签文件失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Cacheable(value = CacheConfig.CACHE_BOOKMARK_BY_URL, key = "#url", unless = "#result == null")
    public BookMarks selectByUrl(String url) {
        if (StrUtil.isEmpty(url)) {
            log.warn("URL为空，无法查询");
            return null;
        }

        LambdaQueryWrapper<BookMarks> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(BookMarks::getHref, url)
                .isNotNull(BookMarks::getAddDate)
                .orderByAsc(BookMarks::getAddDate)
                .last("LIMIT 1");
        BookMarks bookMark = bookMarksService.getOne(lambdaQueryWrapper, false);

        if (bookMark == null) {
            LambdaQueryWrapper<BookMarks> fallbackWrapper = new LambdaQueryWrapper<>();
            fallbackWrapper.eq(BookMarks::getHref, url).last("LIMIT 1");
            bookMark = bookMarksService.getOne(fallbackWrapper, false);
        }

        if (bookMark == null) {
            log.info("未找到URL对应的书签: {}", url);
            return null;
        }

        return bookMark;
    }

    @Override
    public List<BookMarks> parseBookMarks(InputStream inputStream) {
        return jsoupParser.parse(inputStream);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(
            value = {CacheConfig.CACHE_BOOKMARK_ANALYSIS, CacheConfig.CACHE_DOMAIN_STATS},
            allEntries = true)
    public ParseResult uploadAndParse(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(400, "上传文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".html")) {
            throw new BusinessException(400, "只支持HTML格式的书签文件");
        }

        long startTime = System.currentTimeMillis();
        ParseResult result = new ParseResult();
        result.setFileName(fileName);

        try {
            List<BookMarks> bookmarks = parseBookMarks(file.getInputStream());
            result.setTotalParsed(bookmarks.size());
            result.setLinkCount(
                    (int) bookmarks.stream().filter(b -> "a".equals(b.getType())).count());
            result.setFolderCount(
                    (int) bookmarks.stream().filter(b -> "h3".equals(b.getType())).count());

            // 批量插入数据库
            if (!bookmarks.isEmpty()) {
                int inserted = bookMarksService.batchInsert(bookmarks);
                result.setInsertedCount(inserted);
            }

            result.setParseTimeMs(System.currentTimeMillis() - startTime);
            log.info(
                    "文件 {} 解析完成，共 {} 条书签，耗时 {}ms",
                    fileName,
                    bookmarks.size(),
                    result.getParseTimeMs());
        } catch (Exception e) {
            log.error("解析上传文件失败: {}", e.getMessage(), e);
            throw new BusinessException(500, "解析文件失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    @Cacheable(value = CacheConfig.CACHE_BOOKMARK_ANALYSIS)
    public BookmarkAnalysis analyzeBookmarks() {
        log.info("执行书签分析（未命中缓存）");
        BookmarkAnalysis analysis = new BookmarkAnalysis();

        // 1. 统计总量
        long totalCount = bookMarksService.count();
        analysis.setTotalCount((int) totalCount);

        // 2. 统计链接和文件夹数量
        LambdaQueryWrapper<BookMarks> aWrapper = new LambdaQueryWrapper<>();
        aWrapper.eq(BookMarks::getType, "a");
        long linkCount = bookMarksService.count(aWrapper);
        analysis.setLinkCount((int) linkCount);

        LambdaQueryWrapper<BookMarks> h3Wrapper = new LambdaQueryWrapper<>();
        h3Wrapper.eq(BookMarks::getType, "h3");
        long folderCount = bookMarksService.count(h3Wrapper);
        analysis.setFolderCount((int) folderCount);

        // 3. 时间范围分析 (避免全表拉取，查询单列聚合或按序查询1条)
        LambdaQueryWrapper<BookMarks> minDateWrapper = new LambdaQueryWrapper<>();
        minDateWrapper
                .isNotNull(BookMarks::getAddDate)
                .gt(BookMarks::getAddDate, 0)
                .orderByAsc(BookMarks::getAddDate)
                .last("LIMIT 1");
        BookMarks oldest = bookMarksService.getOne(minDateWrapper, false);
        if (oldest != null) analysis.setEarliestAddDate(oldest.getAddDate());

        LambdaQueryWrapper<BookMarks> maxDateWrapper = new LambdaQueryWrapper<>();
        maxDateWrapper
                .isNotNull(BookMarks::getAddDate)
                .gt(BookMarks::getAddDate, 0)
                .orderByDesc(BookMarks::getAddDate)
                .last("LIMIT 1");
        BookMarks newest = bookMarksService.getOne(maxDateWrapper, false);
        if (newest != null) analysis.setLatestAddDate(newest.getAddDate());

        // 4. 分析重复URL与域名分布
        // 仅获取 href 字段，极大降低内存占用
        LambdaQueryWrapper<BookMarks> hrefWrapper = new LambdaQueryWrapper<>();
        hrefWrapper
                .eq(BookMarks::getType, "a")
                .isNotNull(BookMarks::getHref)
                .select(BookMarks::getHref);
        List<Object> targetUrls = bookMarksService.listObjs(hrefWrapper);

        List<String> linksString =
                targetUrls.stream()
                        .map(Object::toString)
                        .filter(StrUtil::isNotEmpty)
                        .collect(Collectors.toList());

        Map<String, Long> urlCountMap =
                linksString.stream()
                        .collect(Collectors.groupingBy(url -> url, Collectors.counting()));

        List<String> duplicateUrls =
                urlCountMap.entrySet().stream()
                        .filter(e -> e.getValue() > 1)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

        analysis.setDuplicateUrls(duplicateUrls);
        analysis.setDuplicateCount(duplicateUrls.size());

        // 域名分布分析 (Top 20)
        Map<String, Integer> domainMap = new LinkedHashMap<>();
        linksString.stream()
                .map(this::extractDomain)
                .filter(StrUtil::isNotEmpty)
                .collect(Collectors.groupingBy(d -> d, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(20)
                .forEach(e -> domainMap.put(e.getKey(), e.getValue().intValue()));
        analysis.setDomainDistribution(domainMap);

        return analysis;
    }

    /** 从URL中提取域名 */
    private String extractDomain(String url) {
        try {
            if (StrUtil.isEmpty(url)) {
                return "";
            }
            URI uri = new URI(url);
            String host = uri.getHost();
            return host != null ? host : "";
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String exportToHtml() {
        List<BookMarks> bookmarks = this.list();

        Map<Long, List<BookMarks>> childrenMap =
                bookmarks.stream()
                        .collect(
                                Collectors.groupingBy(
                                        b -> b.getParentId() != null ? b.getParentId() : 0L,
                                        Collectors.toList()));

        for (List<BookMarks> list : childrenMap.values()) {
            list.sort(
                    Comparator.comparing(
                            BookMarks::getSortOrder,
                            Comparator.nullsLast(Comparator.naturalOrder())));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE NETSCAPE-Bookmark-file-1>\n");
        sb.append("<!-- This is an automatically generated file.\n");
        sb.append("     It will be read and overwritten.\n");
        sb.append("     DO NOT EDIT! -->\n");
        sb.append("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n");
        sb.append("<TITLE>Bookmarks</TITLE>\n");
        sb.append("<H1>Bookmarks</H1>\n");
        sb.append("<DL><p>\n");

        List<BookMarks> rootNodes = childrenMap.getOrDefault(0L, new ArrayList<>());
        renderHtmlTree(rootNodes, childrenMap, sb, 1);

        sb.append("</DL><p>\n");
        return sb.toString();
    }

    private void renderHtmlTree(
            List<BookMarks> nodes,
            Map<Long, List<BookMarks>> childrenMap,
            StringBuilder sb,
            int depth) {
        String indent = StrUtil.repeat("    ", depth);
        for (BookMarks b : nodes) {
            if ("a".equals(b.getType())) {
                sb.append(indent)
                        .append("<DT><A HREF=\"")
                        .append(b.getHref() != null ? escapeHtml(b.getHref()) : "")
                        .append("\" ADD_DATE=\"")
                        .append(b.getAddDate() != null ? b.getAddDate() : "")
                        .append("\">")
                        .append(b.getTitle() != null ? escapeHtml(b.getTitle()) : "")
                        .append("</A>\n");
            } else if ("h3".equals(b.getType())) {
                sb.append(indent)
                        .append("<DT><H3 ADD_DATE=\"")
                        .append(b.getAddDate() != null ? b.getAddDate() : "")
                        .append("\" LAST_MODIFIED=\"")
                        .append(b.getLastModified() != null ? b.getLastModified() : "")
                        .append("\">")
                        .append(b.getTitle() != null ? escapeHtml(b.getTitle()) : "")
                        .append("</H3>\n");

                List<BookMarks> children = childrenMap.get(b.getId());
                if (children != null && !children.isEmpty()) {
                    sb.append(indent).append("<DL><p>\n");
                    renderHtmlTree(children, childrenMap, sb, depth + 1);
                    sb.append(indent).append("</DL><p>\n");
                }
            }
        }
    }

    @Override
    public String exportToMarkdown() {
        List<BookMarks> bookmarks = this.list();
        StringBuilder sb = new StringBuilder();

        sb.append("# 书签导出\n\n");
        sb.append("> 导出时间: ").append(new Date()).append("\n\n");

        // 先输出文件夹
        List<BookMarks> folders =
                bookmarks.stream()
                        .filter(b -> "h3".equals(b.getType()))
                        .sorted(
                                Comparator.comparing(
                                        BookMarks::getAddDate,
                                        Comparator.nullsLast(Comparator.naturalOrder())))
                        .toList();

        if (!folders.isEmpty()) {
            sb.append("## 文件夹\n\n");
            folders.forEach(
                    f ->
                            sb.append("- **")
                                    .append(f.getTitle() != null ? f.getTitle() : "未命名")
                                    .append("**\n"));
            sb.append("\n");
        }

        // 输出链接
        List<BookMarks> links =
                bookmarks.stream()
                        .filter(b -> "a".equals(b.getType()))
                        .sorted(
                                Comparator.comparing(
                                        BookMarks::getAddDate,
                                        Comparator.nullsLast(Comparator.naturalOrder())))
                        .toList();

        if (!links.isEmpty()) {
            sb.append("## 书签链接\n\n");
            links.forEach(
                    l -> {
                        String title = l.getTitle() != null ? l.getTitle() : "未命名";
                        String href = l.getHref() != null ? l.getHref() : "";
                        sb.append("- [").append(title).append("](").append(href).append(")\n");
                    });
        }

        return sb.toString();
    }

    @Override
    public String exportToJson() {
        List<BookMarks> bookmarks = this.list();

        // 构建导出结构
        Map<String, Object> exportData = new LinkedHashMap<>();
        exportData.put("exportTime", System.currentTimeMillis());
        exportData.put("totalCount", bookmarks.size());
        exportData.put("bookmarks", bookmarks);

        return JSONUtil.toJsonPrettyStr(exportData);
    }

    /** HTML转义 */
    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    @Override
    public List<BookMarks> parseWithJsoup(InputStream inputStream) {
        log.info("使用Jsoup解析器解析书签文件");
        try {
            List<BookMarks> bookmarks = jsoupParser.parse(inputStream);
            log.info("Jsoup解析完成，共解析到 {} 条书签", bookmarks.size());
            return bookmarks;
        } catch (Exception e) {
            log.error("Jsoup解析书签失败: {}", e.getMessage(), e);
            throw new BusinessException(500, "解析书签失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(
            value = {CacheConfig.CACHE_BOOKMARK_ANALYSIS, CacheConfig.CACHE_DOMAIN_STATS},
            allEntries = true)
    public ParseResult uploadAndParseWithJsoup(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(400, "上传文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".html")) {
            throw new BusinessException(400, "只支持HTML格式的书签文件");
        }

        long startTime = System.currentTimeMillis();
        ParseResult result = new ParseResult();
        result.setFileName(fileName);

        try {
            // 使用Jsoup解析器
            List<BookMarks> bookmarks = parseWithJsoup(file.getInputStream());
            result.setTotalParsed(bookmarks.size());
            result.setLinkCount(
                    (int) bookmarks.stream().filter(b -> "a".equals(b.getType())).count());
            result.setFolderCount(
                    (int) bookmarks.stream().filter(b -> "h3".equals(b.getType())).count());

            // 批量插入数据库
            if (!bookmarks.isEmpty()) {
                int inserted = bookMarksService.batchInsert(bookmarks);
                result.setInsertedCount(inserted);
            }

            result.setParseTimeMs(System.currentTimeMillis() - startTime);
            log.info(
                    "文件 {} 使用Jsoup解析完成，共 {} 条书签，耗时 {}ms",
                    fileName,
                    bookmarks.size(),
                    result.getParseTimeMs());
        } catch (Exception e) {
            log.error("Jsoup解析上传文件失败: {}", e.getMessage(), e);
            throw new BusinessException(500, "解析文件失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public List<BookMarks> parseWithSafari(InputStream inputStream) {
        log.info("使用Safari解析器解析plist书签文件");
        try {
            List<BookMarks> bookmarks = safariParser.parse(inputStream);
            log.info("Safari解析完成，共解析到 {} 条书签", bookmarks.size());
            return bookmarks;
        } catch (Exception e) {
            log.error("Safari解析书签失败: {}", e.getMessage(), e);
            throw new BusinessException(500, "解析Safari书签失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(
            value = {CacheConfig.CACHE_BOOKMARK_ANALYSIS, CacheConfig.CACHE_DOMAIN_STATS},
            allEntries = true)
    public ParseResult uploadAndParseWithSafari(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(400, "上传文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".plist")) {
            throw new BusinessException(400, "只支持plist格式的Safari书签文件");
        }

        long startTime = System.currentTimeMillis();
        ParseResult result = new ParseResult();
        result.setFileName(fileName);

        try {
            List<BookMarks> bookmarks = parseWithSafari(file.getInputStream());
            result.setTotalParsed(bookmarks.size());
            result.setLinkCount(
                    (int) bookmarks.stream().filter(b -> "a".equals(b.getType())).count());
            result.setFolderCount(
                    (int) bookmarks.stream().filter(b -> "h3".equals(b.getType())).count());

            if (!bookmarks.isEmpty()) {
                int inserted = bookMarksService.batchInsert(bookmarks);
                result.setInsertedCount(inserted);
            }

            result.setParseTimeMs(System.currentTimeMillis() - startTime);
            log.info(
                    "Safari书签文件 {} 解析完成，共 {} 条书签，耗时 {}ms",
                    fileName,
                    bookmarks.size(),
                    result.getParseTimeMs());
        } catch (Exception e) {
            log.error("Safari解析上传文件失败: {}", e.getMessage(), e);
            throw new BusinessException(500, "解析Safari书签失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(
            value = {CacheConfig.CACHE_BOOKMARK_ANALYSIS, CacheConfig.CACHE_DOMAIN_STATS},
            allEntries = true)
    public ParseResult uploadAndParseAuto(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(400, "上传文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new BusinessException(400, "无法获取文件名");
        }

        String lowerFileName = fileName.toLowerCase();

        // 根据文件扩展名自动选择解析器
        if (lowerFileName.endsWith(".plist")) {
            log.info("检测到plist文件，使用Safari解析器");
            return uploadAndParseWithSafari(file);
        } else if (lowerFileName.endsWith(".html") || lowerFileName.endsWith(".htm")) {
            log.info("检测到HTML文件，使用Jsoup解析器");
            return uploadAndParseWithJsoup(file);
        } else {
            // 尝试通过文件内容判断
            try {
                byte[] bytes = file.getBytes();
                String contentStart =
                        new String(bytes, 0, Math.min(100, bytes.length), StandardCharsets.UTF_8);

                if (contentStart.contains("<!DOCTYPE plist") || contentStart.contains("<plist")) {
                    log.info("通过内容检测到plist文件，使用Safari解析器");
                    return uploadAndParseWithSafari(file);
                } else if (contentStart.contains("<!DOCTYPE")
                        || contentStart.contains("<html")
                        || contentStart.contains("<HTML")
                        || contentStart.contains("NETSCAPE-Bookmark")) {
                    log.info("通过内容检测到HTML文件，使用Jsoup解析器");
                    return uploadAndParseWithJsoup(file);
                }
            } catch (Exception e) {
                log.warn("无法读取文件内容进行格式检测: {}", e.getMessage());
            }

            throw new BusinessException(400, "不支持的文件格式，请上传HTML或plist格式的书签文件");
        }
    }
}
