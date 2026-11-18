<template>
<div>
   <el-tabs v-model="activeName">
     <el-tab-pane label="基础信息" name="first">

      <el-form :model="userInfo" label-position="left" label-width="80px" style='width: 90%; margin-left:50px;'>
        
        <el-row :gutter="10">
        <el-col :span="12"> 
        <el-form-item label="用户名" class="requiredLabel">
          <el-input v-model="userInfo.userName" style="width:200px;" name="userName" data-vv-as="用户名" v-validate="'required'"></el-input>
        </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="昵称">
          <el-input v-model="userInfo.name" style="width:200px;"></el-input>
        </el-form-item>
      </el-col>  
        </el-row>  
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="真实姓名">
              <el-input v-model="userInfo.surname" style="width:200px;"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" class="requiredLabel">
              <el-radio-group name="sex" v-model="userInfo.sex" data-vv-as="性别" v-validate="'required'">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="0">女</el-radio>
              </el-radio-group>
              <span class="errortip" v-if="errors.has('sex')">{{errors.first("sex")}}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="手机号" class="requiredLabel">
              <el-input v-model="userInfo.phoneNumber" style="width:200px;"  data-vv-as="手机号" v-validate="'required|mobile'"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" class="requiredLabel">
            <el-input type="email" v-model="userInfo.emailAddress" style="width:200px;" data-vv-as="邮箱" v-validate="'required|email'"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10" v-if="!id">
          <el-col :span="12">
            <el-form-item label="密码" class="requiredLabel">
              <el-input type="password" name="password" ref="password" v-model="userInfo.password" data-vv-as="密码" style="width:200px;" v-validate="'required|password'"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认密码" class="requiredLabel">
            <el-input type="password" name="confimPassword" v-model="userInfo.confimPassword" data-vv-as="确认密码" style="width:200px;" v-validate="'required|confirmed:password'"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态">
              <el-checkbox v-model="userInfo.active">激活</el-checkbox>
              <el-checkbox v-model="userInfo.lockoutEnabled">登录锁定</el-checkbox>
        </el-form-item>
        <div class="errortipWapper" v-if="errors.any()">
        验证提示： {{errors.all()}}
        </div>
        <div style="text-align:right">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="saveUser">确 定</el-button>
        </div>
      </el-form>
     </el-tab-pane>
    <el-tab-pane label="角色信息" name="second">
      <el-table :data="roles">
        <el-table-column prop="displayName" label="角色名称"></el-table-column>
        <el-table-column prop="opt" label="操作">
          <template slot-scope="scope">
            <el-button @click="removeRole(scope.row.id)" type="text" size="small">删除</el-button>            
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
   </el-tabs>

</div>
</template>
<script>
import { getUserInfo, saveUserInfo, getCurrentUserRoles, removeRole } from '@/api/permission/user'
import { Message } from 'element-ui'

export default {
  name: 'userEdit',
  props: ['id'],
  data() {
    return {
      userInfo: { },
      activeName: 'first',
      roles: []
    }
  },
  watch: {
    id() {
      this.bindUserInfo()
    }
  },
  created() {
    this.bindUserInfo()
  },
  methods: {
    bindUserInfo() {
      this.$validator.errors.clear()
      // 判断id是否为空，非空则从后台查询数据
      this.userInfo = { userTypes: ['FrontUser'] }
      if (this.id) {
        getUserInfo(this.id).then(Response => {
          this.userInfo = Response.data
          console.log(this.userInfo)
        })
        this.getRoles(this.id)
      }
    },
    saveUser() {
      this.$validator.validate().then(result => {
        if (result) {
          // 验证成功会执行
          var tip = this.id ? '修改成功' : '创建成功'
          saveUserInfo(this.userInfo).then(() => {
            Message({
              message: tip,
              type: 'success'
            })
            this.ok()
          })
        }
      })
    },
    ok() {
      this.$emit('ok')
    },
    cancel() {
      this.$validator.errors.clear()
      this.$validator.fields.items.forEach((item) => { item.reset() })
      this.$emit('cancel')
    },
    getRoles(userid) {
      getCurrentUserRoles(userid).then(e => {
        this.roles = e.data.data
      })
    },
    removeRole(id) {
      this.$confirm('确定移除该角色吗', '确认信息', {
        distinguishCancelAndClose: true,
        confirmButtonText: '确定',
        cancelButtonText: '删除'
      }).then(e => {
        removeRole(id, this.id).then(e => {
          this.getRoles(this.id)
        })
      })
    }
  }

}
</script>