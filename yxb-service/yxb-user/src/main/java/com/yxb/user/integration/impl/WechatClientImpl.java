package com.yxb.user.integration.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yxb.common.core.constant.CacheConstant;
import com.yxb.common.core.exception.BizException;
import com.yxb.common.core.result.ResultCode;
import com.yxb.common.redis.service.RedisService;
import com.yxb.user.integration.WechatClient;
import com.yxb.user.integration.dto.WechatUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 微信接口客户端实现
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WechatClientImpl implements WechatClient {

    private final RedisService redisService;

    @Value("${wechat.app.appId:}")
    private String appAppId;

    @Value("${wechat.app.appSecret:}")
    private String appAppSecret;

    @Value("${wechat.mini.appId:}")
    private String miniAppId;

    @Value("${wechat.mini.appSecret:}")
    private String miniAppSecret;

    @Value("${wechat.mp.appId:}")
    private String mpAppId;

    @Value("${wechat.mp.appSecret:}")
    private String mpAppSecret;

    private static final String MINI_CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    private static final String MP_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static final String MP_USER_INFO_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    @Override
    public WechatUserInfo getUserInfo(String code, String platform) {
        return switch (platform) {
            case "app" -> getAppUserInfo(code);
            case "mini" -> getMiniUserInfo(code);
            case "mp" -> getMpUserInfo(code);
            default -> throw new BizException("不支持的微信登录平台");
        };
    }

    /**
     * APP登录获取用户信息
     */
    private WechatUserInfo getAppUserInfo(String code) {
        // APP登录通常使用微信SDK，这里简化处理
        // 实际需要通过access_token获取用户信息
        log.info("APP微信登录, code: {}", code);
        throw new BizException("APP微信登录暂未实现");
    }

    /**
     * 小程序登录获取用户信息
     */
    private WechatUserInfo getMiniUserInfo(String code) {
        String url = String.format(MINI_CODE2SESSION_URL, miniAppId, miniAppSecret, code);
        String response = HttpUtil.get(url);
        log.info("小程序code2session响应: {}", response);

        JSONObject json = JSONUtil.parseObj(response);
        if (json.containsKey("errcode") && json.getInt("errcode") != 0) {
            log.error("小程序登录失败: {}", json.getStr("errmsg"));
            throw new BizException(ResultCode.WECHAT_AUTH_ERROR, json.getStr("errmsg"));
        }

        WechatUserInfo userInfo = new WechatUserInfo();
        userInfo.setOpenId(json.getStr("openid"));
        userInfo.setUnionId(json.getStr("unionid"));
        userInfo.setSessionKey(json.getStr("session_key"));
        
        return userInfo;
    }

    /**
     * 公众号登录获取用户信息
     */
    private WechatUserInfo getMpUserInfo(String code) {
        String url = String.format(MP_USER_INFO_URL, mpAppId, mpAppSecret, code);
        String response = HttpUtil.get(url);
        log.info("公众号OAuth响应: {}", response);

        JSONObject json = JSONUtil.parseObj(response);
        if (json.containsKey("errcode") && json.getInt("errcode") != 0) {
            log.error("公众号登录失败: {}", json.getStr("errmsg"));
            throw new BizException(ResultCode.WECHAT_AUTH_ERROR, json.getStr("errmsg"));
        }

        WechatUserInfo userInfo = new WechatUserInfo();
        userInfo.setOpenId(json.getStr("openid"));
        userInfo.setUnionId(json.getStr("unionid"));
        
        return userInfo;
    }

    @Override
    public String getMiniAccessToken() {
        String cacheKey = CacheConstant.PREFIX + "wechat:mini:access_token";
        String token = redisService.get(cacheKey);
        if (token != null) {
            return token;
        }

        String url = String.format(MP_ACCESS_TOKEN_URL, miniAppId, miniAppSecret);
        String response = HttpUtil.get(url);
        JSONObject json = JSONUtil.parseObj(response);

        if (json.containsKey("access_token")) {
            token = json.getStr("access_token");
            int expiresIn = json.getInt("expires_in");
            redisService.set(cacheKey, token, expiresIn - 200);
            return token;
        }

        throw new BizException("获取小程序AccessToken失败");
    }

    @Override
    public String getMpAccessToken() {
        String cacheKey = CacheConstant.PREFIX + "wechat:mp:access_token";
        String token = redisService.get(cacheKey);
        if (token != null) {
            return token;
        }

        String url = String.format(MP_ACCESS_TOKEN_URL, mpAppId, mpAppSecret);
        String response = HttpUtil.get(url);
        JSONObject json = JSONUtil.parseObj(response);

        if (json.containsKey("access_token")) {
            token = json.getStr("access_token");
            int expiresIn = json.getInt("expires_in");
            redisService.set(cacheKey, token, expiresIn - 200);
            return token;
        }

        throw new BizException("获取公众号AccessToken失败");
    }
}
