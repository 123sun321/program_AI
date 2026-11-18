<template>
    <el-row :gutter="20" style="margin-top:15px;margin-bottom: 15px;">
     <el-col :span="6">
         <div class="home-center" @click="goOtherpages(1)">
              <div class="center-img-icon-c1">
                  <img :src="img.img_upcoming" alt="">
              </div>
              <div class="center-info-notice">
                  <p class="info-num-c1"><span>{{dashboardCount.newCourseCount}}</span><span>条</span></p>
                  <p class="info-num-cc1">新增课程</p>
              </div>  
         </div>
     </el-col> 
     <el-col :span="6">
         <div class="home-center" @click="goOtherpages(2)">
             <div class="center-img-icon-c2">
                 <img :src="img.img_summary" alt="">
             </div>
             <div class="center-info-notice">
                  <p class="info-num-c2"><span>{{dashboardCount.noScoreCount}}</span><span>个</span></p>
                  <p class="info-num-cc2">待评分作业</p>
             </div>  
         </div>
     </el-col>
     <el-col :span="6">
         <div class="home-center" @click="goOtherpages(3)">
             <div class="center-img-icon-c3">
                <img :src="img.img_skillScore" alt="">
             </div>
             <div class="center-info-notice">
                  <p class="info-num-c3"><img style="width:35px;margin-top:5px;" :src="img.img_file" /></p>
                  <p class="info-num-cc3" style="margin-top:0px;">我的资源</p>
             </div>  
         </div>
     </el-col>
     <el-col :span="6">
         <div class="home-center" @click="goOtherpages(4)">
             <div class="center-img-icon-c4">
                 <img :src="img.img_knowledge" alt="">
             </div>
             <div class="center-info-notice">
                  <p class="info-num-c4"><span>{{dashboardCount.themeCount}}</span><span>个</span></p>
                  <p class="info-num-cc4">累计发帖</p>
             </div>  
         </div>
     </el-col>
   </el-row>
</template>

<script>
import img_file from '@/assets/images/files/file_1.png'
import img_upcoming from '@/assets/images/home/homepage_icon_todolist.png'
import img_summary from '@/assets/images/home/homepage_icon_summary.png'
import img_skillScore from '@/assets/images/home/homepage_icon_score.png'
import img_knowledge from '@/assets/images/home/homepage_icon_setuo.png'
import { getDashboardCount } from '@/api/base/teacher'
export default {
  data() {
    return {
      img: {
        img_upcoming,
        img_summary,
        img_skillScore,
        img_knowledge,
        img_file
      },
      dashboardCount: {},
      newHomeWork: '', // 新总结数量
      nowSkillNum: '', // 当前周未评分技能数量
      toBeSetKnowledgeNum: ''// 知识点进度待设置数量
    }
  },
  created() {
    this.$nextTick(e => {
      this.getNow()
    })
  },
  methods: {
    getNow() {
      getDashboardCount().then(e => {
        this.dashboardCount = e.data.data
      })
    },
    // 去其他页面
    goOtherpages(type) {
      if (type === 1) {
        this.$router.push({ name: 'coursemanage' })
      } else if (type === 2) {
        this.$router.push({ path: 'assignmentmanage/submit', query: { score: 0 }})
      } else if (type === 3) {
        this.$router.push({ path: 'myfileresource/fileResource' })
      } else if (type === 4) {
        this.$router.push({ path: 'teachercommunity/index' })
      }
    }
  }
}
</script>

<style scoped>
.home-center{
   background-color: #fff;
   height: 100px;
   min-width: 210px;
}
.center-img-icon-c1,.center-img-icon-c2,.center-img-icon-c3,.center-img-icon-c4{
    height: 100%;
    width:35%;
    float: left;
    text-align: center;
}
.center-img-icon-c1{
    background: #7D7DDF;
}
.center-img-icon-c2{
    background: #E3C141;
}
.center-img-icon-c3{
    background: #1FB2AA;
}
.center-img-icon-c4{
    background: #1ABEE1;
}
.center-info-notice{
    /* border:1px solid blue; */
    height: 100%;
      width: 64%;
    float: left;
    text-align: center;
}
.home-center img{
        margin-top: 27px;
}
.info-num-c1,.info-num-c2,.info-num-c3,.info-num-c4{
      margin: 20px 0px 2px 0px;
}
.info-num-c1 span:first-child,.info-num-c2 span:first-child,.info-num-c3 span:first-child,.info-num-c4 span:first-child{
    font-size: 30px;
}
.info-num-c1 span:last-child,.info-num-c2 span:last-child,.info-num-c3 span:last-child,.info-num-c4 span:last-child{
    font-size: 20px;
}
.info-num-c1{
    color:#7D7DDF;
}
.info-num-c2{
    color:#E3C141;
}
.info-num-c3{
    color:#1FB2AA;
}
.info-num-c4{
    color:#1ABEE1;
}
.info-num-cc1,.info-num-cc2,.info-num-cc3,.info-num-cc4{
       margin: 0px;
       font-size: 16px;
       color:#333333;
}
</style>
