
import store from '../store'
const permission = {
  inserted(el, binding) {
    const permission = binding.value
    if (permission) {
      if (store.getters.permissions.indexOf(permission) === -1) {
        el.style.display = 'none'
      }
    }
  }
}
export default permission
