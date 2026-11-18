
/**
 * @file 实验相关交互
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/3/28
 */

import request from '@/utils/request'
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function getCurrentPage(data) {
  return request({
    url: '^/AppMgr/exerciseJoin/getCurrentPage',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Number} id
 * @description  实验文档
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
 * @method
 * @param {Object} data
 * @description 案例库列表
 */
export function exerciseList(data) {
  return request({
    url: '^/AppMgr/exercise/list',
    method: 'post',
    data
  })
}

/**
 * @method
 * @param {Object} data
 * @description 作品展示列表
 */
export function exerciseJoinList(data) {
  return request({
    url: '^/AppMgr/exerciseJoin/exerciseJoinList',
    method: 'post',
    data
  })
}
