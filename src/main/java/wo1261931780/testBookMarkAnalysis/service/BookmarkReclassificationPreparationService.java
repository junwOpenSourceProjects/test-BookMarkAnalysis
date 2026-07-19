package wo1261931780.testBookMarkAnalysis.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationTask;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationDomainGroup;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationSnapshot;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationTaskMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationDomainGroupMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationSnapshotMapper;

@Service
public class BookmarkReclassificationPreparationService {
    private static final String UNKNOWN_DOMAIN = "__unknown__";

    private final BookMarksService bookMarksService;
    private final AiClassificationTaskMapper taskMapper;
    private final AiReclassificationSnapshotMapper snapshotMapper;
    private final AiReclassificationDomainGroupMapper domainGroupMapper;
    private final RegistrableDomainResolver domainResolver;

    public BookmarkReclassificationPreparationService(
            BookMarksService bookMarksService,
            AiClassificationTaskMapper taskMapper,
            AiReclassificationSnapshotMapper snapshotMapper,
            AiReclassificationDomainGroupMapper domainGroupMapper,
            RegistrableDomainResolver domainResolver) {
        this.bookMarksService = bookMarksService;
        this.taskMapper = taskMapper;
        this.snapshotMapper = snapshotMapper;
        this.domainGroupMapper = domainGroupMapper;
        this.domainResolver = domainResolver;
    }

    @Transactional(rollbackFor = Exception.class)
    public PreparedTask prepareNewTask(String apiBaseUrl, String modelName) {
        List<BookMarks> links = new ArrayList<>(
                bookMarksService.list(new LambdaQueryWrapper<BookMarks>().eq(BookMarks::getType, "a")));
        links.sort(Comparator.comparing(BookMarks::getId, Comparator.nullsLast(Long::compareTo)));

        Long taskId = IdUtil.getSnowflakeNextId();
        AiClassificationTask task = new AiClassificationTask();
        task.setId(taskId);
        task.setStrategy("rebuild-domain-ai");
        task.setModelName(modelName);
        task.setApiBaseUrl(apiBaseUrl);
        task.setStatus(ReclassificationConstants.TASK_STATUS_QUEUED);
        task.setPhase(ReclassificationConstants.TASK_PHASE_PREPARING);
        task.setTotalCount(links.size());
        task.setRuleMatchedCount(0);
        task.setAiMatchedCount(0);
        task.setFailedCount(0);
        task.setRecoveryCount(0);
        task.setTotalWorkUnitCount(0);
        task.setCompletedWorkUnitCount(0);
        task.setCreatedFolderCount(0);
        task.setMovedBookmarkCount(0);
        task.setUpdatedTitleCount(0);
        taskMapper.insert(task);

        Map<String, List<AiReclassificationSnapshot>> snapshotsByDomain = new LinkedHashMap<>();
        int ordinal = 0;
        for (BookMarks link : links) {
            String domain = domainResolver.resolve(link.getHref());
            if (domain == null) {
                domain = UNKNOWN_DOMAIN;
            }
            AiReclassificationSnapshot snapshot = new AiReclassificationSnapshot();
            snapshot.setId(IdUtil.getSnowflakeNextId());
            snapshot.setTaskId(taskId);
            snapshot.setBookmarkId(link.getId());
            snapshot.setHref(link.getHref());
            snapshot.setOriginalTitle(link.getTitle());
            snapshot.setRegistrableDomain(domain);
            snapshot.setOrdinal(ordinal++);
            snapshotMapper.insert(snapshot);
            snapshotsByDomain.computeIfAbsent(domain, ignored -> new ArrayList<>()).add(snapshot);
        }

        int largeGroupCount = 0;
        int smallPoolCount = 0;
        for (Map.Entry<String, List<AiReclassificationSnapshot>> entry : snapshotsByDomain.entrySet()) {
            List<AiReclassificationSnapshot> snapshots = entry.getValue();
            boolean largeGroup = snapshots.size() >= ReclassificationConstants.LARGE_DOMAIN_MIN_BOOKMARKS;
            String groupType = largeGroup
                    ? ReclassificationConstants.SNAPSHOT_POOL_LARGE_DOMAIN
                    : ReclassificationConstants.SNAPSHOT_POOL_SMALL_POOL;
            if (largeGroup) {
                largeGroupCount++;
            } else {
                smallPoolCount += snapshots.size();
            }

            AiReclassificationDomainGroup domainGroup = new AiReclassificationDomainGroup();
            domainGroup.setId(IdUtil.getSnowflakeNextId());
            domainGroup.setTaskId(taskId);
            domainGroup.setRegistrableDomain(entry.getKey());
            domainGroup.setBookmarkCount(snapshots.size());
            domainGroup.setGroupType(groupType);
            domainGroup.setStatus(ReclassificationConstants.WORK_UNIT_STATUS_PENDING);
            domainGroupMapper.insert(domainGroup);

            for (AiReclassificationSnapshot snapshot : snapshots) {
                snapshot.setPoolType(groupType);
                snapshotMapper.updateById(snapshot);
            }
        }

        bookMarksService.clearParentIdsForLinks();
        bookMarksService.deleteAllFolders();
        task.setTreeClearedAt(LocalDateTime.now());
        task.setPhase(ReclassificationConstants.TASK_PHASE_LARGE_DOMAINS);
        taskMapper.updateById(task);
        return new PreparedTask(taskId, links.size(), largeGroupCount, smallPoolCount);
    }

    public record PreparedTask(Long taskId, int totalBookmarks, int largeGroupCount, int smallPoolCount) {}
}
