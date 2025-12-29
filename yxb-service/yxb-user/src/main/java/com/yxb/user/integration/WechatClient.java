package com.yxb.user.integration;

import com.yxb.user.integration.dto.WechatUserInfo;

/**
 * 微信接口客户端
 */
public interface WechatClient {

    /**
     * 通过授权码获取用户信息
     *
     * @param code     微信授权码
     * @param platform 平台 app/mini/mp
     * @return 微信用户信息
     */
    WechatUserInfo getUserInfo(String code, String platform);

    /**
     * 获取小程序AccessToken
     */
    String getMiniAccessToken();

    /**
     * 获取公众号AccessToken
     */
    String getMpAccessToken();
}
