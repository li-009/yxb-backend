package com.yxb.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yxb.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_info")
public class User extends BaseEntity {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

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
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;
}
