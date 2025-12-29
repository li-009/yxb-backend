package com.yxb.common.core.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页响应结果
 */
@Data
@Accessors(chain = true)
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private List<T> list;
    private long total;
    private int pageNum;
    private int pageSize;
    private int pages;
    private long timestamp;

    public PageResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> PageResult<T> success(List<T> list, long total, int pageNum, int pageSize) {
        PageResult<T> result = new PageResult<>();
        result.setCode(ResultCode.SUCCESS.getCode())
              .setMessage(ResultCode.SUCCESS.getMessage())
              .setList(list)
              .setTotal(total)
              .setPageNum(pageNum)
              .setPageSize(pageSize)
              .setPages((int) Math.ceil((double) total / pageSize));
        return result;
    }

    public static <T> PageResult<T> empty(int pageNum, int pageSize) {
        return success(Collections.emptyList(), 0, pageNum, pageSize);
    }

    public static <T> PageResult<T> fail(String message) {
        PageResult<T> result = new PageResult<>();
        result.setCode(ResultCode.FAIL.getCode())
              .setMessage(message)
              .setList(Collections.emptyList())
              .setTotal(0);
        return result;
    }
}
