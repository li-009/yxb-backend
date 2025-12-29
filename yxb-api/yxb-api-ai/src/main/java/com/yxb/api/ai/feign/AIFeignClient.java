package com.yxb.api.ai.feign;

import com.yxb.api.ai.dto.GrammarAnalysisDTO;
import com.yxb.api.ai.dto.PronunciationScoreDTO;
import com.yxb.api.ai.dto.WordLookupDTO;
import com.yxb.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * AI服务Feign客户端
 */
@FeignClient(name = "yxb-ai", contextId = "aiFeignClient", path = "/inner/ai")
public interface AIFeignClient {

    /**
     * 语法分析
     */
    @PostMapping("/grammar/analyze")
    Result<GrammarAnalysisDTO> analyzeGrammar(@RequestParam("sentence") String sentence,
                                               @RequestParam("language") String language);

    /**
     * 单词查询
     */
    @GetMapping("/word/lookup")
    Result<WordLookupDTO> lookupWord(@RequestParam("word") String word,
                                      @RequestParam("language") String language);

    /**
     * 发音评分
     */
    @PostMapping(value = "/pronunciation/score", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<PronunciationScoreDTO> scorePronunciation(@RequestPart("audio") MultipartFile audio,
                                                      @RequestParam("text") String text,
                                                      @RequestParam("language") String language);

    /**
     * AI问答
     */
    @PostMapping("/chat")
    Result<String> chat(@RequestParam("question") String question,
                        @RequestParam(value = "context", required = false) String context);

    /**
     * 生成字幕
     */
    @PostMapping(value = "/subtitle/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<String> generateSubtitle(@RequestPart("audio") MultipartFile audio,
                                     @RequestParam("language") String language);
}
