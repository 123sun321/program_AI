/**
 * @file 消息设置api
 * @author 张向宇
 * @copyright zhengzhouchuanghzi
 * @since 2025/01/07
 */
import request from '@/utils/request'
/**
 * @method
 * @description 获取用户消息设置信息数据
 */
export function getUserSetting() {
  return request({
    url: '^/AppMgr/notifySetting/get',
    method: 'get'
  })
}

/**
 * @method
 * @description 设置用户消息配置信息
 */
export function setUserSetting(data) {
  return request({
    url: '^/AppMgr/notifySetting/set',
    method: 'post',
    data: data
  })
}
