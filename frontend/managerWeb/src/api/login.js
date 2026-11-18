import request from '@/utils/request'

export function loginByUsername(data) {
  return request({
    url: '^/account/login',
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
/**
 * 获取学校Logo
 * @param {Number} id
 */
export function getSchoolLogo() {
  return request({
    url: '^/account/getSchoolLogo',
    method: 'get'
  })
}
