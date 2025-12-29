package com.yxb.api.user.dto;

import lombok.Data;

/**
 * 管理员登录返回DTO
 */
@Data
public class AdminLoginDTO {

    private String token;

    private AdminDTO adminInfo;
}
