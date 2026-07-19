package wo1261931780.testBookMarkAnalysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bookmark_ai_reclassification_snapshot")
public class AiReclassificationSnapshot {
    @TableId(type = IdType.ASSIGN_ID) private Long id;
    private Long taskId;
    private Long bookmarkId;
    private String href;
    private String originalTitle;
    private String registrableDomain;
    private Integer ordinal;
    private String poolType;
}
