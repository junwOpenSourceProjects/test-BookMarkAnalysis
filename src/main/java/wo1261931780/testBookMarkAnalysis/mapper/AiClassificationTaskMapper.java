package wo1261931780.testBookMarkAnalysis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationTask;

@Mapper
public interface AiClassificationTaskMapper extends BaseMapper<AiClassificationTask> {
    int markTaskPausedIfRunning(@Param("taskId") Long taskId);

    int markTaskRunningIfResumable(@Param("taskId") Long taskId);

    int markRunningTasksRecoverable();
}
