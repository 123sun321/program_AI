import request from '@/utils/request'

export function sendAiMessage(data) {
  return request({
    url: '^/AppMgr/ai/chat',
    method: 'post',
    data,
    timeout: 60000
  })
}
