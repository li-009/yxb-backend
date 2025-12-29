-- 微信支付订单表
CREATE TABLE IF NOT EXISTS `wechat_pay_order` (
    `id` bigint NOT NULL COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `out_trade_no` varchar(64) NOT NULL COMMENT '商户订单号',
    `transaction_id` varchar(64) DEFAULT NULL COMMENT '微信支付订单号',
    `total_fee` int NOT NULL COMMENT '支付金额(分)',
    `body` varchar(256) NOT NULL COMMENT '商品描述',
    `pay_status` tinyint NOT NULL DEFAULT 0 COMMENT '支付状态: 0-待支付 1-已支付 2-已关闭 3-已退款',
    `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记: 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_out_trade_no` (`out_trade_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pay_status` (`pay_status`)
) COMMENT='微信支付订单表';
