import { Loading } from 'element-ui'
import { createNewAxiosInstance, requestInterceptor, requestErrorInterceptor, reponseInterceptor, responseErrorInterceptor } from './request'

let loadingInstance
function closeLoading(params) {
  if (loadingInstance !== undefined) {
    loadingInstance.close()
  }
}

// create an axios instance
const service = createNewAxiosInstance()

// request interceptor
service.interceptors.request.use(config => {
  loadingInstance = Loading.service({ fullscreen: true })
  return requestInterceptor(config)
}, error => {
  closeLoading()
  requestErrorInterceptor(error)
})

// respone interceptor
service.interceptors.response.use(
  response => {
    closeLoading()
    return reponseInterceptor(response)
  },
  error => {
    closeLoading()
    return responseErrorInterceptor(error)
  }
)

export default service
