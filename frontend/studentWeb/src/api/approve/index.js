/**
 * @author myc
 * 优秀作品点赞列表相关API
 */
import request from '@/utils/request'

/**
 * @author myc
 * 优秀作品点赞
 */
export function addApprove(id) {
  return request({
    url: '^/AppMgr/approve/add',
    method: 'post',
    params: {
      id
    }
  })
}
