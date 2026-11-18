<template>
  <div class="maincontent">
    <div class="back_btn">
       <el-button @click="callback">返回</el-button>
    </div>
    <div style="clear:both"></div>
    <el-tabs v-model="activeName">
      <el-tab-pane label="实验详情" name="first">
        <div class="webEdtor" v-if="exerciseJoinDetail.trainingDto.codeId">
          <iframe
            class="img2"
            :src="'/fstatic/codeeditor/index.html?appid='+appid+'#/fullshow?cid=' + exerciseJoinDetail.trainingDto.codeId"
            allowfullscreen="allowfullscreen"
            frameborder="0"
            scrolling="no"></iframe>
        </div>
      </el-tab-pane>
      <el-tab-pane label="实验报告" name="second">
        <vue-markdown class="markdown-body" v-highlight v-if="exercisejoinreport && exercisejoinreport.content" :source="exercisejoinreport.content"></vue-markdown>
        <div class="nodata" v-else>
            <img :src="require('@/assets/images/c6.jpg')">
            <p>该实验暂无报告！</p>
        </div>
      </el-tab-pane>
  </el-tabs>
  </div>
</template>
<script>
/**
 * @file 实验参与完成详情
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/19
 */
import VueMarkdown from 'vue-markdown' // 直接组件引入
import { get, getByJoinId } from '@/api/coursemanage/exercisejoin'
export default {
  components: {
    VueMarkdown // 声明组件
  },
  data() {
    return {
      loading: false,
      // 选项卡选中
      activeName: 'first',
      // 实验参与编号
      exercisejoinid: this.$route.query.exercisejoinid,
      // 完成详情集合
      exerciseJoinDetail: { trainingDto: { code: {}}},
      // 实验报告
      exercisejoinreport: {},
      appid: this.$store.getters.appid,
      domList: []
    }
  },
  created() {
    this.$nextTick(e => {
      this.init()
    })
  },
  methods: {
    init() {
      if (this.exercisejoinid === undefined || this.exercisejoinid === null) {
        this.$errmsg('实验错误！')
        return
      }
      get(this.exercisejoinid).then(e => {
        this.exerciseJoinDetail = e.data.data
        getByJoinId(this.exerciseJoinDetail.id).then(e => {
          this.exercisejoinreport = e.data.data
        })
      })
    },
    callback() {
      this.$router.go(-1)
    }
  }
}
</script>
<style lang="scss" scoped>
.back_btn{
  float: right;
  z-index: 100;
  margin-bottom:-30px;
  position: relative;
}
.nodata{
  text-align: center;
  p{
    color: #999;
  }
}
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

