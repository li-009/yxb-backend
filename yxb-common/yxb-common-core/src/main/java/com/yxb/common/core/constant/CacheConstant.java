package com.yxb.common.core.constant;

/**
 * 缓存常量
 */
public interface CacheConstant {

    /**
     * 缓存前缀
     */
    String PREFIX = "yxb:";

    /**
     * 用户Token缓存
     */
    String USER_TOKEN = PREFIX + "user:token:";

    /**
     * 用户信息缓存
     */
    String USER_INFO = PREFIX + "user:info:";

    /**
     * 视频信息缓存
     */
    String VIDEO_INFO = PREFIX + "video:info:";

    /**
     * 热门视频列表缓存
     */
    String VIDEO_HOT_LIST = PREFIX + "video:hot:list";

    /**
     * 字幕缓存
     */
    String VIDEO_SUBTITLE = PREFIX + "video:subtitle:";

    /**
     * 学习进度缓存
     */
    String STUDY_PROGRESS = PREFIX + "study:progress:";

    /**
     * AI语法讲解缓存
     */
    String AI_GRAMMAR = PREFIX + "ai:grammar:";

    /**
     * AI单词释义缓存
     */
    String AI_WORD = PREFIX + "ai:word:";

    /**
     * 验证码缓存
     */
    String CAPTCHA = PREFIX + "captcha:";

    /**
     * 打卡排行榜
     */
    String RANK_CHECKIN = PREFIX + "rank:checkin:";

    /**
     * 分布式锁前缀
     */
    String LOCK_PREFIX = PREFIX + "lock:";

    /**
     * 默认过期时间（秒）- 1小时
     */
    long DEFAULT_EXPIRE = 3600;

    /**
     * 用户Token过期时间（秒）- 7天
     */
    long TOKEN_EXPIRE = 7 * 24 * 3600;

    /**
     * 视频信息过期时间（秒）- 1天
     */
    long VIDEO_EXPIRE = 24 * 3600;

    /**
     * AI结果过期时间（秒）- 7天
     */
    long AI_RESULT_EXPIRE = 7 * 24 * 3600;
}
