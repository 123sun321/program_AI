/**
 * @author myc
 * 优秀作品相关API
 */
import request from '@/utils/request'

/**
 * @author myc
 * 优秀作品分页展示
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
 * @param {Number} id
 * @description 获取优秀作品
 */
export function get(id) {
  return request({
    url: '^/AppMgr/recommendWork/get',
    method: 'GET',
    params: {
      id
    }
  })
}
