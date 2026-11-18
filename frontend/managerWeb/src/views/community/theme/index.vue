<template>
<div class="maincontent theme-page">
      <div class="grid-content bg-purple-dark">
          <el-form :inline="true"  :model="form" ref="form" class="demo-form-inline">
            <el-form-item label="标题:" prop="title" fop="like">
              <el-input v-model="form.title"  placeholder="请输入标题"  prefix-icon="el-icon-search"  >
               </el-input>
            </el-form-item>
            <el-form-item>
               <el-button icon="el-icon-search" type="primary" @click="getList()">搜索</el-button>
                <el-button plain @click="resetForm('form')">重置</el-button>
            </el-form-item>
             <div class="operation-button">
                <el-button type="primary" @click="addTheme" icon="el-icon-plus" plain>发帖</el-button>
                <el-button type="danger" @click="batchDeleted" icon="el-icon-delete" >批量删除</el-button>
            </div>
          </el-form>
      </div>
      <div class="theme-table" >
          <el-table
          v-loading="loading"
            ref="multipleTable"
            :data="tableData3"
            tooltip-effect="dark"
            style="width: 100%"
            @selection-change="handleSelectionChange">
            <el-table-column
              type="selection"
              align="center"
              >
            </el-table-column>
            <el-table-column
            min-width="55"
              type="index"
              :index="indexMethod"
              label="序号"
              align="center"
             >
            </el-table-column>
            <el-table-column
              min-width="150"
              prop="title"
              label="标题"
              align="center"
              >
            </el-table-column>
            <!-- <el-table-column
              min-width="200"
              prop="content"
              label="简介"
              align="center"
              >
            </el-table-column> -->
            <el-table-column
              prop="click"
              label="查看次数"
              align="center" sortable
              >
            </el-table-column>
             <el-table-column
              prop="commentNumber"
              label="回复人数"
              align="center" sortable
              >
              <template slot-scope="scope">
              <!-- <router-link style="color:#409EFF;" :to="'/basicInformation/student?professionId='+scope.row.id">查看</router-link> -->
              <el-button type="text" size="small" @click="onTheme(scope.row.id)">{{scope.row.commentNumber}}</el-button>
              </template>
            </el-table-column>
             <el-table-column
              prop="creationTime"
              label="创建时间"
              align="center"
              >
            </el-table-column>

             <el-table-column
             min-width="150"
              label="操作"
              align="center"
              >
               <template slot-scope="scope">
                <el-button type="primary" size="mini" icon="el-icon-edit"  plain @click="editTheme(scope.row.id)" title="编辑" v-permission="'communityPlate:edit'"></el-button>
                <el-button type="danger" size="mini" icon="el-icon-delete"  plain @click="del(scope.row)" title="删除" v-permission="'communityPlate:del'"></el-button>
               </template>
            </el-table-column>
          </el-table>
           <page :page="form.page" @changed="getList"></page>
          </div>
         <add-Theme ref="add" @success="getList"></add-Theme>
       <edit-Theme ref="edit" @success="getList"></edit-Theme>

</div>
</template>

<script>
/*
主题管理首页
@author 张浩杰
@date 2025/03/29 16:30
@version 1.0
*/
import addTheme from './addTheme'
import editTheme from './editTheme'
import page from '@/components/page'
// 引入接口方法
import { findPage, del } from '@/api/community/theme'
export default {
  components: {
    addTheme,
    editTheme,
    page
  },
  data() {
    return {
      // 分页和模糊查询
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        search: ''
      },
      tableData3: [], // 所有主题数据

      multipleSelection: [], // 获取复选框被选中的
      teacherVisible: false, // 添加帖子弹窗 隐藏状态
      loading: false
    }
  },
  // 获取帖子数据
  created() {
    // if (this.$route.query.departmentId) {
    //   this.$set(this.form, 'departmentId', this.$route.query.departmentId)
    // }
    this.$nextTick(e => {
      this.getList()
    })
  },
  methods: {
    // 获取帖子数据
    getList() {
      this.loading = true
      const params = this.$formToParams(this.$refs.form)
      findPage(params).then(e => {
        const data = e.data.data
        this.tableData3 = data.records
        this.form.page.total = data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 获取帖子序号 index [Int] 返回帖子序号
    indexMethod(index) {
      return index + 1 + this.form.page.pageCount * (this.form.page.current - 1)
    },
    // 添加帖子
    addTheme() {
      this.$refs.add.open()
    },
    // 批量删除-------------------------
    batchDeleted() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择需要删除的主题！')
        return
      } else {
        const ids = this.multipleSelection.map(b => b.id)
        this.delete(ids, '确定删除选中的主题吗？')
      }
    },
    del(row) {
      this.delete([row.id], '确定删除该主题吗？')
    },
    delete(ids, title) {
      this.$cfm(title, e => {
        del(ids).then(e => {
          if (e.data.success) {
            this.$sucmsg('删除成功')
            this.getList()
          } else {
            this.$errmsg('删除失败')
          }
        })
      })
    },
    // 删除结束-------------------------

    // 获取复选框被选中的帖子信息
    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    // 重置搜索条件
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.getList()
    },
    // 修改帖子信息
    editTheme(id) {
      this.$refs.edit.open(id)
    },
    onTheme(id) {
      // this.$router.push('replyTheme')
      this.$router.push({ path: 'replyTheme', query: { link_id: id }})
    }

  }
}
</script>
<style scoped>
  p{
    margin:0px;
  }
  .theme-page .el-row {
    margin-bottom: 20px;
  }
  .theme-page .el-col {
    border-radius: 4px;
    /* border:1px solid red; */
    margin-left: 20px;
    margin-right: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  .theme-page .bg-purple-dark>.el-input {
    width:20%;
  }
  .theme-page-search .el-form-item{
        margin-bottom: 0px;
  }
  .theme-page .bg-purple {
    background: #d3dce6;
  }
  .theme-page .grid-content {
    border-radius: 4px;
    min-height: 36px;
    margin-bottom: 10px;
  }
  .theme-page .grid-content span{
    color: #606266;
    font-size: 14px;
  }
  .theme-page .operation-button{
    /* display: inline-block; */
    float: right;
  }
</style>

