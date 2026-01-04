# 书签解析项目 - 开发优化计划

> 文档创建时间：2026年1月4日
> 项目名称：test-BookMarkAnalysis
> 当前状态：**第四阶段优化完成** ✅

---

## 一、项目概述

### 1.1 项目功能
这是一个浏览器书签解析项目，主要功能包括：
- 解析浏览器导出的HTML书签文件（Chrome/Firefox格式）
- 提取书签链接（`<A>`标签）和文件夹（`<H3>`标签）
- 将解析结果存储到MySQL数据库
- 提供书签去重、导出等功能
- 通过REST API提供服务

### 1.2 技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.4.1 | Web框架 |
| MyBatis-Plus | 3.5.9 | ORM框架 |
| MySQL | 8.x | 数据库 |
| Hutool | 5.8.34 | 工具库 |
| springdoc-openapi | 2.7.0 | API文档 |
| Lombok | 1.18.36 | 代码简化 |

---

## 二、依赖升级记录 ✅

### 2.1 升级详情

| 依赖 | 旧版本 | 新版本 | 备注 |
|------|--------|--------|------|
| Spring Boot Parent | 3.1.6 | **3.4.1** | 统一版本管理 |
| spring-boot-starter | 3.2.0（独立指定） | 由parent管理 | 移除独立版本号 |
| spring-boot-starter-web | 3.2.0（独立指定） | 由parent管理 | 移除独立版本号 |
| spring-boot-starter-jdbc | 3.2.0（独立指定） | 由parent管理 | 移除独立版本号 |
| MyBatis-Plus | mybatis-plus-boot-starter 3.5.4.1 | **mybatis-plus-spring-boot3-starter 3.5.9** | Spring Boot 3专用版 |
| MyBatis-Plus Generator | 3.5.4 | **3.5.9** | 代码生成器 |
| Dynamic Datasource | dynamic-datasource-spring-boot-starter 4.2.0 | **dynamic-datasource-spring-boot3-starter 4.3.1** | 多数据源 |
| Lombok | 1.18.30 | **1.18.36** | 最新稳定版 |
| Hutool | 5.8.23 | **5.8.34** | 最新稳定版 |
| MySQL Connector | mysql:mysql-connector-java 8.0.33 | **com.mysql:mysql-connector-j** | 新GAV坐标 |
| Swagger | springfox-swagger2 3.0.0 | **springdoc-openapi-starter-webmvc-ui 2.7.0** | Spring Boot 3兼容 |

### 2.2 解决的冲突

1. **版本不一致问题**
   - 问题：parent是3.1.6，但starter是3.2.0
   - 解决：统一升级到3.4.1，由parent管理版本

2. **Swagger不兼容Spring Boot 3**
   - 问题：springfox不支持Spring Boot 3
   - 解决：迁移到springdoc-openapi，更新所有注解

3. **MyBatis-Plus分页插件找不到类**
   - 问题：`PaginationInnerInterceptor`类找不到
   - 解决：添加`mybatis-plus-jsqlparser`依赖，指定`DbType.MYSQL`

4. **insertOrUpdate方法冲突**
   - 问题：自定义方法与BaseMapper冲突（返回类型不同）
   - 解决：重命名为`customInsertOrUpdate`

5. **MySQL驱动坐标过时**
   - 问题：`mysql:mysql-connector-java`已废弃
   - 解决：更新为`com.mysql:mysql-connector-j`

---

## 三、现存问题分析

### 3.1 高优先级问题 🔴

| 序号 | 问题 | 位置 | 影响 | 状态 |
|------|------|------|------|------|
| 1 | 硬编码Windows文件路径 | `BookmarksParserServiceImpl.java` | 跨平台兼容性 | ✅ 已解决 |
| 2 | 硬编码输出文件路径 | `ShowMeListController.java` | 跨平台兼容性 | ✅ 已解决 |
| 3 | 包结构混乱 | `BookMarks2Service`在controller包 | 代码可维护性 | ✅ 已解决 |
| 4 | 重复实体类 | `BookMarks`和`BookMarks2`几乎相同 | 代码冗余 | ✅ 已解决 |
| 5 | 异常处理简陋 | 仅使用`e.printStackTrace()` | 生产环境不可用 | ✅ 已解决 |

