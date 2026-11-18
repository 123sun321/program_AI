import request from '@/utils/request'

export function loginByUsername(data) {
  return request({
    url: '^/account/studentlogin',
    method: 'post',
    data
  })
}

export function changePassword(data) {
  return request({
    url: '^/account/changePassword',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '^/account/logout',
    method: 'get'
  })
}
export function getUserInfo() {
  return request({
    url: '^/account/userinfo',
    method: 'get'
  })
}
/**
 * 判断用户是否登录
 */
export function jugeLogin() {
  return request({
    url: '^/account/jugeLogin',
    method: 'get'
  })
}
