package wo1261931780.testBookMarkAnalysis.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationResult;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationFolderApplication;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationResultMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationFolderApplicationMapper;

@Service
public class ReclassificationApplicationService {
    private static final String APPLICATION_STATUS_APPLYING = "APPLYING";
    private static final String APPLICATION_STATUS_APPLIED = "APPLIED";

    private final BookMarksService bookMarksService;
    private final AiClassificationResultMapper resultMapper;
    private final AiReclassificationFolderApplicationMapper applicationMapper;

    public ReclassificationApplicationService(
            BookMarksService bookMarksService,
            AiClassificationResultMapper resultMapper,
            AiReclassificationFolderApplicationMapper applicationMapper) {
        this.bookMarksService = bookMarksService;
        this.resultMapper = resultMapper;
        this.applicationMapper = applicationMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public ApplicationStats applyFolder(
            Long taskId, String logicalFolderKey, String folderName, String sourcePhase) {
        AiReclassificationFolderApplication application =
                applicationMapper.selectOne(
                        new LambdaQueryWrapper<AiReclassificationFolderApplication>()
                                .eq(AiReclassificationFolderApplication::getTaskId, taskId)
                                .eq(
                                        AiReclassificationFolderApplication::getLogicalFolderKey,
                                        logicalFolderKey));
        int createdFolders = 0;
        int movedBookmarks = 0;
        int updatedTitles = 0;
        if (application == null) {
            application = new AiReclassificationFolderApplication();
            application.setId(IdUtil.getSnowflakeNextId());
            application.setTaskId(taskId);
            application.setLogicalFolderKey(logicalFolderKey);
            application.setFolderName(folderName);
            application.setSourcePhase(sourcePhase);
            application.setStatus(APPLICATION_STATUS_APPLYING);
            applicationMapper.insert(application);
        }

        if (application.getFolderId() == null) {
            BookMarks folder = new BookMarks();
            folder.setId(IdUtil.getSnowflakeNextId());
            folder.setTitle(application.getFolderName());
            folder.setType("h3");
            folder.setParentId(null);
            folder.setAddDate(System.currentTimeMillis() / 1000);
            folder.setLastModified(System.currentTimeMillis() / 1000);
            if (!bookMarksService.save(folder)) {
                throw new IllegalStateException("无法创建重分类目录: " + application.getFolderName());
            }
            application.setFolderId(folder.getId());
            createdFolders = 1;
        }

        List<AiClassificationResult> pendingResults =
                resultMapper.selectList(
                        new LambdaQueryWrapper<AiClassificationResult>()
                                .eq(AiClassificationResult::getTaskId, taskId)
                                .eq(AiClassificationResult::getLogicalFolderKey, logicalFolderKey)
                                .eq(AiClassificationResult::getStatus, "PENDING"));
        for (AiClassificationResult result : pendingResults) {
            BookMarks bookmark = new BookMarks();
            bookmark.setId(result.getBookmarkId());
            bookmark.setParentId(application.getFolderId());
            if (result.getSuggestedTitle() != null && !result.getSuggestedTitle().isBlank()) {
                bookmark.setTitle(result.getSuggestedTitle());
            }
            if (!bookMarksService.updateById(bookmark)) {
                throw new IllegalStateException("无法应用书签重分类结果: " + result.getBookmarkId());
            }
            movedBookmarks++;
            if (bookmark.getTitle() != null) {
                updatedTitles++;
            }
            result.setTargetFolderId(application.getFolderId());
            result.setStatus("APPLIED");
            result.setAppliedAt(LocalDateTime.now());
            resultMapper.updateById(result);
        }

        application.setStatus(APPLICATION_STATUS_APPLIED);
        application.setAppliedAt(LocalDateTime.now());
        applicationMapper.updateById(application);
        return new ApplicationStats(createdFolders, movedBookmarks, updatedTitles);
    }

    public ApplicationStats totalsForTask(Long taskId) {
        List<AiReclassificationFolderApplication> applications =
                applicationMapper.selectList(
                        new LambdaQueryWrapper<AiReclassificationFolderApplication>()
                                .eq(AiReclassificationFolderApplication::getTaskId, taskId)
                                .eq(
                                        AiReclassificationFolderApplication::getStatus,
                                        APPLICATION_STATUS_APPLIED));
        int createdFolders = applications.size();
        int movedBookmarks =
                Math.toIntExact(
                        resultMapper.selectCount(
                                new LambdaQueryWrapper<AiClassificationResult>()
                                        .eq(AiClassificationResult::getTaskId, taskId)
                                        .eq(AiClassificationResult::getStatus, "APPLIED")));
        int updatedTitles =
                Math.toIntExact(
                        resultMapper.selectCount(
                                new LambdaQueryWrapper<AiClassificationResult>()
                                        .eq(AiClassificationResult::getTaskId, taskId)
                                        .eq(AiClassificationResult::getStatus, "APPLIED")
                                        .isNotNull(AiClassificationResult::getSuggestedTitle)
                                        .ne(AiClassificationResult::getSuggestedTitle, "")));
        return new ApplicationStats(createdFolders, movedBookmarks, updatedTitles);
    }

    public record ApplicationStats(int createdFolders, int movedBookmarks, int updatedTitles) {}
}
