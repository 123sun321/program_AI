<template>
    <el-dialog class="quitePreview" :title="title" :visible.sync="isvisible" :fullscreen="true">
        <iframe class="imgs" :src="baseHost+'/preview/quite/'+codeId" allowfullscreen="allowfullscreen" frameborder="0" scrolling="no" ></iframe>
    </el-dialog>
</template>

<script>
export default {
  props: ['codeId', 'title'],
  data() {
    return {
      isvisible: false,
      baseHost: process.env.BASE_API
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
.quitePreview{
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

