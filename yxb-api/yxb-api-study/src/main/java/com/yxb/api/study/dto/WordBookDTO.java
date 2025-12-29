package com.yxb.api.study.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 单词本DTO
 */
@Data
public class WordBookDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 单词
     */
    private String word;

    /**
     * 音标
     */
    private String phonetic;

    /**
     * 释义
     */
    private String meaning;

    /**
     * 例句
     */
    private String example;

    /**
     * 例句翻译
     */
    private String exampleTranslation;

    /**
     * 语言代码
     */
    private String language;

    /**
     * 来源视频ID
     */
    private Long sourceVideoId;

    /**
     * 来源视频标题
     */
    private String sourceVideoTitle;

    /**
     * 掌握状态 0-未掌握 1-学习中 2-已掌握
     */
    private Integer masteryStatus;

    /**
     * 复习次数
     */
    private Integer reviewCount;

    /**
     * 下次复习时间
     */
    private LocalDateTime nextReviewTime;

    /**
     * 添加时间
     */
    private LocalDateTime createTime;
}
