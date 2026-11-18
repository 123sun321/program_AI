<template>
    <div v-if="this.unitStarScore" class="clearfix" >
        <el-tooltip v-if="showToolTip" effect="dark" :content="personalScore+'积分'" placement="right">
            <div class="tooltip-userstar" @click="pathToScore">
                <img v-for="(imgLevel, index) in scoreList" :width="imgsize" :height="imgsize" :key="index" :src="computeImgUrl(imgLevel)" alt="">
            </div>
        </el-tooltip>
        <div v-else class="userstar" @click="pathToScore">
            <img v-for="(imgLevel, index) in scoreList" :width="imgsize" :height="imgsize" :key="index" :src="computeImgUrl(imgLevel)" alt="">
        </div>
    </div>
</template>

<script>
// import { mapGetters } from 'vuex'
import { selectCount } from '@/api/person/personalscore'
import { getstudentPersonScore } from '@/api/user'
export default {
  props: {
    // 图标大小
    imgsize: {
      type: Number,
      default: 18
    },
    // 用户编号
    userid: {
      type: String,
      default: '0'
    },
    // 是否显示toolTip
    showToolTip: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    // ...mapGetters([
    //   'unitStarScore'
    // ]),
    // 根据积分数返回列表
    scoreList() {
      // 将十进制转化成4进制数组
      const Q = 4
      const Unit = this.unitStarScore
      const totalstarnum = Math.floor(this.personalScore / Unit)
      const arr = []
      // 通过计算对应的太阳,月亮,星星数 最后合成等级数组
      const sunnum = Math.floor(totalstarnum / (Q * Q))
      const moonnum = Math.floor((totalstarnum % (Q * Q)) / Q)
      const starnum = Math.floor(totalstarnum % Q)
      for (let i = 0; i < sunnum; i++) {
        arr.push(3)
      }
      for (let i = 0; i < moonnum; i++) {
        arr.push(2)
      }
      for (let i = 0; i < starnum; i++) {
        arr.push(1)
      }
      return arr
    }
  },
  data() {
    return {
      personalScore: 0,
      unitStarScore: 20
    }
  },
  mounted() {
    this.$nextTick(e => {
      setTimeout(this.getScore, 500)
    })
  },
  methods: {
    getScore() {
      var vm = this
      if (this.userid !== '0' && this.userid !== null && this.userid !== undefined) {
        getstudentPersonScore(vm.userid).then(e => {
          vm.personalScore = e.data.data.scoreAll
        })
      } else {
        selectCount().then(e => {
          vm.personalScore = e.data.data.scoreAll
        })
      }
    },
    pathToScore() {
      if (this.userid === '0') {
        this.$router.push({ path: '/personinfo/personinfo/personalScore' })
      }
    },
    // 根据等级计算图片地址
    computeImgUrl(imgLevel) {
      const sunlight = 3
      const moon = 2
      const stars = 1
      if (imgLevel === sunlight) {
        return require('@/assets/images/Exercisenotes_sunlight.png')
      } else if (imgLevel === moon) {
        return require('@/assets/images/Exercisenotes_moon.png')
      } else if (imgLevel === stars) {
        return require('@/assets/images/Exercisenotes_stars.png')
      } else {
        return ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
    .tooltip-userstar{
        float: left;
    }
</style>
