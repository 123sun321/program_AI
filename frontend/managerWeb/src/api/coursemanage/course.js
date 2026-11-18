/**
 * @file 课程相关交互
 * @author 刘彦杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/3/11
 */
import { fetchItems } from '@/api/dictionary'
import request from '@/utils/request'
/**
 * @method
 * @param {Object} data
 * @description 添加一条数据
 */
export function add(data) {
  return request({
    url: '/course/add',
    method: 'post',
    data
  })
}

/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function fetchPage(data) {
  return request({
    url: '/course/page',
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
    url: '/course/get',
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * @method
 * @param {Object} data
 * @description 编辑
 */
export function edit(data) {
  return request({
    url: '/course/edit',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 教师编辑课程
 */
export function teacheredit(data) {
  return request({
    url: '/course/teacheredit',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Array} ids
 * @description 删除
 */
export function del(ids) {
  return request({
    url: '/course/del',
    method: 'post',
    data: [...ids]
  })
}
/**
 * @method
 * @param {Number} id
 * @param {String} col
 * @param {String} value
 * @description 远程校验
 */
export function remoteValid(id, userId, col, value) {
  return request({
    url: '/course/validate',
    method: 'get',
    params: {
      id,
      userId,
      col,
      value
    }
  })
}
/**
 * @method
 * @param {Array} list
 * @description 批量添加
 */
export function multipleAdd(list) {
  return request({
    url: '/course/multipleAdd',
    method: 'post',
    data: {
      data: list
    }
  })
}
/**
 * @method
 * @param {Object} data
 * @description 批量导出
 */
export function exportByIds(data) {
  return request({
    url: '/course/export',
    method: 'post',
    data,
    responseType: 'arraybuffer'
  })
}

export function fetchList(name) {
  return request({
    url: '/course/list',
    method: 'get',
    params: {
      name
    }
  })
}
export function fetchNature() {
  return fetchItems('课程类型')
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
/**
 * 获取课程详情页
 */
export function searchPage(data) {
  return request({
    url: '^/AppMgr/courseJoin/page',
    method: 'post',
    data
  })
}
/**
 * 获取课程评价页
 */
export function coursePage(data) {
  return request({
    url: '^/AppMgr/comment/coursePage',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 分页获取当前用户下的课程
 */
export function getNowCourse(data) {
  return request({
    url: '^/AppMgr/course/getNowCourse',
    method: 'post',
    data
  })
}
/**
 * @method
 *
 * @description 获取课程类别列表
 */
export function categoryList() {
  return request({
    url: '^/AppMgr/course/categoryList',
    method: 'get'
  })
}
/**
 * @method
 *
 * @description 获取课程难易程度列表
 */
export function complexityList() {
  return request({
    url: '^/AppMgr/course/complexityList',
    method: 'get'
  })
}
/**
 * @method
 * @param {Array} ids
 * @description 导入课程功能中被导入的数据
 */
export function batchImport(ids) {
  return request({
    url: '/course/batchImport',
    method: 'post',
    data: [...ids]
  })
}
/**
 * @method
 * @param {Object} data
 * @description 添加一条数据
 */
export function addTeacherCourse(data) {
  return request({
    url: '/course/addTeacherCourse',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 教师端导入课程页面课程分页
 */

export function pageTeacherCourse(data) {
  return request({
    url: '/course/publicPage',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param
 * @description 获取课程类型
 */

export function getCourseType() {
  return request({
    url: '/course/getCourseType',
    method: 'get'

  })
}
/**
 * @method
 * @param
 * @description 重新生成邀请码
 */

export function regainCode(courseid) {
  return request({
    url: '/course/regainCode',
    method: 'get',
    params: {
      courseid: courseid
    }

  })
}

/**
 * @method
 * @param
 * @description 重新生成邀请码
 */

export function getGroup(courseid) {
  return request({
    url: '^/AppMgr/coursegroup/list',
    method: 'get',
    params: {
      courseId: courseid
    }

  })
}
/**
 *添加小组
 * @param {Number} ids
 * @param {Number} groupid
 */
export function join(ids, groupid) {
  return request({
    url: '^/AppMgr/courseJoin/join',
    method: 'get',
    params: {
      ids, groupid
    }

  })
}
