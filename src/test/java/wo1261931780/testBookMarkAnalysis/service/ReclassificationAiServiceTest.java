package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cn.hutool.json.JSONUtil;
import java.util.Map;
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
    void parsesDraftAssignmentsWithStableDraftKeys() {
        var assignments = service.parseSmallPoolClusterDraft(
                JSONUtil.parseArray("[{\"bookmarkId\":\"1\",\"logicalFolderKey\":\"draft:frontend-tools\",\"folderName\":\"前端开发工具\"}]"),
                Set.of("1"));

        assertEquals("draft:frontend-tools", assignments.get(0).logicalFolderKey());
        assertEquals("前端开发工具", assignments.get(0).folderName());
    }

    @Test
    void normalizesUnprefixedDraftKeyReturnedByTheAi() {
        var assignments = service.parseSmallPoolClusterDraft(
                JSONUtil.parseArray("[{\"bookmarkId\":\"1\",\"logicalFolderKey\":\"photoshop-effects\",\"folderName\":\"Photoshop 特效素材\"}]"),
                Set.of("1"));

        assertEquals("draft:photoshop-effects", assignments.get(0).logicalFolderKey());
    }

    @Test
    void parsesCanonicalMappingsAndRequiresEveryDraftFolderExactlyOnce() {
        var mappings = service.parseSmallPoolCanonicalization(
                JSONUtil.parseArray("[{\"draftFolderKey\":\"draft:frontend-tools\",\"logicalFolderKey\":\"small:frontend-development\",\"folderName\":\"前端开发与工具\"},{\"draftFolderKey\":\"draft:web-development\",\"logicalFolderKey\":\"small:frontend-development\",\"folderName\":\"前端开发与工具\"}]"),
                Set.of("draft:frontend-tools", "draft:web-development"));

        assertEquals("small:frontend-development", mappings.get(0).logicalFolderKey());
        assertEquals("前端开发与工具", mappings.get(1).folderName());
        var fallbackMappings = service.parseSmallPoolCanonicalization(
                JSONUtil.parseArray("[{\"draftFolderKey\":\"draft:frontend-tools\",\"logicalFolderKey\":\"small:frontend-development\",\"folderName\":\"前端开发与工具\"}]"),
                Set.of("draft:frontend-tools", "draft:web-development"));
        assertEquals(2, fallbackMappings.size());
    }

    @Test
    void fallsBackToTheDraftFolderWhenTheAiOmitsCanonicalMappings() {
        var mappings = service.parseSmallPoolCanonicalization(
                JSONUtil.parseArray("[{\"draftFolderKey\":\"draft:frontend-tools\",\"logicalFolderKey\":\"frontend-development\",\"folderName\":\"前端开发与工具\"}]"),
                Map.of(
                        "draft:frontend-tools", "前端开发工具",
                        "draft:web-development", "网站开发资料"));

        assertEquals(2, mappings.size());
        assertEquals("small:frontend-development", mappings.get(0).logicalFolderKey());
        assertEquals("draft:web-development", mappings.get(1).draftFolderKey());
        assertEquals("small:web-development", mappings.get(1).logicalFolderKey());
        assertEquals("网站开发资料", mappings.get(1).folderName());
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
