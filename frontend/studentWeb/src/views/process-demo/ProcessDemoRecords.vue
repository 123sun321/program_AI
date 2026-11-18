<template>
  <div class="process-demo-page">
    <section class="process-demo-hero">
      <div>
        <span>过程记录</span>
        <h1>证据链视角</h1>
        <p>{{ evidence.summary }}</p>
      </div>
      <div class="process-demo-actions">
        <el-button type="primary">过程记录</el-button>
        <el-button @click="goReplay">过程回放</el-button>
        <el-button @click="goAnalysis">过程分析</el-button>
        <el-button @click="goReport">反馈报告</el-button>
      </div>
    </section>

    <section class="process-demo-metrics">
      <div><span>步骤数</span><strong>{{ timeline.length }}</strong><p>完整记录 19 个行为节点。</p></div>
      <div><span>错误节点</span><strong>{{ evidence.errorNodes.length }}</strong><p>保留两次关键错误。</p></div>
      <div><span>调试链路</span><strong>{{ evidence.debugPaths.length }}</strong><p>展示问题从出现到修复。</p></div>
    </section>

    <section class="process-demo-layout">
      <aside class="process-demo-panel process-demo-timeline-panel">
        <div class="process-demo-panel-title">
          <h3>行为时间线</h3>
          <span>{{ timeline.length }} Steps</span>
        </div>
        <div class="process-demo-timeline-list">
          <button
            v-for="item in timeline"
            :key="item.id"
            :class="['process-demo-timeline-item', { active: selectedId === item.id, error: item.isErrorNode }]"
            @click="selectedId = item.id"
          >
            <div class="process-demo-step-no">{{ item.stepCode }}</div>
            <div class="process-demo-step-main">
              <strong>{{ item.actionName }}</strong>
              <p>{{ item.description }}</p>
            </div>
            <span>{{ item.time }}</span>
          </button>
        </div>
      </aside>

      <main class="process-demo-panel process-demo-diff-panel">
        <div class="process-demo-panel-title">
          <h3>代码快照对比</h3>
          <span>{{ currentStep.actionName }}</span>
        </div>
        <div class="process-demo-diff-stats">
          <el-tag type="success">新增 {{ currentStep.addedLines || 0 }} 行</el-tag>
          <el-tag type="danger">删除 {{ currentStep.removedLines || 0 }} 行</el-tag>
          <el-tag>{{ currentStep.language || '-' }}</el-tag>
        </div>
        <div class="process-demo-diff-code">
          <div
            v-for="(row, index) in currentStep.diffBlocks || []"
            :key="index"
            :class="['process-demo-diff-row', row.type]"
          >
            <div class="process-demo-diff-cell left"><span>{{ row.leftNo || '' }}</span><code>{{ row.left }}</code></div>
            <div class="process-demo-diff-cell right"><span>{{ row.rightNo || '' }}</span><code>{{ row.right }}</code></div>
          </div>
        </div>
      </main>

      <aside class="process-demo-panel process-demo-card-panel">
        <div class="process-demo-panel-title">
          <h3>当前步骤证据</h3>
          <span>Evidence</span>
        </div>
        <div class="process-demo-card-list">
          <div v-for="item in currentStep.evidence || []" :key="item.label" class="process-demo-card-item">
            <span>{{ item.label }}</span>
            <strong>{{ item.value }}</strong>
          </div>
        </div>
        <div v-if="currentStep && currentStep.knowledgePoints && currentStep.knowledgePoints.length" class="process-demo-group">
          <p>关联知识点</p>
          <div class="process-demo-tags">
            <el-tag v-for="tag in currentStep.knowledgePoints" :key="tag" type="warning">{{ tag }}</el-tag>
          </div>
        </div>
        <div v-if="currentStepIsError" class="process-demo-group">
          <p>当前错误</p>
          <el-alert
            :title="currentStep.errorType"
            :description="currentStep.errorMessage"
            type="error"
            :closable="false"
            show-icon
          />
        </div>
      </aside>
    </section>

    <section class="process-demo-grid">
      <div class="process-demo-panel span-2">
        <div class="process-demo-panel-title">
          <h3>调试路径摘要</h3>
          <span>Debug Path</span>
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
      <div class="process-demo-panel">
        <div class="process-demo-panel-title">
          <h3>错误概览</h3>
          <span>Error Node</span>
        </div>
        <div class="process-demo-summary-list">
          <div v-for="item in evidence.errorNodes" :key="item.id" class="process-demo-summary-item danger">
            <strong>{{ item.errorLabel }}</strong>
            <p>{{ item.errorMessage }}</p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { processDemoEvidence } from './processDemoData'
import './processDemoStyle.css'

export default {
  name: 'ProcessDemoRecords',
      data() {
        const timeline = processDemoEvidence.timeline
        return {
          evidence: processDemoEvidence,
          timeline: timeline,
          selectedId: timeline[0] ? timeline[0].id : null
        }
      },
  computed: {
    currentStep() {
      const step = this.timeline.find((item) => item.id === this.selectedId)
      return step || this.timeline[0] || {}
    },
    currentStepIsError() {
      return this.currentStep && this.currentStep.resultStatus === 'fail' && Boolean(this.currentStep.errorType)
    }
  },
  watch: {
    selectedId(value) {
      if (!value) return
      this.$router.replace({ path: '/process-demo/records', query: { step: value } })
    }
  },
  methods: {
    goReplay() {
      this.$router.push({ path: '/process-demo/replay', query: { step: this.currentStep ? this.currentStep.id : null } })
    },
    goAnalysis() {
      this.$router.push('/process-demo/analysis')
    },
    goReport() {
      this.$router.push('/process-demo/report')
    }
  }
}
</script>
