package com.yxb.api.study.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学习进度DTO
 */
@Data
public class StudyProgressDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 进度ID
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
     * 视频封面
     */
    private String videoCover;

    /**
     * 视频时长（秒）
     */
    private Integer videoDuration;

    /**
     * 当前播放位置（秒）
     */
    private Integer currentPosition;

    /**
     * 已观看时长（秒）
     */
    private Integer watchedDuration;

    /**
     * 完成百分比
     */
    private Integer completionPercent;

    /**
     * 是否已完成
     */
    private Boolean completed;

    /**
     * 跟读次数
     */
    private Integer readAlongCount;

    /**
     * 平均跟读分数
     */
    private Integer avgReadAlongScore;

    /**
     * 最后学习时间
     */
    private LocalDateTime lastStudyTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
