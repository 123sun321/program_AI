<template>
  <div id="app">
    <div v-html="html" v-highlight></div>
  </div>
</template>

<script>
import vueCodeDiff from 'vue-code-diff'
import { createPatch } from 'diff'
import { Diff2Html } from 'diff2html'
import hljs from 'highlight.js'
import 'highlight.js/styles/googlecode.css'
import 'diff2html/dist/diff2html.css'
export default {
  name: 'code-diff',
  components: {
    vueCodeDiff
  },
  props: {
    oldString: {
      type: String,
      default: ''
    },
    newString: {
      type: String,
      default: ''
    },
    context: {
      type: Number,
      default: 5
    },
    outputFormat: {
      type: String,
      default: 'line-by-line'
    }
  },
  directives: {
    highlight: function(el) {
      const blocks = el.querySelectorAll('code')
      blocks.forEach((block) => {
        hljs.highlightBlock(block)
      })
    }
  },
  computed: {
    html() {
      return this.createdHtml(this.oldString, this.newString, this.context, this.outputFormat)
    }
  },
  methods: {
    createdHtml(oldString, newString, context, outputFormat) {
      function hljs(html) {
        return html.replace(/<span class="d2h-code-line-ctn">(.+?)<\/span>/g, '<span class="d2h-code-line-ctn"><code>$1</code></span>')
      }
      const args = ['', oldString, newString, '', '', { context: context }]
      const dd = createPatch(...args)
      const outStr = Diff2Html.getJsonFromDiff(dd, { inputFormat: 'diff', outputFormat: outputFormat, showFiles: false, matching: 'lines' })
      const html = Diff2Html.getPrettyHtml(outStr, { inputFormat: 'json', outputFormat: outputFormat, showFiles: false, matching: 'lines' })
      return hljs(html)
    }
  }
}
</script>

<style>
.hljs{
  display: inline-block;
  padding: 0;
  background: transparent;
  vertical-align:middle
}
.d2h-file-header{
  display: none
}
</style>
