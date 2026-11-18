
/**
 * @file 作业相关交互
 * @author 张人杰
 * @copyright zhengzhouchuangzhi
 * @since 2025/3/19
 */
import request from '@/utils/request'

/**
 * @method
 * @param {Object} data
 * @description 添加一条作业数据
 */
export function add(data) {
  return request({
    url: '/assignment/add',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 修改一条作业数据
 */
export function edit(data) {
  return request({
    url: '/assignment/edit',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Number} id
 * @description 删除一条作业数据
 */
export function del(id) {
  return request({
    url: '/assignment/del?id=' + id,
    method: 'get'

  })
}
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function fetchPage(data) {
  return request({
    url: '/assignment/page',
    method: 'post',
    data
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
 * @param {String} col
 * @param {String} value
 * @description 远程校验
 */
export function remoteValid(id, col, value, courseid) {
  return request({
    url: '/assignment/validate',
    method: 'post',
    params: {
      id,
      col,
      value,
      courseid
    }
  })
}
/**
 * @method
 * @param {Number} id
 * @param {String} col
 * @param {String} value
 * @param {Number} value
 * @description 远程校验
 */
export function assignmentValidate(id, col, value, courseid) {
  return request({
    url: '/assignment/assignmentValidate',
    method: 'post',
    params: {
      id,
      col,
      value,
      courseid
    }
  })
}
/**
 * @method
 * @param {Array} ids
 * @description 删除多条作业数据数据
 */
export function delnum(ids) {
  return request({
    url: '/assignment/delBatch',
    method: 'post',
    data: [...ids]
  })
}
/**
 * @method
 * @param {Object} ids
 * @description 添加一条作业内容数据
 */
export function setAssignment(data) {
  return request({
    url: '/assignment/add',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {DeleteSingle} id
 * @description 删除一条作业数据
 */
export function deleteSingleAssignment(id) {
  return request({
    url: '/assignment/delete?id=' + id,
    method: 'get'
  })
}
/**
 * @method
 * @param {Object} data
 * @description 编辑
 */
export function updateAssignment(data) {
  return request({
    url: '/assignment/update',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {number} getAssignmentSubmitId
 * @description 通过作业提交ID查看学生作业已提交总人数以及最值评分
 */
export function getAssignmentSubmit(assignmentid, courseid, gradeid, groupid) {
  return request({
    url: '/assignmentsubmit/getCountByAssignmentId',
    method: 'post',
    params: {
      assignmentid,
      courseid,
      gradeid,
      groupid
    }

  })
}
/**
 * @method
 * @param {Object} getAssignmentSubmitId
 * @description 通过作业ID查看作业已提交的学生
 */
export function fetchpageAssignmentSubmit(data) {
  return request({
    url: '/assignmentsubmit/page',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {number} subid 提交作业ID
 * @param {number} score 提交作业分数
 * @description 编辑score
 */
export function updatescore(subid, score) {
  return request({
    url: '/assignmentsubmit/editscore?id=' + subid + '&score=' + score,
    method: 'post'
  })
}
/**
 * @method
 * @param {number} subid 提交作业ID
 * @param {number} recommend 提交作业设为推荐
 * @description 编辑recommend
 */
export function updaterecommend(subid) {
  return request({
    url: '/assignmentsubmit/editrecommend?id=' + subid,
    method: 'get'
  })
}
/**
 * @method
 * @param {number} subid 提交作业ID
 * @param {number} recommend 提交作业取消设为推荐
 * @description 编辑recommend
 */
export function editdelRecommend(subid) {
  return request({
    url: '/assignmentsubmit/editdelRecommend?id=' + subid,
    method: 'get'
  })
}
/**
 * @method
 * @param {number} subid 提交作业ID
 * @description 通过ID获取信息
 */
export function getSubmitByID(subid) {
  return request({
    url: '/assignmentsubmit/getAssignmentSubmitById',
    method: 'get',
    params: {
      id: subid
    }
  })
}

/**
 * @method
 * @param {number} 获取为评分作业列表
 * @description
 */
export function getNoScoreList() {
  return request({
    url: '/assignmentsubmit/getNoScoreList',
    method: 'get'
  })
}

/**
 * @method
 * @param {number} 获取为评分作业列表
 * @description
 */
export function getTrainAndCodeById(id) {
  return request({
    url: '/training/getNoScoreList',
    method: 'get'
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
 * @method
 * @param {Object}data 保存作业资源
 * @description
 */
export function saveAssignmentResource(data) {
  return request({
    url: '/assignmentResource/saveAssignmentResource',
    method: 'post',
    data
  })
}

/**
 * @method
 * @param 根据提交作业id获取代码信息
 */
export function getCodeByAssignmentSubmitId(id1, id2) {
  return request({
    url: '/assignmentsubmit/getCodeByAssignmentSubmitId?id1=' + id1 + '&id2=' + id2,
    method: 'get'
  })
}
/**
 * @method
 * @param 根据提交作业id获取作业和学生信息
 */
export function getStudentAndAssignmentById(id) {
  return request({
    url: '/assignmentsubmit/getStudentAndAssignmentById?id=' + id,
    method: 'get'
  })
}
/**
 * @method
 * @param {List}data 查看代码相似度
 * @description
 */
export function CheckSimilarity(ids) {
  return request({
    url: '/assignmentsubmit/CheckSimilarity',
    method: 'post',
    data: [...ids]
  })
}

