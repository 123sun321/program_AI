<template>
    <div class="run-main">
    <!-- 顶部 -->
    <el-row>
      <el-col :span="7">
        <div class="left-content">
          <div class="nav">
            <!-- <span class="pic" @click="callback"><img :src="require('@/assets/student/Experiment_return.png')" ></span>  -->
            <span class="tit">
              <div class="tit-tab" v-bind:class="{active:show==='text'}" @click="tabschange('text')">实验文档</div>
              <div class="tit-tab" v-bind:class="{active:show==='resource'}" @click="tabschange('resource')">实验资源</div>
              <div class="tit-tab" v-bind:class="{active:show==='report'}" @click="tabschange('report')">实验报告</div>
              <div class="tit-tab" v-bind:class="{active:show==='ai'}" @click="tabschange('ai')">AI伴学</div>
            </span>
          </div>
          <div class="content" style="overflow:auto;">
            <run-left v-show="show==='text'" :content="entity.content"></run-left>
            <resource v-show="show==='resource'" :resource="entity.exerciseResourceDtoList"></resource>
            <report v-show="show==='report'" :exerciseid="exerciseid"></report>
            <ai-chat v-show="show==='ai'" :exerciseId="exerciseid" :entity="entity"></ai-chat>
          </div>
        </div>
      </el-col>
      <el-col :span="17">
        <div class="right-content">
           <web-ide :id="exerciseid" type="exercise" :startType="this.type === 'startagain' ? 1 : 0" redirectUrl="/mycourse/course/trainingPath" v-if="isShow"></web-ide>
        </div>
      </el-col>
    </el-row>
    </div>
</template>
<script>
/**
 * @file 实验运行组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/9
 */
import runLeft from './components/runLeft'
import report from './components/report'
import resource from './components/resource'
import aiChat from './components/aiChat'
import { getExerciseInfo } from '@/api/mycourse/exercise'
import page from '@/components/page'
import WebIde from '@/components/WebIde'
export default {
  components: {
    page,
    runLeft,
    report,
    WebIde,
    resource,
    aiChat
  },
  data() {
    return {
      type: this.$route.query.type,
      exerciseid: this.$route.query.exerciseid, // 练习编号
      show: 'text',
      timer: undefined,
      entity: { codeDto: {}},
      flag: 0,
      isShow: false
    }
  },
  created() {
    if (!window.localStorage.getItem('redirect')) {
      this.startexercise()
      this.$nextTick(e => {
        this.getInformation()
      })
    }
  },
  methods: {
    // 启动试验
    startexercise() {
      const loading = this.$loading({
        lock: true,
        text: '启动实验中',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      setTimeout(() => {
        loading.close()
      }, 2000)
    },
    tabschange(data) {
      this.show = data
    },
    // 获取实验信息
    getInformation() {
      var vm = this
      if (this.exerciseid !== undefined) {
        var startType = 0
        if (this.type === 'startagain') {
          startType = 1
        }
        getExerciseInfo(this.exerciseid, startType).then(e => {
          vm.entity = e.data.data
          this.isShow = true
        })
      }
    },
    callback() {
      this.$router.back(-1)
    }
  }
}
</script>
<style scoped lang="scss">
.run-main{
  .el-row{
    height: 100%;
    .el-col{
      height: 100%;
    }
  }
  height: 100%;
  background-color: #fff;
  .left-content{
    line-height: 1.5!important;
    height: 100%;
    display: flex;
    flex-flow: column;
    .nav{
        height:  50px;
        line-height: 50px;
        width: 100%;
        background-color:#2D2E3F;
        float: left;
        position: relative;
        z-index: 100;
        .pic{
          cursor: pointer;
          img{
            float: left;
          }
        }
        .tit{
            margin-top: -13px;
            font-size: 16px;
            color: #fff;
            z-index: 110;
          }
      .tit-tab{
        cursor: pointer;
        padding: 0px 10px 10px 10px;
        float: left;
      }
      .tit-tab:hover,.active{
        background: url('~@/assets/images/console-tab-bg.png') no-repeat;
        background-size: 100% 100%;
      }
    }
    .content {
      flex: 1;
      overflow: auto;
    }
  }
}
.right-content {
  height: 100%;
}
</style>

