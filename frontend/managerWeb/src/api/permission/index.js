import request from '@/utils/request'

export function getUserPermission() {
  return request({
    url: '/userPermission/list',
    method: 'get'
  })
}

export function fetchPermissions() {
  return request({
    url: '/permission/getList',
    method: 'get'
  })
}
export function syncPermission() {
  return request({
    url: '/permission/syncPermission',
    method: 'get'
  })
}
export function batchPermissions(data) {
  return request({
    url: '/permission/batchInsert',
    method: 'post',
    data: {
      list: data
    }
  })
}
export function fetch(id) {
  return request({
    url: '/permission/get',
    method: 'get',
    params: {
      id
    }
  })
}
export function update(data) {
  return request({
    url: '/permission/update',
    method: 'post',
    data
  })
}
