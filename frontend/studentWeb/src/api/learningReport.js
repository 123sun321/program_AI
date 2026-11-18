import request from '@/utils/request'

export function startAnalyze() {
  return request({
    url: '^/AppMgr/ai/startAnalyze',
    method: 'post',
    timeout: 10000
  })
}

export function getAnalyzeProgress(taskId) {
  return request({
    url: '^/AppMgr/ai/analyzeProgress',
    method: 'get',
    params: { taskId }
  })
}
