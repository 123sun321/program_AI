<template>
  <div class="reportdetail">
      <el-row :gutter="20">
        <el-col :span="18">
           <div class="report-info">
            <el-tabs tab-position="top" type="card" @tab-click="tabClick">
              <el-tab-pane v-for="(item,index) in labels" :key="index" :label="item.name"></el-tab-pane>
              <!-- <el-tab-pane label="实验报告"></el-tab-pane> -->
          </el-tabs>
      <div v-loading="loading">
        <div v-if="this.flag && this.report.length!==0">
          <div  class="top-info" v-for="(r,index) in report" :key="index">
          <el-row :gutter="20">
            <el-col :span="22"> <div class="course-body">
              <div class="c_info" @click="onLookreport(r.id)">【实验报告】 {{r.exerciseJoinDto.exercise.name}}</div>
                <div class="detail">
                 <a @click="touser(r.exerciseJoinDto.student.userId)"> <img :src="r.exerciseJoinDto.student.imgUrl" :onerror="defaultImg"></a>
                  <div class="nickname-wrap">
                 <a @click="touser(r.exerciseJoinDto.student.userId)">  <div class="name">{{r.exerciseJoinDto.student.name}}</div> </a>
                </div>
              </div>
           </div>
           </el-col>
             <el-col :span="1"><ul style="color:#1fad4e;padding-top:24px;text-align:center;"><li class="number">{{r.commentNumber}}</li><li class="tit">回复</li></ul></el-col>
            <el-col :span="1"><ul style="color:#999999;padding-top:24px;text-align:center;"><li class="number">{{r.clickNumber}}</li><li class="tit">查看</li></ul></el-col>
          </el-row>
          </div>
          <page :page="form2.page" @changed="getreport"></page>
        </div>

        <div v-else-if="!flag && tableData.length > 0 ">
          <div  class="top-info" v-for="(c,index) in tableData" :key="index">
          <el-row :gutter="20">
            <el-col :span="22"> <div class="course-body">
              <div class="c_info" @click="onLook(c.id)">【{{c.plateName}}】 {{c.title}}</div>
                <div class="detail">
                  <div>
                   <a @click="touser(c.userId)"><img v-if="c.user" :src="c.user.userPic" :onerror="defaultImg"></a>
                  </div>
                  <div class="nickname-wrap">
                   <a @click="touser(c.userId)"><div class="name">{{c.user ? c.user.name : ''}}</div> </a>
                  </div>
                </div>
              </div>
           </el-col>
             <el-col :span="1"><ul style="color:#1fad4e;padding-top:24px;text-align:center;"><li class="number">{{c.commentNumber}}</li><li class="tit">回复</li></ul></el-col>
            <el-col :span="1"><ul style="color:#999999;padding-top:24px;text-align:center;"><li class="number">{{c.click}}</li><li class="tit">查看</li></ul></el-col>
          </el-row>
          </div>
          <page :page="form.page" @changed="getList"></page>
           </div>

           <div  style="text-align:center;margin-bottom:12px;" v-else  class='noneData'>
            暂无数据
           </div>
       </div>
      </div>
          </el-col>
            <el-col :span="6">
            <topicmain :list="list" :onLook="onLook" :onLookreport="onLookreport" @getLists="getList"></topicmain>
        </el-col>
        </el-row>

 </div>
</template>

<script>
/**
 * @file  社区组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/17
 */
