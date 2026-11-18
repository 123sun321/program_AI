<template>
    <div class="userbody">
        <div class="userinfo clearfix">
          <div class="user-avatar pull-left">
              <img :src="userinfo.avatar" :onerror="defaultImg">
          </div>
          <div class="pull-left userinfo-details">
            <div class="username">{{userinfo.name}} 
              <el-tag v-if="userinfo.userType===2">教师</el-tag>
              <el-tag v-if="userinfo.userType===1" type="success">学生</el-tag> 
            </div>
            <div style="margin:8px 0">
             <user-star v-if="userinfo.userType === 1" :imgsize="16" :userid="userid"></user-star>
            </div>
            <div class="jointime">{{userinfo.creationTime}} 加入实训系统</div>
          </div>
        </div>
        <div class="container">
          <el-row :gutter="15">
            <el-col :span="6">
              <div class="user-visitors">
                <div class="sidebox-title">最近来访</div>
                <div class="sidebox-body">
                  <div class="item clearfix" @click="touser(item.visitor)" v-for="(item,index) in recordList" :key="index">
                      <img class="pull-left" :onerror="defaultImg" :src="item.user.userPic">
                      <div class="info">
                          <p>{{item.user.name}}</p>
                          <p class="time">{{item.creationTime| formatTime('{y}-{m}-{d} {h}:{i}')}}</p>
                      </div>
                  </div>
                </div>
              </div>
            </el-col>
            <el-col :span="18">
              <student-info v-if="userinfo.userType===1" ></student-info>
              <teacher-info v-else :avatar="userinfo.avatar"></teacher-info>
              
            </el-col>
          </el-row>
        </div>
    </div>
</template>

<script>
import studentInfo from './components/studentinfo'
import teacherInfo from './components/teacherinfo'
import userStar from '@/views/user/userStar'
import { getUserInfoByUserId, getUserRecord } from '@/api/user'
export default {
  components: {
    studentInfo, teacherInfo, userStar
  },
  created() {
    this.$nextTick(e => {
      this.init()
    })
  },
  data() {
    return {
      userid: this.$route.params.id,
      defaultImg: 'this.src="' + require('@/assets/images/default.jpeg') + '"', // 默认头像图像,
      userinfo: { userType: 0 },
      recordList: [],
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        userId: this.$route.params.id,
        orderBy: 'a.creation_time desc'
      },
      personScore: 0
    }
  },
  watch: {
    '$route'(to, from) {
      if (to.params.id !== from.params.id) {
        this.userid = this.form.userId = to.params.id
        this.init()
      }
    }
  },
  methods: {
    init() {
      this.userinfo.userType = 0
      // 用户基础信息
      getUserInfoByUserId(this.userid).then(e => {
        this.userinfo = e.data
        // 用户访问记录
        getUserRecord(this.form).then(e => {
          const data = e.data.data
          this.recordList = data.records
          this.form.page.total = data.total
        })
      })
    },
    touser(id) {
      this.$router.push({ path: '/user/info/' + id })
    }
  }
}
</script>

<style lang="scss" scoped>
.icon {
  width: 18px;
}
.userbody{
  width: 1280px;
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
          padding: 7px 0;
          cursor: pointer;
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
              font-size: 14px;
              color: #444;
              line-height: 16px;
            }
            .time{
              color: #999;
               font-size: 12px;
            }
          }
        }
      }
    }
  }
}
</style>

