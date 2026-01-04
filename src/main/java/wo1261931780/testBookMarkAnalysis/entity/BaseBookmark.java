package wo1261931780.testBookMarkAnalysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.entity
 *
 * @author liujiajun_junw
 * @Date 2026-01-04
 * @Description 书签基础实体类，提取公共字段
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseBookmark implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键（雪花ID，后端生成）
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID（雪花ID）")
    private Long id;

    /**
     * 链接地址
     */
    @TableField(value = "href")
    @Schema(description = "链接地址")
    private String href;

    /**
     * 新增时间（Unix时间戳）
     */
    @TableField(value = "add_date")
    @Schema(description = "新增时间（Unix时间戳）")
    private Long addDate;

    /**
     * 标题
     */
    @TableField(value = "title")
    @Schema(description = "书签标题")
    private String title;

    /**
     * 类型：a-链接，h3-文件夹
     */
    @TableField(value = "`type`")
    @Schema(description = "类型：a-链接，h3-文件夹")
    private String type;

    /**
     * 最后修改时间（Unix时间戳）
     */
    @TableField(value = "last_modified")
    @Schema(description = "最后修改时间（Unix时间戳）")
    private Long lastModified;
}