import Vue from 'vue'
import page from '@/components/page'
import topicmain from './components/topicmain'
import { findPage, findlist, getreport, addClick } from '@/api/community/theme'
import correlationReport from './components/correlationReport'
export default {
  components: {
    topicmain,
    page,
    correlationReport
  },
  data() {
    return {
      report: [],
      messageData: [], // 帖子
      labels: [], // 标签
      list: [], // 最新帖子
      flag: false,
      messageReadDialogVisible: false, // 消息阅读窗口显示状态
      tableData: [], // 列表数据
      // 分页和模糊查询帖子
      form: {
        form: {

        },
        page: {
          current: 1,
          pageCount: 6,
          total: 0
        },
        search: ''
      },
      // 分页和模糊查询报告
      form2: {
        form: {
        },
        page: {
          current: 1,
          pageCount: 6,
          total: 0
        },
        search: ''
      },
      defaultImg: 'this.src="' + require('@/assets/images/default.jpeg') + '"', // 默认头像图像
      loading: false // 加载
    }
  },
  created() {
    this.$nextTick(e => {
      this.getnav()
      this.getList()
    })
  },
  methods: {
    // 获取帖子数据
    getList() {
      const vm = this
      vm.loading = true
      const params = vm.form
      debugger
      findPage(params).then(e => {
        debugger
        const data = e.data.data
        vm.tableData = e.data.data.records
        vm.list = vm.tableData.slice(0, 5)
        vm.form.page.total = data.total
        vm.loading = false
      })
    },
    // 获取报告数据
    getReport() {
      const vm = this
      vm.loading = true
      const params = vm.form2
      getreport(params).then(e => {
        const data = e.data.data
        vm.report = data.records
        vm.list = vm.report.slice(0, 5)
        vm.form2.page.total = data.total
        vm.loading = false
      }).catch(() => {
        vm.loading = false
      })
    },
    getnav() {
      const vm = this
      findlist().then(e => {
        vm.labels = e.data.data
        const labelName = {
          title: '全部',
          name: '全部'
        }
        const exerciseName = {
          title: '实验报告',
          name: '实验报告'
        }
        vm.labels.unshift(labelName)
        vm.labels.push(exerciseName)
      })
    },
    touser(id) {
      this.$router.push({ path: '/user/info/' + id })
    },
    tabClick(e) {
      if (e.label === '实验报告') {
        this.flag = true
        this.getReport()
      } else if (e.label === '全部') {
        this.flag = false
        this.form.form = {}
        this.form.page = {
          current: 1,
          pageCount: 6,
          total: 0
        }
        this.getList()
      } else {
        this.flag = false
        for (var i = 0; i < this.labels.length; i++) {
          if (e.label === this.labels[i].name) {
            Vue.set(this.form.form, 'plate', this.labels[i].id)
          }
        }
        this.getList()
      }
    },
    onLook(id) {
      addClick(id).then(e => {})
      this.$router.push({ path: 'topic', query: { communityid: id }})
    },
    onLookreport(id) {
      this.$router.push({ path: 'reportDetail', query: { reportid: id }})
    }
  }
}
</script>
<style lang="scss" scoped>
.reportdetail{
    margin: 15px;
    .course-body {
    position: relative;
    background: #fff;
    margin: 15px 0;
      height: 64px;
}
.noneData{
  height: 490px;
  text-align: center;
  width: 100%;
  padding-top:260px;
  background-repeat:no-repeat;
  background-image: url('~@/assets/images/c6.jpg');
  background-position:50% 30%;
}
 .detail{
  margin:10px 12px;
    // overflow: hidden;
}
 .report-info{
       background-color: #ffffff;
         border-radius: 5px;
        padding: 20px;
    }
.report-info /deep/ .el-tabs__nav{
border:none!important;
padding:10px 0;
.el-tabs__item{
    padding: 0 10px!important;
    margin-right:10px;
    text-align: center;
    height: 30px;
    font-size:16px;
    line-height:30px;
    border:none!important;
}
.el-tabs__item.is-active,.el-tabs__item:hover{
    transition:initial;
    background-color: #e8f7ed;
    border-radius: 15px;
    color:#1fad4e;
}
}
 .c_info{
   cursor: pointer;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 16px;
  color: #07111b;
   }
.detail img{
    width:35px;
    height:35px;
    border-radius:50%
}
.nickname-wrap{
    vertical-align: middle;
    position: relative;
    margin-top: -32px;
    margin-left: 40px;
}
.nickname-wrap .name{
    font-size:15px;
    color:#999999;
}
 .top-info /deep/ .el-col-1{
    min-width:65px;
 }
 .top-info /deep/ .el-col-22{
    width: calc(100% - 130px);
 }
.top-info{
  border-bottom: 1px solid #eeeeee;
       .tit{
            // margin-right:20px;
            // float:right;
            font-size: 14px;
            }
            .number{
                font-size: 22px;
            }
}


}
</style>
