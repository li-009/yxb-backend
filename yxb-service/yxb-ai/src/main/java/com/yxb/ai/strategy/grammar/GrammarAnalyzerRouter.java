package com.yxb.ai.strategy.grammar;

import com.yxb.ai.config.AIProviderConfig;
import com.yxb.ai.strategy.AIProvider;
import com.yxb.api.ai.dto.GrammarAnalysisDTO;
import com.yxb.common.core.constant.CacheConstant;
import com.yxb.common.core.exception.BizException;
import com.yxb.common.core.result.ResultCode;
import com.yxb.common.redis.service.RedisService;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 语法分析器路由（策略模式 + 降级机制）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GrammarAnalyzerRouter {

    private final AIProviderConfig config;
    private final RedisService redisService;
    private final List<GrammarAnalyzer> analyzers;

    private Map<AIProvider, GrammarAnalyzer> analyzerMap;

    /**
     * 初始化分析器映射
     */
    private void initAnalyzerMap() {
        if (analyzerMap == null) {
            analyzerMap = new HashMap<>();
            for (GrammarAnalyzer analyzer : analyzers) {
                analyzerMap.put(analyzer.getProvider(), analyzer);
            }
        }
    }

    /**
     * 执行语法分析（带降级）
     */
    public GrammarAnalysisDTO analyze(String sentence, String language) {
        initAnalyzerMap();

        // 尝试从缓存获取
        String cacheKey = CacheConstant.AI_GRAMMAR + DigestUtil.md5Hex(sentence + language);
        GrammarAnalysisDTO cached = redisService.get(cacheKey);
        if (cached != null) {
            log.info("语法分析命中缓存: {}", sentence);
            return cached;
        }

        // 获取服务商优先级列表
        List<String> providerCodes = config.getProvider().getGrammar();

        // 依次尝试各服务商
        for (String code : providerCodes) {
            AIProvider provider = AIProvider.fromCode(code);
            if (provider == null) {
                continue;
            }

            GrammarAnalyzer analyzer = analyzerMap.get(provider);
            if (analyzer == null || !analyzer.isAvailable()) {
                log.warn("语法分析器不可用: {}", provider.getName());
                continue;
            }

            try {
                log.info("使用{}进行语法分析", provider.getName());
                GrammarAnalysisDTO result = analyzer.analyze(sentence, language);
                
                // 缓存结果
                redisService.set(cacheKey, result, CacheConstant.AI_RESULT_EXPIRE);
                
                return result;
            } catch (Exception e) {
                log.error("{}语法分析失败，尝试降级: {}", provider.getName(), e.getMessage());
            }
        }

        throw new BizException(ResultCode.AI_SERVICE_ERROR, "语法分析服务暂不可用");
    }
}
