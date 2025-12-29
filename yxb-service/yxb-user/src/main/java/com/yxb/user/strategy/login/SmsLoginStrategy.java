package com.yxb.user.strategy.login;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.yxb.common.core.constant.CacheConstant;
import com.yxb.common.core.util.AssertUtil;
import com.yxb.common.redis.service.RedisService;
import com.yxb.user.domain.entity.User;
import com.yxb.user.domain.vo.LoginRequest;
import com.yxb.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 短信验证码登录策略
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SmsLoginStrategy implements LoginStrategy {

    private final UserService userService;
    private final RedisService redisService;

    @Override
    public String getLoginType() {
        return "sms";
    }

    @Override
    public User login(LoginRequest request) {
        AssertUtil.notBlank(request.getPhone(), "手机号不能为空");
        AssertUtil.notBlank(request.getSmsCode(), "验证码不能为空");

        // 验证短信验证码
        String cacheCode = redisService.get(CacheConstant.CAPTCHA + request.getPhone());
        AssertUtil.isTrue(StrUtil.equals(cacheCode, request.getSmsCode()), "验证码错误或已过期");

        // 查询用户
        User user = userService.getByPhone(request.getPhone());

        // 用户不存在则自动注册
        if (user == null) {
            user = new User();
            user.setUsername(request.getPhone());
            user.setPhone(request.getPhone());
            user.setNickname("用户" + IdUtil.nanoId(6));
            user.setStatus(1);
            user.setVipLevel(0);
            user.setLanguageLevel(1);
            user.setTotalStudyMinutes(0);
            user.setTotalStudyDays(0);
            user.setPoints(0);
            userService.save(user);
            log.info("手机号{}自动注册成功", request.getPhone());
        }

        // 删除验证码缓存
        redisService.delete(CacheConstant.CAPTCHA + request.getPhone());

        log.info("用户{}短信登录成功", user.getPhone());
        return user;
    }
}
