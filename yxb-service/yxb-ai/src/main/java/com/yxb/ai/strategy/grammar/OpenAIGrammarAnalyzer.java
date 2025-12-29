package com.yxb.ai.strategy.grammar;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yxb.ai.config.AIProviderConfig;
import com.yxb.ai.integration.OpenAIClient;
import com.yxb.ai.strategy.AIProvider;
import com.yxb.api.ai.dto.GrammarAnalysisDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenAI语法分析器实现
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAIGrammarAnalyzer implements GrammarAnalyzer {

    private final AIProviderConfig config;
    private final OpenAIClient openAIClient;

    private static final String GRAMMAR_PROMPT_TEMPLATE = """
        你是一名专业的%s语法老师。请分析以下句子的语法结构，并以JSON格式返回结果。
        
        句子："%s"
        
        请按以下JSON格式返回（不要返回其他内容）：
        {
            "components": [
                {"text": "词语", "type": "成分类型(主语/谓语/宾语/定语/状语)", "partOfSpeech": "词性"}
            ],
            "grammarPoints": [
                {"name": "语法点名称", "rule": "语法规则说明", "examples": ["例句1", "例句2"]}
            ],
            "keyWords": [
                {"word": "单词", "partOfSpeech": "词性", "meaning": "中文释义", "phonetic": "音标"}
            ],
            "synonymousSentences": ["同义句1", "同义句2"],
            "commonMistakes": ["易错点1", "易错点2"]
        }
        """;

    @Override
    public AIProvider getProvider() {
        return AIProvider.OPENAI;
    }

    @Override
    public GrammarAnalysisDTO analyze(String sentence, String language) {
        String languageName = getLanguageName(language);
        String prompt = String.format(GRAMMAR_PROMPT_TEMPLATE, languageName, sentence);
        
        log.info("调用OpenAI进行语法分析, 语言: {}, 句子: {}", language, sentence);
        
        String response = openAIClient.chat(prompt);
        
        return parseResponse(sentence, response);
    }

    @Override
    public boolean isAvailable() {
        return config.getOpenai() != null && config.getOpenai().isEnabled();
    }

    private String getLanguageName(String language) {
        return switch (language.toLowerCase()) {
            case "en-us", "en" -> "英语";
            case "ja-jp", "ja" -> "日语";
            case "ko-kr", "ko" -> "韩语";
            case "fr-fr", "fr" -> "法语";
            case "de-de", "de" -> "德语";
            default -> "英语";
        };
    }

    private GrammarAnalysisDTO parseResponse(String sentence, String response) {
        GrammarAnalysisDTO result = new GrammarAnalysisDTO();
        result.setSentence(sentence);

        try {
            // 提取JSON部分
            String jsonStr = extractJson(response);
            JSONObject json = JSONUtil.parseObj(jsonStr);

            // 解析句子成分
            JSONArray componentsArr = json.getJSONArray("components");
            if (componentsArr != null) {
                List<GrammarAnalysisDTO.SentenceComponent> components = new ArrayList<>();
                for (int i = 0; i < componentsArr.size(); i++) {
                    JSONObject item = componentsArr.getJSONObject(i);
                    GrammarAnalysisDTO.SentenceComponent component = new GrammarAnalysisDTO.SentenceComponent();
                    component.setText(item.getStr("text"));
                    component.setType(item.getStr("type"));
                    component.setPartOfSpeech(item.getStr("partOfSpeech"));
                    components.add(component);
                }
                result.setComponents(components);
            }

            // 解析语法点
            JSONArray grammarPointsArr = json.getJSONArray("grammarPoints");
            if (grammarPointsArr != null) {
                List<GrammarAnalysisDTO.GrammarPoint> grammarPoints = new ArrayList<>();
                for (int i = 0; i < grammarPointsArr.size(); i++) {
                    JSONObject item = grammarPointsArr.getJSONObject(i);
                    GrammarAnalysisDTO.GrammarPoint point = new GrammarAnalysisDTO.GrammarPoint();
                    point.setName(item.getStr("name"));
                    point.setRule(item.getStr("rule"));
                    point.setExamples(item.getJSONArray("examples").toList(String.class));
                    grammarPoints.add(point);
                }
                result.setGrammarPoints(grammarPoints);
            }

            // 解析重点词汇
            JSONArray keyWordsArr = json.getJSONArray("keyWords");
            if (keyWordsArr != null) {
                List<GrammarAnalysisDTO.KeyWord> keyWords = new ArrayList<>();
                for (int i = 0; i < keyWordsArr.size(); i++) {
                    JSONObject item = keyWordsArr.getJSONObject(i);
                    GrammarAnalysisDTO.KeyWord keyWord = new GrammarAnalysisDTO.KeyWord();
                    keyWord.setWord(item.getStr("word"));
                    keyWord.setPartOfSpeech(item.getStr("partOfSpeech"));
                    keyWord.setMeaning(item.getStr("meaning"));
                    keyWord.setPhonetic(item.getStr("phonetic"));
                    keyWords.add(keyWord);
                }
                result.setKeyWords(keyWords);
            }

            // 解析同义句和易错点
            JSONArray synonymousArr = json.getJSONArray("synonymousSentences");
            if (synonymousArr != null) {
                result.setSynonymousSentences(synonymousArr.toList(String.class));
            }

            JSONArray mistakesArr = json.getJSONArray("commonMistakes");
            if (mistakesArr != null) {
                result.setCommonMistakes(mistakesArr.toList(String.class));
            }

        } catch (Exception e) {
            log.error("解析OpenAI语法分析响应失败", e);
            // 返回基本结果
            result.setComponents(new ArrayList<>());
            result.setGrammarPoints(new ArrayList<>());
            result.setKeyWords(new ArrayList<>());
        }

        return result;
    }

    private String extractJson(String response) {
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");
        if (start >= 0 && end > start) {
            return response.substring(start, end + 1);
        }
        return response;
    }
}
