<template>
  <div>
          <el-tabs v-model="TabActive">
        <el-tab-pane label="角色信息" name="base">
           <el-form label-position="left" label-width="110px" style='width: 90%; margin-left:50px;'>
          <el-form-item label="角色名称" class="requiredLabel">
            <el-input v-model="roleInfo.name" style="width:200px;" name="name" data-vv-as="权限名称" v-validate="'required'"></el-input>
             </el-form-item>
          <el-form-item label="显示名称" class="requiredLabel">
            <el-input  v-model="roleInfo.displayName" style="width:200px;" name="displayName" data-vv-as="显示名称" v-validate="'required'"></el-input>
          </el-form-item>
          <el-form-item label="系统角色">
              <el-switch v-model="roleInfo.isStatis"  
              disabled   
            active-color="#13ce66"
            inactive-color="#cccccc">
            </el-switch>
          </el-form-item>
          <el-form-item label="默认角色">
              <el-switch v-model="roleInfo.isDefault"      
            active-color="#13ce66"
            inactive-color="#cccccc">
            </el-switch>
          </el-form-item> 
           <el-form-item label="禁用租户过滤">
              <el-switch v-model="roleInfo.disableTenantFilter"      
            active-color="#13ce66"
            inactive-color="#cccccc">
            </el-switch>
          </el-form-item> 
           </el-form>
        </el-tab-pane>
        <el-tab-pane label="拥有权限" name="permission">
          <permission-select v-model="permissions"></permission-select>
        </el-tab-pane>       
      </el-tabs>
       <div class="errortipWapper" v-if="errors.any()" style="margin-top:10px;">
          验证提示： {{errors.all()}}
          </div>  
           <div style="text-align:right;margin-top:10px;">
            <el-button @click="cancel">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </div>
  </div>
</template>
<script>
import { getRoleInfo, createRole, updateRole } from '@/api/permission/role'
import { Message } from 'element-ui'
import permissionSelect from '@/views/system/permission/components/permissionSelect'
export default {
  name: 'roleEdit',
  components: {
    permissionSelect
  },
  props: ['id'],
  data() {
    return {
      TabActive: 'base',
      roleInfo: { },
      permissions: []
    }
  },
  watch: {
    id() {
      this.bindRoleInfo()
    }
  },
  created() {
    this.bindRoleInfo()
  },
  methods: {
    bindRoleInfo() {
      this.$validator.errors.clear()
      this.TabActive = 'base'
      // 判断id是否为空，非空则从后台查询数据
      if (this.id) {
        getRoleInfo(this.id).then(Response => {
          this.roleInfo = Response.data.role
          // this._updatePermission(Response.data.permissions)
          this.permissions = Response.data.permissions
          // console.log(Response.data)
        })
      } else {
        this.roleInfo = {}
        this.permissions = []
      }
    },
    save() {
      this.$validator.validate().then(result => {
        if (result) {
          // 验证成功会执行
          var tip = this.id ? '修改成功' : '创建成功'
          var sendData = { role: this.roleInfo, permissions: this.permissions }
          if (this.id) {
            updateRole(sendData).then(() => {
              Message({
                message: tip,
                type: 'success'
              })
              this.ok()
            })
          } else {
            createRole(sendData).then(() => {
              Message({
                message: tip,
                type: 'success'
              })
              this.ok()
            })
          }
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
    }
  }
}
</script>

