<template>
  <div class="login-container">
    <div class="center-container">
      <el-row>
        <el-col :span="14" class="login-left">
          <div class="logo">
            <img :src="require('@/assets/images/logo.png')">
          </div>
          <p class="title">智训云枢</p>
          <p class="abstract">精选优质案例 作业个性辅导</p>
          <div class="img">
            <img :src="require('@/assets/images/Login_img_Illustration.png')">
          </div>
        </el-col>
        <el-col :span="10">
            <el-form class="login-form" autoComplete="on" :model="loginForm" :rules="loginRules" ref="loginForm" label-position="left">
              <div class="title-container">
                <h3 class="title">{{$t('login.title')}}</h3>
              </div>
              <div class="usernamelogin" v-show="usernameLogin">
                <el-form-item class="username" prop="username">
                  <el-input name="username" type="text" v-model="loginForm.username" autoComplete="on" placeholder="用户名" />
                </el-form-item>
                <el-form-item class="password" prop="password">
                  <el-input name="password" :type="passwordType" @keyup.enter.native="handleLogin" v-model="loginForm.password" autoComplete="on" placeholder="密码" />
                </el-form-item>
                <el-form-item class="bottom-text">
                  <el-row>
                    <el-col :span="12" class="changetext">
                      <span>记住密码</span>
                    </el-col>
                    <el-col :span="12" class="forgotpassword">
                      <span>忘记密码？</span>
                    </el-col>
                  </el-row>
                </el-form-item>
                <el-form-item class="login-btn" prop="password">
                  <el-button type="primary" style="width:100%;margin-bottom:30px;" :loading="loading" @click.native.prevent="handleLogin">{{$t('login.logIn')}}</el-button>
                </el-form-item>
              </div>

              <!-- <div class="qrcode" v-show="!usernameLogin">
                <qrcode-login ref="qrcodelogin"></qrcode-login>
              </div>
              <div class="other-login">
                <div class="title">
                  <p class="title-text"><span>其他登录方式</span></p>
                </div>
              </div>
              <div class="wechat-login">
                <div @click="showDialog=true">
                  <img :src="require('@/assets/images/Login_icon_wechat_nor.png')">
                  <p class="text">微信</p>
                </div>
              </div>
              <img class="qr-code" @click="changeLoginType" v-show="usernameLogin" :src="require('@/assets/images/Login_img_QRcode_nor.png')">
              <img class="qr-code" @click="changeLoginType" v-show="!usernameLogin" :src="require('@/assets/images/Login_img_PC_nor.png')"> -->
            </el-form>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import LangSelect from '@/components/LangSelect'
import QrcodeLogin from './qrCodeLogin'
// import { getSchoolLogo } from '@/api/login'
import { getQueryParams } from '@/utils/urlUtil'
export default {
  components: { LangSelect, QrcodeLogin },
  name: 'login',
  data() {
    const validateUsername = (rule, value, callback) => {
      callback()
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 5) {
        callback(new Error('密码不能小于6位'))
      } else {
        callback()
      }
    }
    return {
      params: {},
      usernameLogin: true,
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '用户名不能为空' }, { validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', message: '密码不能为空' }, { validator: validatePassword }]
      },
      passwordType: 'password',
      loading: false,
      showDialog: false
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('LoginByUsername', this.loginForm).then(() => {
            this.loading = false
            this.$router.push({ path: '/' })
          }).catch(() => {
            this.loading = false
          })
        } else {
          return false
        }
      })
    },
    afterQRScan() {
      // const hash = window.location.hash.slice(1)
      // const hashObj = getQueryObject(hash)
      // const originUrl = window.location.origin
      // history.replaceState({}, '', originUrl)
      // const codeMap = {
      //   wechat: 'code',
      //   tencent: 'code'
      // }
      // const codeName = hashObj[codeMap[this.auth_type]]
      // if (!codeName) {
      //   alert('第三方登录失败')
      // } else {
      //   this.$store.dispatch('LoginByThirdparty', codeName).then(() => {
      //     this.$router.push({ path: '/' })
      //   })
      // }
    },
    changeLoginType() {
      this.usernameLogin = !this.usernameLogin
      if (!this.usernameLogin) {
        this.$refs.qrcodelogin.initQrCode()
      }
    }
  },
  created() {
    // window.addEventListener('hashchange', this.afterQRScan)
    this.$nextTick(e => {
      this.params = getQueryParams()
      if (!this.params.appid) {
        const separator = window.location.search ? '&' : '?'
        const newUrl = window.location.pathname + separator + 'appid=123' + window.location.hash
        window.location.replace(newUrl)
      }
    })
  },
  destroyed() {
    // window.removeEventListener('hashchange', this.afterQRScan)
  }
}
</script>
<style lang="scss">
.login-form {
      .el-form-item
      {
       .el-input--small .el-input__inner{background: url(~@/assets/images/Login_icon_account_nor.png) no-repeat 10px 15px;
          background-size: 20px;
          color: #666666;
          width: 100%;
          height: 50px;
          font-size: 15px;
          border: none;
          padding: 0 5% 0 13%;
          background-color: #f6f6f6;
          border-radius: 5px;
          }
      }
      .el-form-item.password{
         .el-input--small .el-input__inner{
         background-image: url(~@/assets/images/Login_icon_psssword_nor.png);
         }
      }
      .login-btn{
        .el-button{
          font-size: 20px;background-color: #f6921e;border-color:#f6921e;
          padding: 11px 15px;
        }
      }
  }
</style>
<style rel="stylesheet/scss" lang="scss" scoped>
.login-container {
  position: fixed;
  height: 100%;
  width: 100%;
  overflow: auto;
  background: url('~@/assets/images/login_bg.png') no-repeat;
  background-size:100% 100%;
  .center-container{
    width: 1280px;
    margin: 0 auto;
    .login-left{
      p{margin: 0px;}
      color: #ffffff;
     .logo{margin-top: 17px;
     }
     .title{font-size: 50px;margin-top: 130px;}
     .abstract{font-size: 30px;margin-top: 31px;}
     .img img{width:900px;}
    }
  }
  .login-form {
    position: relative;
    width: 400px;
    margin: 228px 0 auto auto;
    background-color: #ffffff;
    border-radius: 10px;
    .title-container
    {
      text-align: center;
      .title{color: #f6921e;font-size: 30px;padding: 33px 0;margin: 0px;}
    }
    .el-form-item
    {
      padding: 0 20px;
    } 
    .bottom-text {
      color:#999999;
      .changetext { text-align:left; font-size:18px;cursor: pointer; }
      .forgotpassword { text-align:right; font-size:18px;cursor: pointer;}
    }
    .other-login{
      .title-text span {
        display: block; 
        position: relative; 
        color: #999999; 
        text-align: center; 
        font-size:16px;
      } 
      .title-text span:before, .title-text span:after { 
          content: ''; 
          position: absolute;  
          top: 50%; 
          background: #dbdada;   
          width: 36%;          
          height: 1px; 
      } 
      .title-text span:before { 
          left: 0%;           
      } 
      .title-text span:after { 
          right: 0%; 
      }
    }
    .wechat-login {
      div {
        cursor: pointer;
        text-align: center; 
        padding: 10px 0 23px 0;
        .text{color:#f6921e;font-size:18px;margin:0px;}
      }
    }
    .qr-code{right:2px;bottom:2px;position: absolute;cursor: pointer;}
  }
}
</style>
