-- ============================================
-- 书签分析系统数据库初始化脚本
-- 此脚本会在MySQL容器首次启动时自动执行
-- 编码：utf8mb4
-- 主键：bigint（后端雪花ID生成）
-- 外键：不使用，通过后端逻辑关联
-- ============================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;

-- ============================================
-- 1. 书签表（原始数据）
-- ============================================
CREATE TABLE IF NOT EXISTS `book_marks` (
    `id` BIGINT NOT NULL COMMENT '主键ID（雪花ID，后端生成）',
    `href` VARCHAR(2048) DEFAULT NULL COMMENT '链接地址',
    `add_date` BIGINT DEFAULT NULL COMMENT '添加时间（Unix时间戳）',
    `title` VARCHAR(512) DEFAULT NULL COMMENT '书签标题',
    `type` VARCHAR(10) DEFAULT NULL COMMENT '类型：a-链接，h3-文件夹',
    `last_modified` BIGINT DEFAULT NULL COMMENT '最后修改时间（Unix时间戳）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`) COMMENT '类型索引',
    KEY `idx_href` (`href`(255)) COMMENT '链接前缀索引',
    KEY `idx_add_date` (`add_date`) COMMENT '添加时间索引',
    KEY `idx_title` (`title`(100)) COMMENT '标题前缀索引'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='书签表（原始数据）';

-- ============================================
-- 2. 书签表（去重后数据）
-- ============================================
CREATE TABLE IF NOT EXISTS `book_marks2` (
    `id` BIGINT NOT NULL COMMENT '主键ID（雪花ID，后端生成）',
    `href` VARCHAR(2048) DEFAULT NULL COMMENT '链接地址',
    `add_date` BIGINT DEFAULT NULL COMMENT '添加时间（Unix时间戳）',
    `title` VARCHAR(512) DEFAULT NULL COMMENT '书签标题',
    `type` VARCHAR(10) DEFAULT NULL COMMENT '类型：a-链接，h3-文件夹',
    `last_modified` BIGINT DEFAULT NULL COMMENT '最后修改时间（Unix时间戳）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`) COMMENT '类型索引',
    KEY `idx_href` (`href`(255)) COMMENT '链接前缀索引',
    KEY `idx_add_date` (`add_date`) COMMENT '添加时间索引',
    KEY `idx_title` (`title`(100)) COMMENT '标题前缀索引',
    UNIQUE KEY `uk_href` (`href`(500)) COMMENT '链接唯一索引（用于去重）'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='书签表（去重后数据）';

-- ============================================
-- 3. 链接检测记录表
-- ============================================
CREATE TABLE IF NOT EXISTS `link_check_record` (
    `id` BIGINT NOT NULL COMMENT '主键ID（雪花ID，后端生成）',
    `bookmark_id` BIGINT NOT NULL COMMENT '关联的书签ID（后端逻辑关联，非外键）',
    `url` VARCHAR(2048) NOT NULL COMMENT '检测的URL',
    `status_code` INT DEFAULT NULL COMMENT 'HTTP状态码',
    `accessible` TINYINT(1) DEFAULT 0 COMMENT '是否可访问：0-否，1-是',
    `error_message` VARCHAR(500) DEFAULT NULL COMMENT '错误信息',
    `response_time` BIGINT DEFAULT NULL COMMENT '响应时间（毫秒）',
    `check_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '检测时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_bookmark_id` (`bookmark_id`) COMMENT '书签ID索引（后端逻辑关联）',
    KEY `idx_accessible` (`accessible`) COMMENT '可访问状态索引',
    KEY `idx_check_time` (`check_time`) COMMENT '检测时间索引',
    KEY `idx_status_code` (`status_code`) COMMENT '状态码索引'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='链接检测记录表';

-- ============================================
-- 4. 导入记录表
-- ============================================
CREATE TABLE IF NOT EXISTS `import_record` (
    `id` BIGINT NOT NULL COMMENT '主键ID（雪花ID，后端生成）',
    `file_name` VARCHAR(255) NOT NULL COMMENT '导入文件名',
    `file_type` VARCHAR(20) DEFAULT NULL COMMENT '文件类型：html, plist',
    `browser_type` VARCHAR(50) DEFAULT NULL COMMENT '浏览器类型：chrome, firefox, safari, edge',
    `total_count` INT DEFAULT 0 COMMENT '总书签数',
    `success_count` INT DEFAULT 0 COMMENT '成功导入数',
    `fail_count` INT DEFAULT 0 COMMENT '失败数',
    `duplicate_count` INT DEFAULT 0 COMMENT '重复数',
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending, processing, success, failed',
    `error_message` TEXT DEFAULT NULL COMMENT '错误信息',
    `import_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '导入时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`) COMMENT '状态索引',
    KEY `idx_browser_type` (`browser_type`) COMMENT '浏览器类型索引',
    KEY `idx_import_time` (`import_time`) COMMENT '导入时间索引'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='导入记录表';

-- 授予应用用户权限
GRANT ALL PRIVILEGES ON bookmarks.* TO 'bookmarks'@'%';
FLUSH PRIVILEGES;

SELECT '数据库初始化完成！' AS message;
