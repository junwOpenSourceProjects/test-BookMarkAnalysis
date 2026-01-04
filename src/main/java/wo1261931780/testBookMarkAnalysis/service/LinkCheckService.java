package wo1261931780.testBookMarkAnalysis.service;

import wo1261931780.testBookMarkAnalysis.entity.LinkCheckReport;
import wo1261931780.testBookMarkAnalysis.entity.LinkCheckResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.service
 *
 * @author liujiajun_junw
 * @Date 2026-01-04
 * @Description 链接检测服务接口
 */
public interface LinkCheckService {

    /**
     * 检测单个链接是否可访问
     *
     * @param url 链接地址
     * @return 检测结果
     */
    LinkCheckResult checkLink(String url);

    /**
     * 批量检测链接（同步）
     *
     * @param urls 链接地址列表
     * @return 检测报告
     */
    LinkCheckReport checkLinks(List<String> urls);

    /**
     * 检测数据库中所有书签链接（异步）
     *
     * @return 异步任务，返回检测报告
     */
    CompletableFuture<LinkCheckReport> checkAllLinksAsync();

    /**
     * 检测数据库中所有书签链接（同步，限制数量）
     *
     * @param limit 最大检测数量，0表示不限制
     * @return 检测报告
     */
    LinkCheckReport checkAllLinks(int limit);

    /**
     * 获取异步检测任务的进度
     *
     * @param taskId 任务ID
     * @return 检测报告（包含进度信息）
     */
    LinkCheckReport getCheckProgress(String taskId);
}
