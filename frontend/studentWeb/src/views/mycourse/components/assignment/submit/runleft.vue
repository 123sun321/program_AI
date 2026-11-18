<template>
    <div class="runleft">
       <!-- 左侧文档 -->
          <div class="step header" id="border">
            <span>{{coursedata.title}}</span>
          </div>
          <div class="content customscroll">
            <span v-html="coursedata.summary"></span>
          </div>
    </div>
</template>
<script>
/**
 * @file 作业编辑组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/29
 */
// import { get } from '@/api/mycourse/exercise'
import page from '@/components/page'
import { fetchOne } from '@/api/assignment/index'
import markDown from '@/components/markDown'
export default {
  components: {
    page,
    markDown
  },
  props: ['assignmentId'],
  data() {
    return {
      defaultImg: 'this.src="' + require('@/assets/images/default.jpeg') + '"', // 默认头像图像
      coursedata: '',
      flag: 0
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      fetchOne(this.assignmentId).then(e => {
        const data = e.data.data
        this.coursedata = data
      })
    }
  }
}
</script>
<style scoped lang="scss">
.detail img{
    width:80px;
    height:80px;
    border-radius:50%
}
.runleft{
    border: solid 1px #dfdfdf;
    height: 100%;
    display: flex;
    flex-flow: column;
    .header{
      padding: 15px;
      border-bottom: solid 1px #dfdfdf;
    }
    .content{
      overflow-y: auto;
      padding: 15px;
      flex: 1;
    }
}
</style>

