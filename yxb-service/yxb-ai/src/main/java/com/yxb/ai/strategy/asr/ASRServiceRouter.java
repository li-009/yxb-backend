package com.yxb.ai.strategy.asr;

import com.yxb.ai.config.AIProviderConfig;
import com.yxb.ai.strategy.AIProvider;
import com.yxb.common.core.exception.BizException;
import com.yxb.common.core.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 语音识别服务路由（策略模式 + 降级机制）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ASRServiceRouter {

    private final AIProviderConfig config;
    private final List<ASRService> services;

    private Map<AIProvider, ASRService> serviceMap;

    private void initServiceMap() {
        if (serviceMap == null) {
            serviceMap = new HashMap<>();
            for (ASRService service : services) {
                serviceMap.put(service.getProvider(), service);
            }
        }
    }

    /**
     * 执行语音识别（带降级）
     */
    public String recognize(byte[] audioData, String language) {
        initServiceMap();

        List<String> providerCodes = config.getProvider().getAsr();

        for (String code : providerCodes) {
            AIProvider provider = AIProvider.fromCode(code);
            if (provider == null) {
                continue;
            }

            ASRService service = serviceMap.get(provider);
            if (service == null || !service.isAvailable()) {
                log.warn("ASR服务不可用: {}", provider.getName());
                continue;
            }

            try {
                log.info("使用{}进行语音识别", provider.getName());
                return service.recognize(audioData, language);
            } catch (Exception e) {
                log.error("{}语音识别失败，尝试降级: {}", provider.getName(), e.getMessage());
            }
        }

        throw new BizException(ResultCode.AI_SERVICE_ERROR, "语音识别服务暂不可用");
    }
}
