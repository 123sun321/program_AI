<template>
    <el-row :gutter="20" style="margin-top:15px;margin-bottom: 15px;">
     <el-col :span="6">
         <div class="home-center" @click="goOtherpages(1)">
              <div class="center-img-icon-c1">
                  <img :src="img.img_upcoming" alt="">
              </div>
              <div class="center-info-notice">
                  <p class="info-num-c1"><span>{{this.$store.state.user.noScheduleCount?this.$store.state.user.noScheduleCount:0}}</span><span>条</span></p>
                  <p class="info-num-cc1">待办事项</p>
              </div>  
         </div>
     </el-col> 
     <el-col :span="6">
         <div class="home-center" @click="goOtherpages(2)">
             <div class="center-img-icon-c2">
                 <img :src="img.img_summary" alt="">
             </div>
             <div class="center-info-notice">
                  <p class="info-num-c2"><span>{{newSummary}}</span><span>个</span></p>
                  <p class="info-num-cc2">新的学生总结</p>
             </div>  
         </div>
     </el-col>
     <el-col :span="6">
         <div class="home-center" @click="goOtherpages(3)">
             <div class="center-img-icon-c3">
                <img :src="img.img_skillScore" alt="">
             </div>
             <div class="center-info-notice">
                  <p class="info-num-c3"><span>{{nowSkillNum}}</span><span>个</span></p>
                  <p class="info-num-cc3">本周技能得分未评</p>
             </div>  
         </div>
     </el-col>
     <el-col :span="6">
         <div class="home-center">
             <div class="center-img-icon-c4">
                 <img :src="img.img_knowledge" alt="">
             </div>
             <div class="center-info-notice">
                  <p class="info-num-c4"><span>{{toBeSetKnowledgeNum}}</span><span>个</span></p>
                  <p class="info-num-cc4">知识点进度待设置</p>
             </div>  
         </div>
     </el-col>
   </el-row>
</template>

<script>
import img_upcoming from '@/assets/images/home/homepage_icon_todolist.png'
import img_summary from '@/assets/images/home/homepage_icon_summary.png'
import img_skillScore from '@/assets/images/home/homepage_icon_score.png'
import img_knowledge from '@/assets/images/home/homepage_icon_setuo.png'

export default {
  data() {
    return {
      img: {
        img_upcoming,
        img_summary,
        img_skillScore,
        img_knowledge
      },
      newSummary: '', // 新总结数量
      nowSkillNum: '', // 当前周未评分技能数量
      toBeSetKnowledgeNum: ''// 知识点进度待设置数量
    }
  },
  created() {
  },
  methods: {
    // 去其他页面
    goOtherpages(type) {
      if (type === 2) {
        this.$router.push({ path: 'classroomManager/skillScore' })
      } else if (type === 3) {
        this.$router.push({ path: 'classroomManager/skillScore' })
      } else if (type === 4) {
        this.$router.push({ path: 'classroomManager/myClassSchedule/index' })
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
