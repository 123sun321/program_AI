<template>
  <el-dialog title="修改帖子信息" :visible.sync="visible" width="55%">
    <theme ref="form" :eid="eid" :is-edit="true"></theme>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
/*
修改帖子弹窗组件
@author 张浩杰
@date 2025/03/29 16:30
@version 1.0
*/
import VueMarkdown from 'vue-markdown' // 直接组件引入
import theme from './components/Theme'
import { edit } from '@/api/community/theme'
export default {
  data() {
    return {
      visible: false, // 是否显示修改帖子信息弹窗
      eid: 0
    }
  },
  components: {
    VueMarkdown,
    theme
  },
  methods: {
    // 保存修改帖子信息
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          edit(value).then(e => {
            if (e.data.success) {
              this.$sucmsg('修改帖子信息成功')
              this.$emit('success')
              this.close()
            } else {
              this.$errmsg('帖子已存在')
            }
          })
        }
      })
    },
    // 打开修改帖子信息弹窗
    open(id) {
      this.eid = id
      this.visible = true
    },
    // 关闭修改帖子信息弹窗
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
