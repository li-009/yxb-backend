package com.yxb.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxb.user.domain.entity.UserWechat;

/**
 * 用户微信绑定Service接口
 */
public interface UserWechatService extends IService<UserWechat> {

    /**
     * 根据UnionID查询
     */
    UserWechat getByUnionId(String unionId);

    /**
     * 根据APP OpenID查询
     */
    UserWechat getByOpenIdApp(String openId);

    /**
     * 根据小程序OpenID查询
     */
    UserWechat getByOpenIdMini(String openId);

    /**
     * 根据公众号OpenID查询
     */
    UserWechat getByOpenIdMp(String openId);

    /**
     * 根据用户ID查询
     */
    UserWechat getByUserId(Long userId);
}
