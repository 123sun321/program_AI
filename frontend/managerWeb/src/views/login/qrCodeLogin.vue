<template>
  <div class="wechat-login">
    <span class="text">微信扫码关注公众号登录</span>
    <el-form-item class="qrcode">
      <img :src="url"/>
      <div v-if="overdue" class="masking"></div>
      <div v-if="overdue" class="text">二维码已失效</div>
    </el-form-item>
    <el-form-item class="login-btn">
        <el-button type="primary" @click="initQrCode" style="margin-bottom:10px;">刷新二维码</el-button>
    </el-form-item>
  </div>
</template>

<script>

import { qrCodeCreateTmpTicket } from '@/api/wechat'
import { jugeLogin } from '@/api/login'
export default {
  name: 'wechat-login',
  data() {
    return {
      url: undefined, // 二维码地址
      timer: null, // 定时器
      overdue: false // 是否过期
    }
  },
  created() { },
  methods: {
    initQrCode() {
      var vm = this
      if (vm.timer !== null) { clearInterval(vm.timer) }
      qrCodeCreateTmpTicket('qrcodelogin').then(response => {
        if (response.data) {
          vm.url = 'https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=' + response.data
          var count = 0
          vm.overdue = false
          vm.timer = setInterval(() => {
            jugeLogin().then(e => {
              if (e.data) {
                clearInterval(vm.timer)
                this.$router.push({ path: '/' })
              } else {
                if (count++ >= 120) {
                  clearInterval(vm.timer)
                  vm.overdue = true
                }
              }
            })
          }, 1000)
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.wechat-login{
    text-align: center;
    .text{color: #999999;}
    .qrcode{
      position: relative;
        img{height: 177px;}
        .text,.masking{position:absolute;width: 160px;height: 160px; top: 9px;left: 120px;}
        .masking{background-color: #ffffff;opacity: 0.9;}
        .text{color: #333333;line-height: 160px;font-size: 20px;font-weight: bolder;}
    }
}
.el-form-item--small.el-form-item{margin-bottom: 0px;}
</style>
