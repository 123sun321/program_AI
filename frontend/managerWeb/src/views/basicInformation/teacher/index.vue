<template>
<div class="maincontent teacher-page">
      <div class="grid-content bg-purple-dark">
        <!-- <div  class="teacher-page-search"> -->
          <el-form :inline="true"  :model="form" ref="form" class="demo-form-inline">
            <el-form-item label="教师:" prop="name" fop="like">
              <el-input v-model="form.name"  placeholder="请输入教师姓名"  prefix-icon="el-icon-search"  >
               </el-input>
            </el-form-item>
            <el-form-item>
               <el-button icon="el-icon-search" type="primary" @click="getList()">搜索</el-button>
                <el-button plain @click="resetForm('form')">重置</el-button>
            </el-form-item>
             <div class="operation-button">
                <el-button type="primary" @click="addTeacher" icon="el-icon-plus" v-permission="'teacher:add'">新建</el-button>
                <el-button type="info" @click="importTeacher" icon="el-icon-upload2" v-permission="'teacher:import'">批量导入</el-button>
                <el-button type="success" @click="exportTeacher" icon="el-icon-download" v-permission="'teacher:export'">导出</el-button>
                <el-button type="danger" @click="batchDeleted" icon="el-icon-delete" v-permission="'teacher:del'">批量删除</el-button>
            </div>
          </el-form>
      </div>
      <div class="teacher-table" >
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
              prop="code"
              sortable
              label="工号"
              align="center"
              >
            </el-table-column>
            <el-table-column
              prop="name"
              label="姓名"
              sortable
              align="center"
              >
            </el-table-column>
            <el-table-column
              label="性别"
              sortable
              align="center"
              >
               <template slot-scope="scope">
                   <el-tag
                   :type="scope.row.sex === '男' ? 'primary' : 'danger'"
                   disable-transitions>{{scope.row.sex}}</el-tag>
               </template>
            </el-table-column>
             <el-table-column
              width="200px"
              prop="email"
              label="邮箱"
              align="center"
              >
              <template slot-scope="scope">
                  <span>{{scope.row.email}}</span>
                  <el-tag style="float:right;" v-if="scope.row.email!=='' && scope.row.emailConfirmed">已绑定</el-tag>
                  <el-tag style="float:right;" v-if="scope.row.email!=='' && !scope.row.emailConfirmed">未绑定</el-tag>
               </template>
            </el-table-column>
             <el-table-column
              width="200px"
              prop="contact"
              label="手机号"
              align="center"
              >
              <template slot-scope="scope">
                  <span>{{scope.row.contact}}</span>
                  <el-tag style="float:right;" v-if="scope.row.contact!=='' && scope.row.phoneNumberConfirmed">已绑定</el-tag>
                  <el-tag style="float:right;" v-if="scope.row.contact!=='' && !scope.row.phoneNumberConfirmed">未绑定</el-tag>
               </template>
            </el-table-column>
             <el-table-column
             min-width="200"
              label="操作"
              align="center"
              >
               <template slot-scope="scope">
                <el-button type="primary" size="mini" icon="el-icon-edit" v-permission="'teacher:edit'" plain @click="editTeacher(scope.row.id)"></el-button>
                <el-button type="danger" size="mini" icon="el-icon-delete" v-permission="'teacher:del'" plain @click="deleteTeacher(scope.$index)"></el-button>
                <el-button type="warning" size="mini"  plain @click="resetPassword(scope.row)">重置密码</el-button>
                <el-button type="info" size="mini" plain @click="untiedPhone(scope.row)" :disabled="scope.row.contact === '' || !scope.row.phoneNumberConfirmed ">解绑手机</el-button>
               </template>
            </el-table-column>
          </el-table>
           <page :page="form.page" @changed="getList"></page>
          </div>
         <add-teacher ref="add" @success="getList"></add-teacher>
      <edit-teacher ref="edit" @success="getList"></edit-teacher>
       <import-data ref="imp" @success="onImportSuccess"></import-data>

</div>
</template>

