package wo1261931780.testBookMarkAnalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;
import wo1261931780.testBookMarkAnalysis.entity.BookmarkAnalysis;
import wo1261931780.testBookMarkAnalysis.entity.ParseResult;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.service
 *
 * @author liujiajun_junw
 * @Date 2023-11-15-38  星期二
 * @Description 书签解析服务接口
 */
public interface BookmarksParserService extends IService<BookMarks> {

	/**
	 * 解析配置文件中指定的书签文件（使用正则表达式）
	 * @return 解析后的书签列表
	 */
	List<BookMarks> parseBookMarks();

	/**
	 * 解析上传的书签文件（使用正则表达式）
	 * @param inputStream 文件输入流
	 * @return 解析后的书签列表
	 */
	List<BookMarks> parseBookMarks(InputStream inputStream);

	/**
	 * 使用Jsoup解析书签文件（更健壮，推荐）
	 * @param inputStream 文件输入流
	 * @return 解析后的书签列表
	 */
	List<BookMarks> parseWithJsoup(InputStream inputStream);

	/**
	 * 上传并解析书签文件，同时保存到数据库
	 * @param file 上传的文件
	 * @return 解析结果
	 */
	ParseResult uploadAndParse(MultipartFile file);

	/**
	 * 上传并使用Jsoup解析书签文件（更健壮），同时保存到数据库
	 * @param file 上传的文件
	 * @return 解析结果
	 */
	ParseResult uploadAndParseWithJsoup(MultipartFile file);

	/**
	 * 解析Safari书签文件（plist格式）
	 * @param inputStream 文件输入流
	 * @return 解析后的书签列表
	 */
	List<BookMarks> parseWithSafari(InputStream inputStream);

	/**
	 * 上传并解析Safari书签文件（plist格式），同时保存到数据库
	 * @param file 上传的文件（.plist格式）
	 * @return 解析结果
	 */
	ParseResult uploadAndParseWithSafari(MultipartFile file);

	/**
	 * 智能解析书签文件，自动识别格式（HTML/plist）
	 * @param file 上传的文件
	 * @return 解析结果
	 */
	ParseResult uploadAndParseAuto(MultipartFile file);

	/**
	 * 根据URL查询书签（返回最早添加的）
	 * @param url 书签URL
	 * @return 书签实体
	 */
	BookMarks selectByUrl(String url);

	/**
	 * 分析数据库中的书签
	 * @return 分析结果
	 */
	BookmarkAnalysis analyzeBookmarks();

	/**
	 * 导出书签为HTML格式（Netscape标准格式，可导入浏览器）
	 * @return HTML内容
	 */
	String exportToHtml();

	/**
	 * 导出书签为Markdown格式
	 * @return Markdown内容
	 */
	String exportToMarkdown();

	/**
	 * 导出书签为JSON格式
	 * @return JSON内容
	 */
	String exportToJson();
}
