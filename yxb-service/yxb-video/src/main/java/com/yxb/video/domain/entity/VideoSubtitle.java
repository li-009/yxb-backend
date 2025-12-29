package com.yxb.video.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yxb.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("video_subtitle")
public class VideoSubtitle extends BaseEntity {
    private Long videoId;
    private String language;
    private String format;
    private Integer source;
    private String fileUrl;
    private String content;
    private Integer status;
}
