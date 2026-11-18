<template>
<div class="maincontent studyDetail-page">
      <div class="grid-content bg-purple-dark">
        <el-form :inline="true" :model="form" ref="form" class="demo-form-inline">
          <el-form-item label="姓名:" prop="name">
            <el-input v-model="form.name" placeholder="按姓名查询" prefix-icon="el-icon-search">
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-search" type="primary" @click="getList()">搜索</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="course-table">
        <el-table
          :data="tableData3"
          border
          style="width: 100%">
          <el-table-column type="index"  label="序号" width="50"></el-table-column>
         <el-table-column
           prop="student.name"
           label="姓名">
         </el-table-column>
         <el-table-column
           prop="joinExercise"
           label="参与实验数">
         </el-table-column>
         <el-table-column
           prop="learningProgress"
           label="学习进度">
            <template slot-scope="scope">
             <el-progress v-if="scope.row.percentage===100" :percentage="scope.row.learningProgress" :stroke-width="12" status="success"></el-progress>
             <el-progress v-else :percentage="scope.row.learningProgress" :stroke-width="12" ></el-progress>
           </template>
         </el-table-column>
         <el-table-column
           prop="creationTime"
           label="参与时间"
           width="180">
         </el-table-column>
         <el-table-column label="操作" width="120">
           <template slot-scope="scope">
             <el-button type="text" size="small" @click="viewReport(scope.row)">查看报告</el-button>
           </template>
         </el-table-column>
        </el-table>
      </div>
       <page :page="form.page" @changed="getList" style="margin-top:20px;"></page>
</div>
</template>
<script>
   /**
 * @file 课程参与详情组件
 * @author  刘彦杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/3/11
 */
import page from '@/components/page'
import {
     searchPage,
     del
} from '@/api/coursemanage/course'

export default {
     props: ['courseid'],
     components: {
       page
     },
     name: 'course',
     data() {
       return {
         form: {
           page: {
             current: 1,
             pageCount: 10,
             total: 0
           },
           search: '',
           orderBy: 'tcpd.creation_time desc'
         },
         tableData3: [], // 数据列表
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
         this.loading = true
         const params = this.$formToParams(this.$refs.form)
         params.form.course_id = this.courseid
         searchPage(params).then(e => {
           const data = e.data.data
           debugger
           this.tableData3 = data.records
           this.form.page.total = data.total
           this.loading = false
         })
       },
       callback() {
         this.$router.push({ name: 'exerciseindex' })
       },
       // 2.1批量导入教师
       importCourse() {
         this.$refs.imp.open()
       },
        // 删除方法
        delete(ids) {
          this.$cfm('确定删除课程记录吗？', e => {
            del(ids).then(e => {
              if (e.data.success) {
                this.$sucmsg('删除成功')
                this.getList()
              }
            })
          })
        },
        // 查看学生学习报告
        viewReport(row) {
          const studentId = row.studentId
          const courseId = this.courseid
          const studentName = row.student ? row.student.name : ''
          this.$router.push({
            name: 'studentReport',
            query: { studentId, courseId, studentName }
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

  .studyDetail-page .bg-purple-dark {
    /* background: #99a9bf; */
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
