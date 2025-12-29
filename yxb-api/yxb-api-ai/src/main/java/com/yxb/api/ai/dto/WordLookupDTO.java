package com.yxb.api.ai.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 单词查询结果DTO
 */
@Data
public class WordLookupDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单词
     */
    private String word;

    /**
     * 音标（美式）
     */
    private String phoneticUs;

    /**
     * 音标（英式）
     */
    private String phoneticUk;

    /**
     * 发音URL（美式）
     */
    private String audioUrlUs;

    /**
     * 发音URL（英式）
     */
    private String audioUrlUk;

    /**
     * 释义列表
     */
    private List<Definition> definitions;

    /**
     * 例句列表
     */
    private List<Example> examples;

    /**
     * 同义词
     */
    private List<String> synonyms;

    /**
     * 反义词
     */
    private List<String> antonyms;

    /**
     * 词形变化
     */
    private WordForms wordForms;

    /**
     * 词源
     */
    private String etymology;

    /**
     * 词频等级 1-最常用 5-较少用
     */
    private Integer frequencyLevel;

    /**
     * 释义
     */
    @Data
    public static class Definition implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 词性
         */
        private String partOfSpeech;

        /**
         * 英文释义
         */
        private String meaningEn;

        /**
         * 中文释义
         */
        private String meaningCn;
    }

    /**
     * 例句
     */
    @Data
    public static class Example implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 英文例句
         */
        private String sentence;

        /**
         * 中文翻译
         */
        private String translation;

        /**
         * 来源
         */
        private String source;
    }

    /**
     * 词形变化
     */
    @Data
    public static class WordForms implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 复数
         */
        private String plural;

        /**
         * 过去式
         */
        private String pastTense;

        /**
         * 过去分词
         */
        private String pastParticiple;

        /**
         * 现在分词
         */
        private String presentParticiple;

        /**
         * 第三人称单数
         */
        private String thirdPerson;

        /**
         * 比较级
         */
        private String comparative;

        /**
         * 最高级
         */
        private String superlative;
    }
}
