import { Validator } from 'vee-validate'
function veeExtend() {
  Validator.extend('mobile', {
    getMessage(field, args) {
      // will be added to default locale messages.
      // Returns a message.
      return field + '输入不合法'
    },
    validate: value => {
      return value.length === 11 && /^((13|14|15|17|18)[0-9]{1}\d{8})$/.test(value)
    }
  })
  Validator.extend('password', {
    getMessage(field, args) {
      // will be added to default locale messages.
      // Returns a message.
      return field + '必须为8-20位的字母与数字的组合'
    },
    validate: value => {
      return /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$/.test(value)
    }
  })
}
export default veeExtend
