<template>
  <el-dialog title="修改教师信息" :visible.sync="visible" width="30%">
    <teacher ref="form" :eid="eid" :is-edit="true"></teacher>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
/*
修改教师信息弹窗组件
@author 袁延延
@date 2025/11/27 15:00
@version 1.0
*/
import Teacher from './components/Teacher'
import { edit } from '@/api/base/teacher'
export default {
  data() {
    return {
      visible: false, // 是否显示修改教师信息弹窗
      eid: 0
    }
  },
  components: {
    Teacher
  },
  methods: {
    // 保存修改教师信息
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          edit(value).then(e => {
            if (e.data.data) {
              this.$sucmsg('修改教师信息成功')
              this.$emit('success')
              this.close()
            } else {
              this.$errmsg('教师信息已存在')
            }
          })
        }
      })
    },
    // 打开修改教师信息弹窗
    open(id) {
      this.eid = id
      this.visible = true
    },
    // 关闭修改教师信息弹窗
    close() {
      this.eid = 0
      this.$refs.form.reset()
      this.visible = false
    }
  }
}
</script>

<style>
</style>
