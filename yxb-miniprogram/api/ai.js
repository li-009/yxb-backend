import { request } from '../utils/request'

// 语法分析
export function analyzeGrammar(sentence, language) {
  return request({
    url: '/api/ai/grammar/analyze',
    method: 'POST',
    data: { sentence, language }
  })
}

// 单词查询
export function lookupWord(word, language) {
  return request({
    url: '/api/ai/word/lookup',
    method: 'GET',
    data: { word, language }
  })
}

// AI问答
export function aiChat(question, context) {
  return request({
    url: '/api/ai/chat',
    method: 'POST',
    data: { question, context }
  })
}
