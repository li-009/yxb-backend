package com.yxb.study.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yxb.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("study_progress")
public class StudyProgress extends BaseEntity {
    private Long userId;
    private Long videoId;
    private Integer currentPosition;
    private Integer watchedDuration;
    private Integer completionPercent;
    private Boolean completed;
    private Integer readAlongCount;
    private Integer avgReadAlongScore;
    private LocalDateTime lastStudyTime;
}
