import request from '@/utils/request'

/**
 * @method
 * @param {Object} data
 * @description 新建文件夹
 */
export function addDirectory(data) {
  return request({
    url: '^/AppMgr/directory/add',
    method: 'post',
    data
  })
}

/**
 * @method
 * @param {Object} data
 * @description 获取文件夹列表
 */
export function getDirectoryWithFileList(parentId) {
  return request({
    url: '^/AppMgr/directory/list?parentId=' + parentId,
    method: 'get'
  })
}

/**
 * @method
 * @param {Object} data
 * @description 获取树形列表
 */
export function getTreeList(data) {
  return request({
    url: '^/AppMgr/directory/treeList',
    method: 'get',
    data
  })
}

/**
 * @method
 * @param {Object} data
 * @description 移动文件夹
 */
export function removeFile(data) {
  return request({
    url: '^/AppMgr/directory/removeFile',
    method: 'post',
    data
  })
}

/**
 * @method
 * @param {Object} data
 * @description 文件重命名
 */
export function reName(id, name) {
  return request({
    url: '^/AppMgr/directory/reName',
    method: 'post',
    params: {
      id: id,
      reName: name
    }
  })
}

/**
 * @method
 * @param {Object} data
 * @description 获取资源列表
 */
export function getFileList(data) {
  return request({
    url: '^/AppMgr/fileResource/list',
    method: 'get',
    data
  })
}

/**
 * @method
 * @param {Object} data
 * @description 添加资源
 */
export function addFileResource(data) {
  return request({
    url: '^/AppMgr/fileResource/add',
    method: 'post',
    data
  })
}

/**
 * @method
 * @param {Object} data
 * @description 删除文件夹及资源
 */
export function delBatchesDirectory(ids) {
  return request({
    url: '^/AppMgr/directory/deleteBatches',
    method: 'post',
    data: [...ids]
  })
}

/**
 * @method
 * @param {Object} data
 * @description 上传
 */
export function fecthOne(id) {
  return request({
    url: '/fileResource/get?id=' + id,
    method: 'get'
  })
}

/**
 * @method
 * @param {Object} data
 * @description 上传
 */
export function upload(data) {
  return request({
    url: '/fileResource/upload',
    method: 'post',
    data
  })
}

/**
 * @method
 * @param {Object} data
 * @description 下载
 */
export function downLoad(id) {
  return request({
    url: '/fileResource/downLoad',
    method: 'post',
    params: {
      id
    },
    responseType: 'arraybuffer'
  })
}
