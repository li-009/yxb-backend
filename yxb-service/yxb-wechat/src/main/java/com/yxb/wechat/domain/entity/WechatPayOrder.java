package com.yxb.wechat.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yxb.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 微信支付订单实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wechat_pay_order")
public class WechatPayOrder extends BaseEntity {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 支付金额(分)
     */
    private Integer totalFee;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 支付状态: 0-待支付 1-已支付 2-已关闭 3-已退款
     */
    private Integer payStatus;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;
}
