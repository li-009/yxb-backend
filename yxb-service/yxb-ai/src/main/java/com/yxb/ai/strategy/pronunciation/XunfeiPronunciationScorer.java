package com.yxb.ai.strategy.pronunciation;

import com.yxb.ai.config.AIProviderConfig;
import com.yxb.ai.integration.XunfeiClient;
import com.yxb.ai.strategy.AIProvider;
import com.yxb.api.ai.dto.PronunciationScoreDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 讯飞发音评分实现
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class XunfeiPronunciationScorer implements PronunciationScorer {

    private final AIProviderConfig config;
    private final XunfeiClient xunfeiClient;

    @Override
    public AIProvider getProvider() {
        return AIProvider.XUNFEI;
    }

    @Override
    public PronunciationScoreDTO score(byte[] audioData, String text, String language) {
        log.info("调用讯飞口语评测, 文本: {}, 语言: {}", text, language);
        
        // 调用讯飞口语评测API
        return xunfeiClient.evaluatePronunciation(audioData, text, language);
    }

    @Override
    public boolean isAvailable() {
        return config.getXunfei() != null && config.getXunfei().isEnabled();
    }
}
