<template>
  <el-dialog title="添加教师信息" :visible.sync="visible" width="30%">
    <teacher ref="form" :is-edit="false"></teacher>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
/*
添加教师信息弹窗组件
@author 袁延延
@date 2025/11/27 15:00
@version 1.0
*/
import Teacher from './components/Teacher'
import {
  add
} from '@/api/base/teacher'
export default {
  data() {
    return {
      visible: false // 是否显示添加教师信息弹窗
    }
  },
  components: {
    Teacher
  },
  methods: {
    // 保存添加教师信息
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          console.log(value)
          add(value).then(e => {
            if (e.data.data) {
              this.$sucmsg('添加教师信息成功')
              this.$emit('success')
              this.close()
              this.$refs.form.reset()
            } else {
              this.$errmsg('教师信息已存在')
            }
          })
        }
      })
    },
    // 打开添加教师信息弹窗
    open() {
      this.visible = true
    },
    // 关闭添加教师信息弹窗
    close() {
      this.visible = false
    }
  }
}

</script>

<style>

</style>
