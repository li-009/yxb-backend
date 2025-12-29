package com.yxb.api.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员信息DTO
 */
@Data
public class AdminDTO {

    private Long id;

    private String username;

    private String realName;

    private String phone;

    private String email;

    private String avatar;

    /**
     * 角色 1-超级管理员 2-运营 3-编辑
     */
    private Integer role;

    private Integer status;

    private LocalDateTime lastLoginTime;
}
