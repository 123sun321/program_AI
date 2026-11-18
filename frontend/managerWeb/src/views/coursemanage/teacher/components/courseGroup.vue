<template>
<div class="studyDetail-page">
      <div class="grid-content bg-purple-dark">
          <el-form :inline="true" :model="form" ref="form" class="demo-form-inline pull-left">
            <div class="search-left">
            </div>
          </el-form>
          <div class="operation-button pull-right">
            <el-button type="primary" @click="add" icon="el-icon-plus">新建</el-button>
          </div>
      </div>
      <div class="course-table">
        <el-table
          :data="tableData"
          border
          style="width: 100%">
          <el-table-column type="index"  label="序号"></el-table-column>
         <el-table-column
           prop="name"
           label="名称">
         </el-table-column>
         <el-table-column
           prop="creationTime"
           label="创建时间">
         </el-table-column>
         <el-table-column prop="zip" label="操作" align="center">
            <template slot-scope="scope">
                <el-button type="primary" size="mini" icon="el-icon-edit" plain @click="edit(scope.row.id)"></el-button>
                <el-button type="danger" size="mini" icon="el-icon-delete" plain @click="del(scope.row.id)"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <page :page="form.page" @changed="getList" style="margin-top:20px;"></page>
             <!-- 修改课程 -->
      <add ref="add" :courseId="courseid" @success="getList"></add>
      <edit ref="edit" @success="getList"></edit>
</div>
</template>
<script>
   /**
 * @file 课程小组
 * @author  Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/7/5
 */
import add from './group/add'
import edit from './group/edit'
import page from '@/components/page'
import { pageList, del } from '@/api/coursemanage/courseGroup'

export default {
     props: ['courseid'],
     components: {
       page, edit, add
     },
     data() {
       return {
         form: {
           page: {
             current: 1,
             pageCount: 10,
             total: 0
           },
           search: '',
           orderBy: 'creation_time desc'
         },
         tableData: [], // 数据列表
         loading: false,
         multipleSelection: []
       }
     },
     created() {
       this.$nextTick(e => {
         this.getList()
       })
     },
     methods: {
       getList() {
         this.$emit('getCourseGroupList')
         this.loading = true
         const params = this.$formToParams(this.$refs.form)
         params.form.course_id = this.courseid
         pageList(params).then(e => {
           const data = e.data.data
           this.tableData = data.records
           this.form.page.total = data.total
           this.loading = false
         })
       },
       add() {
         this.$refs.add.open()
       },
       edit(id) {
         this.$refs.edit.open(id)
       },
       // 删除方法
       del(id) {
         this.$cfm('确定删除课程小组吗？', e => {
           del(id).then(e => {
             if (e.data.success) {
               this.$sucmsg('删除成功')
               this.getList()
             }
           })
         })
       }
     }
}

</script>
<style scoped>
  p {
    margin: 0px;
  }

  .studyDetail-page .el-row {
    margin-bottom: 20px;
  }

  .studyDetail-page .el-col {
    border-radius: 4px;
    /* border:1px solid red; */
    margin-left: 20px;
    margin-right: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  .studyDetail-page .bg-purple-dark>.el-input {
    width: 20%;
  }

  .studyDetail-page-search .el-form-item {
    margin-bottom: 0px;
  }

  .studyDetail-page .bg-purple {
    background: #d3dce6;
  }

  .studyDetail-page .bg-purple-light {
    background: #e5e9f2;
  }

  .studyDetail-page .grid-content {
    border-radius: 4px;
    min-height: 36px;
    /* margin-bottom: 10px; */
  }

  .studyDetail-page .grid-content span {
    color: #606266;
    font-size: 14px;
  }

  .studyDetail-page .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }

  .studyDetail-page .operation-button {
    /* display: inline-block; */
    float: right;
  }

  .el-dialog {
    border-radius: 4px;
  }

  .studyDetail-page .el-tooltip {
    cursor: pointer;
    color: #409eff;
  }

  .studyDetail-page .profession-pagination {
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
