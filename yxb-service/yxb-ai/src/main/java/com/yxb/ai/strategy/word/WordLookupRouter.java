package com.yxb.ai.strategy.word;

import com.yxb.ai.config.AIProviderConfig;
import com.yxb.ai.strategy.AIProvider;
import com.yxb.api.ai.dto.WordLookupDTO;
import com.yxb.common.core.constant.CacheConstant;
import com.yxb.common.core.exception.BizException;
import com.yxb.common.core.result.ResultCode;
import com.yxb.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单词查询路由（策略模式 + 降级机制）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WordLookupRouter {

    private final AIProviderConfig config;
    private final RedisService redisService;
    private final List<WordLookupService> services;

    private Map<AIProvider, WordLookupService> serviceMap;

    private void initServiceMap() {
        if (serviceMap == null) {
            serviceMap = new HashMap<>();
            for (WordLookupService service : services) {
                serviceMap.put(service.getProvider(), service);
            }
        }
    }

    /**
     * 执行单词查询（带降级和缓存）
     */
    public WordLookupDTO lookup(String word, String language) {
        initServiceMap();

        // 尝试从缓存获取
        String cacheKey = CacheConstant.AI_WORD + word.toLowerCase() + ":" + language;
        WordLookupDTO cached = redisService.get(cacheKey);
        if (cached != null) {
            log.info("单词查询命中缓存: {}", word);
            return cached;
        }

        List<String> providerCodes = config.getProvider().getWord();

        for (String code : providerCodes) {
            AIProvider provider = AIProvider.fromCode(code);
            if (provider == null) {
                continue;
            }

            WordLookupService service = serviceMap.get(provider);
            if (service == null || !service.isAvailable()) {
                log.warn("单词查询服务不可用: {}", provider.getName());
                continue;
            }

            try {
                log.info("使用{}进行单词查询", provider.getName());
                WordLookupDTO result = service.lookup(word, language);
                
                // 缓存结果（单词信息缓存30天）
                redisService.set(cacheKey, result, 30 * 24 * 3600);
                
                return result;
            } catch (Exception e) {
                log.error("{}单词查询失败，尝试降级: {}", provider.getName(), e.getMessage());
            }
        }

        throw new BizException(ResultCode.AI_SERVICE_ERROR, "单词查询服务暂不可用");
    }
}
