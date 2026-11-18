/**
 * @file 学生课程库相关
 * @author 宋乃乐
 * @copyright zhengzhouchuanghzi
 * @since 2019.4.18
 */
import request from '@/utils/request'

/**
 * @method
 * @param {Object} data
 * @description 课程分页
 */
export function getCoursePage(data) {
  return request({
    url: '/course/stuWebPage',
    method: 'POST',
    data
  })
}
/**
 * 获取当前用户得课程
 */
export function getNow() {
  return request({
    url: '^/AppMgr/course/getNow',
    method: 'get'
  })
}
