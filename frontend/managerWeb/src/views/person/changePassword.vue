<template>
 <div class="maincontent">
    <el-form ref="resetPasswordForm" :model="form" label-width="80px">
  <el-form-item label="原密码" prop="oldPassword" :class="!errors.has('oldPassword')?'vee-required':'vee-required vee-item-nobottom'">
    <el-col :span="4">
      <el-input name="oldPassword" v-model="form.oldPassword" type="password" data-vv-as="原密码" v-validate="'required'"></el-input>
      <div v-show="errors.has('oldPassword')" class="vee-tip">{{ errors.first('oldPassword') }}</div>
      </el-col>    
  </el-form-item>
  <el-form-item label="新密码" prop="newPassword"  :class="!errors.has('oldPassword')?'vee-required':'vee-required vee-item-nobottom'">
    <el-col :span="4">
         <div class="el-input el-input--small">
     <vue-password name="newPassword" data-vv-as="新密码" v-validate="'required|min:8|max:20|password'" v-model="form.newPassword"
            classes="el-input__inner"
            :strengthMessages="['太弱','弱','一般','强','非常强']" ref="newPassword">
      </vue-password>
      <div v-show="errors.has('newPassword')" class="vee-tip">{{ errors.first('newPassword') }}</div>
    </div>
    </el-col>    
  </el-form-item>
  <el-form-item label="确认密码" prop="confimPassword"  :class="!errors.has('oldPassword')?'vee-required':'vee-required vee-item-nobottom'">
    <el-col :span="4">
        <div class="el-input el-input--small">
            <input name="confimPassword" class="el-input__inner" type="password" data-vv-as="确认密码" v-validate data-vv-rules="required|confirmed:newPassword" v-model="form.confimPassword"/>
            <div v-show="errors.has('confimPassword')" class="vee-tip">{{ errors.first('confimPassword') }}</div>
      </div>
    </el-col>    
  </el-form-item>
  <el-form-item>
    <el-button type="primary" @click="submitForm" :loading="isLoading">立即修改</el-button>
    <el-button @click="resetForm"> 取消</el-button>
  </el-form-item>
</el-form>
<el-dialog
  title="修改成功"
  :visible.sync="dialogVisible"
  width="30%"
  >
  <span>恭喜您，密码修改成功，需要您重新进行登录。</span>
  <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="tipColose">确 定</el-button>
  </span>
</el-dialog>
 </div>
</template>

<script>
/**
 * @file 修改当前登录用户密码
 * @author 张向宇
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/25
 */
import VuePassword from 'vue-password'
import { changePassword } from '@/api/login'

export default {
  components: {
    VuePassword
  },
  data() {
    return {
      form: {
        oldPassword: '', // 旧密码
        newPassword: '', // 新密码
        confimPassword: '' // 确认密码
      },
      isLoading: false, // 立即修改按钮loading状态
      dialogVisible: false // 是否显示提示弹窗
    }
  },
  methods: {
    // 提交表单
    submitForm() {
      this.$validator.validate().then(result => {
        if (result) {
          this.isLoading = true
          changePassword(this.form).then(response => {
            // alert('修改成功')
            this.isLoading = false
            this.dialogVisible = true
          })
            .catch(() => {
              this.isLoading = false
            })
        }
      })
    },
    // 重置表单
    resetForm() {
      this.$refs.resetPasswordForm.resetFields()
      this.$validator.reset()
    },
    // 提交成功后显示提示弹窗
    tipColose() {
      this.dialogVisible = false
      this.$store.dispatch('FedLogOut').then(() => {
        location.reload()
      })
    }
  }
}
</script>
