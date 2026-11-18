import request from '@/utils/request'
/**
 * @method
 * @param {Object} data
 * @author 张浩杰
 * @description 社区分页
 */
export function findPage(data) {
  return request({
    url: '^/AppMgr/communityTheme/page',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @author 张浩杰
 * @description 社区标签
*/
export function findlist(data) {
  return request({
    url: '^/AppMgr/communityPlate/list',
    method: 'get',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @author myc
 * @description 个人社区（我的讨论）主题管理分页
 */
export function getStudentPage(data) {
  return request({
    url: '^/AppMgr/communityTheme/studentThemePage',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @author myc
 * @description 个人社区（我的讨论）回复内容分页
 */
export function studentReplyComment(data) {
  return request({
    url: '^/AppMgr/communityTheme/studentReplyComment',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Array} ids
 * @description 删除/批量删除
 */
export function del(ids) {
  return request({
    url: '^/AppMgr/communityTheme/delete',
    method: 'post',
    data: [...ids]
  })
}
/**
 * @method
 * @param {Object} data
 * @description 添加帖子
 */
export function add(data) {
  return request({
    url: '^/AppMgr/communityTheme/add',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 修改帖子
 */
export function edit(data) {
  return request({
    url: '^/AppMgr/communityTheme/edit',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Number} id
 * @description 根据id获取帖子信息
 */
export function getThemeById(id) {
  return request({
    url: '^/AppMgr/communityTheme/getThemeById',
    method: 'get',
    params: {
      id
    }
  })
}
/**
 * @method
 * @param {Number} id
 * @description 根据id获取公开报告
 */
export function getreport(data) {
  return request({
    url: '^/AppMgr/exercisejoinreport/publicpage',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Object} data
 * @description 添加一条数据
 */
export function getPlateByRole(data) {
  return request({
    url: '^/AppMgr/communityPlate/getPlateByRole',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Number} id
 * @param {String} col
 * @param {String} value
 * @description 远程校验
 */
export function remoteValid(id, col, value) {
  return request({
    url: '^/AppMgr/communityTheme/validate',
    method: 'post',
    params: {
      id,
      col,
      value
    }
  })
}
/**
 * @method
 * @param {Object} data
 * @description 评论回复分页
 */
export function fetchPage(data) {
  return request({
    url: '^/AppMgr/comment/themeCommentPage',
    method: 'post',
    data
  })
}
/**
 * @method
 * @param {Array} ids
 * @description 删除
 */
export function dele(id) {
  return request({
    url: '^/AppMgr/comment/deleteComment?id=' + id,
    method: 'get'

  })
}
/**
 * @method
 * @param {Array} id
 * @description 增加帖子浏览量
 */
export function addClick(id) {
  return request({
    url: '^/AppMgr/communityTheme/addClick?communityThemeId=' + id,
    method: 'get'
  })
}
