<template>
    <el-dialog title="修改显示名称和描述" :visible.sync="visible" width="400px" v-loading="loading">
        <el-form :model="entity" ref="form" label-position="right" label-width="100px;">
            <el-form-item label="权限名称：">
                <div>
                    {{entity.name}}
                </div>                
            </el-form-item>
            <el-form-item prop="displayName" :rules="[{required:true,message:'显示名称不能为空'}]" label="显示名称：">
                <el-input v-model="entity.displayName"></el-input>
            </el-form-item>
            <el-form-item prop="description" label="描述名称：">
                <el-input type="textarea" v-model="entity.description"></el-input>
            </el-form-item>
        </el-form>
        <template slot="footer">
            <el-button @click="visible=false">取消</el-button>
            <el-button @click="save" type="primary" :loading="loading">保存</el-button>
        </template>
    </el-dialog>
</template>

<script>
import { fetch, update } from '@/api/permission'
export default {
  data() {
    return {
      visible: false,
      entity: {},
      loading: false
    }
  },

  methods: {
    open(id) {
      this.visible = true
      this.loading = true
      fetch(id).then(e => {
        this.loading = false
        this.entity = e.data.data
      })
    },
    save() {
      const data = {
        displayName: this.entity.displayName,
        description: this.entity.description,
        id: this.entity.id,
        name: this.entity.name
      }
      update(data).then(e => {
        if (e.data.success) {
          this.entity = {}
          this.visible = false
          this.$emit('success')
          this.$sucmsg('修改成功')
        } else {
          this.$emit('修改失败')
        }
      })
    }
  }
}
</script>

<style>

</style>
