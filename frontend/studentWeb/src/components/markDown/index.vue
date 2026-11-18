<template>
    <mavon-editor :ishljs="ishljs" :toolbars="toolbars" :scrollStyle="scrollStyle" :subfield="subfield" :value="content" @change="change" ref="md" @imgAdd="$imgAdd" :style="{minHeight:height+'px'}" />
     <!-- <mavon-editor
      :defaultOpen="preview"
      :toolbarsFlag="false"
      :subfield="subfield"
      v-model="content"/> -->
</template>

<script>
import axios from 'axios'
export default {
  model: {
    prop: 'content',
    event: 'contentChange'
  },
  props: {
    content: String,
    height: { // 默认高度
      type: Number,
      default: 400
    },
    toolbars: {
      type: Object,
      default: function() {
        return {
          bold: true, // 粗体
          italic: true, // 斜体
          header: true, // 标题
          underline: true, // 下划线
          strikethrough: true, // 中划线
          mark: true, // 标记
          superscript: true, // 上角标
          subscript: true, // 下角标
          quote: true, // 引用
          ol: true, // 有序列表
          ul: true, // 无序列表
          link: true, // 链接
          imagelink: true, // 图片链接
          code: true, // code
          table: false, // 表格
          fullscreen: false, // 全屏编辑
          readmodel: false, // 沉浸式阅读
          htmlcode: true, // 展示html源码
          help: true, // 帮助
          /* 1.3.5 */
          undo: true, // 上一步
          redo: true, // 下一步
          trash: false, // 清空
          save: false, // 保存（触发events中的save事件）
          /* 1.4.2 */
          navigation: true, // 导航目录
          /* 2.1.8 */
          alignleft: false, // 左对齐
          aligncenter: false, // 居中
          alignright: false, // 右对齐
          /* 2.2.1 */
          subfield: true, // 单双栏模式
          preview: true // 预览
        }
      }
    },
    ishljs: { // 高亮代码
      type: Boolean,
      default: true
    },
    scrollStyle: { // 开启滚动条样式(暂时仅支持chrome)
      type: Boolean,
      default: true
    },
    subfield: { // 开启双栏编辑
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      action: process.env.BASE_API + '/AppFile/file/upload2'
    }
  },
  created() {
  },
  methods: {
    change(value, html) {
      this.$emit('contentChange', value)
    },
    // 绑定@imgAdd event
    $imgAdd(pos, $file) {
      // 第一步.将图片上传到服务器.
      var formdata = new FormData()
      formdata.append('image', $file)
      axios({
        url: this.action,
        method: 'post',
        data: formdata,
        headers: { 'Content-Type': 'multipart/form-data' }
      }).then((result) => {
        this.$refs.md.$img2Url(pos, result.data.data)
      })
    }
  }
}
</script>
