<template>
  <el-dialog title="添加学习小组" :visible.sync="visible" width="30%">
    <detail ref="form" :is-edit="false"></detail>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>

import detail from './detail'
import {
  add
} from '@/api/coursemanage/courseGroup'
export default {
  props: ['courseId'],
  data() {
    return {
      visible: false // 是否显示添加专业弹窗
    }
  },
  components: {
    detail
  },
  methods: {
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          value.courseId = this.courseId
          add(value).then(e => {
            this.$sucmsg('添加成功')
            this.$emit('success')
            this.$refs.form.reset()
            this.close()
          })
        }
      })
    },
    // 打开添加班级弹窗
    open() {
      this.visible = true
    },
    // 关闭添加班级弹窗
    close() {
      this.visible = false
    }
  }
}

</script>

<style>

</style>
