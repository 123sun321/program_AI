
<template>
<el-dialog title="发表评论" :visible.sync="visiable" width="40%">
    <div class="comments">
    <el-input  type="textarea" ref="input" v-model="value" :rows="6"  :placeholder="tex"></el-input>
        <div class="operation-button">
    <el-button type="primary" plain @click="cancel">取消</el-button>
    <el-button type="primary" @click="onPublish(value)">发表评论</el-button>
    </div>
    <div style="clear:both"></div>
</div>
</el-dialog>
</template>

<script>
/**
 * @file 添加评论组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/14
 */
import { addcomment } from '@/api/coursemanage/pathmanage'
export default {
  props: ['bindData'],
  data() {
    return {
      visiable: false, // 弹窗
      tex: '', // 输入框文字
      value: '', // 输入的值
      texname: '', // 回复对象
      uId: '' // 回复id
    }
  },

  methods: {
    // 打开弹窗
    open(itemname, Id) {
      this.visiable = true
      this.tex = '回复' + itemname + ':'
      this.texname = itemname
      this.uId = Id
    },
    // 回复评论
    onPublish(value) {
      const params = {
        content: value,
        linkId: this.uId,
        reply: true,
        category: 3
      }
      addcomment(params).then(e => {
        if (e.data.success) {
          this.$sucmsg('回复评论成功')
          this.bindData()
          this.close()
        } else {
          this.$errmsg('回复评论失败')
        }
      })
      this.cancel()
    },
    // 取消回复评论
    cancel() {
      this.visiable = false
      this.value = ''
      this.itemname = ''
    }
  }
}
</script>
<style scoped>
.operation-button{
    float: right;
    margin-top: 20px;
  }

</style>

