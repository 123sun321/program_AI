<template>
  <div :class="className" :id="id" :style="{height:height,width:width}"></div>
</template>

<script>
import echarts from 'echarts'
import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    source: { // Echart数据源
      type: Object,
      default: {}
    },
    id: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '200px'
    },
    height: {
      type: String,
      default: '200px'
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.initChart()
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  watch: {
    'source': function() {
      this.initChart()
    }
  },
  methods: {
    initChart() {
      this.chart = echarts.init(document.getElementById(this.id))
      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            lineStyle: {
              color: '#57617B'
            }
          },
          formatter: '00:00 - {b0}:59<br />{a0}: {c0}<br />{a1}: {c1}'
          // formatter: function(params) {
          //   return '00:00 - ' + params[0].dataIndex + ':59'
          // }
        },
        legend: {
          top: 20,
          itemGap: 13,
          data: this.source ? this.source.legend : [] // 图例或分组

        },
        xAxis: {
          type: 'category',
          data: this.source ? this.source.category : [] // X轴
        },
        yAxis: [{
          type: 'value',
          name: '金额',
          axisTick: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: '#57617B'
            }
          },
          axisLabel: {
            margin: 10,
            textStyle: {
              fontSize: 14
            }
          },
          splitLine: {
            lineStyle: {
              color: '#57617B'
            }
          }
        }],
        series: [{
          name: this.source ? this.source.series[0].name : [],
          data: this.source ? this.source.series[0].data : [],
          type: 'line',
          smooth: true,
          itemStyle: {
            normal: {
              color: 'rgb(137,189,27)',
              borderColor: 'rgba(137,189,2,0.27)',
              borderWidth: 5

            }
          }
        }, {
          name: this.source ? this.source.series[1].name : [],
          data: this.source ? this.source.series[1].data : [],
          type: 'line',
          smooth: true
        }]
      })
    }
  }
}
</script>
