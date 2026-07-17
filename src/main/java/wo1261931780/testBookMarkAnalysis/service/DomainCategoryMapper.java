package wo1261931780.testBookMarkAnalysis.service;

import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 域名→分类映射引擎
 * <p>
 * 维护域名白名单，提供三种分类策略：
 * <ul>
 *   <li>按域名：将域名直接映射为文件夹名</li>
 *   <li>按功能：将域名映射到功能类别</li>
 *   <li>按国内外：根据 TLD 和标题语言判断</li>
 * </ul>
 */
public class DomainCategoryMapper {

    /** 检测中文的正则 */
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]");

    /** 已知的国内域名（使用 .com 等国际 TLD 但实际是国内网站） */
    private static final Set<String> KNOWN_DOMESTIC = Set.of(
            "zhihu.com", "bilibili.com", "juejin.cn", "douyin.com",
            "taobao.com", "jd.com", "tmall.com", "163.com",
            "weibo.com", "douban.com", "oschina.net", "csdn.net",
            "cnblogs.com", "infoq.cn", "ithome.com", "ifanr.com",
            "sspai.com", "geekbang.org", "time.geekbang.org",
            "xiaohongshu.com", "kuaishou.com", "huya.com", "douyu.com",
            "meituan.com", "dianping.com", "ctrip.com", "qunar.com",
            "lagou.com", "zhipin.com", "nowcoder.com", "leetcode.cn"
    );

    /**
     * 域名→功能分类映射（覆盖 top 100 高频域名）
     */
    private static final Map<String, String> DOMAIN_CATEGORY = new LinkedHashMap<>();

    static {
        // ===== 开发工具 =====
        put("github.com", "开发工具");
        put("gitee.com", "开发工具");
        put("gitlab.com", "开发工具");
        put("stackoverflow.com", "开发工具");
        put("npmjs.com", "开发工具");
        put("pypi.org", "开发工具");
        put("maven.org", "开发工具");
        put("docker.com", "开发工具");
        put("hub.docker.com", "开发工具");
        put("dev.to", "开发工具");
        put("codepen.io", "开发工具");
        put("codesandbox.io", "开发工具");
        put("replit.com", "开发工具");
        put("vercel.com", "开发工具");
        put("netlify.com", "开发工具");
        put("v2ex.com", "开发工具");

        // ===== 技术文档 =====
        put("developer.mozilla.org", "技术文档");
        put("mdn.dev", "技术文档");
        put("w3.org", "技术文档");
        put("w3schools.com", "技术文档");
        put("runoob.com", "技术文档");
        put("vuejs.org", "技术文档");
        put("react.dev", "技术文档");
        put("nodejs.org", "技术文档");
        put("python.org", "技术文档");
        put("golang.org", "技术文档");
        put("rust-lang.org", "技术文档");
        put("kubernetes.io", "技术文档");
        put("docs.spring.io", "技术文档");
        put("mybatis.org", "技术文档");
        put("tailwindcss.com", "技术文档");
        put("typescriptlang.org", "技术文档");

        // ===== 知识社区 =====
        put("zhihu.com", "知识社区");
        put("juejin.cn", "知识社区");
        put("csdn.net", "知识社区");
        put("segmentfault.com", "知识社区");
        put("cnblogs.com", "知识社区");
        put("oschina.net", "知识社区");
        put("reddit.com", "知识社区");
        put("quora.com", "知识社区");
        put("news.ycombinator.com", "知识社区");
        put("hackernews.ycombinator.com", "知识社区");
        put("producthunt.com", "知识社区");
        put("douban.com", "知识社区");

        // ===== 视频娱乐 =====
        put("bilibili.com", "视频娱乐");
        put("youtube.com", "视频娱乐");
        put("douyin.com", "视频娱乐");
        put("kuaishou.com", "视频娱乐");
        put("huya.com", "视频娱乐");
        put("douyu.com", "视频娱乐");
        put("twitch.tv", "视频娱乐");
        put("vimeo.com", "视频娱乐");
        put("iqiyi.com", "视频娱乐");
        put("youku.com", "视频娱乐");

        // ===== 新闻资讯 =====
        put("36kr.com", "新闻资讯");
        put("ithome.com", "新闻资讯");
        put("ifanr.com", "新闻资讯");
        put("sspai.com", "新闻资讯");
        put("infoq.cn", "新闻资讯");
        put("geekbang.org", "新闻资讯");
        put("time.geekbang.org", "新闻资讯");
        put("techcrunch.com", "新闻资讯");
        put("theverge.com", "新闻资讯");
        put("arstechnica.com", "新闻资讯");

        // ===== 在线购物 =====
        put("taobao.com", "在线购物");
        put("jd.com", "在线购物");
        put("tmall.com", "在线购物");
        put("amazon.com", "在线购物");
        put("ebay.com", "在线购物");
        put("aliexpress.com", "在线购物");
        put("pinduoduo.com", "在线购物");
        put("smzdm.com", "在线购物");

        // ===== 设计资源 =====
        put("dribbble.com", "设计资源");
        put("behance.net", "设计资源");
        put("figma.com", "设计资源");
        put("sketch.com", "设计资源");
        put("canva.com", "设计资源");
        put("unsplash.com", "设计资源");
        put("pexels.com", "设计资源");
        put("iconfont.cn", "设计资源");
        put("zcool.com.cn", "设计资源");
        put("huaban.com", "设计资源");

        // ===== AI 与数据 =====
        put("openai.com", "AI 与数据");
        put("chatgpt.com", "AI 与数据");
        put("claude.ai", "AI 与数据");
        put("huggingface.co", "AI 与数据");
        put("kaggle.com", "AI 与数据");
        put("paperswithcode.com", "AI 与数据");
        put("arxiv.org", "AI 与数据");
        put("tensorflow.org", "AI 与数据");
        put("pytorch.org", "AI 与数据");
        put("modelscope.cn", "AI 与数据");

        // ===== 云服务 =====
        put("aws.amazon.com", "云服务");
        put("cloud.google.com", "云服务");
        put("azure.microsoft.com", "云服务");
        put("aliyun.com", "云服务");
        put("cloud.tencent.com", "云服务");
        put("digitalocean.com", "云服务");
        put("cloudflare.com", "云服务");
        put("linode.com", "云服务");

        // ===== 学术教育 =====
        put("scholar.google.com", "学术教育");
        put("coursera.org", "学术教育");
        put("udemy.com", "学术教育");
        put("edx.org", "学术教育");
        put("wikipedia.org", "学术教育");
        put("medium.com", "学术教育");
        put("acm.org", "学术教育");
        put("ieee.org", "学术教育");

        // ===== 社交媒体 =====
        put("twitter.com", "社交媒体");
        put("x.com", "社交媒体");
        put("weibo.com", "社交媒体");
        put("xiaohongshu.com", "社交媒体");
        put("t.me", "社交媒体");
        put("discord.com", "社交媒体");
        put("slack.com", "社交媒体");

        // ===== 生活服务 =====
        put("meituan.com", "生活服务");
        put("dianping.com", "生活服务");
        put("ctrip.com", "生活服务");
        put("qunar.com", "生活服务");
        put("12306.cn", "生活服务");
        put("didiglobal.com", "生活服务");

        // ===== 求职招聘 =====
        put("linkedin.com", "求职招聘");
        put("zhipin.com", "求职招聘");
        put("lagou.com", "求职招聘");
        put("nowcoder.com", "求职招聘");
        put("indeed.com", "求职招聘");
        put("leetcode.cn", "求职招聘");
        put("leetcode.com", "求职招聘");
    }

    private static void put(String domain, String category) {
        DOMAIN_CATEGORY.put(domain, category);
    }

    /**
     * 从 URL 提取域名（去除 www 前缀）
     */
    public static String extractDomain(String url) {
        if (url == null || url.isBlank()) return null;
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
            URI uri = new URI(url);
            String host = uri.getHost();
            if (host == null) return null;
            host = host.toLowerCase();
            if (host.startsWith("www.")) {
                host = host.substring(4);
            }
            return host;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检测文本是否包含中文
     */
    public static boolean containsChinese(String text) {
        if (text == null || text.isEmpty()) return false;
        return CHINESE_PATTERN.matcher(text).find();
    }

    /**
     * 策略1：按域名分类
     * <p>
     * 白名单域名直接返回文件夹名，未知域名返回 null（需要 AI 处理）
     *
     * @param url 书签 URL
     * @return 文件夹名，或 null 表示未知
     */
    public static String classifyByDomain(String url) {
        String domain = extractDomain(url);
        if (domain == null) return null;
        return DOMAIN_CATEGORY.get(domain);
    }

    /**
     * 策略2：按功能分类
     * <p>
     * 与按域名分类相同，因为白名单已按功能类别组织。
     * 区别在于：按功能分类时，同一功能类别下的不同域名归入同一文件夹。
     *
     * @param url 书签 URL
     * @return 功能类别名，或 null 表示未知
     */
    public static String classifyByFunction(String url) {
        return classifyByDomain(url); // 当前白名单已是功能映射
    }

    /**
     * 策略3：按国内外分类
     * <p>
     * 判断规则（按优先级）：
     * <ol>
     *   <li>TLD 为 .cn / .com.cn → 国内</li>
     *   <li>标题含中文 → 国内</li>
     *   <li>已知国内域名（使用 .com 等国际 TLD）→ 国内</li>
     *   <li>其他 → 国外</li>
     * </ol>
     *
     * @param url   书签 URL
     * @param title 书签标题
     * @return "国内" 或 "国外"
     */
    public static String classifyByRegion(String url, String title) {
        if (url == null) return "国外";

        String lowerUrl = url.toLowerCase();

        // 规则1：国内 TLD
        if (lowerUrl.contains(".cn") || lowerUrl.contains(".中国")) {
            return "国内";
        }

        // 规则2：标题含中文
        if (containsChinese(title)) {
            return "国内";
        }

        // 规则3：已知国内域名
        String domain = extractDomain(url);
        if (domain != null && KNOWN_DOMESTIC.contains(domain)) {
            return "国内";
        }

        return "国外";
    }

    /**
     * 判断标题是否需要 AI 补全
     *
     * @param title 原标题
     * @return true 表示标题为空、过短或无意义，需要 AI 生成
     */
    public static boolean needsTitleGeneration(String title) {
        if (title == null || title.isBlank()) return true;
        String trimmed = title.trim();
        // 过短
        if (trimmed.length() <= 3) return true;
        // 无意义标题
        Set<String> meaningless = Set.of(
                "untitled", "无标题", "首页", "home", "index",
                "new tab", "new page", "空白页", "about:blank",
                "localhost", "127.0.0.1", "未命名", "unnamed"
        );
        return meaningless.contains(trimmed.toLowerCase());
    }

    /**
     * 获取所有已知的域名→分类映射（用于前端展示或调试）
     */
    public static Map<String, String> getAllMappings() {
        return Collections.unmodifiableMap(DOMAIN_CATEGORY);
    }

    /**
     * 获取所有分类名及其包含的域名数
     */
    public static Map<String, Integer> getCategoryStats() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        for (String category : DOMAIN_CATEGORY.values()) {
            stats.merge(category, 1, Integer::sum);
        }
        return stats;
    }
}