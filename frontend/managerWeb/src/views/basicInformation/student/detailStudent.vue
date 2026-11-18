<template>
    <div class="userbody">
      <el-row type="flex"  justify="center">
        <el-col :span="20">
        <div class="userinfo clearfix">
          <div class="user-avatar pull-left">
              <img :src="userinfo.avatar" :onerror="defaultImg">
          </div>
          <div class="pull-left userinfo-details">
            <div class="username">{{userinfo.name}} 
              <el-tag type="primary" v-if="userinfo.userType===2">教师</el-tag>
              <el-tag type="primary" v-if="userinfo.userType===1">学生</el-tag>
            </div>
             <div style="margin:8px 0">
             <span> 
              <img class="icon" v-for="(imgLevel, index) in ScoreToList(personScore)" :key="index"  :src="computeImgUrl(imgLevel)">
             </span> <!-- <img class="icon" v-for="(imgLevel, index) in [3, 3, 3, 2, 2, 2, 1, 1, 1]" :key="index" :src="computeImgUrl(imgLevel)" alt=""> -->
            </div>
            <div class="jointime">{{userinfo.creationTime}} 加入实训系统</div>
          </div>
        </div>
        <div class="container">
          <el-row >
            <!-- <el-col :span="6">
              <div class="user-visitors">
                <div class="sidebox-title">最近来访</div>
                <div class="sidebox-body">
                  <div class="item clearfix" @click="touser(item.visitor)" v-for="(item,index) in recordList" :key="index">
                      <img class="pull-left" :src="item.user.userPic" :onerror="defaultImg">
                      <div class="info">
                          <p>{{item.user.name}}</p>
                          <p>{{item.creationTime| formatTime('{y}-{m}-{d} {h}:{i}')}}</p>
                      </div>
                  </div>
                </div>
              </div>
            </el-col> -->
            <el-col>
              <teacher-info :avatar="avatar" v-if="userinfo.userType===22"></teacher-info>
              <student-info   v-if="userinfo.userType===1"></student-info>
            </el-col>
          </el-row>
        </div>
        </el-col>
      </el-row>
    </div>
</template>

<script>
import studentInfo from './components/studentinfo'
import teacherInfo from './components/teacherinfo'
import { getStudent } from '@/api/base/student'
import { getUserInfoByUserId, getUserRecordByStudentId, getstudentPersonScore } from '@/api/user'
export default {
  components: {
    studentInfo, teacherInfo
  },
  created() {
    this.$nextTick(e => {
      this.init()
    })
  },
  data() {
    return {
      avatar: '',
      userid: '',
      personScore: '', // 积分
      stduentId: this.$route.params.id,
      defaultImg: 'this.src="' + require('@/assets/images/default.jpeg') + '"', // 默认头像图像
      // userid: this.$router.params.id,
      userinfo: { userType: 0 },
      recordList: [],
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        orderBy: 'a.creation_time desc'
      }
    }
  },
  methods: {
    init() {
      // 根据学生id获取学生信息
      getStudent(this.stduentId).then(e => {
        this.userid = e.data.data.userId
        getstudentPersonScore(this.userid).then(e => {
          this.personScore = e.data.data.scoreAll
        })
        // 用户基础信息通过用户Id
        getUserInfoByUserId(this.userid).then(e => {
          this.userinfo = e.data
          this.avatar = this.userinfo.avatar
          // 用户访问记录通过学生Id
          getUserRecordByStudentId(this.stduentId, this.form).then(e => {
            const data = e.data.data
            this.recordList = data.records
            this.form.page.total = data.total
          })
        })
      })
    },
    // 根据等级计算图片地址
    computeImgUrl(imgLevel) {
      const sunlight = 3
      const moon = 2
      const stars = 1

      if (imgLevel === sunlight) {
        return require('../../../assets/images/Exercisenotes_sunlight.png')
      } else if (imgLevel === moon) {
        return require('../../../assets/images/Exercisenotes_moon.png')
      } else if (imgLevel === stars) {
        return require('../../../assets/images/Exercisenotes_stars.png')
      } else {
        return ''
      }
    },
    // 根据积分数返回列表
    ScoreToList(score) {
      // 将十进制转化成4进制数组
      const Q = 4
      const Unit = 20
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
    },
    touser(id) {
      this.$router.push({ path: 'detailStudent', params: { userid: id }})
      // this.$router.push({ path: 'detailStudent' + id })
    }
  }
}
</script>

<style lang="scss" scoped>
.icon {
  width: 18px;
}
.content-new-background .app-main>div {
     background-color: #E4E7ED; 
     -webkit-box-shadow:none; 
    box-shadow: none; 
}
.userbody{
  //  width: 1280px;
  margin: 0 auto;
  .userinfo{
    display: flex;
    flex-flow: row wrap;
    background: #fff;
    padding: 20px;
    margin-top: 15px;
    height: 120px;
    border: 1px solid #eee;
    .user-avatar{
      width: 80px;
      img{
        border-radius: 50%;
        width: 80px;
        height: 80px;
      }
    }
    .userinfo-details{
      flex: 1;
      padding-left: 30px;
      .username{
        font-weight: 700;
        font-size: 22px;
        color: #a4a4a4;
        // margin-top: 10px;
      }
      .jointime{
        // margin-top: 10px;
        font-size: 14px;
        color: #666;
        letter-spacing: .5px;
        line-height: 20px;
      }
    }
  }
  .container{
    margin-top: 15px;
    .user-visitors{
      background-color: #ffffff;
      padding: 15px;
      .sidebox-title{
        padding: 0 0 10px;
        border-bottom: 1px solid #eee;
        font-size: 16px;
      }
      .sidebox-body{
        padding-top: 15px;
        .item{
          padding: 5px 0;
          // cursor: pointer;
          img{
            width: 32px;
            height: 32px;
            border-radius: 50%;
          }
          .info{
            float: left;
            padding-left: 10px;
            p{
              margin: 0px;
              font-size: 12px;
              color: #444;
              line-height: 16px;
            }
          }
        }
      }
    }
  }
}
</style>

