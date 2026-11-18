<template>
  <el-dialog style="text-align:left;color:#FAFDFF" title="复制资源链接" :multipleSelection="multipleSelection" :visible.sync="visible" width="30%">
     <el-input type="text" id="success_form_input" v-model="fileLink">
    <!-- <template slot="append">链接7天后失效</template> -->
  </el-input>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" icon="document" @click="handleCopy(fileLink,$event)">复制链接</el-button>
    </div>
  </el-dialog>
</template>

<script>
/*
移动文件窗口
@author 马勇超
@date 2025/03/19 14:00
@version 1.0
*/
import clip from '@/utils/clipboard' // use clipboard directly
import clipboard from '@/directive/clipboard/index.js'
import RemoveFile from './components/removeFile'
import { fecthOne } from '@/api/file/file'
export default {
  directives: {
    clipboard
  },
  data() {
    return {
      fileLink: '',
      connect: null,
      visible: false, // 是否显示弹窗
      multipleSelection: []
    }
  },
  components: {
    RemoveFile
  },
  methods: {
    handleCopy(text, event) {
      clip(text, event)
    },
    clipboardSuccess() {
      this.$message({
        message: '复制成功！',
        type: 'success',
        duration: 1500
      })
    },
    // 打开移动弹窗
    open(ids) {
      this.multipleSelection = ids
      fecthOne(this.multipleSelection[0]).then(e => {
        this.connect = e.data.data.path
        this.fileLink = process.env.HOST + this.connect
      })
      this.visible = true
    },
    // 关闭移动弹窗
    close() {
      this.visible = false
      this.connect = ''
      this.fileLink = ''
    }
  }
}

</script>

<style>

</style>
