/**
 * @file 课程相关交互
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2018/3/15
 */
import request from '@/utils/request'
/**
 * @method
 * @param {Object} data
 * @description 分页
 */
export function fetchPage(data) {
  return request({
    url: '/course/page',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 学习详情分页
 */
export function findPage(data) {
  return request({
    url: '^/AppMgr/joincoursepath/findpage',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 路径评论详情分页
 */
export function showPage(data) {
  return request({
    url: '^/AppMgr/comment/commentPathPage',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 发表评论
 */
export function addcomment(data) {
  return request({
    url: '^/AppMgr/comment/add',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 学习总人数
 */
export function personcount(data) {
  return request({
    url: '^/AppMgr/joincoursepath/joinCount',
    method: 'get',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description  总课数
 */
export function studycount(data) {
  return request({
    url: '^/AppMgr/joincoursepath/studyschedule',
    method: 'get',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 课程类别
 */
export function categorylists(data) {
  return request({
    url: '^/AppMgr/course/categoryList',
    method: 'get',
    data
  })
}
/**
 * @method
 * @param {Object} id
 * @description 删除路径课程
 */
export function del(id) {
  return request({
    url: '^/AppMgr/coursepath/coursePathDel?id=' + id,
    method: 'get'
  })
}
/**
 * @method
 * @param {Object} id
 * @description 删除评论
 */
export function dele(id) {
  return request({
    url: '^/AppMgr/comment/deleteComment?id=' + id,
    method: 'get'
  })
}
/**
 * @method
 * @param {Object} date
 * @description  加入路径课程
 */
export function setCoursepathadds(ids, code) {
  return request({
    url: '^/AppMgr/coursepath/coursePathMultipleAdd',
    method: 'post',
    params: {
      courseIds: ids,
      category: code
    }
  })
}
/**
 * @method
 * @param {Object} date
 * @description 上下移动路径课程
 */
export function pathup(data) {
  return request({
    url: '^/AppMgr/coursepath/coursePathUpAndDown',
    method: 'get',
    params: data
  })
}
/**
 * @method
 * @param {Object} date
 * @description  路径课程
 */
export function showAll(data) {
  return request({
    url: '^/AppMgr/coursepath/coursePathAll',
    method: 'post',
    data
  })
}
