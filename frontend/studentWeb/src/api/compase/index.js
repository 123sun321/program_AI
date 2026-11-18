import request from '@/utils/request'

export function fecthComposeDependency(params) {
  return request({
    url: '/compose/getDependency',
    method: 'get',
    params
  })
}
