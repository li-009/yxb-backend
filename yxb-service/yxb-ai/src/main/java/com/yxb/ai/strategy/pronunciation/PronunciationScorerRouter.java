package com.yxb.ai.strategy.pronunciation;

import com.yxb.ai.config.AIProviderConfig;
import com.yxb.ai.strategy.AIProvider;
import com.yxb.api.ai.dto.PronunciationScoreDTO;
import com.yxb.common.core.exception.BizException;
import com.yxb.common.core.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发音评分器路由（策略模式 + 降级机制）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PronunciationScorerRouter {

    private final AIProviderConfig config;
    private final List<PronunciationScorer> scorers;

    private Map<AIProvider, PronunciationScorer> scorerMap;

    private void initScorerMap() {
        if (scorerMap == null) {
            scorerMap = new HashMap<>();
            for (PronunciationScorer scorer : scorers) {
                scorerMap.put(scorer.getProvider(), scorer);
            }
        }
    }

    /**
     * 执行发音评分（带降级）
     */
    public PronunciationScoreDTO score(byte[] audioData, String text, String language) {
        initScorerMap();

        List<String> providerCodes = config.getProvider().getPronunciation();

        for (String code : providerCodes) {
            AIProvider provider = AIProvider.fromCode(code);
            if (provider == null) {
                continue;
            }

            PronunciationScorer scorer = scorerMap.get(provider);
            if (scorer == null || !scorer.isAvailable()) {
                log.warn("发音评分服务不可用: {}", provider.getName());
                continue;
            }

            try {
                log.info("使用{}进行发音评分", provider.getName());
                return scorer.score(audioData, text, language);
            } catch (Exception e) {
                log.error("{}发音评分失败，尝试降级: {}", provider.getName(), e.getMessage());
            }
        }

        throw new BizException(ResultCode.AI_SERVICE_ERROR, "发音评分服务暂不可用");
    }
}
