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
 * @description 课 程分页
 */
export function getCoursePage(data) {
  return request({
    url: '/course/stuWebPage',
    method: 'POST',
    data
  })
}

/**
 * @method
 * @param {Number} courseid
 * @description 添加收藏
 */
export function addCollect(courseid) {
  return request({
    url: '^/AppMgr/coursecollect/add',
    method: 'GET',
    params: {
      courseid
    }
  })
}

/**
 * @method
 * @param {Number} courseid
 * @description 删除收藏
 */
export function delCollect(courseId) {
  return request({
    url: '^AppMgr/coursecollect/delByCourseId',
    method: 'GET',
    params: {
      courseId
    }
  })
}

/**
 * @method
 * @description 获取标签列表
 */
export function getLabelList(code) {
  return request({
    url: '^AppMgr/course/labelToCourseCache',
    method: 'GET',
    params: {
      techType: code
    }
  })
}
