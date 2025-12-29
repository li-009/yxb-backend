package com.yxb.common.core.constant;

/**
 * 通用常量
 */
public interface CommonConstant {

    /**
     * 成功标记
     */
    Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    Integer FAIL = 500;

    /**
     * 是
     */
    Integer YES = 1;

    /**
     * 否
     */
    Integer NO = 0;

    /**
     * 删除标记
     */
    Integer DELETED = 1;

    /**
     * 未删除标记
     */
    Integer NOT_DELETED = 0;

    /**
     * 启用状态
     */
    Integer STATUS_ENABLED = 1;

    /**
     * 禁用状态
     */
    Integer STATUS_DISABLED = 0;

    /**
     * UTF-8编码
     */
    String UTF8 = "UTF-8";

    /**
     * Token前缀
     */
    String TOKEN_PREFIX = "Bearer ";

    /**
     * Token Header
     */
    String TOKEN_HEADER = "Authorization";

    /**
     * 用户ID Header
     */
    String USER_ID_HEADER = "X-User-Id";

    /**
     * 用户名 Header
     */
    String USER_NAME_HEADER = "X-User-Name";
}
