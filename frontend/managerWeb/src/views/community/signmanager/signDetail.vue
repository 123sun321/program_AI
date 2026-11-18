<template>
  <div class="reportDetailBox">
    <div class="header-container dk-box">
      <el-row>
      <el-col :span="20">
        <el-form :inline="true"    style="width:70%;" class="demo-form-inline pull-left">
        <el-form-item prop="search" label="签到时间">
            <el-date-picker
            v-model="value4"
            :clearable="false"
            type="daterange"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions2">
          </el-date-picker>
        </el-form-item>
        <el-form-item class="dk-button">
          <el-button type="primary" @click="search()" icon="el-icon-search">查询</el-button>
          <!-- <el-button type="default" @click="reset" icon="el-icon-refresh">重置</el-button> -->
        </el-form-item>
      </el-form>
      </el-col>
      <el-col :span="4" class="dk-txt-right">
        <el-button @click="$router.go(-1)" plain>返回</el-button>
      </el-col>
      </el-row>
    </div>

        <el-row :span="24" class="body-container">
            <el-col :span="12" class="box dk-box">
            <div class="nav">签到班级TOP10</div>
            <div class="noneData" id="main"></div>
            </el-col>
            <el-col :span="12" class="dk-box">
            <div class="nav">签到学生TOP10</div>
              <div class="noneData" id="main2"></div>
            </el-col>
        </el-row>

    <div class="footer-container dk-box">
    <div class="nav">签到统计</div>
      <div class="noneData" id="main3"></div>
    </div>
  </div>
</template>

<script>
/**
 * @file 签到统计组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/20
 */
import echarts from 'echarts'
import { cumul } from '@/api/community/signmanager'
import Vue from 'vue'
Vue.use(echarts)
export default {
  data() {
    return {
      // 签到统计
      everyDay: {
        count: [],
        signTime: []
      },
      // 签到班级
      grade: {
        count: [],
        gradeName: []
      },
      // 签到学生
      student: {
        count: [],
        studentName: []
      },
      value4: [new Date().getTime() - 3600 * 1000 * 24 * 7, new Date()], // 默认近一周
      pickerOptions2: { // 时间选择限制
        disabledDate(time) {
          return time.getTime() >= Date.now() - 8.64e6
        },
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      }
    }
  },
  created() {
    this.list()
  },
  methods: {
    // 取数据
    list() {
      this.loading = true
      var star = this.dateFormat(this.value4[0])
      var end = this.dateFormat(this.value4[1])
      cumul(star, end).then(e => {
        var data = e.data.data
        for (let i = 0; i < data.everyDay.length; i++) {
          this.everyDay.signTime.push(data.everyDay[i].signTime.substring(0, 10))
        }
        this.everyDay.count = data.everyDay.map(b => b.count)
        this.grade.count = data.grade.map(b => b.count)
        this.grade.gradeName = data.grade.map(b => b.gradeName)
        this.student.count = data.student.map(b => b.count)
        this.student.studentName = data.student.map(b => b.studentName)
        this.init()
        this.loading = false
      })
    },
    // 初始数据
    init() {
      var myChart = echarts.init(document.getElementById('main'))
      var myChart2 = echarts.init(document.getElementById('main2'))
      var myChart3 = echarts.init(document.getElementById('main3'))
      var option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        color: ['#3398DB'],
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: this.grade.gradeName
        },
        series: [
          {
            name: '签到人数',
            type: 'bar',
            label: {
              normal: {
                show: true,
                position: 'insideRight'
              }
            },
            data: this.grade.count
          }
        ]
      }
      var option2 = {

        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },

        color: ['#3398DB'],

        xAxis: {
          type: 'value'

        },
        yAxis: {
          type: 'category',
          data: this.student.studentName
        },
        series: [
          {
            name: '签到次数',
            type: 'bar',
            label: {
              normal: {
                show: true,
                position: 'insideRight'
              }
            },
            data: this.student.count
          }
        ]
      }
      var option3 = {

        color: ['#3398DB'],
        tooltip: {
          trigger: 'axis'
        },

        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.everyDay.signTime
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '签到人数',
            type: 'line',
            stack: '总量',

            data: this.everyDay.count
          }
        ]
      }
      myChart.setOption(option)
      myChart2.setOption(option2)
      myChart3.setOption(option3)
    },
    dateFormat(time) {
      var date = new Date(time)
      var year = date.getFullYear()
      /* 在日期格式中，月份是从0开始的，因此要加1
     * 使用三元表达式在小于10的前面加0，以达到格式统一  如 09:11:05
     * */
      var month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
      var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
      // 拼接
      return year + '-' + month + '-' + day
    },
    // 搜索
    search() {
      this.everyDay.signTime = []
      this.list()
    }
  }
}
</script>
<style lang="scss" scoped>
.body-container {
  padding-top: 10px;
  padding-left: 10px;
   background-color: #E4E7ED;
}
.nav{
    padding-bottom: 10px;
    border-bottom: 1px solid #CCCCCC;
}
.box{
  margin-left: -10px;
   margin-right: 10px;
// box-sizing: border-box;
}
.footer-container{
    margin-top: 10px;
}
.header-container{
  overflow: hidden;
}
.content-new-background .app-main>div {
    background-color: #E4E7ED;
    box-shadow: 0 0 0px 0 rgba(0,0,0,.2);
}
.noneData{
  height: 500px;
  text-align: center;
  width: 100%;
}
div.dk-box{
    background-color: #fff;
    // box-shadow: 0 0 1px 0 rgba(0,0,0,.2);
}
</style>
