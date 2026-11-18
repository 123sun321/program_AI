/**
 * @file 学生课程详情相关
 * @author 宋乃乐
 * @copyright zhengzhouchuanghzi
 * @since 2019.4.23
 */
import request from '@/utils/request'

/**
 * @method
 * @param {String} id
 * @description 课程详情
 */
export function getCourseDetail(id) {
  return request({
    url: '^/AppMgr/course/get',
    method: 'GET',
    params: {
      id
    }
  })
}
