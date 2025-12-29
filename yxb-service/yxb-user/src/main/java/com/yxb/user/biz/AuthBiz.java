package com.yxb.user.biz;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.yxb.api.user.dto.LoginDTO;
import com.yxb.api.user.dto.UserDTO;
import com.yxb.common.core.constant.CacheConstant;
import com.yxb.common.core.exception.BizException;
import com.yxb.common.core.result.ResultCode;
import com.yxb.common.core.util.AssertUtil;
import com.yxb.common.redis.service.RedisService;
import com.yxb.user.convert.UserConvert;
import com.yxb.user.domain.entity.User;
import com.yxb.user.domain.entity.UserWechat;
import com.yxb.user.domain.vo.LoginRequest;
import com.yxb.user.domain.vo.RegisterRequest;
import com.yxb.user.service.UserService;
import com.yxb.user.service.UserVipService;
import com.yxb.user.service.UserWechatService;
import com.yxb.user.strategy.login.LoginContext;
import com.yxb.user.strategy.login.LoginStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证业务层
 * 处理登录、注册、授权等业务逻辑
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthBiz {

    private final UserService userService;
    private final UserWechatService userWechatService;
    private final UserVipService userVipService;
    private final RedisService redisService;
    private final LoginContext loginContext;

    /**
     * 统一登录入口（策略模式）
     */
    public LoginDTO login(LoginRequest request, String ip) {
        // 使用策略模式处理不同登录方式
        LoginStrategy strategy = loginContext.getStrategy(request.getLoginType());
        AssertUtil.notNull(strategy, "不支持的登录类型");
        
        User user = strategy.login(request);
        AssertUtil.notNull(user, ResultCode.USER_LOGIN_ERROR);
        AssertUtil.isTrue(user.getStatus() == 1, ResultCode.USER_ACCOUNT_FORBIDDEN);

        // 更新登录信息
        userService.updateLastLogin(user.getId(), ip);

        // 生成Token
        return generateLoginResult(user, false);
    }

    /**
     * 手机号注册
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginDTO register(RegisterRequest request, String ip) {
        // 验证短信验证码
        String cacheCode = redisService.get(CacheConstant.CAPTCHA + request.getPhone());
        AssertUtil.isTrue(StrUtil.equals(cacheCode, request.getSmsCode()), "验证码错误或已过期");

        // 检查手机号是否已注册
        User existUser = userService.getByPhone(request.getPhone());
        AssertUtil.isNull(existUser, ResultCode.USER_HAS_EXIST);

        // 创建用户
        User user = new User();
        user.setUsername(request.getPhone());
        user.setPhone(request.getPhone());
        user.setPassword(BCrypt.hashpw(request.getPassword()));
        user.setNickname(StrUtil.isNotBlank(request.getNickname()) ? request.getNickname() : "用户" + IdUtil.nanoId(6));
        user.setStatus(1);
        user.setVipLevel(0);
        user.setLanguageLevel(1);
        user.setTotalStudyMinutes(0);
        user.setTotalStudyDays(0);
        user.setPoints(0);
        userService.save(user);

        // 删除验证码缓存
        redisService.delete(CacheConstant.CAPTCHA + request.getPhone());

        // 更新登录信息
        userService.updateLastLogin(user.getId(), ip);

        return generateLoginResult(user, true);
    }

    /**
     * 刷新Token
     */
    public LoginDTO refreshToken() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        AssertUtil.notNull(user, ResultCode.USER_NOT_EXIST);
        
        // 重新生成Token
        StpUtil.logout();
        return generateLoginResult(user, false);
    }

    /**
     * 退出登录
     */
    public void logout() {
        StpUtil.logout();
    }

    /**
     * 生成登录结果
     */
    private LoginDTO generateLoginResult(User user, boolean isNewUser) {
        // 登录并获取Token
        StpUtil.login(user.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 存储用户信息到Session
        StpUtil.getSession().set("username", user.getUsername());
        StpUtil.getSession().set("nickname", user.getNickname());
        StpUtil.getSession().set("avatar", user.getAvatar());
        StpUtil.getSession().set("vipLevel", user.getVipLevel());

        // 获取微信绑定信息
        UserWechat wechat = userWechatService.getByUserId(user.getId());
        if (wechat != null) {
            StpUtil.getSession().set("openId", wechat.getOpenIdMini());
            StpUtil.getSession().set("unionId", wechat.getUnionId());
        }

        // 构建返回结果
        LoginDTO result = new LoginDTO();
        result.setAccessToken(tokenInfo.getTokenValue());
        result.setExpiresIn(tokenInfo.getTokenTimeout());
        result.setUser(UserConvert.INSTANCE.toDTO(user));
        result.setIsNewUser(isNewUser);

        return result;
    }
}
