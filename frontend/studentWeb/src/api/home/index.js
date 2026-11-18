import request from '@/utils/request'

/**
 * @description 功能：获取当前学期
 */
export function getNowSemester() {
  return request({
    url: '/semester/now',
    method: 'get'
  })
}
/**
 * @description 功能：获取班课报告列表
 */
export function getGradeCourseReportList(sid) {
  return request({
    url: '/gradeCourseReport/getReportListBySemesterId',
    method: 'get',
    params: {
      semesterId: sid
    }
  })
}

