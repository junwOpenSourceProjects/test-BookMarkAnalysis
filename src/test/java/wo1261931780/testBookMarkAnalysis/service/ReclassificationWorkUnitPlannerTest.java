package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationDomainGroup;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationSnapshot;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationWorkUnit;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationDomainGroupMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationSnapshotMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationWorkUnitMapper;

@ExtendWith(MockitoExtension.class)
class ReclassificationWorkUnitPlannerTest {

    @Mock private AiReclassificationDomainGroupMapper domainGroupMapper;
    @Mock private AiReclassificationSnapshotMapper snapshotMapper;
    @Mock private AiReclassificationWorkUnitMapper workUnitMapper;

    @Test
    void createsOneLargeDomainNameUnitAndBatchedSmallPoolAnalysisUnits() {
        AiReclassificationDomainGroup largeGroup = new AiReclassificationDomainGroup();
        largeGroup.setId(100L);
        largeGroup.setTaskId(1L);
        largeGroup.setRegistrableDomain("github.com");
        largeGroup.setBookmarkCount(5);
        largeGroup.setGroupType(ReclassificationConstants.SNAPSHOT_POOL_LARGE_DOMAIN);
        when(domainGroupMapper.selectList(any(Wrapper.class))).thenReturn(List.of(largeGroup));
        when(snapshotMapper.selectList(any(Wrapper.class)))
                .thenReturn(largeGroupSnapshots(), smallPoolSnapshots(30));
        when(workUnitMapper.selectList(any(Wrapper.class))).thenReturn(List.of());

        ReclassificationWorkUnitPlanner planner =
                new ReclassificationWorkUnitPlanner(
                        domainGroupMapper, snapshotMapper, workUnitMapper);

        int created = planner.planInitialWorkUnits(1L);

        assertEquals(3, created);
        ArgumentCaptor<AiReclassificationWorkUnit> units =
                ArgumentCaptor.forClass(AiReclassificationWorkUnit.class);
        verify(workUnitMapper, times(3)).insert(units.capture());
        assertEquals(
                "large-folder:100",
                units.getAllValues().get(0).getUnitKey());
        assertEquals(
                ReclassificationConstants.UNIT_LARGE_DOMAIN_FOLDER_NAME,
                units.getAllValues().get(0).getUnitKind());
        assertEquals(true, units.getAllValues().get(0).getInputJson().contains("github-bookmark-1"));
        assertEquals("small-analysis:0001", units.getAllValues().get(1).getUnitKey());
        assertEquals("small-analysis:0002", units.getAllValues().get(2).getUnitKey());
    }

    private List<AiReclassificationSnapshot> largeGroupSnapshots() {
        List<AiReclassificationSnapshot> snapshots = new ArrayList<>();
        for (int index = 0; index < 5; index++) {
            AiReclassificationSnapshot snapshot = new AiReclassificationSnapshot();
            snapshot.setId((long) index + 101);
            snapshot.setTaskId(1L);
            snapshot.setBookmarkId((long) index + 101);
            snapshot.setHref("https://docs.github.com/github-bookmark-" + (index + 1));
            snapshot.setOriginalTitle("GitHub " + index);
            snapshot.setRegistrableDomain("github.com");
            snapshot.setOrdinal(index);
            snapshot.setPoolType(ReclassificationConstants.SNAPSHOT_POOL_LARGE_DOMAIN);
            snapshots.add(snapshot);
        }
        return snapshots;
    }

    private List<AiReclassificationSnapshot> smallPoolSnapshots(int count) {
        List<AiReclassificationSnapshot> snapshots = new ArrayList<>();
        for (int index = 0; index < count; index++) {
            AiReclassificationSnapshot snapshot = new AiReclassificationSnapshot();
            snapshot.setId((long) index + 1);
            snapshot.setTaskId(1L);
            snapshot.setBookmarkId((long) index + 1);
            snapshot.setHref("https://small-" + index + ".example.com");
            snapshot.setOriginalTitle("Small " + index);
            snapshot.setRegistrableDomain("example.com");
            snapshot.setOrdinal(index);
            snapshot.setPoolType(ReclassificationConstants.SNAPSHOT_POOL_SMALL_POOL);
            snapshots.add(snapshot);
        }
        return snapshots;
    }
}
