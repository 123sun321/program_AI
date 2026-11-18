import {
  Message,
  MessageBox
} from 'element-ui'
export const messageExtend = {
  install(Vue) {
    Vue.prototype.$sucmsg = e => {
      showMessage('success', e)
    }
    Vue.prototype.$warnmsg = e => {
      showMessage('warning', e)
    }
    Vue.prototype.$errmsg = e => {
      showMessage('error', e)
    }
    Vue.prototype.$infomsg = e => {
      showMessage('info', e)
    }
    Vue.prototype.$cfm = (msg, done, cancel) => {
      if (cancel) {
        cancel = e => {
          console.log(e)
        }
      }
      MessageBox.confirm(msg, '提示').then(done).catch(cancel)
    }
  }

}

function showMessage(type, message) {
  Message({
    showClose: true,
    type: type,
    message: message
  })
}
