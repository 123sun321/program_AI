import request from '@/utils/request'
/**
 * @description 添加奖惩类别
 * @author HuangPengju
 * @param {*} data 需要添加的数据
 * @since 2018/12/4
 */
export function add(data) {
  return request({
    url: '/rewardPunishCategory/add',
    method: 'post',
    data
  })
}
/**
 * @description 获取奖惩类别列表
 * @author HuangPengju
 * @param {*} data  搜索、分页条件
 */
export function fetchList(data) {
  return request({
    url: '/rewardPunishCategory/page',
    method: 'post',
    data
  })
}
/**
 * @description 根据id删除信息
 * @author HuangPengju
 * @param {*} id 删除条件id
 */
export function del(id) {
  return request({
    url: '/rewardPunishCategory/del/' + id,
    method: 'get'
  })
}
/**
 * @description 根据id数组删除信息
 * @author HuangPengju
 * @param {*} ids 删除条件id数组
 */
export function dels(ids) {
  return request({
    url: '/rewardPunishCategory/deleteBatch',
    method: 'post',
    data: [...ids]
  })
}
/**
 * @description 根据id数组查询信息
 * @author HuangPengju
 * @param {*} id  查询条件
 */
export function fetchOne(id) {
  return request({
    url: '/rewardPunishCategory/get',
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * @description 修改奖惩信息
 * @author HuangPengju
 * @param {*} data 需要修改的数据
 */
export function edit(data) {
  return request({
    url: '/rewardPunishCategory/edit',
    method: 'post',
    data
  })
}
/**
 * @description 奖惩表单查重
 * @author HuangPengju
 * @param {*} id 修改时验证的数据id
 * @param {*} col  验证的字段
 * @param {*} value 验证字段的值
 */
export function remoteValid(id, col, value) {
  return request({
    url: '/rewardPunishCategory/validate',
    method: 'get',
    params: {
      id,
      col,
      value
    }
  })
}
