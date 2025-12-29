package com.yxb.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求
 */
@Data
@Schema(description = "登录请求")
public class LoginRequest {

    @Schema(description = "登录类型 password-密码登录 sms-短信登录 wechat-微信登录", example = "password")
    @NotBlank(message = "登录类型不能为空")
    private String loginType;

    @Schema(description = "用户名/手机号", example = "admin")
    private String username;

    @Schema(description = "密码", example = "123456")
    private String password;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "短信验证码", example = "123456")
    private String smsCode;

    @Schema(description = "微信授权码", example = "xxx")
    private String wechatCode;

    @Schema(description = "微信登录平台 app-APP mini-小程序 mp-公众号", example = "mini")
    private String wechatPlatform;
}
