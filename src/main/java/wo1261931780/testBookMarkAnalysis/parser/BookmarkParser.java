package wo1261931780.testBookMarkAnalysis.parser;

import wo1261931780.testBookMarkAnalysis.entity.BookMarks;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.parser
 *
 * @author liujiajun_junw
 * @Date 2026-01-04
 * @Description 书签解析器接口
 */
public interface BookmarkParser {

    /**
     * 解析书签文件
     *
     * @param inputStream 输入流
     * @return 解析后的书签列表
     */
    List<BookMarks> parse(InputStream inputStream);

    /**
     * 解析书签HTML内容
     *
     * @param html HTML内容
     * @return 解析后的书签列表
     */
    List<BookMarks> parse(String html);

    /**
     * 获取解析器名称
     *
     * @return 解析器名称
     */
    String getParserName();
}