### 3.2 中优先级问题 🟡

| 序号 | 问题 | 位置 | 影响 | 状态 |
|------|------|------|------|------|
| 1 | 正则表达式每次重新编译 | `BookmarksParserServiceImpl.java` | 性能损耗 | ✅ 已解决 |
| 2 | 排序实现冗余 | `selectByUrl`方法中的Comparator | 代码简洁性 | ✅ 已解决 |
| 3 | 无参数校验 | Controller层 | 数据安全性 | ✅ 已解决 |
| 4 | 无事务管理 | 批量操作无@Transactional | 数据一致性 | ✅ 已解决 |
| 5 | 未使用的依赖 | dynamic-datasource（实际单数据源） | 依赖臃肿 | ✅ 已解决 |

### 3.3 低优先级问题 🟢

| 序号 | 问题 | 位置 | 影响 |
|------|------|------|------|
| 1 | 无单元测试 | test目录为空 | 代码质量保障 |
| 2 | 无统一响应处理 | ShowResult手动包装 | 开发效率 |
| 3 | 命名不规范 | BookMarks应为Bookmark | 代码规范 |
| 4 | 静态常量定义在实体类 | `COL_*`常量 | 代码设计 |

---

## 四、开发优化计划

### 第一阶段：代码重构（预计1-2天）

#### 4.1.1 消除硬编码路径 ✅ 已完成
**目标**：支持跨平台运行，路径可配置

**已实现**：
- 创建了 `BookmarkConfig.java` 配置类
- 在 `application.yml` 中添加了配置项
- `BookmarksParserServiceImpl.java` 使用 `ResourceLoader` 加载文件
- `ShowMeListController.java` 注入配置读取路径

```yaml
# application.yml 新增配置
bookmark:
  # 输入文件路径，支持classpath和绝对路径
  input-path: classpath:bookmarks/bookmarks.html
  # 输出文件路径
  output-path: ${user.home}/bookmark-output/result.txt
```

**修改文件**：
- [x] `application.yml` - 添加配置项
- [x] `BookmarkConfig.java` - 新增配置类
- [x] `BookmarksParserServiceImpl.java` - 注入配置读取路径
- [x] `ShowMeListController.java` - 注入配置读取路径

#### 4.1.2 重构包结构 ✅ 部分完成
**目标**：符合Spring Boot最佳实践

**已实现**：
- [x] 移动 `BookMarks2Service.java` 到 service 包
- [x] 添加全局异常处理类
- [x] 添加自定义业务异常类

**待处理**：
- [ ] 重命名类名符合规范（BookMarks → Bookmark）
- [ ] 合并重复实体类
- [ ] 更新所有引用

#### 4.1.3 优化正则解析性能 ✅ 已完成
**目标**：预编译Pattern，提升解析效率

**已实现**：所有正则表达式已预编译为静态常量

```java
public class BookmarksParserServiceImpl {
    // 预编译Pattern为静态常量
    private static final Pattern H3_TAG_PATTERN = Pattern.compile("<H3([^>]*)>", Pattern.CASE_INSENSITIVE);
    private static final Pattern A_TAG_PATTERN = Pattern.compile("<A([^>]*)>", Pattern.CASE_INSENSITIVE);
    private static final Pattern HREF_PATTERN = Pattern.compile("HREF=\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern ADD_DATE_PATTERN = Pattern.compile("ADD_DATE=\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern LAST_MODIFIED_PATTERN = Pattern.compile("LAST_MODIFIED=\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern H3_TITLE_PATTERN = Pattern.compile("\">([^<]+)</H3>", Pattern.CASE_INSENSITIVE);
    private static final Pattern A_TITLE_PATTERN = Pattern.compile("\">([^<]+)</A>", Pattern.CASE_INSENSITIVE);
}
```

