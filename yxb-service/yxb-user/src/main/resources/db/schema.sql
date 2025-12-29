-- 用户服务数据库初始化脚本

-- 用户信息表
CREATE TABLE IF NOT EXISTS `user_info` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) DEFAULT NULL COMMENT '密码',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像',
    `gender` TINYINT DEFAULT 0 COMMENT '性别 0-未知 1-男 2-女',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    `vip_level` TINYINT DEFAULT 0 COMMENT 'VIP等级 0-普通 1-VIP 2-SVIP',
    `vip_expire_time` DATETIME DEFAULT NULL COMMENT 'VIP过期时间',
    `study_language` VARCHAR(20) DEFAULT 'en-US' COMMENT '学习语言代码',
    `language_level` TINYINT DEFAULT 1 COMMENT '语言水平 1-入门 2-初级 3-中级 4-高级',
    `total_study_minutes` INT DEFAULT 0 COMMENT '累计学习时长(分钟)',
    `total_study_days` INT DEFAULT 0 COMMENT '累计学习天数',
    `points` INT DEFAULT 0 COMMENT '积分',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB COMMENT='用户信息表';

-- 用户微信绑定表
CREATE TABLE IF NOT EXISTS `user_wechat` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `open_id_app` VARCHAR(100) DEFAULT NULL COMMENT '微信OpenID(APP)',
    `open_id_mini` VARCHAR(100) DEFAULT NULL COMMENT '微信OpenID(小程序)',
    `open_id_mp` VARCHAR(100) DEFAULT NULL COMMENT '微信OpenID(公众号)',
    `union_id` VARCHAR(100) DEFAULT NULL COMMENT '微信UnionID',
    `nickname` VARCHAR(100) DEFAULT NULL COMMENT '微信昵称',
    `avatar` VARCHAR(500) DEFAULT NULL COMMENT '微信头像',
    `gender` TINYINT DEFAULT 0 COMMENT '性别',
    `country` VARCHAR(50) DEFAULT NULL COMMENT '国家',
    `province` VARCHAR(50) DEFAULT NULL COMMENT '省份',
    `city` VARCHAR(50) DEFAULT NULL COMMENT '城市',
    `language` VARCHAR(20) DEFAULT NULL COMMENT '语言',
    `session_key` VARCHAR(100) DEFAULT NULL COMMENT '会话密钥',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_union_id` (`union_id`),
    KEY `idx_open_id_app` (`open_id_app`),
    KEY `idx_open_id_mini` (`open_id_mini`),
    KEY `idx_open_id_mp` (`open_id_mp`)
) ENGINE=InnoDB COMMENT='用户微信绑定表';

-- 用户VIP记录表
CREATE TABLE IF NOT EXISTS `user_vip` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `vip_level` TINYINT NOT NULL COMMENT 'VIP等级 1-VIP 2-SVIP',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `source` TINYINT DEFAULT 1 COMMENT '来源 1-购买 2-赠送 3-兑换',
    `order_id` BIGINT DEFAULT NULL COMMENT '订单ID',
    `pay_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '支付金额',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0-已过期 1-生效中',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_end_time` (`end_time`)
) ENGINE=InnoDB COMMENT='用户VIP记录表';
