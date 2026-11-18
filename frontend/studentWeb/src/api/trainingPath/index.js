/**
 * @file 学生练习列表相关
 * @author 宋乃乐
 * @copyright zhengzhouchuanghzi
 * @since 2019.3.23
 */
import request from '@/utils/request'
/**
 * @method
 * @param {Number} dataId
 * @description 得到某个id下的文件和文件夹
 */
export function getTrainingPathInfo(dataId) {
  return request({
    url: '/training-path/getFolderList',
    method: 'GET',
    params: {
      dataId
    }
  })
}
/**
 * @method
 * @description 得到所有的练习记录(去除文件夹)
 */
export function getStuAllPractices(data) {
  return request({
    url: '/training/pageOneself',
    method: 'POST',
    data
  })
}
/**
 * @method
 * @param {Array} data
 * @description 删除对应的练习列表
 */
export function delFilesByIds(data) {
  return request({
    url: '/training-path/delFolder',
    method: 'POST',
    data: [...data]
  })
}
/**
 * @method
 * @param {Number} dataId 父级id
 * @param {String} folderName 文件夹名称
 * @description 添加文件夹
 */
export function addFolder(dataId, folderName) {
  return request({
    url: '/training-path/addFolder',
    method: 'POST',
    params: {
      dataId,
      folderName
    }
  })
}
/**
 * @method
 * @param {Number} folderId 需要修改的文件夹id
 * @param {String} folderName 文件夹名称
 * @description 修改文件夹名称
 */
export function editFolderName(folderId, folderName) {
  return request({
    url: '/training-path/editFolderName',
    method: 'POST',
    params: {
      folderId,
      folderName
    }
  })
}
/**
 * @method
 * @description 文件夹树形列表
 */
export function getTreeList() {
  return request({
    url: '/training-path/getTrainingPathTreeSet',
    method: 'POST'
  })
}

/**
 * @method
 * @description 批量移动文件和文件夹
 */
export function removeFile(removeFile) {
  return request({
    url: '/training-path/moveFoldersAndFiles',
    method: 'POST',
    data: removeFile
  })
}
