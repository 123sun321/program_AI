<template>
    <div class="mainbox customscroll">
       <iframe
        class="codeEditor"
        :src="'/fstatic/codeeditor/index.html?appid='+appid+'#/fullshow?cid=' + codeId"
        allowfullscreen="allowfullscreen"
        frameborder="0"
        scrolling="no"></iframe>
    </div>
</template>

<script>
export default {
  props: ['codeId'],
  created() {
  },
  data() {
    return {
      appid: this.$store.getters.appid
    }
  },
  methods: {
    showRecord() { // 开始
      // var vm = this
      // if (!vm.recordData) {
      //   vm.$errmsg('暂无编辑记录!')
      //   return
      // }
      // vm.codeDto.html = vm.codeDto.js = vm.codeDto.css = ''
      // var recordList = JSON.parse(this.recordData)
      // var time = 0
      // var timer = setInterval(function() {
      //   if (time === recordList.length) {
      //     clearInterval(timer)
      //   }
      //   var element = recordList[time]
      //   if (element.name === 'html') {
      //     vm.codeDto.html = element.text
      //   } else if (element.name === 'css') {
      //     vm.codeDto.css = element.text
      //   } else {
      //     vm.codeDto.js = element.text
      //   }
      // var element = recordList[count]
      // if (time === element.endTime) {
      //   if (element.name === 'html') {
      //     vm.codeDto.html = element.text
      //   } else if (element.name === 'css') {
      //     vm.codeDto.css = element.text
      //   } else {
      //     vm.codeDto.js = element.text
      //   }
      //   count++
      // }
      //   time++
      // }, 1000)
    }
  }
}
</script>

<style lang="scss" scoped>
.mainbox
{
  width: 100%;
  height: 700px;
  border: 1px solid #cac6c6;
  iframe{
    border: none;
    height: 100%;
    width: 100%;
  }
}
</style>

