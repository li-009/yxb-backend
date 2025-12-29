package com.yxb.common.core.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 统一响应结果
 */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private T data;
    private long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>()
                .setCode(ResultCode.SUCCESS.getCode())
                .setMessage(ResultCode.SUCCESS.getMessage())
                .setData(data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<T>()
                .setCode(ResultCode.SUCCESS.getCode())
                .setMessage(message)
                .setData(data);
    }

    public static <T> Result<T> fail() {
        return fail(ResultCode.FAIL);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<T>()
                .setCode(ResultCode.FAIL.getCode())
                .setMessage(message);
    }

    public static <T> Result<T> fail(IResultCode resultCode) {
        return new Result<T>()
                .setCode(resultCode.getCode())
                .setMessage(resultCode.getMessage());
    }

    public static <T> Result<T> fail(IResultCode resultCode, String message) {
        return new Result<T>()
                .setCode(resultCode.getCode())
                .setMessage(message);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<T>()
                .setCode(code)
                .setMessage(message);
    }

    public boolean isSuccess() {
        return this.code == ResultCode.SUCCESS.getCode();
    }
}
