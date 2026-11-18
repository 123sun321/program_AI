<template>
  <el-dialog v-if="showDialog" style="text-align:left;color:#FAFDFF" title="移动到" :multipleSelection="multipleSelection" :visible.sync="visible" width="30%">
    <remove-file ref="form"></remove-file>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
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
import RemoveFile from './components/removeFile'
import { removeFile } from '../../../api/trainingPath'
export default {
  data() {
    return {
      visible: false, // 是否显示弹窗
      multipleSelection: [],
      showDialog: false
    }
  },
  components: {
    RemoveFile
  },
  methods: {
    // 保存添加
    save() {
      const value = this.$refs.form.getValue()
      const params = {
        childrenIds: this.multipleSelection,
        parentId: value.id ? '-' + value.id : '0'
      }
      removeFile(params).then(e => {
        if (e.data) {
          this.$sucmsg('移动文件夹成功')
          this.$emit('success')
          this.close()
        } else {
          this.$errmsg('文件夹移动失败！')
        }
      })
    },
    // 打开移动弹窗
    open(ids) {
      this.showDialog = true
      this.multipleSelection = ids
      this.visible = true
    },
    // 关闭移动弹窗
    close() {
      this.visible = false
      this.showDialog = false
    }
  }
}

</script>

<style>

</style>
