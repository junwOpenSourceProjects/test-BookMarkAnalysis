package wo1261931780.testBookMarkAnalysis.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by Intellij IDEA.
 * Project:test-BookMarkAnalysis
 * Package:wo1261931780.testBookMarkAnalysis.config
 *
 * @author liujiajun_junw
 * @Date 2026-01-04
 * @Description 缓存配置类
 *              <p>
 *              使用Caffeine作为本地缓存实现，具有以下特点：
 *              1. 高性能 - 基于W-TinyLFU算法，缓存命中率高
 *              2. 轻量级 - 无需额外部署Redis等中间件
 *              3. 功能丰富 - 支持过期、最大容量、统计等
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 缓存名称常量
     */
    public static final String CACHE_BOOKMARK_ANALYSIS = "bookmarkAnalysis";
    public static final String CACHE_BOOKMARK_BY_URL = "bookmarkByUrl";
    public static final String CACHE_DOMAIN_STATS = "domainStats";

    @Bean
    @SuppressWarnings("null")
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeineCacheBuilder());
        // 注册缓存名称
        cacheManager.setCacheNames(java.util.List.of(
                CACHE_BOOKMARK_ANALYSIS,
                CACHE_BOOKMARK_BY_URL,
                CACHE_DOMAIN_STATS));
        return cacheManager;
    }

    /**
     * Caffeine缓存配置
     * - 最大容量: 500条
     * - 写入后过期时间: 10分钟
     * - 访问后过期时间: 5分钟
     */
    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .recordStats(); // 开启统计
    }
}
