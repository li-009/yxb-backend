package com.yxb.api.ai.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 语法分析结果DTO
 */
@Data
public class GrammarAnalysisDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 原句
     */
    private String sentence;

    /**
     * 句子成分分析
     */
    private List<SentenceComponent> components;

    /**
     * 语法点列表
     */
    private List<GrammarPoint> grammarPoints;

    /**
     * 重点词汇
     */
    private List<KeyWord> keyWords;

    /**
     * 同义句
     */
    private List<String> synonymousSentences;

    /**
     * 易错点提醒
     */
    private List<String> commonMistakes;

    /**
     * 句子成分
     */
    @Data
    public static class SentenceComponent implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 文本
         */
        private String text;

        /**
         * 成分类型（主语/谓语/宾语/定语/状语等）
         */
        private String type;

        /**
         * 词性
         */
        private String partOfSpeech;
    }

    /**
     * 语法点
     */
    @Data
    public static class GrammarPoint implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 语法点名称
         */
        private String name;

        /**
         * 语法规则说明
         */
        private String rule;

        /**
         * 示例句子
         */
        private List<String> examples;
    }

    /**
     * 重点词汇
     */
    @Data
    public static class KeyWord implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 单词
         */
        private String word;

        /**
         * 词性
         */
        private String partOfSpeech;

        /**
         * 释义
         */
        private String meaning;

        /**
         * 音标
         */
        private String phonetic;
    }
}