**修改文件**：
- [x] `BookmarksParserServiceImpl.java`

#### 4.1.4 合并重复实体类
**目标**：消除代码冗余

```java
@Schema(description = "书签实体")
@Data
@TableName("book_marks")
public class Bookmark implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String href;
    private Long addDate;
    private String title;
    private String type;      // "a" 或 "h3"
    private Long lastModified;

    /**
     * 数据状态：0-原始数据，1-去重后数据
     */
    private Integer status;
}
```

**修改文件**：
- [ ] 删除 `BookMarks2.java`
- [ ] 修改 `BookMarks.java` → `Bookmark.java`
- [ ] 更新所有Mapper和Service

---

### 第二阶段：功能增强（预计2-3天）✅ 已完成

#### 4.2.1 添加文件上传接口 ✅ 已完成
**目标**：支持通过API上传书签文件

**已实现**：
- 在 `ShowMeListController` 中添加了 `POST /BookMarks/upload` 接口
- 创建了 `ParseResult` DTO 类返回解析结果
- 支持解析上传的 HTML 书签文件并保存到数据库

```java
@Operation(summary = "上传书签文件")
@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ShowResult<ParseResult> uploadBookmarks(@RequestParam("file") MultipartFile file)
```

**修改文件**：
- [x] `ShowMeListController.java` - 添加上传接口
- [x] `BookmarksParserService.java` - 添加接口方法
- [x] `BookmarksParserServiceImpl.java` - 实现上传解析逻辑
- [x] `ParseResult.java` - 新增解析结果DTO

#### 4.2.2 支持多种浏览器书签格式
**目标**：自动识别并解析不同浏览器的书签格式

| 浏览器 | 格式特征 | 支持状态 |
|--------|----------|----------|
| Chrome | Netscape Bookmark File Format | ✅ 已支持 |
| Firefox | Netscape Bookmark File Format | ✅ 已支持 |
| Edge | Netscape Bookmark File Format | ✅ 已支持 |
| Safari | plist格式 | ✅ 已支持 |

#### 4.2.3 添加书签分析功能 ✅ 已完成
**目标**：提供书签统计和分析

**已实现**：
- 创建了 `BookmarkAnalysis` DTO 类
- 添加了 `GET /BookMarks/analyze` 接口
- 添加了 `GET /BookMarks/duplicates` 接口
- 支持统计总数、链接数、文件夹数、重复URL、域名分布等

```java
@Data
@Schema(description = "书签分析结果")
public class BookmarkAnalysis {
    private int totalCount;           // 总书签数
    private int folderCount;          // 文件夹数
    private int linkCount;            // 链接数
    private int duplicateCount;       // 重复链接数量
    private Map<String, Integer> domainDistribution;  // 域名分布(Top20)
    private List<String> duplicateUrls;               // 重复链接
    private Long earliestAddDate;     // 最早添加时间
    private Long latestAddDate;       // 最近添加时间
}
```

**新增接口**：
- [x] `GET /BookMarks/analyze` - 分析书签统计
- [x] `GET /BookMarks/duplicates` - 获取重复书签
- [x] `GET /BookMarks/checkLinks` - 检测失效链接（同步，支持限制数量）
- [x] `POST /BookMarks/checkLinks/async` - 异步检测所有链接
- [x] `GET /BookMarks/checkLinks/progress/{taskId}` - 查询检测任务进度

#### 4.2.4 添加导出功能 ✅ 已完成
**目标**：支持多种格式导出

**已实现**：
- 添加了 `GET /BookMarks/export` 接口
- 支持 HTML、Markdown、JSON 三种格式

