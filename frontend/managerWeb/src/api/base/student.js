/**
 * @file 学生相关交互
 * @author 赵雪丽
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/06
 */
import request from '@/utils/request'
import { fetchItems } from '@/api/dictionary'
/**
 * @method
 * @param {Object} data
 * @description 添加一条数据
 */
export function setStudent(data) {
  return request({
    url: '^/AppMgr/student/create',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Array} ids
 * @description 删除
 */
export function delSingleStudent(id) {
  return request({
    url: '^/AppMgr/student/delete' + id,
    method: 'get'
  })
}
/**
 * 重置密码
 * @param {*} id
 */
export function resetPass(id) {
  return request({
    url: '^/AppMgr/student/resetPass?id=' + id,
    method: 'get'
  })
}
/**
 * 解绑手机
 * @param {*} id
 */
export function unbindPhone(id) {
  return request({
    url: '^/AppMgr/student/bindPhone/unbind?id=' + id,
    method: 'get'
  })
}
/**
 * @method
 * @param {Object} data
 * @description 编辑
 */
export function updateStudent(data) {
  return request({
    url: '^/AppMgr/student/update',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Number} id
 * @description 提取一条
 */
export function getStudent(id) {
  return request({
    url: '^/AppMgr/student/' + id,
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
export function getClassByYear(year) {
  return request({
    url: '^/AppMgr/grade/getByYear?year=' + year,
    method: 'get'
  })
}
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function pageStudent(data) {
  return request({
    url: '^/AppMgr/student/page',
    method: 'post',
    data
  })
}

/**
 * @method
 * @param {Object} data
 * @description 学生学习情况记录分页
 */
export function pageDetailStudent(data) {
  return request({
    url: '^/AppMgr/exerciseJoin/pageDetail',
    method: 'post',
    data
  })
}

/**
 * @method
 * @description 获取专业
 */
export function getAllProfession() {
  return request({
    url: '/profession/list',
    method: 'get'
  })
}

/**
 * @method
 * @description 获取专业并包含班级
 */
export function getProfessionWithGrade() {
  return request({
    url: '/profession/listWithGrade',
    method: 'get'
  })
}
/**
 * @method
 * @param {Array} list
 * @description 批量删除
 */
export function delBatchStudent(ids) {
  return request({
    url: '^/AppMgr/student/deleteBatch',
    method: 'post',
    data: [...ids]
  })
}
/**
 * @method
 * @param {Object} data
 * @description 导入Excel
 */
export function upload(data) {
  return request({
    url: '^/AppMgr/student/upload',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Array} list
 * @description 批量添加
 */
export function createBatches(data) {
  return request({
    url: '^/AppMgr/student/createBatches',
    method: 'post',
    data: {
      data: data
    }
  })
}
/**
 * @method
 * @description 获取院系专业
 */
export function getDepPro() {
  return request({
    url: '^/AppMgr/department/listWithProfessions',
    method: 'get'
  })
}
/**
 * @method
 * @param {Object} data
 * @description 远程校验
 */
export function remoteValid(id, col, value) {
  return request({
    url: '/student/validate',
    method: 'post',
    params: {
      id,
      col,
      value
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
    url: '^/AppMgr/student/download/export',
    method: 'post',
    data,
    responseType: 'arraybuffer'
  })
}
/**
 * @method
 * @description 获取院系专业班级
 */
export function getDepProClass() {
  return request({
    url: '^/AppMgr/department/listWithProfessionsAndGrades',
    method: 'get'
  })
}
// 提去列表
export function fetchList(data) {
  return request({
    url: '/student/list',
    method: 'post',
    data
  })
}
// 获取年级
export function fetchGrades() {
  return fetchItems('年级')
}
/**
 * @method
 * 生成报告
 * @author 陈乐乐
 *
 */
export function createReport(list) {
  return request({
    url: '/student/createReport',
    method: 'post',
    data: [...list]
  })
}

