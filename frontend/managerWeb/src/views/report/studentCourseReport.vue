<template>
  <div class="report-container">
    <el-page-header @back="goBack" :content="pageTitle"></el-page-header>

    <div class="action-bar">
      <el-button 
        type="primary" 
        :loading="starting"
        @click="handleStartAnalyze"
        :disabled="analyzing"
        size="large"
      >
        {{ analyzing ? '分析中...' : '生成学习报告' }}
      </el-button>
      <el-button 
        v-if="analyzing" 
        @click="handleCancel"
        size="large"
      >
        取消
      </el-button>
    </div>

    <div v-if="analyzing" class="progress-section">
      <el-progress 
        :percentage="progress" 
        :status="progressStatus"
        :stroke-width="20"
      ></el-progress>
      <div class="progress-text">
        <span v-if="status === 'running'">
          正在分析知识点... {{ completedItems }}/{{ totalItems }}
        </span>
        <span v-else-if="status === 'pending'">任务排队中...</span>
      </div>
    </div>

    <div v-if="loading && !analyzing" class="loading-overlay">
      <i class="el-icon-loading"></i>
      <p>加载分析报告...</p>
    </div>

    <div v-else-if="knowledgeList.length > 0" class="report-content">
      <div class="summary-cards">
        <el-card shadow="hover" class="summary-card">
          <div class="card-label">总知识点</div>
          <div class="card-value">{{ knowledgeList.length }}</div>
        </el-card>
        <el-card shadow="hover" class="summary-card">
          <div class="card-label">已掌握</div>
          <div class="card-value text-success">{{ masteredCount }}</div>
        </el-card>
        <el-card shadow="hover" class="summary-card">
          <div class="card-label">需加强</div>
          <div class="card-value text-danger">{{ weakCount }}</div>
        </el-card>
      </div>

      <div v-if="weakTags.length > 0" class="tag-section">
        <div class="section-header">
          <i class="el-icon-info"></i>
          <span>待提升知识点</span>
          <el-tag size="mini" type="warning">{{ weakTags.length }}</el-tag>
        </div>
        <div class="tag-container">
          <el-tag 
            v-for="tag in weakTags" 
            :key="tag.knowledge"
            type="warning"
            class="knowledge-tag"
            @click="scrollToCard(tag.knowledge)"
          >
            {{ tag.knowledge }}
          </el-tag>
        </div>
      </div>

      <div v-if="strengthTags.length > 0" class="tag-section">
        <div class="section-header">
          <i class="el-icon-success"></i>
          <span>已掌握知识点</span>
          <el-tag size="mini" type="success">{{ strengthTags.length }}</el-tag>
        </div>
        <div class="tag-container">
          <el-tag 
            v-for="tag in strengthTags" 
            :key="tag.knowledge"
            type="success"
            class="knowledge-tag"
            @click="scrollToCard(tag.knowledge)"
          >
            {{ tag.knowledge }}
          </el-tag>
        </div>
      </div>

      <div class="detail-section">
        <div class="section-title">详细分析报告</div>
        <el-card 
          v-for="item in knowledgeList" 
          :key="item.knowledge"
          :id="'card-' + item.knowledge"
          class="knowledge-card"
          :class="item.level"
          shadow="hover"
        >
          <div class="card-header">
            <div class="knowledge-info">
              <span class="knowledge-name">{{ item.knowledge }}</span>
              <el-tag size="mini">{{ item.exerciseCount }} 次练习</el-tag>
            </div>
            <div class="score-section">
              <div class="score-circle" :class="item.level">
                <span class="score">{{ item.masteryScore }}</span>
              </div>
              <el-tag :type="getLevelType(item.level)">{{ getLevelText(item.level) }}</el-tag>
            </div>
          </div>

          <div class="weakness-block">
            <div class="block-title"><i class="el-icon-info"></i> 薄弱环节</div>
            <p>{{ item.weakness }}</p>
          </div>

          <div class="suggestions-block">
            <div class="block-title"><i class="el-icon-light-rain"></i> 改进建议</div>
            <ul>
              <li v-for="(s, idx) in item.suggestions" :key="idx">{{ s }}</li>
            </ul>
          </div>
        </el-card>
      </div>
    </div>

    <div v-else class="empty-state">
      <i class="el-icon-document-delete"></i>
      <p>暂无学习数据，点击「生成学习报告」开始分析</p>
    </div>
  </div>
