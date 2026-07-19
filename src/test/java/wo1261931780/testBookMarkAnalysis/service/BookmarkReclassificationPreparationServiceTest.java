package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationTask;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationDomainGroup;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationSnapshot;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationTaskMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationDomainGroupMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationSnapshotMapper;

@ExtendWith(MockitoExtension.class)
class BookmarkReclassificationPreparationServiceTest {

    @Mock private BookMarksService bookMarksService;
    @Mock private AiClassificationTaskMapper taskMapper;
    @Mock private AiReclassificationSnapshotMapper snapshotMapper;
    @Mock private AiReclassificationDomainGroupMapper domainGroupMapper;

    @Test
    void snapshotsBeforeClearingFoldersAndUsesTheFiveBookmarkThreshold() {
        List<BookMarks> links = new ArrayList<>();
        for (long id = 1; id <= 5; id++) {
            links.add(link(id, "https://docs.github.com/page-" + id));
        }
        for (long id = 6; id <= 9; id++) {
            links.add(link(id, "https://small-" + id + ".example.com/article"));
        }
        when(bookMarksService.list(any(Wrapper.class))).thenReturn(links);

        BookmarkReclassificationPreparationService service =
                new BookmarkReclassificationPreparationService(
                        bookMarksService,
                        taskMapper,
                        snapshotMapper,
                        domainGroupMapper,
                        new RegistrableDomainResolver());

        BookmarkReclassificationPreparationService.PreparedTask prepared =
                service.prepareNewTask("https://example.test", "test-model");

        assertEquals(9, prepared.totalBookmarks());
        assertEquals(1, prepared.largeGroupCount());
        assertEquals(4, prepared.smallPoolCount());
        verify(taskMapper, times(1)).insert(any(AiClassificationTask.class));
        verify(snapshotMapper, times(9)).insert(any(AiReclassificationSnapshot.class));
        verify(domainGroupMapper, times(2)).insert(any(AiReclassificationDomainGroup.class));

        InOrder order = inOrder(snapshotMapper, bookMarksService);
        order.verify(snapshotMapper, times(9)).insert(any(AiReclassificationSnapshot.class));
        order.verify(bookMarksService).clearParentIdsForLinks();
        order.verify(bookMarksService).deleteAllFolders();
    }

    private BookMarks link(long id, String href) {
        BookMarks bookmark = new BookMarks();
        bookmark.setId(id);
        bookmark.setHref(href);
        bookmark.setTitle("Bookmark " + id);
        bookmark.setType("a");
        return bookmark;
    }
}
