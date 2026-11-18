import 'babel-polyfill'
import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/iconfont/iconfont.css'
import '@/styles/index.scss' // global css
import '@/styles/markdown.css'
import '@/styles/dk-style.scss'

import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'

import App from './App'
import router from './router'
import store from './store'

import i18n from './lang' // Internationalization
import './icons' // icon
import './errorLog' // error log
import './permission' // permission control
// import './mock' // simulation data

import * as filters from './filters' // global filters
import {
  formExtend
} from './global/formExtend'
import {
  messageExtend
} from './global/messageExtend'
import {
  markdownFormat
} from './global/markdownFormat'

import vpermission from './global/vpermission'

Vue.use(Element, {
  size: 'small', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
})
Vue.use(formExtend)
Vue.use(messageExtend)
Vue.use(markdownFormat)
Vue.directive('permission', vpermission)

// markdown编辑器
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
Vue.use(mavonEditor)
import hljs from 'highlight.js'
import 'highlight.js/styles/googlecode.css'
Vue.directive('highlight', (el) => {
  const blocks = el.querySelectorAll('pre code')
  blocks.forEach((block) => {
    hljs.highlightBlock(block)
  })
})

// 使用表单验证
import VeeValidate from 'vee-validate'
import zh_CN from 'vee-validate/dist/locale/zh_CN'
import veeExtend from '@/utils/veeExtend'

// moment.js
import moment from 'moment'
Vue.prototype.$moment = moment

const config = {
  errorBagName: 'errors', // change if property conflicts.
  delay: 0,
  locale: 'zh_CN',
  i18nRootKey: 'validation',
  dictionary: {
    zh_CN
  },
  events: 'blur',
  strict: true,
  fieldsBagName: 'veefields' // 此处默认为fields
}

Vue.use(VeeValidate, config)
veeExtend()

// 复制剪贴板
import VueClipboard from 'vue-clipboard2'
Vue.use(VueClipboard)
// register global utility filters.
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

export const vm = new Vue({
  el: '#app',
  router,
  store,
  i18n,
  template: '<App/>',
  components: {
    App
  },
  methods: {
    redirect(url) {
      router.push(url)
    }
  }
})

/* eslint-disable*/
Date.prototype.format = function (format) {
  var date = {
    'M+': this.getMonth() + 1,
    'd+': this.getDate(),
    'h+': this.getHours(),
    'm+': this.getMinutes(),
    's+': this.getSeconds(),
    'q+': Math.floor((this.getMonth() + 3) / 3),
    'S+': this.getMilliseconds()
  }
  if (/(y+)/i.test(format)) {
    format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  for (var k in date) {
    if (new RegExp('(' + k + ')').test(format)) {
      format = format.replace(RegExp.$1, RegExp.$1.length == 1 ?
        date[k] : ('00' + date[k]).substr(('' + date[k]).length))
    }
  }
  return format
}
