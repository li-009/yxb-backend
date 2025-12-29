package com.yxb.user.strategy.login;

import com.yxb.user.domain.entity.User;
import com.yxb.user.domain.vo.LoginRequest;

/**
 * 登录策略接口（策略模式）
 */
public interface LoginStrategy {

    /**
     * 获取登录类型
     */
    String getLoginType();

    /**
     * 执行登录
     */
    User login(LoginRequest request);
}
