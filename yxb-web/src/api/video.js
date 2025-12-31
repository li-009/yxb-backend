import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 30000
})

export const getVideoList = (params) => api.get('/video/page', { params })

export const getVideoDetail = (id) => api.get(`/video/${id}`)

export const getVideoSubtitles = (videoId) => {
  return api.get(`/video/${videoId}/subtitles`)
}

export const searchVideos = (keyword, params = {}) => {
  return api.get('/video/search', { params: { keyword, ...params } })
}

export const getVideoSubtitle = (id, language) => api.get(`/video/${id}/subtitle`, { params: { language } })

export const playVideo = (id) => api.post(`/video/${id}/play`)

export const importVideoByUrl = (data) => api.post('/video/import/url', data)

export const uploadVideo = (file, title, description, language, level) => {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('title', title)
  formData.append('description', description || '')
  formData.append('language', language || 'en')
  formData.append('level', level || 1)
  return api.post('/video/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
