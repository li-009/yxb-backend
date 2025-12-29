package com.yxb.study.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yxb.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("study_word_book")
public class WordBook extends BaseEntity {
    private Long userId;
    private String word;
    private String phonetic;
    private String meaning;
    private String example;
    private String exampleTranslation;
    private String language;
    private Long sourceVideoId;
    private Integer masteryStatus;
    private Integer reviewCount;
    private LocalDateTime nextReviewTime;
}
