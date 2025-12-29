package com.yxb.api.ai.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 发音评分结果DTO
 */
@Data
public class PronunciationScoreDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总分（0-100）
     */
    private Integer totalScore;

    /**
     * 准确度分数（0-100）
     */
    private Integer accuracyScore;

    /**
     * 流利度分数（0-100）
     */
    private Integer fluencyScore;

    /**
     * 语调分数（0-100）
     */
    private Integer intonationScore;

    /**
     * 完整度分数（0-100）
     */
    private Integer completenessScore;

    /**
     * 参考文本
     */
    private String referenceText;

    /**
     * 识别文本
     */
    private String recognizedText;

    /**
     * 音频时长（毫秒）
     */
    private Long audioDuration;

    /**
     * 单词详情
     */
    private List<WordDetail> wordDetails;

    /**
     * 发音建议
     */
    private List<String> suggestions;

    /**
     * 单词详情
     */
    @Data
    public static class WordDetail implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 单词
         */
        private String word;

        /**
         * 分数
         */
        private Integer score;

        /**
         * 是否发音错误
         */
        private Boolean hasError;

        /**
         * 错误类型
         */
        private String errorType;

        /**
         * 纠正建议
         */
        private String correction;

        /**
         * 音素详情
         */
        private List<PhonemeDetail> phonemes;
    }

    /**
     * 音素详情
     */
    @Data
    public static class PhonemeDetail implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 音素
         */
        private String phoneme;

        /**
         * 分数
         */
        private Integer score;
    }
}
