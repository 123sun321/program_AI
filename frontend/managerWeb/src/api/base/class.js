/**
 * @file 班级相关交互
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
export function setClass(data) {
  return request({
    url: '^/AppMgr/grade/create',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Array} ids
 * @description 删除
 */
export function deleteSingleClass(id) {
  return request({
    url: '^/AppMgr/grade/delete/' + id,
    method: 'get'
  })
}
/**
 * @method
 * @param {Object} data
 * @description 编辑
 */
export function updateClass(data) {
  return request({
    url: '^/AppMgr/grade/update',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Number} id
 * @description 提取一条
 */
export function getClass(id) {
  return request({
    url: '^/AppMgr/grade/' + id,
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function pageClass(data) {
  return request({
    url: '^/AppMgr/grade/page',
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
 * @param {Array} list
 * @description 批量删除
 */
export function delBatchClass(ids) {
  return request({
    url: '^/AppMgr/grade/deleteBatch',
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
    url: '^/AppMgr/grade/upload',
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
    url: '^/AppMgr/grade/createBatches',
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
export function remoteValid(data) {
  return request({
    url: '^/AppMgr/grade/validate',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 批量导出
 */
export function exportByIds(data) {
  return request({
    url: '^/AppMgr/grade/download/export',
    method: 'post',
    data,
    responseType: 'arraybuffer'
  })
}
/**
 * @method
 * @param {Array} list
 * @description 获取列表
 */
export function fetchList() {
  return request({
    url: '/grade/list',
    method: 'get'
  })
}
// 获取年级
export function fetchGrades() {
  return fetchItems('年级')
}
/**
 * @author 郜强
 * @param {Object} data
 * @description 批量导出
 */
export function getBatchByIds(str) {
  return request({
    url: '^/AppMgr/grade/getBatchByIds',
    method: 'get',
    params: {
      ids: str
    }
  })
}
