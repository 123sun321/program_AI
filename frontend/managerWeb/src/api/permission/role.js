import request from '@/utils/request'

export function fetchList(data) {
  return new Promise((r, j) => {
    request({
      url: '/role/list',
      method: 'post',
      data: data
    }).then(e => {
      r({ data: e.data.data })
    }).catch(e => {
      j(e)
    })
  })
}

export function getRoleInfo(id) {
  return new Promise((r, j) => {
    request({
      url: '/role/',
      method: 'get',
      params: { id }
    }).then(e => {
      r({ data: e.data.data })
    }).catch(e => {
      j(e)
    })
  })
}

export function deleteRole(id) {
  return new Promise((r, j) => {
    request({
      url: '/role/',
      method: 'delete',
      params: { id }
    }).then(e => {
      r({ data: e.data.data })
    }).catch(e => {
      j(e)
    })
  })
}

export function createRole(data) {
  return new Promise((r, j) => {
    request({
      url: '/role/create/',
      method: 'post',
      data: data
    }).then(e => {
      r({ data: e.data.data })
    }).catch(e => {
      j(e)
    })
  })
}

export function updateRole(data) {
  return new Promise((r, j) => {
    request({
      url: '/role/update/',
      method: 'post',
      data: data
    }).then(e => {
      r({ data: e.data.data })
    }).catch(e => {
      j(e)
    })
  })
}

export function getUserListByRole(data) {
  return new Promise((r, j) => {
    request({
      url: '/role/userList/',
      method: 'post',
      data: data
    }).then(e => {
      r({ data: e.data.data })
    }).catch(e => {
      j(e)
    })
  })
}

export function getUserListWithRoleStatus(data) {
  return new Promise((r, j) => {
    request({
      url: '/role/userList/roleStatus',
      method: 'post',
      data: data
    }).then(e => {
      r({ data: e.data.data })
    }).catch(e => {
      j(e)
    })
  })
}

export function removeUser(roleId, userId) {
  return new Promise((r, j) => {
    request({
      url: '/role/userList/',
      method: 'delete',
      params: { roleId, userId }
    }).then(e => {
      r({ data: e.data.data })
    }).catch(e => {
      j(e)
    })
  })
}

export function addUser(roleId, userId) {
  return new Promise((r, j) => {
    request({
      url: '/role/userList/add',
      method: 'post',
      params: { roleId, userId }
    }).then(e => {
      r({ data: e.data.data })
    }).catch(e => {
      j(e)
    })
  })
}

