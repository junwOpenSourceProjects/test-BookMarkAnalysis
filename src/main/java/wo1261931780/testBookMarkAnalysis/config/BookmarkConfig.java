package wo1261931780.testBookMarkAnalysis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 书签解析配置类
 *
 * @author liujiajun_junw
 */
@Data
@Component
@ConfigurationProperties(prefix = "bookmark")
public class BookmarkConfig {

    /**
     * 输入文件路径（支持classpath:前缀或绝对路径）
     */
    private String inputPath = "classpath:wo1261931780/testBookMarkAnalysis/bookmarks/bookmarks.html";

    /**
     * 输出文件路径
     */
    private String outputPath = "${user.home}/bookmark-output/result.txt";
}
