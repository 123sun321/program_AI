<template>
  <div class="maincontent profession-page">
      <div class="grid-content bg-purple-dark">
        <el-form :inline="true" :model="form" ref="form" class="demo-form-inline">
          <el-form-item label="专业:" prop="name" fop="like">
            <el-input v-model="form.name"  placeholder="按专业名称查询" prefix-icon="el-icon-search">
            </el-input>
          </el-form-item>
          <el-form-item>
             <el-button icon="el-icon-search" type="primary" @click="getList()">搜索</el-button>
                <el-button plain @click="resetForm('form')">重置</el-button>
          </el-form-item>
           <div class="operation-button">
               <el-button type="primary" @click="addProfession" icon="el-icon-plus" v-permission="'profession:add'">新建</el-button>
                <el-button type="info" @click="importProfession" icon="el-icon-upload2" v-permission="'profession:import'">批量导入</el-button>
                <el-button type="success" @click="exportProfession" icon="el-icon-download" v-permission="'profession:export'">导出</el-button>
                <el-button type="danger" @click="batchDeleted" icon="el-icon-delete" v-permission="'profession:del'">批量删除</el-button>
          </div>
        </el-form>
      </div>
      <div class="profession-table">
          <el-table
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            ref="multipleTable"
            :data="tableData"
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
              label="序号"
              align="center"
             >
            </el-table-column>
            <el-table-column
              prop="name"
              label="专业名称"
              align="center"
              >
            </el-table-column>
             <el-table-column

              label="下设班级"
              align="center"
              show-overflow-tooltip>
              <template slot-scope="scope">
              <!-- <router-link style="color:#409EFF;" :to="'/basicInformation/class?professionId='+scope.row.id">查看</router-link> -->
              <el-button type="text" size="small" @click="onClass(scope.row)">查看</el-button>
              </template>
            </el-table-column>
             <el-table-column
              prop="studentId"
              label="下辖学生"
              align="center"
              show-overflow-tooltip>
               <template slot-scope="scope">
              <!-- <router-link style="color:#409EFF;" :to="'/basicInformation/student?professionId='+scope.row.id">查看</router-link> -->
              <el-button type="text" size="small" @click="onStudent(scope.row)">查看</el-button>
              </template>
            </el-table-column>
             <el-table-column
              prop="creationTime"
              label="创建时间"
              align="center"
              >
            </el-table-column>
             <el-table-column
              label="操作"
              align="center" width="120"
              >
               <template slot-scope="scope">
                <el-button type="primary" size="mini" icon="el-icon-edit" v-permission="'profession:edit'" plain @click="editProfession(scope.row.id)"></el-button>
                <el-button type="danger" size="mini" icon="el-icon-delete" v-permission="'profession:del'" plain @click="deleteProfession(scope.$index)"></el-button>
               </template>
            </el-table-column>
          </el-table>
           <page :page="form.page" @changed="getList"></page>
      </div>
      <!-- 修改专业 -->
      <add-profession ref="add" @success="getList"></add-profession>
      <edit-profession ref="edit" @success="getList"></edit-profession>
       <import-data  ref="imp" @success="onImportSuccess"></import-data>
  </div>
</template>

<script>
/*
专业管理首页
@author 袁延延
@date 2025/11/26 13:30
@version 1.0
*/
import addProfession from './addProfession'
import editProfession from './editProfession'
import importData from './importData'
import page from '@/components/page'
import { searchProfession, deletePro, del, exportByIds } from '@/api/base/professions'
export default {
  components: {
    addProfession,
    editProfession,
    importData,
    page
  },
  name: 'profession',
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
      tableData: [], // 专业信息
      loading: false, // 加载
      multipleSelection: [] // 获取复选框被选中的
    }
  },
  // 获取专业数据
  created() {
    // if (this.$route.query.departmentId) {
    //   this.$set(this.form, 'departmentsId', this.$route.query.departmentId)
    // }
    this.$nextTick(e => {
      this.getList()
    })
  },
  methods: {
    // 获取专业数据
    getList() {
      this.loading = true
      const params = this.$formToParams(this.$refs.form)
      searchProfession(params).then(e => {
        const data = e.data.data
        this.tableData = data.records
        this.form.page.total = data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    onClass(row) {
      this.$router.push({ path: '/basicInformation/class', query: { professionId: row.id }})
    },
    onStudent(row) {
      this.$router.push({ path: '/basicInformation/student/index', query: { professionId: row.id }})
    },
    // 获取专业序号 index [Int] 返回专业序号
    indexMethod(index) {
      return index + 1 + this.form.page.pageCount * (this.form.page.current - 1)
    },
    // 新建专业
    addProfession() {
      this.$refs.add.open()
    },
    // 批量导入专业
    importProfession() {
      this.$refs.imp.open()
    },
    // 实现导入功能
    onImportSuccess(e) {
      if (e) {
        this.getList()
      }
    },
    // 重置搜索条件
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.getList()
    },
    // 导出专业数据
    exportProfession() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要导出的专业')
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
    // 删除专业单条数据
    deleteProfession(index) {
      this.$confirm('很重要的信息，你确定删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        deletePro(this.tableData[index].id).then(e => {
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
              message: '删除失败[本专业已创建班级]！'
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
    // 获取复选框被选中的专业数据
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 批量删除选中的专业数据
    batchDeleted() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要删除的专业')
        return
      } else {
        const ids = this.multipleSelection.map(b => b.id)
        this.delete(ids)
      }
    },
    // 删除方法
    delete(ids) {
      this.$cfm('确定删除专业记录吗？', e => {
        del(ids).then(e => {
          if (e.data.data) {
            this.$sucmsg('删除成功')
            this.getList()
          } else {
            this.$errmsg('删除失败[某专业已创建班级]！')
          }
        })
      })
    },
    // 修改专业信息
    editProfession(id) {
      this.$refs.edit.open(id)
    }
  }
}
</script>

<style scoped>
  p{
    margin:0px;
  }
  .profession-page .el-row {
    margin-bottom: 20px;
  }
  .profession-page .el-col {
    border-radius: 4px;
    /* border:1px solid red; */
    margin:20px;
  }
  .profession-page .bg-purple-dark>.el-input {
    width:20%;
  }
  .profession-page .bg-purple {
    background: #d3dce6;
  }
  .profession-page .bg-purple-light {
    background: #e5e9f2;
  }
  .profession-page .grid-content {
    border-radius: 4px;
    min-height: 36px;
    margin-bottom: 10px;
  }
  .profession-page .operation-button{
    /* display: inline-block; */
    float: right;
  }
</style>
