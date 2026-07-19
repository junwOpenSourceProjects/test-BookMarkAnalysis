package wo1261931780.testBookMarkAnalysis.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wo1261931780.testBookMarkAnalysis.entity.AiClassificationResult;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationDomainGroup;
import wo1261931780.testBookMarkAnalysis.entity.AiReclassificationWorkUnit;
import wo1261931780.testBookMarkAnalysis.mapper.AiClassificationResultMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationDomainGroupMapper;
import wo1261931780.testBookMarkAnalysis.mapper.AiReclassificationWorkUnitMapper;

@Service
public class ReclassificationResultPersistenceService {
    private final AiClassificationResultMapper resultMapper;
    private final AiReclassificationDomainGroupMapper domainGroupMapper;
    private final AiReclassificationWorkUnitMapper workUnitMapper;
    private final ReclassificationWorkUnitPlanner planner;

    public ReclassificationResultPersistenceService(
            AiClassificationResultMapper resultMapper,
            AiReclassificationDomainGroupMapper domainGroupMapper,
            AiReclassificationWorkUnitMapper workUnitMapper,
            ReclassificationWorkUnitPlanner planner) {
        this.resultMapper = resultMapper;
        this.domainGroupMapper = domainGroupMapper;
        this.workUnitMapper = workUnitMapper;
        this.planner = planner;
    }

    @Transactional(rollbackFor = Exception.class)
    public int persistFolderNaming(
            AiReclassificationWorkUnit unit,
            AiClientService.AiJsonReply reply,
            ReclassificationAiService.FolderNaming naming) {
        if (unit.getDomainGroupId() == null) {
            throw new IllegalArgumentException("大域名目录命名工作单元缺少域名组");
        }
        AiReclassificationDomainGroup group = domainGroupMapper.selectById(unit.getDomainGroupId());
        if (group == null) {
            throw new IllegalArgumentException("重分类域名组不存在: " + unit.getDomainGroupId());
        }
        group.setLogicalFolderKey("large:" + group.getId());
        group.setFolderName(naming.folderName());
        group.setStatus(ReclassificationConstants.WORK_UNIT_STATUS_SUCCEEDED);
        domainGroupMapper.updateById(group);
        completeUnit(unit, reply);
        return planner.planLargeDomainBookmarkAnalysisUnits(group);
    }

    @Transactional(rollbackFor = Exception.class)
    public void persistBookmarkAnalyses(
            AiReclassificationWorkUnit unit,
            String logicalFolderKey,
            List<ReclassificationAiService.BookmarkAnalysis> analyses,
            AiClientService.AiJsonReply reply) {
        for (ReclassificationAiService.BookmarkAnalysis analysis : analyses) {
            Long bookmarkId = Long.valueOf(analysis.bookmarkId());
            AiClassificationResult result = resultMapper.selectOne(
                    new LambdaQueryWrapper<AiClassificationResult>()
                            .eq(AiClassificationResult::getTaskId, unit.getTaskId())
                            .eq(AiClassificationResult::getBookmarkId, bookmarkId));
            if (result == null) {
                result = new AiClassificationResult();
                result.setId(IdUtil.getSnowflakeNextId());
                result.setTaskId(unit.getTaskId());
                result.setBookmarkId(bookmarkId);
                result.setDomainGroupId(unit.getDomainGroupId());
            }
            result.setSuggestedTitle(analysis.suggestedTitle());
            result.setKeywords(analysis.keywordsJson());
            result.setPageType(analysis.pageType());
            result.setTopicHint(analysis.topicHint());
            result.setConfidence(analysis.confidence());
            result.setReason(analysis.reason());
            result.setAnalysisJson(JSONUtil.toJsonStr(analysis));
            result.setLogicalFolderKey(logicalFolderKey);
            result.setSource("ai");
            result.setStatus("PENDING");
            if (result.getId() == null) {
                resultMapper.insert(result);
            } else if (resultMapper.selectById(result.getId()) == null) {
                resultMapper.insert(result);
            } else {
                resultMapper.updateById(result);
            }
        }
        completeUnit(unit, reply);
    }

    @Transactional(rollbackFor = Exception.class)
    public void markUnitRetryableFailed(AiReclassificationWorkUnit unit, Exception exception) {
        unit.setStatus(ReclassificationConstants.WORK_UNIT_STATUS_RETRYABLE_FAILED);
        unit.setErrorMessage(exception.getMessage());
        unit.setCompletedAt(LocalDateTime.now());
        workUnitMapper.updateById(unit);
    }

    private void completeUnit(AiReclassificationWorkUnit unit, AiClientService.AiJsonReply reply) {
        unit.setStatus(ReclassificationConstants.WORK_UNIT_STATUS_SUCCEEDED);
        unit.setRequestJson(reply.requestJson());
        unit.setRawResponse(reply.rawContent());
        unit.setParsedOutputJson(reply.array().toString());
        unit.setErrorMessage(null);
        unit.setCompletedAt(LocalDateTime.now());
        workUnitMapper.updateById(unit);
    }
}
