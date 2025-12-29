package com.yxb.common.core.util;

import com.yxb.common.core.exception.BizException;
import com.yxb.common.core.result.IResultCode;
import com.yxb.common.core.result.ResultCode;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Collection;

/**
 * 断言工具类
 */
public class AssertUtil {

    private AssertUtil() {
    }

    public static void notNull(Object obj, String message) {
        if (ObjectUtil.isNull(obj)) {
            throw new BizException(message);
        }
    }

    public static void notNull(Object obj, IResultCode resultCode) {
        if (ObjectUtil.isNull(obj)) {
            throw new BizException(resultCode);
        }
    }

    public static void isNull(Object obj, String message) {
        if (ObjectUtil.isNotNull(obj)) {
            throw new BizException(message);
        }
    }

    public static void isNull(Object obj, IResultCode resultCode) {
        if (ObjectUtil.isNotNull(obj)) {
            throw new BizException(resultCode);
        }
    }

    public static void notBlank(String str, String message) {
        if (StrUtil.isBlank(str)) {
            throw new BizException(message);
        }
    }

    public static void notBlank(String str, IResultCode resultCode) {
        if (StrUtil.isBlank(str)) {
            throw new BizException(resultCode);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollUtil.isEmpty(collection)) {
            throw new BizException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, IResultCode resultCode) {
        if (CollUtil.isEmpty(collection)) {
            throw new BizException(resultCode);
        }
    }

    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new BizException(message);
        }
    }

    public static void isTrue(boolean condition, IResultCode resultCode) {
        if (!condition) {
            throw new BizException(resultCode);
        }
    }

    public static void isFalse(boolean condition, String message) {
        if (condition) {
            throw new BizException(message);
        }
    }

    public static void isFalse(boolean condition, IResultCode resultCode) {
        if (condition) {
            throw new BizException(resultCode);
        }
    }

    public static void fail(String message) {
        throw new BizException(message);
    }

    public static void fail(IResultCode resultCode) {
        throw new BizException(resultCode);
    }

    public static void userNotLogin() {
        throw new BizException(ResultCode.USER_NOT_LOGIN);
    }

    public static void permissionDenied() {
        throw new BizException(ResultCode.PERMISSION_NO_ACCESS);
    }
}
