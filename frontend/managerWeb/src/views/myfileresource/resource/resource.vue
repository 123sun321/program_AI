<template>
  <el-dialog style="position:absolute;text-align:left;color:#FAFDFF;margin-top:-3%;" title="选择资源：" :visible.sync="visible" width="80%">
    <share-resource ref="form"></share-resource>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
/*
公共资源文件窗口
@author 马勇超
@date 2025/03/19 14:00
@version 1.0
*/
import ShareResource from '../resource/components/shareResource'
export default {
  data() {
    return {
      visible: false // 是否显示弹窗
    }
  },
  components: {
    ShareResource
  },
  methods: {
    // 保存添加
    save() {
      const value = this.$refs.form.getValue()
      this.$emit('chooseList', value)
      this.close()
    },
    // 打开弹窗
    open() {
      this.visible = true
    },
    // 关闭弹窗
    close() {
      this.visible = false
    }
  }
}

</script>

<style>

</style>
