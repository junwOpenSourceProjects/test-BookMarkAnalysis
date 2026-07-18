CREATE TABLE IF NOT EXISTS bookmark_ai_classification_task (
  id BIGINT NOT NULL,
  strategy VARCHAR(32) NOT NULL,
  model_name VARCHAR(128) NOT NULL,
  status VARCHAR(20) NOT NULL,
  total_count INT NOT NULL DEFAULT 0,
  rule_matched_count INT NOT NULL DEFAULT 0,
  ai_matched_count INT NOT NULL DEFAULT 0,
  failed_count INT NOT NULL DEFAULT 0,
  started_at DATETIME NULL,
  completed_at DATETIME NULL,
  error_message TEXT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id), KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI书签分类任务';

CREATE TABLE IF NOT EXISTS bookmark_ai_classification_result (
  id BIGINT NOT NULL,
  task_id BIGINT NOT NULL,
  bookmark_id BIGINT NOT NULL,
  original_title TEXT NULL,
  suggested_title TEXT NULL,
  suggested_folder VARCHAR(512) NULL,
  target_folder_id BIGINT NULL,
  keywords JSON NULL,
  page_type VARCHAR(128) NULL,
  confidence INT NULL,
  reason TEXT NULL,
  source VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  applied_at DATETIME NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id), KEY idx_task_id (task_id), KEY idx_bookmark_id (bookmark_id), KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI书签分类结果';
