import { request } from '../utils/request'

// 更新学习进度
export function updateProgress(data) {
  return request({
    url: '/api/study/progress/update',
    method: 'POST',
    data
  })
}

// 获取学习进度列表
export function getProgressList(params) {
  return request({
    url: '/api/study/progress/page',
    method: 'GET',
    data: params
  })
}

// 添加生词
export function addWord(data) {
  return request({
    url: '/api/study/word/add',
    method: 'POST',
    data
  })
}

// 获取单词本
export function getWordList(params) {
  return request({
    url: '/api/study/word/page',
    method: 'GET',
    data: params
  })
}

// 获取待复习单词
export function getReviewWords(limit = 20) {
  return request({
    url: '/api/study/word/review',
    method: 'GET',
    data: { limit }
  })
}

// 更新单词掌握状态
export function updateWordMastery(id, masteryStatus) {
  return request({
    url: `/api/study/word/${id}/mastery`,
    method: 'POST',
    data: { masteryStatus }
  })
}
