package com.yxb.video.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yxb.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("video_info")
public class Video extends BaseEntity {
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
    private Integer playCount;
    private Integer collectCount;
    private Boolean hasSubtitle;
    private Integer status;
    private Long uploadUserId;
}
