package wo1261931780.testBookMarkAnalysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("bookmark_ai_classification_result")
public class AiClassificationResult {
    @TableId(type = IdType.ASSIGN_ID) private Long id;
    private Long taskId;
    private Long domainGroupId;
    private Long bookmarkId;
    private String originalTitle;
    private String suggestedTitle;
    private String suggestedFolder;
    private String logicalFolderKey;
    private Long targetFolderId;
    private String keywords;
    private String pageType;
    private String topicHint;
    private Integer confidence;
    private String reason;
    private String analysisJson;
    private String source;
    private String status;
    private LocalDateTime appliedAt;
}
