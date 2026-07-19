package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationResult;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationDomainGroup;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationWorkUnit;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationResultMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationDomainGroupMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationWorkUnitMapper;

@ExtendWith(MockitoExtension.class)
class ReclassificationResultPersistenceServiceTest {

    @Mock private AiClassificationResultMapper resultMapper;
    @Mock private AiReclassificationDomainGroupMapper domainGroupMapper;
    @Mock private AiReclassificationWorkUnitMapper workUnitMapper;
    @Mock private ReclassificationWorkUnitPlanner planner;

    @Test
    void persistsFolderNamingAuditsUnitAndPlansLargeGroupAnalysis() {
        AiReclassificationWorkUnit unit = unit(1L, 100L);
        AiReclassificationDomainGroup group = new AiReclassificationDomainGroup();
        group.setId(100L);
        group.setTaskId(1L);
        when(domainGroupMapper.selectById(100L)).thenReturn(group);
        when(planner.planLargeDomainBookmarkAnalysisUnits(group)).thenReturn(2);

        ReclassificationResultPersistenceService service = new ReclassificationResultPersistenceService(
                resultMapper, domainGroupMapper, workUnitMapper, planner);

        int createdUnits = service.persistFolderNaming(
                unit,
                new AiClientService.AiJsonReply("{}", "[{\"folderName\":\"GitHub 项目\"}]", JSONUtil.createArray()),
                new ReclassificationAiService.FolderNaming("GitHub 项目", "代码协作"));

        assertEquals(2, createdUnits);
        assertEquals("large:100", group.getLogicalFolderKey());
        assertEquals("GitHub 项目", group.getFolderName());
        assertEquals(ReclassificationConstants.WORK_UNIT_STATUS_SUCCEEDED, unit.getStatus());
        verify(domainGroupMapper, times(1)).updateById(group);
        verify(workUnitMapper, times(1)).updateById(unit);
    }

    @Test
    void insertsPendingBookmarkResultsWithTheLogicalFolderKey() {
        AiReclassificationWorkUnit unit = unit(1L, 100L);
        when(resultMapper.selectOne(any(Wrapper.class))).thenReturn(null);
        ReclassificationResultPersistenceService service = new ReclassificationResultPersistenceService(
                resultMapper, domainGroupMapper, workUnitMapper, planner);

        service.persistBookmarkAnalyses(
                unit,
                "large:100",
                List.of(new ReclassificationAiService.BookmarkAnalysis(
                        "10", "GitHub 文档", "[\"GitHub\"]", "文档", "开发", 90, "官方文档")),
                new AiClientService.AiJsonReply("{}", "[]", JSONUtil.createArray()));

        verify(resultMapper, times(1)).insert(any(AiClassificationResult.class));
        verify(workUnitMapper, times(1)).updateById(unit);
    }

    @Test
    void replacesDraftKeysWithCanonicalKeysBeforeCompletingTheWorkUnit() {
        AiReclassificationWorkUnit unit = unit(1L, null);
        AiClassificationResult first = new AiClassificationResult();
        first.setId(99L);
        first.setLogicalFolderKey("draft:frontend-tools");
        AiClassificationResult second = new AiClassificationResult();
        second.setId(100L);
        second.setLogicalFolderKey("draft:web-development");
        when(resultMapper.selectList(any(Wrapper.class))).thenReturn(List.of(first), List.of(second));
        ReclassificationResultPersistenceService service = new ReclassificationResultPersistenceService(
                resultMapper, domainGroupMapper, workUnitMapper, planner);

        service.persistSmallPoolCanonicalAssignments(
                unit,
                List.of(
                        new ReclassificationAiService.CanonicalFolderAssignment(
                                "draft:frontend-tools", "small:frontend-development", "前端开发与工具"),
                        new ReclassificationAiService.CanonicalFolderAssignment(
                                "draft:web-development", "small:frontend-development", "前端开发与工具")),
                new AiClientService.AiJsonReply("{}", "[]", JSONUtil.createArray()));

        assertEquals("small:frontend-development", first.getLogicalFolderKey());
        assertEquals("small:frontend-development", second.getLogicalFolderKey());
        assertEquals("前端开发与工具", first.getSuggestedFolder());
        verify(resultMapper, times(2)).updateById(any(AiClassificationResult.class));
        verify(workUnitMapper).updateById(unit);
    }

    @Test
    void persistsSmallPoolDraftAssignmentsWithoutApplyingThemYet() {
        AiReclassificationWorkUnit unit = unit(1L, null);
        AiClassificationResult existing = new AiClassificationResult();
        existing.setId(99L);
        existing.setTaskId(1L);
        existing.setBookmarkId(10L);
        when(resultMapper.selectOne(any(Wrapper.class))).thenReturn(existing);
        ReclassificationResultPersistenceService service = new ReclassificationResultPersistenceService(
                resultMapper, domainGroupMapper, workUnitMapper, planner);

        service.persistSmallPoolDraftAssignments(
                unit,
                List.of(new ReclassificationAiService.ClusterDraftAssignment(
                        "10", "draft:frontend-tools", "前端开发工具")),
                new AiClientService.AiJsonReply("{}", "[]", JSONUtil.createArray()));

        assertEquals("draft:frontend-tools", existing.getLogicalFolderKey());
        assertEquals("前端开发工具", existing.getSuggestedFolder());
        verify(resultMapper).updateById(existing);
        verify(workUnitMapper).updateById(unit);
    }

    private AiReclassificationWorkUnit unit(Long taskId, Long groupId) {
        AiReclassificationWorkUnit unit = new AiReclassificationWorkUnit();
        unit.setId(9L);
        unit.setTaskId(taskId);
        unit.setDomainGroupId(groupId);
        unit.setUnitKind(ReclassificationConstants.UNIT_LARGE_DOMAIN_BOOKMARK_ANALYSIS);
        return unit;
    }
}
