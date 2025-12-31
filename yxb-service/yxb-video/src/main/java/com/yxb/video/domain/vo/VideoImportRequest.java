package com.yxb.video.domain.vo;

import lombok.Data;

@Data
public class VideoImportRequest {
    private String title;
    private String description;
    private String videoUrl;
    private String coverUrl;
    private Integer duration;
    private String language;
    private Integer level;
}
