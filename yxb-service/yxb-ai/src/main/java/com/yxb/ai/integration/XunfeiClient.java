package com.yxb.ai.integration;

import com.yxb.ai.config.AIProviderConfig;
import com.yxb.api.ai.dto.PronunciationScoreDTO;
import com.yxb.common.core.exception.BizException;
import com.yxb.common.core.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 讯飞AI客户端
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class XunfeiClient {

    private final AIProviderConfig config;

    /**
     * 语音识别(ASR)
     */
    public String recognize(byte[] audioData, String language) {
        AIProviderConfig.XunfeiConfig xunfeiConfig = config.getXunfei();
        if (xunfeiConfig == null || !xunfeiConfig.isEnabled()) {
            throw new BizException(ResultCode.AI_SERVICE_ERROR, "讯飞服务未配置");
        }

        // TODO: 实现讯飞语音识别WebSocket调用
        // 这里需要实现WebSocket连接到讯飞ASR API
        log.info("调用讯飞语音识别, 音频大小: {} bytes, 语言: {}", audioData.length, language);
        
        throw new BizException("讯飞ASR接口待实现");
    }

    /**
     * 发音评测
     */
    public PronunciationScoreDTO evaluatePronunciation(byte[] audioData, String text, String language) {
        AIProviderConfig.XunfeiConfig xunfeiConfig = config.getXunfei();
        if (xunfeiConfig == null || !xunfeiConfig.isEnabled()) {
            throw new BizException(ResultCode.AI_SERVICE_ERROR, "讯飞服务未配置");
        }

        // TODO: 实现讯飞口语评测WebSocket调用
        log.info("调用讯飞口语评测, 文本: {}, 语言: {}", text, language);

        // 模拟返回评测结果
        PronunciationScoreDTO result = new PronunciationScoreDTO();
        result.setTotalScore(85);
        result.setAccuracyScore(88);
        result.setFluencyScore(82);
        result.setIntonationScore(85);
        result.setCompletenessScore(90);
        result.setReferenceText(text);
        result.setRecognizedText(text);
        result.setWordDetails(new ArrayList<>());
        result.setSuggestions(Arrays.asList(
                "注意单词重音的位置",
                "语速可以稍微放慢一些"
        ));

        return result;
    }

    /**
     * 生成讯飞API签名
     */
    private String generateSignature(String apiKey, String apiSecret) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = sdf.format(new Date());

        String signatureOrigin = "host: iat-api.xfyun.cn\n" +
                "date: " + date + "\n" +
                "GET /v2/iat HTTP/1.1";

        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(signatureOrigin.getBytes(StandardCharsets.UTF_8));
        String sha = Base64.getEncoder().encodeToString(hexDigits);

        String authorization = String.format(
                "api_key=\"%s\", algorithm=\"hmac-sha256\", headers=\"host date request-line\", signature=\"%s\"",
                apiKey, sha
        );

        return URLEncoder.encode(Base64.getEncoder().encodeToString(authorization.getBytes()), StandardCharsets.UTF_8);
    }
}
