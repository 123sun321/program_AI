<template>
<div class="maincontent course-page">
      <div class="grid-content">
        <el-form :inline="true" :model="form" ref="form" class="demo-form-inline">
              <el-form-item label="板块名称:" prop="name" fop="like">
            <el-input v-model="form.name" placeholder="按板块名称查询"  prefix-icon="el-icon-search">
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-search" type="primary" @click="getList()">搜索</el-button>
            <el-button plain @click="resetForm('form')">重置</el-button>
          </el-form-item>
          <div class="operation-button">
            <el-button type="primary" @click="addPlate" icon="el-icon-plus" v-permission="'communityPlate:add'">新增</el-button>
            <!-- <el-button type="primary" @click="importCourse" icon="el-icon-upload2" v-permission="'plate:import'">批量导入</el-button>
            <el-button type="primary" @click="exportCourse" icon="el-icon-download" v-permission="'plate:export'">导出</el-button> -->
            <el-button type="danger" @click="batchDeleted" icon="el-icon-delete" v-permission="'communityPlate:del'">批量删除</el-button>
          </div>
        </el-form>
      </div>
      <div class="course-table">
        <el-table ref="multipleTable" :data="courses" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange"
          v-loading="loading">
          <el-table-column type="selection" align="center">
          </el-table-column>
          <el-table-column type="index" width="50">
          </el-table-column>

           <el-table-column prop="name" label="名称" align="center">
          </el-table-column>
          <el-table-column prop="sort"  label="排序" sortable align="center" width="150">

          </el-table-column>

            <el-table-column prop="summary" label="简介" align="center">
          </el-table-column>
          <!-- <el-table-column prop="creationTime" label="添加时间" align="center"></el-table-column> -->
          <el-table-column prop="zip" width="160" label="操作" align="center">
          <template slot-scope="scope">
             <el-button type="primary" size="mini" icon="el-icon-edit" plain @click="editPlate(scope.row.id)" title="编辑" v-permission="'communityPlate:edit'"></el-button>
            <!-- <el-button type="info" size="mini" icon="el-icon-search"  @click="coursebasic(scope.row)" title="课程详情" plain></el-button> -->
            <el-button type="danger" size="mini" icon="el-icon-delete" plain @click="singleDelete(scope.row.id)" title="删除" v-permission="'communityPlate:del'"></el-button>
          </template>
        </el-table-column>
        </el-table>
        <page :page="form.page" @changed="getList"></page>
      </div>
      <!-- 修改课程 -->
      <add-Plate ref="add" @success="getList"></add-Plate>
      <edit-Plate ref="edit" @success="getList"></edit-Plate>

</div>
</template>

<script>
   /**
 * @file 板块管理组件
 * @author  张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/3/28
 */
import addPlate from './addPlate'
import editPlate from './editPlate'
import page from '@/components/page'
import { fetchPage, del } from '@/api/community/platemanage'
import { fetchItems } from '@/api/dictionary'
export default {
  components: {
       addPlate,
       editPlate,
       page
  },
  data() {
       return {
         form: {
           page: {
             current: 1,
             pageCount: 10,
             total: 0
           },
           search: ''
         },
         coursecategory: [],
         courses: [],
         loading: false,
         multipleSelection: [],
         value2: null
       }
  },
  created() {
       this.$nextTick(e => {
         this.init()
         this.getList()
       })
  },
  methods: {
       init() {
         fetchItems(null, 'course_category').then(e => {
           this.coursecategory = e.data.data
         })
       },
       getList() {
         this.loading = true
         const params = this.$formToParams(this.$refs.form)
         fetchPage(params).then(e => {
           const data = e.data.data
           this.courses = data.records
           this.form.page.total = data.total
           this.loading = false
         })
       },
       // 重置搜索条件
       resetForm(formName) {
         this.$refs[formName].resetFields()
         this.getList()
       },
       // 1.1添加课程
       addPlate() {
         this.$refs.add.open()
       },
       editPlate(id) {
         this.$refs.edit.open(id)
       },
       // 5.删除课程单条数据
       deleteCourse() {
         this.$confirm('很重要的信息，你确定删除吗？', '提示', {
           confirmButtonText: '确定',
           cancelButtonText: '取消',
           type: 'warning'
         }).then(_ => {
           this.$message({
             type: 'success',
             message: '删除成功！'
           })
         }).catch(_ => {
           this.$message({
             type: 'info',
             message: '已取消删除'
           })
         })
       },
       // 6.1获取复选框被选中的教师数据
       handleSelectionChange(val) {
         this.multipleSelection = val
         //  console.log(val)
       },
       // 单删
       singleDelete(id) {
         this.del([id])
       },
       // 6.2批量删除选中的社区板块数据
       batchDeleted() {
         if (this.multipleSelection.length === 0) {
           this.$warnmsg('请选择要删除的课程')
           return
         } else {
           const ids = this.multipleSelection.map(b => b.id)
           this.del(ids)
         }
       },
       // 删除方法
       del(ids) {
         this.$cfm('确定删除课程记录吗？', e => {
           del(ids).then(e => {
             if (e.data.success) {
               this.$sucmsg('删除成功')
               this.getList()
             }
           })
         })
       },
       // 参与详情
       coursebasic(item) {
         this.$router.push({ name: 'coursedetail', query: { courseid: item.id }})
       },
       getcategoryname: function(value) {
         var result = this.coursecategory.filter(m => m.code === value)
         if (result.length > 0) { return result[0].name } else { return '-' }
       }
  }
}

</script>
<style scoped>
  p {
    margin: 0px;
  }

  .course-page .el-row {
    margin-bottom: 20px;
  }

  .course-page .el-col {
    border-radius: 4px;
    /* border:1px solid red; */
    margin-left: 20px;
    margin-right: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  .course-page .bg-purple-dark>.el-input {
    width: 20%;
  }

  .course-page-search .el-form-item {
    margin-bottom: 0px;
  }

  .course-page .bg-purple {
    background: #d3dce6;
  }

  .course-page .bg-purple-light {
    background: #e5e9f2;
  }

  .course-page .grid-content {
    border-radius: 4px;
    min-height: 36px;
    /* margin-bottom: 10px; */
  }

  .course-page .grid-content span {
    color: #606266;
    font-size: 14px;
  }

  .course-page .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }

  .course-page .operation-button {
    /* display: inline-block; */
    float: right;
  }

  .el-dialog {
    border-radius: 4px;
  }

  .course-page .el-tooltip {
    cursor: pointer;
    color: #409eff;
  }

  .course-page .profession-pagination {
    margin-top: 20px;
    text-align: right;
  }

</style>

<style>
  .export-table {
    border-right: 1px solid #DCDFE6;
    border-bottom: 1px solid #DCDFE6;
  }

  .export-table td {
    border-left: 1px solid #DCDFE6;
    border-top: 1px solid #DCDFE6;
  }

  .export-table {
    width: 100%;
  }

  .export-table td:first-child {
    width: 20%;
    text-align: center;
  }

  .export-table td:last-child {
    width: 80%;
  }

  .export-table tr:last-child>td:last-child {
    padding: 35px;
  }

  .export-table .upload-demo {
    width: 80%;
    display: inline-block;
  }

  .export-table .el-upload {
    margin-right: 10px;
  }

  .export-table .el-upload-list__item:first-child {
    margin-top: 3px;
  }

  .export-table .upload-demo .el-upload-list {
    border: 1px solid #DCDFE6;
    height: 32px;
    display: inline-block;
    width: 78%;
    vertical-align: top;
  }

</style>
