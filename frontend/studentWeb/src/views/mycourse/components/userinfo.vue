<template>
    <div class="userinfo">
        <div class="pic" @click="onclick">
           <img :src="avatar || require('@/assets/images/team-logo.png')" >
         <!-- <div class="medium" @click="onclick"><img :src="require('@/assets/student/Worksdisplay_edit.png')" ></div> -->
        </div>
        <div class="username">{{name}}
        </div>
        <div class="userstar">
          <user-star :imgsize="14" :showToolTip="false"></user-star>
        </div>
        <div class="grad">{{className}}</div>
        <div></div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { selectCount } from '../../../api/person/personalscore'
import userStar from '@/views/user/userStar'
export default {
  components: {
    userStar
  },
  computed: {
    ...mapGetters([
      'name',
      'avatar',
      'unitStarScore',
      'className'
    ])
  },
  data() {
    return {
      personalScore: 0, // 个人积分总数
      showScore: false
    }
  },
  mounted() {
    this.$nextTick(e => {
      selectCount().then(e => {
        this.personalScore = e.data.data.scoreAll
        this.showScore = true
      })
    })
  },
  methods: {
    onclick() {
      this.$router.push({ path: '/personinfo/personinfo/userinfo' })
    },
    pathToScore() {
      this.$router.push({ path: '/personinfo/personinfo/personalScore' })
    },
    // 根据等级计算图片地址
    computeImgUrl(imgLevel) {
      const sunlight = 3
      const moon = 2
      const stars = 1

      if (imgLevel === sunlight) {
        return require('../../../assets/student/Exercisenotes_sunlight.png')
      } else if (imgLevel === moon) {
        return require('../../../assets/student/Exercisenotes_moon.png')
      } else if (imgLevel === stars) {
        return require('../../../assets/student/Exercisenotes_stars.png')
      } else {
        return ''
      }
    },
    // 根据积分数返回列表
    ScoreToList(score) {
      // 将十进制转化成4进制数组
      const Q = 4
      const Unit = this.unitStarScore
      const totalstarnum = Math.floor(score / Unit)
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
  }
}
</script>

<style lang="scss" scoped>
.userinfo{
    text-align: center;
    border-bottom: 2px solid #3b8a80;
    position: relative;
    .pic{
      cursor: pointer;
      padding: 40px 60px 0 60px;
      img{width: 110px;height: 110px;border-radius: 5px;}
    }
    .medium{
    margin-top: -24px;
    // height: 30px;
    width: 120px;
    text-align: right;
    cursor: pointer;
      img{
        width: 20px;
        height: 20px;
      }
    }
    .userstar{
      padding: 5px 0;
    }
    .username{color: #ffffff;font-size: 22px;padding: 15px 0 0px 0;}
    .grad{color: #ffffff;font-size: 16px;padding-bottom: 30px;}
}
</style>
