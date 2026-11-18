<template>
  <div class="studypathBox">
    <div class="header-container dk-box">
      <path-top :num="num" :finishedCourse="finishedCourse" :times="times"></path-top>
    </div>
    <!-- <div > -->
        <el-row class="body-container">
            <el-col :span="18" class="box dk-box">
              <path-detail  v-on:headCallBack="headCall"></path-detail>
            </el-col>
            <el-col :span="6" class="dk-box">
              <discuss></discuss>
            </el-col>
        </el-row>
     </div>
  <!-- </div>  -->
</template>

<script>
/**
 * @file 学习路径组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/28
 */
import pathTop from './components/pathTop'
import pathDetail from './components/pathDetail'
import discuss from './components/discuss'
export default {
  components: {
    pathTop,
    discuss,
    pathDetail
  },
  data() {
    return {
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        year: '',
        gradeId: undefined,
        search: undefined,
        orderBy: 'creation_time desc'
      },
      num: 0, // 课程数
      finishedCourse: 0,
      times: 0, // 学习时间
      student: null, // 学生信息
      activetab: 'first' // 选项卡初始值
      //   echarts: echarts// Echarts对象

    }
  },
  created() {
    this.headCall()
  },
  methods: {
    headCall(nu) {
      // 回调方法，接收子组件传的参数
      if (nu) {
        this.num = nu.detailLength
        this.finishedCourse = nu.finishedLength
        this.times = nu.detailLength * 10
      } else {
        this.num = 0
        this.finishedCourse = 0
        this.times = 0
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.studypathBox{
  background-color: #E4E7ED;
  padding:10px;
 box-sizing: border-box;
}
.body-container {
  padding-top: 10px;
  padding-left: 10px;
   background-color: #E4E7ED;
}
 .grid{
    border-bottom: 1px solid #e3e3e3;
    font-size: 16px;
    padding-bottom: 10px;
      color: #333;
   }
.box{
  margin-left: -10px;
   margin-right: 10px;
// box-sizing: border-box;
}
.box2{
padding-bottom: 20px;
}
.myimage{
  border-radius: 4px;
height: 40px;
width: 40px;
}
.header-container{
 height: 180px;
  overflow: hidden;
}
.content-new-background .app-main>div {
    background-color: #E4E7ED;
    box-shadow: 0 0 0px 0 rgba(0,0,0,.2);
}
.dk-box{
    background-color: #fff;
    border-radius:4px;
    //  padding-left:10px;
    //  padding-right: 10px;
    box-shadow: 0 0 1px 0 rgba(0,0,0,.2);
}

</style>
