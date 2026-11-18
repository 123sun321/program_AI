<template>
  <div class="run-main">
    <!-- 顶部 -->
    <el-row>
      <el-col :span="7">
        <div class="left-content">
          <div class="nav">
            <span class="pic" @click="callback">
              <img :src="require('@/assets/student/Experiment_return.png')">
            </span>
            <span class="tit">练习介绍</span>
          </div>
          <div class="content">
            <runleft :entity="entity" @onsave="onsave"></runleft>
          </div>
        </div>
      </el-col>
      <el-col :span="17">
        <div class="right-content">
          <web-ide :id="trainingid" :codeId="entity.code.id" type="training" redirectUrl="/mycourse/course/trainingPath" v-if="isShow"></web-ide>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
/**
 * @file 作品运行组件
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/05/10
 */
import { mapGetters } from 'vuex'
import WebIde from '@/components/WebIde'
import runleft from './components/runleft'
import { get, save } from '@/api/training'
import page from '@/components/page'
export default {
  computed: {
    ...mapGetters([
      'userId'
    ])
  },
  components: {
    page,
    runleft,
    WebIde
  },
  data() {
    return {
      entity: { code: {}, user: {}, student: {}},
      isShow: false,
      trainingid: this.$route.query.id
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
      if (this.trainingid !== undefined) {
        get(this.trainingid).then(e => {
          vm.entity = e.data.data
          vm.isShow = true
        })
      }
    },
    callback() {
      this.$router.go(-1)
    },
    onrun() {
      this.$sucmsg('运行！')
    },
    onsave() {
      save(this.entity).then(e => {
        this.$sucmsg('保存成功！')
      })
    },
    onload() {
      this.$sucmsg('格式刷！')
    }
  }
}
</script>
<style scoped lang="scss">
.run-main {
  .el-row{
    height: 100%;
    .el-col{
      height: 100%;
    }
  }
  height: 100%;
  background-color: #fff;
  .left-content{

    height: 100%;
    display: flex;
    flex-flow: column;
    .nav {
        height: 50px;
        width: 100%;
        background-color: #2d2e3f;
        display: flex;
        flex-flow: row wrap;
        align-items: center;
        .pic {
          cursor: pointer;
          padding: 0px 10px;
        }
        .tit {
          font-size: 16px;
          color: #fff;
          z-index: 110;
        }
      }
      .content{
        flex: 1;
      }
  }
  .right-content{
    height: 100%;
  }
}
</style>

