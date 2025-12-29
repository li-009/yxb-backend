package com.yxb.wechat.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * 支付订单请求
 */
@Data
@Schema(description = "支付订单请求")
public class PayOrderRequest {

    @NotBlank(message = "订单号不能为空")
    @Schema(description = "商户订单号")
    private String orderNo;

    @NotNull(message = "金额不能为空")
    @Positive(message = "金额必须大于0")
    @Schema(description = "支付金额(单位:分)")
    private Integer amount;

    @NotBlank(message = "商品描述不能为空")
    @Schema(description = "商品描述")
    private String description;

    @Schema(description = "客户端IP")
    private String clientIp;
}
