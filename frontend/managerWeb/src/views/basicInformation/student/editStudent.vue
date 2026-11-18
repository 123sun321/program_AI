<template>
    <!-- 修改学生 -->
    <el-dialog   title="修改学生" :visible.sync="visible"  width="40%">
        <student ref="form" :eid="eid" :is-edit="true" :flag="flag"></student>
        <div slot="footer" class="dialog-footer">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </div>
    </el-dialog>
</template>
  
<script>
/**
 * @file 编辑学生组件
 * @author 赵雪丽
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/06
 */
import Student from './components/Student'
import {
  updateStudent
} from '@/api/base/student'
export default {
  data() {
    return {
      flag: false,
      visible: false, // 是否显示修改学生弹窗
      eid: 0 // 所修改学生的Id
    }
  },
  components: {
    Student
  },
  methods: {
    // 保存修改学生
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          console.log(value)
          updateStudent(value).then(e => {
            if (e.data.data) {
              this.$sucmsg('修改成功')
              this.$emit('success')
              this.close()
            } else {
              this.$errmsg('修改失败')
            }
          })
        }
      })
    },
    // 打开修改学生弹窗 id [String] 获取所修改的学生弹窗Id
    open(id) {
      this.eid = id
      this.visible = true
    },
    openDetail(id) {
      this.eid = id
      this.flag = true
      this.visible = true
    },
    // 关闭修改学生弹窗
    close() {
      this.eid = 0
      this.$refs.form.reset()
      this.visible = false
    }
  }
}
</script>
