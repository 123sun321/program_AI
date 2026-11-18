export function routerWarpper(router, parent) {
  router.forEach(function(value, key, arr) {
    value.parent = parent
    var children = value.children
    if (children != null) {
      children.forEach(function(childValue, childKey, childArr) {
        childValue.parent = value
        if (childValue.children != null) {
          routerWarpper(childValue.children, childValue)
        }
      })
    }
  })
}
