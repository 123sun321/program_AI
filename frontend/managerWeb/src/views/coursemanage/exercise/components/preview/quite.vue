<template>
    <el-dialog class="preview" title="效果预览" :visible.sync="isvisible" :fullscreen="true">
        <iframe
          class="img2"
          :src="'/fstatic/codeeditor/index.html?appid='+appid+'#/show?cid=' + codeId"
          allowfullscreen="allowfullscreen"
          frameborder="0"
          scrolling="no"
        ></iframe>
    </el-dialog>
</template>

<script>
export default {
  props: ['codeId'],
  data() {
    return {
      isvisible: false,
      appid: this.$store.getters.appid
    }
  },
  methods: {
    open() {
      this.isvisible = true
    }
  }
}
</script>

<style lang="scss">
.preview{
    .el-dialog
    {
        display: flex;
        flex-flow: column wrap;
        .el-dialog__header{
            border-bottom: 1px solid #e5e5e5;
        }
        .el-dialog__body {
            overflow: hidden;
            flex:1;
            padding: 0px!important;
            iframe{
                height: 100%;
                width: 100%;
            }
        }
    }
}
</style>

