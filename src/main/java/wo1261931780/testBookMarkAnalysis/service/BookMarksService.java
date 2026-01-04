package wo1261931780.testBookMarkAnalysis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;
import wo1261931780.testBookMarkAnalysis.mapper.BookMarksMapper;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.service
 *
 * @author liujiajun_junw
 * @Date 2023-11-15-18  星期二
 * @Description 书签服务类
 */
@Service
public class BookMarksService extends ServiceImpl<BookMarksMapper, BookMarks> {

    @Transactional(rollbackFor = Exception.class)
    public int updateBatch(List<BookMarks> list) {
        return baseMapper.updateBatch(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateBatchSelective(List<BookMarks> list) {
        return baseMapper.updateBatchSelective(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<BookMarks> list) {
        return baseMapper.batchInsert(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchInsert2(List<BookMarks> list) {
        return baseMapper.batchInsert2(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public int customInsertOrUpdate(BookMarks record) {
        return baseMapper.customInsertOrUpdate(record);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insertOrUpdateSelective(BookMarks record) {
        return baseMapper.insertOrUpdateSelective(record);
    }
}
