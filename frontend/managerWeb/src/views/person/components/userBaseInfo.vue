<template>
    <div class="userInfo" style="margin:20px;">
        <el-row :gutter="20">
            <el-col :span="2"><div class="f_label">账号:</div></el-col>
            <el-col :span="5"><div class="f_value">{{userInfo.userName}}&nbsp;</div></el-col>
            <el-col :span="2"><div class="f_label">姓名:</div></el-col>
            <el-col :span="5"><div class="f_value">{{userInfo.name}}&nbsp;</div></el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="2"><div class="f_label">学校:</div></el-col>
            <el-col :span="5"><div class="f_value">{{userInfo.schoolName}}&nbsp;</div></el-col>
            <el-col :span="2"><div class="f_label">院系:</div></el-col>
            <el-col :span="5"><div class="f_value">{{userInfo.departmentName}}&nbsp;</div></el-col>
        </el-row>
        <!-- <el-row :gutter="20">
            <el-col :span="2"><div class="f_label">专业:</div></el-col>
            <el-col :span="5"><div class="f_value">{{userInfo.professionName}}&nbsp;</div></el-col>
            <el-col :span="2"><div class="f_label">班级:</div></el-col>
            <el-col :span="5"><div class="f_value">{{userInfo.className}}&nbsp;</div></el-col>
        </el-row> -->
        <el-row :gutter="20">
            <el-col :span="2"><div class="f_label">工号:</div></el-col>
            <el-col :span="5"><div class="f_value">{{userInfo.schoolIdNumber}}&nbsp;</div></el-col>
            <el-col :span="2"><div class="f_label">注册时间:</div></el-col>
            <el-col :span="5"><div class="f_value">{{userInfo.creationTime}}&nbsp;</div></el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="15">
                <div class="f_label">手机:</div>
                <div style="margin-top:10px;">
                    <el-input
                    placeholder="请输入内容"
                    v-model="userInfo.phoneNumber"
                     clearable style="width:230px;" name="phone" data-vv-as="手机" v-validate="'required|mobile'">
                    </el-input>
                    <span class="bindTip" v-if="!userInfo.phoneNumberConfirmed">该手机还未绑定，<a @click="bindPhoneDialogVisible=true">立即绑定</a></span>
                    <span class="bindTip" v-else><i class="el-icon-success"></i>手机号已验证</span>
                    <div v-show="errors.has('phone')" class="tip">{{ errors.first('phone') }}</div>
                </div>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="15">
                <div class="f_label">电子邮箱:</div>
                <div style="margin-top:10px;">
                    <el-input
                    placeholder="请输入内容"
                    v-model="userInfo.emailAddress"
                     clearable style="width:230px;" name="email" data-vv-as="电子邮箱" v-validate="'required|email'">
                    </el-input>
                    <span class="bindTip" v-if="!userInfo.emailConfirmed">该邮箱还未绑定，<a @click="bindEmailDialogVisible=true">立即绑定</a></span>
                    <span class="bindTip" v-else><i class="el-icon-success"></i>邮箱已验证</span>
                    <div v-show="errors.has('email')" class="tip">{{ errors.first('email') }}</div>
                </div>
            </el-col>
        </el-row>
        <!-- <el-row :gutter="20">
            <el-col :span="7">
                <div class="f_label">微信:</div>
                <div style="margin-top:10px;">
                    <div v-if="userInfo.weiXinAvatar">
                    <el-row>
                        <el-col :span="10" style="text-align:center">
                            <img class="userPic" :src="userInfo.weiXinAvatar"/>
                            <div>昵称：{{userInfo.weiXinNickName}}</div>
                             <el-button plain @click="unbindWeiXinClick()">解除绑定</el-button>
                        </el-col>

                    </el-row>
                    </div>
                    <div v-if="!userInfo.weiXinAvatar">
                    <el-row>
                        <el-col :span="24" style="text-align:center">
                            <span style="font-size:12px;">暂未绑定微信，绑定后可以使用微信扫码登录，接收系统通知</span>
                            <el-button plain @click="bindWechat" >立即绑定</el-button>
                        </el-col>
                    </el-row>
                    </div>
                </div>
            </el-col>
        </el-row> -->
        <el-row :gutter="20">
            <el-col :span="24">
                <el-button type="primary"  size="medium" style="width:130px;" @click="save">保存</el-button>
            </el-col>
        </el-row>
        <el-dialog
        title="绑定手机"
        :visible.sync="bindPhoneDialogVisible"
        width="50%"
        >
        <bind-phone v-on:BindSuccess="bindSuccess('phone')"></bind-phone>
        </el-dialog>
        <el-dialog
        title="绑定电子邮箱"
        :visible.sync="bindEmailDialogVisible"
        width="50%"
        >
        <bing-email v-on:BindSuccess="bindSuccess('email')"></bing-email>
        </el-dialog>
        <el-dialog
        title="绑定微信"
        :visible.sync="bindWechatDialogVisible" width="20%">
           <div style="text-align: center;">
             <span>扫码关注公众号绑定微信账号</span>
            <img :src="qrCodeUrl" style="width:100%;" />
            <el-button plain @click="get" >我已绑定</el-button>
          </div>
        </el-dialog>
    </div>
