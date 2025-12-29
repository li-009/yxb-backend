package com.yxb.wechat.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 小程序登录请求
 */
@Data
@Schema(description = "小程序登录请求")
public class MiniappLoginRequest {

    @NotBlank(message = "登录凭证不能为空")
    @Schema(description = "wx.login获取的code")
    private String code;
}
