/**
 * @file 学习小组相关交互
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/7/5
 */

import request from '@/utils/request'

export function add(data) {
  return request({
    url: '^/AppMgr/coursegroup/add',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function pageList(data) {
  return request({
    url: '^/AppMgr/coursegroup/page',
    method: 'post',
    data
  })
}
/**
 * 通过编号
 * @param {Number} id
 */
export function get(id) {
  return request({
    url: '^/AppMgr/coursegroup/get',
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * 删除
 * @param {Number} id
 */
export function del(id) {
  return request({
    url: '^/AppMgr/coursegroup/del',
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * 编辑报告
 * @param {Object} data
 */
export function edit(data) {
  return request({
    url: '^/AppMgr/coursegroup/edit',
    method: 'post',
    data
  })
}
/**
 * 获取小组列表
 * @param {Number} courseId
 */
export function list(courseId) {
  return request({
    url: '^/AppMgr/coursegroup/list',
    method: 'get',
    params: {
      courseId
    }
  })
}
