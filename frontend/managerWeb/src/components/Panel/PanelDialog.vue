<template>
  <div class="dialog">
    <el-dialog :title="title" :visible.sync="isVisible" :width="Width" :before-close="handleClose">
      <slot></slot>
      <div slot="footer" :style="{textAlign: alignment}" class="dialog-footer">
        <slot name="footerBtn"></slot>
      </div>      
    </el-dialog>
  </div>
</template>

<script>
export default {
  props: {
    title: String,
    isVisible: Boolean,
    alignment: {
      type: String,
      default: 'right'
    },
    Width: {
      type: String,
      default: '30%'
    }
  },
  methods: {
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done()
          this.sendMsgToParent()
        })
        .catch(_ => {})
    },
    sendMsgToParent() {
      this.$emit('listenToChildEvent', false)
    }
  }
}
</script>

<style scoped lang="scss">
.dialog /deep/ .el-dialog__header {
    border-bottom: 1px solid #dcdfe6;
    .el-dialog__title {
      font-size: 16px;
    }
}
// .el-dialog__body {
//   padding: 5px 20px;
// }
.dialog /deep/ .el-dialog__footer {
  border-top: 1px solid #dcdfe6;
  padding: 11px 15px;
}
</style>

