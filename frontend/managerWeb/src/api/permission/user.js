import request from '@/utils/request'
export function fetchList(data) {
  return new Promise((r, j) => {
    request({
      url: '/user/getList',
      method: 'post',
      data: data
    }).then(e => {
      r({ data: e.data.data })
    }).catch(e => {
      j(e)
    })
  })
}

export function getUserInfo(id) {
  return new Promise((r, j) => {
    request({
      url: '/user/get',
      method: 'get',
      params: { id }
    }).then(e => {
      r({ data: e.data.data })
    }).catch(e => {
      j(e)
    })
  })
}

export function saveUserInfo(data) {
  return new Promise((r, j) => {
    request({
      url: '/user/save',
      method: 'post',
      data: data
    }).then(e => {
      r({ data: e.data.data })
    }).catch(e => {
      j(e)
    })
  })
}
export function deleteUser(id) {
  return new Promise((r, j) => {
    request({
      url: '/user/delete',
      method: 'delete',
      params: { id }
    }).then(e => {
      r({ data: e.data.data })
    }).catch(e => {
      j(e)
    })
  })
}
export function resetPassword(account) {
  return new Promise((r, j) => {
    request({
      url: '/user/resetPassword',
      method: 'get',
      params: { account }
    }).then(e => {
      r({ data: e.data.data })
    }).catch(e => {
      j(e)
    })
  })
}
export function getCurrentUserRoles(userId) {
  return request({
    url: '/user/roles',
    method: 'get',
    params: {
      userId
    }

  })
}
export function removeRole(roleId, userId) {
  return request({
    url: '/user/removeRole',
    method: 'post',
    params: {
      roleId,
      userId
    }
  })
}
