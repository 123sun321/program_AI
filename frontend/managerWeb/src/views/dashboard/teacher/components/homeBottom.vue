<template>
     <el-row :gutter="20" >
     <el-col :span="24" >
         <div class="home-bottom">
            <div class="often-title">
                 待评分作业
            </div>
             <div  v-loading="Loading" style="min-height:148px;">
                 <div v-if="noScoreList.length>0" class="grade-course-list-t1" @click="goOtherpages(4)">
                    <div class="course-centent-detail" v-for="(item,index) in noScoreList" :key="index">
                        <div class="graderCourse-info">
                            <div class="gradeCourseName">
                                    <span>《{{item.assignment.title}}》{{item.student.name}}({{item.student.code}})</span>
                            </div>
                        </div>
                    </div>
                 </div>
                 <div class="not-gradeCourse" v-if="!Loading && noScoreList.length===0">
                        暂无~
                 </div>
             </div>
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
const echarts = require('echarts/lib/echarts')
require('echarts/lib/chart/pie')
require('echarts/lib/component/tooltip')
require('echarts/lib/component/title')
require('echarts/lib/component/legendScroll')
import { getNoScoreList } from '@/api/assignmentmanage/assgin'

export default {
  data() {
    return {
      Loading: false,
      noScoreList: []
    }
  },
  created() {
    this.$nextTick(e => {
      this.getList()
    })
  },
  methods: {
    goOtherpages(type) {
      if (type === 4) {
        this.$router.push({ path: 'assignmentmanage/submit', query: { score: 0 }})
      }
    },
    getList() {
      const vm = this
      getNoScoreList().then(e => {
        vm.noScoreList = e.data.data
      }).catch(() => {
        vm.Loading = false
      })
    },
    chart() {
      for (var i = 0; i < this.gradeCourseReportList.length; i++) {
        if (this.gradeCourseReportList[i].reportNum > 0) {
          this.drawPie(i, this.gradeCourseReportList[i].poorStudentNum, this.gradeCourseReportList[i].passStudentNum, this.gradeCourseReportList[i].excellentStudentNum)
        }
      }
    },
    drawPie(index, poorStudentNum, passStudentNum, excellentStudentNum) {
      const myPie = echarts.init(document.getElementById('myPie' + index))
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
    min-width: 400px;
     min-height: 290px;
}
.home-bottom-right{
  background-color: #fff;
  min-width: 400px;
  min-height: 290px;
}
.often-title{
    font-size: 20px;
    color:#333333;
    padding: 15px;
}
.course-centent-detail{
    margin-left: 40px;
    min-width: 715px;
}
.grade-course-list-t1 {
  overflow: hidden;
 height: 152px;
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
    height: 100%;
    width: 100%;
    float: left;
}
.gradeCourseName{
    font-size: 15px;
     margin-bottom: 5px;
    color:#222222;
}
.reportNumber{
    font-size: 14px;
    color:#666666;
}
.gradeCourse-echarts{
    float: left;
    min-width: 400px;
    height: 100%;
}
.gradeCourse-avg-score{
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
