<template>
    <div class="reportdetail">
      <el-row :gutter="20">
        <el-col :span="18">
            <div class="report-info">
                <div class="top-info">
                    <el-row>
                        <el-col :span="18" class="top-name">
                            <div class="pic" @click="callback"><img  :src="require('@/assets/student/Experiment_return.png')" ></div>  <div class="title">“{{report.exerciseJoinDto.exercise.name}}”实验报告</div>
                        </el-col>
                        <el-col :span="6">
                            <div class="right-info">
                                <span class="click">{{report.clickNumber}} 查看</span>
                                <span class="comment">{{report.commentNumber}} 评论</span>
                            </div>
                        </el-col>
                    </el-row>
                </div>
                <div class="user-info clearfix">
                    <div class="user-logo pull-left">
                        <a @click="touser(report.exerciseJoinDto.student.userId)"><img :src="report.exerciseJoinDto.student.imgUrl" alt=""></a>
                    </div>
                    <a class="user-name pull-left" @click="touser(report.exerciseJoinDto.student.userId)">{{report.exerciseJoinDto.student.name}}</a>
                    <div class="time pull-left">{{report.creationTime}}</div>
                </div>
                <div class="report-body">
                    <div class="report-ctrl"  v-if="report.exerciseJoinDto.student.userId===userId">
                        <el-tooltip class="item" effect="dark" content="继续试验" placement="bottom">
                            <el-button type="success" icon="el-icon-caret-right"></el-button>
                        </el-tooltip>
                        <el-tooltip class="item" effect="dark" content="编辑报告" placement="bottom">
                            <el-button icon="el-icon-edit" @click="editReport=true"></el-button>
                        </el-tooltip>
                    </div>
                    <div class="report-detail" v-show="!editReport">
                        <vue-markdown class="markdown-body" v-highlight :source="report.content"></vue-markdown>
                    </div>
                    <div class="report-edit" v-show="editReport">
                      <el-form ref="form" :model="report" label-width="90px">
                        <el-form-item label-width="0px">
                             <mark-down v-model="report.content"></mark-down>  
                        </el-form-item>
                        <el-form-item label="是否私有：">
                            <el-switch v-model="report.privateReport"></el-switch>
                        </el-form-item>
                        <div class="btn">
                            <el-button icon="el-icon-edit" @click="onSave" type="success">发布</el-button>
                            <el-button icon="el-icon-close" @click="editReport=false">关闭</el-button>
                        </div>
                      </el-form>
                    </div>
                </div>
            </div>
           <report-comment  :reportid="reportId" :init="init"></report-comment>
        </el-col>
        <el-col :span="6">
            <correlation-report v-on:headCallBack="headCall" :reports="list"></correlation-report>
        </el-col>
       </el-row>
    </div>
</template>

<script>
import VueMarkdown from 'vue-markdown'
import markDown from '@/components/markDown'
import { getreport } from '@/api/community/theme'
import { getReport, edit, getCorrelationReport } from '@/api/mycourse/report'
import reportComment from './components/reportComment'
import correlationReport from './components/correlationReport'
import { mapGetters } from 'vuex'
export default {
  components: {
    markDown, reportComment, correlationReport, VueMarkdown // 声明组件
  },
  data() {
    return {
      list: [],
      reports: '',
      editReport: false,
      // 分页和模糊查询报告
      form2: {
        form: {
        },
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        search: ''
      },
      content: undefined,
      reportId: this.$route.query.reportid,
      report: { exerciseJoinDto: { exercise: {}, student: {}}}
    }
  },
  created() {
    this.init()
  },
  // watch: {
  //   '$route'(to, from) {
  //     if (to.query.reportid !== from.query.reportid) {
  //       this.reportId = to.query.reportid
  //       this.init()
  //     }
  //   }
  // },
  computed: {
    ...mapGetters([
      'userId'
    ]),
    isShow() {
      return this.reportId
    }
  },
  methods: {
    // 获取报告数据
    init() {
      getReport(this.reportId).then(e => {
        this.report = e.data.data
        this.initreport(this.report.exerciseJoinDto.exerciseId)
        this.getReport()
      })
    },
    // 获取关联报告
    initreport(exerciseid) {
      getCorrelationReport(exerciseid).then(e => {
        this.correlationreports = e.data.data
      })
    },
    // 获取报告数据
    getReport() {
      this.loading = true
      const params = this.form2
      getreport(params).then(e => {
        const data = e.data.data
        this.reports = data.records
        const len = this.reports.filter(b => b.id !== this.reportId)
        this.list = len.slice(0, 10)
        this.form2.page.total = data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 保存
    onSave() {
      edit(this.report).then(e => {
        if (e.data.success) {
          this.editReport = false
          this.$sucmsg('修改成功！')
        }
      })
    },
    callback() {
      this.$router.go(-1)
    },
    headCall(id) { // 回调方法，接收子组件传的参数
      this.reportId = id
      this.$router.push({ name: 'reportDetail', query: { reportid: id }})
      this.init()
    },
    touser(id) {
      this.$router.push({ path: '/user/info/' + id })
    }
  }
}
</script>

<style lang="scss" scoped>
.top-name{
  display: flex;
flex-wrap: row;
}
.pic{
      cursor: pointer;
      margin-top: 3px;
      padding: 0px 5px;
      margin-left: -15px;
      // margin-top: -13px;
    }
.reportdetail{
    margin: 15px;
    .report-info{
        background-color: #ffffff;
        border-radius: 5px;
        padding: 20px;
        .top-info{
            .title{font-size: 24px;color: #07111b;}
            .right-info{
                text-align: right;
                .click{
                    color: #666666;
                }
                .comment{
                    margin-left: 15px;
                    color: #1fad4e;
                }
            }
        }
        .user-info{
            margin: 30px 0;
            .user-logo{
                img{
                    width: 50px;height: 50px;border-radius: 50%;
                }
            }
            .user-name{
                line-height: 50px;
                margin-left: 15px;
                font-size: 20px;
                color: #eca875;
            }
            .time{
                line-height: 50px;
                font-size: 16px;
                color: #999999;
                margin-left: 20px;
            }
        }
        .report-body{
            padding: 10px 15px;
            background: #fdfefd;
            overflow: hidden;
            border: 1px solid #efefef;
            clear: both;
            .report-ctrl{
                text-align: right;
                padding-bottom: 5px;
                border-bottom: 1px solid #efefef;
                min-height: 40px;
                overflow: hidden;
                .el-button{
                    font-size: 15px;
                }
            }
            .report-detail{
                margin: 30px 20px;
            }
            .report-edit{
                margin-top: 15px;
                .item{margin-top: 10px;}
                .btn{
                    padding: 15px 0;
                }
            }
        }
        .report-show{
            padding: 10px 15px;
        }
    }
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
    }
}
</style>
 