<script>
/*
教师管理首页
@author 袁延延
@date 2025/11/29 16:30
@version 1.0
*/
import addTeacher from './addTeacher'
import editTeacher from './editTeacher'
import importData from './importData'
import page from '@/components/page'
import { search, deletePro, del, exportByIds, resetPass, unbindPhone } from '@/api/base/teacher'
export default {
  components: {
    addTeacher,
    editTeacher,
    importData,
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
        search: '',
        orderBy: 'a.creation_time desc'
      },
      tableData3: [], // 所有教师数据
      professions: [], // 所有专业
      multipleSelection: [], // 获取复选框被选中的
      teacherVisible: false, // 添加教师弹窗 隐藏状态
      loading: false
    }
  },
  // 获取教师数据
  created() {
    // if (this.$route.query.departmentId) {
    //   this.$set(this.form, 'departmentId', this.$route.query.departmentId)
    // }
    this.$nextTick(e => {
      this.getList()
    })
  },
  methods: {
    // 获取教师数据
    getList() {
      this.loading = true
      const params = this.$formToParams(this.$refs.form)
      search(params).then(e => {
        const data = e.data.data
        this.tableData3 = data.records
        this.form.page.total = data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 获取教师序号 index [Int] 返回教师序号
    indexMethod(index) {
      return index + 1 + this.form.page.pageCount * (this.form.page.current - 1)
    },
    // 添加教师
    addTeacher() {
      this.$refs.add.open()
    },
    // 批量导入教师
    importTeacher() {
      this.$refs.imp.open()
    },
    // 实现导入功能
    onImportSuccess(e) {
      if (e) {
        this.getList()
      }
    },
    // 导出教师数据
    exportTeacher() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要导出的教师')
        return
      } else {
        const ids = this.multipleSelection.map(b => b.id)
        exportByIds(ids).then(e => {
          const blob = new Blob([e.data], {
            type: 'application/vnd.ms-excel'
          })
          const blobUrl = URL.createObjectURL(blob)
          window.open(blobUrl)
        })
      }
    },
    // 删除教师单条数据
    deleteTeacher(index) {
      this.$confirm('很重要的信息，你确定删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        deletePro(this.tableData3[index].id).then(e => {
          if (e.data.data) {
            this.getList()
            this.form.page.total--
            this.$message({
              type: 'success',
              message: '删除成功！'
            })
          } else {
            this.$message({
              type: 'error',
              message: '删除失败[本教师已创建学生]！'
            })
          }
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 获取复选框被选中的教师数据
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 批量删除选中的教师数据
    batchDeleted() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要删除的教师')
        return
      } else {
        const ids = this.multipleSelection.map(b => b.id)
        this.delete(ids)
      }
    },
    // 删除方法
    delete(ids) {
      this.$cfm('确定删除教师记录吗？', e => {
        del(ids).then(e => {
          if (e.data.data) {
            this.$sucmsg('删除成功')
            this.getList()
          } else {
            this.$errmsg('删除失败')
          }
        })
      })
    },
    // 重置搜索条件
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.getList()
    },
    // 修改教师信息
    editTeacher(id) {
      this.$refs.edit.open(id)
    },
    // 重置密码
    resetPassword(teacher) {
      this.$confirm('您确定将' + teacher.name + '的密码重置为【000000】吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        resetPass(teacher.id).then(response => {
          this.$message({
            type: 'success',
            message: '重置成功！'
          })
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消重置'
        })
      })
    },
    untiedPhone(teacher) {
      this.$confirm('您确定要解绑' + teacher.name + '的手机吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        unbindPhone(teacher.id).then(response => {
          this.$message({
            type: 'success',
            message: '解绑成功！'
          })
          this.getList()
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消解绑'
        })
      })
    }
  }
}
</script>
<style scoped>
  p{
    margin:0px;
  }
  .teacher-page .el-row {
    margin-bottom: 20px;
  }
  .teacher-page .el-col {
    border-radius: 4px;
    /* border:1px solid red; */
    margin-left: 20px;
    margin-right: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  .teacher-page .bg-purple-dark>.el-input {
    width:20%;
  }
  .teacher-page-search .el-form-item{
        margin-bottom: 0px;
  }
  .teacher-page .bg-purple {
    background: #d3dce6;
  }
  .teacher-page .grid-content {
    border-radius: 4px;
    min-height: 36px;
    margin-bottom: 10px;
  }
  .teacher-page .grid-content span{
    color: #606266;
    font-size: 14px;
  }
  .teacher-page .operation-button{
    /* display: inline-block; */
    float: right;
  }
</style>

