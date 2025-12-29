package com.yxb.ai.integration;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import com.yxb.ai.config.AIProviderConfig;
import com.yxb.common.core.exception.BizException;
import com.yxb.common.core.result.ResultCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * OpenAI客户端
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAIClient {

    private final AIProviderConfig config;
    
    private OpenAiService openAiService;

    @PostConstruct
    public void init() {
        AIProviderConfig.OpenAIConfig openaiConfig = config.getOpenai();
        if (openaiConfig != null && openaiConfig.isEnabled() && openaiConfig.getApiKey() != null) {
            try {
                openAiService = new OpenAiService(
                        openaiConfig.getApiKey(),
                        Duration.ofSeconds(openaiConfig.getTimeout())
                );
                log.info("OpenAI客户端初始化成功");
            } catch (Exception e) {
                log.error("OpenAI客户端初始化失败", e);
            }
        }
    }

    /**
     * 发送聊天请求
     */
    public String chat(String userMessage) {
        if (openAiService == null) {
            throw new BizException(ResultCode.AI_SERVICE_ERROR, "OpenAI服务未初始化");
        }

        try {
            List<ChatMessage> messages = new ArrayList<>();
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), userMessage));

            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(config.getOpenai().getModel())
                    .messages(messages)
                    .maxTokens(2000)
                    .temperature(0.7)
                    .build();

            String response = openAiService.createChatCompletion(request)
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();

            log.debug("OpenAI响应: {}", response);
            return response;
        } catch (Exception e) {
            log.error("OpenAI调用失败", e);
            throw new BizException(ResultCode.AI_SERVICE_ERROR, "AI服务调用失败: " + e.getMessage());
        }
    }

    /**
     * 发送带系统消息的聊天请求
     */
    public String chat(String systemMessage, String userMessage) {
        if (openAiService == null) {
            throw new BizException(ResultCode.AI_SERVICE_ERROR, "OpenAI服务未初始化");
        }

        try {
            List<ChatMessage> messages = new ArrayList<>();
            messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemMessage));
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), userMessage));

            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(config.getOpenai().getModel())
                    .messages(messages)
                    .maxTokens(2000)
                    .temperature(0.7)
                    .build();

            return openAiService.createChatCompletion(request)
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();
        } catch (Exception e) {
            log.error("OpenAI调用失败", e);
            throw new BizException(ResultCode.AI_SERVICE_ERROR, "AI服务调用失败: " + e.getMessage());
        }
    }
}
