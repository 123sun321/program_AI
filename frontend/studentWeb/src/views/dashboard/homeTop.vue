<template>
   <el-row :gutter="20">
       <!-- 用户信息 -->
     <el-col :span="8">
         <div class="home-top-left">
             <div class="user-info-t1">
                <div class="user-img-t1">
                  <img class="user-avatar" :src="avatar || require('@/assets/images/team-logo.png')">
                </div>
                <div class="user-info-t2">
                    <div class="user-name">
                        <span>{{this.$store.getters.name}}</span>
                    </div>
                    <div class="user-address">
                        <img :src="homeImg.img_address" alt="">
                        <span>{{this.$store.state.user.schoolName}}</span>
                    </div>
                </div>
             </div>
             <div class="notice-user-message">
                 <div class="notice-title">
                     消息通知
                 </div>
                 <div v-loading="loading" style="min-height:148px;">
                    <div v-if="messageList.length>0"  >
                        <div class="notice-content" v-for="(item,index) in messageList" :key="index" @click="titleClick(item)">
                            <div class="message-details">{{item.title}}</div>
                            <div class="message-time">{{item.sendTime}}</div>
                        </div>
                        <div class="more-message">
                            <router-link  to="/person/message"  style="display:block;color:#666;font-size:14px;">查看更多></router-link>
                        </div>
                    </div>
                    <div class="not-message" v-if="!loading && messageList.length===0">
                        暂无~
                    </div>
                 </div>
             </div>
         </div>
     </el-col>
     <!-- 我的应用 -->
     <el-col :span="16">
         <div class="home-top-right">
             <div class="app-title">
                 我的应用
             </div>
             <div class="app-detail">
                <div class="app-detail-d1" @click="goOtherpages(1)">
                    <img :src="homeImg.img_gradeCourse" alt="">
                    <p>我的班课</p>
                </div>
                <div class="app-detail-d1"  @click="goOtherpages(2)">
                    <img :src="homeImg.img_skillscore" alt="">
                    <p>技能评分</p>
                </div>
                <div class="app-detail-d1"  @click="goOtherpages(3)">
                    <img :src="homeImg.img_classroom" alt="">
                    <p>课堂记录</p>
                </div>
                <div class="app-detail-d1"  @click="goOtherpages(4)">
                    <img :src="homeImg.img_career" alt="">
                    <p>职业素质评分</p>
                </div>
                <!-- <div class="app-detail-d1"  @click="goOtherpages(5)">
                    <img :src="homeImg.img_knowledge" alt="">
                    <p>课程知识点进度</p>
                </div> -->
             </div>
             <div style="clear:both;"></div>
         </div>
     </el-col>
    <el-dialog
        :title="currentMessage.title"
        :visible.sync="messageReadDialogVisible"
        width="50%"
        @open="dialogOpend"
        >
        <span>{{currentMessage.messageText}}</span>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="messageReadDialogVisible = false">确 定</el-button>
        </span>
     </el-dialog>
   </el-row>
</template>

