package wo1261931780.testBookMarkAnalysis.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wo1261931780.testBookMarkAnalysis.common.exception.BusinessException;
import wo1261931780.testBookMarkAnalysis.config.BookmarkConfig;
import wo1261931780.testBookMarkAnalysis.config.CacheConfig;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;
import wo1261931780.testBookMarkAnalysis.entity.BookmarkAnalysis;
import wo1261931780.testBookMarkAnalysis.entity.ParseResult;
import wo1261931780.testBookMarkAnalysis.mapper.BookMarksMapper;
import wo1261931780.testBookMarkAnalysis.parser.BookmarkParser;
import wo1261931780.testBookMarkAnalysis.service.BookMarksService;
import wo1261931780.testBookMarkAnalysis.service.BookmarksParserService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.service.impl
 *
 * @author liujiajun_junw
 * @Date 2023-11-15-38 星期二
 * @Description 书签解析服务实现类
 */
@Slf4j
@Service
public class BookmarksParserServiceImpl extends ServiceImpl<BookMarksMapper, BookMarks>
		implements BookmarksParserService {

	// ==================== 预编译正则表达式（性能优化，保留作为后备方案） ====================
	private static final Pattern H3_TAG_PATTERN = Pattern.compile("<H3([^>]*)>");
	private static final Pattern A_TAG_PATTERN = Pattern.compile("<A([^>]*)>");
	private static final Pattern HREF_PATTERN = Pattern.compile("HREF=\"([^\"]+)\"");
	private static final Pattern ADD_DATE_PATTERN = Pattern.compile("ADD_DATE=\"([^\"]+)\"");
	private static final Pattern LAST_MODIFIED_PATTERN = Pattern.compile("LAST_MODIFIED=\"([^\"]+)\"");
	private static final Pattern H3_TITLE_PATTERN = Pattern.compile("\">([^<]+)</H3>");
	private static final Pattern A_TITLE_PATTERN = Pattern.compile("\">([^<]+)</A>");

	@Autowired
	private BookMarksService bookMarksService;

	@Autowired
	private BookmarkConfig bookmarkConfig;

	@Autowired
	@Qualifier("jsoupBookmarkParser")
	private BookmarkParser jsoupParser;

	@Autowired
	@Qualifier("safariBookmarkParser")
	private BookmarkParser safariParser;

	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	@SuppressWarnings("null")
	public List<BookMarks> parseBookMarks() {
		List<BookMarks> bookmarkList = new ArrayList<>();
		try {
			// 使用配置文件中的路径，支持classpath和绝对路径
			Resource resource = resourceLoader.getResource(bookmarkConfig.getInputPath());
			log.info("正在解析书签文件: {}", bookmarkConfig.getInputPath());

			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					log.debug("line:{}", line);
					if (H3_TAG_PATTERN.matcher(line).find()) {
						// 处理 <H3> 逻辑（文件夹）
						parseH3(line, bookmarkList);
					} else if (A_TAG_PATTERN.matcher(line).find()) {
						// 处理 <A> 逻辑（书签链接）
						parseA(line, bookmarkList);
					}
				}
			}
		} catch (Exception e) {
			log.error("解析书签文件失败: {}", e.getMessage(), e);
			throw new RuntimeException("解析书签文件失败: " + e.getMessage(), e);
		}
		// 处理解析结果
		log.info("解析完成，共解析到 {} 条书签", bookmarkList.size());
		return bookmarkList;
	}

	@Override
	@Cacheable(value = CacheConfig.CACHE_BOOKMARK_BY_URL, key = "#url", unless = "#result == null")
	public BookMarks selectByUrl(String url) {
		if (StrUtil.isEmpty(url)) {
			log.warn("URL为空，无法查询");
			return null;
		}

		LambdaQueryWrapper<BookMarks> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(BookMarks::getHref, url);
		List<BookMarks> bookMarks = bookMarksService.list(lambdaQueryWrapper);

		if (bookMarks.isEmpty()) {
			log.info("未找到URL对应的书签: {}", url);
			return null;
		}

		// 使用Comparator.comparingLong优化排序
		return bookMarks.stream()
				.filter(b -> b.getAddDate() != null)
				.min(Comparator.comparingLong(BookMarks::getAddDate))
				.orElse(bookMarks.get(0));
	}

	/**
	 * 解析H3标签（文件夹）
	 */
	private void parseH3(String text, List<BookMarks> list) {
		BookMarks entity = new BookMarks();
		entity.setType("h3");

		Matcher matcher = HREF_PATTERN.matcher(text);
		if (matcher.find()) {
			entity.setHref(matcher.group(1));
		}

		matcher = ADD_DATE_PATTERN.matcher(text);
		if (matcher.find()) {
			entity.setAddDate(Long.valueOf(matcher.group(1)));
		}

		matcher = LAST_MODIFIED_PATTERN.matcher(text);
		if (matcher.find()) {
			entity.setLastModified(Long.valueOf(matcher.group(1)));
		}

		matcher = H3_TITLE_PATTERN.matcher(text);
		if (matcher.find()) {
			entity.setTitle(matcher.group(1));
		}

		entity.setId(IdUtil.getSnowflakeNextId());
		log.debug("解析文件夹: {}", entity.getTitle());
		list.add(entity);
	}

	/**
	 * 解析A标签（书签链接）
	 */
	private void parseA(String text, List<BookMarks> list) {
		BookMarks entity = new BookMarks();
		entity.setType("a");

		Matcher matcher = HREF_PATTERN.matcher(text);
		if (matcher.find()) {
			entity.setHref(matcher.group(1));
		}

		matcher = ADD_DATE_PATTERN.matcher(text);
		if (matcher.find()) {
			entity.setAddDate(Long.valueOf(matcher.group(1)));
		}

		matcher = A_TITLE_PATTERN.matcher(text);
		if (matcher.find()) {
			entity.setTitle(matcher.group(1));
		}

		entity.setId(IdUtil.getSnowflakeNextId());
		log.debug("解析书签: {}", entity.getTitle());
		list.add(entity);
	}

	@Override
	public List<BookMarks> parseBookMarks(InputStream inputStream) {
		List<BookMarks> bookmarkList = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (H3_TAG_PATTERN.matcher(line).find()) {
					parseH3(line, bookmarkList);
				} else if (A_TAG_PATTERN.matcher(line).find()) {
					parseA(line, bookmarkList);
				}
			}
		} catch (Exception e) {
			log.error("解析书签流失败: {}", e.getMessage(), e);
			throw new RuntimeException("解析书签失败: " + e.getMessage(), e);
		}
		log.info("从输入流解析完成，共解析到 {} 条书签", bookmarkList.size());
		return bookmarkList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = { CacheConfig.CACHE_BOOKMARK_ANALYSIS, CacheConfig.CACHE_DOMAIN_STATS }, allEntries = true)
	public ParseResult uploadAndParse(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new BusinessException(400, "上传文件不能为空");
		}

		String fileName = file.getOriginalFilename();
		if (fileName == null || !fileName.toLowerCase().endsWith(".html")) {
			throw new BusinessException(400, "只支持HTML格式的书签文件");
		}

		long startTime = System.currentTimeMillis();
		ParseResult result = new ParseResult();
		result.setFileName(fileName);

		try {
			List<BookMarks> bookmarks = parseBookMarks(file.getInputStream());
			result.setTotalParsed(bookmarks.size());
			result.setLinkCount((int) bookmarks.stream().filter(b -> "a".equals(b.getType())).count());
			result.setFolderCount((int) bookmarks.stream().filter(b -> "h3".equals(b.getType())).count());

			// 批量插入数据库
			if (!bookmarks.isEmpty()) {
				int inserted = bookMarksService.batchInsert(bookmarks);
				result.setInsertedCount(inserted);
			}

			result.setParseTimeMs(System.currentTimeMillis() - startTime);
			log.info("文件 {} 解析完成，共 {} 条书签，耗时 {}ms",
					fileName, bookmarks.size(), result.getParseTimeMs());
		} catch (Exception e) {
			log.error("解析上传文件失败: {}", e.getMessage(), e);
			throw new BusinessException(500, "解析文件失败: " + e.getMessage());
		}

		return result;
	}

	@Override
	@Cacheable(value = CacheConfig.CACHE_BOOKMARK_ANALYSIS)
	public BookmarkAnalysis analyzeBookmarks() {
		log.info("执行书签分析（未命中缓存）");
		BookmarkAnalysis analysis = new BookmarkAnalysis();

		// 查询所有书签
		List<BookMarks> allBookmarks = this.list();
		analysis.setTotalCount(allBookmarks.size());

		// 统计链接和文件夹数量
		long linkCount = allBookmarks.stream().filter(b -> "a".equals(b.getType())).count();
		long folderCount = allBookmarks.stream().filter(b -> "h3".equals(b.getType())).count();
		analysis.setLinkCount((int) linkCount);
		analysis.setFolderCount((int) folderCount);

		// 分析重复URL
		List<BookMarks> links = allBookmarks.stream()
				.filter(b -> "a".equals(b.getType()) && StrUtil.isNotEmpty(b.getHref()))
				.collect(Collectors.toList());

		Map<String, Long> urlCountMap = links.stream()
				.collect(Collectors.groupingBy(BookMarks::getHref, Collectors.counting()));

		List<String> duplicateUrls = urlCountMap.entrySet().stream()
				.filter(e -> e.getValue() > 1)
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());

		analysis.setDuplicateUrls(duplicateUrls);
		analysis.setDuplicateCount(duplicateUrls.size());

		// 域名分布分析（Top 20）
		Map<String, Integer> domainMap = new LinkedHashMap<>();
		links.stream()
				.map(b -> extractDomain(b.getHref()))
				.filter(StrUtil::isNotEmpty)
				.collect(Collectors.groupingBy(d -> d, Collectors.counting()))
				.entrySet().stream()
				.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
				.limit(20)
				.forEach(e -> domainMap.put(e.getKey(), e.getValue().intValue()));
		analysis.setDomainDistribution(domainMap);

		// 时间范围分析
		allBookmarks.stream()
				.filter(b -> b.getAddDate() != null && b.getAddDate() > 0)
				.mapToLong(BookMarks::getAddDate)
				.min()
				.ifPresent(analysis::setEarliestAddDate);

		allBookmarks.stream()
				.filter(b -> b.getAddDate() != null && b.getAddDate() > 0)
				.mapToLong(BookMarks::getAddDate)
				.max()
				.ifPresent(analysis::setLatestAddDate);

		return analysis;
	}

	/**
	 * 从URL中提取域名
	 */
	private String extractDomain(String url) {
		try {
			if (StrUtil.isEmpty(url)) {
				return "";
			}
			URI uri = new URI(url);
			String host = uri.getHost();
			return host != null ? host : "";
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public String exportToHtml() {
		List<BookMarks> bookmarks = this.list();
		StringBuilder sb = new StringBuilder();

		// Netscape Bookmark File Format 标准头部
		sb.append("<!DOCTYPE NETSCAPE-Bookmark-file-1>\n");
		sb.append("<!-- This is an automatically generated file.\n");
		sb.append("     It will be read and overwritten.\n");
		sb.append("     DO NOT EDIT! -->\n");
		sb.append("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n");
		sb.append("<TITLE>Bookmarks</TITLE>\n");
		sb.append("<H1>Bookmarks</H1>\n");
		sb.append("<DL><p>\n");

		// 按添加时间排序
		bookmarks.stream()
				.sorted(Comparator.comparing(BookMarks::getAddDate, Comparator.nullsLast(Comparator.naturalOrder())))
				.forEach(b -> {
					if ("a".equals(b.getType())) {
						sb.append("    <DT><A HREF=\"").append(b.getHref() != null ? b.getHref() : "")
								.append("\" ADD_DATE=\"").append(b.getAddDate() != null ? b.getAddDate() : "")
								.append("\">").append(b.getTitle() != null ? escapeHtml(b.getTitle()) : "")
								.append("</A>\n");
					} else if ("h3".equals(b.getType())) {
						sb.append("    <DT><H3 ADD_DATE=\"").append(b.getAddDate() != null ? b.getAddDate() : "")
								.append("\" LAST_MODIFIED=\"")
								.append(b.getLastModified() != null ? b.getLastModified() : "")
								.append("\">").append(b.getTitle() != null ? escapeHtml(b.getTitle()) : "")
								.append("</H3>\n");
					}
				});

		sb.append("</DL><p>\n");
		return sb.toString();
	}

	@Override
	public String exportToMarkdown() {
		List<BookMarks> bookmarks = this.list();
		StringBuilder sb = new StringBuilder();

		sb.append("# 书签导出\n\n");
		sb.append("> 导出时间: ").append(new Date()).append("\n\n");

		// 先输出文件夹
		List<BookMarks> folders = bookmarks.stream()
				.filter(b -> "h3".equals(b.getType()))
				.sorted(Comparator.comparing(BookMarks::getAddDate, Comparator.nullsLast(Comparator.naturalOrder())))
				.collect(Collectors.toList());

		if (!folders.isEmpty()) {
			sb.append("## 文件夹\n\n");
			folders.forEach(f -> sb.append("- **").append(f.getTitle() != null ? f.getTitle() : "未命名")
					.append("**\n"));
			sb.append("\n");
		}

		// 输出链接
		List<BookMarks> links = bookmarks.stream()
				.filter(b -> "a".equals(b.getType()))
				.sorted(Comparator.comparing(BookMarks::getAddDate, Comparator.nullsLast(Comparator.naturalOrder())))
				.collect(Collectors.toList());

		if (!links.isEmpty()) {
			sb.append("## 书签链接\n\n");
			links.forEach(l -> {
				String title = l.getTitle() != null ? l.getTitle() : "未命名";
				String href = l.getHref() != null ? l.getHref() : "";
				sb.append("- [").append(title).append("](").append(href).append(")\n");
			});
		}

		return sb.toString();
	}

	@Override
	public String exportToJson() {
		List<BookMarks> bookmarks = this.list();

		// 构建导出结构
		Map<String, Object> exportData = new LinkedHashMap<>();
		exportData.put("exportTime", System.currentTimeMillis());
		exportData.put("totalCount", bookmarks.size());
		exportData.put("bookmarks", bookmarks);

		return JSONUtil.toJsonPrettyStr(exportData);
	}

	/**
	 * HTML转义
	 */
	private String escapeHtml(String text) {
		if (text == null) {
			return "";
		}
		return text.replace("&", "&amp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;");
	}

	@Override
	public List<BookMarks> parseWithJsoup(InputStream inputStream) {
		log.info("使用Jsoup解析器解析书签文件");
		try {
			List<BookMarks> bookmarks = jsoupParser.parse(inputStream);
			log.info("Jsoup解析完成，共解析到 {} 条书签", bookmarks.size());
			return bookmarks;
		} catch (Exception e) {
			log.error("Jsoup解析书签失败: {}", e.getMessage(), e);
			throw new BusinessException(500, "解析书签失败: " + e.getMessage());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = { CacheConfig.CACHE_BOOKMARK_ANALYSIS, CacheConfig.CACHE_DOMAIN_STATS }, allEntries = true)
	public ParseResult uploadAndParseWithJsoup(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new BusinessException(400, "上传文件不能为空");
		}

		String fileName = file.getOriginalFilename();
		if (fileName == null || !fileName.toLowerCase().endsWith(".html")) {
			throw new BusinessException(400, "只支持HTML格式的书签文件");
		}

		long startTime = System.currentTimeMillis();
		ParseResult result = new ParseResult();
		result.setFileName(fileName);

		try {
			// 使用Jsoup解析器
			List<BookMarks> bookmarks = parseWithJsoup(file.getInputStream());
			result.setTotalParsed(bookmarks.size());
			result.setLinkCount((int) bookmarks.stream().filter(b -> "a".equals(b.getType())).count());
			result.setFolderCount((int) bookmarks.stream().filter(b -> "h3".equals(b.getType())).count());

			// 批量插入数据库
			if (!bookmarks.isEmpty()) {
				int inserted = bookMarksService.batchInsert(bookmarks);
				result.setInsertedCount(inserted);
			}

			result.setParseTimeMs(System.currentTimeMillis() - startTime);
			log.info("文件 {} 使用Jsoup解析完成，共 {} 条书签，耗时 {}ms",
					fileName, bookmarks.size(), result.getParseTimeMs());
		} catch (Exception e) {
			log.error("Jsoup解析上传文件失败: {}", e.getMessage(), e);
			throw new BusinessException(500, "解析文件失败: " + e.getMessage());
		}

		return result;
	}

	@Override
	public List<BookMarks> parseWithSafari(InputStream inputStream) {
		log.info("使用Safari解析器解析plist书签文件");
		try {
			List<BookMarks> bookmarks = safariParser.parse(inputStream);
			log.info("Safari解析完成，共解析到 {} 条书签", bookmarks.size());
			return bookmarks;
		} catch (Exception e) {
			log.error("Safari解析书签失败: {}", e.getMessage(), e);
			throw new BusinessException(500, "解析Safari书签失败: " + e.getMessage());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = { CacheConfig.CACHE_BOOKMARK_ANALYSIS, CacheConfig.CACHE_DOMAIN_STATS }, allEntries = true)
	public ParseResult uploadAndParseWithSafari(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new BusinessException(400, "上传文件不能为空");
		}

		String fileName = file.getOriginalFilename();
		if (fileName == null || !fileName.toLowerCase().endsWith(".plist")) {
			throw new BusinessException(400, "只支持plist格式的Safari书签文件");
		}

		long startTime = System.currentTimeMillis();
		ParseResult result = new ParseResult();
		result.setFileName(fileName);

		try {
			List<BookMarks> bookmarks = parseWithSafari(file.getInputStream());
			result.setTotalParsed(bookmarks.size());
			result.setLinkCount((int) bookmarks.stream().filter(b -> "a".equals(b.getType())).count());
			result.setFolderCount((int) bookmarks.stream().filter(b -> "h3".equals(b.getType())).count());

			if (!bookmarks.isEmpty()) {
				int inserted = bookMarksService.batchInsert(bookmarks);
				result.setInsertedCount(inserted);
			}

			result.setParseTimeMs(System.currentTimeMillis() - startTime);
			log.info("Safari书签文件 {} 解析完成，共 {} 条书签，耗时 {}ms",
					fileName, bookmarks.size(), result.getParseTimeMs());
		} catch (Exception e) {
			log.error("Safari解析上传文件失败: {}", e.getMessage(), e);
			throw new BusinessException(500, "解析Safari书签失败: " + e.getMessage());
		}

		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = { CacheConfig.CACHE_BOOKMARK_ANALYSIS, CacheConfig.CACHE_DOMAIN_STATS }, allEntries = true)
	public ParseResult uploadAndParseAuto(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new BusinessException(400, "上传文件不能为空");
		}

		String fileName = file.getOriginalFilename();
		if (fileName == null) {
			throw new BusinessException(400, "无法获取文件名");
		}

		String lowerFileName = fileName.toLowerCase();

		// 根据文件扩展名自动选择解析器
		if (lowerFileName.endsWith(".plist")) {
			log.info("检测到plist文件，使用Safari解析器");
			return uploadAndParseWithSafari(file);
		} else if (lowerFileName.endsWith(".html") || lowerFileName.endsWith(".htm")) {
			log.info("检测到HTML文件，使用Jsoup解析器");
			return uploadAndParseWithJsoup(file);
		} else {
			// 尝试通过文件内容判断
			try {
				byte[] bytes = file.getBytes();
				String contentStart = new String(bytes, 0, Math.min(100, bytes.length), StandardCharsets.UTF_8);

				if (contentStart.contains("<!DOCTYPE plist") || contentStart.contains("<plist")) {
					log.info("通过内容检测到plist文件，使用Safari解析器");
					return uploadAndParseWithSafari(file);
				} else if (contentStart.contains("<!DOCTYPE") || contentStart.contains("<html") ||
						contentStart.contains("<HTML") || contentStart.contains("NETSCAPE-Bookmark")) {
					log.info("通过内容检测到HTML文件，使用Jsoup解析器");
					return uploadAndParseWithJsoup(file);
				}
			} catch (Exception e) {
				log.warn("无法读取文件内容进行格式检测: {}", e.getMessage());
			}

			throw new BusinessException(400, "不支持的文件格式，请上传HTML或plist格式的书签文件");
		}
	}
}
