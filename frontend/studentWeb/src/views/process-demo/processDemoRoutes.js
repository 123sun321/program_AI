export const processDemoRoutes = [
  {
    path: '/process-demo/records',
    name: 'ProcessDemoRecords',
    component: () => import('./ProcessDemoRecords.vue')
  },
  {
    path: '/process-demo/replay',
    name: 'ProcessDemoReplay',
    component: () => import('./ProcessDemoReplay.vue')
  },
  {
    path: '/process-demo/analysis',
    name: 'ProcessDemoAnalysis',
    component: () => import('./ProcessDemoAnalysis.vue')
  },
  {
    path: '/process-demo/report',
    name: 'ProcessDemoReport',
    component: () => import('./ProcessDemoReport.vue')
  }
]

export default processDemoRoutes
