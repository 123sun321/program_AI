/**
 * @author myc
 * 作业相关API
 */
import request from '@/utils/request'

/**
 * @author myc
 * 提交作业
 */
export function submitAssignment(data) {
  return request({
    url: '^/AppMgr/assignmentsubmit/submitAssignment',
    method: 'post',
    data
  })
}
/**
 * @author myc
 * 重新提交作业
 */
export function resetSubmitAssignment(data) {
  return request({
    url: '^/AppMgr/assignmentsubmit/resetSubmitAssignment',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object}data 分页获取作业资源
 * @description
 */
export function fetchpageAssignmentResource(data) {
  return request({
    url: '/assignmentResource/getPage',
    method: 'post',
    data
  })
}
/**
 * @author myc
 * @param {Number} id
 * @description 根据作业id获取已提交的代码详情
 */
export function getCodeByAssignmentId(assignmentId) {
  return request({
    url: '/assignmentsubmit/getCodeByAssignmentId',
    method: 'get',
    params: {
      assignmentId
    }
  })
}
/**
 * @method
 * @param {Number} id
 * @description 提取一条
 */
export function fetchOne(id) {
  return request({
    url: '/assignment/getAssignmentID',
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * @method
 * @param {Number} id
 * @description 提取一条
 */
export function get(id) {
  return request({
    url: '^/AppMgr/exercise/get',
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * @author myc
 * @param {Number} id
 * @description 根据练习id获取代码详情
 */
export function getTrainAndCodeById(id) {
  return request({
    url: '^/AppMgr/training/getTrainAndCodeById',
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * @method
 * @param {Number} id
 * @description 取所有练习
 */
export function TrainingList() {
  return request({
    url: '^/AppMgr/training/getTrainingList',
    method: 'get'
  })
}
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function gradPage(data) {
  return request({
    url: '^/AppMgr/assignment/gradPage',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function groupPage(data) {
  return request({
    url: '^/AppMgr/assignment/groupPage',
    method: 'post',
    data
  })
}
