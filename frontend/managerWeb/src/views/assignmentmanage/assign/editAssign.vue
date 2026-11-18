<template>
  <el-dialog title="修改作业" :visible.sync="visible" width="55%" :before-close="closeDialog">
    <assign ref="form" :List="List" :courseId="courseId" :eid="eid" :is-edit="true"></assign>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
/*
修改作业弹窗组件
@author 袁延延
@date 2025/3/22 15:00
@version 1.0
*/
import VueMarkdown from 'vue-markdown' // 直接组件引入
import assign from './components/assign'
import { edit } from '@/api/assignmentmanage/assgin'
export default {
  data() {
    return {
      visible: false, // 是否显示修改作业弹窗
      eid: 0,
      List: undefined,
      courseId: 0
    }
  },
  components: {
    assign,
    VueMarkdown
  },
  methods: {
    // 保存修改作业
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          edit(value).then(e => {
            if (e.data) {
              this.$sucmsg('修改作业成功')
              this.$emit('success')
              this.close()
            } else {
              this.$errmsg('作业已存在')
            }
          })
        }
      })
    },
    // 打开修改作业弹窗
    open(id, courseId, List) {
      this.eid = id
      this.courseId = courseId
      this.List = List
      this.visible = true
    },
    // 关闭修改作业弹窗
    close() {
      this.eid = 0
      this.$refs.form.reset()
      this.visible = false
    },
    closeDialog() {
      this.$refs.form.reset()
      this.visible = false
    }
  }
}
</script>

<style>
</style>
