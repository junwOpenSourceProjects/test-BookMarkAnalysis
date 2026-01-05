package wo1261931780.testBookMarkAnalysis.controller;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
import wo1261931780.testBookMarkAnalysis.service.LinkCheckService;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.controller
 *
 * @author liujiajun_junw
 * @Date 2023-11-15-29 星期二
 * @Description 书签管理控制器
 */
@RestController
@RequestMapping("/BookMarks")
@Slf4j
@Validated
@Tag(name = "书签管理", description = "书签的增删改查和解析导出接口")
public class ShowMeListController {

	@Autowired
	private BookMarksService bookMarksService;
	@Autowired
	private BookMarks2Service bookMarks2Service;

	@Autowired
	private BookMarksMapper bookMarksMapper;
	@Autowired
	private BookmarksParserService bookmarksParserService;
	@Autowired
	private BookmarkConfig bookmarkConfig;
	@Autowired
	private LinkCheckService linkCheckService;

	/**
	 * 查询所有书签
	 *
	 * @param page  页码
	 * @param limit 每页条数
	 * @param sort  排序
	 * @param type  类型
	 * @return 书签分页
	 */
	@Operation(summary = "查询书签列表", description = "分页查询所有书签")
	@GetMapping("/list")
	public ShowResult<Page<BookMarks>> showMeList(
			@Parameter(description = "页码，最小为1") @RequestParam @Min(value = 1, message = "页码最小为1") Integer page,
			@Parameter(description = "每页条数，1-100") @RequestParam @Min(value = 1, message = "每页条数最小为1") @Max(value = 100, message = "每页条数最大为100") Integer limit,
			@Parameter(description = "排序字段") String sort,
			@Parameter(description = "书签类型") String type) {
		Page<BookMarks> pageInfo = new Page<>();// 页码，每页条数
		pageInfo.setCurrent(page);// 当前页
		pageInfo.setSize(limit);// 每页条数
		Page<BookMarks> testPage = bookmarksParserService.page(pageInfo);
		return ShowResult.sendSuccess(testPage);
	}

	/**
	 * 新增或者更新一条书签
	 *
	 * @param bookMarks 书签
	 * @return 新增或者更新结果
	 */
	@PostMapping("/insertOrUpdateOne")
	public ShowResult<Boolean> insertOrUpdateOne(BookMarks bookMarks) {
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
			if (ObjectUtil.isNull(selectedByUrl)) {// 为空则跳过
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
		bookMarksList.sort(Comparator.comparing(BookMarks2::getHref, Comparator.nullsLast(Comparator.reverseOrder())));

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

		File outputFile = path.toFile();
		log.info("输出文件路径: {}", outputFile.getAbsolutePath());

		bookMarksList.forEach(s -> {
			if (ObjectUtil.isNotNull(s) && s.getType() != null) {
				FileWriter writer = new FileWriter(outputFile);
				switch (s.getType()) {
					case "a":
						writer.append("<DT><A HREF=\"" + s.getHref() + "\" ADD_DATE=\"" + s.getAddDate()
								+ "\" ICON=\" \">" + s.getTitle() + "</A>\r\n");
						break;
					case "h3":
						writer.append("<DT><H3 ADD_DATE=\"" + s.getAddDate() + "\" LAST_MODIFIED=\""
								+ s.getLastModified() + "\">" + s.getTitle() + "</H3>\r\n");
						break;
					default:
						break;
				}
			}
		});

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
	@Operation(summary = "上传Safari书签文件", description = "上传Safari的plist格式书签文件（位于~/Library/Safari/Bookmarks.plist）并保存到数据库")
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
			@Parameter(description = "书签文件（支持HTML和plist格式）") @RequestParam("file") MultipartFile file) {
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
	 * @param format   导出格式：html, markdown, json
	 * @param response HTTP响应
	 */
	@Operation(summary = "导出书签", description = "支持导出为HTML（可导入浏览器）、Markdown、JSON格式")
	@GetMapping("/export")
	public void exportBookmarks(
			@Parameter(description = "导出格式: html, markdown, json") @RequestParam(defaultValue = "html") String format,
			HttpServletResponse response) throws IOException {

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
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + "\"");

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
			@Parameter(description = "最大检测数量，0表示不限制，默认100") @RequestParam(defaultValue = "100") @Min(value = 0, message = "数量不能为负数") int limit) {
		log.info("开始检测链接，限制数量: {}", limit);
		LinkCheckReport report = linkCheckService.checkAllLinks(limit);
		log.info("链接检测完成，共检测 {} 个，失效 {} 个",
				report.getTotalChecked(), report.getBrokenCount());
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
		linkCheckService.checkAllLinksAsync();
		// 返回初始状态，客户端需要通过taskId查询进度
		return ShowResult.sendSuccess(LinkCheckReport.builder()
				.status("STARTED")
				.progress(0)
				.build());
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
