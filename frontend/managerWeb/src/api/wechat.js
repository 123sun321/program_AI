/**
 * Author:Juci
 * Time:2019/01/08 10:35
 */
import request from '@/utils/request'

export function qrCodeCreateTmpTicket(data) {
  return request({
    url: '^/wechat/qrCodeCreateTmpTicket?prefix=' + data,
    method: 'get'
  })
}
