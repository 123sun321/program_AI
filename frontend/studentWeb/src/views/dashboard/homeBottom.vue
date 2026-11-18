<template>
     <el-row :gutter="20" >
     <el-col :span="12" >
         <div class="home-bottom">
            <div class="often-title">
                 本学期班课报告汇报
            </div>
             <div  v-loading="Loading" style="min-height:189px;">
                 <div v-if="gradeCourseReportList.length>0" class="grade-course-list-t1">
                    <div class="course-centent-detail" v-for="(item,index) in gradeCourseReportList" :key="index">
                        <div class="graderCourse-info">
                            <div class="gradeCourseName">
                                    <span>{{item.gradeCourseName}}</span>
                            </div>
                            <div class="reportNumber">
                                    <span>{{item.studentNum}}位学生,共:{{item.reportNum}}份报告</span>
                            </div>
                        </div>
                        <div class="gradeCourse-echarts">
                             <div v-if="item.reportNum>0" :id="'myPie'+index"  class="chart-score" style="height:150px;width:100%;" ></div>
                             <div v-else class="report-null">缺少报告，暂无数据</div>
                        </div>
                        <div class="gradeCourse-avg-score">
                            <div class="score-avg-content">
                                <p class="avg"><span>{{item.skillScore}}</span></p>
                                <p class="avg-text"><span>技能平均分</span></p>
                            </div>
                            <div class="score-avg-content">
                                <p class="avg"><span>{{item.synthesizeScore}}</span></p>
                                <p class="avg-text"><span>综合平均分</span></p>
                            </div>
                        </div>
                    </div>
                 </div>
                 <div class="not-gradeCourse" v-if="!Loading && gradeCourseReportList.length===0">
                        暂无~
                 </div>
             </div>
         </div>
     </el-col>
     <el-col :span="12">
         <div class="home-bottom-right">
             <div class="often-title">
                 常用链接
             </div>
             <div class="often-detail dk-flex" >
                <div class="often-detail-d1" @click="goOtherpages(1)">
                    <img :src="img.img_commune" alt="">
                    <p>五四公社</p>
                </div>
                <div class="often-detail-d1" @click="goOtherpages(2)">
                    <img :src="img.img_task" alt="">
                    <p>任务宝</p>
                </div>
                <div class="often-detail-d1" @click="goOtherpages(3)">
                    <img :src="img.img_chiwise" alt="">
                    <p>创智微商城</p>
                </div>
             </div>
             <div style="clear:both;"></div>
         </div>
     </el-col>
   </el-row>
</template>

<script>
/**
 * @description 底部组件
 * @author huangPengju
 * @since 2025/1/18
 */
// 引入基本模板
const echarts = require('echarts/lib/echarts')
// 引入柱状图组件
require('echarts/lib/chart/pie')// 饼图
// 引入提示框和title组件
require('echarts/lib/component/tooltip') // 鼠标移入提示
require('echarts/lib/component/title') // 标题
require('echarts/lib/component/legendScroll') // 数据代表
import img_commune from '@/assets/images/home/homepage_img_commune.png'
import img_task from '@/assets/images/home/homepage_img_task.png'
import img_chiwise from '@/assets/images/home/homepage_img_chiwise.png'
import { getGradeCourseReportList } from '@/api/home/index'

