/**
 * @file 实验练习参与详情相关交互
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/15
 */
import request from '@/utils/request'

/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function pageByExerciseId(data) {
  return request({
    url: '^/AppMgr/exerciseJoin/pageByExerciseId',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Number} id
 * @description 提取一条
 */
export function get(joinId) {
  return request({
    url: '^/AppMgr/exerciseJoin/get',
    method: 'get',
    params: {
      joinId
    }
  })
}
/**
 * 相关报告
 * @param {Long} id
 */
export function getByJoinId(id) {
  return request({
    url: '^/AppMgr/exercisejoinreport/getbyjoinid',
    method: 'get',
    params: {
      id
    }
  })
}
