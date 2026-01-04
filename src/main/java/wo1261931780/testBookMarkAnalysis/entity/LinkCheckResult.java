package wo1261931780.testBookMarkAnalysis.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
 * @Description 链接检测结果DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "链接检测结果")
public class LinkCheckResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 书签ID
     */
    @Schema(description = "书签ID")
    private Long bookmarkId;

    /**
     * 链接地址
     */
    @Schema(description = "链接地址")
    private String url;

    /**
     * 书签标题
     */
    @Schema(description = "书签标题")
    private String title;

    /**
     * HTTP状态码
     */
    @Schema(description = "HTTP状态码，-1表示连接失败")
    private int statusCode;

    /**
     * 是否可访问
     */
    @Schema(description = "链接是否可访问")
    private boolean accessible;

    /**
     * 错误信息
     */
    @Schema(description = "错误信息，访问成功时为null")
    private String errorMessage;

    /**
     * 响应时间（毫秒）
     */
    @Schema(description = "响应时间（毫秒）")
    private long responseTimeMs;
}
