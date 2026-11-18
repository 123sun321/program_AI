import axios from 'axios'
import { Message, MessageBox } from 'element-ui'

import store from '@/store'
import { getToken } from '@/utils/auth'
import { setToken } from '@/utils/auth'
import { getQueryParams } from './urlUtil'

export function createNewAxiosInstance() {
  const instance = axios.create({
    baseURL: process.env.BASE_API, // api的base_url
    timeout: 5000 // request timeout
  })
  return instance
}
// create an axios instance
const service = createNewAxiosInstance()

function getAppid() {
  const paramObj = getQueryParams()
  return paramObj.appid
}

export function requestInterceptor(config) {
  // Do something before request is sent
  if (config.url.substring(0, 1) === '^') {
    config.url = config.url.substr(1)
  } else {
    config.url = process.env.SHOP_API_PREFIX + config.url
  }
  if (process.env.LoginType === 'token' && store.getters.token) {
    // config.headers['X-Token'] = getToken() // 让每个请求携带token-- ['X-Token']为自定义key 请根据实际情况自行修改
    config.headers.Authorization = `${getToken()}`
    config.headers['X-Requested-With'] = 'XMLHttpRequest'
    if (!config.url.includes('userinfo')) {
      config.headers.app_id = getAppid()
    }
  } else if (process.env.LoginType === 'session') {
    config.headers['X-Requested-With'] = 'XMLHttpRequest'
    if (!config.url.includes('userinfo')) {
      config.headers.app_id = getAppid()
    }
  }
  return config
}

export function requestErrorInterceptor(error) {
  // Do something with request error
  console.log(error) // for debug
  Promise.reject(error)
}

// request interceptor
service.interceptors.request.use(requestInterceptor, requestErrorInterceptor)

export function reponseInterceptor(respone) {
  if (respone.data && respone.data.total && typeof respone.data.total === 'string') {
    respone.data.total = parseInt(respone.data.total)
  }
  return respone
}

export function responseErrorInterceptor(error) {
  console.log('err' + error)// for debug
  if (!error.response) {
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
  const res = error.response.data
  if (res.code === 'reToken') {
    setToken(res.data)
    error.config.headers.Authorization = res.data
    return axios.request(error.config)
  } else if (res.code === 'reLogin') {
    if (!store.getters.reLoginTip) {
      store.commit('SET_RELOGINTIP', true)
      MessageBox.confirm('你已被登出，可以取消继续留在该页面，或者重新登录', '确定登出', {
        confirmButtonText: '重新登录',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        store.dispatch('FedLogOut').then(() => {
          location.reload()// 为了重新实例化vue-router对象 避免bug
        })
      })
    }
    return Promise.reject('error')
  } else {
    const data = error.response.data || {}
    let message = error.message
    if (data.code === 'friend') {
      message = data.data
    }
    console.log(error)
    Message({
      message: message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
}

// respone interceptor
service.interceptors.response.use(reponseInterceptor, responseErrorInterceptor)

export default service
