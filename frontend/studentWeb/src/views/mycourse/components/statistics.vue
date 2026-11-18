<template>
    <div class="time_statistics">
        <div class="statistics_number">
            <div class="title">累计实验次数</div>
            <img :src="require('@/assets/student/Mycourse_Laboratory.png')">
            <div class="bottom">
                <span class="number">{{statisticNumber.exerciseCount}}</span><br />
                <span class="dec">FREQUENCY</span>
            </div>
        </div>
        <div class="statistics_number">
            <div class="title">有效学习时间</div>
            <img :src="require('@/assets/student/Mycourse_time.png')">
            <div class="bottom">
                <span class="number">{{statisticNumber.exerciseTime}}</span><br />
                <span class="dec">MINUTE</span>
            </div>
        </div>
        <div class="record">
            <div class="top clearfix">
                <div class="title">学习记录</div>
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
    </div>
</template>

<script>
import { statisticnumber, exerciserecordstatistics } from '@/api/mycourse/exercisejoinrecord'
import { CalendarHeatmap } from 'vue-calendar-heatmap'
export default {
  data() {
    return {
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
      statisticnumber(null).then(e => {
        this.statisticNumber = e.data.data
      })
      exerciserecordstatistics(null).then(e => {
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
    .time_statistics{
        padding: 20px;
        display: flex;
        flex-flow: row wrap;
        .statistics_number{
            background-color: #ffffff;
            width: 170px;
            height: 230px;
            margin-right: 20px;
            border-radius: 5px;
            padding: 15px;
            text-align: center;
            .title{
                font-size: 14px;
                color: #666666;              
            }
            img{
                margin: 15px 0;
            }
            .bottom{
                .number{
                    font-size: 30px;
                    color: #459d92;
                }
                .dec{
                    font-size: 10px;
                    color: #666666;
                }
            }
        }
        .record{
            flex: 1;
            background-color: #ffffff;
            border-radius: 5px;
            height: 230px;
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
    }
</style>
