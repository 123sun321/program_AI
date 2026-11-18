import axios from 'axios'
import {
  Message
} from 'element-ui'
import {
  getQueryParams
} from './urlUtil'

import store from '@/store'
import router from '@/router'

export function createNewAxiosInstance() {
  const instance = axios.create({
    baseURL: process.env.BASE_API, // api的base_url
    timeout: 5000 * 3 // request timeout
  })
  return instance
}

function getAppid() {
  const paramObj = getQueryParams()
  return paramObj.appid || '123'
}
// create an axios instance
const service = createNewAxiosInstance()

export function requestInterceptor(config) {
  // Do something before request is sent
  if (config.url.substring(0, 1) === '^') {
    config.url = config.url.substr(1)
  } else if (config.url.substring(0, 8) === '/AppFile') {
    config.url = config.url
  } else {
    config.url = process.env.SHOP_API_PREFIX + config.url
  }
  config.headers['X-Requested-With'] = 'XMLHttpRequest'
  config.headers.appid = getAppid()
  return config
}

export function requestErrorInterceptor(error) {
  // Do something with request error
  console.log(error) // for debug
  return Promise.reject(error)
}

// request interceptor
service.interceptors.request.use(requestInterceptor, requestErrorInterceptor)

export function reponseInterceptor(respone) {
  const data = respone.data.data
  if (data && data.total) {
    data.total = parseInt(data.total)
  }
  return respone
}

export function responseErrorInterceptor(error) {
  console.log('err' + error) // for debug
  if (!error.response) {
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  } else {
    const data = error.response.data || {}
    let message = error.message
    if (data.code === 'friend') {
      message = data.data
      Message({
        message: message,
        type: 'error',
        duration: 5 * 1000
      })
    } else {
      if (error.response.status === 401) {
        if (store.getters.login) {
          Message({
            message: '未授权页面，请重新登录',
            type: 'error',
            duration: 5 * 1000
          })
        }
        store.dispatch('LogOut').then(e => {
          router.push('/login')
        })
      }
    }
    return Promise.reject(error)
  }
}

// respone interceptor
service.interceptors.response.use(reponseInterceptor, responseErrorInterceptor)

export default service
