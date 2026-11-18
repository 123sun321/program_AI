<template>
  <div class="record">
      <div class="top clearfix">
          <div class="title">他的学习记录</div>
            <div class="r_number pull-right">
              <span class="number">当前连续学习 <span>{{statisticNumber.continuousDay}}</span> 天</span>
              <span class="number">最大连续学习 <span>{{statisticNumber.maxDay}}</span> 天</span>
              <span class="number">总学习天数 <span>{{statisticNumber.sumDay}}</span> 天</span>
            </div>
      </div>
      <div class="chart">
          <calendar-heatmap tooltip-unit="min" :range-color="rangeColor" style="max-height:167px;width:100%;" :endDate="nowDate" :values="studytime"></calendar-heatmap>
      </div>
  </div>
</template>

<script>
import { statisticnumber, exerciserecordstatistics } from '@/api/mycourse/exercisejoinrecord'
import { CalendarHeatmap } from 'vue-calendar-heatmap'
export default {
  data() {
    return {
      userId: this.$route.params.id,
      studytime: [],
      rangeColor: ['#eeeeee', '#b5e2d7', '#a1decf', '#7bd7c0', '#08bf91'],
      statisticNumber: {}
    }
  },
  components: {
    CalendarHeatmap
  },
  created() {
    this.init()
  },
  computed: {
    nowDate: function() {
      var myDate = new Date()// 获取系统当前时间
      return myDate.getFullYear() + '-' + (myDate.getMonth() + 1) + '-' + myDate.getDate()
    }
  },
  methods: {
    init() {
      statisticnumber(this.userId).then(e => {
        this.statisticNumber = e.data.data
      })
      exerciserecordstatistics(this.userId).then(e => {
        const data = e.data.data
        data.forEach(element => {
          this.studytime.push({ date: element.time, count: element.studyTime })
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.record{
    background-color: #ffffff;
    .top{
        padding: 10px;
        .title{
            font-size: 18px;
            color: #333333;
            float: left;
        }
        .number{
            margin-left: 20px;
            font-size: 16px;
            color: #666666;
            span{
                font-size: 20px;
                color: #459d92;
            }
        }
    }
    .chart{
        padding: 10px;
    }
}
</style>
