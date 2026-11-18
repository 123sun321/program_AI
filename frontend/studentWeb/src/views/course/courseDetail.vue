<template>
  <div class="courseDetailWrapper clearfix">
    <!--头部条目 -->
    <div class="courseDetailHeader">
      <!--头部条目,图片-->
      <div class="courseDetailPhoto">
        <img :src="courseData.logo" style="width:379px;height:237px;">
      </div>
      <!--头部条目,右侧-->
      <div class="headerLeft">
        <div class="courseTitle">
          {{ courseData.name }}
        </div>
        <div class="courseStar" v-if="isCollected" @click="changeCollect">
          <i :class="isCollected === '1' ? 'el-icon-star-on' : 'el-icon-star-off'"
            :style="isCollected === '1' ? 'color: #eca857' : ''"
          ></i>
        </div>
        <br>
        <div class="courseInfo">
          <span>{{ courseData.joinNumber }}</span>人学过&nbsp;&nbsp;&nbsp;&nbsp;
          <span>{{ courseData.collectedNumber }}</span>人收藏
        </div>
        <div class="courseTeacher">课程教师:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{ courseData.author }}</div>
        <div class="courseBrief">
          {{ courseData.summary }}
        </div>
        <div class="btnBig">
          <el-button type="success" @click="beginToLearn">开始学习</el-button>
        </div>
      </div>
    </div>

    <!--主框-->
    <div class="courseDetailSection">

      <div class="courseTopicTitle">课程章节</div>
      <div v-if="exercises.length !== 0">
        <div class="courseItem" v-for="(exercise, index) in exercises" :key="index">
          <div class="courseItemTitle">
            {{index + 1}}.&nbsp;&nbsp;&nbsp;&nbsp;{{ exercise.exercise.name }}
            <div class="courseItemBtn">
              <el-button :style="currentStudyCourseIndex === index ? 'visibility: visible': ''" :class="exercise.studentId ? 'studyedButton' : 'studyingButton'" @click="pathToCaselibraryRunning(exercise.exercise.id,exercise.studentId)">{{exercise.studentId ? '继续学习' : '开始学习'}}</el-button>
            </div>
          </div>
          <div class="courseItemBrief">
            知识点: {{ exercise.exercise.knowledge }}
          </div>

        </div>
      </div>
      <div v-if="exercises.length === 0" class="noCourseBackground">
        <img src="../../assets/student/Privatecourses_Illustration.png" alt="">
        <span>该课程下还没有章节信息哦~</span>
      </div>

    </div>
    <div class="clearfix rightSection">
      <div class="courseDetailAsideOfSimilar">
        <div  class="asideTitle">相似课程</div>
        <hr style="border-color: #eee;border-width: 0.5px;"/>
        <div v-if="recommendDataList.length !== 0">
          <ul style="padding-left: 30px;">
            <li v-for="(recommendData, index) in recommendDataList" :key="index"
            @click="pathToDetail(recommendData.id, recommendData.courseId)">{{ recommendData.name }}</li>
          </ul>
        </div>
        <div v-if="recommendDataList.length === 0" style="text-align: center;color: #999;">
          <img style="display:block; margin: 0 auto;" src="../../assets/images/emptyFolder.png" alt="">
          <span>该课程暂无推荐课程</span>
        </div>
      </div>
      <div class="discuss">
        <Discuss :courseId="courseId" v-if="showDiscuss"/>
      </div>
    </div>
  </div>
</template>

<script>
/**
 * @file 课程详情
 * @author 宋乃乐
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/19
 */
