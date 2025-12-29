package com.yxb.api.study.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学习笔记DTO
 */
@Data
public class StudyNoteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 笔记ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 视频ID
     */
    private Long videoId;

    /**
     * 视频标题
     */
    private String videoTitle;

    /**
     * 时间戳（秒）- 关联视频位置
     */
    private Integer timestamp;

    /**
     * 笔记内容
     */
    private String content;

    /**
     * 关联的字幕文本
     */
    private String subtitleText;

    /**
     * 笔记分类
     */
    private String category;

    /**
     * 标签（逗号分隔）
     */
    private String tags;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
