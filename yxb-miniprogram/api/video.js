import { request } from '../utils/request'

// 获取视频列表
export function getVideoList(params) {
  return request({
    url: '/api/video/page',
    method: 'GET',
    data: params
  })
}

// 获取视频详情
export function getVideoDetail(id) {
  return request({
    url: `/api/video/${id}`,
    method: 'GET'
  })
}

// 获取视频字幕
export function getVideoSubtitles(id) {
  return request({
    url: `/api/video/${id}/subtitles`,
    method: 'GET'
  })
}

// 获取指定语言字幕
export function getVideoSubtitle(id, language) {
  return request({
    url: `/api/video/${id}/subtitle`,
    method: 'GET',
    data: { language }
  })
}

// 增加播放次数
export function playVideo(id) {
  return request({
    url: `/api/video/${id}/play`,
    method: 'POST'
  })
}
