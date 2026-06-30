package wo1261931780.testBookMarkAnalysis.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Intellij IDEA. Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.config
 *
 * @author liujiajun_junw @Date 2026-01-04 @Description OpenAPI/Swagger配置类
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("书签解析系统 API")
                                .version("1.0.0")
                                .description(
                                        "浏览器书签解析、存储、分析、导出服务。\n\n"
                                                + "## 功能特性\n"
                                                + "- 📁 解析Chrome/Firefox/Edge导出的书签文件\n"
                                                + "- 📤 支持文件上传解析\n"
                                                + "- 📊 书签统计分析（域名分布、重复检测）\n"
                                                + "- 📥 多格式导出（HTML/Markdown/JSON）\n"
                                                + "- 🔍 书签去重处理")
                                .contact(
                                        new Contact()
                                                .name("liujiajun_junw")
                                                .url("https://github.com/wo1261931780"))
                                .license(
                                        new License()
                                                .name("AGPL-3.0")
                                                .url("https://www.gnu.org/licenses/agpl-3.0.txt")))
                .servers(List.of(new Server().url("http://localhost:8000").description("本地开发环境")));
    }
}
