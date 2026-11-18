<template>
<div class="maincontent course-page">
      <div class="grid-content">
        <el-form :inline="true" :model="form" ref="form" class="demo-form-inline">
              <el-form-item label="课程名称:" prop="name" fop="like">
            <el-input v-model="form.name" placeholder="按课程名称查询"  prefix-icon="el-icon-search">
            </el-input>
          </el-form-item>
           <el-form-item label="难易程度:" clearable prop="complexity">
            <el-select v-model="form.complexity" clearable filterable placeholder="全部">
                <el-option  v-for="(item,index) in List.complexityList" :key="index" :label="item.name"  :value="item.complexity">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="课程类别:" prop="category">
            <el-select v-model="form.category" clearable placeholder="全部">
                <el-option  v-for="(item,index) in coursecategory" :key="index" :label="item.name"   :value="item.code">
                <template>{{getcategoryname(item.code)}}</template>
                </el-option>
            </el-select>
        </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-search" type="primary" @click="getList()">搜索</el-button>
            <el-button plain @click="resetForm('form')">重置</el-button>
          </el-form-item>
          <div class="operation-button">
            <el-button type="primary" @click="batchCourse" icon="el-icon-plus" v-permission="'course:add'">新建</el-button>
            <!-- <el-button type="primary" @click="importcourse" icon="el-icon-upload2" >导入课程</el-button> -->
            <el-button type="danger" @click="batchDeleted" icon="el-icon-delete" v-permission="'course:del'">批量删除</el-button>
          </div>
        </el-form>
      </div>
      <div class="course-table">
        <el-table ref="multipleTable" :data="courses" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange" @sort-change="sortChange"
          v-loading="loading">
          <el-table-column type="selection" align="center">
          </el-table-column>
          <el-table-column type="index" width="50">
          </el-table-column>

           <el-table-column prop="name" label="名称" align="center">
          </el-table-column>
          <!-- <el-table-column prop="summary" label="课程简介" align="center">
          </el-table-column> -->
          <el-table-column prop="category" label="课程类别" align="center" sortable="custom">
            <template slot-scope="scope">
             {{getcategoryname(scope.row.category)}}
            </template>
          </el-table-column>
          <el-table-column prop="code" label="邀请码" align="center">
          </el-table-column>
          <el-table-column prop="complexity" label="难易程度" align="center" width="150" sortable="custom">
            <template slot-scope="scope">
                <el-tag v-if="scope.row.complexity==1" >{{scope.row.complexity}}星</el-tag>
                <el-tag v-if="scope.row.complexity==2"  type="success">{{scope.row.complexity}}星</el-tag>
                <el-tag v-if="scope.row.complexity==3" type="info">{{scope.row.complexity}}星</el-tag>
                <el-tag v-if="scope.row.complexity==4"  type="warning">{{scope.row.complexity}}星</el-tag>
                <el-tag v-if="scope.row.complexity==5"  type="danger">{{scope.row.complexity}}星</el-tag>
            </template>
          </el-table-column>
          <!-- <el-table-column prop="stage" label="课程阶段" align="center"></el-table-column> -->

          <el-table-column prop="logo" label="课程logo" align="center">
            <template slot-scope="scope">
              <img :src="scope.row.logo" alt="" width ='80' height = "80" >
            </template>
          </el-table-column>

          <el-table-column prop="joinNumber" label="参与学习人数" align="center"  sortable="custom"></el-table-column>
          <el-table-column prop="commentNumber" label="课程评价人数" align="center" sortable="custom"></el-table-column>
          <el-table-column prop="creationTime" label="创建时间" align="center" sortable="custom"></el-table-column>
          <el-table-column prop="zip" width="210" label="操作" align="center">
          <template slot-scope="scope">
             <el-button type="primary" size="mini" icon="el-icon-edit" plain @click="editCourse(scope.row.id)" title="编辑课程"></el-button>
            <el-button type="info" size="mini" icon="el-icon-search"  @click="coursebasic(scope.row)" title="课程详情" plain></el-button>
            <el-button type="info" size="mini" icon="el-icon-printer"  @click="coursecopy(scope.row)" title="复制邀请码" plain></el-button>
            <el-button type="danger" size="mini" icon="el-icon-delete" plain @click="singleDelete(scope.row.id)" title="删除课程"></el-button>
          </template>
        </el-table-column>
        </el-table>
        <page :page="form.page" @changed="getList"></page>
      </div>
      <!-- 修改课程 -->
      <!-- <add-course ref="add" @success="getList"></add-course> -->
      <edit-course ref="edit" @success="getList"></edit-course>
       <share ref="share" @success="getList"></share>
      <batch-course ref="batch"  @success="getList"></batch-course>
