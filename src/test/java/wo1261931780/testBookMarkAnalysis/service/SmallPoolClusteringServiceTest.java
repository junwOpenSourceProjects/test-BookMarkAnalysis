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
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationResult;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationWorkUnit;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationResultMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationWorkUnitMapper;

@ExtendWith(MockitoExtension.class)
class SmallPoolClusteringServiceTest {

    @Mock private AiClassificationResultMapper resultMapper;
    @Mock private AiReclassificationWorkUnitMapper workUnitMapper;

    @Test
    void createsStableEightyRecordClusterDraftUnitsForSmallPoolResults() {
        when(resultMapper.selectList(any(Wrapper.class))).thenReturn(smallResults(81));
        when(workUnitMapper.selectList(any(Wrapper.class))).thenReturn(List.of());
        SmallPoolClusteringService service = new SmallPoolClusteringService(resultMapper, workUnitMapper);

        int created = service.planClusterDrafts(1L);

        assertEquals(2, created);
        ArgumentCaptor<AiReclassificationWorkUnit> units =
                ArgumentCaptor.forClass(AiReclassificationWorkUnit.class);
        verify(workUnitMapper, times(2)).insert(units.capture());
        assertEquals("small-draft:0001", units.getAllValues().get(0).getUnitKey());
        assertEquals("small-draft:0002", units.getAllValues().get(1).getUnitKey());
        assertEquals(
                ReclassificationConstants.UNIT_SMALL_POOL_CLUSTER_DRAFT,
                units.getAllValues().get(0).getUnitKind());
    }

    private List<AiClassificationResult> smallResults(int count) {
        List<AiClassificationResult> results = new ArrayList<>();
        for (int index = 1; index <= count; index++) {
            AiClassificationResult result = new AiClassificationResult();
            result.setId((long) index + 1000);
            result.setTaskId(1L);
            result.setBookmarkId((long) index);
            result.setSuggestedTitle("Title " + index);
            result.setTopicHint("Topic " + index);
            result.setPageType("文章");
            result.setLogicalFolderKey("small:pending");
            result.setStatus("PENDING");
            results.add(result);
        }
        return results;
    }
}
