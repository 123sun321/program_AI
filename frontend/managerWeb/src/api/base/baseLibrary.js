import request from '@/utils/request'

/**
 * @method
 * @param {Object} data
 * @description 获取所有
 */
export function list() {
  return request({
    url: '/baselibrary/list',
    method: 'get'
  })
}
/**
 * @method
 * @param {Number} id
 * @description 提取一条
 */
export function get(id) {
  return request({
    url: '/baselibrary/get',
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
    url: '/baselibrary/add',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Number} id
 * @description 批量删除
 */
export function del(id) {
  return request({
    url: '^/AppMgr/baselibrary/del',
    method: 'get',
    params: {
      id
    }
  })
}
