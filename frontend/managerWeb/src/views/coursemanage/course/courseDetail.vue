<template>
    <div class="course_main">
      <el-row :gutter="15">
          <el-col :span="6">
            <div class = 'course_left'>
              <div class="course_logo">
                <img :src="coursedata.logo" alt="" >
              </div>
              <div class= 'course_ico'>
                  <i title="参与人数"><svg-icon  icon-class="peoples" /></i>
                  <span>{{coursedata.joinNumber}}</span>
                  <i class = 'el-icon-document' style="margin-left:20px;" title="课程实验数"></i>
                  <span>{{coursedata.exerciseNumber}}</span>
              </div>
              <div class = 'course_content'>
                 <div class = 'course_title'><span>创建时间：</span>{{coursedata.creationTime}}</div>
                <div class = 'course_title'><span>标题：</span>{{coursedata.name}}</div>
                <div class = 'course_info'><span>简介：</span>{{coursedata.summary}}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="18">
            <div class="course_right">
              <div class="btn">
                  <el-button @click="callback">返回</el-button>
              </div>
              <el-tabs v-model="activeName">
                <el-tab-pane label="课程实验" name="first">
                    <course-experiment :courseid='courseid'  :type="type"></course-experiment>
                </el-tab-pane>
                <el-tab-pane label="参与详情" name="second">
                    <study-detail :courseid="courseid"></study-detail>
                </el-tab-pane>
                <el-tab-pane label="评价" name="third">
                  <course-discuss :courseid="courseid"></course-discuss>
                </el-tab-pane>
                 <el-tab-pane label="课程作业" name="four">
                  <course-assignment :courseid="courseid"></course-assignment>
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
import studyDetail from './components/studyDetail'
import courseAssignment from './courseAssignment'
import courseExperiment from './components/courseExperiment'
import courseDiscuss from './components/coursediscuss'
import { fetchOne } from '@/api/coursemanage/course'

export default {
   data() {
     return {
       courseid: this.$route.query.courseid,
       type: this.$route.query.type,
       activeName: 'first',
       coursedata: {}
     }
   },
   components: {
     studyDetail,
     courseExperiment,
     courseDiscuss,
     courseAssignment
   },
   created() {
     this.init()
   },
   methods: {
     callback() {
       this.$router.push({ name: 'courseindex' })
     },
     init() {
       fetchOne(this.courseid).then(e => {
         const data = e.data.data
         this.coursedata = data
       })
     }

   }
}

</script>

<style scoped lang="scss">
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
.course_info{
  word-wrap : break-word ;
}

</style>

