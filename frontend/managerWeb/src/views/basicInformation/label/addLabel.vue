<template>
  <el-dialog title="添加标签信息" :visible.sync="visible" width="30%">
    <Label ref="form" :is-edit="false"></Label>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { add } from '@/api/base/label'
import Label from './components/Label'
export default {
  data() {
    return {
      visible: false
    }
  },
  components: {
    Label
  },
  methods: {
    // 保存添加标签
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          add(value).then(e => {
            if (e.data) {
              this.$sucmsg('保存成功')
              this.$emit('success')
              this.$refs.form.reset()
              this.close()
            } else {
              this.$errmsg('保存失败，该标签已存在！')
            }
          })
        }
      })
    },
    // 打开添加标签弹窗
    open() {
      this.visible = true
    },
    // 关闭添加标签弹窗
    close() {
      this.visible = false
    }
  }
}
</script>

<style>

</style>
