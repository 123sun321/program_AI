<template>
  <el-dialog title="修改课程信息" :visible.sync="visible" width="40%">
    <course ref="form" :eid="eid" :is-edit="true"></course>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  /**
 * @file 编辑课程组件
 * @author 张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/4/17
 */
  import Course from './components/Course'
  import {
    edit
  } from '@/api/coursemanage/course'
  export default {
    data() {
      return {
        visible: false,
        eid: ''
      }
    },
    components: {
      Course
    },
    methods: {
      save() {
        this.$refs.form.validate(e => {
          if (e) {
            const value = this.$refs.form.getValue()
            for (var i = 0; i < value.tags.length; i++) {
              if (value.tags[i].length > 20) {
                this.$message.error('单个标签长度不能大于20字符!')
                return
              }
            }
            edit(value).then(e => {
              if (e.data.success) {
                this.$sucmsg('保存成功')
                this.$emit('success')
                this.close()
              } else {
                this.$errmsg('保存失败')
              }
            })
          }
        })
      },
      open(id) {
        this.eid = id
        this.visible = true
      },
      close() {
        this.eid = ''
        this.visible = false
        this.$refs.form.reset()
      }
    }
  }

</script>

<style>

</style>
