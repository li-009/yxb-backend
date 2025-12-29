package com.yxb.api.video.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 字幕信息DTO
 */
@Data
public class SubtitleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字幕ID
     */
    private Long id;

    /**
     * 视频ID
     */
    private Long videoId;

    /**
     * 字幕语言
     */
    private String language;

    /**
     * 字幕格式 srt/ass/vtt
     */
    private String format;

    /**
     * 字幕来源 1-内嵌 2-外部导入 3-AI生成
     */
    private Integer source;

    /**
     * 字幕文件URL
     */
    private String fileUrl;

    /**
     * 字幕内容列表
     */
    private List<SubtitleItem> items;

    /**
     * 字幕条目
     */
    @Data
    public static class SubtitleItem implements Serializable {
        
        private static final long serialVersionUID = 1L;

        /**
         * 序号
         */
        private Integer index;

        /**
         * 开始时间（毫秒）
         */
        private Long startTime;

        /**
         * 结束时间（毫秒）
         */
        private Long endTime;

        /**
         * 原文内容
         */
        private String text;

        /**
         * 译文内容
         */
        private String translation;
    }
}
