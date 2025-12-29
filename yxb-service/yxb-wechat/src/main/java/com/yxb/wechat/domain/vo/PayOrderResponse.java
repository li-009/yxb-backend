package com.yxb.wechat.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 支付订单响应
 */
@Data
@Schema(description = "支付订单响应")
public class PayOrderResponse {

    @Schema(description = "小程序ID")
    private String appId;

    @Schema(description = "时间戳")
    private String timeStamp;

    @Schema(description = "随机字符串")
    private String nonceStr;

    @Schema(description = "统一下单接口返回的prepay_id参数值")
    private String packageValue;

    @Schema(description = "签名类型")
    private String signType;

    @Schema(description = "签名")
    private String paySign;
}
