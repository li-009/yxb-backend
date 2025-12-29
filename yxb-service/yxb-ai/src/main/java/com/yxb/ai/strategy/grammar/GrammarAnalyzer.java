package com.yxb.ai.strategy.grammar;

import com.yxb.ai.strategy.AIProvider;
import com.yxb.api.ai.dto.GrammarAnalysisDTO;

/**
 * 语法分析器接口（策略模式）
 */
public interface GrammarAnalyzer {

    /**
     * 获取服务商
     */
    AIProvider getProvider();

    /**
     * 分析语法
     *
     * @param sentence 句子
     * @param language 语言代码
     * @return 语法分析结果
     */
    GrammarAnalysisDTO analyze(String sentence, String language);

    /**
     * 检查服务是否可用
     */
    boolean isAvailable();
}
