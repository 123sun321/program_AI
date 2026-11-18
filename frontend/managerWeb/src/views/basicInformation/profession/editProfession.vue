<template>
  <el-dialog title="修改专业信息" :visible.sync="visible" width="30%">
    <profession ref="form" :eid="eid" :is-edit="true"></profession>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
/*
修改专业弹窗组件
@author 袁延延
@date 2025/11/27 15:00
@version 1.0
*/
import Profession from './components/Profession'
import { edit } from '@/api/base/professions'
export default {
  data() {
    return {
      visible: false, // 修改专业信息 弹窗
      eid: 0
    }
  },
  components: {
    Profession
  },
  methods: {
    // 保存新建专业
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          value.gradeList = null
          console.log(value)
          edit(value).then(e => {
            if (e.data.data) {
              this.$sucmsg('修改专业信息成功')
              this.$emit('success')
              this.close()
            } else {
              this.$errmsg('专业名称已存在')
            }
          })
        }
      })
    },
    // 打开新建专业弹窗
    open(id) {
      this.eid = id
      this.visible = true
    },
    // 关闭新建专业弹窗
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
