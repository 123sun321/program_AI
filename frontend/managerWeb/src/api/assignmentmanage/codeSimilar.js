/**
 * @file 代码相似度对比
 * @author 张人杰
 * @copyright zhengzhouchuangzhi
 * @since 2025/3/19
 */
import request from '@/utils/request'
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function fetchPage(data) {
  return request({
    url: '/code/page',
    method: 'post',
    data
  })
}
