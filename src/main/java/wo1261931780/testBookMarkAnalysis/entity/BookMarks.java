package wo1261931780.testBookMarkAnalysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.entity
 *
 * @author liujiajun_junw
 * @Date 2023-11-15-18  星期二
 * @Description 书签实体类（原始数据表）
 */
@Schema(description = "书签实体（原始数据）")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName(value = "book_marks")
public class BookMarks extends BaseBookmark {

    private static final long serialVersionUID = 1L;

    /**
     * 列名常量已移至 {@link BookmarkColumns}
     * @deprecated 使用 BookmarkColumns.COL_* 替代
     */
    @Deprecated
    public static final String COL_ID = BookmarkColumns.COL_ID;
    @Deprecated
    public static final String COL_HREF = BookmarkColumns.COL_HREF;
    @Deprecated
    public static final String COL_ADD_DATE = BookmarkColumns.COL_ADD_DATE;
    @Deprecated
    public static final String COL_TITLE = BookmarkColumns.COL_TITLE;
    @Deprecated
    public static final String COL_TYPE = BookmarkColumns.COL_TYPE;
    @Deprecated
    public static final String COL_LAST_MODIFIED = BookmarkColumns.COL_LAST_MODIFIED;
}
