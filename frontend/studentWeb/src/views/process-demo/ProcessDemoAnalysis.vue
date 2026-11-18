<template>
  <div class="process-demo-page">
    <section class="process-demo-hero">
      <div>
        <span>过程分析</span>
        <h1>分析视角</h1>
        <p>{{ report.report.summary }}</p>
      </div>
      <div class="process-demo-actions">
        <el-button @click="goRecords">过程记录</el-button>
        <el-button @click="goReplay">过程回放</el-button>
        <el-button type="primary">过程分析</el-button>
        <el-button @click="goReport">反馈报告</el-button>
      </div>
    </section>

    <section class="process-demo-metrics">
      <div><span>过程事件</span><strong>{{ report.metrics.eventCount }}</strong><p>逐步记录输入、运行、错误和修复。</p></div>
      <div><span>错误节点</span><strong>{{ evidence.errorNodes.length }}</strong><p>两个关键错误均可追溯。</p></div>
      <div><span>调试链路</span><strong>{{ evidence.debugPaths.length }}</strong><p>展示错误如何被修复。</p></div>
    </section>

    <section class="process-demo-grid">
      <div class="process-demo-panel span-2">
        <div class="process-demo-panel-title">
          <h3>行为路径</h3>
          <span>Path</span>
        </div>
        <div ref="pathChart" class="process-demo-chart"></div>
      </div>
      <div class="process-demo-panel">
        <div class="process-demo-panel-title">
          <h3>行为分布</h3>
          <span>Action</span>
        </div>
        <div ref="actionChart" class="process-demo-chart"></div>
      </div>
    </section>

    <section class="process-demo-grid">
      <div class="process-demo-panel">
        <div class="process-demo-panel-title">
          <h3>关键错误</h3>
          <span>Error</span>
        </div>
        <div class="process-demo-summary-list">
          <div v-for="item in evidence.errorNodes" :key="item.id" class="process-demo-summary-item danger">
            <strong>{{ item.errorLabel }}</strong>
            <p>{{ item.errorMessage }}</p>
            <em>{{ item.fixHint }}</em>
          </div>
        </div>
      </div>
      <div class="process-demo-panel span-2">
        <div class="process-demo-panel-title">
          <h3>调试解决链路</h3>
          <span>Debug</span>
        </div>
        <div class="process-demo-debug-list">
          <div v-for="item in evidence.debugPaths" :key="item.id" class="process-demo-debug-card">
            <strong>{{ item.title }}</strong>
            <p>{{ item.summary }}</p>
            <div class="process-demo-debug-chain">
              <div v-for="node in item.chain" :key="node.title + node.time" :class="['process-demo-chain-node', node.status]">
                <span>{{ node.title }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="process-demo-panel">
      <div class="process-demo-panel-title">
        <h3>能力标签与薄弱点</h3>
        <span>Profile</span>
      </div>
      <div class="process-demo-two-col">
        <div>
          <p>能力标签</p>
          <div class="process-demo-tags">
            <el-tag v-for="tag in report.abilityTags" :key="tag" type="success">{{ tag }}</el-tag>
          </div>
        </div>
        <div>
          <p>薄弱标签</p>
          <div class="process-demo-tags">
            <el-tag v-for="tag in report.weakTags" :key="tag" type="danger">{{ tag }}</el-tag>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { processDemoEvidence, processDemoReport } from './processDemoData'
import './processDemoStyle.css'

export default {
  name: 'ProcessDemoAnalysis',
  data() {
    return {
      evidence: processDemoEvidence,
      report: processDemoReport,
      pathInstance: null,
      actionInstance: null
    }
  },
  mounted() {
    const recordId = this.$route.query.recordId
    if (recordId) {
      console.log('加载记录ID:', recordId)
    }
    this.$nextTick(() => {
      this.renderCharts()
    })
    window.addEventListener('resize', this.resizeCharts)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts)
    if (this.pathInstance) {
      this.pathInstance.dispose()
    }
    if (this.actionInstance) {
      this.actionInstance.dispose()
    }
  },
  methods: {
    renderCharts() {
      this.pathInstance = echarts.init(this.$refs.pathChart)
      this.actionInstance = echarts.init(this.$refs.actionChart)
      this.pathInstance.setOption({
        tooltip: {},
        series: [{
          type: 'graph',
          layout: 'force',
          roam: true,
          symbolSize: (value) => 38 + Number(value || 1) * 8,
          label: { show: true, color: '#10233f', fontWeight: 700 },
          edgeSymbol: ['none', 'arrow'],
          force: { repulsion: 420, edgeLength: 120 },
          data: this.report.pathNodes.map((item) => ({
            id: item.id,
            name: item.name,
            value: item.value,
            itemStyle: { color: this.nodeColor(item.id) }
          })),
          links: this.report.pathLinks,
          lineStyle: { color: '#2b74d6', width: 2, curveness: 0.18 }
        }]
      })
      const countsMap = {}
      this.evidence.timeline.forEach((item) => {
        countsMap[item.actionType] = (countsMap[item.actionType] || 0) + 1
      })
      const actionNameMap = {
        view_problem: '阅读题目',
        code_edit: '输入代码',
        run_code: '运行代码',
        debug_delete: 'debug_delete',
        debug_modify: '调试修改',
        submit_code: '提交代码',
        dom_ready_error: 'DOM加载错误',
        event_binding_error: '事件绑定错误'
      }
      this.actionInstance.setOption({
        tooltip: { trigger: 'item' },
        color: ['#1f5fbf', '#21b6c7', '#f56c6c', '#67c23a', '#e6a23c', '#8b5cf6'],
        series: [{
          type: 'pie',
          radius: ['42%', '72%'],
          data: Object.entries(countsMap).map(([name, value]) => ({ name: actionNameMap[name] || name, value })),
          label: { color: '#344054' }
        }]
      })
    },
    nodeColor(id) {
      if (!id) return '#1f5fbf'
      if (id.includes('error')) return '#f56c6c'
      if (id.includes('run')) return '#e6a23c'
      if (id.includes('submit') || id.includes('complete')) return '#67c23a'
      if (id.includes('debug')) return '#21b6c7'
      return '#1f5fbf'
    },
    resizeCharts() {
      if (this.pathInstance) {
        this.pathInstance.resize()
      }
      if (this.actionInstance) {
        this.actionInstance.resize()
      }
    },
    goRecords() {
      this.$router.push('/process-demo/records')
    },
    goReplay() {
      this.$router.push('/process-demo/replay')
    },
    goReport() {
      this.$router.push('/process-demo/report')
    }
  }
}
</script>
