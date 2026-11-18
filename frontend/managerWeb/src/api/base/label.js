import request from '@/utils/request'

/** 标签分页 */
export function pageWithLabel(data) {
  return request({
    url: '/label/page',
    method: 'post',
    data
  })
}
/**
 * 单条删除标签
 * 马勇超
 * @param {*} id
 */
export function deleteSingleLabel(id) {
  return request({
    url: '/label/del',
    method: 'post',
    params: {
      id
    }
  })
}
/**
 * 批量删除
 * 马勇超
 * @param {*} ids
 */
export function delBatches(ids) {
  return request({
    url: '/label/delBatches',
    method: 'post',
    data: [...ids]
  })
}
/**
 *添加
 * @param {*} data
 */
export function add(data) {
  return request({
    url: '/label/add',
    method: 'post',
    data
  })
}
/**
 * 获取单条信息
 * @param {*} id
 */
export function fetchOne(id) {
  return request({
    url: '/label/get',
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * 修改
 * @param {*} data
 */
export function updateLabel(data) {
  return request({
    url: '/label/edit',
    method: 'post',
    data
  })
}

/**
 * 获取所有的标签列表
 */
export function getAllList() {
  return request({
    url: '/label/list',
    method: 'get'
  })
}

/**
 * 通过关键字获取列表
 * @param {*} str
 */
export function chooseList(str) {
  return request({
    url: '/label/chooseList',
    method: 'get',
    params: {
      str
    }
  })
}

/**
 * 通过名称精确查询
 * @param {*} name
 */
export function getByName(name) {
  return request({
    url: '/label/getByName?name=' + name,
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
    url: '/label/validate',
    method: 'get',
    params: {
      id,
      col,
      value
    }
  })
}

