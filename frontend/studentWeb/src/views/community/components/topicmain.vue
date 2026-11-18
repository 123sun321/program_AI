<template>
<div>
  <el-button type="success" @click="addTheme()">我要发帖</el-button><br>
  <el-button v-if="!this.isSign" class="c_btn" @click="onsign()">每日签到</el-button>
  <el-button v-else :disabled="this.isSign" class="c_btn" @click="onsign()">今日已签到</el-button>
      <div class="report-card">
        <div class="title">最新帖子</div>
        <div class="correlation-report">
            <div class="report-item" v-for="item in list" :key="item.id">
              <span @click="onclick(item)" class="tit"> {{item.title||item.exerciseJoinDto.exercise.name}}</span> <span class="time">{{item.creationTime | formatTime('{y}-{m}-{d} {h}:{i}')}}</span>
            </div>

        </div>
      </div>
      <add-Theme ref="add" @success="flush()"></add-Theme>
</div>
</template>

<script>
/**
 * @file  社区右侧组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/17
 */
import { sign, getIsSign } from '@/api/community/signmanager'
import addTheme from './addTheme'
export default {
  props: ['getLists', 'list', 'onLook', 'onLookreport'],
  components: {
    addTheme

  },
  data() {
    return {
      title: '每日签到',
      isSign: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 添加帖子
    addTheme() {
      this.$refs.add.open()
    },
    onclick(item) {
      if (item.exerciseJoinDto) {
        this.onLookreport(item.id)
      } else {
        this.onLook(item.id)
      }
    },
    flush() {
      this.$emit('getLists')
    },
    getList() {
      getIsSign().then(e => {
        this.isSign = e.data.data
      })
    },
    // 签到
    onsign() {
      sign().then(e => {
        if (e.data.success) {
          this.$sucmsg('签到成功！')
          this.getList()
        }
      })
      this.title = '今日已签到'
    }
    // 获取报告数据
  }
}
</script>

<style lang="scss" scoped>
.el-button {
    width: 100%;
    height: 70px;
}
.el-button--small {
    font-size: 22px;
}
.c_btn{
  margin: 15px 0px;
        color: #1fad4e;
        border-color:#1fad4e;
        background:#fff;
        border-radius: 4px;
 }
    .c_btn:hover{border-color:#1fad4e; }

.report-card{
    background-color: #ffffff;
    border-radius: 5px;
    padding: 10px 20px 20px 20px;
    .title{
        line-height: 50px;
        font-size: 22px;
        color: #666666;
        border-bottom:1px solid #eeeeee;
    }
    .correlation-report{
        padding: 10px 0;
        font-size: 15px;
        .report-item{
           display: flex;
           flex-flow: row;
            padding: 12px 0px;
            color: #4c5157;
            .tit{
              flex:1;
                 overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
              line-height: 20px;
            }
            .time{
              // width: 60px;
             color: #1fad4e;
            }
        }
        .report-item:hover{
            cursor: pointer;
            color: #4c5157;
            background-color: #f9f9f9;
        }
    }
}
</style>
