<template>
  <div class="co-upload">
    <input :id="id" type="file" @change="change" class="hideinput" />
     <slot>
         <el-button><span :class="icon"></span>{{title}}</el-button> 
     </slot>  
  </div>
</template>
<script>
import request from '@/utils/request'
export default {
  props: ['title', 'icon', 'args', 'action', 'limit', 'types'],
  data() {
    return {
      file: ''
    }
  },
  created() {
    if (this.id === undefined) this.id = 'cofile'
  },

  methods: {
    change(e) {
      if (e.target.files.length === 0) return
      this.file = e.target.files[0]
      if (this.filter()) {
        this.upload()
      }
    },
    filter() {
      if (this.limit !== undefined) {
        if (this.file.size / 1024 / 1024 > this.limit) { this.$message.error('上传文件过大!不能超过' + this.limit + 'M'); return false }
      }
      if (this.types !== undefined) {
        if (this.types.indexOf(this.file.type) === -1) { this.$message.error('上传文件类型不合法'); return false }
      }
      return true
    },
    upload() {
      const file = this.file
      const param = new FormData()
      param.append('file', file)
      if (this.args !== null && this.args !== undefined) {
        for (const i in this.args) {
          param.append(i, this.args[i])
        }
      }
      const config = {
        headers: { 'Content-Type': 'multipart/form-data' }
      }
      request.post(this.action, param, config).then(e => {
        this.getFileOdiv().value = ''
        this.$emit('success', e, this.file)
      }).catch(e => {
        this.$emit('error', e, this.file)
        this.getFileOdiv().value = ''
      })
    },
    getFileOdiv() {
      var fileODiv = window.document.getElementById(this.id)
      return fileODiv
    }
  }

}
</script>
<style scoped lang="scss">
.co-upload{
  position: relative; 
  display: inline-block;
  overflow: hidden;
  .hideinput{
    position: absolute;
    top: 0px;
    left: 0px;
    right: 0px;
    bottom: 0px;
    opacity:0;
    filter:alpha(opacity=0);
  }
}

</style>


