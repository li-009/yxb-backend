package com.yxb.ai.biz;

import com.yxb.ai.strategy.asr.ASRServiceRouter;
import com.yxb.ai.strategy.chat.ChatServiceRouter;
import com.yxb.ai.strategy.grammar.GrammarAnalyzerRouter;
import com.yxb.ai.strategy.pronunciation.PronunciationScorerRouter;
import com.yxb.ai.strategy.word.WordLookupRouter;
import com.yxb.api.ai.dto.GrammarAnalysisDTO;
import com.yxb.api.ai.dto.PronunciationScoreDTO;
import com.yxb.api.ai.dto.WordLookupDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * AI业务层 - 统一管理AI相关业务逻辑
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AIBiz {

    private final GrammarAnalyzerRouter grammarAnalyzerRouter;
    private final ASRServiceRouter asrServiceRouter;
    private final PronunciationScorerRouter pronunciationScorerRouter;
    private final WordLookupRouter wordLookupRouter;
    private final ChatServiceRouter chatServiceRouter;

    public GrammarAnalysisDTO analyzeGrammar(String sentence, String language) {
        log.info("语法分析请求: 句子={}, 语言={}", sentence, language);
        return grammarAnalyzerRouter.analyze(sentence, language);
    }

    public String recognizeSpeech(byte[] audioData, String language) {
        log.info("语音识别请求: 音频大小={} bytes, 语言={}", audioData.length, language);
        return asrServiceRouter.recognize(audioData, language);
    }

    public PronunciationScoreDTO scorePronunciation(byte[] audioData, String text, String language) {
        log.info("发音评分请求: 文本={}, 语言={}", text, language);
        return pronunciationScorerRouter.score(audioData, text, language);
    }

    public WordLookupDTO lookupWord(String word, String language) {
        log.info("单词查询请求: 单词={}, 语言={}", word, language);
        return wordLookupRouter.lookup(word, language);
    }

    public String chat(String question, String context) {
        log.info("AI问答请求: 问题={}", question);
        return chatServiceRouter.chat(question, context);
    }

    public String generateSubtitle(byte[] audioData, String language) {
        log.info("字幕生成请求: 音频大小={} bytes, 语言={}", audioData.length, language);
        String text = asrServiceRouter.recognize(audioData, language);
        return formatToSRT(text);
    }

    private String formatToSRT(String text) {
        StringBuilder srt = new StringBuilder();
        String[] sentences = text.split("[.!?。！？]");
        int index = 1;
        int currentTime = 0;
        
        for (String sentence : sentences) {
            sentence = sentence.trim();
            if (sentence.isEmpty()) continue;
            
            int duration = Math.max(2000, sentence.length() * 100);
            srt.append(index++).append("\n");
            srt.append(formatTime(currentTime)).append(" --> ").append(formatTime(currentTime + duration)).append("\n");
            srt.append(sentence).append("\n\n");
            currentTime += duration + 200;
        }
        return srt.toString();
    }

    private String formatTime(int ms) {
        int hours = ms / 3600000;
        int minutes = (ms % 3600000) / 60000;
        int seconds = (ms % 60000) / 1000;
        int millis = ms % 1000;
        return String.format("%02d:%02d:%02d,%03d", hours, minutes, seconds, millis);
    }
}
