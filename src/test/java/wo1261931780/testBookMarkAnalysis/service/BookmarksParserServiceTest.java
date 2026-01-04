package wo1261931780.testBookMarkAnalysis.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;
import wo1261931780.testBookMarkAnalysis.entity.BookmarkAnalysis;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.service
 *
 * @author liujiajun_junw
 * @Date 2026-01-04
 * @Description 书签解析服务单元测试
 */
@SpringBootTest
class BookmarksParserServiceTest {

    @Autowired
    private BookmarksParserService bookmarksParserService;

    /**
     * 测试解析标准Chrome书签格式
     */
    @Test
    @DisplayName("测试解析Chrome书签HTML")
    void testParseBookMarks() {
        String html = """
                <!DOCTYPE NETSCAPE-Bookmark-file-1>
                <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
                <TITLE>Bookmarks</TITLE>
                <H1>Bookmarks</H1>
                <DL><p>
                    <DT><H3 ADD_DATE="1609459200" LAST_MODIFIED="1609459300">测试文件夹</H3>
                    <DL><p>
                        <DT><A HREF="https://www.example.com" ADD_DATE="1609459400">示例网站</A>
                        <DT><A HREF="https://www.google.com" ADD_DATE="1609459500">谷歌</A>
                    </DL><p>
                </DL><p>
                """;

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        List<BookMarks> bookmarks = bookmarksParserService.parseBookMarks(inputStream);

        assertNotNull(bookmarks);
        assertEquals(3, bookmarks.size());

        // 验证文件夹
        long folderCount = bookmarks.stream().filter(b -> "h3".equals(b.getType())).count();
        assertEquals(1, folderCount);

        // 验证链接
        long linkCount = bookmarks.stream().filter(b -> "a".equals(b.getType())).count();
        assertEquals(2, linkCount);

        // 验证解析的内容
        BookMarks folder = bookmarks.stream()
                .filter(b -> "h3".equals(b.getType()))
                .findFirst()
                .orElse(null);
        assertNotNull(folder);
        assertEquals("测试文件夹", folder.getTitle());
        assertEquals(1609459200L, folder.getAddDate());
        assertEquals(1609459300L, folder.getLastModified());
    }

    /**
     * 测试解析空内容
     */
    @Test
    @DisplayName("测试解析空书签文件")
    void testParseEmptyBookmarks() {
        String html = "<!DOCTYPE NETSCAPE-Bookmark-file-1>\n<TITLE>Bookmarks</TITLE>";

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        List<BookMarks> bookmarks = bookmarksParserService.parseBookMarks(inputStream);

        assertNotNull(bookmarks);
        assertTrue(bookmarks.isEmpty());
    }

    /**
     * 测试解析包含特殊字符的书签
     */
    @Test
    @DisplayName("测试解析特殊字符书签")
    void testParseSpecialCharacters() {
        String html = """
                <DT><A HREF="https://example.com?q=test&lang=zh" ADD_DATE="1609459400">测试 &amp; 特殊字符 &lt;&gt;</A>
                """;

        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        List<BookMarks> bookmarks = bookmarksParserService.parseBookMarks(inputStream);

        assertNotNull(bookmarks);
        assertEquals(1, bookmarks.size());
        assertTrue(bookmarks.get(0).getHref().contains("q=test&lang=zh"));
    }

    /**
     * 测试导出HTML格式
     */
    @Test
    @DisplayName("测试导出HTML格式")
    void testExportToHtml() {
        String html = bookmarksParserService.exportToHtml();

        assertNotNull(html);
        assertTrue(html.contains("<!DOCTYPE NETSCAPE-Bookmark-file-1>"));
        assertTrue(html.contains("<TITLE>Bookmarks</TITLE>"));
    }

    /**
     * 测试导出Markdown格式
     */
    @Test
    @DisplayName("测试导出Markdown格式")
    void testExportToMarkdown() {
        String markdown = bookmarksParserService.exportToMarkdown();

        assertNotNull(markdown);
        assertTrue(markdown.contains("# 书签导出"));
    }

    /**
     * 测试导出JSON格式
     */
    @Test
    @DisplayName("测试导出JSON格式")
    void testExportToJson() {
        String json = bookmarksParserService.exportToJson();

        assertNotNull(json);
        assertTrue(json.contains("\"exportTime\""));
        assertTrue(json.contains("\"totalCount\""));
        assertTrue(json.contains("\"bookmarks\""));
    }

    /**
     * 测试书签分析功能
     */
    @Test
    @DisplayName("测试书签分析功能")
    void testAnalyzeBookmarks() {
        BookmarkAnalysis analysis = bookmarksParserService.analyzeBookmarks();

        assertNotNull(analysis);
        assertTrue(analysis.getTotalCount() >= 0);
        assertTrue(analysis.getLinkCount() >= 0);
        assertTrue(analysis.getFolderCount() >= 0);
        assertNotNull(analysis.getDomainDistribution());
        assertNotNull(analysis.getDuplicateUrls());
    }
}
