package wo1261931780.testBookMarkAnalysis.parser.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;
import wo1261931780.testBookMarkAnalysis.parser.BookmarkParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.parser.impl
 *
 * @author liujiajun_junw
 * @Date 2026-01-04
 * @Description 基于Jsoup的HTML书签解析器
 * <p>
 * 相比正则表达式解析的优势：
 * 1. 更健壮 - 能正确处理格式不规范的HTML
 * 2. 更易维护 - 使用CSS选择器语法，代码更清晰
 * 3. 更安全 - 不会因为HTML变化而导致解析失败
 * 4. 更快速 - 对于大文件，Jsoup的DOM解析更高效
 */
@Slf4j
@Component("jsoupBookmarkParser")
public class JsoupBookmarkParser implements BookmarkParser {

    @Override
    public List<BookMarks> parse(InputStream inputStream) {
        try {
            Document doc = Jsoup.parse(inputStream, StandardCharsets.UTF_8.name(), "");
            return parseDocument(doc);
        } catch (IOException e) {
            log.error("Jsoup解析书签文件失败: {}", e.getMessage(), e);
            throw new RuntimeException("解析书签文件失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BookMarks> parse(String html) {
        Document doc = Jsoup.parse(html);
        return parseDocument(doc);
    }

    @Override
    public String getParserName() {
        return "JsoupBookmarkParser";
    }

    /**
     * 解析Document对象
     */
    private List<BookMarks> parseDocument(Document doc) {
        List<BookMarks> bookmarkList = new ArrayList<>();

        // 解析所有链接 <A> 标签
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            BookMarks bookmark = parseLink(link);
            if (bookmark != null) {
                bookmarkList.add(bookmark);
            }
        }

        // 解析所有文件夹 <H3> 标签
        Elements folders = doc.select("h3");
        for (Element folder : folders) {
            BookMarks bookmark = parseFolder(folder);
            if (bookmark != null) {
                bookmarkList.add(bookmark);
            }
        }

        log.info("[Jsoup] 解析完成，共解析到 {} 条书签（链接: {}, 文件夹: {}）",
                bookmarkList.size(), links.size(), folders.size());
        return bookmarkList;
    }

    /**
     * 解析链接元素
     */
    private BookMarks parseLink(Element link) {
        BookMarks bookmark = new BookMarks();
        bookmark.setId(IdUtil.getSnowflakeNextId());
        bookmark.setType("a");

        // 获取链接地址
        String href = link.attr("href");
        if (StrUtil.isNotEmpty(href)) {
            bookmark.setHref(href);
        }

        // 获取添加日期
        String addDate = link.attr("add_date");
        if (StrUtil.isNotEmpty(addDate)) {
            try {
                bookmark.setAddDate(Long.parseLong(addDate));
            } catch (NumberFormatException e) {
                log.debug("无法解析添加日期: {}", addDate);
            }
        }

        // 获取标题
        String title = link.text();
        if (StrUtil.isNotEmpty(title)) {
            bookmark.setTitle(title);
        }

        // 获取图标（可选，未来可扩展支持）
        // String icon = link.attr("icon");
        // 如果需要存储图标，可以在这里处理

        log.debug("[Jsoup] 解析书签: {} -> {}", title, href);
        return bookmark;
    }

    /**
     * 解析文件夹元素
     */
    private BookMarks parseFolder(Element folder) {
        BookMarks bookmark = new BookMarks();
        bookmark.setId(IdUtil.getSnowflakeNextId());
        bookmark.setType("h3");

        // 获取添加日期
        String addDate = folder.attr("add_date");
        if (StrUtil.isNotEmpty(addDate)) {
            try {
                bookmark.setAddDate(Long.parseLong(addDate));
            } catch (NumberFormatException e) {
                log.debug("无法解析添加日期: {}", addDate);
            }
        }

        // 获取最后修改日期
        String lastModified = folder.attr("last_modified");
        if (StrUtil.isNotEmpty(lastModified)) {
            try {
                bookmark.setLastModified(Long.parseLong(lastModified));
            } catch (NumberFormatException e) {
                log.debug("无法解析最后修改日期: {}", lastModified);
            }
        }

        // 获取标题
        String title = folder.text();
        if (StrUtil.isNotEmpty(title)) {
            bookmark.setTitle(title);
        }

        log.debug("[Jsoup] 解析文件夹: {}", title);
        return bookmark;
    }
}
