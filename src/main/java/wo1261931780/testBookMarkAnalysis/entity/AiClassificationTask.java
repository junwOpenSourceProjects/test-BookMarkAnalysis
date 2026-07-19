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
    private String apiBaseUrl;
    private String status;
    private String phase;
    private Integer totalCount;
    private Integer ruleMatchedCount;
    private Integer aiMatchedCount;
    private Integer failedCount;
    private Integer recoveryCount;
    private Integer totalWorkUnitCount;
    private Integer completedWorkUnitCount;
    private Integer createdFolderCount;
    private Integer movedBookmarkCount;
    private Integer updatedTitleCount;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime treeClearedAt;
    private LocalDateTime pausedAt;
    private LocalDateTime resumedAt;
    private String errorMessage;
}
