package wo1261931780.testBookMarkAnalysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("bookmark_ai_reclassification_work_unit")
public class AiReclassificationWorkUnit {
    @TableId(type = IdType.ASSIGN_ID) private Long id;
    private Long taskId;
    private Long domainGroupId;
    private String unitKind;
    private String unitKey;
    private String status;
    private Integer ordinal;
    private Integer attemptCount;
    private String promptVersion;
    private String inputJson;
    private String requestJson;
    private String rawResponse;
    private String parsedOutputJson;
    private String errorMessage;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
