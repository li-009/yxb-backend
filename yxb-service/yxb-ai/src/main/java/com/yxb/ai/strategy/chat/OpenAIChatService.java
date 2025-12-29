package com.yxb.ai.strategy.chat;

import cn.hutool.core.util.StrUtil;
import com.yxb.ai.config.AIProviderConfig;
import com.yxb.ai.integration.OpenAIClient;
import com.yxb.ai.strategy.AIProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * OpenAI聊天服务实现
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAIChatService implements ChatService {

    private final AIProviderConfig config;
    private final OpenAIClient openAIClient;

    private static final String SYSTEM_PROMPT = """
        你是一个专业的外语学习助手，名叫"小鹦"。你的职责是：
        1. 解答用户的外语学习问题（语法、词汇、翻译等）
        2. 提供学习建议和方法
        3. 用简洁易懂的方式解释复杂的语法规则
        4. 鼓励用户坚持学习
        
        请用友好、专业的语气回答问题。如果问题涉及多个知识点，请分条列出。
        """;

    @Override
    public AIProvider getProvider() {
        return AIProvider.OPENAI;
    }

    @Override
    public String chat(String question, String context) {
        log.info("调用OpenAI进行问答, 问题: {}", question);
        
        StringBuilder prompt = new StringBuilder();
        prompt.append(SYSTEM_PROMPT).append("\n\n");
        
        if (StrUtil.isNotBlank(context)) {
            prompt.append("上下文信息：\n").append(context).append("\n\n");
        }
        
        prompt.append("用户问题：").append(question);
        
        return openAIClient.chat(prompt.toString());
    }

    @Override
    public boolean isAvailable() {
        return config.getOpenai() != null && config.getOpenai().isEnabled();
    }
}
