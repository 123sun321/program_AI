<template>
    <div class="course_main">
      <el-row :gutter="15">
          <el-col :span="24">
            <div class = 'course_left'>
              <div class= 'course_ico'>
                      <el-row :gutter="20">
                        <el-col :span="5"><div class="grid-content bg-purple"> <div class = 'course_title'><span>标题：</span>{{coursedata.title}}</div></div></el-col>
                   <el-col :span="4"><div class="grid-content bg-purple"> <div class = 'course_title'> <span class="nav-number" >未提交人数&nbsp;&nbsp;{{listMistakeSum.number-listMistakeSum.submtnumber}}&nbsp;&nbsp;</span></div></div></el-col>
                    <el-col :span="5"><div class="grid-content bg-purple"> <div class = 'course_title'> <span class="nav-number" >提交人数&nbsp;&nbsp;{{listMistakeSum.submtnumber}}&nbsp;&nbsp;</span></div></div></el-col>
                    <el-col :span="5"><div class="grid-content bg-purple"> <div class = 'course_title'> <span class="nav-number" >最高分&nbsp;&nbsp;{{listMistakeSum.maxnumber}}&nbsp;&nbsp;</span></div></div></el-col>
                     <el-col :span="5"><div class="grid-content bg-purple"> <div class = 'course_title'><span class="nav-number" >最低分&nbsp;&nbsp;{{listMistakeSum.minnumber}}</span></div></div></el-col>
                      </el-row>
              </div>
              <div class = 'course_content'>
              </div>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="course_right">
              <div class="btn">

                  <el-button @click="callback">返回</el-button>
              </div>
              <el-tabs v-model="activeName">
                <el-tab-pane label="提交详情" name="first">
                    <submit :assignmentId="assignmentId" @success="headcount"></submit>
                </el-tab-pane>
                <el-tab-pane label="作业资源" name="second">
                    <assignment-resource :assignmentId="assignmentId"></assignment-resource>
                </el-tab-pane>
                <el-tab-pane label="作业详情" name="third">
                    <assignment-summary :coursedata="coursedata"></assignment-summary>
                </el-tab-pane>
            </el-tabs>
            </div>
          </el-col>
      </el-row>
    </div>
</template>
<script>
 /**
 * @file 课程参与组件
 * @author  张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/3/22
 */
 import submit from './submit'
 import assignmentResource from './assignmentResource'
 import assignmentSummary from './assignmentSummary'
// import courseDiscuss from './components/coursediscuss'
import { fetchOne, getAssignmentSubmit } from '@/api/assignmentmanage/assgin'

export default {
   data() {
     return {
       assignmentId: this.$route.query.assignmentId,
       courseID: this.$route.query.courseid,
       gradeID: this.$route.query.gradeid,
       groupID: this.$route.query.groupid,
       activeName: 'first',
       coursedata: {},
       listMistakeSum: []

     }
   },
   components: {
     submit,
     assignmentResource,
     assignmentSummary
     //  courseAssignment
   },
   created() {
     this.$nextTick(e => {
       this.init()
       this.headcount()
     })
   },
   methods: {
     callback() {
       //  this.$router.push({ name: 'assign' })
       this.$router.go(-1)
     },
     init() {
       fetchOne(this.assignmentId).then(e => {
         const data = e.data.data
         this.coursedata = data
       })
     },
     // 提交数量
     headcount() {
       getAssignmentSubmit(this.assignmentId, this.courseID, this.gradeID, this.groupID).then(response => {
         this.loading = false
         const data = response.data.data
         this.listMistakeSum = data
       })
       this.init()
     }

   }
}

</script>

<style lang="scss" scoped>
.course_main{
  position: relative;
  background-color:transparent!important;
  box-shadow:none!important;
  .course_left,.course_right{
    background-color: #ffffff;
    -webkit-box-shadow: 0 0 1px 0 rgba(0,0,0,.2);
    box-shadow: 0 0 1px 0 rgba(0,0,0,.2);
    padding: 15px;
  }
  .course_left{
    .course_logo{
      text-align: center;
      img{width: 100%;height: 100%; border: 1px solid #e4e7ed;}
    }
  }
  .course_right{
    .btn{
      text-align: right;
      .el-button{
        position: relative;
        z-index: 1001;
        margin-bottom: -30px;
      }
    }
  }
}

.course_ico {
  text-align:center;
  line-height: 20px;
  margin: 20px 0;
}
.course_content {
  font-size:14px;
  margin-left:20px;
  span{font-weight: bolder;}
  .course_title {
    margin-bottom:20px;
  }
}

</style>

