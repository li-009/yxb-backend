package com.yxb.study.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yxb.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("study_note")
public class StudyNote extends BaseEntity {
    private Long userId;
    private Long videoId;
    private Integer timestamp;
    private String content;
    private String subtitleText;
    private String category;
    private String tags;
}
