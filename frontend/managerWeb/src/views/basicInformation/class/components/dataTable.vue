<template>
<div class="student-page">
  <h4 class="dk-font400">
      <i class="dk-mr5">
        <img src="/src/assets/images/icon/u30.png" class="dk-title-icon" alt>
      </i>本班学生
    </h4>
      <div class="grid-content bg-purple-dark">
          <el-form :inline="true" :model="form" ref="form" class="demo-form-inline">
            <el-form-item label="学生:" prop="name" fop="like">
              <el-input v-model="form.name"  placeholder="请输入学生姓名"  prefix-icon="el-icon-search"  >
               </el-input>
            </el-form-item>
            <el-form-item>
               <el-button icon="el-icon-search" type="primary" @click="getList">搜索</el-button>
                <el-button plain @click="resetForm('form')">重置</el-button>
            </el-form-item>
            <!-- <div class="detail">
              <el-button type="info" plain icon="el-icon-document"  @click="detailStudent">查看</el-button>
            </div> -->
            <div class="operation-button">
              <el-button type="primary" @click="addStudent" icon="el-icon-plus">新建</el-button>
              <el-button type="danger" @click="batchDeleted" icon="el-icon-delete">批量删除</el-button>
            </div>
          </el-form>
      </div>

      <div class="student-table">
          <el-table
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
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
            type="index"
            :index="indexMethod"
            width="50"
            label="序号"
             >
            </el-table-column>
            <el-table-column
              prop="code"
              label="学号"
              sortable
              align="center"
              >
            </el-table-column>
            <el-table-column
              prop="userName"
              sortable
              label="登录用户名"
              align="center"
              >
            </el-table-column>
            <el-table-column
              prop="name"
              sortable
              label="姓名"
              align="center"
              >
            </el-table-column>
            <el-table-column  label="性别" align="center" width="75px" sortable>
              <template slot-scope="scope">
                <el-tag
                   :type="scope.row.sex === '男' ? 'primary' : 'danger'"
                   disable-transitions>
                   {{scope.row.sex}}
                </el-tag>
               </template>
            </el-table-column>
             <el-table-column
              prop="email"
              label="邮箱"
              align="center"
              min-width="200px"
              show-overflow-tooltip>
                <template slot-scope="scope">
                  <span>{{scope.row.email}}</span>
                  <el-tag v-if="scope.row.email!=='' && scope.row.emailConfirmed">已绑定</el-tag>
                  <el-tag v-if="scope.row.email!=='' && !scope.row.emailConfirmed">未绑定</el-tag>
               </template>
            </el-table-column>
             <el-table-column
              prop="contact"
              label="手机号"
              align="center"
              min-width="180px"
              show-overflow-tooltip
              >
               <template slot-scope="scope">
                  <span>{{scope.row.contact}}</span>
                  <el-tag v-if="scope.row.contact!=='' && scope.row.phoneNumberConfirmed">已绑定</el-tag>
                  <el-tag v-if="scope.row.contact!=='' && !scope.row.phoneNumberConfirmed">未绑定</el-tag>

               </template>
            </el-table-column>
             <el-table-column
              min-width="280"
              label="操作"
              align="center"
              >
               <template slot-scope="scope">
                <el-button type="primary" size="mini" plain icon="el-icon-edit"  @click="editStudent(scope.row.id)"></el-button>
                <el-button type="danger" size="mini"  plain icon="el-icon-delete"  @click="singleDelete(scope.row.id)"></el-button>
                <el-button type="warning" size="mini" plain @click="resetPassword(scope.row)">重置密码</el-button>
                 <el-button type="info" size="mini" plain @click="untiedPhone(scope.row)" :disabled="scope.row.contact ==='' || !scope.row.phoneNumberConfirmed">解绑手机</el-button>
               </template>
            </el-table-column>
          </el-table>
            <page :page="form.page" @changed="getList"></page>
      </div>
      <add-student ref="add" @success="getList"></add-student>
      <edit-student ref="edit" @success="getList"></edit-student>
</div>
</template>

<script>
/**
 * @file 班级学生组件
 * @author 马勇超
 * @copyright zhengzhouchuanghzi
 * @since 2025/3/8
 */
