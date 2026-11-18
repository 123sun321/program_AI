<template>
<div class="maincontent class-page">
      <div class="grid-content bg-purple-dark">
          <el-form :inline="true" :model="form" ref="form" class="demo-form-inline pull-left">
            <div class="search-left">
              <el-form-item label="专业:"  prop="professionId" >
                <el-select v-model="form.professionId" filterable placeholder="全部">
                  <el-option  v-for="(item,index) in professions" :key="index" :label="item.name"  :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="年级:" prop="year">
                <el-select v-model="form.year" filterable placeholder="请选择">
                  <el-option  v-for="(item,index) in grades" :key="index" :label="item.name"  :value="item.code">
                  </el-option>
                </el-select>
              </el-form-item>
            </div>
            <el-form-item label="班级:" prop="name" fop="like">
               <el-input  v-model="form.name"  placeholder="按班级名称查询"  prefix-icon="el-icon-search"        >
              </el-input>
            </el-form-item>
            <el-form-item label="导员:" prop="counsellor" fop="like">
              <el-input v-model="form.counsellor"  placeholder="按辅导员名称查询"  prefix-icon="el-icon-search"  >
               </el-input>
            </el-form-item>
            <el-form-item>
               <el-button icon="el-icon-search" @click="getList()" type="primary" >搜索</el-button>
                <el-button plain @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
          <div class="operation-button pull-right">
            <el-button type="primary" @click="addClass" icon="el-icon-plus">新建</el-button>
            <el-button type="info" @click="importClass" icon="el-icon-upload2" >批量导入</el-button>
            <el-button type="success" @click="exportClass" icon="el-icon-download">导出</el-button>
            <el-button type="danger" @click="batchDeleted" icon="el-icon-delete">批量删除</el-button>
          </div>
      </div>
     
      <div class="class-table">
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
              prop="name"
              label="班级名称"
              align="center"
              >
            </el-table-column>
            <el-table-column
              prop="profession.name"
              label="专业名称"
              align="center"
            >
            </el-table-column>
             <el-table-column
              prop="grade"
              label="年级"
              align="center"
              show-overflow-tooltip>
            </el-table-column>
             <el-table-column
              prop="limit"
              label="学制"
              align="center"
              show-overflow-tooltip>
            </el-table-column>
             <el-table-column
              prop="counsellor"
              label="导员"
              align="center"
              >
            </el-table-column>
             <el-table-column
              label="操作"
              align="center" width="200">
               <template slot-scope="scope">
                     <router-link :to="'/basicInformation/class/classDetail/'+scope.row.professionId+'/'+scope.row.id">
              <el-button type="info" size="mini" icon="el-icon-search" plain></el-button>&nbsp;&nbsp;&nbsp;
              </router-link>
                <el-button type="primary" size="mini" icon="el-icon-edit" plain @click="editClass(scope.row.id)"></el-button>
                <el-button type="danger" size="mini" icon="el-icon-delete" plain @click="(deleteClass(scope.row.id))"></el-button>
               </template>
            </el-table-column>
          </el-table>
          <page :page="form.page" @changed="getList"></page>
      </div>
    <!--修改班级-->
      <add-class ref="add" @success="getList"></add-class>
      <edit-class ref="edit" @success="getList"></edit-class>
      <import-data ref="imp" @success="onImportSuccess"></import-data>
</div>
</template>

<script>
/**
 * @file 班级管理组件
 * @author 赵雪丽
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/06
 */
