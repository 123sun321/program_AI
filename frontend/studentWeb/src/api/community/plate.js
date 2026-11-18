/**
 * 社区板块
 * @author myc
 */
import request from '@/utils/request'

/**
 * 获取所有板块
 * @param {*} data
 */
export function getCommunityPlateList(data) {
  return request({
    url: '^/AppMgr/communityPlate/list',
    method: 'get',
    data
  })
}
