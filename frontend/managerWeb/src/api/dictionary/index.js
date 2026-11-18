/**
 * @file 字典
 * @author 陈乐乐
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/04
 */
import request from '@/utils/request'
/**
 * @method
 * @param {Number} id
 * @description 提取一个
 */
export function fetch(id) {
  return request({
    url: '^/dictionary/get',
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * @method
 * @param {Object} data
 * @description 添加
 */
export function add(data) {
  return request({
    url: '^/dictionary/add',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 编辑字典
 */
export function edit(data) {
  return request({
    url: '^/dictionary/edit',
    method: 'put',
    data
  })
}
/**
 * @method
 * @param {Number} id
 * @description 获取子项
 */
export function fetchItem(id) {
  return request({
    url: '^/dictionary/getitem',
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * @method
 * @param {Object} data
 * @description 添加子项
 */
export function addItem(data) {
  return request({
    url: '^/dictionary/additem',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 编辑子项
 */
export function editItem(data) {
  return request({
    url: '^/dictionary/edititem',
    method: 'put',
    data
  })
}
/**
 * @method
 * @description 取全部字典
 */
export function fetchList() {
  return request({
    url: '^/dictionary/listall',
    method: 'get'
  })
}
/**
 * @method
 * @param {*} code
 * @description 取字典和字典项
 */
export function fetchWithItems(code) {
  return request({
    url: '^/dictionary/listitem',
    method: 'get',
    params: {
      code: code
    }
  })
}
/**
 * @method
 * @param {String} name 字典名称
 * @param {String} code 字典代码
 * @description 取字典的字典项
 */
export function fetchItems(name, code) {
  return request({
    url: '^/dictionary/items',
    method: 'get',
    params: {
      name,
      code
    }
  })
}
/**
 * @param {number} id
 * @param {String} key
 * @param {String} value
 * @description 远程教研字典
 */
export function remoteValid(id, key, value) {
  return request({
    url: '^/dictionary/validate',
    method: 'get',
    params: {
      id,
      key,
      value
    }
  })
}
/**
 * @method
 * @param {Number} id
 * @param {Number} dictionaryid
 * @param {String} key
 * @param {String} value
 * @description 远程校验字典项
 */
export function remoteValidItem(id, dictionaryid, key, value) {
  return request({
    url: '^/dictionary/validateitem',
    method: 'get',
    params: {
      key,
      value,
      id,
      dictionaryid
    }
  })
}
/**
 * @method
 * @param {Number} id
 * @description 删除字典
 */
export function del(id) {
  return request({
    url: '^/dictionary/del',
    method: 'delete',
    params: {
      id
    }
  })
}
/**
 * @method
 * @param {Number} id
 * @description 删除子项
 */
export function delItem(id) {
  return request({
    url: '^/dictionary/delitem',
    method: 'delete',
    params: {
      id
    }
  })
}
