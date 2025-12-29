package com.yxb.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yxb.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户微信绑定信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_wechat")
public class UserWechat extends BaseEntity {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 微信OpenID（APP）
     */
    private String openIdApp;

    /**
     * 微信OpenID（小程序）
     */
    private String openIdMini;

    /**
     * 微信OpenID（公众号）
     */
    private String openIdMp;

    /**
     * 微信UnionID
     */
    private String unionId;

    /**
     * 微信昵称
     */
    private String nickname;

    /**
     * 微信头像
     */
    private String avatar;

    /**
     * 性别 0-未知 1-男 2-女
     */
    private Integer gender;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 语言
     */
    private String language;

    /**
     * 会话密钥（小程序）
     */
    private String sessionKey;
}
