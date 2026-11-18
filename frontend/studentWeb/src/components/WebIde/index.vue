<template>
    <div class="web-ide">
      <iframe :src="webIdeUrl"></iframe>
    </div>
</template>
<script>
import { getQueryParams } from '@/utils/urlUtil'
export function redirectPage() {

}
export default {
  props: {
    id: {// 编号
      type: String,
      default: null
    },
    codeId: {// 代码编号
      type: String,
      default: null
    },
    type: {// production：优秀作品 ，exercise：实验 ，assignment：作业 ，training：练习
      type: String,
      default: null
    },
    redirectUrl: { // 结束代码编辑需要重定向的路由
      type: String,
      default: null
    },
    startType: {
      type: Number,
      default: null
    }
  },
  computed: {
    appid: function() {
      return getQueryParams().appid
    }
  },
  data() {
    return {
      baseHost: process.env.BASE_API,
      webIdeUrl: '/fstatic/codeeditor/index.html'
    }
  },
  created() {
    this.$nextTick(e => {
      this.init()
    })
  },
  methods: {
    init() {
      this.webIdeUrl += '?appid=' + this.appid
      if (this.codeId != null) {
        this.webIdeUrl += '&cid=' + this.codeId
      }
      if (this.id != null) {
        this.webIdeUrl += '&id=' + this.id
      }
      if (this.redirectUrl != null) {
        this.webIdeUrl += '&redirectUrl=' + this.redirectUrl
      }
      this.webIdeUrl += '#/' + this.type
      // 开始试验模式
      if (this.startType != null) {
        this.webIdeUrl += '?starttype=' + this.startType
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.web-ide{
  height: 100%;
  overflow: hidden;
  iframe{
    border: none;
    height: 100%;
    width: 100%;
  }
}
</style>

