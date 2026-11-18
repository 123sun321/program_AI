/**
 * @author Juci
 * @date 2025/4/22 11:31
 */

import request from '@/utils/request'

export function getUserInfoByUserId(userid) {
  return request({
    url: '^/account/userbyid',
    method: 'get',
    params: {
      userid
    }
  })
}
/**
 * 获取浏览记录
 * @author Juci
 * @param {Object} data
 */
export function getUserRecord(data) {
  return request({
    url: '^/AppMgr/userrecord/page',
    method: 'post',
    data
  })
}
/**
 * 通过学生Id获取浏览记录
 * @author myc
 * @param {Object} data
 */
export function getUserRecordByStudentId(studentId, data) {
  return request({
    url: '^/AppMgr/userrecord/pageByStudentId?studentId=' + studentId,
    method: 'post',
    data
  })
}
/**
 * 通过用户Id获取积分
 * @author yyy
 * @param {Object} data
 */
export function getstudentPersonScore(userId) {
  return request({
    url: '^/AppMgr/integral-score/studentPersonScore?userId=' + userId,
    method: 'get'
  })
}
