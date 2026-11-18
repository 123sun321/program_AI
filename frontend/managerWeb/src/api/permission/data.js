import request from '@/utils/request'
export function fetchCondition() {
  return request({
    url: '/dataPermission/getCondition',
    method: 'get'
  })
}
export function fetch(roleId) {
  return request({
    url: '/dataPermission/get',
    method: 'get',
    params: {
      roleId
    }
  })
}
export function add(data) {
  return request({
    url: '/dataPermission/add',
    method: 'post',
    data
  })
}
