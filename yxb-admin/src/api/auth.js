import request from '@/utils/request'

export function login(username, password) {
  return request({
    url: '/api/auth/admin/login',
    method: 'post',
    data: { username, password }
  })
}

export function logout() {
  return request({
    url: '/api/auth/admin/logout',
    method: 'post'
  })
}

export function getUserInfo() {
  return request({
    url: '/api/auth/admin/info',
    method: 'get'
  })
}
