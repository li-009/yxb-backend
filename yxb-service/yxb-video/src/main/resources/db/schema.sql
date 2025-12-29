-- 视频服务数据库初始化脚本

CREATE TABLE IF NOT EXISTS `video_info` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `description` TEXT COMMENT '描述',
    `cover_url` VARCHAR(500) COMMENT '封面URL',
    `video_url` VARCHAR(500) NOT NULL COMMENT '视频URL',
    `duration` INT DEFAULT 0 COMMENT '时长(秒)',
    `format` VARCHAR(20) COMMENT '格式',
    `file_size` BIGINT DEFAULT 0 COMMENT '文件大小',
    `source` TINYINT DEFAULT 1 COMMENT '来源 1-本地 2-在线 3-第三方',
    `source_platform` VARCHAR(50) COMMENT '来源平台',
    `original_url` VARCHAR(500) COMMENT '原始链接',
    `language` VARCHAR(20) DEFAULT 'en-US' COMMENT '语言',
    `level` TINYINT DEFAULT 1 COMMENT '难度 1-4',
    `category_id` BIGINT COMMENT '分类ID',
    `tags` VARCHAR(500) COMMENT '标签',
    `play_count` INT DEFAULT 0 COMMENT '播放次数',
    `collect_count` INT DEFAULT 0 COMMENT '收藏次数',
    `has_subtitle` TINYINT DEFAULT 0 COMMENT '是否有字幕',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0-待审核 1-正常 2-下架',
    `upload_user_id` BIGINT COMMENT '上传用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_by` BIGINT,
    `update_by` BIGINT,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_language` (`language`),
    KEY `idx_category` (`category_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB COMMENT='视频信息表';

CREATE TABLE IF NOT EXISTS `video_subtitle` (
    `id` BIGINT NOT NULL,
    `video_id` BIGINT NOT NULL COMMENT '视频ID',
    `language` VARCHAR(20) NOT NULL COMMENT '语言',
    `format` VARCHAR(20) DEFAULT 'srt' COMMENT '格式',
    `source` TINYINT DEFAULT 1 COMMENT '来源 1-内嵌 2-外部 3-AI生成',
    `file_url` VARCHAR(500) COMMENT '文件URL',
    `content` MEDIUMTEXT COMMENT '字幕内容',
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_by` BIGINT,
    `update_by` BIGINT,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_video_id` (`video_id`),
    KEY `idx_language` (`language`)
) ENGINE=InnoDB COMMENT='视频字幕表';

CREATE TABLE IF NOT EXISTS `video_category` (
    `id` BIGINT NOT NULL,
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    `icon` VARCHAR(200) COMMENT '图标',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父级ID',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `create_by` BIGINT,
    `update_by` BIGINT,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='视频分类表';
