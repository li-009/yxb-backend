-- 学习服务数据库初始化脚本

CREATE TABLE IF NOT EXISTS `study_progress` (
    `id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `video_id` BIGINT NOT NULL,
    `current_position` INT DEFAULT 0 COMMENT '当前播放位置(秒)',
    `watched_duration` INT DEFAULT 0 COMMENT '已观看时长(秒)',
    `completion_percent` INT DEFAULT 0 COMMENT '完成百分比',
    `completed` TINYINT DEFAULT 0,
    `read_along_count` INT DEFAULT 0 COMMENT '跟读次数',
    `avg_read_along_score` INT DEFAULT 0 COMMENT '平均跟读分数',
    `last_study_time` DATETIME,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_by` BIGINT,
    `update_by` BIGINT,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_video` (`user_id`, `video_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB COMMENT='学习进度表';

CREATE TABLE IF NOT EXISTS `study_note` (
    `id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `video_id` BIGINT NOT NULL,
    `timestamp` INT DEFAULT 0 COMMENT '时间戳(秒)',
    `content` TEXT COMMENT '笔记内容',
    `subtitle_text` VARCHAR(500) COMMENT '关联字幕',
    `category` VARCHAR(50) COMMENT '分类',
    `tags` VARCHAR(200) COMMENT '标签',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_by` BIGINT,
    `update_by` BIGINT,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_user_video` (`user_id`, `video_id`)
) ENGINE=InnoDB COMMENT='学习笔记表';

CREATE TABLE IF NOT EXISTS `study_word_book` (
    `id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `word` VARCHAR(100) NOT NULL,
    `phonetic` VARCHAR(100),
    `meaning` VARCHAR(500),
    `example` VARCHAR(500),
    `example_translation` VARCHAR(500),
    `language` VARCHAR(20) DEFAULT 'en-US',
    `source_video_id` BIGINT,
    `mastery_status` TINYINT DEFAULT 0 COMMENT '0-未掌握 1-学习中 2-已掌握',
    `review_count` INT DEFAULT 0,
    `next_review_time` DATETIME,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_by` BIGINT,
    `update_by` BIGINT,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_word` (`user_id`, `word`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_next_review` (`next_review_time`)
) ENGINE=InnoDB COMMENT='单词本表';

CREATE TABLE IF NOT EXISTS `study_user_collect` (
    `id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `video_id` BIGINT NOT NULL,
    `collect_type` TINYINT DEFAULT 1,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_by` BIGINT,
    `update_by` BIGINT,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_video` (`user_id`, `video_id`)
) ENGINE=InnoDB COMMENT='用户收藏表';