import Discuss from './discuss'
import {
  getCourseDetail,
  getCourseRecommend,
  getExercisesByCourseId,
  judgeCourseIsCollected
} from '../../api/course/courseDetail'
import {
  addCollect,
  delCollect
} from '../../api/course/index'
export default {
  name: 'App',
  components: {
    Discuss
  },
  created() {
    this.$nextTick(e => {
      this.bindData()
    })
  },
  data() {
    return {
      courseId: this.$route.query.courseId, // 父目录id
      isCollected: '0',
      courseData: {},
      recommendDataList: [],
      showDiscuss: true,
      exercises: []
    }
  },
  methods: {
    bindData() {
      console.log('this.isCollected : ' + this.isCollected)
      if (this.courseId) {
        getCourseDetail(this.courseId).then(data => {
          this.courseData = data.data.data
          // 强制重新渲染头部的信息
          // this.$forceUpdate()
        })
        getCourseRecommend(this.courseId).then(data => {
          this.recommendDataList = data.data.data
          // this.isCollected = this.$route.query.isCollected
        })
        getExercisesByCourseId(this.courseId).then(data => {
          this.exercises = data.data.data
        })
        // 查询收藏字段
        judgeCourseIsCollected(this.courseId).then(data => {
          this.isCollected = data.data.data === true ? '1' : '0'
        })
      }
    },
    // 点击进入课程详情
    pathToDetail(id, isCollected) {
      this.$router.push({ name: 'courseDetail', query: { courseId: id, isCollected }})
      this.courseId = id
      // 这里是强制评论模块重新渲染
      this.showDiscuss = false
      this.bindData()
      this.$nextTick(() => {
        this.showDiscuss = true
      })
    },
    // 点击收藏
    changeCollect() {
      const isCollected = this.isCollected
      if (isCollected === '1') {
        this.cancalCollectCourse(this.courseId)
      } else {
        this.collectCourse(this.courseId)
      }
    },
    // 收藏课程
    collectCourse(id) {
      addCollect(id).then(data => {
        this.$sucmsg('收藏成功')
        this.isCollected = '1'
      }, () => {
        // this.$message.error('啊哦 服务器君正在开小差!')
      })
    },
    // 取消收藏课程
    cancalCollectCourse(id) {
      delCollect(id).then(data => {
        this.$sucmsg('取消收藏成功')
        this.isCollected = '0'
        // this.$route.query.isCollected = '0'
      }, () => {
        // this.$message.error('啊哦 服务器君正在开小差!')
      })
    },
    // 点击开始学习
    beginToLearn() {
      this.$router.push({ name: 'running', query: { exerciseid: this.currentStudyCourseId }})
    },
    // 点击
    pathToCaselibraryRunning(exerciseid, studentId) {
      console.log(studentId)
      const type = 'startagain'
      if (studentId) {
        this.$router.push({ name: 'running', query: { exerciseid, type }})
      } else {
        this.$router.push({ name: 'running', query: { exerciseid }})
      }
    }
  },
  computed: {
    currentStudyCourseId() {
      // 找到第一个 studentId 为空的元素对应的 exercise.id
      for (let i = 0; i < this.exercises.length; i++) {
        const obj = this.exercises[i].studentId
        if (typeof obj === 'undefined' || obj === null || obj === '') {
          return this.exercises[i].exercise.id
        }
      }
      return this.exercises[this.exercises.length - 1].exercise.id
    },
    currentStudyCourseIndex() {
      for (let i = 0; i < this.exercises.length; i++) {
        const obj = this.exercises[i].studentId
        if (typeof obj === 'undefined' || obj === null || obj === '') {
          return i
        }
      }
      return this.exercises.length - 1
    }
  }
}
</script>

