<template>
  <el-dialog title="添加学生信息" :visible.sync="visible" width="40%">
    <Student ref="form" :is-edit="false" :init-entity="entity"></Student>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
/**
 * @file 导入学生组件
 * @author 赵雪丽
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/06
 */
import Student from './components/Student'
export default {
  components: {
    Student
  },
  data() {
    return {
      entity: {}, // 学生信息
      visible: false // 是否显示弹窗
    }
  },
  methods: {
    // 显示学生弹窗 entity [Object] 获取学生信息
    open(entity) {
      this.entity = entity
      this.visible = true
    },
    // 保存学生信息
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
    // 关闭学生弹窗
    close() {
      this.visible = false
    }
  }
}

</script>

<style>

</style>
