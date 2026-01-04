package wo1261931780.testBookMarkAnalysis.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.entity
 *
 * @author liujiajun_junw
 * @Date 2026-01-04
 * @Description 批量链接检测报告
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "链接检测报告")
public class LinkCheckReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 检测的链接总数
     */
    @Schema(description = "检测的链接总数")
    private int totalChecked;

    /**
     * 可访问的链接数
     */
    @Schema(description = "可访问的链接数")
    private int accessibleCount;

    /**
     * 失效的链接数
     */
    @Schema(description = "失效的链接数")
    private int brokenCount;

    /**
     * 检测耗时（毫秒）
     */
    @Schema(description = "检测耗时（毫秒）")
    private long checkTimeMs;

    /**
     * 失效链接详情列表
     */
    @Schema(description = "失效链接详情列表")
    private List<LinkCheckResult> brokenLinks;

    /**
     * 检测进度（异步时使用）
     */
    @Schema(description = "检测进度百分比（0-100）")
    private int progress;

    /**
     * 任务状态
     */
    @Schema(description = "任务状态：PENDING/RUNNING/COMPLETED/FAILED")
    private String status;

    /**
     * 任务ID（异步时使用）
     */
    @Schema(description = "异步任务ID")
    private String taskId;
}
