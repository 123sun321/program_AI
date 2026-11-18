<template>
  <el-dialog title="添加教师信息" :visible.sync="visible" width="30%">
    <teacher ref="form" :is-edit="false" :init-entity="entity"></teacher>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
/*
修改教师弹窗组件
@author 袁延延
@date 2025/11/28 14:00
@version 1.0
*/
import Teacher from './components/Teacher'
export default {
  components: {
    Teacher
  },
  data() {
    return {
      entity: {}, // 教师信息
      visible: false // 是否显示添加教师弹窗
    }
  },
  methods: {
    // 显示教师弹窗
    open(entity) {
      this.entity = entity
      this.visible = true
    },
    // 保存教师
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          this.entity.success = true
          this.entity.tips = ''
          this.visible = false
          this.$emit('success', this.entity)
        } else {
          this.$errmsg('验证不通过')
        }
      })
    },
    // 关闭教师弹窗
    close() {
      this.visible = false
    }
  }
}

</script>

<style>

</style>
