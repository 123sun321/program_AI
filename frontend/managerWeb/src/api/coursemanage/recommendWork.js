/**
 * @file 优秀作品相关交互
 * @author myc
 * @copyright zhengzhouchuanghzi
 * @since 2025/05/14
 */
import request from '@/utils/request'
/**
 * @method
 * @author myc
 * @param {Object} data
 * @description 优秀作品分页展示
 */
export function recommendWorkPage(data) {
  return request({
    url: '^/AppMgr/recommendWork/recommendWorkPage',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Array} id
 * @description 删除
 */
export function deleteSingle(id) {
  return request({
    url: '/recommendWork/del',
    method: 'post',
    params: {
      id
    }
  })
}
/**
 * @method
 * @param {Array} ids
 * @description 批量删除
 */
export function deleteBatches(ids) {
  return request({
    url: '/recommendWork/deleteBatches',
    method: 'post',
    data: [...ids]
  })
}
/**
 * @method
 * @param {Array} id
 * @description 通过编号查询优秀作品
 */
export function getCodeWithId(id) {
  return request({
    url: '/recommendWork/get',
    method: 'get',
    params: {
      id
    }
  })
}
