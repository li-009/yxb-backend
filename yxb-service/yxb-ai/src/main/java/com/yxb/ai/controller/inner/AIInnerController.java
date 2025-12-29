package com.yxb.ai.controller.inner;

import com.yxb.ai.biz.AIBiz;
import com.yxb.api.ai.dto.GrammarAnalysisDTO;
import com.yxb.api.ai.dto.PronunciationScoreDTO;
import com.yxb.api.ai.dto.WordLookupDTO;
import com.yxb.common.core.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * AI内部接口（服务间调用）
 */
@Tag(name = "AI内部接口")
@RestController
@RequestMapping("/inner/ai")
@RequiredArgsConstructor
public class AIInnerController {

    private final AIBiz aiBiz;

    @Operation(summary = "语法分析")
    @PostMapping("/grammar/analyze")
    public Result<GrammarAnalysisDTO> analyzeGrammar(@RequestParam String sentence, @RequestParam String language) {
        return Result.success(aiBiz.analyzeGrammar(sentence, language));
    }

    @Operation(summary = "单词查询")
    @GetMapping("/word/lookup")
    public Result<WordLookupDTO> lookupWord(@RequestParam String word, @RequestParam String language) {
        return Result.success(aiBiz.lookupWord(word, language));
    }

    @Operation(summary = "发音评分")
    @PostMapping("/pronunciation/score")
    public Result<PronunciationScoreDTO> scorePronunciation(@RequestPart("audio") MultipartFile audio,
                                                            @RequestParam String text,
                                                            @RequestParam String language) throws Exception {
        return Result.success(aiBiz.scorePronunciation(audio.getBytes(), text, language));
    }

    @Operation(summary = "AI问答")
    @PostMapping("/chat")
    public Result<String> chat(@RequestParam String question,
                               @RequestParam(required = false) String context) {
        return Result.success(aiBiz.chat(question, context));
    }

    @Operation(summary = "生成字幕")
    @PostMapping("/subtitle/generate")
    public Result<String> generateSubtitle(@RequestPart("audio") MultipartFile audio,
                                           @RequestParam String language) throws Exception {
        return Result.success(aiBiz.generateSubtitle(audio.getBytes(), language));
    }
}
