package com.yxb.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yxb.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户VIP信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_vip")
public class UserVip extends BaseEntity {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * VIP等级 1-VIP 2-SVIP
     */
    private Integer vipLevel;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 来源 1-购买 2-赠送 3-兑换
     */
    private Integer source;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 状态 0-已过期 1-生效中
     */
    private Integer status;
}
