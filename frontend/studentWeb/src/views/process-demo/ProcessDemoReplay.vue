<template>
  <div class="process-demo-page">
    <section class="process-demo-hero">
      <div>
        <span>重点功能 · 行为回放</span>
        <h1>编程过程回放</h1>
        <p>回放直接复用静态演示数据中的代码快照差异，可以看到每一步到底改了什么。</p>
      </div>
      <div class="process-demo-actions">
        <el-button @click="previous">上一步</el-button>
        <el-button type="primary" @click="togglePlay">{{ playing ? '暂停' : '播放' }}</el-button>
        <el-button @click="next">下一步</el-button>
        <el-divider direction="vertical"></el-divider>
        <el-button @click="goRecords">过程记录</el-button>
        <el-button type="primary">过程回放</el-button>
        <el-button @click="goAnalysis">过程分析</el-button>
        <el-button @click="goReport">反馈报告</el-button>
      </div>
    </section>

    <section class="process-demo-layout">
      <aside class="process-demo-panel process-demo-timeline-panel">
        <div class="process-demo-panel-title">
          <h3>回放帧</h3>
          <span>{{ currentIndex + 1 }} / {{ timeline.length }}</span>
        </div>
        <el-slider v-model="currentIndex" :min="0" :max="timeline.length - 1" :show-tooltip="false" />
        <div class="process-demo-timeline-list">
          <button
            v-for="(item, idx) in timeline"
            :key="item.id"
            :class="['process-demo-timeline-item', { active: idx === currentIndex, error: item.isErrorNode }]"
            @click="currentIndex = idx"
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
          <h3>{{ currentStep.actionName || '过程步骤' }}</h3>
          <span>{{ currentStep.time }}</span>
        </div>
        <p class="process-demo-desc">{{ currentStep.description }}</p>
        <div class="process-demo-diff-head">
          <div><strong>上一步代码</strong><span>{{ previousStep ? previousStep.actionName : '初始状态' }}</span></div>
          <div><strong>当前步骤代码</strong><span>{{ currentStep.actionName }}</span></div>
        </div>
        <div class="process-demo-diff-stats">
          <el-tag v-if="codeUnchanged" type="info">本次未修改代码</el-tag>
          <el-tag v-else type="success">新增 {{ currentStep.addedLines || 0 }} 行</el-tag>
          <el-tag v-if="!codeUnchanged" type="danger">删除 {{ currentStep.removedLines || 0 }} 行</el-tag>
          <el-tag>{{ currentStep.stage }}</el-tag>
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
        <div v-if="currentStepIsError" class="process-demo-group">
          <p>错误节点</p>
          <el-alert
            :title="currentStep.errorType"
            :description="currentStep.errorMessage"
            type="error"
            :closable="false"
            show-icon
          />
        </div>
        <div class="process-demo-group">
          <p>阶段标签</p>
          <div class="process-demo-tags">
            <el-tag>{{ currentStep.stage }}</el-tag>
            <el-tag v-for="tag in currentStep.knowledgePoints || []" :key="tag" type="warning">{{ tag }}</el-tag>
          </div>
        </div>
      </aside>
    </section>
  </div>
</template>

<script>
import { processDemoTimeline } from './processDemoData'
import './processDemoStyle.css'

export default {
  name: 'ProcessDemoReplay',
  data() {
    const timeline = processDemoTimeline
    const initialStep = Number(this.$route.query.step)
    const initialIndex = Number.isFinite(initialStep) && initialStep > 0 ? Math.min(initialStep - 1, timeline.length - 1) : 0
    return {
      timeline: timeline,
      currentIndex: initialIndex,
      playing: false,
      timer: null
    }
  },
  computed: {
    currentStep() {
      return this.timeline[this.currentIndex] || {}
    },
    previousStep() {
      return this.currentIndex > 0 ? this.timeline[this.currentIndex - 1] : null
    },
    codeUnchanged() {
      return this.previousStep && this.previousStep.codeSnapshot === this.currentStep && this.currentStep.codeSnapshot
    },
    currentStepIsError() {
      return this.currentStep && this.currentStep.resultStatus === 'fail' && Boolean(this.currentStep.errorType)
    }
  },
  watch: {
    playing(value) {
      clearInterval(this.timer)
      if (value) {
        this.timer = setInterval(this.next, 1600)
      }
    },
    currentIndex(value) {
      this.$router.replace({ path: '/process-demo/replay', query: { step: this.timeline[value] ? this.timeline[value].id : null } })
    }
  },
  beforeDestroy() {
    clearInterval(this.timer)
  },
  methods: {
    togglePlay() {
      this.playing = !this.playing
    },
    next() {
      this.currentIndex = this.currentIndex >= this.timeline.length - 1 ? 0 : this.currentIndex + 1
    },
    previous() {
      this.currentIndex = this.currentIndex <= 0 ? this.timeline.length - 1 : this.currentIndex - 1
    },
    goRecords() {
      this.$router.push({ path: '/process-demo/records', query: { step: this.currentStep ? this.currentStep.id : null } })
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
