-- Existing databases must have run 20260718_ai_classification_persistence.sql first.
-- This is a one-time forward migration. Do not re-run it after the columns/indexes exist.

ALTER TABLE bookmark_ai_classification_task
  ADD COLUMN phase VARCHAR(48) NOT NULL DEFAULT 'PREPARING' AFTER status,
  ADD COLUMN api_base_url VARCHAR(512) NULL AFTER model_name,
  ADD COLUMN tree_cleared_at DATETIME NULL AFTER completed_at,
  ADD COLUMN paused_at DATETIME NULL AFTER tree_cleared_at,
  ADD COLUMN resumed_at DATETIME NULL AFTER paused_at,
  ADD COLUMN recovery_count INT NOT NULL DEFAULT 0 AFTER failed_count,
  ADD COLUMN total_work_unit_count INT NOT NULL DEFAULT 0 AFTER recovery_count,
  ADD COLUMN completed_work_unit_count INT NOT NULL DEFAULT 0 AFTER total_work_unit_count,
  ADD COLUMN created_folder_count INT NOT NULL DEFAULT 0 AFTER completed_work_unit_count,
  ADD COLUMN moved_bookmark_count INT NOT NULL DEFAULT 0 AFTER created_folder_count,
  ADD COLUMN updated_title_count INT NOT NULL DEFAULT 0 AFTER moved_bookmark_count,
  ADD KEY idx_phase_status (phase, status);

ALTER TABLE bookmark_ai_classification_result
  ADD COLUMN domain_group_id BIGINT NULL AFTER task_id,
  ADD COLUMN logical_folder_key VARCHAR(128) NULL AFTER suggested_folder,
  ADD COLUMN topic_hint VARCHAR(256) NULL AFTER page_type,
  ADD COLUMN analysis_json JSON NULL AFTER reason,
  ADD UNIQUE KEY uk_task_bookmark (task_id, bookmark_id),
  ADD KEY idx_task_folder (task_id, logical_folder_key);

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
