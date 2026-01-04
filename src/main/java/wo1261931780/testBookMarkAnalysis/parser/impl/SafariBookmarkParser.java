package wo1261931780.testBookMarkAnalysis.parser.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.dd.plist.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import wo1261931780.testBookMarkAnalysis.entity.BookMarks;
import wo1261931780.testBookMarkAnalysis.parser.BookmarkParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.parser.impl
 *
 * @author liujiajun_junw
 * @Date 2026-01-04
 * @Description Safari书签解析器（plist格式）
 *
 * Safari书签文件位置：~/Library/Safari/Bookmarks.plist
 * 文件结构：
 * - Root (dict)
 *   - Children (array) - 包含书签栏、阅读列表等
 *     - WebBookmarkType: "WebBookmarkTypeList" (文件夹) 或 "WebBookmarkTypeLeaf" (书签)
 *     - URLString: 书签URL
 *     - URIDictionary.title: 书签标题
 *     - Children: 子书签列表
 */
@Slf4j
@Component("safariBookmarkParser")
public class SafariBookmarkParser implements BookmarkParser {

    /**
     * Safari书签类型常量
     */
    private static final String TYPE_LIST = "WebBookmarkTypeList";
    private static final String TYPE_LEAF = "WebBookmarkTypeLeaf";
    private static final String TYPE_PROXY = "WebBookmarkTypeProxy";

    @Override
    public List<BookMarks> parse(InputStream inputStream) {
        List<BookMarks> bookmarks = new ArrayList<>();

        try {
            // 解析plist文件
            NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(inputStream);

            if (rootDict == null) {
                log.warn("Safari书签文件为空或格式无效");
                return bookmarks;
            }

            // 获取Children数组（包含书签栏、阅读列表等顶级项）
            NSArray children = (NSArray) rootDict.get("Children");
            if (children != null) {
                parseChildren(children, bookmarks, null);
            }

            log.info("[Safari] 解析完成，共解析到 {} 条书签", bookmarks.size());

        } catch (Exception e) {
            log.error("[Safari] 解析plist文件失败: {}", e.getMessage(), e);
            throw new RuntimeException("解析Safari书签文件失败: " + e.getMessage(), e);
        }

        return bookmarks;
    }

    @Override
    public List<BookMarks> parse(String content) {
        // plist通常是二进制或XML格式，不支持直接字符串解析
        throw new UnsupportedOperationException("Safari书签解析不支持字符串输入，请使用InputStream");
    }

    @Override
    public String getParserName() {
        return "Safari Plist Parser";
    }

    /**
     * 递归解析Children数组
     *
     * @param children   子项数组
     * @param bookmarks  结果列表
     * @param parentName 父文件夹名称
     */
    private void parseChildren(NSArray children, List<BookMarks> bookmarks, String parentName) {
        if (children == null) {
            return;
        }

        for (NSObject child : children.getArray()) {
            if (!(child instanceof NSDictionary)) {
                continue;
            }

            NSDictionary item = (NSDictionary) child;
            String webBookmarkType = getStringValue(item, "WebBookmarkType");

            if (TYPE_LEAF.equals(webBookmarkType)) {
                // 书签链接
                BookMarks bookmark = parseLeaf(item);
                if (bookmark != null) {
                    bookmarks.add(bookmark);
                }
            } else if (TYPE_LIST.equals(webBookmarkType)) {
                // 文件夹
                BookMarks folder = parseFolder(item);
                if (folder != null) {
                    bookmarks.add(folder);
                }

                // 递归解析子项
                NSArray subChildren = (NSArray) item.get("Children");
                if (subChildren != null) {
                    String folderName = folder != null ? folder.getTitle() : parentName;
                    parseChildren(subChildren, bookmarks, folderName);
                }
            } else if (TYPE_PROXY.equals(webBookmarkType)) {
                // 代理类型（如阅读列表），递归解析其子项
                NSArray subChildren = (NSArray) item.get("Children");
                if (subChildren != null) {
                    String title = getTitle(item);
                    parseChildren(subChildren, bookmarks, title);
                }
            }
        }
    }

    /**
     * 解析书签链接
     */
    private BookMarks parseLeaf(NSDictionary item) {
        String url = getStringValue(item, "URLString");
        if (StrUtil.isEmpty(url)) {
            return null;
        }

        BookMarks bookmark = new BookMarks();
        bookmark.setId(IdUtil.getSnowflakeNextId());
        bookmark.setType("a");
        bookmark.setHref(url);
        bookmark.setTitle(getTitle(item));

        // Safari plist中没有直接的时间戳，尝试获取
        // 有些版本可能有 DateAdded 或 DateModified
        NSObject dateAdded = item.get("DateAdded");
        if (dateAdded instanceof NSDate) {
            bookmark.setAddDate(((NSDate) dateAdded).getDate().getTime() / 1000);
        } else {
            // 使用当前时间
            bookmark.setAddDate(System.currentTimeMillis() / 1000);
        }

        log.debug("[Safari] 解析书签: {} -> {}", bookmark.getTitle(), url);
        return bookmark;
    }

    /**
     * 解析文件夹
     */
    private BookMarks parseFolder(NSDictionary item) {
        String title = getTitle(item);

        // 跳过系统文件夹（如阅读列表的内部结构）
        String uuid = getStringValue(item, "WebBookmarkUUID");
        if (StrUtil.isEmpty(title) && StrUtil.isEmpty(uuid)) {
            return null;
        }

        BookMarks folder = new BookMarks();
        folder.setId(IdUtil.getSnowflakeNextId());
        folder.setType("h3");
        folder.setTitle(StrUtil.isNotEmpty(title) ? title : "未命名文件夹");

        // 获取时间戳
        NSObject dateAdded = item.get("DateAdded");
        if (dateAdded instanceof NSDate) {
            folder.setAddDate(((NSDate) dateAdded).getDate().getTime() / 1000);
        } else {
            folder.setAddDate(System.currentTimeMillis() / 1000);
        }

        log.debug("[Safari] 解析文件夹: {}", folder.getTitle());
        return folder;
    }

    /**
     * 获取书签标题
     * Safari书签的标题可能在URIDictionary.title或直接的Title字段
     */
    private String getTitle(NSDictionary item) {
        // 首先尝试直接的Title字段
        String title = getStringValue(item, "Title");
        if (StrUtil.isNotEmpty(title)) {
            return title;
        }

        // 尝试URIDictionary中的title
        NSDictionary uriDict = (NSDictionary) item.get("URIDictionary");
        if (uriDict != null) {
            title = getStringValue(uriDict, "title");
            if (StrUtil.isNotEmpty(title)) {
                return title;
            }
        }

        // 使用URL作为标题
        String url = getStringValue(item, "URLString");
        if (StrUtil.isNotEmpty(url)) {
            return url;
        }

        return "未命名";
    }

    /**
     * 安全获取字符串值
     */
    private String getStringValue(NSDictionary dict, String key) {
        if (dict == null) {
            return null;
        }
        NSObject value = dict.get(key);
        if (value instanceof NSString) {
            return ((NSString) value).getContent();
        }
        return null;
    }
}
