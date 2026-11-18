<template>
    <div class="moreinfo">
        <statistics></statistics>
        <div class="content">
          <el-tabs v-model="activeName">
            <el-tab-pane label="课程" name="course">
              <course></course>
            </el-tab-pane>
            <el-tab-pane label="实验" name="exercises">
              <exercises></exercises>
            </el-tab-pane>
            <el-tab-pane label="报告" name="report">
              <report></report>
            </el-tab-pane>
            <el-tab-pane label="讨论" name="community">
              <discussion ></discussion>
            </el-tab-pane>
          </el-tabs>
        </div>
    </div>
</template>

<script>
import statistics from './statistics'
import course from './tabs/course'
import exercises from './tabs/exercises'
import report from './tabs/report'
import discussion from './tabs/discussion'
export default {
  components: {
    statistics, course, exercises, report, discussion
  },
  props: ['avatar'],
  data() {
    return {
      activeName: 'course'
    }
  }
}
</script>

<style lang="scss">
.moreinfo{
  .content{
    margin: 15px 0;
    padding: 20px;
    background: #fff;
    border: 1px solid #eee;
    .el-tabs__item.is-active,.el-tabs__item:hover{
      color: #459d92;
    }
    .el-tabs__active-bar{
      background-color:#459d92;
    }
  }
}
</style>

