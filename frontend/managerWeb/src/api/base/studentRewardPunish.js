import request from '@/utils/request'
import { fetchItems } from '@/api/dictionary'
/**
 * @description 获取学生奖惩列表
 * @author HuangPengju
 * @param {*} data 搜索、分页数据
 */
export function fetchList(data) {
  return request({
    url: '/studenthonor/page',
    method: 'post',
    data
  })
}
/**
 * @description 根据id数组删除信息
 * @author HuangPengju
 * @param {*} ids 删除条件id数组
 */
export function dels(ids) {
  return request({
    url: '/studenthonor/deleteBatch',
    method: 'post',
    data: [...ids]
  })
}
/**
 * @description 获取所有院系
 * @author HuangPengju
 */
export function departmentList() {
  return request({
    url: '/department/listDepartments',
    method: 'get'
  })
}
/**
 * @description 获取所有专业
 * @author HuangPengju
 */
export function professionList() {
  return request({
    url: '/professions/all',
    method: 'get'
  })
}
/**
 * @description 根据姓名、学号、证件号码查询学生
 * @author HuangPengju
 * @param {*} data 搜索条件
 */
export function getStudent(data) {
  return request({
    url: '/studenthonor/getStudent',
    method: 'post',
    data
  })
}
/**
  * @description 获取所有的奖惩种类
  * @author HuangPengju
 */
export function rewardPunishList() {
  return request({
    url: '/studenthonor/getRewardPunish',
    method: 'get'
  })
}
/**
 * @description 添加学生奖惩信息
 * @author HuangPengju
 * @param {*} data 添加的学生奖惩信息
 */
export function add(data) {
  return request({
    url: '/studenthonor/add',
    method: 'post',
    data
  })
}
/**
 * @description 根据id查找学生信息
 * @author HuangPengju
 * @param {*} id 查找条件
 */
export function fetchOne(id) {
  return request({
    url: '/studenthonor/get',
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * @description 修改学生奖惩信息
 * @author HuangPengju
 * @param {*} data  需要修改的数据
 */
export function edit(data) {
  return request({
    url: '/studenthonor/edit',
    method: 'post',
    data
  })
}
// 获取年级
export function fetchGrades() {
  return fetchItems('年级')
}
