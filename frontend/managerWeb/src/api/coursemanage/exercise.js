/**
 * @file 实验练习相关交互
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/12
 */
import request from '@/utils/request'
/**
 * @method
 * @param {Object} data
 * @description 添加一条数据
 */
export function add(data) {
  return request({
    url: '^/AppMgr/exercise/add',
    method: 'post',
    data
  })
}

/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function page(data) {
  return request({
    url: '^/AppMgr/exercise/page',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 学校端实验分页
 */
export function managerpage(data) {
  return request({
    url: '^/AppMgr/exercise/managerpage',
    method: 'post',
    data
  })
}

/**
 * @method
 * @param {Object} data
 * @description 通过课程ID分页获取实验
 */
export function pageByCourseId(data) {
  return request({
    url: '^/AppMgr/exercise/pageByCourseId',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Number} id
 * @description 提取一条
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
 * @description 编辑
 */
export function edit(data) {
  return request({
    url: '^/AppMgr/exercise/edit',
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
    url: '^/AppMgr/exercise/del',
    method: 'post',
    data: [...ids]
  })
}
/**
 * @method
 * @param {Array} ids
 * @description 删除
 */
export function moveExercise(id1, id2) {
  return request({
    url: '^/AppMgr/exercise/sort',
    method: 'get',
    params: {
      id1,
      id2
    }
  })
}
