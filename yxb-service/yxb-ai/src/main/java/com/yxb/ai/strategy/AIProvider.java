package com.yxb.ai.strategy;

/**
 * AI服务商枚举
 */
public enum AIProvider {
    
    OPENAI("openai", "OpenAI"),
    XUNFEI("xunfei", "讯飞"),
    BAIDU("baidu", "百度"),
    YOUDAO("youdao", "有道");

    private final String code;
    private final String name;

    AIProvider(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static AIProvider fromCode(String code) {
        for (AIProvider provider : values()) {
            if (provider.code.equalsIgnoreCase(code)) {
                return provider;
            }
        }
        return null;
    }
}