```java
@Operation(summary = "导出书签")
@GetMapping("/export")
public void exportBookmarks(
    @RequestParam(defaultValue = "html") String format,
    HttpServletResponse response)
```

**支持格式**：
- [x] HTML（标准Netscape格式，可导入浏览器）
- [x] Markdown（带层级的列表格式）
- [x] JSON（结构化数据）

---

### 第三阶段：工程化提升（预计1-2天）✅ 已完成

#### 4.3.1 添加全局异常处理 ✅ 已完成
**目标**：统一异常处理和错误响应

**已实现**：
- 创建了 `GlobalExceptionHandler.java`
- 创建了 `BusinessException.java`

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ShowResult<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ShowResult.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ShowResult<?> handleValidationException(MethodArgumentNotValidException e) {
        // 处理参数校验异常
    }

    @ExceptionHandler(Exception.class)
    public ShowResult<?> handleException(Exception e) {
        log.error("系统异常", e);
        return ShowResult.fail(500, "系统繁忙，请稍后重试");
    }
}
```

**已创建文件**：
- [x] `GlobalExceptionHandler.java`
- [x] `BusinessException.java`

#### 4.3.2 添加参数校验 ✅ 已完成
**目标**：确保接口入参合法

**已实现**：
- 添加了 `spring-boot-starter-validation` 依赖
- Controller添加了 `@Validated` 注解
- 分页参数添加了 `@Min`、`@Max` 校验

```java
@GetMapping("/list")
@Operation(summary = "分页查询书签列表")
public ShowResult<Page<BookMarks>> showMeList(
    @Parameter(description = "页码")
    @RequestParam @Min(value = 1, message = "页码最小为1") Integer page,
    @Parameter(description = "每页条数")
    @RequestParam @Min(value = 1, message = "每页条数最小为1")
                  @Max(value = 100, message = "每页条数最大为100") Integer limit) {
    // ...
}
```

**修改文件**：
- [x] `pom.xml` - 添加validation依赖
- [x] `ShowMeListController.java` - 添加校验注解

#### 4.3.3 添加事务管理 ✅ 已完成
**目标**：保证批量操作的数据一致性

**已实现**：所有批量操作方法都添加了 `@Transactional` 注解

```java
@Transactional(rollbackFor = Exception.class)
public int batchInsert(List<BookMarks> list) {
    return baseMapper.batchInsert(list);
}
```

**修改文件**：
- [x] `BookMarksService.java` - 所有方法添加事务注解
- [x] `BookMarks2Service.java` - 所有方法添加事务注解

#### 4.3.4 添加单元测试 ✅ 已完成
**目标**：覆盖核心业务逻辑

**已实现**：
- 创建了 `BookmarksParserServiceTest.java` - Service层测试
- 创建了 `ShowMeListControllerTest.java` - Controller集成测试

```
src/test/java/wo1261931780/testBookMarkAnalysis/
├── service/
│   └── BookmarksParserServiceTest.java   # 解析服务测试
└── controller/
    └── ShowMeListControllerTest.java     # Controller接口测试
