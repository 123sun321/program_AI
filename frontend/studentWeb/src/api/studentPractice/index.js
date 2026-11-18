/**
 * @method
 * @description 获取学生练习记录
 */
import request from '@/utils/request'
// import mock from '../../mock/index'
export function getStuPracticeInfo(url, stuId, dataId) {
  return request({
    url: '/training-path/home',
    method: 'GET',
    params: {
      stuID: 999
    }
  })
}

export function getStuAllPractices(url, stuId) {
  // console.log('发送了**************************')
  return request({
    url: '/training-path/showAll',
    method: 'GET',
    params: {
      stuID: 999
    }
  })
}

export function delFilesById(url, stuId, dataId) {
  console.log('发送请求的URL:' + url + '?stuId=' + stuId + '&dataId=' + dataId)
  return []
}

export function addFolder(stuId, dataId, folderName) {
  console.log('发送了**************************')
  console.log('stuId:' + stuId + 'dataId:' + dataId + 'folderName:' + folderName)
  return request({
    url: '/training-path/addFolder',
    method: 'GET',
    params: {
      stuId,
      dataId,
      folderName
    }
  })
}