<script>
import { mapGetters } from 'vuex'
import { getUserSiteMessage, read } from '@/api/person/siteMessage'
import img_address from '@/assets/images/home/homepage_icon_location.png'
import img_gradeCourse from '@/assets/images/home/homepage_icon_Class.png'
import img_skillscore from '@/assets/images/home/homepage_icon_Skill.png'
import img_classroom from '@/assets/images/home/homepage_icon_record.png'
import img_career from '@/assets/images/home/homepage_icon_occupation.png'
import img_knowledge from '@/assets/images/home/homepage_icon_Speedofprogress.png'
export default {
  data() {
    return {
      // 图标
      homeImg: {
        img_address,
        img_gradeCourse,
        img_skillscore,
        img_classroom,
        img_career,
        img_knowledge
      },
      // 分页和模糊查询
      form: {
        form: {
        },
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        search: '',
        orderBy: 'receivetime'
      },
      messageList: [], // 报告列表
      messageReadDialogVisible: false, // 消息阅读窗口显示状态
      currentMessage: {}, // 报告内容
      loading: true // 加载

    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'name',
      'avatar'
    ])
  },
  created() {
    this.$nextTick(e => {
      this.getList()
    })
  },
  methods: {
    // 获取列表
    getList() {
      this.loading = true
      const params = this.form
      const vm = this
      getUserSiteMessage(params).then(e => {
        const data = e.data.data.records.splice(0, 3).map(e => {
          return {
            id: e.id,
            title: e.title,
            messageText: e.messageText,
            sendTime: e.sendTime.slice(0, 10)
          }
        })
        vm.loading = false
        vm.messageList = data
        console.log(vm.messageList)
      }).catch(() => {
        vm.loading = false
      })
    },
    // 打开弹窗，message 消息
    titleClick(message) {
      this.currentMessage = message
      this.messageReadDialogVisible = true
    },
    // 标记已读
    dialogOpend() {
      if (this.currentMessage.readFlag === false) {
        read(this.currentMessage.id).then(e => {
          this.currentMessage.readFlag = true
          this.$store.commit('SET_NOREADMSGCOUNT', e.data.data)
        })
      }
    },
    // 去其他页面
    goOtherpages(type) {
      if (type === 1) {
        this.$router.push({ path: 'classroomManager/myClassSchedule/index' })
      } else if (type === 2) {
        this.$router.push({ path: 'classroomManager/skillScore' })
      } else if (type === 3) {
        this.$router.push({ path: 'classroomManager/classroom/index' })
      } else if (type === 4) {
        this.$router.push({ path: 'classroomManager/professionalQuality' })
      }
    }
  }
}
</script>

<style scoped>
.home-top-left{
   background-color: #fff;
   min-height: 290px;
   min-width: 370px;
}
.home-top-right{
    background-color: #fff;
    min-height: 290px;
    min-width: 550px;
}
.user-info-t1{
    height: 100px;
}
.user-img-t1{
    margin: 20px 10px 20px 30px;
    width:60px;
    height:60px;
    border-radius: 30px;
    border:1px solid #eee;
    float: left;
    overflow:hidden;
    box-shadow: 0 1px 3px 0 rgba(158,158,158,.2);
}
.user-img-t1 img{
    width:100%;
    height: 100%;
}
.user-info-t2{
    float: left;
    margin: 30px 0px;
    height: 40px;
    line-height: 40px;
}
.user-name{
    float: left;
    font-size: 20px;
    color: #333333;
}
.user-address{
    float: left;
    margin-left:20px;
    font-size: 16px;
    color: #333333;
    padding-top: 3px;
}
.notice-user-message{
    height: 190px;
    padding: 0px 30px 20px 30px;
}
.notice-user-message p{
    margin:0px;
}
.notice-title{
    text-align: left;
    font-size: 16px;
    color:#333333;
}
.notice-content{
    height: 42px;
    line-height: 43px;
    border-bottom:1px dashed #e5e5e5;
}
.notice-content .message-details{
    width: 75%;
    font-size: 14px;
    overflow: hidden;
    text-overflow:ellipsis;
    color: #333333;
    white-space: nowrap;
    float: left;
}
.notice-content .message-time{
    float: right;
    font-size:12px;
    color: #333333;
    width:20%;
    text-align: right;
}
.more-message{
    text-align: right;
    margin-top: 10px;
    font-size: 14px;
    color: #999;
}
.app-title{
    color: #333333;
    font-size: 22px;
    padding: 15px;
}
.app-detail{
    min-height: 230px;
}
.app-detail-d1{
    /* border:1px solid blue; */
    text-align: center;
    margin: 4% 5%;
    float: left;
    flex:1;
    cursor:pointer
}
.app-detail-d1 > img{
    /* border:1px solid red; */
    width:70px;
    height: 70px;
    border-radius: 35px;
}
.app-detail-d1 p{
    font-size: 14px;
    color:#333333;
}
.not-message{
    height: 120px;
    text-align: center;
    line-height: 120px;
    font-size: 14px;
    color: #999;
}
</style>
