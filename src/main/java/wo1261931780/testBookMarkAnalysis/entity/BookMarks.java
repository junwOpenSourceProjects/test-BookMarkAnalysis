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
}
