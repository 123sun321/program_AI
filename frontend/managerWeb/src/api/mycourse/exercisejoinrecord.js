
/**
 * @file 实验参与详情统计相关交互
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/4/8
 */

import request from '@/utils/request'
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function statisticnumber(userId) {
  return request({
    url: '^/AppMgr/exercisejoinrecord/statisticnumber',
    method: 'get',
    params: {
      userId
    }
  })
}
/**
 * @author myc
 * @param {Object} data
 * @description 根据学生ID获取学生学习详情
 */
export function statisticNumberByStudentId(studentId) {
  return request({
    url: '^/AppMgr/exercisejoinrecord/statisticNumberByStudentId',
    method: 'get',
    params: {
      studentId
    }
  })
}
/**
 * @author myc
 * @param {Object} data
 * @description 统计学生一年的学习情况通过学生Id
 */
export function exerciseRecordStatisticsByStduentId(studentId) {
  return request({
    url: '^/AppMgr/exercisejoinrecord/exerciseRecordStatisticsByStduentId',
    method: 'get',
    params: {
      studentId
    }
  })
}
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function exerciserecordstatistics(userId) {
  return request({
    url: '^/AppMgr/exercisejoinrecord/exerciserecordstatistics',
    method: 'get',
    params: {
      userId
    }
  })
}
