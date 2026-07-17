package wo1261931780.testBookMarkAnalysis.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wo1261931780.testBookMarkAnalysis.config.BookmarkConfig;
import wo1261931780.testBookMarkAnalysis.config.ShowResult;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks2;
import wo1261931780.testBookMarkAnalysis.entity.BookmarkAnalysis;
import wo1261931780.testBookMarkAnalysis.entity.LinkCheckReport;
import wo1261931780.testBookMarkAnalysis.entity.ParseResult;
import wo1261931780.testBookMarkAnalysis.mapper.BookMarksMapper;
import wo1261931780.testBookMarkAnalysis.service.BookMarks2Service;
import wo1261931780.testBookMarkAnalysis.service.BookMarksService;
import wo1261931780.testBookMarkAnalysis.service.BookmarksParserService;
import wo1261931780.testBookMarkAnalysis.service.DomainCategoryMapper;
import wo1261931780.testBookMarkAnalysis.service.LinkCheckService;

/**
 * Created by Intellij IDEA. Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.controller
 *
 * @author liujiajun_junw @Date 2023-11-15-29 星期二 @Description 书签管理控制器
 */
@RestController
@RequestMapping("/BookMarks")
@Slf4j
@Validated
@Tag(name = "书签管理", description = "书签的增删改查和解析导出接口")
public class ShowMeListController {

    @Autowired private BookMarksService bookMarksService;
    @Autowired private BookMarks2Service bookMarks2Service;

    @Autowired private BookMarksMapper bookMarksMapper;
    @Autowired private BookmarksParserService bookmarksParserService;
    @Autowired private BookmarkConfig bookmarkConfig;
    @Autowired private LinkCheckService linkCheckService;

    /**
     * 查询所有书签
     *
     * @param page 页码
     * @param limit 每页条数
     * @param sort 排序
     * @param type 类型
     * @return 书签分页
     */
    @Operation(summary = "查询书签列表", description = "分页查询所有书签")
    @GetMapping("/list")
    public ShowResult<Page<BookMarks>> showMeList(
            @Parameter(description = "页码，最小为1") @RequestParam @Min(value = 1, message = "页码最小为1")
                    Integer page,
            @Parameter(description = "每页条数，1-100")
                    @RequestParam
                    @Min(value = 1, message = "每页条数最小为1")
                    @Max(value = 100, message = "每页条数最大为100")
                    Integer limit,
            @Parameter(description = "排序字段") String sort,
            @Parameter(description = "书签类型") String type) {
        Page<BookMarks> pageInfo = new Page<>(); // 页码，每页条数
        pageInfo.setCurrent(page); // 当前页
        pageInfo.setSize(limit); // 每页条数
        LambdaQueryWrapper<BookMarks> wrapper = new LambdaQueryWrapper<>();
        // 默认只返回链接类型，保持与 /search 一致；传入 type=all 可查看全部
        if ("all".equalsIgnoreCase(type)) {
            // 不过滤
        } else if (StrUtil.isNotBlank(type)) {
            wrapper.eq(BookMarks::getType, type);
        } else {
            wrapper.eq(BookMarks::getType, "a");
        }
        Page<BookMarks> testPage = bookmarksParserService.page(pageInfo, wrapper);
        return ShowResult.sendSuccess(testPage);
    }

    @Operation(summary = "查询所有书签", description = "不分页查询所有书签，用于生成前端全量树状图")
    @GetMapping("/all")
    public ShowResult<List<BookMarks>> showAll() {
        return ShowResult.sendSuccess(bookmarksParserService.list());
    }

    @Operation(summary = "仪表盘统计", description = "返回书签总数、文件夹数、标签数和近7天新增数")
    @GetMapping("/stats")
    public ShowResult<Map<String, Object>> stats() {
        Map<String, Object> stats = new java.util.HashMap<>();
        long total = bookMarksService.count();
        long folders =
                bookMarksService.count(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<
                                        BookMarks>()
                                .eq(BookMarks::getType, "h3"));
        long recent =
                bookMarksService.count(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<
                                        BookMarks>()
                                .eq(BookMarks::getType, "a")
                                .ge(
                                        BookMarks::getAddDate,
                                        System.currentTimeMillis() / 1000 - 7 * 24 * 60 * 60));
        stats.put("total", total);
        stats.put("folders", folders);
        stats.put("tags", 0);
        stats.put("recent", recent);
        return ShowResult.sendSuccess(stats);
    }

