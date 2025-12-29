package com.yxb.user.strategy.login;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.yxb.common.core.util.AssertUtil;
import com.yxb.user.domain.entity.User;
import com.yxb.user.domain.entity.UserWechat;
import com.yxb.user.domain.vo.LoginRequest;
import com.yxb.user.integration.WechatClient;
import com.yxb.user.integration.dto.WechatUserInfo;
import com.yxb.user.service.UserService;
import com.yxb.user.service.UserWechatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 微信登录策略
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WechatLoginStrategy implements LoginStrategy {

    private final UserService userService;
    private final UserWechatService userWechatService;
    private final WechatClient wechatClient;

    @Override
    public String getLoginType() {
        return "wechat";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User login(LoginRequest request) {
        AssertUtil.notBlank(request.getWechatCode(), "微信授权码不能为空");
        AssertUtil.notBlank(request.getWechatPlatform(), "登录平台不能为空");

        // 通过微信接口获取用户信息
        WechatUserInfo wechatInfo = wechatClient.getUserInfo(request.getWechatCode(), request.getWechatPlatform());
        AssertUtil.notNull(wechatInfo, "微信授权失败");

        // 根据UnionID查找已绑定用户
        UserWechat userWechat = null;
        if (StrUtil.isNotBlank(wechatInfo.getUnionId())) {
            userWechat = userWechatService.getByUnionId(wechatInfo.getUnionId());
        }

        // UnionID未找到，尝试通过OpenID查找
        if (userWechat == null) {
            userWechat = findByOpenId(wechatInfo.getOpenId(), request.getWechatPlatform());
        }

        User user;
        if (userWechat != null) {
            // 已有绑定，更新微信信息并获取用户
            updateWechatInfo(userWechat, wechatInfo, request.getWechatPlatform());
            user = userService.getById(userWechat.getUserId());
        } else {
            // 新用户，创建用户和微信绑定
            user = createUserWithWechat(wechatInfo, request.getWechatPlatform());
        }

        log.info("用户{}微信登录成功, 平台: {}", user.getId(), request.getWechatPlatform());
        return user;
    }

    private UserWechat findByOpenId(String openId, String platform) {
        return switch (platform) {
            case "app" -> userWechatService.getByOpenIdApp(openId);
            case "mini" -> userWechatService.getByOpenIdMini(openId);
            case "mp" -> userWechatService.getByOpenIdMp(openId);
            default -> null;
        };
    }

    private void updateWechatInfo(UserWechat userWechat, WechatUserInfo wechatInfo, String platform) {
        userWechat.setNickname(wechatInfo.getNickname());
        userWechat.setAvatar(wechatInfo.getAvatar());
        userWechat.setGender(wechatInfo.getGender());
        
        if (StrUtil.isNotBlank(wechatInfo.getUnionId())) {
            userWechat.setUnionId(wechatInfo.getUnionId());
        }

        switch (platform) {
            case "app" -> userWechat.setOpenIdApp(wechatInfo.getOpenId());
            case "mini" -> {
                userWechat.setOpenIdMini(wechatInfo.getOpenId());
                userWechat.setSessionKey(wechatInfo.getSessionKey());
            }
            case "mp" -> userWechat.setOpenIdMp(wechatInfo.getOpenId());
        }

        userWechatService.updateById(userWechat);
    }

    private User createUserWithWechat(WechatUserInfo wechatInfo, String platform) {
        // 创建用户
        User user = new User();
        user.setUsername("wx_" + IdUtil.nanoId(8));
        user.setNickname(StrUtil.isNotBlank(wechatInfo.getNickname()) ? wechatInfo.getNickname() : "微信用户");
        user.setAvatar(wechatInfo.getAvatar());
        user.setGender(wechatInfo.getGender());
        user.setStatus(1);
        user.setVipLevel(0);
        user.setLanguageLevel(1);
        user.setTotalStudyMinutes(0);
        user.setTotalStudyDays(0);
        user.setPoints(0);
        userService.save(user);

        // 创建微信绑定
        UserWechat userWechat = new UserWechat();
        userWechat.setUserId(user.getId());
        userWechat.setUnionId(wechatInfo.getUnionId());
        userWechat.setNickname(wechatInfo.getNickname());
        userWechat.setAvatar(wechatInfo.getAvatar());
        userWechat.setGender(wechatInfo.getGender());
        userWechat.setCountry(wechatInfo.getCountry());
        userWechat.setProvince(wechatInfo.getProvince());
        userWechat.setCity(wechatInfo.getCity());

        switch (platform) {
            case "app" -> userWechat.setOpenIdApp(wechatInfo.getOpenId());
            case "mini" -> {
                userWechat.setOpenIdMini(wechatInfo.getOpenId());
                userWechat.setSessionKey(wechatInfo.getSessionKey());
            }
            case "mp" -> userWechat.setOpenIdMp(wechatInfo.getOpenId());
        }

        userWechatService.save(userWechat);

        log.info("微信用户{}自动注册成功", user.getId());
        return user;
    }
}
