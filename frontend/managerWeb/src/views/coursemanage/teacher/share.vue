<template>
  <el-dialog style="text-align:left;color:#FAFDFF" title="复制课程邀请码"  :visible.sync="visible" width="30%">
    <el-row >
        <el-col :span="18"><el-input type="text" id="success_form_input" v-model="courseCode" :disabled="true"></el-input></el-col>
       <el-col :span="6"><el-button type="primary" @click="regain()">刷新邀请码</el-button></el-col>




  </el-row>

    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" icon="document" @click="handleCopy(courseCode,$event)">复制邀请码</el-button>
    </div>
  </el-dialog>
</template>

<script>
   /**
 * @file 复制课程邀请码
 * @author  张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/4/10
 */
import clip from '@/utils/clipboard' // use clipboard directly
import clipboard from '@/directive/clipboard/index.js'

import { regainCode } from '@/api/coursemanage/course'
export default {
  directives: {
       clipboard
  },
  data() {
       return {
         courseCode: '',
         courseid: '',
         connect: null,
         visible: false // 是否显示弹窗

       }
  },
  components: {
       regainCode
  },
  methods: {
       // 复制验证码
       handleCopy(text, event) {
         clip(text, event)
         this.visible = false
       },
       clipboardSuccess() {
         this.$message({
           message: '复制成功！',
           type: 'success',
           duration: 1500
         })
       },
       // 打开移动弹窗
       open(ids, courseId) {
         this.courseCode = ids
         this.courseid = courseId
         this.visible = true
       },
       // 关闭移动弹窗
       close() {
         this.visible = false
         this.connect = ''
         this.courseCode = ''
       },
       regain() {
         regainCode(this.courseid).then(e => {
           // debugger
           if (e.data.success) {
             this.courseCode = e.data.data
             this.$sucmsg('刷新成功')
             //  this.$emit('success')
             //  this.close()
           } else {
             this.$errmsg('刷新失败!')
           }
         })
       }
  }
}

</script>

<style>

</style>
