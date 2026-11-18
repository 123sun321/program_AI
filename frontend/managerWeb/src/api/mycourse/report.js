
/**
 * @file 报告相关交互
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/3/29
 */

import request from '@/utils/request'
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function pageList(data) {
  return request({
    url: '^/AppMgr/exercisejoinreport/page',
    method: 'post',
    data
  })
}
/**
 * 通过编号获取报告详情
 * @param {Number} id
 */
export function getReport(id) {
  return request({
    url: '^/AppMgr/exercisejoinreport/get',
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * 编辑报告
 * @param {Object} data
 */
export function edit(data) {
  return request({
    url: '^/AppMgr/exercisejoinreport/edit',
    method: 'post',
    data
  })
}
/**
 * 相关报告
 * @param {Long} exerciseId
 */
export function getCorrelationReport(exerciseId) {
  return request({
    url: '^/AppMgr/exercisejoinreport/correlationreport',
    method: 'get',
    params: {
      exerciseId
    }
  })
}

