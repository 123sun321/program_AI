<template>
<el-dialog title="实验详情预览" :visible.sync="visible" width="40%">
    <preview-content :content="content" ref="previewcontent"></preview-content>
    <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="close">确 定</el-button>
    </div>
</el-dialog>
</template>

<script>
/**
 * @file 实验详情预览
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/12
 */
import previewContent from './content'
export default {
  props: ['content'],
  data() {
    return {
      visible: false
    }
  },
  components: {
    previewContent
  },
  methods: {
    open() {
      this.visible = true
      this.$nextTick(e => {
        this.$refs.previewcontent.inithtml()
      })
    },
    close() {
      this.visible = false
    }
  }
}
</script>

<style>

</style>