import addStudent from '../../student/addStudent'
import editStudent from '../../student/editStudent'
import page from '@/components/page'
import {
  pageStudent,
  delBatchStudent,
  exportByIds,
  delSingleStudent,
  resetPass,
  unbindPhone,
  getProfessionWithGrade
} from '@/api/base/student'
export default {
  name: 'student',
  props: ['gridData', 'gcid', 'professionid'],
  components: {
    addStudent,
    editStudent,
    page
  },
  data() {
    return {
      loading: false, // 是否加载
      professions: [], // 所有专业
      grades: [], // 所有年级
      classInfo: [], // 所有班级
      searchForm: {}, // 搜索条件
      tableData3: [], // 列表数据
      multipleSelection: [], // 多选框
      studentVisible: false, // 添加学生弹窗 隐藏状态
      exportVisible: false, // 导入
      fileList: [], // 上传文件列表
      editStudentVisible: false, // 修改
      check: false, // 查看
      // 分页和模糊查询
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        form: {
          gradeId: null
        }
      }
    }
  },
  // 获取学生信息列表
  created() {
    this.$nextTick(e => {
      this.getList()
      if (this.check) {
        this.getProfessionWithGrade()
      }
    })
  },
  methods: {
    // 获取学生序号 index [Int] 返回学生序号
    indexMethod(index) {
      return index + 1 + this.form.page.pageCount * (this.form.page.current - 1)
    },
    getList() {
      this.loading = true
      // 获取所有的学生数据 params [Object] 返回所有的学生数据
      this.$set(this.form.form, 'gradeId', this.gcid)
      pageStudent(this.form).then(e => {
        this.tableData3 = e.data.data.records
        this.form.page.total = parseInt(e.data.data.total)
        this.loading = false
      })
      // 根据专业获取班级
      getProfessionWithGrade().then(response => {
        this.professions = response.data.data
        for (let i = 0; i < this.professions.length; i++) {
          if (this.professions[i].id === this.form.professionId) {
            this.classInfo = this.professions[i].gradeList
          }
        }
      })
    },
    singleDelete(id) {
      this.delete([id])
    },
    // 添加学生
    addStudent() {
      this.$refs.add.openDetail(this.professionid, this.gcid)
    },
    // 批量导入学生
    importStudent() {
      this.$refs.imp.open()
    },
    // 实现导入功能
    onImportSuccess(e) {
      if (e) {
        this.getList()
      }
    },
    // 导出学生数据
    exportStudent() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要导出的学生')
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

    // 删除学生单条数据
    deleteClass(index) {
      this.$confirm('很重要的信息，你确定删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        delSingleStudent(index).then(e => {
          if (e.data) {
            this.tableData3.splice(index, 1)
            this.$message({
              type: 'success',
              message: '删除成功！'
            })
            this.getList()
          } else {
            this.$message({
              type: 'error',
              message: '对应班级下还有学生数据，删除失败！'
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
    // 获取复选框被选中的学生数据
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 批量删除选中的学生数据
    batchDeleted() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要删除的学生')
        return
      } else {
        const ids = this.multipleSelection.map(b => b.id)
        this.delete(ids)
      }
    },
    // 删除方法
    delete(ids) {
      this.$cfm('确定删除学生记录吗？', e => {
        delBatchStudent(ids).then(e => {
          if (e.data.data) {
            this.$sucmsg('删除成功')
            this.getList()
          }
        })
      })
    },
    // 重置搜索条件
    resetForm() {
      this.$refs.form.resetFields()
      this.form.professionId = ''
      this.classInfo = []
      this.getList()
    },
    // 修改学生信息
    editStudent(id) {
      this.$refs.edit.openDetail(id)
    },
    detailStudent() {
      this.$router.push('detailStudent')
    },
    // 重置密码
    resetPassword(student) {
      this.$confirm('您确定要重置' + student.name + '的密码为【000000】吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        resetPass(student.id).then(response => {
          this.$message({
            type: 'success',
            message: '重置成功！'
          })
          this.getList()
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消重置'
        })
      })
    },
    untiedPhone(student) {
      this.$confirm('您确定要解绑' + student.name + '的手机吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        unbindPhone(student.id).then(response => {
          this.$message({
            type: 'success',
            message: '解绑成功！'
          })
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
  .student-page .el-row {
    margin-bottom: 20px;
  }
  .student-page .el-col {
    border-radius: 4px;
    /* border:1px solid red; */
    margin-left: 20px;
    margin-right: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  .student-page .bg-purple-dark>.el-input {
    width:20%;
  }
  .student-page-search .el-form-item{
        margin-bottom: 0px;
  }
  .student-page-search .search-left{
    margin-bottom: 10px;
  }
  .student-page .bg-purple {
    background: #d3dce6;
  }
  .student-page .bg-purple-light {
    background: #e5e9f2;
  }
  .student-page .grid-content {
    border-radius: 4px;
    min-height: 36px;
    margin-bottom: 10px;
  }
  .student-page .grid-content span{
    color: #606266;
    font-size: 14px;
  }
  .student-page .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
  .student-page .operation-button{
    /* display: inline-block; */
    float: right;
  }
  .el-dialog{
    border-radius: 4px;
  }
  .student-page .el-tooltip{
     cursor: pointer;
     color:#409eff;
   }
   .student-page .profession-pagination{
     margin-top:20px;
     text-align: right;
   }
</style>

<style scoped lang="scss">


 .export-table{
     border-right: 1px solid #DCDFE6;
     border-bottom: 1px solid #DCDFE6;
   }
   .export-table td{
     border-left: 1px solid #DCDFE6;
     border-top: 1px solid #DCDFE6;
   }
   .export-table{
     width: 100%;
   }
   .export-table td:first-child{
     width: 20%;
     text-align: center;
   }
   .export-table td:last-child{
     width: 80%;
    }
    .export-table tr:last-child>td:last-child{
      padding: 35px;
    }
   .export-table .upload-demo{
      width: 80%;
      display: inline-block;
    }

    .export-table .el-upload{
      margin-right: 10px;
    }

    .export-table  .el-upload-list__item:first-child{
      margin-top:3px;
    }
    .export-table .upload-demo .el-upload-list{
      border:1px solid #DCDFE6;
      height: 32px;
      display: inline-block;
      width: 78%;
      vertical-align: top;
    }
    .detail{
      float: right;
      // margin-top: 40px;
      margin-left: 20px;
      // margin-bottom: 10px;
    }


</style>

