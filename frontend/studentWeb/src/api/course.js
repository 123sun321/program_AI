
/**
 * @file 课程相关交互
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/3/27
 */

import request from '@/utils/request'
/**
 * @method
 * @param {Object} data
 * @description 分页获取公开课程
 */
export function page(data) {
  return request({
    url: '^/AppMgr/course/publicPage',
    method: 'post',
    data
  })
}
