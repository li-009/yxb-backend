package com.yxb.api.user.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息DTO
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别 0-未知 1-男 2-女
     */
    private Integer gender;

    /**
     * 微信OpenID
     */
    private String openId;

    /**
     * 微信UnionID
     */
    private String unionId;

    /**
     * 用户状态 0-禁用 1-正常
     */
    private Integer status;

    /**
     * VIP等级 0-普通用户 1-VIP 2-SVIP
     */
    private Integer vipLevel;

    /**
     * VIP过期时间
     */
    private LocalDateTime vipExpireTime;

    /**
     * 学习语言代码
     */
    private String studyLanguage;

    /**
     * 语言水平 1-入门 2-初级 3-中级 4-高级
     */
    private Integer languageLevel;

    /**
     * 累计学习时长（分钟）
     */
    private Integer totalStudyMinutes;

    /**
     * 累计学习天数
     */
    private Integer totalStudyDays;

    /**
     * 积分
     */
    private Integer points;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;
}
