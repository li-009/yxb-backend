package com.yxb.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户信息更新请求
 */
@Data
@Schema(description = "用户信息更新请求")
public class UserUpdateRequest {

    @Schema(description = "昵称", example = "小明")
    private String nickname;

    @Schema(description = "头像URL", example = "https://xxx.com/avatar.jpg")
    private String avatar;

    @Schema(description = "性别 0-未知 1-男 2-女", example = "1")
    private Integer gender;

    @Schema(description = "学习语言代码", example = "en-US")
    private String studyLanguage;

    @Schema(description = "语言水平 1-入门 2-初级 3-中级 4-高级", example = "2")
    private Integer languageLevel;
}
