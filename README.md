# 📚 BookMarkAnalysis - 浏览器书签解析系统

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.9-blue.svg)](https://baomidou.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

一个功能完整的浏览器书签解析、存储、分析和导出系统。支持 Chrome、Firefox、Edge、Safari 等主流浏览器的书签文件解析。

---

## ✨ 功能特性

| 功能 | 说明 |
|------|------|
| 🔍 多格式解析 | 支持 HTML（Chrome/Firefox/Edge）和 plist（Safari）格式 |
| 📤 文件上传 | 通过 API 上传书签文件并自动解析入库 |
| 📊 统计分析 | 域名分布、重复检测、时间范围统计 |
| 🔗 链接检测 | 同步/异步检测书签链接是否可访问 |
| 📥 多格式导出 | 支持 HTML、Markdown、JSON 格式导出 |
| 🗂️ 书签去重 | 自动识别并处理重复书签 |
| 🐳 Docker 部署 | 一键容器化部署，开箱即用 |

---

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.4.1 | Web 框架 |
| MyBatis-Plus | 3.5.9 | ORM 框架 |
| MySQL | 8.x | 数据库 |
| Jsoup | 1.18.3 | HTML 解析 |
| dd-plist | 1.28 | Safari plist 解析 |
| Caffeine | - | 本地缓存 |
| springdoc-openapi | 2.7.0 | API 文档 |
| Hutool | 5.8.34 | 工具库 |
| Docker | - | 容器化部署 |

---

## 🚀 快速开始

### 方式一：Docker 部署（推荐）

```bash
# 1. 克隆项目
git clone https://github.com/junwOpenSourceProjects/test-BookMarkAnalysis.git
cd test-BookMarkAnalysis

# 2. 复制环境变量配置
cp .env.example .env

# 3. 启动服务（MySQL + 应用）
docker-compose up -d --build

# 4. 查看日志
docker-compose logs -f app

# 5. 访问服务
# API 文档：http://localhost:8000/swagger-ui.html
# 健康检查：http://localhost:8000/actuator/health
```

**可选：启用 Redis 缓存**
```bash
docker-compose --profile redis up -d --build
```

### 方式二：本地开发

#### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.x

#### 2. 数据库初始化

```bash
# 连接 MySQL 执行建表脚本
mysql -u root -p < sql/schema.sql
```

或手动创建数据库：
```sql
CREATE DATABASE bookmarks DEFAULT CHARACTER SET utf8mb4;
```

#### 3. 修改配置

编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bookmarks?useUnicode=true&characterEncoding=utf-8
    username: your_username
    password: your_password
```

#### 4. 运行项目

```bash
# 编译
./mvnw clean compile

# 运行
./mvnw spring-boot:run

# 或打包后运行
./mvnw clean package -DskipTests
java -jar target/test-BookMarkAnalysis-0.0.1-SNAPSHOT.jar
```

#### 5. 访问服务

| 服务 | 地址 |
|------|------|
| API 文档 | http://localhost:8000/swagger-ui.html |
| 健康检查 | http://localhost:8000/actuator/health |
| 接口前缀 | http://localhost:8000/BookMarks |

---

## 📖 API 接口

### 书签解析

| 方法 | 接口 | 说明 |
|------|------|------|
| POST | `/BookMarks/upload` | 上传 HTML 书签文件（正则解析） |
| POST | `/BookMarks/upload/jsoup` | 上传 HTML 书签文件（Jsoup 解析） |
| POST | `/BookMarks/upload/safari` | 上传 Safari plist 书签文件 |
| POST | `/BookMarks/upload/auto` | 自动识别格式并解析 |

### 书签查询

| 方法 | 接口 | 说明 |
|------|------|------|
| GET | `/BookMarks/list?page=1&limit=10` | 分页查询书签列表 |
| GET | `/BookMarks/{id}` | 根据 ID 查询书签 |
| GET | `/BookMarks/url?url=xxx` | 根据 URL 查询书签 |

### 书签分析

| 方法 | 接口 | 说明 |
|------|------|------|
| GET | `/BookMarks/analyze` | 获取书签统计分析 |
| GET | `/BookMarks/duplicates` | 获取重复书签列表 |

### 链接检测

| 方法 | 接口 | 说明 |
|------|------|------|
| GET | `/BookMarks/checkLinks?limit=10` | 同步检测链接（限制数量） |
| POST | `/BookMarks/checkLinks/async` | 异步检测所有链接 |
| GET | `/BookMarks/checkLinks/progress/{taskId}` | 查询检测任务进度 |

### 书签导出

| 方法 | 接口 | 说明 |
|------|------|------|
| GET | `/BookMarks/export?format=html` | 导出为 HTML 格式 |
| GET | `/BookMarks/export?format=markdown` | 导出为 Markdown 格式 |
| GET | `/BookMarks/export?format=json` | 导出为 JSON 格式 |

### 书签管理

| 方法 | 接口 | 说明 |
|------|------|------|
| POST | `/BookMarks/removeDuplicates` | 去重并保存到 book_marks2 表 |
| DELETE | `/BookMarks/{id}` | 删除指定书签 |

---

## 📁 项目结构

```
test-BookMarkAnalysis/
├── src/main/java/wo1261931780/testBookMarkAnalysis/
│   ├── TestBookMarkAnalysisApplication.java    # 启动类
│   ├── common/
│   │   └── exception/
│   │       ├── GlobalExceptionHandler.java     # 全局异常处理
│   │       └── BusinessException.java          # 业务异常
│   ├── config/
│   │   ├── BookmarkConfig.java                 # 书签配置
│   │   ├── CacheConfig.java                    # 缓存配置
│   │   ├── MybatisPlusConfig.java              # MyBatis-Plus配置
│   │   └── OpenApiConfig.java                  # Swagger配置
│   ├── controller/
│   │   └── ShowMeListController.java           # 书签API控制器
│   ├── entity/
│   │   ├── BaseBookmark.java                   # 书签基类
│   │   ├── BookMarks.java                      # 原始书签实体
│   │   ├── BookMarks2.java                     # 去重书签实体
│   │   ├── BookmarkAnalysis.java               # 分析结果DTO
│   │   ├── ParseResult.java                    # 解析结果DTO
│   │   ├── LinkCheckResult.java                # 链接检测结果
│   │   └── LinkCheckReport.java                # 链接检测报告
│   ├── mapper/
│   │   ├── BookMarksMapper.java
│   │   └── BookMarks2Mapper.java
│   ├── parser/
│   │   ├── BookmarkParser.java                 # 解析器接口
│   │   └── impl/
│   │       ├── JsoupBookmarkParser.java        # Jsoup解析器
│   │       └── SafariBookmarkParser.java       # Safari解析器
│   └── service/
│       ├── BookMarksService.java
│       ├── BookmarksParserService.java
│       ├── LinkCheckService.java               # 链接检测服务
│       └── impl/
│           ├── BookmarksParserServiceImpl.java
│           └── LinkCheckServiceImpl.java
├── src/main/resources/
│   ├── application.yml                         # 配置文件
│   └── wo1261931780/.../mapper/
│       ├── BookMarksMapper.xml
│       └── BookMarks2Mapper.xml
├── sql/
│   └── schema.sql                              # 完整建表脚本
├── docker/
│   └── mysql/init/
│       └── 01-init.sql                         # Docker MySQL初始化
├── Dockerfile                                  # Docker镜像构建
├── docker-compose.yml                          # Docker编排
├── .env.example                                # 环境变量模板
├── pom.xml                                     # Maven配置
├── DEVELOPMENT_PLAN.md                         # 开发计划文档
└── README.md                                   # 本文档
```

---

## 🗄️ 数据库设计

### 主键策略

- **类型**：`BIGINT`
- **生成方式**：后端雪花 ID（MyBatis-Plus `IdType.ASSIGN_ID`）
- **外键**：不使用数据库外键，通过后端逻辑关联

### 核心表结构

| 表名 | 说明 |
|------|------|
| `book_marks` | 原始书签数据表 |
| `book_marks2` | 去重后书签数据表 |
| `link_check_record` | 链接检测记录表 |
| `import_record` | 导入记录表 |
| `bookmark_category` | 书签分类表 |
| `bookmark_tag` | 标签表 |

完整建表脚本见 [sql/schema.sql](sql/schema.sql)

---

## 📋 开发阶段

项目按阶段迭代开发，详细计划见 [DEVELOPMENT_PLAN.md](DEVELOPMENT_PLAN.md)

### 第一阶段：代码重构 ✅

| 完成项 | 说明 |
|--------|------|
| 依赖升级 | Spring Boot 3.4.1, MyBatis-Plus 3.5.9 |
| 消除硬编码 | 配置类 + application.yml |
| 包结构重构 | 按职责分包 |
| 正则优化 | Pattern 预编译 |
| 全局异常处理 | 统一错误响应 |
| 参数校验 | @Validated + JSR-303 |
| 事务管理 | @Transactional |

### 第二阶段：功能增强 ✅

| 完成项 | 说明 |
|--------|------|
| 文件上传 | POST /upload 接口 |
| 书签分析 | 域名统计、重复检测 |
| 多格式导出 | HTML/Markdown/JSON |
| OpenAPI 文档 | Swagger UI |
| 单元测试 | Service + Controller 测试 |

### 第三阶段：工程化提升 ✅

| 完成项 | 说明 |
|--------|------|
| API 文档完善 | @Operation、@Parameter 注解 |
| 测试覆盖 | 核心业务逻辑测试 |

### 第四阶段：高级功能 ✅

| 完成项 | 说明 |
|--------|------|
| Jsoup 解析器 | 更健壮的 HTML 解析 |
| Caffeine 缓存 | 本地缓存提升性能 |
| Docker 部署 | 多阶段构建 + docker-compose |
| 健康检查 | Actuator /health 端点 |

### 第五阶段：实体重构 ✅

| 完成项 | 说明 |
|--------|------|
| BaseBookmark 基类 | 公共字段抽取 |
| 常量类提取 | BookmarkColumns |
| 链接检测 | 同步/异步检测失效链接 |

### 第六阶段：Safari 支持 ✅

| 完成项 | 说明 |
|--------|------|
| Safari plist 解析 | dd-plist 库 |
| 智能格式识别 | 自动判断 HTML/plist |
| 雪花ID | 主键改为后端生成 |

---

## 🔧 配置说明

### application.yml 主要配置

```yaml
server:
  port: 8000

spring:
  datasource:
    url: jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${MYSQL_DATABASE:bookmarks}
    username: ${MYSQL_USER:bookmarks}
    password: ${MYSQL_PASSWORD:bookmarks123}

# 书签配置
bookmark:
  input-path: classpath:bookmarks/bookmarks.html
  output-path: ${user.home}/bookmark-output/result.txt
```

### 环境变量

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `MYSQL_HOST` | localhost | MySQL 主机 |
| `MYSQL_PORT` | 3306 | MySQL 端口 |
| `MYSQL_DATABASE` | bookmarks | 数据库名 |
| `MYSQL_USER` | bookmarks | 数据库用户 |
| `MYSQL_PASSWORD` | bookmarks123 | 数据库密码 |

---

## 🧪 测试

```bash
# 运行所有测试
./mvnw test

# 运行指定测试类
./mvnw test -Dtest=BookmarksParserServiceTest

# 生成测试报告
./mvnw test jacoco:report
```

---

## 📦 构建与部署

### Maven 打包

```bash
# 跳过测试打包
./mvnw clean package -DskipTests

# 运行 JAR
java -jar target/test-BookMarkAnalysis-0.0.1-SNAPSHOT.jar
```

### Docker 构建

```bash
# 构建镜像
docker build -t bookmark-analysis:latest .

# 运行容器
docker run -d -p 8000:8000 \
  -e MYSQL_HOST=your-mysql-host \
  -e MYSQL_PASSWORD=your-password \
  bookmark-analysis:latest
```

---

## 📄 License

MIT License - 详见 [LICENSE](LICENSE) 文件

---

## 👤 作者

**liujiajun_junw**

---

## 🔗 相关链接

- [Spring Boot 3.4 文档](https://docs.spring.io/spring-boot/docs/3.4.1/reference/html/)
- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [Jsoup 官方文档](https://jsoup.org/)
- [开发计划文档](DEVELOPMENT_PLAN.md)
