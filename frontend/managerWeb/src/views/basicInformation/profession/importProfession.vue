<template>
  <el-dialog title="添加专业信息" :visible.sync="visible" width="30%">
    <profession ref="form" :is-edit="false" :init-entity="entity"></profession>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
/*
批量导入添加专业组件
@author 袁延延
@date 2025/11/28 10:15
@version 1.0
*/
import Profession from './components/Profession'
export default {
  components: {
    Profession
  },
  data() {
    return {
      entity: {}, // 专业信息
      visible: false // 是否显示添加专业弹窗
    }
  },
  methods: {
    // 显示专业弹窗
    open(entity) {
      this.entity = entity
      this.visible = true
    },
    // 保存专业弹窗
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
    // 关闭专业弹窗
    close() {
      this.visible = false
    }
  }
}

</script>

<style>

</style>
