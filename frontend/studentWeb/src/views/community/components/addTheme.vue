<template>
<el-dialog title="新增帖子" :visible.sync="visible" width="55%" :before-close="handleDialogClose">
    <theme ref="form" :is-edit="false"></theme>
    <div slot="footer">
        <el-button  @click="close">取 消</el-button>
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

import theme from './Theme'
// 引入接口方法
import { add } from '@/api/community/theme'
export default {
  data() {
    return {
      visible: false // 是否显示添加帖子信息弹窗
    }
  },
  components: {
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
              // this.$refs.form.wangeditorReset()
              this.$sucmsg('添加帖子成功')
              this.$emit('success')
              this.$refs.form.reset()
              this.close()
              this.$router.push({ path: 'index' })
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
      this.$refs.form.wangeditorReset()
      this.$refs.form.reset()
      this.visible = false
    },
    handleDialogClose() {
      this.close()
    }
  }
}

</script>

<style scoped lang="scss">
.c_btn{
  margin: 15px 0px;
        color: #1fad4e;
        border-color:#e8f7ed;
        background:#e8f7ed;
        border-radius: 4px;
 }
.c_btn:hover{border-color:#1fad4e; }
</style>
