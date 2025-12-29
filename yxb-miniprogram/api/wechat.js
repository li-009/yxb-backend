import { request } from '../utils/request'

export function miniappLogin(code) {
  return request({
    url: '/api/wechat/miniapp/login',
    method: 'POST',
    data: { code }
  })
}

export function miniappPhone(code) {
  return request({
    url: '/api/wechat/miniapp/phone',
    method: 'POST',
    data: { code }
  })
}
