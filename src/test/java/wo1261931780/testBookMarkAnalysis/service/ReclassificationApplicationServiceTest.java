package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationResult;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationFolderApplication;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationResultMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationFolderApplicationMapper;

@ExtendWith(MockitoExtension.class)
class ReclassificationApplicationServiceTest {

    @Mock private BookMarksService bookMarksService;
    @Mock private AiClassificationResultMapper resultMapper;
    @Mock private AiReclassificationFolderApplicationMapper applicationMapper;

    @Test
    void createsOneFolderMovesPendingBookmarksAndMarksResultsApplied() {
        AiClassificationResult first = pendingResult(1L);
        AiClassificationResult second = pendingResult(2L);
        when(applicationMapper.selectOne(any(Wrapper.class))).thenReturn(null);
        when(resultMapper.selectList(any(Wrapper.class))).thenReturn(List.of(first, second));
        when(bookMarksService.save(any(BookMarks.class))).thenReturn(true);
        when(bookMarksService.updateById(any(BookMarks.class))).thenReturn(true);

        ReclassificationApplicationService service = new ReclassificationApplicationService(
                bookMarksService, resultMapper, applicationMapper);

        ReclassificationApplicationService.ApplicationStats stats = service.applyFolder(
                10L, "large:github", "GitHub 开源项目", "LARGE_DOMAINS");

        assertEquals(1, stats.createdFolders());
        assertEquals(2, stats.movedBookmarks());
        verify(applicationMapper, times(1)).insert(any(AiReclassificationFolderApplication.class));
        verify(bookMarksService, times(1)).save(any(BookMarks.class));
        verify(bookMarksService, times(2)).updateById(any(BookMarks.class));
        verify(resultMapper, times(2)).updateById(any(AiClassificationResult.class));
    }

    @Test
    void reusesPersistedFolderInsteadOfCreatingDuplicateOnResume() {
        AiReclassificationFolderApplication application = new AiReclassificationFolderApplication();
        application.setFolderId(999L);
        when(applicationMapper.selectOne(any(Wrapper.class))).thenReturn(application);
        when(resultMapper.selectList(any(Wrapper.class))).thenReturn(List.of());

        ReclassificationApplicationService service = new ReclassificationApplicationService(
                bookMarksService, resultMapper, applicationMapper);

        ReclassificationApplicationService.ApplicationStats stats = service.applyFolder(
                10L, "large:github", "GitHub 开源项目", "LARGE_DOMAINS");

        assertEquals(0, stats.createdFolders());
        verify(bookMarksService, never()).save(any(BookMarks.class));
    }

    private AiClassificationResult pendingResult(long bookmarkId) {
        AiClassificationResult result = new AiClassificationResult();
        result.setId(bookmarkId + 100L);
        result.setTaskId(10L);
        result.setBookmarkId(bookmarkId);
        result.setSuggestedTitle("Updated title " + bookmarkId);
        result.setStatus("PENDING");
        return result;
    }
}
