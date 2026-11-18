import Vue from 'vue'
import Router from 'vue-router'
const _import = require('./_import_' + process.env.NODE_ENV)
// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/** note: submenu only apppear when children.length>=1
 *   detail see  https://panjiachen.github.io/vue-element-admin-site/#/router-and-nav?id=sidebar
 **/

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    roles: ['admin','editor']     will control the page roles (you can set multiple roles)
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
    noCache: true                if true ,the page will no be cached(default is false)
  }
**/

export const constantRouterMap = [{
  path: '/login',
  component: _import('login/index'),
  hidden: true
},
{
  path: '/authredirect',
  component: _import('login/authredirect'),
  hidden: true
},
{
  path: '/404',
  component: _import('errorPage/404'),
  hidden: true
},
{
  path: '/401',
  component: _import('errorPage/401'),
  hidden: true
},
{
  path: '/preview',
  component: _import('execute/preview'),
  hidden: true
},
{
  path: '/user',
  name: 'user',
  component: Layout,
  redirect: 'userInfo',
  hidden: true,
  children: [
    {
      path: 'info/:id',
      component: _import('user/index'),
      name: 'userInfo',
      meta: {
        title: 'userInfo',
        noCache: true
      }
    }
  ]
},
// 课程管理
{
  path: '/mycourse',
  name: 'mycourse',
  component: Layout,
  redirect: 'course',
  children: [{
    path: 'course',
    component: _import('mycourse/index'),
    name: 'mycourselayout',
    meta: {
      title: 'coursemanage',
      icon: 'home',
      noCache: true
    },
    children: [
      {
        path: 'index',
        component: _import('mycourse/components/course'),
        name: 'mycourseindex',
        meta: {
          title: 'course',
          noCache: true
        }
      },
      {
        path: 'exercises',
        component: _import('mycourse/components/exercises'),
        name: 'exercises',
        meta: {
          title: 'exercises',
          noCache: true
        }
      },
      {
        path: 'trainingPath',
        component: _import('trainingPath/index'),
        name: 'trainingPath',
        meta: {
          title: 'trainingPath',
          noCache: true
        }
      },
      {
        path: 'report',
        component: _import('mycourse/components/report'),
        name: 'report',
        meta: {
          title: 'report',
          noCache: true
        }
      },
      {
        path: 'discussion',
        component: _import('mycourse/components/discussion'),
        name: 'discussion',
        meta: {
          title: 'discussion',
          noCache: true
        }
      },
      {
        path: 'assignment',
        component: _import('mycourse/components/assignment/index'),
        name: 'assignment',
        meta: {
          title: 'assignment',
          noCache: true
        }
      }
    ]
  }]
},
{
  path: '/assignment',
  name: 'assignment',
  component: Layout,
  redirect: 'index',
  children: [
    {
      path: 'submitassignment',
      component: _import('mycourse/components/assignment/run'),
      name: 'submitassignment',
      meta: {
        title: 'submitassignment',
        noCache: true
      },
      hidden: true
    }]
},
// 个人中心
{
  path: '/personinfo',
  name: 'personinfo',
  component: Layout,
  redirect: 'personinfo',
  hidden: true,
  children: [{
    path: 'personinfo',
    component: _import('personinfo/index'),
    name: 'personinfo',
    meta: {
      title: 'personinfo',
      icon: 'home',
      noCache: true
    },
    children: [
      {
        path: 'userinfo',
        component: _import('personinfo/userinfo'),
        name: 'userinfo',
        meta: {
          title: 'userinfo',
          noCache: true
        }
      },
      {
        path: 'password',
        component: _import('personinfo/password'),
        name: 'password',
        meta: {
          title: 'password',
          noCache: true
        }
      },
      {
        path: 'message',
        component: _import('personinfo/message'),
        name: 'message',
        meta: {
          title: 'message',
          noCache: true
        }
      },
      {
        path: 'fileResource',
        component: _import('personinfo/fileResource/index'),
        name: 'fileResource',
        meta: {
          title: 'fileResource',
          noCache: true
        }
      },
      {
        path: 'personalScore',
        component: _import('personinfo/personalScore'),
        name: 'personalScore',
        meta: {
          title: 'personalScore',
          noCache: true
        }
      }
    ]
  }]
},
{
  path: '',
  name: 'dashboard',
  component: Layout,
  redirect: 'dashboard',
  hidden: true,
  children: [{
    path: 'dashboard',
    component: _import('dashboard/index'),
    name: 'dashboard',
    meta: {
      title: 'dashboard',
      icon: 'dashboard',
      noCache: true
    }
  }]
},
// 课程库
{
  path: '/course',
  name: 'course',
  component: Layout,
  redirect: 'index',
  children: [{
    path: 'courseindex',
    component: _import('course/index'),
    name: 'index',
    meta: {
      title: 'courses',
      icon: 'course',
      noCache: true
    }
  }, {
    path: 'courseDetail',
    component: _import('course/courseDetail'),
    name: 'courseDetail',
    meta: {
      title: 'courseDetail',
      icon: 'course',
      noCache: true
    },
    hidden: true
  }]
},
// 案例库
{
  path: '/caselibrary',
  name: 'caselibrary',
  component: Layout,
  redirect: 'index',
  children: [{
    path: 'index',
    component: _import('caselibrary/index'),
    name: 'caselibraryindex',
    meta: {
      title: 'caselibrary',
      icon: 'caselibrary',
      noCache: true
    }
  },
  {
    path: 'reportdetail',
    component: _import('caselibrary/reportDetail'),
    name: 'reportdetail',
    hidden: true,
    meta: {
      title: 'reportDetail',
      noCache: true
    }
  },
  {
    path: 'running',
    component: _import('caselibrary/running'),
    name: 'running',
    hidden: true,
    meta: {
      title: 'running',
      noCache: true
    }
  },
  {
    path: 'exercisesstep',
    component: _import('caselibrary/exercisesstep'),
    name: 'exercisesstep',
    hidden: true,
    meta: {
      title: 'exercisesstep',
      noCache: true
    }
  }]
},
{
  path: '/studypath',
  name: 'studypath',
  component: Layout,
  redirect: 'index',
  children: [{
    path: 'index',
    component: _import('studypath/index'),
    name: 'studypathindex',
    meta: {
      title: 'studypath',
      icon: 'studypath',
      noCache: true
    }
  }]
},
{
  path: '/training',
  name: 'training',
  component: Layout,
  redirect: 'index',
  children: [{
    path: 'index',
    component: _import('training/index'),
    name: 'trainingindex',
    meta: {
      title: 'training',
      icon: 'studypath',
      noCache: true
    }
  }, {
    path: 'runtraining',
    component: _import('training/run'),
    name: 'runtraining',
    meta: {
      title: 'runtraining',
      noCache: true
    },
    hidden: true
  }]
},
{
  path: '/commend',
  name: 'commend',
  component: Layout,
  redirect: 'index',
  children: [{
    path: 'index',
    component: _import('commend/index'),
    name: 'commendindex',
    meta: {
      title: 'commend',
      icon: 'commend',
      noCache: true
    }
  },
  {
    path: 'commendrun',
    component: _import('commend/run'),
    name: 'commendrun',
    hidden: true,
    meta: {
      title: 'commendrun',
      noCache: true
    }
  }]
},
{
  path: '/community',
  name: 'community',
  component: Layout,
  redirect: 'index',
  children: [{
    path: 'index',
    component: _import('community/index'),
    name: 'communityindex',
    meta: {
      title: 'community',
      icon: 'community',
      noCache: true
    }
  },
  {
    path: 'topic',
    component: _import('community/topic'),
    name: 'topic',
    meta: {
      title: 'topic',
      icon: 'topic',
      noCache: true
    },
    hidden: true
  },
  {
    path: 'reportDetail',
    component: _import('community/reportDetail'),
    name: 'reportDetail',
    hidden: true,
    meta: {
      title: 'reportDetail',
      noCache: true
    }
  }]
},
// 工具
{
  path: '/tools',
  name: 'tools',
  component: Layout,
  redirect: 'codeEditor',
  children: [
    {
      path: 'codeEditor',
      component: _import('tools/codeEditor/index'),
      name: 'codeEditor',
      meta: {
        title: 'codeEditor',
        icon: 'tool',
        noCache: true
      }
    }
    // {
    //   path: 'swagger',
    //   component: _import('tools/Swagger/index'),
    //   name: 'swagger',
    //   meta: {
    //     title: 'swagger',
    //     icon: 'tool',
    //     noCache: true
    //   }
    // }
  ]
},
// 学习报告
{
  path: '/learning-report',
  name: 'learningReport',
  component: Layout,
  redirect: 'index',
  children: [{
    path: 'index',
    component: _import('learningReport/index'),
    name: 'learningReportIndex',
    meta: {
      title: '学习报告',
      icon: 'report',
      noCache: true
    }
  }]
},
// 过程回放演示
{
  path: '/process-demo',
  name: 'processDemo',
  component: Layout,
  redirect: 'analysis',
  hidden: true,
  children: [{
    path: 'analysis',
    component: _import('process-demo/ProcessDemoAnalysis'),
    name: 'ProcessDemoAnalysis',
    meta: {
      title: '过程分析',
      noCache: true
    }
  }, {
    path: 'records',
    component: _import('process-demo/ProcessDemoRecords'),
    name: 'ProcessDemoRecords',
    meta: {
      title: '证据链',
      noCache: true
    }
  }, {
    path: 'replay',
    component: _import('process-demo/ProcessDemoReplay'),
    name: 'ProcessDemoReplay',
    meta: {
      title: '过程回放',
      noCache: true
    }
  }, {
    path: 'report',
    component: _import('process-demo/ProcessDemoReport'),
    name: 'ProcessDemoReport',
    meta: {
      title: '反馈报告',
      noCache: true
    }
  }]
}
]
export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  { path: '*', redirect: '/404', hidden: true }
]