    @Operation(summary = "搜索书签", description = "按标题或链接搜索类型为a的书签")
    @GetMapping("/search")
    public ShowResult<Page<BookMarks>> search(
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码，最小为1") @RequestParam @Min(value = 1, message = "页码最小为1")
                    Integer page,
            @Parameter(description = "每页条数，1-100")
                    @RequestParam
                    @Min(value = 1, message = "每页条数最小为1")
                    @Max(value = 100, message = "每页条数最大为100")
                    Integer limit) {
        Page<BookMarks> pageInfo = new Page<>(page, limit);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BookMarks> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BookMarks>()
                        .eq(BookMarks::getType, "a")
                        .and(
                                org.apache.commons.lang3.StringUtils.isNotBlank(keyword),
                                w ->
                                        w.like(BookMarks::getTitle, keyword)
                                                .or()
                                                .like(BookMarks::getHref, keyword));
        return ShowResult.sendSuccess(bookMarksService.page(pageInfo, wrapper));
    }

    @Operation(summary = "节点移动", description = "用于资源管理器拖拽节点更改所属父级文件夹")
    @PostMapping("/move")
    public ShowResult<Boolean> moveNode(
            @RequestParam Long nodeId,
            @RequestParam(required = false) Long targetParentId,
            @RequestParam(defaultValue = "0") Integer sortOrder) {
        BookMarks bm = new BookMarks();
        bm.setId(nodeId);
        // Root drop will send targetParentId as null or 0. Since 0 is usually root context in some
        // structures, handle mapping purely to null if desired, or keep direct sync.
        bm.setParentId(targetParentId != null && targetParentId == 0 ? null : targetParentId);
        bm.setSortOrder(sortOrder);
        boolean result = bookMarksService.updateById(bm);
        return ShowResult.sendSuccess(result);
    }

    @Operation(summary = "节点重命名", description = "修改节点标题")
    @PostMapping("/rename")
    public ShowResult<Boolean> renameNode(
            @RequestParam Long nodeId, @RequestParam String newTitle) {
        BookMarks bm = new BookMarks();
        bm.setId(nodeId);
        bm.setTitle(newTitle);
        boolean result = bookMarksService.updateById(bm);
        return ShowResult.sendSuccess(result);
    }

    @Operation(summary = "新建文件夹", description = "在指定父级下创建同级新目录")
    @PostMapping("/createFolder")
    public ShowResult<BookMarks> createFolder(
            @RequestParam(required = false) Long parentId, @RequestParam String folderName) {
        BookMarks folder = new BookMarks();
        folder.setId(IdUtil.getSnowflakeNextId());
        folder.setParentId(parentId != null && parentId == 0 ? null : parentId);
        folder.setTitle(folderName);
        folder.setType("h3");
        folder.setAddDate(System.currentTimeMillis() / 1000);
        folder.setLastModified(System.currentTimeMillis() / 1000);
        bookMarksService.save(folder);
        return ShowResult.sendSuccess(folder);
    }

    @Operation(summary = "批量删除", description = "一键删除选定的多个节点资源")
    @PostMapping("/deleteNodes")
    public ShowResult<Boolean> deleteNodes(@RequestBody List<Long> nodeIds) {
        boolean result = bookMarksService.removeByIds(nodeIds);
        return ShowResult.sendSuccess(result);
    }

