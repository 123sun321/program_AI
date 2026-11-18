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
  path: '/person',
  name: 'person',
  component: Layout,
  redirect: 'person',
  hidden: true,
  children: [
    {
      path: 'changePassword',
      component: _import('person/changePassword'),
      name: 'changePassword',
      meta: {
        title: 'changePassword',
        icon: 'changePassword',
        noCache: true
      }
    },
    {
      path: 'message',
      component: _import('person/message'),
      name: 'message',
      meta: {
        title: 'message',
        noCache: true
      }
    },
    {
      path: 'notifySetting',
      component: _import('person/notifySetting'),
      name: 'notifySetting',
      meta: {
        title: 'notifySetting',
        noCache: true
      }
    },
    {
      path: 'userInfo',
      component: _import('person/userInfo'),
      name: 'userInfo',
      meta: {
        title: 'userInfo',
        noCache: true
      }
    }
  ]
},
{
  path: '',
  name: 'dashboard',
  component: Layout,
  redirect: 'dashboard',
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
}
]
export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  { path: '*', redirect: '/404', hidden: true },
  {
    path: '/basicInformation',
    component: Layout,
    name: 'basicInformation',
    redirect: '/basicInformation/index',
    meta: {
      title: 'basicInformation',
      icon: 'component',
      permissions: ['profession']
    },
    children: [
      {
        path: 'profession',
        component: _import('basicInformation/profession/index'),
        name: 'profession',
        meta: {
          title: 'profession',
          noCache: true,
          permissions: ['profession']
        }
      }, // 专业
      {
        path: 'class',
        component: _import('layout/router'),
        name: 'class',
        redirect: '/basicInformation/class/index',
        meta: {
          title: 'class',
          permissions: ['student']
        },
        children: [{
          path: 'index',
          component: _import('basicInformation/class/index'),
          name: 'class',
          hidden: true,
          meta: {
            title: 'class',
            noCache: true
          }
        },
        {
          path: 'classDetail/:professionId/:classId',
          component: _import('basicInformation/class/detail'),
          name: 'classDetail',
          meta: {
            title: 'classDetail',
            noCache: true
          },
          hidden: true
        } // 我的班课详情
        ]
      }, // 班级
      {
        path: 'student',
        component: _import('layout/router'),
        name: 'student',
        redirect: '/basicInformation/student/index',
        meta: {
          title: 'student',
          permissions: ['student']
        },
        children: [
          {
            path: 'index',
            component: _import('basicInformation/student/index'),
            name: 'student',
            hidden: true,
            meta: {
              title: 'student',
              noCache: true
            }
          },
          {
            path: 'detailStudent/:id',
            component: _import('basicInformation/student/detailStudent'),
            name: 'detailStudent',
            hidden: true,
            meta: {
              title: 'detailStudent',
              noCache: true
            }
          } // 学生学习情况
        ]
      }, // 学生
      {
        path: 'teacher',
        component: _import('basicInformation/teacher/index'),
        name: 'teacher',
        meta: {
          title: 'teacher',
          noCache: true,
          permissions: ['teacher']
        }
      }, // 教师
      {
        path: 'label',
        component: _import('basicInformation/label/index'),
        name: 'label',
        meta: {
          title: 'label',
          noCache: true,
          permissions: ['label']
        }
      } // 标签
    ]
  }, // 基础信息
  {
    path: '/coursemanage',
    component: Layout,
    name: 'coursemanage',
    redirect: '/coursemanage/course',
    meta: {
      title: 'coursemanage',
      icon: 'example',
      permissions: ['course:schoolPage']
    },
    children: [
      {
        path: 'course',
        component: _import('layout/router'),
        name: 'course',
        redirect: '/coursemanage/course/index',
        meta: {
          title: 'course'
        },
        children: [
          {
            path: 'index',
            component: _import('coursemanage/course/index'),
            name: 'courseindex',
            meta: {
              title: 'courseindex',
              noCache: true
            },
            hidden: true
          },
          {
            path: 'coursedetail',
            component: _import('coursemanage/course/courseDetail'),
            name: 'coursedetail',
            meta: {
              title: 'coursedetail',
              noCache: true
            },
            hidden: true
          }

        ]
      }, // 所有课程
      {
        path: 'exercise',
        component: _import('layout/router'),
        name: 'exercise',
        redirect: '/coursemanage/exercise/index',
        meta: {
          title: 'exercise',
          permissions: ['exercise']
        },
        children: [
          {
            path: 'index',
            component: _import('coursemanage/exercise/index'),
            name: 'exerciseindex',
            meta: {
              title: 'exerciseindex',
              noCache: true
            },
            hidden: true
          },
          {
            path: 'joinexercise',
            component: _import('coursemanage/exercise/joininfo'),
            name: 'joinexercise',
            meta: {
              title: 'joinexercise',
              noCache: true
            },
            hidden: true
          },
          {
            path: 'exerciseinfo',
            component: _import('coursemanage/exercise/exerciseinfo'),
            name: 'exerciseinfo',
            meta: {
              title: 'exerciseinfo',
              noCache: true
            },
            hidden: true
          },
          {
            path: 'addexercise',
            component: _import('coursemanage/exercise/components/index'),
            name: 'addexercise',
            hidden: true,
            meta: {
              title: 'addexercise',
              noCache: true
            }
          }
        ]
      }, // 实验管理
      {
        path: 'pathmanage',
        component: _import('layout/router'),
        name: 'manage',
        redirect: '/coursemanage/pathmanage/index',
        meta: {
          title: 'manage',
          permissions: ['course:schoolPage']
        },
        children: [
          {
            path: 'index',
            component: _import('coursemanage/pathmanage/index'),
            name: 'manageindex',
            meta: {
              title: 'manageindex',
              noCache: true
            },
            hidden: true
          },
          {
            path: 'studyDetail',
            component: _import('coursemanage/pathmanage/study'),
            name: 'studyDetail',
            meta: {
              title: 'studyDetail',
              noCache: true
            },
            hidden: true
          }
        ]
      }, // 课程路径
      {
        path: 'recommendManage',
        component: _import('layout/router'),
        name: 'recommendManage',
        redirect: '/coursemanage/recommendManage/index',
        meta: {
          title: 'recommendManage'
        },
        children: [
          {
            path: 'index',
            component: _import('coursemanage/recommendManage/index'),
            name: 'recommendManageIndex',
            meta: {
              title: 'recommendManage',
              noCache: true
            },
            hidden: true
          },
          {
            path: 'visitRecommend',
            component: _import('coursemanage/recommendManage/visitRecommend'),
            name: 'visitRecommend',
            meta: {
              title: 'visitRecommend',
              noCache: true
            },
            hidden: true
          }
        ]
      } // 优秀作品管理
    ]
  }, // 课程管理
  {
    path: '/coursemanage',
    component: Layout,
    name: 'coursemanage',
    redirect: '/coursemanage/index',
    meta: {
      title: 'coursemanage',
      permissions: ['course:teacherPage']
    },
    children: [
      {
        path: 'index',
        component: _import('coursemanage/teacher/index'),
        name: 'teachercourseindex',
        meta: {
          title: 'courseindex',
          icon: 'example',
          noCache: true
        }
      },
      {
        path: 'teacherCoursedetail',
        component: _import('coursemanage/teacher/courseDetail'),
        name: 'teacherCoursedetail',
        meta: {
          title: 'coursedetail',
          noCache: true
        },
        hidden: true
      },
      {
        path: 'joinexercise',
        component: _import('coursemanage/exercise/joininfo'),
        name: 'joinexercise',
        meta: {
          title: 'joinexercise',
          noCache: true
        },
        hidden: true
      },
      {
        path: 'exerciseinfo',
        component: _import('coursemanage/exercise/exerciseinfo'),
        name: 'exerciseinfo',
        meta: {
          title: 'exerciseinfo',
          noCache: true
        },
        hidden: true
      },
      {
        path: 'addexercise',
        component: _import('coursemanage/exercise/components/index'),
        name: 'addexercise',
        hidden: true,
        meta: {
          title: 'addexercise',
          noCache: true
        }
      }
    ]
  }, // 教师课程管理
  {
    path: '/assignmentmanage',
    component: Layout,
    name: 'assignmentmanage',
    meta: {
      title: 'assignmentmanage',
      icon: 'example',
      permissions: ['teacherManager']

    },
    children: [
      {
        path: 'index',
        component: _import('assignmentmanage/assign/index'),
        name: 'assign',
        meta: {
          icon: 'chart',
          title: 'assign',
          noCache: true
        }
      },
      {
        path: 'submit',
        component: _import('assignmentmanage/assign/submit'),
        name: 'submit',
        meta: {
          title: 'submit',
          noCache: true
        },
        hidden: true
      },
      {
        path: 'assignmentDetail',
        component: _import('assignmentmanage/assign/assignmentDetail'),
        name: 'assignmentDetail',
        meta: {
          title: 'assignmentDetail',
          noCache: true
        },
        hidden: true
      },
      {
        path: 'codeDetail',
        component: _import('assignmentmanage/assign/codeDetail'),
        name: 'codeDetail',
        meta: {
          title: 'codeDetail',
          noCache: true
        },
        hidden: true
      },
      {
        path: 'codeSimilarity',
        component: _import('assignmentmanage/assign/codeSimilarity'),
        name: 'codeSimilarity',
        meta: {
          title: 'codeSimilarity',
          noCache: true
        },
        hidden: true
      },
      {
        path: 'mergely',
        component: _import('assignmentmanage/mergely/index'),
        name: 'mergelyIndex',
        meta: {
          title: 'mergely',
          noCache: true
        },
        hidden: true
      },
      {
        path: 'globalMergely',
        component: _import('assignmentmanage/mergely/globalMergely'),
        name: 'globalMergely',
        meta: {
          title: 'globalMergely',
          noCache: true
        },
        hidden: true
      }

    ]
  }, // 作业管理
  {
    path: '/community',
    component: Layout,
    name: 'community',
    redirect: '/community/index',
    meta: {
      title: 'community',
      icon: 'form',
      permissions: ['schoolManager']
    },
    children: [
      {
        path: 'signmanager',
        component: _import('layout/router'),
        name: 'signmanager',
        redirect: '/community/signmanager/index',
        meta: {
          title: 'signmanager',
          noCache: true,
          permissions: ['signRecord']
        }, // 签到管理
        children: [
          {
            path: 'index',
            component: _import('community/signmanager/index'),
            name: 'signmanager',
            meta: {
              title: 'signmanager',
              noCache: true
            },
            hidden: true
          },
          {
            path: 'signDetail',
            component: _import('community/signmanager/signDetail'),
            name: 'signDetail',
            meta: {
              title: 'signDetail',
              noCache: true
            },
            hidden: true
          }
        ]
      },
      // 板块列表
      {
        path: 'platemanage',
        component: _import('community/platemanage/index'),
        name: 'platemanage',
        meta: {
          title: 'platemanage',
          noCache: true
        }
      },
      {
        path: 'theme',
        component: _import('layout/router'),
        redirect: '/community/theme/index',
        name: 'themeDetail',
        meta: {
          title: 'themeDetail',
          noCache: true
        },
        children: [
          {
            path: 'index',
            component: _import('community/theme/index'),
            name: 'themeDetail',
            meta: {
              title: 'themeDetail',
              noCache: true
            },
            hidden: true
          },
          {
            path: 'replyTheme',
            component: _import('community/theme/replyTheme'),
            name: 'replyTheme',
            hidden: true,
            meta: {
              title: 'replyTheme',
              noCache: true
            }
          }
        ]
      }
    ]
  }, // 社区管理
  {
    path: '/myfileresource',
    component: Layout,
    redirect: '/myfileresource/index',
    name: 'myfileresource',
    meta: {
      title: 'myfileresource',
      icon: 'file'
    },
    children: [
      {
        path: 'fileResource',
        component: _import('myfileresource/fileResource/index'),
        name: 'fileResource',
        meta: {
          icon: 'file',
          title: 'fileResource',
          noCache: true
        }
      }
    ]
  }, // 我的资源
  {
    path: '/Initialization',
    component: Layout,
    redirect: '/Initialization/index',
    name: 'Initialization',
    meta: {
      title: 'Initialization',
      icon: 'file'
    },
    children: [
      {
        path: 'Initialization',
        component: _import('Initialization/index'),
        name: 'Initialization',
        meta: {
          icon: 'file',
          title: 'Initialization',
          noCache: true
        }
      }
    ]
  }, // 初始化数据
  {
    path: '/teachercommunity',
    component: Layout,
    name: 'teachercommunity',
    meta: {
      title: 'teachercommunity',
      permissions: ['systemManager']
    },
    children: [
      {
        path: 'index',
        component: _import('teachercommunity/index'),
        name: 'teachercommunity',
        meta: {
          icon: 'peoples',
          title: 'teachercommunity',
          noCache: true
        }
      },
      {
        path: 'topic',
        component: _import('teachercommunity/topic'),
        name: 'topic',
        meta: {
          icon: 'peoples',
          title: 'topic',
          noCache: true
        },
        hidden: true
      },
      {
        path: 'reportdetail',
        component: _import('teachercommunity/reportDetail'),
        name: 'reportdetail',
        hidden: true,
        meta: {
          title: 'reportDetail',
          noCache: true
        }
      }
    ]
  }, // 教师社区
  {
    path: '/systemManager',
    name: 'systemManager',
    component: Layout,
    meta: {
      title: 'systemManager',
      icon: 'lock',
      permissions: ['systemManager']
    },
    children: [
      {
        path: 'dictionary',
        component: _import('dictionary/index'),
        name: 'dictionary',
        meta: {
          title: 'dictionary',
          noCache: false
        }
      },
      {
        path: 'user',
        component: _import('system/user/index'),
        name: 'userManager',
        meta: {
          title: 'userManager',
          noCache: true
        }
      }, // 用户管理
      {
        path: 'role',
        component: _import('system/role/index'),
        name: 'roleManager',
        // redirect: 'index',
        meta: {
          title: 'roleManager',
          noCache: true
        }
      }, // 角色管理
      {
        path: 'roleUser/:id',
        component: _import('system/role/components/roleUserList'),
        name: 'roleUser',
        meta: {
          title: 'roleUser',
          noCache: true
        },
        hidden: true
      },
      {
        path: 'permission',
        component: _import('system/permission/index'),
        name: 'permissionManager',
        meta: {
          title: 'permissionManager',
          noCache: true
        }
      } // 权限管理
    ]
  }, // 安全中心
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
  {
    path: '/report',
    name: 'report',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'studentCourseReport',
        component: _import('report/studentCourseReport'),
        name: 'studentReport',
        meta: {
          title: '学生学习报告',
          noCache: true
        }
      }
    ]
  }
]
