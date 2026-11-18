/**
 * @file 用户个人资料api
 * @author 张向宇
 * @copyright zhengzhouchuanghzi
 * @since 2025/01/10
 */
import request from '@/utils/request'
import qs from 'qs'

/**
 * @method
 * @description 修改用户个人资料
 */
export function updateUserPersonInfo(data) {
  return request({
    url: '^/account/updateUserInfo',
    method: 'post',
    data: data
  })
}

/**
 * @method
 * @description 修改用户个人资料
 */
export function ubBindWeiXin() {
  return request({
    url: '^/account/unbindWeiXin',
    method: 'get'
  })
}

/**
 * @method
 * @description 发送绑定手机号短信验证码
 */
export function sendBindPhoneCaptcha(phone, picCaptcha) {
  const postData = qs.stringify({
    phone: phone,
    picCaptcha: picCaptcha
  })
  return request({
    url: '^/account/bindPhone/sendPhoneCaptcha',
    method: 'post',
    data: postData,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}
/**
 * @method
 * @description 绑定手机号保存
 */
export function bindPhoneSave(phone, code) {
  const postData = qs.stringify({
    phone: phone,
    code: code
  })
  return request({
    url: '^/account/bindPhone/save',
    method: 'post',
    data: postData,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}

/**
 * @method
 * @description 发送绑定邮箱验证码
 */
export function sendBindEmailCaptcha(email, picCaptcha) {
  const postData = qs.stringify({
    email: email,
    picCaptcha: picCaptcha
  })
  return request({
    url: '^/account/bindEmail/sendCaptcha',
    method: 'post',
    data: postData,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}
/**
 * @method
 * @description 绑定邮箱保存
 */
export function bindEmailSave(email, code) {
  const postData = qs.stringify({
    email: email,
    code: code
  })
  return request({
    url: '^/account/bindEmail/save',
    method: 'post',
    data: postData,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}
