-- ====================================
-- 鹦学伴 数据库设计
-- 数据库名: yxb_main
-- 版本: V1.0
-- 日期: 2025-12-22
-- ====================================

-- ====================================
-- 1. 用户模块
-- ====================================

-- 用户基本信息表
CREATE TABLE `yxb_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `open_id` VARCHAR(128) DEFAULT NULL COMMENT '微信OpenID',
  `union_id` VARCHAR(128) DEFAULT NULL COMMENT '微信UnionID',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `nickname` VARCHAR(100) DEFAULT NULL COMMENT '昵称',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
  `language_level` VARCHAR(50) DEFAULT NULL COMMENT '语言水平',
  `learning_languages` VARCHAR(200) DEFAULT NULL COMMENT '学习的语言列表',
  `total_learning_days` INT DEFAULT 0 COMMENT '累计学习天数',
  `total_learning_minutes` INT DEFAULT 0 COMMENT '累计学习时长',
  `total_words_count` INT DEFAULT 0 COMMENT '掌握单词数',
  `total_follow_count` INT DEFAULT 0 COMMENT '跟读次数',
  `vip_status` TINYINT DEFAULT 0 COMMENT 'VIP状态',
  `vip_expire_time` DATETIME DEFAULT NULL COMMENT 'VIP过期时间',
  `status` TINYINT DEFAULT 1 COMMENT '状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_open_id` (`open_id`),
  KEY `idx_phone` (`phone`),
  KEY `idx_union_id` (`union_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户基本信息表';

-- ====================================
-- 2. 视频模块
-- ====================================

-- 视频信息表
CREATE TABLE `yxb_video` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '视频ID',
  `user_id` BIGINT NOT NULL COMMENT '上传用户ID',
  `title` VARCHAR(200) NOT NULL COMMENT '视频标题',
  `description` TEXT COMMENT '视频描述',
  `cover_url` VARCHAR(500) DEFAULT NULL COMMENT '封面图URL',
  `video_url` VARCHAR(500) NOT NULL COMMENT '视频文件URL',
  `video_format` VARCHAR(20) DEFAULT NULL COMMENT '视频格式',
  `video_size` BIGINT DEFAULT NULL COMMENT '视频大小',
  `duration` INT DEFAULT NULL COMMENT '视频时长',
  `language` VARCHAR(50) DEFAULT NULL COMMENT '视频语言',
  `difficulty` VARCHAR(20) DEFAULT NULL COMMENT '难度等级',
  `category` VARCHAR(50) DEFAULT NULL COMMENT '分类',
  `source_type` VARCHAR(20) DEFAULT NULL COMMENT '来源类型',
  `source_url` VARCHAR(500) DEFAULT NULL COMMENT '原始来源URL',
  `view_count` INT DEFAULT 0 COMMENT '播放次数',
  `like_count` INT DEFAULT 0 COMMENT '点赞数',
  `collect_count` INT DEFAULT 0 COMMENT '收藏数',
  `status` TINYINT DEFAULT 1 COMMENT '状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_language` (`language`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频信息表';

-- 视频下载记录表
CREATE TABLE `yxb_video_download` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '下载ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `video_id` BIGINT NOT NULL COMMENT '视频ID',
  `download_url` VARCHAR(500) DEFAULT NULL COMMENT '下载地址',
  `download_quality` VARCHAR(20) DEFAULT NULL COMMENT '下载清晰度',
  `file_size` BIGINT DEFAULT NULL COMMENT '文件大小',
  `download_status` TINYINT DEFAULT 0 COMMENT '下载状态',
  `download_progress` INT DEFAULT 0 COMMENT '下载进度',
  `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_video_id` (`video_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频下载记录表';

-- ====================================
-- 3. 字幕模块
-- ====================================

-- 字幕文件表
CREATE TABLE `yxb_subtitle` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字幕ID',
  `video_id` BIGINT NOT NULL COMMENT '视频ID',
  `language` VARCHAR(50) NOT NULL COMMENT '字幕语言',
  `subtitle_type` VARCHAR(20) DEFAULT NULL COMMENT '字幕类型',
  `subtitle_url` VARCHAR(500) DEFAULT NULL COMMENT '字幕文件URL',
  `subtitle_format` VARCHAR(20) DEFAULT NULL COMMENT '字幕格式',
  `is_default` TINYINT DEFAULT 0 COMMENT '是否默认字幕',
  `generate_by_ai` TINYINT DEFAULT 0 COMMENT '是否AI生成',
  `status` TINYINT DEFAULT 1 COMMENT '状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_video_id` (`video_id`),
  KEY `idx_language` (`language`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字幕文件表';

-- 字幕内容表
CREATE TABLE `yxb_subtitle_content` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '内容ID',
  `subtitle_id` BIGINT NOT NULL COMMENT '字幕ID',
  `start_time` DECIMAL(10, 3) NOT NULL COMMENT '开始时间',
  `end_time` DECIMAL(10, 3) NOT NULL COMMENT '结束时间',
  `content` TEXT NOT NULL COMMENT '字幕内容',
  `translation` TEXT DEFAULT NULL COMMENT '翻译内容',
  `seq_number` INT DEFAULT NULL COMMENT '序号',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_subtitle_id` (`subtitle_id`),
  KEY `idx_time` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字幕内容表';

-- ====================================
-- 4. 学习记录模块
-- ====================================

-- 学习进度表
CREATE TABLE `yxb_learning_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `video_id` BIGINT NOT NULL COMMENT '视频ID',
  `current_position` INT DEFAULT 0 COMMENT '当前播放位置',
  `total_duration` INT DEFAULT 0 COMMENT '视频总时长',
  `play_count` INT DEFAULT 0 COMMENT '播放次数',
  `last_play_time` DATETIME DEFAULT NULL COMMENT '最后播放时间',
  `is_completed` TINYINT DEFAULT 0 COMMENT '是否完成',
  `completion_rate` DECIMAL(5, 2) DEFAULT 0.00 COMMENT '完成度',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_video` (`user_id`, `video_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习进度表';

-- 跟读记录表
CREATE TABLE `yxb_follow_reading` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `video_id` BIGINT NOT NULL COMMENT '视频ID',
  `subtitle_id` BIGINT DEFAULT NULL COMMENT '字幕ID',
  `reference_text` TEXT NOT NULL COMMENT '参考文本',
  `audio_url` VARCHAR(500) DEFAULT NULL COMMENT '录音URL',
  `audio_duration` INT DEFAULT NULL COMMENT '录音时长',
  `overall_score` DECIMAL(5, 2) DEFAULT NULL COMMENT '总分',
  `pronunciation_score` DECIMAL(5, 2) DEFAULT NULL COMMENT '发音得分',
  `fluency_score` DECIMAL(5, 2) DEFAULT NULL COMMENT '流利度得分',
  `integrity_score` DECIMAL(5, 2) DEFAULT NULL COMMENT '完整度得分',
  `word_scores` TEXT DEFAULT NULL COMMENT '单词级评分',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_video_id` (`video_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='跟读记录表';

-- 笔记表
CREATE TABLE `yxb_note` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '笔记ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `video_id` BIGINT NOT NULL COMMENT '视频ID',
  `timestamp` INT DEFAULT NULL COMMENT '视频时间点',
  `content` TEXT NOT NULL COMMENT '笔记内容',
  `screenshot_url` VARCHAR(500) DEFAULT NULL COMMENT '截图URL',
  `tags` VARCHAR(200) DEFAULT NULL COMMENT '标签',
  `is_public` TINYINT DEFAULT 0 COMMENT '是否公开',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_video_id` (`video_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='笔记表';

-- ====================================
-- 5. 单词本模块
-- ====================================

-- 单词本表
CREATE TABLE `yxb_vocabulary` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '单词ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `word` VARCHAR(100) NOT NULL COMMENT '单词',
  `language` VARCHAR(50) NOT NULL COMMENT '语言',
  `definition` TEXT DEFAULT NULL COMMENT '释义',
  `phonetic` VARCHAR(100) DEFAULT NULL COMMENT '音标',
  `example_sentence` TEXT DEFAULT NULL COMMENT '例句',
  `video_id` BIGINT DEFAULT NULL COMMENT '来源视频ID',
  `timestamp` INT DEFAULT NULL COMMENT '视频时间点',
  `mastery_level` TINYINT DEFAULT 0 COMMENT '掌握程度',
  `review_count` INT DEFAULT 0 COMMENT '复习次数',
  `next_review_time` DATETIME DEFAULT NULL COMMENT '下次复习时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_word` (`word`),
  KEY `idx_next_review` (`user_id`, `next_review_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单词本表';

-- 单词复习记录表
CREATE TABLE `yxb_vocabulary_review` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `vocabulary_id` BIGINT NOT NULL COMMENT '单词ID',
  `review_result` TINYINT DEFAULT NULL COMMENT '复习结果',
  `review_duration` INT DEFAULT NULL COMMENT '复习时长',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_vocabulary_id` (`vocabulary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单词复习记录表';

-- ====================================
-- 6. AI辅助功能
-- ====================================

-- 语法讲解表
CREATE TABLE `yxb_grammar_explanation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '讲解ID',
  `sentence` TEXT NOT NULL COMMENT '句子',
  `language` VARCHAR(50) NOT NULL COMMENT '语言',
  `sentence_hash` VARCHAR(64) NOT NULL COMMENT '句子哈希',
  `grammar_points` TEXT DEFAULT NULL COMMENT '语法点JSON',
  `sentence_structure` TEXT DEFAULT NULL COMMENT '句子结构分析',
  `difficulty_notes` TEXT DEFAULT NULL COMMENT '易错点',
  `ai_model` VARCHAR(50) DEFAULT NULL COMMENT 'AI模型',
  `cache_hit_count` INT DEFAULT 0 COMMENT '缓存命中次数',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sentence_hash` (`sentence_hash`),
  KEY `idx_language` (`language`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='语法讲解表';

-- AI问答记录表
CREATE TABLE `yxb_ai_question` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '问答ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `session_id` VARCHAR(64) DEFAULT NULL COMMENT '会话ID',
  `question` TEXT NOT NULL COMMENT '问题',
  `answer` TEXT DEFAULT NULL COMMENT '回答',
  `context` TEXT DEFAULT NULL COMMENT '上下文',
  `ai_model` VARCHAR(50) DEFAULT NULL COMMENT 'AI模型',
  `tokens_used` INT DEFAULT NULL COMMENT '消耗Token数',
  `response_time` INT DEFAULT NULL COMMENT '响应时间',
  `satisfaction` TINYINT DEFAULT NULL COMMENT '满意度评分',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI问答记录表';

-- ====================================
-- 7. 运营模块
-- ====================================

-- 打卡记录表
CREATE TABLE `yxb_checkin` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '打卡ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `checkin_date` DATE NOT NULL COMMENT '打卡日期',
  `learning_minutes` INT DEFAULT 0 COMMENT '学习时长',
  `word_count` INT DEFAULT 0 COMMENT '新增单词数',
  `follow_count` INT DEFAULT 0 COMMENT '跟读次数',
  `points_earned` INT DEFAULT 0 COMMENT '获得积分',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_date` (`user_id`, `checkin_date`),
  KEY `idx_checkin_date` (`checkin_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打卡记录表';

-- 积分记录表
CREATE TABLE `yxb_points` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '积分ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `points_change` INT NOT NULL COMMENT '积分变化',
  `points_balance` INT DEFAULT 0 COMMENT '积分余额',
  `change_type` VARCHAR(50) NOT NULL COMMENT '变化类型',
  `change_reason` VARCHAR(200) DEFAULT NULL COMMENT '变化原因',
  `ref_id` BIGINT DEFAULT NULL COMMENT '关联业务ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_change_type` (`change_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分记录表';

-- 每日推荐表
CREATE TABLE `yxb_daily_recommendation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '推荐ID',
  `recommend_date` DATE NOT NULL COMMENT '推荐日期',
  `video_id` BIGINT NOT NULL COMMENT '视频ID',
  `language` VARCHAR(50) DEFAULT NULL COMMENT '语言',
  `difficulty` VARCHAR(20) DEFAULT NULL COMMENT '难度',
  `recommend_reason` TEXT DEFAULT NULL COMMENT '推荐理由',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_date` (`recommend_date`),
  KEY `idx_video_id` (`video_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日推荐表';

-- ====================================
-- 8. 微信生态模块
-- ====================================

-- 微信分享记录表
CREATE TABLE `yxb_wechat_share` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分享ID',
  `user_id` BIGINT NOT NULL COMMENT '分享用户ID',
  `share_type` VARCHAR(50) NOT NULL COMMENT '分享类型',
  `share_content_id` BIGINT DEFAULT NULL COMMENT '分享内容ID',
  `share_title` VARCHAR(200) DEFAULT NULL COMMENT '分享标题',
  `share_scene` VARCHAR(50) DEFAULT NULL COMMENT '分享场景',
  `invitee_user_id` BIGINT DEFAULT NULL COMMENT '被邀请用户ID',
  `reward_points` INT DEFAULT 0 COMMENT '奖励积分',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_invitee` (`invitee_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信分享记录表';

-- 学习社群表
CREATE TABLE `yxb_study_group` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '社群ID',
  `group_name` VARCHAR(100) NOT NULL COMMENT '社群名称',
  `group_desc` TEXT DEFAULT NULL COMMENT '社群描述',
  `group_avatar` VARCHAR(500) DEFAULT NULL COMMENT '社群头像',
  `language` VARCHAR(50) DEFAULT NULL COMMENT '学习语言',
  `difficulty` VARCHAR(20) DEFAULT NULL COMMENT '难度级别',
  `creator_id` BIGINT NOT NULL COMMENT '创建者ID',
  `member_count` INT DEFAULT 0 COMMENT '成员数量',
  `max_member` INT DEFAULT 500 COMMENT '最大成员数',
  `status` TINYINT DEFAULT 1 COMMENT '状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_language` (`language`),
  KEY `idx_creator` (`creator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习社群表';

-- 社群成员表
CREATE TABLE `yxb_study_group_member` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '成员ID',
  `group_id` BIGINT NOT NULL COMMENT '社群ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role` VARCHAR(20) DEFAULT 'member' COMMENT '角色',
  `join_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  `status` TINYINT DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_user` (`group_id`, `user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社群成员表';

-- 学习任务表
CREATE TABLE `yxb_study_task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `task_name` VARCHAR(100) NOT NULL COMMENT '任务名称',
  `task_desc` TEXT DEFAULT NULL COMMENT '任务描述',
  `task_type` VARCHAR(50) NOT NULL COMMENT '任务类型',
  `target_value` INT DEFAULT NULL COMMENT '目标值',
  `reward_points` INT DEFAULT 0 COMMENT '奖励积分',
  `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `qrcode_url` VARCHAR(500) DEFAULT NULL COMMENT '二维码URL',
  `status` TINYINT DEFAULT 1 COMMENT '状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_task_type` (`task_type`),
  KEY `idx_time` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习任务表';

-- 任务完成记录表
CREATE TABLE `yxb_task_completion` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `task_id` BIGINT NOT NULL COMMENT '任务ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `current_value` INT DEFAULT 0 COMMENT '当前完成值',
  `is_completed` TINYINT DEFAULT 0 COMMENT '是否完成',
  `completion_time` DATETIME DEFAULT NULL COMMENT '完成时间',
  `reward_received` TINYINT DEFAULT 0 COMMENT '是否领取奖励',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_user` (`task_id`, `user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务完成记录表';

-- ====================================
-- 9. 在线资源库模块
-- ====================================

-- 资源分类表
CREATE TABLE `yxb_resource_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父分类ID',
  `category_name` VARCHAR(100) NOT NULL COMMENT '分类名称',
  `category_desc` TEXT DEFAULT NULL COMMENT '分类描述',
  `category_icon` VARCHAR(500) DEFAULT NULL COMMENT '分类图标',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源分类表';

-- 在线资源表
CREATE TABLE `yxb_online_resource` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `resource_name` VARCHAR(200) NOT NULL COMMENT '资源名称',
  `resource_desc` TEXT DEFAULT NULL COMMENT '资源描述',
  `resource_url` VARCHAR(500) NOT NULL COMMENT '资源URL',
  `resource_type` VARCHAR(50) DEFAULT NULL COMMENT '资源类型',
  `language` VARCHAR(50) DEFAULT NULL COMMENT '语言',
  `difficulty` VARCHAR(20) DEFAULT NULL COMMENT '难度',
  `tags` VARCHAR(200) DEFAULT NULL COMMENT '标签',
  `view_count` INT DEFAULT 0 COMMENT '浏览次数',
  `rating` DECIMAL(3, 2) DEFAULT 0.00 COMMENT '评分',
  `is_free` TINYINT DEFAULT 1 COMMENT '是否免费',
  `price` DECIMAL(10, 2) DEFAULT 0.00 COMMENT '价格',
  `status` TINYINT DEFAULT 1 COMMENT '状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_language` (`language`),
  KEY `idx_difficulty` (`difficulty`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='在线资源表';

-- ====================================
-- 10. 系统配置模块
-- ====================================

-- 系统配置表
CREATE TABLE `yxb_system_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
  `config_value` TEXT DEFAULT NULL COMMENT '配置值',
  `config_type` VARCHAR(50) DEFAULT NULL COMMENT '配置类型',
  `config_desc` VARCHAR(200) DEFAULT NULL COMMENT '配置描述',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';
