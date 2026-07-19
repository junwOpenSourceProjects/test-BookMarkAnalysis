package wo1261931780.testBookMarkAnalysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("bookmark_ai_reclassification_folder_application")
public class AiReclassificationFolderApplication {
    @TableId(type = IdType.ASSIGN_ID) private Long id;
    private Long taskId;
    private String logicalFolderKey;
    private String folderName;
    private Long folderId;
    private String sourcePhase;
    private String status;
    private LocalDateTime appliedAt;
}