</template>

<script>
import { startCourseAnalyze, getAnalyzeProgress } from '@/api/learningReport'

export default {
  name: 'StudentCourseReport',
  data() {
    return {
      studentId: null,
      courseId: null,
      studentName: '',
      loading: false,
      starting: false,
      analyzing: false,
      taskId: null,
      status: '',
      progress: 0,
      totalItems: 0,
      completedItems: 0,
      knowledgeList: [],
      pollingTimer: null
    }
  },
  computed: {
    pageTitle() {
      return this.studentName ? `${this.studentName} - 学习报告` : '学生学习报告'
    },
    weakTags() {
      return this.knowledgeList.filter(item => item.level === 'weak')
    },
    strengthTags() {
      return this.knowledgeList.filter(item => item.level !== 'weak')
    },
    masteredCount() {
      return this.knowledgeList.filter(item => item.level !== 'weak').length
    },
    weakCount() {
      return this.knowledgeList.filter(item => item.level === 'weak').length
    },
    progressStatus() {
      if (this.status === 'completed') return 'success'
      if (this.status === 'failed') return 'exception'
      return null
    }
  },
  created() {
    this.studentId = this.$route.query.studentId
    this.courseId = this.$route.query.courseId
    this.studentName = this.$route.query.studentName || ''
    this.loadLastAnalysis()
  },
  beforeDestroy() {
    this.stopPolling()
  },
  methods: {
    goBack() {
      this.$router.push({ name: 'courseindex' })
    },
    async handleStartAnalyze() {
      this.starting = true
      try {
        const res = await startCourseAnalyze({
          studentId: this.studentId,
          courseId: this.courseId
        })
        const body = res.data
        if (body.code === 200) {
          const data = body.data
          if (data.result && data.status === 'completed') {
            this.knowledgeList = data.result
            this.$message.success('已加载缓存分析报告')
          } else {
            this.taskId = typeof data === 'string' ? data : data.taskId
            this.analyzing = true
            this.status = 'pending'
            this.progress = 0
            this.startPolling()
            this.$message.success('分析任务已启动')
          }
        }
      } catch (error) {
        this.$message.error('启动失败：' + (error.message || '未知错误'))
      } finally {
        this.starting = false
      }
    },
    startPolling() {
      this.pollingTimer = setInterval(async () => {
        try {
          const res = await getAnalyzeProgress(this.taskId)
          const body = res.data
          if (body.code === 200) {
            const task = body.data
            this.status = task.status
            this.progress = task.progress || 0
            this.totalItems = task.totalItems || 0
            this.completedItems = task.completedItems || 0

            if (task.status === 'completed') {
              this.knowledgeList = task.result || []
              this.analyzing = false
              this.stopPolling()
              this.$message.success('分析报告生成完成')
            } else if (task.status === 'failed') {
              this.analyzing = false
              this.stopPolling()
              this.$message.error('分析失败：' + task.errorMessage)
            }
          }
        } catch (error) {
          console.error('轮询失败', error)
        }
      }, 2000)
    },
    stopPolling() {
      if (this.pollingTimer) {
        clearInterval(this.pollingTimer)
        this.pollingTimer = null
      }
    },
    handleCancel() {
      this.stopPolling()
      this.analyzing = false
      this.$message.info('已取消分析')
    },
    async loadLastAnalysis() {
      this.loading = true
      try {
        const res = await startCourseAnalyze({
          studentId: this.studentId,
          courseId: this.courseId
        })
        const body = res.data
        if (body.code === 200) {
          const data = body.data
          if (data.result && data.status === 'completed') {
            this.knowledgeList = data.result
          }
        }
      } catch (error) {
        console.error('加载分析结果失败', error)
      } finally {
        this.loading = false
      }
    },
    getLevelType(level) {
      const map = { excellent: 'success', good: 'success', weak: 'warning' }
      return map[level] || 'info'
    },
    getLevelText(level) {
      const map = { excellent: '已掌握', good: '学习中', weak: '待提升' }
      return map[level] || '未知'
    },
    scrollToCard(knowledge) {
      const el = document.getElementById('card-' + knowledge)
      if (el) el.scrollIntoView({ behavior: 'smooth', block: 'center' })
    }
  }
}
</script>

