<template>
    <div class="list" v-loading="loading">
     <el-row :span="24">
        <!-- <el-col :span="5">
        <div class="logo">
           <img  width="100%" :src="tableData.logo">
        </div>
        </el-col> -->
        <el-col :span="24" >
            <div class="c_title"><a href="#">Web 前端工程师</a></div>
            <div class="top">
              <ul>
            <li class="number">学习人数<span> {{this.joinCount}}</span> 人</li>
            <li class="number">总课程<span> {{num}}</span></li>
            <li class="number">已学课程<span> {{finishedCourse}}</span></li>
            <li class="number">预计学习<span> {{times}}</span> 小时</li>
            </ul>
            </div>
           <br>
            <div class="c_info">
                <span> Web 前端学习路径中首先实践 HTML、CSS、JavaScript、CSS3、HTML5等基础知识，然后完成实现页面特效组件、网页游戏、Web应用等一系列项目，帮助你通过动手实践成长为合格的 Web 前端工程师。</span>
            </div>
            <div >
                  <el-button v-if="!check" type="success" @click="onjoin"  >加入路径</el-button><el-button v-else :disabled="check" type="warning">已加入</el-button><span style="color:#999;font-size:13px;">&nbsp;&nbsp;&nbsp;加入获得路径课程更新提醒</span>
            </div>
        </el-col>
    </el-row>
    </div>
</template>

<script>
/**
 * @file 学习路径top组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/28
 */
import { addJoinCoursePath, isJoin, joinCount } from '@/api/studypath/pathmanage'
export default {
  props: ['num', 'times', 'finishedCourse'],
  data() {
    return {
      form: {
        page: {
          current: 2,
          pageCount: 9,
          total: 0
        },
        search: '',
        orderBy: 'creation_time desc'
      },
      check: false,
      joinCount: 0,
      loading: false
    }
  },
  created() {
    this.loading = true
    isJoin().then(e => {
      this.check = e.data.data
    })
    joinCount().then(e => {
      this.joinCount = e.data.data
      this.loading = false
    })
  },
  methods: {
    // 加入路径
    onjoin() {
      addJoinCoursePath().then(e => {
        if (e.data.success) {
          this.$message({
            type: 'success',
            message: '加入路径成功！'
          })
          this.check = true
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.list{
   .logo{
    max-width: 360px;
    max-height: 205px;
    //  overflow: hidden;
   }
   .c_title{
font-size: 22px;
color: #000001;
   }
   .c_info{
      display: -webkit-box;
      -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    word-wrap: break-word;
     margin: 12px 0px;
     line-height: 22px;
     font-size: 15px;
     color: #999;
   }
 .top{
   margin-top: 1%;
      li{
     float: left;
   }
    // padding: 10px;
        .number{
            margin-right:20px;
            // float:right;
            font-size: 14px;
            color: #666666;
            span{
              vertical-align: middle;
                font-size: 20px;
                color: #1fad4e;
            }
        }
    }
}
</style>
