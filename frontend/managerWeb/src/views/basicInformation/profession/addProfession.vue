<template>
  <el-dialog title="添加专业信息" :visible.sync="visible" width="30%">
    <profession ref="form" :is-edit="false"></profession>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
/*
新建专业弹窗组件
@author 袁延延
@date 2025/11/27 15:00
@version 1.0
*/
import Profession from './components/Profession'
import {
  addProfession
} from '@/api/base/professions'
export default {
  data() {
    return {
      visible: false // 是否显示添加专业弹窗
    }
  },
  components: {
    Profession
  },
  methods: {
    // 保存添加专业
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          console.log(value)
          addProfession(value).then(e => {
            if (e.data.success) {
              this.$sucmsg('添加专业信息成功')
              this.$emit('success')
              this.$refs.form.reset()
              this.close()
            } else {
              this.$errmsg('专业名称已存在')
            }
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
