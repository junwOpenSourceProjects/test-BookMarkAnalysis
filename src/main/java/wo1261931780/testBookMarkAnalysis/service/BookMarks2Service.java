package wo1261931780.testBookMarkAnalysis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks2;
import wo1261931780.testBookMarkAnalysis.mapper.BookMarks2Mapper;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.service
 *
 * @author liujiajun_junw
 * @Date 2023-12-17-47  星期日
 * @Description BookMarks2服务类
 */
@Service
public class BookMarks2Service extends ServiceImpl<BookMarks2Mapper, BookMarks2> {

    @Transactional(rollbackFor = Exception.class)
    public int updateBatch(List<BookMarks2> list) {
        return baseMapper.updateBatch(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateBatchSelective(List<BookMarks2> list) {
        return baseMapper.updateBatchSelective(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<BookMarks2> list) {
        return baseMapper.batchInsert(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public int customInsertOrUpdate(BookMarks2 record) {
        return baseMapper.customInsertOrUpdate(record);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insertOrUpdateSelective(BookMarks2 record) {
        return baseMapper.insertOrUpdateSelective(record);
    }
}
