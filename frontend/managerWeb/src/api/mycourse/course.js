
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
 * @description 分页
 */
export function studentJoinCourse(data) {
  return request({
    url: '^/AppMgr/courseJoin/studentJoinCourse',
    method: 'post',
    data
  })
}
/**
 * 通过邀请码加入课程
 * @param {String} code
 */
export function joincourse(code) {
  return request({
    url: '^/AppMgr/courseJoin/joincourse',
    method: 'get',
    params: {
      code
    }
  })
}

/**
 * @author myc
 * @param {Object} data
 * @description 获取学生作业列表
 */
export function getStudentTask(data) {
  return request({
    url: '^/AppMgr/courseJoin/studentJoinPrivateCourseTask',
    method: 'post',
    data
  })
}

/**
 * @author myc
 * @param {Object} data
 * @description 获取学生私有课程列表
 */
export function getStudentPrivateCourses(data) {
  return request({
    url: '^/AppMgr/courseJoin/studentJoinPrivateCourse',
    method: 'post',
    data
  })
}

/**
 * @method
 * @param {Object} data
 * @description 获取收藏课程分页
 */
export function collectPage(data) {
  return request({
    url: '^/AppMgr/coursecollect/page',
    method: 'post',
    data
  })
}
