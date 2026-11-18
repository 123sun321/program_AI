<template>
    <div class="userInfo">
          <el-row :gutter="20">
            <el-col :span="2">
             <div class="f_label" style="margin-top:50%;">头像:</div></el-col>
            <el-col :span="5">
                 <img class="userPicMax" :src="avatar"/>
                 <div class="medium" @click="imagecropperShow=true">修改头像</div>
            </el-col>
        </el-row>
            <image-cropper
        v-show="imagecropperShow"
        :width="300"
        :height="300"
        :key="imagecropperKey"
        url="^/account/changehead"
        lang-type="zh"
        :noRotate="false"
        @close="close"
        @crop-upload-success="cropSuccess"/>
        <el-row :gutter="20">
            <el-col :span="2"><div class="f_label">姓名</div></el-col>
            <el-col :span="10"><div class="f_value">{{userInfo.name}}&nbsp;</div></el-col>
        </el-row>
        <el-row :gutter="20">
             <el-col :span="2"><div class="f_label">专业:</div></el-col>
            <el-col :span="10"><div class="f_value">{{userInfo.professionName}}&nbsp;</div></el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="2"><div class="f_label">班级:</div></el-col>
            <el-col :span="10"><div class="f_value">{{userInfo.className}}&nbsp;</div></el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="2">
                <div class="f_label">手机:</div>
                </el-col>
                <el-col :span="10">
                <div style="margin-top:-10px;">
                    <el-input
                    label-width="100px"
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
            <el-col :span="2">
                <div class="f_label">电子邮箱:</div>
               </el-col>
                <el-col :span="10">
                <div style="margin-top:-10px;">
                    <el-input
                    label-width="100px"
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
        <el-row :gutter="20">
            <el-col :span="24" class="c_btn">
                <el-button  type="success" plain size="medium" style="width:100px;margin-left:12%;" @click="save">保存</el-button>
            </el-col>
        </el-row>
        <el-dialog
        title="绑定手机"
        :visible.sync="bindPhoneDialogVisible"
        width="50%">
        <bind-phone v-on:BindSuccess="bindSuccess('phone')"></bind-phone>
        </el-dialog>
        <el-dialog
        title="绑定电子邮箱"
        :visible.sync="bindEmailDialogVisible"
        width="50%">
        <bing-email v-on:BindSuccess="bindSuccess('email')"></bing-email>
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
import { updateUserPersonInfo } from '@/api/person/userInfo'
import bindPhone from './components/bindPhone'
import bingEmail from './components/bindEmail'
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'
import { mapGetters } from 'vuex'
export default {
  components: {
    ImageCropper,
    PanThumb,
    bindPhone, bingEmail
  },
  data() {
    return {
      dialogVisible: false,
      imagecropperShow: false,
      imagecropperKey: 0,
      bindPhoneDialogVisible: false,
      bindEmailDialogVisible: false,
      userInfo: {},
      oldInfo: {},
      qrCodeUrl: undefined
    }
  },
  computed: {
    ...mapGetters([
      'avatar'
    ])
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
    cropSuccess(resData) {
      this.imagecropperShow = false
      this.imagecropperKey = this.imagecropperKey + 1
      this.image = resData.newFileName
      // 更新vuex
      this.$store.commit('SET_AVATAR', resData.newFileName)
    },
    close() {
      this.imagecropperShow = false
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
<style scoped lang="scss">
 .c_btn{
            .el-button{
                color: #1fad4e;
                border-color:#ffffff;
                border-radius: 10px;
                background:#e8f7ed;
            }
            .el-button:hover{border-color:#1fad4e; }
        }
.userInfo  /deep/ .el-input__inner:focus{
    border: 1px solid #1fad4e !important;
}
.userInfo{
  margin: 10px;
  background-color: #fff;
  padding: 20px 10%;
  min-height: 470px;
}
.medium{
    margin-top: -34px;
    height: 30px;
    width: 120px;
    padding: 8px;
    text-align: center;
    color: #fff;
    background-color: #000;
    opacity: 0.8;
    border-radius: 5px;
    cursor: pointer;
}
.userPicMax{
  width: 120px;height: 120px;border-radius: 5px;
  }
   .userPicNomal{
     width:100px;
  height:100px;
  border-radius: 50%;
  border: solid 1px #c3c4c7;
  }
     .userPicMin{
     width:50px;
  height:50px;
  border-radius: 50%;
  border: solid 1px #c3c4c7;
  }
 .userInfo .el-row{margin:20px 0px;}
 .userPic{
     width:100px;
  height:100px;
  border-radius: 50%;
  border: solid 1px #c3c4c7;
  }
  .f_label{color: #666666;
    font-size: 15px;
    font-weight: 700;}
  .f_value{color: #414244;
    font-size: 15px;}
  .bindTip{font-size: 14px;}
  .bindTip a{color: #0f70b3;
    text-decoration: underline; }
  .bindTip i{color: #13c313}
  .tip{
      font-size: 14px;
    color: red;
    line-height: 14px;
    margin-top: 5px;
  }
</style>
