
/**
 * @file 实验参与详情统计相关交互
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/4/8
 */

import request from '@/utils/request'
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function statisticnumber(userId) {
  return request({
    url: '^/AppMgr/exercisejoinrecord/statisticnumber',
    method: 'get',
    params: {
      userId
    }
  })
}
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function exerciserecordstatistics(userId) {
  return request({
    url: '^/AppMgr/exercisejoinrecord/exerciserecordstatistics',
    method: 'get',
    params: {
      userId
    }
  })
}
