package com.yxb.ai.strategy.chat;

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
 * 聊天服务路由（策略模式 + 降级机制）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatServiceRouter {

    private final AIProviderConfig config;
    private final List<ChatService> services;

    private Map<AIProvider, ChatService> serviceMap;

    private void initServiceMap() {
        if (serviceMap == null) {
            serviceMap = new HashMap<>();
            for (ChatService service : services) {
                serviceMap.put(service.getProvider(), service);
            }
        }
    }

    /**
     * 执行聊天问答（带降级）
     */
    public String chat(String question, String context) {
        initServiceMap();

        List<String> providerCodes = config.getProvider().getChat();

        for (String code : providerCodes) {
            AIProvider provider = AIProvider.fromCode(code);
            if (provider == null) {
                continue;
            }

            ChatService service = serviceMap.get(provider);
            if (service == null || !service.isAvailable()) {
                log.warn("聊天服务不可用: {}", provider.getName());
                continue;
            }

            try {
                log.info("使用{}进行聊天问答", provider.getName());
                return service.chat(question, context);
            } catch (Exception e) {
                log.error("{}聊天服务失败，尝试降级: {}", provider.getName(), e.getMessage());
            }
        }

        throw new BizException(ResultCode.AI_SERVICE_ERROR, "AI问答服务暂不可用");
    }
}
