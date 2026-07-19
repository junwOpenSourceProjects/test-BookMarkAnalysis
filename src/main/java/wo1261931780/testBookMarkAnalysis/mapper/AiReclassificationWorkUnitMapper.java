package wo1261931780.testBookMarkAnalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationWorkUnit;

@Mapper
public interface AiReclassificationWorkUnitMapper extends BaseMapper<AiReclassificationWorkUnit> {
    int markRunningUnitsRetryableFailed();

    AiReclassificationWorkUnit selectNextClaimableUnit(@Param("taskId") Long taskId);

    int markUnitRunning(@Param("unitId") Long unitId);

    int countIncompleteAnalysisUnitsForGroup(
            @Param("taskId") Long taskId, @Param("domainGroupId") Long domainGroupId);

    int countByTask(@Param("taskId") Long taskId);

    int countSucceededByTask(@Param("taskId") Long taskId);
}
