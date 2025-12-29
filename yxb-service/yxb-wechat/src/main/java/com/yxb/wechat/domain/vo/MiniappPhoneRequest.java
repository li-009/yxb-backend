package com.yxb.wechat.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 获取手机号请求
 */
@Data
@Schema(description = "获取手机号请求")
public class MiniappPhoneRequest {

    @NotBlank(message = "code不能为空")
    @Schema(description = "手机号获取凭证")
    private String code;
}
