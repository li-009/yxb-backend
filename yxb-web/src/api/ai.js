import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 60000
})

export const analyzeGrammar = (sentence, language = 'en') => 
  api.post('/ai/grammar/analyze', null, { params: { sentence, language } })

export const lookupWord = (word, language = 'en') => 
  api.get('/ai/word/lookup', { params: { word, language } })

export const scorePronunciation = (audioBlob, text, language = 'en') => {
  const formData = new FormData()
  formData.append('audio', audioBlob, 'audio.wav')
  formData.append('text', text)
  formData.append('language', language)
  return api.post('/ai/pronunciation/score', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export const chat = (question, context = '') => 
  api.post('/ai/chat', null, { params: { question, context } })
