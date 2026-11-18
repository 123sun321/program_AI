/**
 * @file 教师管理相关交互
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/04
 */
import request from '@/utils/request'
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function search(data) {
  return request({
    url: '/teachers/page',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 获取所有院系
 */
export function listDepartments(data) {
  return request({
    url: '/department/listDepartments',
    method: 'get',
    data
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
 * @description 获取专业
 */
export function getAllProfession() {
  return request({
    url: '/professions/list',
    method: 'get'
  })
}
/**
 * @method
 * @param {Number} id
 * @description 提取一条
 */
export function fetchOne(id) {
  return request({
    url: '/teachers/get',
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * @method
 * @param {Object} data
 * @description 添加一条数据
 */
export function add(data) {
  return request({
    url: '/teachers/add',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 编辑
 */
export function edit(data) {
  return request({
    url: '/teachers/edit',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Array} ids
 * @description 批量删除
 */
export function del(ids) {
  return request({
    url: '/teachers/deleteBatches',
    method: 'post',
    data: [...ids]
  })
}
/**
 * @method
 * @param {Array} id
 * @description 删除
 */
export function deletePro(id) {
  return request({
    url: '/teachers/del/' + id,
    method: 'post'
  })
}
/**
 * @method
 * @param {Array} data
 * @description 批量添加
 */
export function insertBatches(data) {
  return request({
    url: '/teachers/multipleAdd',
    method: 'post',
    data: {
      data: data
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
    url: '/teachers/export',
    method: 'post',
    data,
    responseType: 'arraybuffer'
  })
}
/**
 * @method
 * @param {Number} id
 * @param {String} col
 * @param {String} value
 * @description 远程校验
 */
export function remoteValid(id, col, value) {
  return request({
    url: '/teachers/validate',
    method: 'get',
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
 * @author 陈乐乐
 * @description 去列表
 */
export function fetchList(data) {
  return request({
    url: '/teachers/list',
    method: 'post',
    data
  })
}

/**
 * @method
 * @param {Object} data
 * @author 马勇超
 * @description 教师首页统计
 */
export function getDashboardCount(data) {
  return request({
    url: '/teachers/getDashboardCount',
    method: 'get'
  })
}
/**
 * @method
 * @param {Object} data
 * @author 马勇超
 * @description 学校管理端首页统计
 */
export function getSchoolDashboardCount() {
  return request({
    url: '/teachers/getSchoolDashboardCount',
    method: 'get'
  })
}
/**
 * 重置密码
 * @param {*} id
 */
export function resetPass(id) {
  return request({
    url: '^/AppMgr/teachers/resetPass?id=' + id,
    method: 'get'
  })
}
/**
 * 解绑手机
 * @param {*} id
 */
export function unbindPhone(id) {
  return request({
    url: '^/AppMgr/teachers/bindPhone/unbind?id=' + id,
    method: 'get'
  })
}
