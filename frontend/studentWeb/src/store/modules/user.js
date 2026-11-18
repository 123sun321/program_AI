import { loginByUsername, logout, getUserInfo } from '@/api/login'
import { getUserPermission } from '@/api/permission/index'
import { getNoReadCount } from '@/api/person/siteMessage'
const user = {
  state: {
    user: '',
    userId: '',
    status: '',
    code: '',
    name: '',
    avatar: '',
    className: '',
    introduction: '',
    roles: [],
    setting: {
      articlePlatform: []
    },
    permissions: [], // 功能点
    isExtractPermission: false,
    noReadMsgCount: 0, // 未读消息数量
    schoolName: '',
    extraUserInfo: false,
    noScheduleCount: 0, // 待办事项数量
    login: false
  },

  mutations: {
    SET_RELOGINTIP: (state, isTip) => {
      state.reLoginTip = isTip
    },
    SET_CODE: (state, code) => {
      state.code = code
    },

    SET_EXTRAPERMISSION: (state, isExtract) => {
      state.isExtractPermission = isExtract
    },

    SET_USERID: (state, userId) => {
      state.userId = userId
    },
    SET_INTRODUCTION: (state, introduction) => {
      state.introduction = introduction
    },
    SET_SETTING: (state, setting) => {
      state.setting = setting
    },
    SET_STATUS: (state, status) => {
      state.status = status
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS(state, permissions) {
      state.permissions = permissions
    },
    SET_NOREADMSGCOUNT(state, noReadMsgCount) {
      state.noReadMsgCount = noReadMsgCount
    },

    SET_SCHOOLNAME(state, schoolName) {
      state.schoolName = schoolName
    },
    SET_EXTRAUSERINFO(state, isextra) {
      state.extraUserInfo = isextra
    },
    SET_LOGIN(state, value) {
      state.login = value
    },
    SET_CLASSNAME(state, value) {
      state.className = value
    }
  },

  actions: {
    // 用户名登录
    LoginByUsername({ commit }, userInfo) {
      // const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        loginByUsername(userInfo).then(response => {
          commit('SET_LOGIN', true)
          // const data = response.data
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取功能点(权限)
    GetUserPermissions({ commit }) {
      return new Promise((resolve, reject) => {
        getUserPermission().then(e => {
          commit('SET_PERMISSIONS', e.data.data)
          commit('SET_EXTRAPERMISSION', true)
          resolve(e.data.data)
        }).catch(e => {
          reject(e)
        })
      })
    },

    // 获取用户信息
    GetUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo(state.token).then(response => {
          if (!response.data) { // 由于mockjs 不支持自定义状态码只能这样hack
            reject('error')
          }
          const data = response.data
          commit('SET_LOGIN', true)
          commit('SET_ROLES', data.roles)
          commit('SET_NAME', data.name)
          commit('SET_USERID', data.id)
          commit('SET_AVATAR', data.avatar)
          commit('SET_INTRODUCTION', data.introduction)
          commit('SET_SCHOOLNAME', data.schoolName)
          commit('SET_EXTRAUSERINFO', true)
          commit('SET_CLASSNAME', data.className)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 第三方验证登录
    // LoginByThirdparty({ commit, state }, code) {
    //   return new Promise((resolve, reject) => {
    //     commit('SET_CODE', code)
    //     loginByThirdparty(state.status, state.email, state.code).then(response => {
    //       commit('SET_TOKEN', response.data.token)
    //       setToken(response.data.token)
    //       resolve()
    //     }).catch(error => {
    //       reject(error)
    //     })
    //   })
    // },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout().then(() => {
          commit('SET_PERMISSIONS', [])
          commit('SET_EXTRAPERMISSION', false)
          commit('SET_EXTRAUSERINFO', false)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        resolve()
      })
    },

    // 动态修改权限
    ChangeRoles({ commit }, role) {
      return new Promise(resolve => {
        getUserInfo(role).then(response => {
          const data = response.data
          commit('SET_ROLES', data.roles)
          commit('SET_NAME', data.name)
          commit('SET_AVATAR', data.avatar)
          commit('SET_INTRODUCTION', data.introduction)
          resolve()
        })
      })
    },
    // 获取用户未读消息数
    GetNoReadMsgCount({ commit }) {
      return new Promise((resolve, reject) => {
        getNoReadCount().then(response => {
          if (!response.data) { // 由于mockjs 不支持自定义状态码只能这样hack
            reject('error')
          }
          const data = response.data
          commit('SET_NOREADMSGCOUNT', data.data)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default user
