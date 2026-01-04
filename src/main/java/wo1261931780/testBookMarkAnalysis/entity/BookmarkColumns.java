package wo1261931780.testBookMarkAnalysis.entity;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.entity
 *
 * @author liujiajun_junw
 * @Date 2026-01-04
 * @Description 书签字段常量类，提供数据库列名常量
 */
public final class BookmarkColumns {

    private BookmarkColumns() {
        // 私有构造函数，防止实例化
    }

    /**
     * 主键列名
     */
    public static final String COL_ID = "id";

    /**
     * 链接列名
     */
    public static final String COL_HREF = "href";

    /**
     * 添加时间列名
     */
    public static final String COL_ADD_DATE = "add_date";

    /**
     * 标题列名
     */
    public static final String COL_TITLE = "title";

    /**
     * 类型列名
     */
    public static final String COL_TYPE = "type";

    /**
     * 最后修改时间列名
     */
    public static final String COL_LAST_MODIFIED = "last_modified";

    // ==================== 类型常量 ====================

    /**
     * 书签链接类型
     */
    public static final String TYPE_LINK = "a";

    /**
     * 文件夹类型
     */
    public static final String TYPE_FOLDER = "h3";
}
