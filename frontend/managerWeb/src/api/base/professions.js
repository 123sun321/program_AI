/**
 * @file 专业管理相关交互
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
export function searchProfession(data) {
  return request({
    url: '/profession/page',
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
 * @param {Number} id
 * @description 提取一条
 */
export function fetchOne(id) {
  return request({
    url: '/profession/get',
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
export function addProfession(data) {
  return request({
    url: '/profession/add',
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
    url: '^/AppMgr/profession/edit',
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
    url: '^/AppMgr/profession/deleteBatches',
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
    url: '^/AppMgr/profession/del/' + id,
    method: 'post'
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
    url: '^/AppMgr/profession/validate',
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
 * @param {Array} list
 * @description 批量添加
 */
export function multipleAdd(list) {
  return request({
    url: '^/AppMgr/profession/multipleAdd',
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
    url: '/profession/export',
    method: 'post',
    data,
    responseType: 'arraybuffer'
  })
}
