-- ============================================
-- 书签分析系统 MySQL 建表脚本
-- 数据库：bookmarks
-- 编码：utf8mb4
-- 主键：bigint（后端雪花ID生成）
-- 外键：不使用，通过后端逻辑关联
-- ============================================

-- 创建数据库（如不存在）
CREATE DATABASE IF NOT EXISTS `bookmarks`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE `bookmarks`;

-- ============================================
-- 1. 书签表（原始数据）
-- ============================================
DROP TABLE IF EXISTS `book_marks`;
CREATE TABLE `book_marks` (
    `id` BIGINT NOT NULL COMMENT '主键ID（雪花ID，后端生成）',
    `href` LONGTEXT DEFAULT NULL COMMENT '链接地址',
    `add_date` BIGINT DEFAULT NULL COMMENT '添加时间（Unix时间戳）',
    `title` TEXT DEFAULT NULL COMMENT '书签标题',
    `type` VARCHAR(10) DEFAULT NULL COMMENT '类型：a-链接，h3-文件夹',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父级目录ID（关联本表主键）',
    `sort_order` INT DEFAULT 0 COMMENT '排序权重',
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
DROP TABLE IF EXISTS `book_marks2`;
CREATE TABLE `book_marks2` (
    `id` BIGINT NOT NULL COMMENT '主键ID（雪花ID，后端生成）',
    `href` LONGTEXT DEFAULT NULL COMMENT '链接地址',
    `add_date` BIGINT DEFAULT NULL COMMENT '添加时间（Unix时间戳）',
    `title` TEXT DEFAULT NULL COMMENT '书签标题',
    `type` VARCHAR(10) DEFAULT NULL COMMENT '类型：a-链接，h3-文件夹',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父级目录ID（关联本表主键）',
    `sort_order` INT DEFAULT 0 COMMENT '排序权重',
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
DROP TABLE IF EXISTS `link_check_record`;
CREATE TABLE `link_check_record` (
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
-- 4. 书签分类表
-- ============================================
DROP TABLE IF EXISTS `bookmark_category`;
CREATE TABLE `bookmark_category` (
    `id` BIGINT NOT NULL COMMENT '主键ID（雪花ID，后端生成）',
    `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父分类ID（后端逻辑关联，非外键）',
    `level` INT DEFAULT 1 COMMENT '层级深度',
    `sort_order` INT DEFAULT 0 COMMENT '排序顺序',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '分类描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`) COMMENT '父分类索引（后端逻辑关联）',
    KEY `idx_level` (`level`) COMMENT '层级索引',
    KEY `idx_sort_order` (`sort_order`) COMMENT '排序索引'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='书签分类表';

-- ============================================
-- 5. 书签与分类关联表
-- ============================================
DROP TABLE IF EXISTS `bookmark_category_rel`;
CREATE TABLE `bookmark_category_rel` (
    `id` BIGINT NOT NULL COMMENT '主键ID（雪花ID，后端生成）',
    `bookmark_id` BIGINT NOT NULL COMMENT '书签ID（后端逻辑关联，非外键）',
    `category_id` BIGINT NOT NULL COMMENT '分类ID（后端逻辑关联，非外键）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_bookmark_id` (`bookmark_id`) COMMENT '书签ID索引',
    KEY `idx_category_id` (`category_id`) COMMENT '分类ID索引',
    UNIQUE KEY `uk_bookmark_category` (`bookmark_id`, `category_id`) COMMENT '书签分类唯一索引'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='书签与分类关联表';

-- ============================================
-- 6. 导入记录表
-- ============================================
DROP TABLE IF EXISTS `import_record`;
CREATE TABLE `import_record` (
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

-- ============================================
-- 7. 标签表
-- ============================================
DROP TABLE IF EXISTS `bookmark_tag`;
CREATE TABLE `bookmark_tag` (
    `id` BIGINT NOT NULL COMMENT '主键ID（雪花ID，后端生成）',
    `name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `color` VARCHAR(20) DEFAULT NULL COMMENT '标签颜色（十六进制）',
    `usage_count` INT DEFAULT 0 COMMENT '使用次数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`) COMMENT '标签名唯一索引',
    KEY `idx_usage_count` (`usage_count`) COMMENT '使用次数索引'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='标签表';

-- ============================================
-- 8. 书签与标签关联表
-- ============================================
DROP TABLE IF EXISTS `bookmark_tag_rel`;
CREATE TABLE `bookmark_tag_rel` (
    `id` BIGINT NOT NULL COMMENT '主键ID（雪花ID，后端生成）',
    `bookmark_id` BIGINT NOT NULL COMMENT '书签ID（后端逻辑关联，非外键）',
    `tag_id` BIGINT NOT NULL COMMENT '标签ID（后端逻辑关联，非外键）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_bookmark_id` (`bookmark_id`) COMMENT '书签ID索引',
    KEY `idx_tag_id` (`tag_id`) COMMENT '标签ID索引',
    UNIQUE KEY `uk_bookmark_tag` (`bookmark_id`, `tag_id`) COMMENT '书签标签唯一索引'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='书签与标签关联表';

-- ============================================
-- AI 可恢复全量重分类任务
-- ============================================
CREATE TABLE IF NOT EXISTS `bookmark_ai_classification_task` (
    `id` BIGINT NOT NULL COMMENT '雪花任务ID',
    `strategy` VARCHAR(32) NOT NULL DEFAULT 'rebuild-domain-ai' COMMENT '任务模式',
    `model_name` VARCHAR(128) NOT NULL COMMENT 'AI模型名称',
    `api_base_url` VARCHAR(512) DEFAULT NULL COMMENT '非机密AI接口地址',
    `status` VARCHAR(20) NOT NULL COMMENT 'QUEUED/RUNNING/PAUSED/RECOVERABLE/COMPLETED/FAILED',
    `phase` VARCHAR(48) NOT NULL DEFAULT 'PREPARING' COMMENT '当前重建阶段',
    `total_count` INT NOT NULL DEFAULT 0 COMMENT '快照书签总数',
    `rule_matched_count` INT NOT NULL DEFAULT 0 COMMENT '兼容旧统计字段',
    `ai_matched_count` INT NOT NULL DEFAULT 0 COMMENT 'AI分析完成书签数',
    `failed_count` INT NOT NULL DEFAULT 0 COMMENT '可重试失败工作单元数',
    `recovery_count` INT NOT NULL DEFAULT 0 COMMENT '恢复次数',
    `total_work_unit_count` INT NOT NULL DEFAULT 0 COMMENT '工作单元总数',
    `completed_work_unit_count` INT NOT NULL DEFAULT 0 COMMENT '成功工作单元数',
    `created_folder_count` INT NOT NULL DEFAULT 0 COMMENT '已创建目录数',
    `moved_bookmark_count` INT NOT NULL DEFAULT 0 COMMENT '已移动书签数',
    `updated_title_count` INT NOT NULL DEFAULT 0 COMMENT '已更新标题数',
    `started_at` DATETIME NULL,
    `completed_at` DATETIME NULL,
    `tree_cleared_at` DATETIME NULL COMMENT '旧目录打散完成时间',
    `paused_at` DATETIME NULL,
    `resumed_at` DATETIME NULL,
    `error_message` TEXT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_phase_status` (`phase`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI书签重分类任务';

CREATE TABLE IF NOT EXISTS `bookmark_ai_classification_result` (
    `id` BIGINT NOT NULL COMMENT '雪花结果ID',
    `task_id` BIGINT NOT NULL,
    `domain_group_id` BIGINT NULL,
    `bookmark_id` BIGINT NOT NULL,
    `original_title` TEXT NULL,
    `suggested_title` TEXT NULL,
    `suggested_folder` VARCHAR(512) NULL,
    `logical_folder_key` VARCHAR(128) NULL COMMENT '重试幂等目录键',
    `target_folder_id` BIGINT NULL,
    `keywords` JSON NULL,
    `page_type` VARCHAR(128) NULL,
    `topic_hint` VARCHAR(256) NULL,
    `confidence` INT NULL,
    `reason` TEXT NULL,
    `analysis_json` JSON NULL COMMENT '结构化AI分析结果',
    `source` VARCHAR(20) NOT NULL,
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    `applied_at` DATETIME NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_bookmark` (`task_id`, `bookmark_id`),
    KEY `idx_task_id` (`task_id`),
    KEY `idx_bookmark_id` (`bookmark_id`),
    KEY `idx_status` (`status`),
    KEY `idx_task_folder` (`task_id`, `logical_folder_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI书签重分类结果';

CREATE TABLE IF NOT EXISTS `bookmark_ai_reclassification_snapshot` (
    `id` BIGINT NOT NULL,
    `task_id` BIGINT NOT NULL,
    `bookmark_id` BIGINT NOT NULL,
    `href` TEXT NULL,
    `original_title` TEXT NULL,
    `registrable_domain` VARCHAR(255) NULL,
    `ordinal` INT NOT NULL,
    `pool_type` VARCHAR(20) NULL COMMENT 'LARGE_DOMAIN/SMALL_POOL',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_bookmark` (`task_id`, `bookmark_id`),
    KEY `idx_task_domain` (`task_id`, `registrable_domain`),
    KEY `idx_task_pool_ordinal` (`task_id`, `pool_type`, `ordinal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='重分类书签快照';

CREATE TABLE IF NOT EXISTS `bookmark_ai_reclassification_domain_group` (
    `id` BIGINT NOT NULL,
    `task_id` BIGINT NOT NULL,
    `registrable_domain` VARCHAR(255) NOT NULL,
    `bookmark_count` INT NOT NULL,
    `group_type` VARCHAR(20) NOT NULL COMMENT 'LARGE_DOMAIN/SMALL_POOL',
    `status` VARCHAR(20) NOT NULL,
    `logical_folder_key` VARCHAR(128) NULL,
    `folder_name` VARCHAR(512) NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_domain` (`task_id`, `registrable_domain`),
    KEY `idx_task_type_status` (`task_id`, `group_type`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='重分类主域名分组';

CREATE TABLE IF NOT EXISTS `bookmark_ai_reclassification_work_unit` (
    `id` BIGINT NOT NULL,
    `task_id` BIGINT NOT NULL,
    `domain_group_id` BIGINT NULL,
    `unit_kind` VARCHAR(64) NOT NULL,
    `unit_key` VARCHAR(128) NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    `ordinal` INT NOT NULL DEFAULT 0,
    `attempt_count` INT NOT NULL DEFAULT 0,
    `prompt_version` VARCHAR(32) NOT NULL,
    `input_json` JSON NOT NULL,
    `request_json` JSON NULL,
    `raw_response` MEDIUMTEXT NULL,
    `parsed_output_json` JSON NULL,
    `error_message` TEXT NULL,
    `started_at` DATETIME NULL,
    `completed_at` DATETIME NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_unit_key` (`task_id`, `unit_key`),
    KEY `idx_task_status_ordinal` (`task_id`, `status`, `ordinal`),
    KEY `idx_group_kind` (`domain_group_id`, `unit_kind`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='重分类可恢复工作单元';

CREATE TABLE IF NOT EXISTS `bookmark_ai_reclassification_folder_application` (
    `id` BIGINT NOT NULL,
    `task_id` BIGINT NOT NULL,
    `logical_folder_key` VARCHAR(128) NOT NULL,
    `folder_name` VARCHAR(512) NOT NULL,
    `folder_id` BIGINT NULL,
    `source_phase` VARCHAR(48) NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    `applied_at` DATETIME NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_folder_key` (`task_id`, `logical_folder_key`),
    KEY `idx_task_status` (`task_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='重分类目录自动应用记录';

-- ============================================
-- 初始化完成
-- ============================================
SELECT '数据库初始化完成！' AS message;
SELECT CONCAT('共创建 ', COUNT(*), ' 张表') AS table_count
FROM information_schema.tables
WHERE table_schema = 'bookmarks';