<style lang="scss" scoped>
.report-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.action-bar {
  margin: 20px 0;
  text-align: right;
}

.progress-section {
  margin-bottom: 30px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  
  .progress-text {
    margin-top: 12px;
    text-align: center;
    color: #606266;
    font-size: 14px;
  }
}

.loading-overlay {
  text-align: center;
  padding: 80px 0;
  
  i {
    font-size: 48px;
    color: #409EFF;
  }
  
  p {
    margin-top: 16px;
    color: #909399;
    font-size: 16px;
  }
}

.empty-state {
  text-align: center;
  padding: 80px 0;
  color: #909399;
  
  i {
    font-size: 64px;
    color: #C0C4CC;
    margin-bottom: 16px;
  }
  
  p {
    font-size: 14px;
  }
}

.summary-cards {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  
  .summary-card {
    flex: 1;
    text-align: center;
    
    .card-label {
      color: #909399;
      font-size: 14px;
      margin-bottom: 8px;
    }
    
    .card-value {
      font-size: 36px;
      font-weight: bold;
      color: #303133;
    }
    
    .text-success { color: #67C23A; }
    .text-danger { color: #F56C6C; }
  }
}

.tag-section {
  margin-bottom: 24px;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  
  .section-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 16px;
    font-size: 16px;
    font-weight: 500;
    color: #303133;
  }
  
  .tag-container {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }
  
  .knowledge-tag {
    cursor: pointer;
    transition: transform 0.2s;
    
    &:hover {
      transform: scale(1.05);
    }
  }
}

.detail-section {
  .section-title {
    font-size: 18px;
    font-weight: 500;
    color: #303133;
    margin-bottom: 20px;
    padding-bottom: 12px;
    border-bottom: 2px solid #409EFF;
  }
}

.knowledge-card {
  margin-bottom: 20px;
  border-left: 4px solid;
  transition: transform 0.2s;
  
  &:hover {
    transform: translateX(4px);
  }
  
  &.excellent { border-left-color: #67C23A; }
  &.good { border-left-color: #67C23A; }
  &.weak { border-left-color: #E6A23C; }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    
    .knowledge-info {
      display: flex;
      align-items: center;
      gap: 12px;
      
      .knowledge-name {
        font-size: 18px;
        font-weight: 500;
        color: #303133;
      }
    }
    
    .score-section {
      display: flex;
      align-items: center;
      gap: 12px;
      
      .score-circle {
        width: 56px;
        height: 56px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20px;
        font-weight: bold;
        
        &.excellent {
          background: #f0f9eb;
          color: #67C23A;
          border: 2px solid #67C23A;
        }
        &.good {
          background: #f0f9eb;
          color: #67C23A;
          border: 2px solid #67C23A;
        }
        &.weak {
          background: #fdf6ec;
          color: #E6A23C;
          border: 2px solid #E6A23C;
        }
      }
    }
  }
  
  .weakness-block {
    background: #fdf6ec;
    padding: 12px 16px;
    border-radius: 6px;
    margin-bottom: 16px;
    
    .block-title {
      font-size: 14px;
      font-weight: 500;
      color: #E6A23C;
      margin-bottom: 8px;
    }
    
    p {
      color: #606266;
      line-height: 1.6;
      margin: 0;
    }
  }
  
  .suggestions-block {
    .block-title {
      font-size: 14px;
      font-weight: 500;
      color: #409EFF;
      margin-bottom: 8px;
    }
    
    ul {
      margin: 0;
      padding-left: 20px;
      
      li {
        color: #606266;
        line-height: 1.8;
        margin: 4px 0;
      }
    }
  }
}
</style>
