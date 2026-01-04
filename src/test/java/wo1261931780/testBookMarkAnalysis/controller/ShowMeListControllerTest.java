package wo1261931780.testBookMarkAnalysis.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.controller
 *
 * @author liujiajun_junw
 * @Date 2026-01-04
 * @Description 书签控制器集成测试
 */
@SpringBootTest
@AutoConfigureMockMvc
class ShowMeListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 测试分页查询接口
     */
    @Test
    @DisplayName("测试分页查询书签列表")
    void testShowMeList() throws Exception {
        mockMvc.perform(get("/BookMarks/list")
                        .param("page", "1")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试分页参数校验 - 页码小于1
     */
    @Test
    @DisplayName("测试分页参数校验-页码不合法")
    void testShowMeListInvalidPage() throws Exception {
        mockMvc.perform(get("/BookMarks/list")
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isBadRequest());
    }

    /**
     * 测试分页参数校验 - 每页条数超过100
     */
    @Test
    @DisplayName("测试分页参数校验-条数超限")
    void testShowMeListInvalidLimit() throws Exception {
        mockMvc.perform(get("/BookMarks/list")
                        .param("page", "1")
                        .param("limit", "101"))
                .andExpect(status().isBadRequest());
    }

    /**
     * 测试书签分析接口
     */
    @Test
    @DisplayName("测试书签分析接口")
    void testAnalyzeBookmarks() throws Exception {
        mockMvc.perform(get("/BookMarks/analyze"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalCount").exists())
                .andExpect(jsonPath("$.data.linkCount").exists())
                .andExpect(jsonPath("$.data.folderCount").exists());
    }

    /**
     * 测试获取重复书签接口
     */
    @Test
    @DisplayName("测试获取重复书签接口")
    void testGetDuplicates() throws Exception {
        mockMvc.perform(get("/BookMarks/duplicates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试导出HTML格式
     */
    @Test
    @DisplayName("测试导出HTML格式")
    void testExportHtml() throws Exception {
        mockMvc.perform(get("/BookMarks/export")
                        .param("format", "html"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        org.hamcrest.Matchers.containsString("bookmarks_export.html")));
    }

    /**
     * 测试导出Markdown格式
     */
    @Test
    @DisplayName("测试导出Markdown格式")
    void testExportMarkdown() throws Exception {
        mockMvc.perform(get("/BookMarks/export")
                        .param("format", "markdown"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        org.hamcrest.Matchers.containsString("bookmarks_export.md")));
    }

    /**
     * 测试导出JSON格式
     */
    @Test
    @DisplayName("测试导出JSON格式")
    void testExportJson() throws Exception {
        mockMvc.perform(get("/BookMarks/export")
                        .param("format", "json"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        org.hamcrest.Matchers.containsString("bookmarks_export.json")));
    }

    /**
     * 测试文件上传接口
     */
    @Test
    @DisplayName("测试文件上传接口")
    void testUploadBookmarks() throws Exception {
        String bookmarkContent = """
                <!DOCTYPE NETSCAPE-Bookmark-file-1>
                <TITLE>Bookmarks</TITLE>
                <DL><p>
                    <DT><A HREF="https://www.example.com" ADD_DATE="1609459400">示例网站</A>
                </DL><p>
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test_bookmarks.html",
                MediaType.TEXT_HTML_VALUE,
                bookmarkContent.getBytes()
        );

        mockMvc.perform(multipart("/BookMarks/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalParsed").exists())
                .andExpect(jsonPath("$.data.fileName").value("test_bookmarks.html"));
    }

    /**
     * 测试上传非HTML文件
     */
    @Test
    @DisplayName("测试上传非HTML文件")
    void testUploadInvalidFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "invalid content".getBytes()
        );

        mockMvc.perform(multipart("/BookMarks/upload").file(file))
                .andExpect(status().isBadRequest());
    }
}