<style scoped>
  .clearfix {
    *zoom: 1;
  }
  .clearfix:after {
    content: " ";
    display: block;
    clear: both;
    visibility: hidden;
  }
  .discuss {
    width: 100%;
    background: #fff;
    float: left;
    margin-top: 10px;
    padding: 10px;
    border-radius: 5px;
  }
  ul li{
    margin-top: 28px;
    font-family: "PingFang-SC-Regular";
    font-size: 16px;
    color: #666666;
    list-style: disc;
    cursor: pointer;
  }
  a{
    text-decoration:none;
    color: #169bd5;
  }
  .teacherCourseAll{
    text-align: right;
    padding-right: 16px;
    padding-top: 28px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    text-overflow: -o-ellipsis-lastline;
    position: absolute;
    top: 116px;
    right: 0;
  }
  .teacherBrief{
    magin:0 6px;
    margin-top: 10px;
    font-family: "PingFang-SC-Regular";
    font-size: 16px;
    color: #666666;
    padding-left: 110px;
  }
  .teacherName{
    magin-left: 6px;
    font-family: "PingFang-SC-Regular";
    font-size: 18px;
    color: #333333;
    padding-left: 110px;
  }
  .asidePicture img{
    padding-bottom: 30px;
    margin-left: 20px;
    width: 80px;
    height: 80px;
    display: block;
    background: url('../../assets/images/default.jpeg') no-repeat;
    background-position: 50% 50%;
    background-size: contain;
  }
  .asidePicture{
    width: 100px;
    height: 100px;
    float: left;
  }
  .asideTitle{
    margin: 14px 0px 14px 16px;
    font-family: "PingFang-SC-Regular";
    font-size: 22px;
    color: #333333;
  }
  .courseItemPrompt{
    font-family: "PingFang-SC-Medium";
    font-size: 14px;
    color: #1fad4e;
    text-align: right;
  }
  .studyingButton{
    /*position: absolute;*/
    font-family: "PingFang-SC-Medium";
    font-size: 14px;
    color: #ffffff;
    background-color: #1fad4e;
    vertical-align: bottom;
    margin-right: -10px;
    visibility: hidden;
  }
  .studyedButton {
    font-family: "PingFang-SC-Medium";
    font-size: 14px;
    color: #ffffff;
    background-color: #666;
    vertical-align: bottom;
    margin-right: -10px;
    /* visibility: hidden; */
  }
  .courseItemBtn{
    float: right;
    vertical-align: bottom;
    margin-right: 20px;
    margin-top: 30px;
    display: inline-block;
  }
  .courseItemBrief{
    font-family: "PingFang-SC-Medium";
    font-size: 16px;
    color: #999999;
    width: auto;
    /*float: left;*/
  }
  .courseItemTitle{
    font-family: "PingFang-SC-Medium";
    font-size: 22px;
    color: #1c1f21;
    /*padding-bottom: 40px;*/
    height: 66px;
    /*float: left;*/
  }
  .courseItem{
    height: 110px;
    /* width: 1340px; */
    background-color: #EEEEEE;
    padding: 14px;
    margin-top: 20px;
    position: relative;
  }
  .courseItem:hover .studyedButton{
    visibility: visible;
  }
  .courseItem:hover .studyingButton{
    visibility: visible;
  }
  .courseTopicTitle{
    font-family: "PingFang-SC-Medium";
    font-size: 24px;
    color: #333333;
  }
  .courseStar{
    float: right;
    font-size: 27px;
    margin-right: 35px;
    cursor: pointer;
  }
  .btnBig button{
    /*color: #1fad4e;*/
    border-radius: 6px;
    width: 140px;
    height: 40px;
    font-family: "PingFang-SC-Medium";
    font-size: 18px;
    color: #ffffff;
    background-color: #1fad4e;
  }
  .btnBig{
    margin-top:30px ;
    position: absolute;
    bottom: 24px;
    left: 424px;
  }
  .courseBrief{
    font-family: "PingFang-SC-Regular";
    font-size: 18px;
    color: #666666;
    margin-top: 10px;
    max-height: 60px;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 3;
    overflow: hidden;
    text-overflow: ellipsis;
    text-overflow: -o-ellipsis-lastline;
  }
  .courseInfo{
    font-family: "PingFang-SC-Medium";
    font-size: 20px;
    color: #666666;
    padding-top: 20px;
  }
  .courseInfo span{
    font-family: "PingFang-SC-Regular";
    font-size: 22px;
    color: #1fad4e;
  }
  .courseTitle{
    font-family: "PingFang-SC-Medium";
    font-size: 26px;
    color: #1c1f21;
    float: left;
  }
  .courseDetailAsideOfSimilar{
    margin-top: 20px;
    width: 100%;
    background-color:white;
    float: left;
    padding-left: 16px;
    padding-bottom: 12px;
    border-radius: 5px;
  }
  .courseTeacher {
    font-family: "PingFang-SC-Medium";
    font-size: 18px;
    color: #1c1f21;
    margin-top: 10px;
  }
  /* .courseDetailAsideOfTeacher{
    float: left;
    margin-top: 20px;
    margin-left: 1.8%;
    width: 25%;
    height: 168px;
    background-color:white;
    border-radius: 5px;
    position: relative;
  } */
  .courseDetailPhoto{
    float: left;
    width: 379px;
    height: 237px;
    margin: 0px 28px 30px 14px;
  }
  .rightSection {
    width: 25%;
    float: right;
  }
  .courseDetailSection{
    margin-top: 20px;
    margin-left: -420px;
    float: left;
    width: 73%;
    background-color:white;
    padding: 18px 14px 0 25px;
    border-radius: 5px;
    padding-bottom: 20px;
  }
  .courseDetailHeader{
    padding-top: 26px;
    height: 285px;
    width: 100%;
    background-color:white;
    border-radius: 5px;
    position: relative;
  }
  .courseDetailWrapper{
    width: 100%;
    padding: 10px;
    /* height: 1265px; */
    margin: 0 auto;
  }
.authorInfo p {
  margin: 0;
  height: 40px;
  color: #fff;
  background: #666;
  line-height: 40px;
  font-size: 18px;
  padding-left: 18px;
}
.footer i {
  margin: 20px 0 0 24px;
  font-size: 24px;
  cursor: pointer;
}
/* .footer i:hover{
  cursor: pointer;
} */
.noCourseBackground {
  text-align: center;
  color: #999;
}
.noCourseBackground img {
  margin: 0 auto;
  display: block;
}
</style>