    @Operation(summary = "工具箱：一键归档失效链接", description = "将选定的失效节点统一移动到系统预设的保留文件夹下")
    @PostMapping("/toolbox/archiveDeadLinks")
    public ShowResult<Boolean> archiveDeadLinks(@RequestBody List<Long> nodeIds) {
        if (nodeIds == null || nodeIds.isEmpty()) return ShowResult.sendSuccess(true);

        // 查找或者创建系统隔离文件夹
        String archiveName = "[系统归档] 疑似失联区 (404 Archive)";
        BookMarks archiveFolder =
                bookMarksService.getOne(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<
                                        BookMarks>()
                                .eq(BookMarks::getType, "h3")
                                .eq(BookMarks::getTitle, archiveName)
                                .last("LIMIT 1"));

        Long archiveId;
        if (archiveFolder == null) {
            archiveFolder = new BookMarks();
            archiveFolder.setId(IdUtil.getSnowflakeNextId());
            archiveFolder.setParentId(null); // Root级别
            archiveFolder.setTitle(archiveName);
            archiveFolder.setType("h3");
            archiveFolder.setAddDate(System.currentTimeMillis() / 1000);
            archiveFolder.setLastModified(System.currentTimeMillis() / 1000);
            bookMarksService.save(archiveFolder);
            archiveId = archiveFolder.getId();
        } else {
            archiveId = archiveFolder.getId();
        }

        // 执行批量父级归属变更
        com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<BookMarks>
                updateWrapper =
                        new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<>();
        updateWrapper.in(BookMarks::getId, nodeIds).set(BookMarks::getParentId, archiveId);
        boolean result = bookMarksService.update(updateWrapper);

        return ShowResult.sendSuccess(result);
    }

    @Operation(summary = "工具箱：格式化清空", description = "高危：清空所有书签记录")
    @PostMapping("/toolbox/reset")
    public ShowResult<Map<String, Object>> resetDb() {
        long totalBefore = bookMarksService.count();
        // using remove with empty wrapper to delete all safely via Mybatis-plus
        bookMarksService.remove(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>());
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("deletedCount", totalBefore);
        return ShowResult.sendSuccess(result);
    }

    @Operation(summary = "工具箱：一键去重", description = "查找所有重复 href 的链接，保留最早的记录，删除其他")
    @PostMapping("/toolbox/deduplicate")
    public ShowResult<Map<String, Object>> deduplicate() {
        List<BookMarks> all =
                bookMarksService.list(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<
                                        BookMarks>()
                                .isNotNull(BookMarks::getHref));
        Map<String, List<BookMarks>> grouped =
                all.stream().collect(Collectors.groupingBy(BookMarks::getHref));
        List<Long> idsToDelete = new ArrayList<>();
        int duplicateGroups = 0;
        for (List<BookMarks> group : grouped.values()) {
            if (group.size() > 1) {
                duplicateGroups++;
                group.sort(
                        Comparator.comparing(
                                BookMarks::getAddDate, Comparator.nullsLast(Long::compareTo)));
                // 保留最老的一个，也就是排序后的第一个
                for (int i = 1; i < group.size(); i++) {
                    idsToDelete.add(group.get(i).getId());
                }
            }
        }
        if (!idsToDelete.isEmpty()) {
            // 分批清除兜底
            bookMarksService.removeByIds(idsToDelete);
        }
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("totalChecked", all.size());
        result.put("duplicateGroups", duplicateGroups);
        result.put("deletedCount", idsToDelete.size());
        return ShowResult.sendSuccess(result);
    }

    @Autowired
    private wo1261931780.testBookMarkAnalysis.service.BookmarkCategorizationService
            aiCategorizationService;

