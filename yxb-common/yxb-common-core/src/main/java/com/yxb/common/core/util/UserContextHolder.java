package com.yxb.common.core.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.Data;

/**
 * 用户上下文持有者
 * 使用TransmittableThreadLocal支持线程池场景
 */
public class UserContextHolder {

    private static final TransmittableThreadLocal<UserContext> CONTEXT = new TransmittableThreadLocal<>();

    private UserContextHolder() {
    }

    public static void setContext(UserContext context) {
        CONTEXT.set(context);
    }

    public static UserContext getContext() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static Long getUserId() {
        UserContext context = getContext();
        return context != null ? context.getUserId() : null;
    }

    public static String getUsername() {
        UserContext context = getContext();
        return context != null ? context.getUsername() : null;
    }

    public static String getOpenId() {
        UserContext context = getContext();
        return context != null ? context.getOpenId() : null;
    }

    public static boolean isLogin() {
        return getUserId() != null;
    }

    public static Long requireUserId() {
        Long userId = getUserId();
        if (userId == null) {
            AssertUtil.userNotLogin();
        }
        return userId;
    }

    @Data
    public static class UserContext {
        private Long userId;
        private String username;
        private String nickname;
        private String avatar;
        private String openId;
        private String unionId;
        private Integer vipLevel;
        private String token;
    }
}
