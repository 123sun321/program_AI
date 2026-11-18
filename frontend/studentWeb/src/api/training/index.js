/**
 * @file 所有学生练习记录列表相关
 * @author 宋乃乐
 * @copyright zhengzhouchuanghzi
 * @since 2019.3.30
 */
import request from '@/utils/request'

/**
 * @method
 * @param {Object} data
 * @description 得到所有学生的练习列表
 */
export function getTrainingList(data) {
  return request({
    url: '/training/pageAllSelf',
    method: 'POST',
    data
  })
}

/**
 * @method
 * @param {Number} id
 * @description 增加点赞数
 */
export function insertLike(id) {
  return request({
    url: '/training/trainingApprove',
    method: 'GET',
    params: {
      trainingId: id
    }
  })
}

/**
 * fork优秀作品
 * @param {Object} data
 */
export function fork(data) {
  return request({
    url: '/training/fork',
    method: 'POST',
    data
  })
}
/**
 * 获取练习及代码
 * @param {Number} id
 */
export function get(id) {
  return request({
    url: '/training/getTrainAndCodeById',
    method: 'GET',
    params: {
      id
    }
  })
}
/**
 * 保存实验
 * @param {Object} data
 */
export function save(data) {
  return request({
    url: '/training/save',
    method: 'POST',
    data
  })
}
