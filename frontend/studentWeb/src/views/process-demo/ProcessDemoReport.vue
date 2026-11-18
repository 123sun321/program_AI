<template>
  <div class="process-demo-page">
    <section class="process-demo-hero">
      <div>
        <span>过程反馈</span>
        <h1>反馈视角</h1>
        <p>{{ report.report.summary }}</p>
      </div>
      <div class="process-demo-score">
        <strong>{{ report.metrics.processScore }}</strong>
        <span>过程评分</span>
      </div>
      <div class="process-demo-actions">
        <el-button @click="goRecords">过程记录</el-button>
        <el-button @click="goReplay">过程回放</el-button>
        <el-button @click="goAnalysis">过程分析</el-button>
        <el-button type="primary">反馈报告</el-button>
      </div>
    </section>

    <section class="process-demo-report-sheet">
      <div class="process-demo-report-header">
        <div>
          <h2>{{ report.experiment.title }}</h2>
          <p>{{ report.report.pattern }}</p>
        </div>
        <div>
          <strong>{{ report.report.latestScore }}</strong>
          <span>最新得分</span>
        </div>
      </div>

      <div class="process-demo-section two-cols">
        <div>
          <h3>学习画像</h3>
          <p>能力标签</p>
          <div class="process-demo-tags">
            <el-tag v-for="tag in report.abilityTags" :key="tag" type="success">{{ tag }}</el-tag>
          </div>
          <p>薄弱标签</p>
          <div class="process-demo-tags">
            <el-tag v-for="tag in report.weakTags" :key="tag" type="danger">{{ tag }}</el-tag>
          </div>
        </div>
        <div>
          <h3>持续跟踪指标</h3>
          <div class="process-demo-tracking-grid">
            <div><strong>{{ report.metrics.eventCount }}</strong><span>过程事件</span></div>
            <div><strong>{{ report.metrics.runCount }}</strong><span>运行验证</span></div>
            <div><strong>{{ report.metrics.errorCount }}</strong><span>错误次数</span></div>
            <div><strong>{{ report.metrics.submitCount }}</strong><span>提交次数</span></div>
          </div>
        </div>
      </div>

      <div class="process-demo-section">
        <h3>关键问题</h3>
        <div class="process-demo-summary-list">
          <div v-for="item in report.errorInsights" :key="item.type" class="process-demo-summary-item danger">
            <strong>{{ item.type }}</strong>
            <p>{{ item.reason }}</p>
            <em>修复：{{ item.fix }}</em>
          </div>
        </div>
      </div>

      <div class="process-demo-section">
        <h3>反馈建议</h3>
        <div class="process-demo-summary-list">
          <div v-for="item in report.recommendations" :key="item.title" class="process-demo-summary-item success">
            <strong>{{ item.title }}</strong>
            <p>{{ item.reason }}</p>
            <em>下一步：{{ item.nextStep }}</em>
          </div>
          <div class="process-demo-summary-item">
            <strong>系统建议</strong>
            <p>{{ report.report.feedbackSuggestion }}</p>
          </div>
        </div>
      </div>

      <div class="process-demo-section two-cols">
        <div>
          <h3>调试结论</h3>
          <div class="process-demo-summary-list">
            <div v-for="item in evidence.debugPaths" :key="item.id" class="process-demo-summary-item">
              <strong>{{ item.title }}</strong>
              <p>{{ item.summary }}</p>
            </div>
          </div>
        </div>
        <div>
          <h3>阶段节奏</h3>
          <div class="process-demo-stage-list">
            <div v-for="item in evidence.stages" :key="item.id">
              <strong>{{ item.name }}</strong>
              <span>{{ item.eventCount }} 步 / {{ item.duration }} 秒</span>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { processDemoEvidence, processDemoReport } from './processDemoData'
import './processDemoStyle.css'

export default {
  name: 'ProcessDemoReport',
  data() {
    return {
      evidence: processDemoEvidence,
      report: processDemoReport
    }
  },
  methods: {
    goRecords() {
      this.$router.push('/process-demo/records')
    },
    goReplay() {
      this.$router.push('/process-demo/replay')
    },
    goAnalysis() {
      this.$router.push('/process-demo/analysis')
    }
  }
}
</script>
