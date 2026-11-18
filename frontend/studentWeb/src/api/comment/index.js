/**
 * @author Juci
 * 评论相关API
 */
import request from '@/utils/request'

/**
 * 报告评价分页
 * @param {Object} data
 */
export function reportPage(data) {
  return request({
    url: '^/AppMgr/comment/reportPage',
    method: 'post',
    data
  })
}
/**
 * 发布评论
 * @param {Object} data
 */
export function add(data) {
  return request({
    url: '^/AppMgr/comment/add',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Array} ids
 * @description 删除
 */
export function dele(id) {
  return request({
    url: '^/AppMgr/comment/deleteComment?id=' + id,
    method: 'get'

  })
}
/**
 * 评论点赞
 * @param {Long} id
 */
export function recommend(id) {
  return request({
    url: '^/AppMgr/comment/recommend',
    method: 'get',
    params: {
      id
    }
  })
}