```

**测试用例**：
- [x] 解析Chrome书签文件
- [x] 解析空文件
- [x] 解析特殊字符书签
- [x] 分页查询
- [x] 参数校验测试
- [x] 文件上传测试
- [x] 多格式导出测试
- [x] 书签分析测试

#### 4.3.5 完善API文档 ✅ 已完成
**目标**：提供完整的接口文档

**已实现**：
- 创建了 `OpenApiConfig.java` 配置类
- 所有Controller接口添加了 `@Operation`、`@Parameter`、`@Tag` 注解

```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("书签解析系统 API")
                .version("1.0.0")
                .description("浏览器书签解析、存储、导出服务")
                .contact(new Contact()
                    .name("liujiajun_junw")
                    .email("xxx@example.com")));
    }
}
```

**新增/修改文件**：
- [x] `OpenApiConfig.java` - 新增配置类
- [x] Controller添加`@Operation`、`@Parameter`等注解

---

### 第四阶段：可选优化（未来迭代）✅ 已完成

#### 4.4.1 使用Jsoup替代正则解析 ✅ 已完成
**目标**：更健壮的HTML解析

**已实现**：
- 添加 `jsoup 1.18.3` 依赖
- 创建 `BookmarkParser` 解析器接口
- 创建 `JsoupBookmarkParser` 实现类
- 在 `BookmarksParserService` 中添加 `parseWithJsoup()` 方法
- 在 Controller 中添加 `POST /BookMarks/upload/jsoup` 接口

```java
// 使用Jsoup的CSS选择器解析
public List<BookMarks> parse(InputStream inputStream) {
    Document doc = Jsoup.parse(inputStream, "UTF-8", "");
    Elements links = doc.select("a[href]");  // 选择所有带href的链接
    Elements folders = doc.select("h3");      // 选择所有文件夹
    // ...
}
```

**新增文件**：
- [x] `src/main/java/.../parser/BookmarkParser.java` - 解析器接口
- [x] `src/main/java/.../parser/impl/JsoupBookmarkParser.java` - Jsoup实现

#### 4.4.2 添加缓存机制 ✅ 已完成
**目标**：减少数据库查询，提升性能

**已实现**：
- 添加 `spring-boot-starter-cache` 依赖
- 添加 `caffeine` 本地缓存依赖
- 创建 `CacheConfig` 配置类
- 在 `analyzeBookmarks()` 方法添加 `@Cacheable` 注解
- 在 `selectByUrl()` 方法添加 `@Cacheable` 注解
- 在数据变更方法添加 `@CacheEvict` 注解

```java
@Configuration
@EnableCaching
public class CacheConfig {
    public static final String CACHE_BOOKMARK_ANALYSIS = "bookmarkAnalysis";
    public static final String CACHE_BOOKMARK_BY_URL = "bookmarkByUrl";
    public static final String CACHE_DOMAIN_STATS = "domainStats";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(500)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .expireAfterAccess(5, TimeUnit.MINUTES));
        return cacheManager;
    }
}
```

**新增文件**：
- [x] `src/main/java/.../config/CacheConfig.java` - 缓存配置

#### 4.4.3 添加前端界面
**目标**：提供可视化管理

技术选型：
- Vue 3 + Element Plus
- 或 React + Ant Design

功能：
- 书签列表展示（支持搜索、筛选）
- 文件上传
- 统计图表
- 导出下载

**状态**：⏳ 待开发（可选）

#### 4.4.4 Docker化部署 ✅ 已完成
**目标**：容器化部署

**已实现**：
- 创建多阶段构建 `Dockerfile`（构建阶段 + 运行阶段）
- 创建 `docker-compose.yml`（MySQL + App + Redis可选）
- 创建 `.env.example` 环境变量模板
- 创建 `.dockerignore` 忽略文件
- 创建 MySQL 初始化脚本
- 添加 `spring-boot-starter-actuator` 健康检查支持
- 更新 `application.yml` 支持环境变量配置

```yaml
# docker-compose.yml 服务
services:
  mysql:      # MySQL 8.0 数据库
  app:        # Spring Boot 应用
  redis:      # Redis 缓存（可选，使用 --profile redis 启动）
```

**启动命令**：
```bash
# 构建并启动
docker-compose up -d --build

# 包含Redis缓存
docker-compose --profile redis up -d --build

