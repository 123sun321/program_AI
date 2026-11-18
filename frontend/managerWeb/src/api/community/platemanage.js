/**
 * @file 板块管理交互
 * @author 张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2018/3/28
 */
import request from '@/utils/request'
/**
 * @method
 * @param {Object} data
 * @description 板块分页
 */
export function fetchPage(data) {
  return request({
    url: '^/AppMgr/communityPlate/page',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} id
 * @description 删除签到记录
 */
export function del(ids) {
  return request({
    url: '^/AppMgr/communityPlate/delete',
    method: 'post',
    data: [...ids]

  })
}
/**
 * @method
 * @param {Object} id
 * @description 通过id获取板块数据
 */
export function getById(id) {
  return request({
    url: '/communityPlate/getById?id=' + id,
    method: 'get'
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
    url: '^/AppMgr/communityPlate/validate',
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
 * @description 添加一条社区板块数据
 */
export function add(data) {
  return request({
    url: '/communityPlate/add',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 编辑一条社区板块数据
 */
export function edite(data) {
  return request({
    url: '/communityPlate/edit',
    method: 'post',
    data
  })
}
