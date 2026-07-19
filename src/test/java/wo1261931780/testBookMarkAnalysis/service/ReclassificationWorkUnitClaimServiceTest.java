package wo1261931780.testBookMarkAnalysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationWorkUnit;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationWorkUnitMapper;

@ExtendWith(MockitoExtension.class)
class ReclassificationWorkUnitClaimServiceTest {

    @Mock private AiReclassificationWorkUnitMapper workUnitMapper;

    @Test
    void claimsOnlyOnePendingOrRetryableUnitAtATime() {
        AiReclassificationWorkUnit unit = new AiReclassificationWorkUnit();
        unit.setId(11L);
        unit.setStatus(ReclassificationConstants.WORK_UNIT_STATUS_PENDING);
        when(workUnitMapper.selectNextClaimableUnit(1L)).thenReturn(unit);
        when(workUnitMapper.markUnitRunning(11L)).thenReturn(1);

        ReclassificationWorkUnitClaimService service =
                new ReclassificationWorkUnitClaimService(workUnitMapper);

        AiReclassificationWorkUnit claimed = service.claimNext(1L);

        assertEquals(11L, claimed.getId());
        assertEquals(ReclassificationConstants.WORK_UNIT_STATUS_RUNNING, claimed.getStatus());
    }

    @Test
    void returnsNullWhenNoUnitCanBeClaimed() {
        when(workUnitMapper.selectNextClaimableUnit(anyLong())).thenReturn(null);
        ReclassificationWorkUnitClaimService service =
                new ReclassificationWorkUnitClaimService(workUnitMapper);

        assertNull(service.claimNext(1L));
    }
}
