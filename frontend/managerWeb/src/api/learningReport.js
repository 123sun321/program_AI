import request from '@/utils/request'

export function startCourseAnalyze(data) {
  return request({
    url: '^/AppMgr/ai/startCourseAnalyze',
    method: 'post',
    data,
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
