package com.yxb.ai.strategy.word;

import com.yxb.ai.strategy.AIProvider;
import com.yxb.api.ai.dto.WordLookupDTO;

/**
 * 单词查询服务接口
 */
public interface WordLookupService {

    /**
     * 获取服务商
     */
    AIProvider getProvider();

    /**
     * 查询单词
     *
     * @param word     单词
     * @param language 语言代码
     * @return 单词详情
     */
    WordLookupDTO lookup(String word, String language);

    /**
     * 检查服务是否可用
     */
    boolean isAvailable();
}