</template>
<script>
/**
 * @file 用户个人资料基本信息展示组件
 * @author 张向宇
 * @copyright zhengzhouchuanghzi
 * @since 2025/01/10
 */
import { getUserInfo } from '@/api/login'
import { updateUserPersonInfo, ubBindWeiXin } from '@/api/person/userInfo'
import bindPhone from './bindPhone'
import bingEmail from './bindEmail'
import { qrCodeCreateTmpTicket } from '@/api/wechat'

export default {
  components: {
    bindPhone, bingEmail
  },
  data() {
    return {
      bindPhoneDialogVisible: false,
      bindEmailDialogVisible: false,
      bindWechatDialogVisible: false,
      userInfo: {},
      oldInfo: {},
      qrCodeUrl: undefined
    }
  },

  created() {
    this.$nextTick(e => {
      this.get()
    })
  },
  props: {

  },
  methods: {
    get() {
      this.bindWechatDialogVisible = false
      getUserInfo().then(e => {
        this.userInfo = e.data
        this.oldInfo = JSON.parse(JSON.stringify(e.data)) // 深copy一份数据
      })
    },
    // 保存信息
    save() {
      this.$validator.validate().then(result => {
        if (result) {
          var tip = ''
          if (this.userInfo.phoneNumber !== this.oldInfo.phoneNumber && this.userInfo.phoneNumberConfirmed) {
            tip += '您修改了<span style="color:red">手机号码</span>，修改后原来的绑定信息失效，需要重新进行手机号的绑定。<br/>'
          }
          if (this.userInfo.emailAddress !== this.oldInfo.emailAddress && this.userInfo.emailConfirmed) {
            tip += '您修改了<span style="color:red">邮箱地址</span>，修改后原来的邮箱绑定信息失效，需要重新进行邮箱的绑定。<br/>'
          }
          if (tip === '') {
            tip = '确认要修改用户信息吗,'
          }
          this.$confirm(tip + '是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            dangerouslyUseHTMLString: true
          }).then(() => {
            updateUserPersonInfo(this.userInfo).then(e => {
              this.$message({
                type: 'success',
                message: '修改成功!'
              })
              this.get()
            })
          }).catch(() => {

          })
        }
      })
    },
    // 解除微信绑定
    unbindWeiXinClick() {
      this.$confirm('解绑后，将不能使用微信扫码登录，不能通过微信接收系统消息?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }).then(() => {
        ubBindWeiXin().then(e => {
          this.$message({
            type: 'success',
            message: '解绑微信成功!'
          })
          this.get()
        })
      }).catch(() => {

      })
    },
    bindWechat() {
      var vm = this
      qrCodeCreateTmpTicket(vm.userInfo.id).then(response => {
        if (response.data) {
          vm.qrCodeUrl = 'https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=' + response.data
          vm.bindWechatDialogVisible = true
        }
      })
    },
    bindSuccess(obj) {
      this.get()
      if (obj === 'phone') {
        this.bindPhoneDialogVisible = false
      } else if (obj === 'email') {
        this.bindEmailDialogVisible = false
      }
    }
  }
}
</script>
<style scoped>
 .userInfo .el-row{margin:20px 0px;}
 .userPic{
     width:100px;
  height:100px;
  border-radius: 50%;
  border: solid 1px #c3c4c7;
  }
  .f_label{color: #77787b;
    font-size: 14px;
    font-weight: 700;}
  .f_value{color: #414244;
    font-size: 14px;}
  .bindTip{font-size: 12px;}
  .bindTip a{color: #0f70b3;
    text-decoration: underline; }
  .bindTip i{color: #13c313}
  .tip{
      font-size: 12px;
    color: red;
    line-height: 14px;
    margin-top: 5px;
  }
</style>
