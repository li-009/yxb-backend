package com.yxb.video.domain.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 视频更新请求
 */
@Data
public class VideoUpdateRequest {

    @NotNull(message = "视频ID不能为空")
    private Long id;

    private String title;

    private String description;

    private String coverUrl;

    private String videoUrl;

    private Integer duration;

    private String format;

    private Long fileSize;

    private Integer source;

    private String sourcePlatform;

    private String originalUrl;

    private String language;

    private Integer level;

    private Long categoryId;

    private String tags;
}