    @Operation(summary = "工具箱：智能分类补全", description = "使用大语言模型为书签提供归类建议")
    @PostMapping("/toolbox/ai/categorize")
    public ShowResult<List<Map<String, Object>>> aiCategorize(
            @RequestBody Map<String, Object> req) {
        try {
            String apiBaseUrl = (String) req.get("apiBaseUrl");
            String apiKey = (String) req.get("apiKey");
            String modelName = (String) req.get("modelName");
            if (apiBaseUrl == null || apiBaseUrl.isBlank()) {
                apiBaseUrl = bookmarkConfig.getAiApiBaseUrl();
            }
            if (apiKey == null || apiKey.isBlank()) {
                apiKey = bookmarkConfig.getAiApiKey();
            }
            if (modelName == null || modelName.isBlank()) {
                modelName = bookmarkConfig.getAiModelName();
            }
            if (apiKey == null || apiKey.isBlank()) {
                throw new wo1261931780.testBookMarkAnalysis.common.exception.BusinessException(
                        400, "未配置 AI API Key");
            }

            List<?> rawIds = (List<?>) req.get("bookmarkIds");
            if (rawIds == null) {
                rawIds = (List<?>) req.get("ids");
            }
            if (rawIds == null || rawIds.isEmpty()) {
                throw new wo1261931780.testBookMarkAnalysis.common.exception.BusinessException(
                        400, "bookmarkIds/ids 不能为空");
            }
            List<Long> bookmarkIds =
                    rawIds.stream()
                            .map(Object::toString)
                            .map(Long::parseLong)
                            .collect(Collectors.toList());

            List<Map<String, Object>> result =
                    aiCategorizationService.categorizeBookmarks(
                            apiBaseUrl, apiKey, modelName, bookmarkIds);
            return ShowResult.sendSuccess(result);
        } catch (Exception e) {
            log.error("AI 分类异常", e);
            return ShowResult.sendError("AI 分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "全局托管初始化", description = "清空旧分类结构，预备生成人工交互批次")
    @PostMapping("/toolbox/ai/reconstructTree/init")
    public ShowResult<Map<String, Object>> initReconstructTree() {
        return ShowResult.sendSuccess(aiCategorizationService.initReconstructTree());
    }

    @Operation(summary = "获取下一批建议", description = "调用AI同步返回几十条归类推荐供人工确认")
    @PostMapping("/toolbox/ai/reconstructTree/nextBatch")
    public ShowResult<List<Map<String, Object>>> fetchNextBatch(
            @RequestBody Map<String, Object> req) {
        String apiBaseUrl = (String) req.get("apiBaseUrl");
        String apiKey = (String) req.get("apiKey");
        String modelName = (String) req.get("modelName");
        try {
            return ShowResult.sendSuccess(
                    aiCategorizationService.fetchNextBatch(apiBaseUrl, apiKey, modelName));
        } catch (Exception e) {
            log.error("AI 批次请求异常", e);
            return ShowResult.sendError(e.getMessage());
        }
    }

    @Operation(summary = "用户手动确认落库", description = "确认当前批次并立即生效入库")
    @PostMapping("/toolbox/ai/reconstructTree/confirmBatch")
    public ShowResult<Map<String, Object>> confirmBatch(
            @RequestBody List<Map<String, Object>> mappings) {
        return ShowResult.sendSuccess(aiCategorizationService.confirmBatch(mappings));
    }

    @Operation(summary = "工具箱：清理空壳文件夹", description = "递归扫描并删除没有任何所属子元素的空文件夹")
    @PostMapping("/toolbox/clearEmptyFolders")
    public ShowResult<Integer> clearEmptyFolders() {
        int totalDeleted = 0;
        while (true) {
            List<BookMarks> folders =
                    bookMarksService.list(
                            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<
                                            BookMarks>()
                                    .eq(BookMarks::getType, "h3"));
            List<Long> emptyFolderIds = new ArrayList<>();
            for (BookMarks folder : folders) {
                long childCount =
                        bookMarksService.count(
                                new com.baomidou.mybatisplus.core.conditions.query
                                                        .LambdaQueryWrapper<
                                                BookMarks>()
                                        .eq(BookMarks::getParentId, folder.getId()));
                if (childCount == 0) {
                    emptyFolderIds.add(folder.getId());
                }
            }
            if (emptyFolderIds.isEmpty()) {
                break;
            }
            bookMarksService.removeByIds(emptyFolderIds);
            totalDeleted += emptyFolderIds.size();
        }
        return ShowResult.sendSuccess(totalDeleted);
    }

    // ==================== 智能分类接口 ====================

    @Operation(summary = "智能分类预览", description = "按指定策略对书签进行分类预览，返回每个书签的归属文件夹建议")
    @PostMapping("/toolbox/classify")
    public ShowResult<List<Map<String, Object>>> classifyBookmarks(
            @RequestBody Map<String, Object> req) {
        String strategy = (String) req.getOrDefault("strategy", "function");
        @SuppressWarnings("unchecked")
        List<Integer> rawIds = (List<Integer>) req.get("bookmarkIds");
        List<Long> bookmarkIds = null;
        if (rawIds != null && !rawIds.isEmpty()) {
            bookmarkIds = rawIds.stream().map(Long::valueOf).collect(Collectors.toList());
        }
        List<BookMarks> bookmarks;
        if (bookmarkIds != null && !bookmarkIds.isEmpty()) {
            bookmarks = bookMarksService.listByIds(bookmarkIds);
        } else {
            // 未指定 ID 则默认使用全部链接类型书签
            bookmarks = bookMarksService.list(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<BookMarks>()
                            .eq(BookMarks::getType, "a"));
        }

        List<Map<String, Object>> results = new ArrayList<>();
        int matched = 0;
        int unmatched = 0;

        for (BookMarks bm : bookmarks) {
            Map<String, Object> item = new java.util.LinkedHashMap<>();
            item.put("bookmarkId", bm.getId() != null ? bm.getId().toString() : "0");
            item.put("url", bm.getHref());
            item.put("originalTitle", bm.getTitle());
            item.put("needsTitle", DomainCategoryMapper.needsTitleGeneration(bm.getTitle()));

            String folderName = null;
            switch (strategy) {
                case "domain":
                    folderName = DomainCategoryMapper.classifyByDomain(bm.getHref());
                    break;
                case "region":
                    folderName = DomainCategoryMapper.classifyByRegion(bm.getHref(), bm.getTitle());
                    break;
                case "function":
                default:
                    folderName = DomainCategoryMapper.classifyByFunction(bm.getHref());
                    break;
            }
            item.put("suggestedFolder", folderName);

            if (folderName != null) {
                matched++;
            } else {
                unmatched++;
            }
            results.add(item);
        }

        Map<String, Object> wrapper = new java.util.LinkedHashMap<>();
        wrapper.put("strategy", strategy);
        wrapper.put("total", bookmarks.size());
        wrapper.put("matched", matched);
        wrapper.put("unmatched", unmatched);
        wrapper.put("results", results);

        return ShowResult.sendSuccess(Collections.singletonList(wrapper));
    }

    @Autowired
    private wo1261931780.testBookMarkAnalysis.service.TitleGenerationService titleGenerationService;

    @Operation(summary = "AI 标题补全", description = "对空标题或无意义标题的书签，调用 AI 生成完整标题建议")
    @PostMapping("/toolbox/generateTitles")
    public ShowResult<List<Map<String, Object>>> generateTitles(
            @RequestBody Map<String, Object> req) {
        try {
            String apiBaseUrl = (String) req.getOrDefault("apiBaseUrl",
                    bookmarkConfig.getAiApiBaseUrl());
            String apiKey = (String) req.getOrDefault("apiKey",
                    bookmarkConfig.getAiApiKey());
            String modelName = (String) req.getOrDefault("modelName",
                    bookmarkConfig.getAiModelName());
            if (apiKey == null || apiKey.isBlank()) {
                return ShowResult.sendError("未配置 AI API Key");
            }

            @SuppressWarnings("unchecked")
            List<Integer> rawIds = (List<Integer>) req.get("bookmarkIds");
            List<Long> bookmarkIds = null;
            if (rawIds != null && !rawIds.isEmpty()) {
                bookmarkIds = rawIds.stream().map(Long::valueOf).collect(Collectors.toList());
            }

            List<Map<String, Object>> suggestions =
                    titleGenerationService.generateTitles(apiBaseUrl, apiKey, modelName, bookmarkIds);
            return ShowResult.sendSuccess(suggestions);
        } catch (Exception e) {
            log.error("标题补全异常", e);
            return ShowResult.sendError("标题补全失败: " + e.getMessage());
        }
    }

    @Operation(summary = "应用标题补全", description = "将 AI 生成的标题建议写入数据库")
    @PostMapping("/toolbox/applyTitles")
    public ShowResult<Map<String, Object>> applyTitles(
            @RequestBody List<Map<String, Object>> suggestions) {
        int updated = titleGenerationService.applyTitles(suggestions);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("updated", updated);
        result.put("total", suggestions.size());
        return ShowResult.sendSuccess(result);
    }

    @Autowired
    private wo1261931780.testBookMarkAnalysis.service.DeadLinkScannerService deadLinkScannerService;

    @Operation(summary = "工具箱：启动死链探针", description = "异步使用虚拟线程扫描全网死链")
    @PostMapping("/toolbox/scanDeadLinks/start")
    public ShowResult<Boolean> startDeadLinkScan() {
        List<BookMarks> links =
                bookMarksService.list(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<
                                        BookMarks>()
                                .eq(BookMarks::getType, "a")
                                .isNotNull(BookMarks::getHref));
        boolean started = deadLinkScannerService.startScan(links);
        if (started) {
            return ShowResult.sendSuccess(true);
        } else {
            return ShowResult.sendError("探针任务已经在运行中");
        }
    }

    @Operation(summary = "工具箱：巡检扫描进度", description = "获取死链探针的任务进度和结果")
    @GetMapping("/toolbox/scanDeadLinks/progress")
    public ShowResult<Map<String, Object>> getScanProgress() {
        return ShowResult.sendSuccess(deadLinkScannerService.getProgress());
    }

    /**
     * 新增或者更新一条书签
     *
     * @param bookMarks 书签
     * @return 新增或者更新结果
     */
    @PostMapping("/insertOrUpdateOne")
    public ShowResult<Boolean> insertOrUpdateOne(@RequestBody @Valid BookMarks bookMarks) {
        boolean oneResult = bookmarksParserService.saveOrUpdate(bookMarks);
        return ShowResult.sendSuccess(oneResult);
    }

    /**
     * 新增解析后的书签，包含文件夹和重复
     *
     * @return 插入数量
     */
    @PostMapping("/insertOrUpdateBatch")
    public ShowResult<Integer> insertBatchBookMarks() {
        List<BookMarks> bookMarks = bookmarksParserService.parseBookMarks();
        if (bookMarks.isEmpty()) {
            return ShowResult.sendSuccess(0);
        }
        int batchInsert = bookMarksService.batchInsert(bookMarks);
        log.info("解析得到的书签数量：{}", bookMarks.size());
        log.info("批量插入的数量：{}", batchInsert);
        return ShowResult.sendSuccess(batchInsert);
    }

    /**
     * 书签去重后插入新表，不包含文件夹
     *
     * @return 插入结果
     */
    @PostMapping("/insertNewOne")
    @SuppressWarnings("null")
    public ShowResult<Boolean> insertNewOne() {
        List<BookMarks> bookMarksList = new ArrayList<>();
        List<String> oneUrls = bookMarksMapper.selectAll();
        for (String oneUrl : oneUrls) {
            BookMarks bookMarks1 = new BookMarks();
            BookMarks selectedByUrl = bookmarksParserService.selectByUrl(oneUrl);
            if (ObjectUtil.isNull(selectedByUrl)) { // 为空则跳过
                continue;
            }
            BeanUtils.copyProperties(selectedByUrl, bookMarks1);
            bookMarksList.add(bookMarks1);
        }
        log.info("去重后的书签数量：{}", bookMarksList.size());
        int batchInsert = bookMarksService.batchInsert2(bookMarksList);
        return ShowResult.sendSuccess(batchInsert > 0);
    }

    /**
     * 文件夹插入新表
     *
     * @return 插入结果
     */
    @PostMapping("/insertNewH3")
    public ShowResult<Boolean> insertNewH3() {
        List<BookMarks> oneUrls = bookMarksMapper.selectAllH3();
        log.info("当前文件夹数量：{}", oneUrls.size());
        int batchInsert = bookMarksService.batchInsert2(oneUrls);
        return ShowResult.sendSuccess(batchInsert > 0);
    }

    /**
     * 将对象拼接属性到文件中
     *
     * @return 拼接结果
     */
    @PostMapping("/requestWriteHtml")
    public ShowResult<Boolean> requestWriteHtml() {
        List<BookMarks2> bookMarksList = bookMarks2Service.list();
        bookMarksList.sort(
                Comparator.comparing(
                        BookMarks2::getHref, Comparator.nullsLast(Comparator.reverseOrder())));

        // 使用配置文件中的输出路径
        String outputPath = bookmarkConfig.getOutputPath();
        // 处理${user.home}占位符
        if (outputPath.contains("${user.home}")) {
            outputPath = outputPath.replace("${user.home}", System.getProperty("user.home"));
        }

        Path path = Paths.get(outputPath);
        try {
            // 确保父目录存在
            Files.createDirectories(path.getParent());
        } catch (Exception e) {
            log.error("创建目录失败: {}", e.getMessage());
            return ShowResult.sendError("创建输出目录失败: " + e.getMessage());
        }

        log.info("输出文件路径: {}", path.toAbsolutePath());

        StringBuilder content = new StringBuilder();
        bookMarksList.forEach(
                s -> {
                    if (ObjectUtil.isNotNull(s) && s.getType() != null) {
                        switch (s.getType()) {
                            case "a":
                                content.append("<DT><A HREF=\"")
                                        .append(s.getHref())
                                        .append("\" ADD_DATE=\"")
                                        .append(s.getAddDate())
                                        .append("\" ICON=\" \">")
                                        .append(s.getTitle())
                                        .append("</A>\r\n");
                                break;
                            case "h3":
                                content.append("<DT><H3 ADD_DATE=\"")
                                        .append(s.getAddDate())
                                        .append("\" LAST_MODIFIED=\"")
                                        .append(s.getLastModified())
                                        .append("\">")
                                        .append(s.getTitle())
                                        .append("</H3>\r\n");
                                break;
                            default:
                                break;
                        }
                    }
                });

        try {
            Files.writeString(
                    path,
                    content.toString(),
                    StandardCharsets.UTF_8,
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            log.error("写入文件失败: {}", e.getMessage());
            return ShowResult.sendError("写入输出文件失败: " + e.getMessage());
        }

        log.info("书签导出完成，共 {} 条记录", bookMarksList.size());
        return ShowResult.sendSuccess(Boolean.TRUE);
    }

    // ==================== 第二阶段：功能增强接口 ====================

    /**
     * 上传并解析书签文件
     *
     * @param file 书签HTML文件
     * @return 解析结果
     */
    @Operation(summary = "上传书签文件", description = "上传HTML格式的书签文件，使用正则解析并保存到数据库")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ShowResult<ParseResult> uploadBookmarks(
            @Parameter(description = "书签HTML文件") @RequestParam("file") MultipartFile file) {
        ParseResult result = bookmarksParserService.uploadAndParse(file);
        return ShowResult.sendSuccess(result);
    }

    /**
     * 上传并使用Jsoup解析书签文件（推荐，更健壮）
     *
     * @param file 书签HTML文件
     * @return 解析结果
     */
    @Operation(summary = "上传书签文件（Jsoup解析）", description = "上传HTML格式的书签文件，使用Jsoup解析（更健壮，推荐）并保存到数据库")
    @PostMapping(value = "/upload/jsoup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ShowResult<ParseResult> uploadBookmarksWithJsoup(
            @Parameter(description = "书签HTML文件") @RequestParam("file") MultipartFile file) {
        ParseResult result = bookmarksParserService.uploadAndParseWithJsoup(file);
        return ShowResult.sendSuccess(result);
    }

    /**
     * 上传并解析Safari书签文件（plist格式）
     *
     * @param file Safari书签plist文件
     * @return 解析结果
     */
    @Operation(
            summary = "上传Safari书签文件",
            description = "上传Safari的plist格式书签文件（位于~/Library/Safari/Bookmarks.plist）并保存到数据库")
    @PostMapping(value = "/upload/safari", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ShowResult<ParseResult> uploadSafariBookmarks(
            @Parameter(description = "Safari书签plist文件") @RequestParam("file") MultipartFile file) {
        ParseResult result = bookmarksParserService.uploadAndParseWithSafari(file);
        return ShowResult.sendSuccess(result);
    }

    /**
     * 智能上传书签文件（自动识别格式）
     *
     * @param file 书签文件（支持HTML和plist格式）
     * @return 解析结果
     */
    @Operation(summary = "智能上传书签文件", description = "自动识别文件格式（HTML/plist），选择合适的解析器进行解析。推荐使用此接口")
    @PostMapping(value = "/upload/auto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ShowResult<ParseResult> uploadBookmarksAuto(
            @Parameter(description = "书签文件（支持HTML和plist格式）") @RequestParam("file")
                    MultipartFile file) {
        ParseResult result = bookmarksParserService.uploadAndParseAuto(file);
        return ShowResult.sendSuccess(result);
    }

    /**
     * 分析数据库中的书签
     *
     * @return 书签分析结果
     */
    @Operation(summary = "分析书签", description = "统计分析数据库中的书签，包括数量、重复、域名分布等")
    @GetMapping("/analyze")
    public ShowResult<BookmarkAnalysis> analyzeBookmarks() {
        BookmarkAnalysis analysis = bookmarksParserService.analyzeBookmarks();
        return ShowResult.sendSuccess(analysis);
    }

    /**
     * 获取重复的书签URL列表
     *
     * @return 重复URL列表
     */
    @Operation(summary = "获取重复书签", description = "返回所有重复的书签URL列表")
    @GetMapping("/duplicates")
    public ShowResult<List<String>> getDuplicates() {
        BookmarkAnalysis analysis = bookmarksParserService.analyzeBookmarks();
        return ShowResult.sendSuccess(analysis.getDuplicateUrls());
    }

    /**
     * 导出书签
     *
     * @param format 导出格式：html, markdown, json
     * @param response HTTP响应
     */
    @Operation(summary = "导出书签", description = "支持导出为HTML（可导入浏览器）、Markdown、JSON格式")
    @GetMapping("/export")
    public void exportBookmarks(
            @Parameter(description = "导出格式: html, markdown, json")
                    @RequestParam(defaultValue = "html")
                    String format,
            HttpServletResponse response)
            throws IOException {

        String content;
        String fileName;
        String contentType;

        switch (format.toLowerCase()) {
            case "markdown":
            case "md":
                content = bookmarksParserService.exportToMarkdown();
                fileName = "bookmarks_export.md";
                contentType = "text/markdown";
                break;
            case "json":
                content = bookmarksParserService.exportToJson();
                fileName = "bookmarks_export.json";
                contentType = "application/json";
                break;
            case "html":
            default:
                content = bookmarksParserService.exportToHtml();
                fileName = "bookmarks_export.html";
                contentType = "text/html";
                break;
        }

        // 设置响应头
        response.setContentType(contentType + "; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\""
                        + URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                        + "\"");

        // 写入响应
        response.getWriter().write(content);
        response.getWriter().flush();
        log.info("书签导出完成，格式: {}，文件: {}", format, fileName);
    }

    // ==================== 链接检测接口 ====================

    /**
     * 检测书签链接是否有效（同步，限制数量）
     *
     * @param limit 最大检测数量，默认100
     * @return 检测报告
     */
    @Operation(summary = "检测失效链接", description = "检测数据库中书签链接是否可访问，返回失效链接列表。注意：检测大量链接可能需要较长时间")
    @GetMapping("/checkLinks")
    public ShowResult<LinkCheckReport> checkLinks(
            @Parameter(description = "最大检测数量，0表示不限制，默认100")
                    @RequestParam(defaultValue = "100")
                    @Min(value = 0, message = "数量不能为负数")
                    int limit) {
        log.info("开始检测链接，限制数量: {}", limit);
        LinkCheckReport report = linkCheckService.checkAllLinks(limit);
        log.info("链接检测完成，共检测 {} 个，失效 {} 个", report.getTotalChecked(), report.getBrokenCount());
        return ShowResult.sendSuccess(report);
    }

    /**
     * 异步检测所有书签链接
     *
     * @return 任务ID，用于查询进度
     */
    @Operation(summary = "异步检测所有链接", description = "启动异步任务检测所有书签链接，返回任务ID用于查询进度")
    @PostMapping("/checkLinks/async")
    public ShowResult<LinkCheckReport> checkLinksAsync() {
        log.info("启动异步链接检测任务");
        LinkCheckReport report = linkCheckService.checkAllLinksAsync();
        return ShowResult.sendSuccess(report);
    }

    /**
     * 查询异步检测任务进度
     *
     * @param taskId 任务ID
     * @return 检测进度和结果
     */
    @Operation(summary = "查询检测任务进度", description = "根据任务ID查询异步链接检测任务的进度和结果")
    @GetMapping("/checkLinks/progress/{taskId}")
    public ShowResult<LinkCheckReport> getCheckProgress(
            @Parameter(description = "任务ID") @PathVariable String taskId) {
        LinkCheckReport report = linkCheckService.getCheckProgress(taskId);
        return ShowResult.sendSuccess(report);
    }
}
