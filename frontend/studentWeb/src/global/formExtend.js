export const formExtend = {

  /**
   *
   * @param {import Vue from 'vue'} Vue
   */
  install(Vue) {
    Vue.directive('fop', { // 操作符
      bind(el, binding, vnode, oldVnode) {
        el.setAttribute('fop', binding.expression)
      }
    })
    Vue.directive('fname', { // 名称
      bind(el, binding, vnode, oldVnode) {
        el.setAttribute('fname', binding.expression)
      }
    })
    Vue.directive('fcon', { // 连接符
      bind(el, binding, vnode, oldVnode) {
        el.setAttribute('fcon', binding.expression)
      }
    })

    Vue.prototype.$formToParams = formToParams
    Vue.prototype.$formToParamsAsync = formToParamsAsync
  }
}

/**
 *从form中取出数据
 *@param {object} VueComponent
 */
export async function formToParamsAsync(form, key) {
  const refs = await new Promise((resolve, reject) => {
    let interval
    let time = 0
    if (form[key]) {
      resolve(form[key])
    } else {
      interval = setInterval(e => {
        time++
        if (form[key]) {
          clearInterval(interval)
          resolve(form[key])
        } else if (time > 20) {
          clearInterval(interval)
          reject(new Error('value of $refs is not find!'))
        }
      }, 100)
    }
  })
  return formToParams(refs)
}
export function formToParams(form) {
  const model = form.model
  const params = {
    search: model.search,
    page: model.page || {
      pageCount: 10,
      current: 1
    },
    form: {},
    op: {},
    con: {},
    orderBy: undefined
  }
  form.$children.forEach(e => {
    const prop = e.prop
    if (prop === null || prop === '' || prop === 'search') return // 若未曾标记prop属性则直接退出此次条件拼接

    const val = model[prop]
    if (val === undefined || val === '') return

    const fname = e.$el.getAttribute('fname') || prop // 名称
    let fop = params.op[fname] || e.$el.getAttribute('fop') || '=' // 操作符
    fop = fop.replace(/\'/g, '')

    if (fop === 'in' || fop === 'bet') {
      if (typeof params.form[fname] === 'undefined') {
        params.form[fname] = []
      }
      if (Array.isArray(val)) {
        params.form[fname].push(...val)
      } else {
        params.form[fname].push(val)
      }
    } else {
      params.form[fname] = val
    }

    if (fop !== '=') {
      params.op[fname] = fop
    }
    let fcon = e.$el.getAttribute('fcon') || 'and'
    fcon = fcon.replace(/\'/g, '')
    if (fcon !== 'and') {
      params.con[fname] = fcon
    }
  })
  if (typeof model.orderBy !== 'undefined') {
    params.orderBy = model.orderBy
  }
  return params
}
