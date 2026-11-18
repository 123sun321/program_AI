<template>
  <el-dialog title="新增作业" :visible.sync="visible" width="55%" :before-close="closeDialog">
    <assign ref="form" :List="List" :is-edit="false"></assign>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
/*
新增作业弹窗组件
@author 袁延延
@date 2025/3/22 15:00
@version 1.0
*/
import VueMarkdown from 'vue-markdown' // 直接组件引入
import assign from './components/assign'
import {
  add
} from '@/api/assignmentmanage/assgin'
export default {
  data() {
    return {
      List: undefined, // 所有数据
      visible: false // 是否显示新增作业弹窗
    }
  },
  components: {
    assign,
    VueMarkdown
  },
  methods: {
    // 保存新增作业
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          add(value).then(e => {
            if (e.data) {
              this.$refs.form.wangeditorReset()
              this.$sucmsg('新增作业成功')
              this.$emit('success')
              this.close()
            } else {
              this.$errmsg('作业信息已存在')
            }
          })
        }
      })
    },
    // 打开新增作业弹窗
    open(List) {
      this.visible = true
      this.List = List
    },
    // 关闭新增作业弹窗
    close() {
      this.visible = false
      this.$refs.form.reset()
    },
    closeDialog() {
      this.close()
    }
  }
}

</script>

<style>

</style>
