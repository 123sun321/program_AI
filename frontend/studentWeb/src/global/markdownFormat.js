export const markdownFormat = {
  install(Vue) {
    Vue.prototype.$markdownFormat = formatFunction
  }
}
export function formatFunction(doms) {
  const domList = []
  const count = doms.$el.querySelectorAll('h1')
  for (var j = 0; j < count.length; j++) {
    domList.push({ title: '', content: '' })
  }
  const childendom = doms.$el.children
  var h_number = -1
  for (var i = 0; i < childendom.length; i++) {
    var element = childendom[i]
    if (element.tagName === 'H1') {
      h_number = h_number + 1
      domList[h_number].title = element.innerHTML
    } else {
      domList[h_number].content += element.outerHTML
    }
  }
  return domList
}
