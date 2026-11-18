<template>
    <div class="wangeditor">
     <div :id="toolbarId" class="toolbar"></div>
     <div :id="editorId" class="text" :style="{height:height+'px'}"></div>
    </div>
</template>
<script>
import E from 'wangeditor'
export default {
  name: 'editor',
  model: {
    prop: 'content',
    event: 'proChange'
  },
  props: {
    content: String,
    menubar: {
      type: Array,
      default: function() {
        return ['head', // 标题
          'bold', // 粗体
          'fontSize', // 字号
          'fontName', // 字体
          'italic', // 斜体
          'underline', // 下划线
          'strikeThrough', // 删除线
          'foreColor', // 文字颜色
          'backColor', // 背景颜色
          'link', // 插入链接
          'list', // 列表
          'justify', // 对齐方式
          'quote', // 引用
          'emoticon', // 表情
          'image', // 插入图片
          'table', // 表格
          'video', // 插入视频
          'code', // 插入代码
          'undo', // 撤销
          'redo' // 重复
        ]
      }
    },
    height: { // 默认高度
      type: Number,
      default: 400
    }
  },
  data() {
    return {
      editorId: undefined,
      toolbarId: undefined,
      isInit: true,
      editor: undefined
    }
  },
  created() {
    var numberId = Math.floor(Math.random() * 10 + 1)
    this.editorId = 'editorId' + numberId
    this.toolbarId = 'toolbarId' + numberId
  },
  mounted() {
    this.editor = new E('#' + this.toolbarId, '#' + this.editorId)
    this.editor.customConfig.onchange = (html) => {
      this.isInit = false
      this.$emit('proChange', html)
    }
    this.editor.customConfig.menus = this.menubar
    this.editor.customConfig.uploadImgServer = process.env.BASE_API + '/AppFile/file/upload2'
    this.editor.customConfig.uploadImgHooks = {
      customInsert: function(insertImg, result, editor) {
        var url = result.data
        insertImg(url)
      }
    }
    this.editor.create()
  },
  watch: {
    content(n, o) {
      if (this.isInit) {
        if (n === undefined) {
          n = null
        }
        this.editor.$textElem[0].innerHTML = n
      }
    }
  },
  methods: {
    reset() {
      this.isInit = true
    }
  }
}
</script>

<style scoped lang="scss">
.toolbar{
  background-color:#f1f1f1;border:1px solid #ccc;
}
.text{
  border:1px solid #ccc; border-top:none; z-index:10000;
  /deep/ .w-e-text{
    overflow-y: auto;
  }
}
</style>
