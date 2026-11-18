<template>
    <div>
       <div>
           <el-alert
                title="绑定邮箱后，您可以用邮箱登录，以及通过邮箱接收验证码来进行安全验证。"
                type="info"
                :closable="false"
                show-icon>
            </el-alert>
            <div style=" width:400px; margin: 0px auto; margin-top: 30px;">
                <el-form :model="formInfo" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                    <div >
                    <el-form-item label="邮箱" :class="!errors.has('phoneNumber')?'vee-required':'vee-required vee-item-nobottom'">
                        <el-input size="large" v-model="formInfo.phoneNumber" name="phoneNumber" data-vv-as="邮箱" v-validate="'required|email'" data-vv-scope="sendCode"></el-input>
                        <div v-show="errors.has('phoneNumber')" class="vee-tip">{{ errors.first('phoneNumber') }}</div>
                        <div v-show="errors.has('phoneNumber','sendCode')" class="vee-tip">{{ errors.first('phoneNumber','sendCode') }}</div>
                    </el-form-item>
                    <el-form-item label="图片验证码" :class="!errors.has('kaptchaCode')?'vee-required':'vee-required vee-item-nobottom'">
                        <el-row>
                            <el-col :span="15"><el-input size="large" v-model="formInfo.kaptchaCode" name="kaptchaCode" data-vv-as="图片验证码" v-validate="'required'" data-vv-scope="sendCode"></el-input></el-col>
                            <el-col :span="4"><div><img style="width:110px;" :src="kaptcha" @click="reloadKaptcha()"/></div></el-col>

                        </el-row>
                         <div v-show="errors.has('kaptchaCode')" class="vee-tip">{{ errors.first('kaptchaCode') }}</div>
                          <div v-show="errors.has('kaptchaCode','sendCode')" class="vee-tip">{{ errors.first('kaptchaCode','sendCode') }}</div>
                    </el-form-item>
                    </div>
                    <el-form-item label="邮箱验证码" :class="!errors.has('code')?'vee-required':'vee-required vee-item-nobottom'">
                        <el-row>
                            <el-col :span="15"><el-input size="large" v-model="formInfo.code" name="code" data-vv-as="短信验证码" v-validate="'required'"></el-input></el-col>
                            <el-col :span="4">
                              <div class="c_btn"><el-button type="success" :disabled="sendCaptcha.btnDisable"  style="height:40px" @click="sendCode()">获取邮箱验证码<span v-show="sendCaptcha.btnDisable">({{sendCaptcha.num}})</span></el-button></div>
                            </el-col>

                        </el-row>
                         <div v-show="errors.has('code')" class="vee-tip">{{ errors.first('code') }}</div>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="success" @click="save()" style="width:200px; height:40px;">立即绑定</el-button>
                    </el-form-item>
                    </el-form>
            </div>
       </div>
    </div>
</template>
<script>
/**
 * @file 用户绑定电子邮箱组件
 * @author 张向宇
 * @copyright zhengzhouchuanghzi
 * @since 2025/01/10
 */
import { sendBindEmailCaptcha, bindEmailSave } from '@/api/person/userInfo'
import { setTimeout } from 'timers'

export default {
  data() {
    return {
      kaptcha: '',
      formInfo: {
        phoneNumber: '',
        kaptchaCode: '',
        code: ''
      },
      sendCaptcha: {
        btnDisable: false,
        num: 100,
        phoneCaptcha: '',
        password: ''
      }
    }
  },
  created() {
    this.$nextTick(e => {
      this.reloadKaptcha()
    })
  },
  methods: {
    reloadKaptcha() {
      this.kaptcha = process.env.BASE_API + '/kaptcha?' + Math.floor(Math.random() * 100)
    },
    sendCode() {
      this.$validator.validateAll('sendCode').then((result) => {
        if (result) {
          sendBindEmailCaptcha(this.formInfo.phoneNumber, this.formInfo.kaptchaCode).then(e => {
            // 倒计时开始
            setTimeout(() => {
              this.timerStart(this.sendCaptcha)
            }, 100)

            this.$alert('验证码已发送到您的邮箱，请注意查收', '提示', {
              confirmButtonText: '确定'
            })
          })
            .catch(err => {
              this.$alert(err.response.data.data, '提示', {
                confirmButtonText: '确定'
              })
            })
        } else {
          this.$forceUpdate()
        }
      })
    },
    timerStart(timeObj) {
      timeObj.btnDisable = true
      timeObj.num = 100
      this.sumTime = function(timeObj) {
        timeObj.num--
        if (timeObj.num === 0) {
          timeObj.btnDisable = false
          timeObj.num = 100
          clearTimeout(this.RestTimeOut)
        } else {
          clearTimeout(this.RestTimeOut)
          this.RestTimeOut = setTimeout(() => {
            this.sumTime(timeObj)
          }, 1000)
        }
      }
      this.RestTimeOut = setTimeout(() => {
        this.sumTime(timeObj)
      }, 1000)
    },
    save() {
      this.$validator.validateAll().then((result) => {
        if (result) {
          bindEmailSave(this.formInfo.phoneNumber, this.formInfo.code).then(e => {
            this.$alert('绑定邮箱成功成功', '提示', {
              confirmButtonText: '确定',
              callback: action => {
                this.$emit('BindSuccess')
              }
            })
          })
            .catch(err => {
              this.$alert(err.response.data.data, '提示', {
                confirmButtonText: '确定'
              })
            })
        }
      })
    }
  }

}
</script>
<style scoped lang="scss">
 .c_btn{
            .el-button{
                color: #1fad4e;
                border-color:#e8f7ed;
                background:#e8f7ed;
            }
            .el-button:hover{border-color:#1fad4e; }
        }
</style>
