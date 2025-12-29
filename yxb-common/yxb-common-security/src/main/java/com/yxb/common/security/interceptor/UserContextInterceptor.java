package com.yxb.common.security.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.yxb.common.core.util.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用户上下文拦截器
 */
@Slf4j
@Component
public class UserContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (StpUtil.isLogin()) {
            try {
                Long userId = StpUtil.getLoginIdAsLong();
                UserContextHolder.UserContext context = new UserContextHolder.UserContext();
                context.setUserId(userId);
                
                // 从Session中获取用户信息
                Object username = StpUtil.getSession().get("username");
                Object nickname = StpUtil.getSession().get("nickname");
                Object avatar = StpUtil.getSession().get("avatar");
                Object openId = StpUtil.getSession().get("openId");
                Object unionId = StpUtil.getSession().get("unionId");
                Object vipLevel = StpUtil.getSession().get("vipLevel");
                
                if (username != null) context.setUsername((String) username);
                if (nickname != null) context.setNickname((String) nickname);
                if (avatar != null) context.setAvatar((String) avatar);
                if (openId != null) context.setOpenId((String) openId);
                if (unionId != null) context.setUnionId((String) unionId);
                if (vipLevel != null) context.setVipLevel((Integer) vipLevel);
                context.setToken(StpUtil.getTokenValue());
                
                UserContextHolder.setContext(context);
            } catch (Exception e) {
                log.warn("设置用户上下文失败: {}", e.getMessage());
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                Object handler, Exception ex) {
        UserContextHolder.clear();
    }
}
