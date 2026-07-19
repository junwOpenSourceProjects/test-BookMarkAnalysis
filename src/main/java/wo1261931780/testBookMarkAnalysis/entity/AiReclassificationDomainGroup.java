package wo1261931780.testBookMarkAnalysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bookmark_ai_reclassification_domain_group")
public class AiReclassificationDomainGroup {
    @TableId(type = IdType.ASSIGN_ID) private Long id;
    private Long taskId;
    private String registrableDomain;
    private Integer bookmarkCount;
    private String groupType;
    private String status;
    private String logicalFolderKey;
    private String folderName;
}
