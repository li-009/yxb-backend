import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 30000
})

export const login = (phone, password) => 
  api.post('/auth/login', { phone, password, loginType: 'phone' })

export const register = (phone, password, code) => 
  api.post('/auth/register', { phone, password, verifyCode: code })

export const refreshToken = () => api.post('/auth/refresh')

export const logout = () => api.post('/auth/logout')

export const getUserInfo = () => 
  api.get('/user/info')

export const wechatMiniappLogin = (code) =>
  api.post('/wechat/miniapp/login', { code })

export const getWechatJssdkSign = (url) =>
  api.get('/wechat/mp/jssdk/sign', { params: { url } })

export const updateUserInfo = (data) => api.put('/user/info', data)

export const getStudyStats = () => api.get('/user/study-stats')
