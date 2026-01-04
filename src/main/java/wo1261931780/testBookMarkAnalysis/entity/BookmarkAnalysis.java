package wo1261931780.testBookMarkAnalysis.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.entity
 *
 * @author liujiajun_junw
 * @Date 2026-01-04
 * @Description 书签分析结果DTO
 */
@Data
@Schema(description = "书签分析结果")
public class BookmarkAnalysis {

    @Schema(description = "总书签数")
    private int totalCount;

    @Schema(description = "文件夹数量")
    private int folderCount;

    @Schema(description = "链接数量")
    private int linkCount;

    @Schema(description = "重复链接数量")
    private int duplicateCount;

    @Schema(description = "域名分布（Top 20）")
    private Map<String, Integer> domainDistribution;

    @Schema(description = "重复的URL列表")
    private List<String> duplicateUrls;

    @Schema(description = "最早添加时间")
    private Long earliestAddDate;

    @Schema(description = "最近添加时间")
    private Long latestAddDate;
}
