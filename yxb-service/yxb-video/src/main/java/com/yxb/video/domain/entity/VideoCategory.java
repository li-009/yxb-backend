package com.yxb.video.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yxb.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("video_category")
public class VideoCategory extends BaseEntity {
    private String name;
    private String icon;
    private Long parentId;
    private Integer sort;
    private Integer status;
}
