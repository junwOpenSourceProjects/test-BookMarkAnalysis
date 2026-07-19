package wo1261931780.testBookMarkAnalysis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationWorkUnit;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationWorkUnitMapper;

@Service
public class ReclassificationWorkUnitClaimService {
    private final AiReclassificationWorkUnitMapper workUnitMapper;

    public ReclassificationWorkUnitClaimService(AiReclassificationWorkUnitMapper workUnitMapper) {
        this.workUnitMapper = workUnitMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public AiReclassificationWorkUnit claimNext(Long taskId) {
        AiReclassificationWorkUnit candidate = workUnitMapper.selectNextClaimableUnit(taskId);
        if (candidate == null) {
            return null;
        }
        if (workUnitMapper.markUnitRunning(candidate.getId()) != 1) {
            return null;
        }
        candidate.setStatus(ReclassificationConstants.WORK_UNIT_STATUS_RUNNING);
        candidate.setAttemptCount(candidate.getAttemptCount() == null ? 1 : candidate.getAttemptCount() + 1);
        return candidate;
    }
}