export default {
  data() {
    return {
      img: {
        img_commune,
        img_task,
        img_chiwise
      },
      Loading: false, // 加载
      gradeCourseReportList: [] // 班课列表
    }
  },
  created() {
  },
  methods: {
    // 去其他页面
    goOtherpages(type) {
      if (type === 1) {
        window.location.href = 'http://www.hi-54.com/'
      } else if (type === 2) {
        window.location.href = 'http://task.hi-54.com'
      } else if (type === 3) {
        window.location.href = 'http://www.hi-54.com/'
      }
    },
    // 班课列表  semesterId当前学期id
    getList(semesterId) {
      const vm = this
      getGradeCourseReportList('1087669555606020097').then(e => {
        vm.gradeCourseReportList = e.data
        vm.$nextTick(n => {
          vm.chart()
        })
        vm.Loading = false
      }).catch(() => {
        vm.Loading = false
      })
    },
    // 调用图形
    chart() {
      for (var i = 0; i < this.gradeCourseReportList.length; i++) {
        if (this.gradeCourseReportList[i].reportNum > 0) {
          this.drawPie(i, this.gradeCourseReportList[i].poorStudentNum, this.gradeCourseReportList[i].passStudentNum, this.gradeCourseReportList[i].excellentStudentNum)
        }
      }
    },
    // 制作饼图
    drawPie(index, poorStudentNum, passStudentNum, excellentStudentNum) {
      // 基于准备好的dom，初始化echarts实例
      const myPie = echarts.init(document.getElementById('myPie' + index))
      // 绘制图表
      myPie.setOption({
        title: {
          text: '',
          subtext: '',
          x: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: ['0-15分', '16分-30分', '30分以上']
        },
        series: [

          {
            name: '',
            type: 'pie',
            radius: ['50%', '70%'],
            center: ['65%', '55%'],
            data: [
              { value: poorStudentNum, name: '0-15分', itemStyle: { color: '#4858f5' }},
              { value: passStudentNum, name: '16分-30分', itemStyle: { color: '#d98064' }},
              { value: excellentStudentNum, name: '30分以上', itemStyle: { color: '#92cBb0' }}
            ],
            color: ['#4858f5', '#d98064', '#92cBb0']
          }
        ]
      })
    }
  }
}
</script>

<style scoped>
.home-bottom{
    background-color: #fff;
    min-height: 240px;
    min-width: 685px;
}
.home-bottom-right{
  background-color: #fff;
  min-width: 400px;
}
.often-title{
    font-size: 20px;
    color:#333333;
    padding: 15px;
}
.often-detail-d1{
    text-align: center;
    margin: 4% 6.5%;
    float: left;
    /* flex:1; */
    cursor:pointer
}
.often-detail-d1 > img{
    /* border:1px solid red; */
    width:70px;
    height: 70px;
    border-radius: 35px;
}
.often-detail-d1 p{
    font-size: 14px;
    color:#333333;
}
.course-centent-detail{
    border-bottom:1px dashed #e5e5e5;
    height: 152px;
    margin-left: 40px;
    min-width: 715px;
}
.home-bottom .grade-course-list-t1 .course-centent-detail:last-child{
    border-bottom:none;
}
.not-gradeCourse{
    height: 160px;
    text-align: center;
    line-height: 160px;
    font-size: 14px;
    color: #999;
}
.graderCourse-info{
    /* border:1px solid red; */
    height: 100%;
    width: 155px;
    float: left;
}
.gradeCourseName{
    font-size: 16px;
    margin-bottom: 5px;
    margin-top: 10px;
    color:#222222;
}
.reportNumber{
    font-size: 14px;
    color:#666666;
}
.gradeCourse-echarts{
    float: left;
    /* border:1px solid red; */
    min-width: 400px;
    height: 100%;
}
.gradeCourse-avg-score{
    /* border:1px solid red; */
    float: right;
    text-align: center;
    height: 100%;
    margin-right: 88px;
}
.gradeCourse-avg-score p{
    margin:0px;
}
.score-avg-content{
    width:70px;
    height: 46px;
    border:1px solid #c2c2c2;
    border-radius: 6px;
}
.score-avg-content:first-child{
    margin-top: 28px;
    margin-bottom: 13px;
}
.avg{
    font-size: 20px;
    color:#ea2721;
}
.avg-text{
    font-size: 10px;
    color:#666666;
}
.report-null{
    text-align: center;
    font-size: 15px;
    color: #ed6a0c;
    margin-top: 72px;
}
</style>
