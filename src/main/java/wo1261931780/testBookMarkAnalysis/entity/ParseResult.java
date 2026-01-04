package wo1261931780.testBookMarkAnalysis.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.entity
 *
 * @author liujiajun_junw
 * @Date 2026-01-04
 * @Description 解析结果DTO
 */
@Data
@Schema(description = "书签解析结果")
public class ParseResult {

    @Schema(description = "解析的书签总数")
    private int totalParsed;

    @Schema(description = "链接数量")
    private int linkCount;

    @Schema(description = "文件夹数量")
    private int folderCount;

    @Schema(description = "成功插入数据库的数量")
    private int insertedCount;

    @Schema(description = "解析耗时（毫秒）")
    private long parseTimeMs;

    @Schema(description = "文件名")
    private String fileName;
}
