/**
 * @file 积分查询
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/12
 */
import request from '@/utils/request'
/**
 * @method
 * @author 赵金毅
 * @param
 * @description 分页查找积分列表
 */
export function fetchPage(data) {
  return request({
    url: '/integral-score/page',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param
 * @author 赵金毅
 * @description 查找积分
 */
export function selectCount() {
  return request({
    url: '/integral-score/scoreNum',
    method: 'post'
  })
}
