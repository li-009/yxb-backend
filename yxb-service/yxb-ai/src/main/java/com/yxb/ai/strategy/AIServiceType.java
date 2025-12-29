package com.yxb.ai.strategy;

/**
 * AI服务类型枚举
 */
public enum AIServiceType {
    
    /**
     * 语法分析
     */
    GRAMMAR("grammar", "语法分析"),
    
    /**
     * 语音识别(ASR)
     */
    ASR("asr", "语音识别"),
    
    /**
     * 发音评分
     */
    PRONUNCIATION("pronunciation", "发音评分"),
    
    /**
     * 单词查询
     */
    WORD("word", "单词查询"),
    
    /**
     * 聊天问答
     */
    CHAT("chat", "聊天问答");

    private final String code;
    private final String name;

    AIServiceType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
