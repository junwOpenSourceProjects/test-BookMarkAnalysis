package wo1261931780.testBookMarkAnalysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("bookmark_ai_classification_task")
public class AiClassificationTask {
    @TableId(type = IdType.ASSIGN_ID) private Long id;
    private String strategy;
    private String modelName;
    private String status;
    private Integer totalCount;
    private Integer ruleMatchedCount;
    private Integer aiMatchedCount;
    private Integer failedCount;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String errorMessage;
}
