<template>
  <div class="dialog">
    <el-dialog :title="title" :visible.sync="isVisible" width="30%" :before-close="handleClose">
      <slot></slot>
      <div slot="footer" class="dialog-footer">
        <slot name="footerBtn">没有成功！</slot>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    props: {
      title: String,
      isVisible: Boolean,
      dialogObj: Object
    },
    methods: {
      handleClose() {
        console.log(this.dialogObj)
        this.$emit('ievent', false)
      }
    }
  }

</script>
<style scoped lang="scss">
  .dialog /deep/.el-dialog__header {
    background-color: #F2F6FC;
  }

</style>
