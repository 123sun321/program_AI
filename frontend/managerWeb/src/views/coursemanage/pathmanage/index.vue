<template>
    <div class="maincontent">
        <div class="new">
          <el-button type="primary" @click="openpag">学习详情</el-button>
          </div>
         <div style="clear:both"></div>
         <!-- <el-button type="primary" @click="openpag">学习详情</el-button> -->
         <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="路径详情" name="baseinfo">
               <path-detail></path-detail>
            </el-tab-pane>
            <el-tab-pane label="评论" name="avatar">
              <span slot="label">评论<span class="messageCount" v-if="this.msg>0">({{msg}})</span></span>
                <discuss v-on:headCallBack="headCall"></discuss>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>
<script>
/**
 * @file 路径管理组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/13
 */
import pathDetail from './pathDetail'
import discuss from './discuss'
export default {
  components: {
    pathDetail, discuss
  },
  data() {
    return {
      activeName: 'baseinfo',
      msg: ''// 评论条数
    }
  },
  created() {
    this.headCall()
  },
  methods: {
    openpag() {
      this.$router.push('studyDetail')
    },
    headCall(msg) { // 回调方法，接收子组件传的参数
      this.msg = msg
    },
    handleClick(tab, event) {
      console.log(tab, event)
    }
  }

}
</script>
<style lang="scss" scoped>
.maincontent{
  position: relative;
}
.new{
  float: right;
  z-index: 100;
  margin-bottom:-30px;
  position: relative;
}
</style>
