<template>
    <el-dialog title="添加学生信息" :visible.sync="visible"  width="40%">
        <Student :professionid="professionid" :gcid="gcid" ref="form" :flag="flag" :is-edit="false"></Student>
        <div slot="footer" class="dialog-footer">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </div>
    </el-dialog> 
</template>
<script>
/**
 * @file 添加学生组件
 * @author 赵雪丽
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/06
 */
import Student from './components/Student'
import {
  setStudent
} from '@/api/base/student'
export default {
  data() {
    return {
      flag: false,
      visible: false, // 是否显示添加学生弹窗
      gcid: 0,
      professionid: 0
    }
  },
  components: {
    Student
  },
  methods: {
    // 保存添加学生
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          setStudent(value).then(e => {
            if (e.data.success) {
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
    // 打开添加学生弹窗
    open() {
      this.visible = true
    },
    openDetail(professionid, gcid) {
      this.visible = true
      this.flag = true
      this.professionid = professionid
      this.gcid = gcid
    },
    // 关闭添加学生弹窗
    close() {
      this.visible = false
    }
  }
}

</script>