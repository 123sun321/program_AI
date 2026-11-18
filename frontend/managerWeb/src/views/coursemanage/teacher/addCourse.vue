<template>
<el-dialog title="添加课程信息" :visible.sync="visible" width="40%">
    <course ref="form" :is-edit="false"></course>
    <div slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
    </div>
</el-dialog>
</template>

<script>
/**
 * @file 添加课程基本信息
 * @author 张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2018/4/10
 */
import Course from './components/Course'
import {
  addTeacherCourse
} from '@/api/coursemanage/course'
export default {
  data() {
    return {
      visible: false
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
          addTeacherCourse(value).then(e => {
            if (e.data.data) {
              this.$sucmsg('保存成功')
              this.$emit('success')
              this.$refs.form.reset()
              this.close()
            } else {
              this.$errmsg('保存失败')
            }
          })
        }
      })
    },
    open() {
      this.visible = true
    },
    close() {
      this.visible = false
      this.$refs.form.reset()
    }
  }
}
</script>

<style>

</style>
