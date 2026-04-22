# 项目规格说明书 - test-BookMarkAnalysis

## 基本信息

| 项目名 | test-BookMarkAnalysis |
|--------|----------------------|
| 路径 | /Users/junw/Documents/GitHub/test-BookMarkAnalysis |
| 简介 | 浏览器书签解析、存储、分析和导出系统 |
| 技术栈 | Spring Boot 3.4.1 + MyBatis-Plus 3.5.9 + MySQL + Jsoup + Caffeine |
| License | AGPL-3.0 |

## 功能概述

- 多格式书签解析（Chrome/Firefox/Edge HTML、Safari plist）
- 书签上传、存储、统计分析
- 链接有效性检测
- 多格式导出（HTML/Markdown/JSON）
- Docker 容器化部署

## 项目结构

```
src/main/java/wo1261931780/
├── controller/     # 控制器层（Bookmark/Upload/Export）
├── service/        # 服务层（Parse/Analysis/Export/Check）
├── mapper/         # MyBatis Mapper
├── entity/        # 实体类
├── parser/        # HTML/Plist 解析器
├── config/        # Swagger/Cache 配置
└── utils/         # 工具类
```

## 编译问题

**编译问题：JDK 25 与 Lombok 1.18.x 版本不兼容**

错误信息：
```
Fatal error compiling: java.lang.ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag :: UNKNOWN
```

原因分析：
- 项目使用 Lombok 1.18.x，该版本与 JDK 25 不兼容
- JDK 25 移除了部分 sun.misc.Unsafe 相关内部 API
- lombok.permit.Permit 尝试调用已废弃的 sun.misc.Unsafe::objectFieldOffset 导致警告

建议解决方案：
1. 升级 Lombok 至 1.18.32+ 版本（支持 JDK 25）
2. 或降级 JDK 至 17/21
3. 或在编译参数中添加 `--add-opens=java.base/java.lang=ALL-UNNAMED`

## 数据库设计

### bookmark 表

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| title | varchar(255) | 书签标题 |
| url | varchar(500) | 书签URL |
| domain | varchar(100) | 域名 |
| folder_path | varchar(255) | 文件夹路径 |
| create_time | datetime | 创建时间 |
| status | int | 状态码 |

## API 接口

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/bookmarks/upload | POST | 上传书签文件 |
| /api/bookmarks | GET | 获取书签列表 |
| /api/bookmarks/analysis | GET | 统计分析 |
| /api/bookmarks/export | GET | 导出书签 |

## 环境要求

- JDK 17+（当前使用 JDK 25 有兼容问题）
- MySQL 8.x
- Maven 3.6+