</div>
</template>

<script>
   /**
 * @file 教师端课程管理组件
 * @author  张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/4/10
 */

import editCourse from './editCourse'
import BatchCourse from './BatchCourse'
import share from './share'
import page from '@/components/page'
import { getNowCourse, del } from '@/api/coursemanage/course'
import { fetchItems } from '@/api/dictionary'
export default {
  components: {

       editCourse,
       BatchCourse,
       share,
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
           search: '',
           category: '',
           complexity: '',
           orderBy: 'creationTime desc'
         },
         coursecategory: [],
         courses: [],
         loading: false,
         multipleSelection: [],
         value2: null,
         List: {
           complexityList: [

             {
               name: '1星',
               complexity: 1
             },
             {
               name: '2星',
               complexity: 2
             },
             {
               name: '3星',
               complexity: 3
             },
             {
               name: '4星',
               complexity: 4
             },
             {
               name: '5星',
               complexity: 5
             }
           ] // 课程难易程度列表

         }
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
         //  categoryList().then(e => {
         //    this.List.categoryList = e.data.data
         //  })
         //  complexityList().then(e => {
         //    this.List.complexityList = e.data.data
         //  })
       },
       getList() {
         this.loading = true
         const params = this.$formToParams(this.$refs.form)
         getNowCourse(params).then(e => {
           const data = e.data.data
           this.courses = data.records
           this.form.page.total = data.total
           this.loading = false
         })
       },
       // 1.1添加课程
       batchCourse() {
         this.$refs.batch.open()
       },
       editCourse(id) {
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
       coursecopy(code) {
         if (this.multipleSelection.length > 1) {
           this.$message({
             type: 'info',
             message: '支持复制单条课程邀请码'
           })
         } else {
           // debugger
           this.$refs.share.open(code.code, code.id)
         }
       },
       // 6.1获取复选框被选中的教师数据
       handleSelectionChange(val) {
         this.multipleSelection = val
       },
       // 单删
       singleDelete(id) {
         this.delete([id])
       },
       // 6.2批量删除选中的课程数据
       batchDeleted() {
         if (this.multipleSelection.length === 0) {
           this.$warnmsg('请选择要删除的课程')
           return
         } else {
           const ids = this.multipleSelection.map(b => b.id)
           this.delete(ids)
         }
       },
       // 重置搜索条件
       resetForm(formName) {
         this.$refs[formName].resetFields()
         this.getList()
       },
       // 删除方法
       delete(ids) {
         this.$cfm('确定删除课程记录吗？', e => {
           del(ids).then(e => {
             if (e.data.success) {
               this.$sucmsg('删除成功')
               this.getList()
             } else {
               this.$errmsg('不可以删除有实验的课程!')
             }
           })
         })
       },
       // 参与详情
       coursebasic(item) {
         this.$router.push({ name: 'teacherCoursedetail', query: { courseid: item.id }})
       },
       getcategoryname: function(value) {
         var result = this.coursecategory.filter(m => m.code === value)
         if (result.length > 0) { return result[0].name } else { return '-' }
       },
       // 改变排序
       sortChange(data) {
         console.log(data)
         debugger
         var sortStr = 'creation_time desc'
         if (data.prop !== null) {
           sortStr = data.prop
           if (data.order === 'ascending') {
             sortStr += ' asc'
           } else {
             sortStr += ' desc'
           }
         }
         this.form.orderBy = sortStr
         this.getList()
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
