/**
 * @file 签到管理交互
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2018/3/21
 */
import request from '@/utils/request'
/**
 * @method
 * @param {Object} data
 * @description 签到详情分页
 */
export function findPage(data) {
  return request({
    url: '^/AppMgr/SignRecord/findpage',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 签到统计
 */
export function cumul(startime, endtime) {
  return request({
    url: '^/AppMgr/SignRecord/cumulativeSign?starTime=' + startime + '&endTime=' + endtime,
    method: 'get'

  })
}
/**
 * @method
 * @param {Object} id
 * @description 删除签到记录
 */
export function dele(id) {
  return request({
    url: '^/AppMgr/SignRecord/delete?id=' + id,
    method: 'get'
  })
}

