<template>
 <div class="maincontent">
    <el-tabs tab-position="top" @tab-click="tabClick">
    <el-tab-pane label="全部消息"></el-tab-pane>
    <el-tab-pane label="未读消息"><span slot="label"> 未读消息<span class="messageCount" v-if="this.$store.state.user.noReadMsgCount>0">{{this.$store.state.user.noReadMsgCount}}</span></span></el-tab-pane>
    <el-tab-pane label="已读消息"></el-tab-pane>
  </el-tabs>
  <div class="class-table">
          <el-table
            ref="multipleTable"
            :data="tableData3"
            tooltip-effect="dark"
            style="width: 100%"
            >
            <el-table-column
            type="index"
            :index="indexMethod"
            width="50"
            label="序号"
            >
          </el-table-column>
            <el-table-column
              prop="title"
              label="标题"
              align="left"
              >
                <template slot-scope="scope" >
                   <span v-bind:class="{ noRead: !scope.row.readFlag,titleInfo:true }" @click="titleClick(scope.row)">{{ scope.row.title }}</span>
                </template>
            </el-table-column>
            <el-table-column
              prop="receiveType"
              label="类型"
               width="200"
              align="center"
            >
             <template slot-scope="scope">
                   <span v-if="scope.row.receiveType==='2'">通知公告</span>
                   <span v-if="scope.row.receiveType!=='2'">个人消息</span>
            </template>
            </el-table-column>
            <el-table-column
              prop="sendTime"
              label="发送时间"
              width="200"
              align="center"
            >
            </el-table-column>
          </el-table>
          <page :page="form.page" @changed="getList"></page>
      </div>
      <el-dialog
        :title="currentMessage.title"
        :visible.sync="messageReadDialogVisible"
        width="50%"
        @open="dialogOpend"
        >
        <span>{{currentMessage.messageText}}</span>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="messageReadDialogVisible = false">确 定</el-button>
        </span>
     </el-dialog>
 </div>
</template>

<script>
/**
 * @file 个人消息组件
 * @author 张向宇
 * @copyright zhengzhouchuanghzi
 * @since 2025/01/08
 */
import page from '@/components/page'
import { getUserSiteMessage, read } from '@/api/person/siteMessage'
export default {
  components: {
    page
  },
  data() {
    return {
      messageReadDialogVisible: false, // 消息阅读窗口显示状态
      tableData3: [], // 列表数据
      // 分页和模糊查询
      form: {
        form: {
        },
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        search: '',
        orderBy: 'receivetime'
      },
      loading: false, // 加载
      currentMessage: {}
    }
  },
  created() {
    this.$nextTick(e => {
      this.getList()
    })
  },
  watch: {

  },
  computed: {

  },
  methods: {
    indexMethod(index) {
      return index + 1 + this.form.page.pageCount * (this.form.page.current - 1)
    },
    tabClick(e) {
      if (e.label === '未读消息') {
        this.form.form.readflag = 0
      } else if (e.label === '已读消息') {
        this.form.form.readflag = 1
      } else {
        delete this.form.form.readflag
      }
      this.getList()
    },
    getList() {
      this.loading = true
      const params = this.form
      getUserSiteMessage(params).then(e => {
        this.tableData3 = e.data.data.records
        this.form.page.total = parseInt(e.data.data.total)
        this.loading = false
      })
    },
    titleClick(message) {
      this.currentMessage = message
      this.messageReadDialogVisible = true
    },
    dialogOpend() {
      if (this.currentMessage.readFlag === false) {
        read(this.currentMessage.id).then(e => {
          this.currentMessage.readFlag = true
          this.$store.commit('SET_NOREADMSGCOUNT', e.data.data)
        })
      }
    }
  }
}
</script>
<style scoped>
.messageCount{border-radius: 15px;
    background-color: red;
    min-width: 20px;
    height: 20px;
    display: inline-grid;
    margin-top: 10p;
    line-height: 20px;
    text-align: center;
    color: #fff;
    margin-left: 2px;}
.noRead{font-weight: bold;font-size: 14px;}
.titleInfo{cursor: pointer;display: block;}
  p{
    margin:0px;
  }
  .class-page .el-row {
    margin-bottom: 20px;
  }
  .class-page .el-col {
    border-radius: 4px;
    margin-left: 20px;
    margin-right: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  .class-page .bg-purple-dark>.el-input {
    width:20%;
  }
  .class-page .bg-purple {
    background: #d3dce6;
  }
  .class-page .bg-purple-light {
    background: #e5e9f2;
  }
  .class-page .grid-content {
    border-radius: 4px;
    min-height: 36px;
    margin-bottom: 10px;
  }
  .class-page .grid-content span{
    color: #606266;
    font-size: 14px;
  }
  .class-page .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
  .class-page .operation-button{
    /* display: inline-block; */
    float: right;
  }
  .el-dialog{
    border-radius: 4px;
  }
  .class-page .el-tooltip{
     cursor: pointer;
     color:#409eff;
   }
   .class-page .profession-pagination{
     margin-top:20px;
     text-align: right;
   }
</style>