# 查看日志
docker-compose logs -f app
```

**新增文件**：
- [x] `Dockerfile` - 多阶段构建镜像
- [x] `docker-compose.yml` - 编排文件
- [x] `.env.example` - 环境变量模板
- [x] `.dockerignore` - Docker构建忽略
- [x] `docker/mysql/init/01-init.sql` - 数据库初始化脚本

---

## 五、里程碑计划

| 阶段 | 目标 | 预计工时 | 优先级 | 状态 |
|------|------|----------|--------|------|
| 第一阶段 | 代码重构 | 1-2天 | P0 | ✅ 已完成 |
| 第二阶段 | 功能增强 | 2-3天 | P1 | ✅ 已完成 |
| 第三阶段 | 工程化提升 | 1-2天 | P1 | ✅ 已完成 |
| 第四阶段 | 可选优化 | 按需 | P2 | ✅ 已完成 |

---

## 六、已完成优化汇总

### 6.1 第一阶段完成项 (2026年1月4日)

| 序号 | 优化项 | 说明 |
|------|--------|------|
| 1 | 依赖升级 | Spring Boot 3.4.1, MyBatis-Plus 3.5.9, springdoc-openapi 2.7.0 |
| 2 | 消除硬编码路径 | 创建 `BookmarkConfig` 配置类，支持 classpath 和绝对路径 |
| 3 | 包结构重构 | 将 `BookMarks2Service` 移至 service 包 |
| 4 | 正则表达式优化 | 预编译所有 Pattern 为静态常量 |
| 5 | 排序算法优化 | 使用 `Comparator.comparingLong()` |
| 6 | 全局异常处理 | 新增 `GlobalExceptionHandler` 和 `BusinessException` |
| 7 | 参数校验 | 添加 `@Validated`、`@Min`、`@Max` 注解 |
| 8 | 事务管理 | 所有批量操作添加 `@Transactional` |
| 9 | 移除未使用依赖 | 注释掉 `dynamic-datasource` 和 `mybatis-plus-generator` |

### 6.2 第二阶段完成项 (2026年1月4日)

| 序号 | 优化项 | 说明 |
|------|--------|------|
| 1 | 文件上传接口 | `POST /BookMarks/upload` 支持上传HTML书签文件并解析 |
| 2 | 书签分析功能 | `GET /BookMarks/analyze` 统计分析（域名分布、重复检测等） |
| 3 | 获取重复书签 | `GET /BookMarks/duplicates` 返回重复URL列表 |
| 4 | 多格式导出 | `GET /BookMarks/export` 支持 HTML/Markdown/JSON 格式 |
| 5 | OpenAPI配置 | 创建 `OpenApiConfig.java` 完善API文档 |
| 6 | 单元测试 | 创建 Service 和 Controller 测试类 |

### 6.4 第四阶段完成项 (2026年1月)

| 序号 | 优化项 | 说明 |
|------|--------|------|
| 1 | Jsoup解析器 | 创建 `BookmarkParser` 接口和 `JsoupBookmarkParser` 实现 |
| 2 | Caffeine缓存 | 添加本地缓存配置，`@Cacheable`/`@CacheEvict` 注解 |
| 3 | Docker化部署 | 多阶段构建Dockerfile + docker-compose编排 |
| 4 | Actuator健康检查 | 添加 `/actuator/health` 端点支持 |
| 5 | 环境变量配置 | application.yml 支持环境变量覆盖 |

### 6.5 第五阶段完成项 (2026年1月)

| 序号 | 优化项 | 说明 |
|------|--------|------|
| 1 | 实体类重构 | 创建 `BaseBookmark` 基类，`BookMarks`/`BookMarks2` 继承它 |
| 2 | 常量类提取 | 创建 `BookmarkColumns` 常量类，移除实体类中的列名常量 |
| 3 | 失效链接检测 | 同步/异步检测书签链接是否可访问 |
| 4 | 异步任务支持 | 启用 `@EnableAsync`，支持大量链接异步检测 |

### 6.6 第六阶段完成项 (2026年1月)

| 序号 | 优化项 | 说明 |
|------|--------|------|
| 1 | Safari plist解析 | 添加 dd-plist 依赖，创建 `SafariBookmarkParser` 解析器 |
| 2 | 智能格式识别 | `uploadAndParseAuto()` 自动识别 HTML/plist 格式 |
| 3 | 多格式上传接口 | 新增 `/upload/safari` 和 `/upload/auto` 接口 |

### 6.3 新增/修改文件清单

**第一阶段新增文件：**
- `src/main/java/.../config/BookmarkConfig.java` - 配置属性类
- `src/main/java/.../common/exception/GlobalExceptionHandler.java` - 全局异常处理
- `src/main/java/.../common/exception/BusinessException.java` - 自定义异常
- `DEVELOPMENT_PLAN.md` - 开发计划文档

**第二阶段新增文件：**
- `src/main/java/.../config/OpenApiConfig.java` - OpenAPI配置
- `src/main/java/.../entity/BookmarkAnalysis.java` - 分析结果DTO
- `src/main/java/.../entity/ParseResult.java` - 解析结果DTO
- `src/test/java/.../service/BookmarksParserServiceTest.java` - Service测试
- `src/test/java/.../controller/ShowMeListControllerTest.java` - Controller测试

**第四阶段新增文件：**
- `src/main/java/.../parser/BookmarkParser.java` - 解析器接口
- `src/main/java/.../parser/impl/JsoupBookmarkParser.java` - Jsoup实现
- `src/main/java/.../config/CacheConfig.java` - Caffeine缓存配置
- `Dockerfile` - 多阶段构建镜像
- `docker-compose.yml` - 容器编排文件
- `.env.example` - 环境变量模板
- `.dockerignore` - Docker构建忽略文件
- `docker/mysql/init/01-init.sql` - MySQL初始化脚本

**第五阶段新增文件：**
- `src/main/java/.../entity/BaseBookmark.java` - 书签基础实体类
- `src/main/java/.../entity/BookmarkColumns.java` - 列名常量类
- `src/main/java/.../entity/LinkCheckResult.java` - 链接检测结果DTO
- `src/main/java/.../entity/LinkCheckReport.java` - 链接检测报告DTO
- `src/main/java/.../service/LinkCheckService.java` - 链接检测服务接口
- `src/main/java/.../service/impl/LinkCheckServiceImpl.java` - 链接检测服务实现

**第六阶段新增文件：**
- `src/main/java/.../parser/impl/SafariBookmarkParser.java` - Safari plist书签解析器

**修改文件：**
- `pom.xml` - 依赖升级和清理，添加 dd-plist 依赖
- `application.yml` - 添加 bookmark 配置
- `BookMarksService.java` - 事务管理
- `BookMarks2Service.java` - 事务管理 + 移动包
- `BookmarksParserService.java` - 新增接口方法
- `BookmarksParserServiceImpl.java` - 性能优化 + 新功能实现
- `ShowMeListController.java` - 参数校验 + Swagger注解 + 新接口
- `MybatisPlusConfig.java` - 添加 DbType.MYSQL
- `BookMarks.java`, `BookMarks2.java` - Swagger注解迁移
- `BookMarksMapper.java`, `BookMarks2Mapper.java` - 方法重命名
- `BookMarksMapper.xml`, `BookMarks2Mapper.xml` - 方法ID更新

---

## 七、注意事项

1. **数据库兼容性**
   - 升级后需要测试现有数据的兼容性
   - 建议在测试环境先行验证

2. **API兼容性**
   - Swagger访问地址变更：`http://localhost:8000/swagger-ui.html`
   - 如有前端对接，需同步更新

3. **回滚方案**
   - 保留原有pom.xml备份
   - 使用Git标签标记升级前版本

---

## 七、参考资料

- [Spring Boot 3.4 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.4-Release-Notes)
- [MyBatis-Plus 官方文档](https://baomidou.com/guides/new-code-generator/)
- [springdoc-openapi 迁移指南](https://springdoc.org/v2/)
- [Jsoup 官方文档](https://jsoup.org/)

---

*文档版本：v1.5*
*最后更新：2026年1月*
*更新内容：第五阶段完成 - 实体类重构、失效链接检测功能*
