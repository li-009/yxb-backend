package com.yxb.ai.strategy.pronunciation;

import com.yxb.ai.strategy.AIProvider;
import com.yxb.api.ai.dto.PronunciationScoreDTO;

/**
 * 发音评分器接口
 */
public interface PronunciationScorer {

    /**
     * 获取服务商
     */
    AIProvider getProvider();

    /**
     * 评分发音
     *
     * @param audioData 音频数据
     * @param text      参考文本
     * @param language  语言代码
     * @return 评分结果
     */
    PronunciationScoreDTO score(byte[] audioData, String text, String language);

    /**
     * 检查服务是否可用
     */
    boolean isAvailable();
}
