<template>
  <div class="ai-chat">
    <div class="chat-messages" ref="messageContainer">
      <div v-if="messages.length === 0" class="welcome-tips">
        <p class="welcome-text">你好！我是 AI 学习助手，可以帮助你理解代码、解答问题。</p>
        <p class="welcome-subtitle">点击下方快捷按钮开始提问：</p>
        <div class="preset-prompts">
          <el-button 
            v-for="(prompt, index) in presetPrompts" 
            :key="index"
            size="small"
            @click="sendPrompt(prompt.text)"
          >
            {{ prompt.label }}
          </el-button>
        </div>
      </div>
      
      <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.role]">
        <div class="message-content">
          <div v-if="msg.role === 'user'" class="message-text">{{ msg.content }}</div>
          <div v-else class="message-text markdown-body" v-html="renderMarkdown(msg.content)"></div>
        </div>
      </div>
      
      <div v-if="isLoading" class="message ai">
        <div class="message-content">
          <div class="loading-dots">
            <span></span><span></span><span></span>
          </div>
        </div>
      </div>
    </div>
    
    <div class="chat-input">
      <el-input
        v-model="inputMessage"
        type="textarea"
        :rows="3"
        placeholder="输入你的问题..."
        @keyup.enter.native="handleEnter"
        :disabled="isLoading"
      ></el-input>
      <el-button 
        type="primary" 
        size="small" 
        @click="sendMessage"
        :loading="isLoading"
        :disabled="!inputMessage.trim()"
      >
        发送
      </el-button>
    </div>
  </div>
</template>

<script>
import Showdown from 'showdown'
import { sendAiMessage } from '@/api/ai'

export default {
  name: 'AiChat',
  props: {
    exerciseId: {
      type: String,
      default: ''
    },
    entity: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      messages: [],
      inputMessage: '',
      isLoading: false,
      converter: new Showdown.Converter({
        tables: true,
        simplifiedAutoLink: true,
        strikethrough: true,
        tasklists: true,
        smoothLivePreview: true
      }),
      presetPrompts: [
        { label: '分析代码', text: '请帮助分析现有代码，说明整体架构和主要功能' },
        { label: '逐行讲解', text: '请逐行讲解这段代码的作用和逻辑' },
        { label: '代码规范', text: '请检查代码规范，给出改进建议' },
        { label: '优化建议', text: '请给出代码优化建议，提升性能和可读性' },
        { label: '查找 Bug', text: '请帮我查找代码中可能存在的 Bug 和边界问题' },
        { label: '添加注释', text: '请为这段代码添加详细的注释' },
        { label: '重构建议', text: '请给出代码重构建议，改进代码结构' },
        { label: '样式优化', text: '请给出 CSS/样式方面的优化建议' },
        { label: '无障碍优化', text: '请给出提升可访问性的建议' },
        { label: '响应式适配', text: '请给出移动端响应式适配建议' }
      ]
    }
  },
  computed: {
    fullCodeContent() {
      const codeDto = this.entity.codeDto || {}
      const parts = []
      if (codeDto.html) {
        parts.push('<!-- HTML 代码 -->\n' + codeDto.html)
      }
      if (codeDto.css) {
        parts.push('/* CSS 代码 */\n' + codeDto.css)
      }
      if (codeDto.js) {
        parts.push('// JavaScript 代码\n' + codeDto.js)
      }
      return parts.join('\n\n')
    }
  },
  methods: {
    renderMarkdown(text) {
      return this.converter.makeHtml(text || '')
    },
    sendPrompt(text) {
      this.inputMessage = text
      this.sendMessage()
    },
    handleEnter(e) {
      if (!e.shiftKey) {
        e.preventDefault()
        this.sendMessage()
      }
    },
    async sendMessage() {
      const content = this.inputMessage.trim()
      if (!content || this.isLoading) return

      this.messages.push({
        role: 'user',
        content: content
      })

      this.inputMessage = ''
      this.isLoading = true

      try {
        const response = await sendAiMessage({
          message: content,
          exerciseId: this.exerciseId,
          codeContent: this.fullCodeContent
        })

        this.messages.push({
          role: 'ai',
          content: response.data.data || response.data.message || '抱歉，暂时无法回复'
        })
      } catch (error) {
        this.messages.push({
          role: 'ai',
          content: '请求失败，请稍后重试'
        })
      } finally {
        this.isLoading = false
        this.$nextTick(() => {
          this.scrollToBottom()
        })
      }
    },
    scrollToBottom() {
      const container = this.$refs.messageContainer
      if (container) {
        container.scrollTop = container.scrollHeight
      }
    }
  }
}
</script>

<style scoped lang="scss">
.ai-chat {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}

.welcome-tips {
  text-align: center;
  padding: 40px 20px;
  
  .welcome-text {
    font-size: 18px;
    color: #303133;
    margin-bottom: 10px;
    font-weight: 500;
  }
  
  .welcome-subtitle {
    font-size: 14px;
    color: #909399;
    margin-bottom: 20px;
  }
}

.preset-prompts {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
  
  .el-button {
    margin: 0;
  }
}

.message {
  margin-bottom: 16px;
  display: flex;
  
  &.user {
    justify-content: flex-end;
    
    .message-content {
      background: #409eff;
      color: #fff;
      border-radius: 12px 12px 0 12px;
      max-width: 70%;
    }
  }
  
  &.ai {
    justify-content: flex-start;
    
    .message-content {
      background: #fff;
      color: #333;
      border-radius: 12px 12px 12px 0;
      max-width: 85%;
      box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    }
  }
}

.message-content {
  padding: 12px 16px;
  line-height: 1.6;
  word-break: break-word;
}

.message-text {
  font-size: 14px;
}

.markdown-body {
  :first-child {
    margin-top: 0;
  }
  :last-child {
    margin-bottom: 0;
  }
  
  pre {
    background: #f6f8fa;
    padding: 12px;
    border-radius: 6px;
    overflow-x: auto;
  }
  
  code {
    background: #f0f0f0;
    padding: 2px 6px;
    border-radius: 4px;
    font-size: 13px;
  }
}

.loading-dots {
  display: flex;
  gap: 4px;
  
  span {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: #999;
    animation: bounce 1.4s infinite ease-in-out both;
    
    &:nth-child(1) { animation-delay: -0.32s; }
    &:nth-child(2) { animation-delay: -0.16s; }
  }
}

@keyframes bounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

.chat-input {
  padding: 12px;
  background: #fff;
  border-top: 1px solid #e8e8e8;
  
  .el-textarea {
    margin-bottom: 8px;
  }
  
  .el-button {
    width: 100%;
  }
}
</style>
