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
 * @description 课 程详情
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

/**
 * @method
 * @param {String} id
 * @description 相似课程
 */
export function getCourseRecommend(id) {
  return request({
    url: '^/AppMgr/course/courseRecommend',
    method: 'GET',
    params: {
      id
    }
  })
}

/**
 * @method
 * @param {Object} data
 * @description 评价分页
 */
export function getCourseComment(data) {
  return request({
    url: '^AppMgr/comment/selStuCourseCommentPage',
    method: 'POST',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 获取实验列表
 */
export function getExercisesByCourseId(courseId) {
  return request({
    url: '^AppMgr/exerciseJoin/getListByCourseId',
    method: 'GET',
    params: {
      courseId
    }
  })
}
/**
 * @method
 * @param {Object} courseId
 * @description 判断当前课程是否收藏了，true、false
 */
export function judgeCourseIsCollected(courseId) {
  return request({
    url: '^AppMgr/course/judgeCourseIsCollected',
    method: 'GET',
    params: {
      courseId
    }
  })
}
