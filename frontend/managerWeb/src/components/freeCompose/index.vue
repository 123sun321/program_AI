
<script>
import '@/global/register.js'
export default {
  functional: true,
  props: ['mname', 'mprop'],
  render(createElement, context) {
    const mname = context.props.mname
    const mprop = context.props.mprop
    console.log()
    return createElement(mname, {
      props: mprop
    })
  }
}
</script>


