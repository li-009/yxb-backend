-- 视频字幕表
CREATE TABLE `video_subtitle` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `video_id` bigint NOT NULL COMMENT '视频ID',
  `language` varchar(10) NOT NULL COMMENT '字幕语言代码',
  `language_name` varchar(50) DEFAULT NULL COMMENT '语言名称',
  `subtitle_url` varchar(500) DEFAULT NULL COMMENT '字幕文件URL',
  `subtitle_content` mediumtext COMMENT '字幕内容(JSON格式)',
  `format` varchar(20) DEFAULT 'srt' COMMENT '字幕格式 srt/vtt/json',
  `is_ai_generated` tinyint NOT NULL DEFAULT '0' COMMENT '是否AI生成 0-否 1-是',
  `is_primary` tinyint NOT NULL DEFAULT '0' COMMENT '是否主字幕 0-否 1-是',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态 0-禁用 1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除 0-否 1-是',
  PRIMARY KEY (`id`),
  KEY `idx_video_id` (`video_id`),
  KEY `idx_language` (`language`)
) COMMENT='视频字幕表';
