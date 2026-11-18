import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'// progress bar style

NProgress.configure({ showSpinner: false })// NProgress Configuration

// permissiom judge function
function hasPermission(permissions, routePermissions) {
  if (permissions.indexOf('admin') >= 0) return true // admin permission passed directly
  if (!routePermissions) return true
  return permissions.some(role => routePermissions.indexOf(role) >= 0)
}
const whiteList = ['/login', '/authredirect', '/401']// no redirect whitelist

router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar
  if (needFetch(to)) {
    fetchUserInfoAndPermission().then(() => {
      next({ ...to, replace: true })
    })
  } else {
    permissionIntercept(to).then(e => {
      if (e) {
        next()
      } else {
        next({ path: '/401', replace: true, query: { noGoBack: true }})
      }
    }).catch(e => {
      next('/login')
    })
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})

/**
 * 需要提取？
 * @param {*} to
 */
function needFetch(to) {
  if (to.path === '/login') {
    return false
  } else if (!store.getters.extractedUserInfo || !store.getters.extractedPermission) {
    return true
  }
  return false
}
/**
 * 提取信息和权限
 */
async function fetchUserInfoAndPermission() {
  await fetchUserInfo()
  await fectchPermission()
}

/**
 * @method
 * 提取用户信息
 */
async function fetchUserInfo() {
  if (!store.getters.extractedUserInfo) {
    await store.dispatch('GetUserInfo')
  }
}

// 提权权限
async function fectchPermission() {
  if (!store.getters.extractedPermission) {
    const permissions = await store.dispatch('GetUserPermissions')
    await store.dispatch('GenerateRoutes', { permissions })
    console.log(store.getters.addRouters)
    router.addRoutes(store.getters.addRouters)
  }
}

/**
 * 权限拦截
 */
async function permissionIntercept(to) {
  if (whiteList.indexOf(to.path) > -1) {
    return true
  }
  if (hasPermission(store.getters.permissions, to.meta.permissions)) {
    return true
  } else {
    return false
  }
}
