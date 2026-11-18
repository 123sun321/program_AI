<template>
   <el-dialog
    title="预览" class="webEdtor"
    :visible.sync="dialogVisible"
    width="100%">
        <iframe v-if="dialogVisible"
        class="img2"
        :src="'/fstatic/codeeditor/index.html?appid='+appid+'#/fullshow?cid=' + codeId"
        allowfullscreen="allowfullscreen"
        frameborder="0"
        scrolling="no"></iframe>
    </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      dialogVisible: false,
      appid: this.$store.getters.appid,
      codeId: undefined
    }
  },
  methods: {
    open(id) {
      this.codeId = id
      this.dialogVisible = true
    }
  }
}
</script>

<style lang="scss" scoped>
.webEdtor{
  width: 100%;
  height: 600px;
  border: 1px solid #cac6c6;
  margin-top: 30px;
  iframe{
    border: none;
    height: 100%;
    width: 100%;
  }
}
</style>

