package com.yxb.video.domain.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 视频创建请求
 */
@Data
public class VideoCreateRequest {

    @NotBlank(message = "标题不能为空")
    private String title;

    private String description;

    private String coverUrl;

    @NotBlank(message = "视频地址不能为空")
    private String videoUrl;

    private Integer duration;

    private String format;

    private Long fileSize;

    /**
     * 来源 1-上传 2-YouTube 3-其他
     */
    private Integer source;

    private String sourcePlatform;

    private String originalUrl;

    @NotBlank(message = "语种不能为空")
    private String language;

    /**
     * 难度等级 1-入门 2-初级 3-中级 4-高级
     */
    private Integer level;

    private Long categoryId;

    private String tags;
}
