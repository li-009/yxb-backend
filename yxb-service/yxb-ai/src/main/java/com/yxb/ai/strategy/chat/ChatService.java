package com.yxb.ai.strategy.chat;

import com.yxb.ai.strategy.AIProvider;

/**
 * AI聊天服务接口
 */
public interface ChatService {

    /**
     * 获取服务商
     */
    AIProvider getProvider();

    /**
     * 聊天问答
     *
     * @param question 问题
     * @param context  上下文（可选）
     * @return 回答
     */
    String chat(String question, String context);

    /**
     * 检查服务是否可用
     */
    boolean isAvailable();
}
