package com.yxb.common.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),

    // 参数错误 1001-1999
    PARAM_ERROR(1001, "参数错误"),
    PARAM_NOT_VALID(1002, "参数校验失败"),
    PARAM_IS_BLANK(1003, "参数为空"),
    PARAM_TYPE_ERROR(1004, "参数类型错误"),

    // 用户错误 2001-2999
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_LOGIN_ERROR(2002, "用户名或密码错误"),
    USER_ACCOUNT_FORBIDDEN(2003, "账号已被禁用"),
    USER_NOT_EXIST(2004, "用户不存在"),
    USER_HAS_EXIST(2005, "用户已存在"),
    USER_TOKEN_EXPIRED(2006, "登录已过期"),
    USER_TOKEN_INVALID(2007, "无效的Token"),

    // 权限错误 3001-3999
    PERMISSION_NO_ACCESS(3001, "无访问权限"),
    PERMISSION_FORBIDDEN(3002, "权限不足"),

    // 业务错误 4001-4999
    BIZ_ERROR(4001, "业务处理失败"),
    VIDEO_NOT_FOUND(4002, "视频不存在"),
    VIDEO_FORMAT_NOT_SUPPORT(4003, "视频格式不支持"),
    SUBTITLE_PARSE_ERROR(4004, "字幕解析失败"),
    FILE_UPLOAD_ERROR(4009, "文件上传失败"),
    FILE_TOO_LARGE(4010, "文件大小超过限制"),
    FILE_TYPE_NOT_SUPPORT(4011, "文件类型不支持"),
    AI_SERVICE_ERROR(4005, "AI服务调用失败"),
    AI_SERVICE_BUSY(4006, "AI服务繁忙，请稍后重试"),
    WECHAT_AUTH_ERROR(4007, "微信授权失败"),
    RESOURCE_NOT_FOUND(4008, "资源不存在"),

    // 系统错误 5001-5999
    SYSTEM_ERROR(5001, "系统内部错误"),
    SYSTEM_BUSY(5002, "系统繁忙"),
    RATE_LIMIT_EXCEEDED(5003, "请求过于频繁"),
    SERVICE_UNAVAILABLE(5004, "服务暂不可用");

    private final int code;
    private final String message;
}
