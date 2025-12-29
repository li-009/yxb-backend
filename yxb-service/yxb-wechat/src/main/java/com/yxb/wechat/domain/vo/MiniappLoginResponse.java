package com.yxb.wechat.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 小程序登录响应
 */
@Data
@Schema(description = "小程序登录响应")
public class MiniappLoginResponse {

    @Schema(description = "用户唯一标识")
    private String openid;

    @Schema(description = "用户在开放平台的唯一标识")
    private String unionid;

    @Schema(description = "会话密钥")
    private String sessionKey;
}
