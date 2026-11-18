<template>
  <div class="run-main">
    <!-- 顶部 -->
    <!-- <el-row>
      <el-col :span="7">
        <div class="nav">
          <span class="pic" @click="callback">
            <img :src="require('@/assets/student/Experiment_return.png')">
          </span>
          <span class="tit">作品介绍</span>
        </div>
      </el-col>
      <el-col :span="17">
        <div class="nav" style="background-color:#666666;">
          <ul class="but">
            <li @click="onrun">
              <span class="img">
                <img :src="require('@/assets/student/Experiment_play.png')">
              </span>
              <span class="type">运行</span>
            </li>
            <li @click="onFork">
              <span class="img">
                <img :src="require('@/assets/student/fork.png')">
              </span>
              <span class="type">Fork</span>
            </li>
            <li @click="onload">
              <span class="img">
                <img :src="require('@/assets/student/Experiment_Formatbrush.png')">
              </span>
              <span class="type">格式刷</span>
            </li>
          </ul>
        </div>
      </el-col>
    </el-row> -->
    <el-row style="height:100%;">
      <el-col :span="7" class="left-content">
          <div class="nav">
            <span class="pic" @click="callback">
              <img :src="require('@/assets/student/Experiment_return.png')">
            </span>
            <span class="tit">作品介绍</span>
          </div>
          <div class="info" >
              <runleft :entity="entity"></runleft>
          </div>
      </el-col>
      <!-- 右侧编辑框 -->
      <el-col :span="17" class="run-content">
        <web-ide :id="recommendWorkId" :codeId="entity.codeDto.id" redirectUrl="/mycourse/course/trainingPath" type="production" v-if="isShow"></web-ide>
      </el-col>
    </el-row>
  </div>
</template>
<script>
/**
 * @file 作品运行组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/9
 */
import WebIde from '@/components/WebIde'
import runleft from './components/runleft'
import { get } from '@/api/recommendWork'
import { fork } from '@/api/training'
import page from '@/components/page'
export default {
  components: {
    page,
    runleft,
    WebIde
  },
  data() {
    return {
      entity: { codeDto: {}, user: {}, student: {}},
      recommendWorkId: this.$route.query.id,
      isShow: false,
      flag: 0
    }
  },
  created() {
    this.$nextTick(e => {
      this.init()
    })
  },
  methods: {
    init() {
      var vm = this
      if (this.recommendWorkId !== undefined) {
        get(this.recommendWorkId).then(e => {
          vm.entity = e.data.data
          vm.isShow = true
        })
      }
    },
    callback() {
      this.$router.push('index')
    },
    onFork() {
      var vm = this
      this.$cfm('是否Fork该作品？', e => {
        const loading = this.$loading({
          lock: true,
          text: '作品Fork中...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        fork(this.entity).then(e => {
          // 跳转对应的练习页面
          setTimeout(element => {
            vm.$sucmsg('Fork成功！')
            vm.$router.push({ name: 'runtraining', query: { id: e.data.data }})
            loading.close()
          }, 2000)
        })
      })
    }
  }
}
</script>
<style scoped lang="scss">
.run-main {
  height: 100%;
  background-color: #fff;
  .left-content{
    height: 100%;
    position: relative;
    display: flex;
    flex-flow: column;
    .nav {
      height: 50px;
      line-height: 50px;
      width: 100%;
      background-color: #2d2e3f;
      .pic {
        cursor: pointer;
        img{
          float: left;
          margin: 16px 5px 0 10px;
        }
      }
      .tit {
        font-size: 16px;
        color: #fff;
      }
    }
    .info{
      flex:1;
    }
  }
  .run-content {
   height: 100%;
  }
}

</style>

