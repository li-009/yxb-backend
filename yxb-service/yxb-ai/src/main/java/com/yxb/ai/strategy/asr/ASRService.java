package com.yxb.ai.strategy.asr;

import com.yxb.ai.strategy.AIProvider;

/**
 * 语音识别(ASR)服务接口
 */
public interface ASRService {

    /**
     * 获取服务商
     */
    AIProvider getProvider();

    /**
     * 语音转文字
     *
     * @param audioData 音频数据
     * @param language  语言代码
     * @return 识别文本
     */
    String recognize(byte[] audioData, String language);

    /**
     * 检查服务是否可用
     */
    boolean isAvailable();
}