import addClass from './addClass'
import editClass from './editClass'
import importData from './importData'
import page from '@/components/page'
import {
  pageClass,
  delBatchClass,
  exportByIds,
  deleteSingleClass,
  fetchGrades,
  getAllProfession
} from '@/api/base/class'
export default {
  name: 'class',
  components: {
    addClass,
    editClass,
    importData,
    page
  },
  data() {
    return {
      professions: [], // 所有专业
      grades: [], // 所有年级
      classStatus: [], // 在校状态
      limits: [], // 学制
      tableData3: [], // 班级信息
      multipleSelection: [], // 全选
      // 分页和模糊查询
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        search: ''
      },
      loading: false, // 加载
      check: false, // 查看
      professionId: null
    }
  },
  // 获取列表班级数据
  created() {
    if (this.$route.query.professionId) {
      this.$set(this.form, 'professionId', this.$route.query.professionId)
      this.check = true
    }
    this.$nextTick(e => {
      this.getList()
      this.getAllProfession()
    })
    if (this.grades.length === 0) {
      this.getGrades()
    }
  },
  // 监测专业
  watch: {
    newVal(val, oldVal) {
      if (val) {
        // this.getAllProfession()
        this.$set(this.form, 'professionId', this.$route.query.professionId)

        if (this.check === false) {
          this.$set(this.form, 'professionId', undefined)
        } else {
          this.check = false
        }
      }
    }
  },
  // 监测专业是否改变，返回专业的Id
  computed: {
    newVal() {
      // return this.form.professionId
    }
  },
  methods: {
    // 获取班级序号 index [Int] 返回班级序号
    indexMethod(index) {
      return index + 1 + this.form.page.pageCount * (this.form.page.current - 1)
    },
    // 获取专业
    getAllProfession() {
      getAllProfession().then(e => {
        this.professions = e.data.data
      })
    },
    getList() {
      this.loading = true
      const params = this.$formToParams(this.$refs.form)
      // 获取所有的班级数据 params [Object] 返回所有的班级数据
      pageClass(params).then(e => {
        this.tableData3 = e.data.data.records
        for (let i = 0; i < this.tableData3.length; i++) {
          this.tableData3[i].grade = this.tableData3[i].year + '级'
          this.tableData3[i].limit = this.tableData3[i].schoolLength + '年制'
        }
        this.form.page.total = parseInt(e.data.data.total)
        this.loading = false
      })
    },
    // 新建班级
    addClass() {
      this.$refs.add.open()
    },
    // 批量导入班级
    importClass() {
      this.$refs.imp.open()
    },
    // 导出班级数据
    exportClass() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要导出的班级')
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
    // 删除班级单条数据
    deleteClass(index) {
      this.$confirm('很重要的信息，你确定删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        deleteSingleClass(index).then(e => {
          if (e.data.data) {
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

    // 获取复选框被选中的班级数据
    handleSelectionChange(val) {
      this.multipleSelection = val
      console.log(val)
    },
    // 批量删除选中的班级数据
    batchDeleted() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要删除的班级')
        return
      } else {
        const ids = this.multipleSelection.map(b => b.id)
        this.delete(ids)
      }
    },
    // 删除方法
    delete(ids) {
      this.$cfm('确定删除班级记录吗？', e => {
        delBatchClass(ids).then(e => {
          if (e.data.data) {
            this.$sucmsg('删除成功')
            this.getList()
          } else {
            this.$errmsg('对应班级下还有学生数据，删除失败')
            this.getList()
          }
        })
      })
    },
    // 重置搜索条件
    resetForm() {
      this.$refs.form.resetFields()
      this.form.professionId = ''
      this.getList()
    },
    // 修改班级信息
    editClass(id) {
      this.$refs.edit.open(id)
    },
    // 实现导入功能
    onImportSuccess(e) {
      if (e) {
        this.getList()
      }
    },
    // 获取年级
    getGrades() {
      fetchGrades().then(e => {
        this.grades = e.data.data
      })
    }
  }
}
</script>
<style scoped>
  p{
    margin:0px;
  }
  .class-page .el-row {
    margin-bottom: 20px;
  }
  .class-page .el-col {
    border-radius: 4px;
    /* border:1px solid red; */
    margin-left: 20px;
    margin-right: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  /* .class-page .bg-purple-dark {
    background: #99a9bf;
  } */
  .class-page .bg-purple-dark>.el-input {
    width:20%;
  }
  /* .class-page-search .el-form-item{
     margin-bottom: 0px;
  }
  .class-page-search .search-left{
    margin-bottom: 10px;
  } */
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

<style>
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

</style>

