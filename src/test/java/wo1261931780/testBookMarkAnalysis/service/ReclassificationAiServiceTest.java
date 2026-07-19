package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cn.hutool.json.JSONUtil;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ReclassificationAiServiceTest {
    private final ReclassificationAiService service = new ReclassificationAiService(null);

    @Test
    void parsesOneNonBlankLargeDomainFolderName() {
        ReclassificationAiService.FolderNaming naming = service.parseLargeDomainFolder(
                JSONUtil.parseArray("[{\"folderName\":\"GitHub 开源项目与开发协作\",\"reason\":\"代码仓库\"}]"));

        assertEquals("GitHub 开源项目与开发协作", naming.folderName());
    }

    @Test
    void rejectsBookmarkAnalysisContainingUnknownOrDuplicateIds() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.parseBookmarkAnalyses(
                        JSONUtil.parseArray("[{\"bookmarkId\":\"1\",\"suggestedTitle\":\"A\",\"topicHint\":\"前端\"},{\"bookmarkId\":\"1\",\"suggestedTitle\":\"B\",\"topicHint\":\"前端\"}]"),
                        Set.of("1", "2")));
        assertThrows(
                IllegalArgumentException.class,
                () -> service.parseBookmarkAnalyses(
                        JSONUtil.parseArray("[{\"bookmarkId\":\"3\",\"suggestedTitle\":\"A\",\"topicHint\":\"前端\"}]"),
                        Set.of("1", "2")));
    }

    @Test
    void requiresEveryInputBookmarkToHaveAnAnalysis() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.parseBookmarkAnalyses(
                        JSONUtil.parseArray("[{\"bookmarkId\":\"1\",\"suggestedTitle\":\"A\",\"topicHint\":\"前端\"}]"),
                        Set.of("1", "2")));
    }
}
