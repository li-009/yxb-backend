package com.yxb.api.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录响应DTO
 */
@Data
public class LoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 过期时间（秒）
     */
    private Long expiresIn;

    /**
     * 用户信息
     */
    private UserDTO user;

    /**
     * 是否新用户
     */
    private Boolean isNewUser;
}
