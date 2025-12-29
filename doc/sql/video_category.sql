-- 视频分类表
CREATE TABLE `video_category` (
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

-- 初始化分类数据
INSERT INTO `video_category` (`name`, `icon`, `parent_id`, `sort`) VALUES 
('The Daily Show', NULL, 0, 1),
('每日快讯', NULL, 0, 2),
('电影片段', NULL, 0, 3),
('TED演讲', NULL, 0, 4),
('纪录片', NULL, 0, 5);
