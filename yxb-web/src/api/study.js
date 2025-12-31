import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 30000
})

export const updateProgress = (videoId, position, watchedDuration) => 
  api.post('/study/progress/update', null, { params: { videoId, position, watchedDuration } })

export const getProgressList = (pageNum = 1, pageSize = 10) => 
  api.get('/study/progress/page', { params: { pageNum, pageSize } })

export const addWord = (word) => api.post('/study/word/add', word)

export const getWordList = (masteryStatus, pageNum = 1, pageSize = 20) => 
  api.get('/study/word/page', { params: { masteryStatus, pageNum, pageSize } })

export const getReviewWords = (limit = 20) => 
  api.get('/study/word/review', { params: { limit } })

export const updateWordMastery = (id, masteryStatus) => 
  api.post(`/study/word/${id}/mastery`, null, { params: { masteryStatus } })

export const addNote = (note) => api.post('/study/note/add', note)

export const getNoteList = (videoId, pageNum = 1, pageSize = 20) => 
  api.get('/study/note/page', { params: { videoId, pageNum, pageSize } })

export const deleteNote = (id) => api.delete(`/study/note/${id}`)

export const getNoteCount = () => api.get('/study/note/count')

export const collectVideo = (videoId) => api.post(`/study/collect/${videoId}`)

export const uncollectVideo = (videoId) => api.delete(`/study/collect/${videoId}`)

export const isVideoCollected = (videoId) => api.get(`/study/collect/${videoId}/check`)

export const getCollectedVideos = (pageNum = 1, pageSize = 20) => 
  api.get('/study/collect/page', { params: { pageNum, pageSize } })

export const getCollectCount = () => api.get('/study/collect/count')
