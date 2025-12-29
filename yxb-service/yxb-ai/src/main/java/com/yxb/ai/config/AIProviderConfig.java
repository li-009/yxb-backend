package com.yxb.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * AI服务商配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AIProviderConfig {

    /**
     * OpenAI配置
     */
    private OpenAIConfig openai;

    /**
     * 讯飞配置
     */
    private XunfeiConfig xunfei;

    /**
     * 百度配置
     */
    private BaiduConfig baidu;

    /**
     * 服务商优先级配置
     */
    private ProviderPriority provider;

    @Data
    public static class OpenAIConfig {
        private boolean enabled;
        private String apiKey;
        private String baseUrl;
        private String model;
        private int timeout;
        private int maxRetries;
    }

    @Data
    public static class XunfeiConfig {
        private boolean enabled;
        private String appId;
        private String apiKey;
        private String apiSecret;
        private String asrUrl;
        private String ttsUrl;
        private String oralUrl;
    }

    @Data
    public static class BaiduConfig {
        private boolean enabled;
        private String appId;
        private String apiKey;
        private String secretKey;
    }

    @Data
    public static class ProviderPriority {
        private List<String> grammar;
        private List<String> asr;
        private List<String> pronunciation;
        private List<String> word;
        private List<String> chat;
    }
}
