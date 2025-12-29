package com.yxb.user.strategy.login;

import cn.hutool.crypto.digest.BCrypt;
import com.yxb.common.core.result.ResultCode;
import com.yxb.common.core.util.AssertUtil;
import com.yxb.user.domain.entity.User;
import com.yxb.user.domain.vo.LoginRequest;
import com.yxb.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 密码登录策略
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordLoginStrategy implements LoginStrategy {

    private final UserService userService;

    @Override
    public String getLoginType() {
        return "password";
    }

    @Override
    public User login(LoginRequest request) {
        AssertUtil.notBlank(request.getUsername(), "用户名不能为空");
        AssertUtil.notBlank(request.getPassword(), "密码不能为空");

        // 根据用户名查询（支持用户名或手机号登录）
        User user = userService.getByUsername(request.getUsername());
        if (user == null) {
            user = userService.getByPhone(request.getUsername());
        }

        AssertUtil.notNull(user, ResultCode.USER_LOGIN_ERROR);

        // 验证密码
        boolean checkPwd = BCrypt.checkpw(request.getPassword(), user.getPassword());
        AssertUtil.isTrue(checkPwd, ResultCode.USER_LOGIN_ERROR);

        log.info("用户{}密码登录成功", user.getUsername());
        return user;
    }
}
