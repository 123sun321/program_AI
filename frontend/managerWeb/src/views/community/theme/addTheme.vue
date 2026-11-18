<template>
<el-dialog title="新增帖子" :visible.sync="visible" width="55%">
    <theme ref="form" :is-edit="false"></theme>
    <div slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
    </div>
</el-dialog>
</template>

<script>
/*
添加帖子信息弹窗组件
@author 张浩杰
@date 2025/03/29 16:30
@version 1.0
*/
import Tinymce from '@/components/Tinymce'
import VueMarkdown from 'vue-markdown' // 直接组件引入
import theme from './components/Theme'
// 引入接口方法
import { add } from '@/api/community/theme'
export default {
  data() {
    return {
      visible: false // 是否显示添加帖子信息弹窗
    }
  },
  components: {
    Tinymce,
    VueMarkdown,
    theme
  },
  methods: {
    // 保存社区帖子信息
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          add(value).then(e => {
            if (e.data.success) {
              this.$refs.form.wangeditorReset()
              this.$sucmsg('添加帖子成功')
              this.$emit('success')
              this.$refs.form.reset()
              this.close()
            } else {
              this.$errmsg('帖子已存在')
            }
          })
        }
      })
    },
    // 打开添加帖子信息弹窗
    open() {
      this.visible = true
    },
    // 关闭添加帖子信息弹窗
    close() {
      this.visible = false
    }
  }
}

</script>

<style scoped>

</style>
