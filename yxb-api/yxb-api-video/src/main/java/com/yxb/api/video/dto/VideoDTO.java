package com.yxb.api.video.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 视频信息DTO
 */
@Data
public class VideoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 视频ID
     */
    private Long id;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频描述
     */
    private String description;

    /**
     * 封面图URL
     */
    private String coverUrl;

    /**
     * 视频URL
     */
    private String videoUrl;

    /**
     * 视频时长（秒）
     */
    private Integer duration;

    /**
     * 视频格式
     */
    private String format;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 视频来源 1-本地上传 2-在线资源 3-第三方链接
     */
    private Integer source;

    /**
     * 来源平台名称
     */
    private String sourcePlatform;

    /**
     * 原始链接
     */
    private String originalUrl;

    /**
     * 语言代码
     */
    private String language;

    /**
     * 难度等级 1-入门 2-初级 3-中级 4-高级
     */
    private Integer level;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 标签（逗号分隔）
     */
    private String tags;

    /**
     * 播放次数
     */
    private Integer playCount;

    /**
     * 收藏次数
     */
    private Integer collectCount;

    /**
     * 是否有字幕
     */
    private Boolean hasSubtitle;

    /**
     * 状态 0-待审核 1-正常 2-下架
     */
    private Integer status;

    /**
     * 上传用户ID
     */
    private Long uploadUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
