/**
 * @file 站内信api
 * @author 张向宇
 * @copyright zhengzhouchuanghzi
 * @since 2025/01/08
 */
import request from '@/utils/request'

/**
 * @method
 * @description 获取用户站内信
 */
export function getUserSiteMessage(data) {
  return request({
    url: '^/AppMgr/message/get',
    method: 'post',
    data: data
  })
}

/**
 * @method
 * @description 标记消息为已读
 */
export function read(messageId) {
  return request({
    url: '^/AppMgr/message/read',
    method: 'get',
    params: {
      messageId: messageId
    }
  })
}

/**
 * @method
 * @description 获取未读消息数
 */
export function getNoReadCount() {
  return request({
    url: '^/AppMgr/message/countNoRead',
    method: 'get'
  })
}
