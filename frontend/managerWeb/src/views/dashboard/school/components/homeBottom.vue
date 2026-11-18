<template>
     <el-row :gutter="20" >
     <el-col :span="24">
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

export default {
  data() {
    return {
      Loading: false,
      noScoreList: []
    }
  },
  created() {
    this.$nextTick(e => {
    })
  },
  methods: {
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
</style>
