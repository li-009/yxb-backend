-- ========================================
-- 鹦学伴 数据库初始化脚本
-- ========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `yxb_user`;
CREATE DATABASE IF NOT EXISTS `yxb_video`;
CREATE DATABASE IF NOT EXISTS `yxb_study`;

-- ========================================
-- yxb_user 库
-- ========================================
USE `yxb_user`;

-- 管理员表
CREATE TABLE IF NOT EXISTS `admin_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码(MD5加密)',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像',
  `role` tinyint NOT NULL DEFAULT '2' COMMENT '角色 1-超级管理员 2-运营 3-编辑',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态 0-禁用 1-正常',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除 0-否 1-是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) COMMENT='管理员表';

-- 初始化超级管理员 密码: admin123
INSERT INTO `admin_info` (`username`, `password`, `real_name`, `role`, `status`) 
VALUES ('admin', '0192023a7bbd73250516f069df18b500', '超级管理员', 1, 1)
ON DUPLICATE KEY UPDATE `username`=`username`;

-- 用户信息表
CREATE TABLE IF NOT EXISTS `user_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像',
  `gender` tinyint DEFAULT '0' COMMENT '性别 0-未知 1-男 2-女',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '用户状态 0-禁用 1-正常',
  `vip_level` tinyint NOT NULL DEFAULT '0' COMMENT 'VIP等级 0-普通用户 1-VIP 2-SVIP',
  `vip_expire_time` datetime DEFAULT NULL COMMENT 'VIP过期时间',
  `study_language` varchar(10) DEFAULT 'en' COMMENT '学习语言代码',
  `language_level` tinyint DEFAULT '1' COMMENT '语言水平 1-入门 2-初级 3-中级 4-高级',
  `total_study_minutes` int NOT NULL DEFAULT '0' COMMENT '累计学习时长(分钟)',
  `total_study_days` int NOT NULL DEFAULT '0' COMMENT '累计学习天数',
  `points` int NOT NULL DEFAULT '0' COMMENT '积分',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除 0-否 1-是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_status` (`status`),
  KEY `idx_vip_level` (`vip_level`)
) COMMENT='用户信息表';

-- ========================================
-- yxb_video 库
-- ========================================
USE `yxb_video`;

-- 视频分类表
CREATE TABLE IF NOT EXISTS `video_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `icon` varchar(500) DEFAULT NULL COMMENT '分类图标',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父分类ID 0表示顶级分类',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序号',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态 0-禁用 1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除 0-否 1-是',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) COMMENT='视频分类表';

-- 初始化分类
INSERT INTO `video_category` (`name`, `parent_id`, `sort`) VALUES 
('The Daily Show', 0, 1),
('每日快讯', 0, 2),
('电影片段', 0, 3),
('TED演讲', 0, 4),
('纪录片', 0, 5)
ON DUPLICATE KEY UPDATE `name`=`name`;

-- 视频信息表
CREATE TABLE IF NOT EXISTS `video_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(200) NOT NULL COMMENT '视频标题',
  `description` text COMMENT '视频描述',
  `cover_url` varchar(500) DEFAULT NULL COMMENT '封面图URL',
  `video_url` varchar(500) NOT NULL COMMENT '视频URL',
  `duration` int DEFAULT '0' COMMENT '视频时长(秒)',
  `format` varchar(20) DEFAULT NULL COMMENT '视频格式',
  `file_size` bigint DEFAULT '0' COMMENT '文件大小(字节)',
  `source` tinyint NOT NULL DEFAULT '1' COMMENT '来源 1-上传 2-YouTube 3-其他',
  `source_platform` varchar(50) DEFAULT NULL COMMENT '来源平台',
  `original_url` varchar(500) DEFAULT NULL COMMENT '原始URL',
  `language` varchar(10) NOT NULL DEFAULT 'en' COMMENT '语种代码',
  `level` tinyint NOT NULL DEFAULT '1' COMMENT '难度等级 1-入门 2-初级 3-中级 4-高级',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `tags` varchar(500) DEFAULT NULL COMMENT '标签(逗号分隔)',
  `play_count` int NOT NULL DEFAULT '0' COMMENT '播放次数',
  `collect_count` int NOT NULL DEFAULT '0' COMMENT '收藏次数',
  `has_subtitle` tinyint NOT NULL DEFAULT '0' COMMENT '是否有字幕 0-否 1-是',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态 0-下架 1-上架',
  `upload_user_id` bigint DEFAULT NULL COMMENT '上传用户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除 0-否 1-是',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_language` (`language`),
  KEY `idx_status` (`status`)
) COMMENT='视频信息表';

-- 视频字幕表
CREATE TABLE IF NOT EXISTS `video_subtitle` (
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

SELECT '初始化完成!' AS result;
