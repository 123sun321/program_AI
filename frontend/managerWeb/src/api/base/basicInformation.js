import request from '@/utils/request'

// 1. 获取学校信息-logo
export function getSchoolInfo() {
  return request({
    url: '^/school/schoolLogo',
    method: 'post'
  })
}
// 2. 设置学校logo
export function setSchoolLogo(data) {
  return request({
    url: '^/school/setLogo',
    method: 'put',
    params: data
  })
}
