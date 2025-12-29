package com.yxb.user.strategy.login;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录策略上下文（策略模式）
 */
@Component
public class LoginContext {

    private final Map<String, LoginStrategy> strategyMap = new HashMap<>();

    public LoginContext(List<LoginStrategy> strategies) {
        for (LoginStrategy strategy : strategies) {
            strategyMap.put(strategy.getLoginType(), strategy);
        }
    }

    /**
     * 获取登录策略
     */
    public LoginStrategy getStrategy(String loginType) {
        return strategyMap.get(loginType);
    }
}
