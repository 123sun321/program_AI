<template>
  <el-dialog class="mydialog" :visible.sync="visible" :show-close="false" :width="mywidth ? mywidth :'40%'" :close-on-click-modal="false">
    <div slot="title" class="dialog-title"><img :src="require('@/assets/student/logo.png')" /></div>
    <slot></slot>
    <div slot="footer" class="dialog-footer">
      <el-button size="medium" @click="visible = false">取 消</el-button>
      <el-button size="medium" type="success" @click="onsoure">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  props: ['mywidth'],
  data() {
    return {
      visible: false
    }
  },
  methods: {
    open() {
      this.visible = true
    },
    close() {
      this.visible = false
    },
    onsoure() {
      this.$emit('onsoure')
    }
  }
}
</script>

<style lang="scss">
.mydialog{
    .el-dialog{
        background: url(~@/assets/student/Privatecourses_Windowbg.png) no-repeat;
        background-size:100% 100%;
        border-radius: 5px;
        .dialog-title{background-color: #1d1d1d;height: 53px;padding: 10px;border-radius: 5px 5px 0 0;}
        .el-dialog__header{padding: 0px;}
        .el-dialog__footer{padding: 20px 15px;text-align: center;}
    }
}
</style>
