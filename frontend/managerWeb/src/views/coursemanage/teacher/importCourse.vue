<template>
    <div class="maincontent course-page">
      <div class="grid-content">
        <el-form :inline="true" :model="form" ref="form" class="demo-form-inline">
              <el-form-item label="课程:" prop="name" fop="like">
            <el-input v-model="form.name" placeholder="按课程名称查询"  prefix-icon="el-icon-search">
            </el-input>
          </el-form-item>
          <el-form-item label="难易程度:" prop="complexity" clearable>
            <el-select v-model="form.complexity" clearable filterable placeholder="全部">
                <el-option  v-for="(item,index) in List.complexityList" :key="index" :label="item.complexity+'星'"  :value="item.complexity">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="课程类别:" prop="category" clearable>
            <el-select v-model="form.category" clearable placeholder="全部">
                <el-option  v-for="(item,index) in List.categoryList" :key="index"   :value="getcategoryname(item.category)">
                <template>{{getcategoryname(item.category)}}</template>
                </el-option>
            </el-select>
        </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-search" type="primary" @click="getList()">搜索</el-button>
            <el-button plain @click="resetForm('form')">重置</el-button>
          </el-form-item>

        </el-form>
      </div>
      <div class="course-table">
        <el-table ref="multipleTable" :data="courses" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange"
          v-loading="loading1">
          <!-- <el-table-column type="selection" align="center">
          </el-table-column> -->
          <el-table-column type="index" width="50">
          </el-table-column>

           <el-table-column prop="name" label="名称" align="center">
          </el-table-column>
          <!-- <el-table-column prop="summary" label="课程简介" align="center">
          </el-table-column> -->
          <!-- <el-table-column prop="category" label="课程类别" align="center">
            <template slot-scope="scope">
             {{getcategoryname(scope.row.category)}}
            </template>
          </el-table-column> -->
          <el-table-column prop="code" label="邀请码" align="center">
          </el-table-column>
           <el-table-column prop="complexity" label="难易程度" align="center" width="150">
            <template slot-scope="scope">
                <el-tag v-if="scope.row.complexity==1" >{{scope.row.complexity}}星</el-tag>
                <el-tag v-if="scope.row.complexity==2"  type="success">{{scope.row.complexity}}星</el-tag>
                <el-tag v-if="scope.row.complexity==3" type="info">{{scope.row.complexity}}星</el-tag>
                <el-tag v-if="scope.row.complexity==4"  type="warning">{{scope.row.complexity}}星</el-tag>
                <el-tag v-if="scope.row.complexity==5"  type="danger">{{scope.row.complexity}}星</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="logo" label="课程logo" align="center">
            <template slot-scope="scope">
              <img :src="scope.row.logo" alt="" width ='80' height = "80" >
            </template>
          </el-table-column>
          <el-table-column prop="joinNumber" label="参与学习人数" align="center"></el-table-column>
          <el-table-column prop="creationTime" label="添加时间" align="center"></el-table-column>
          <el-table-column prop="zip" width="210" label="操作" align="center">
          <template slot-scope="scope">
             <!-- <el-button type="text"   plain @click="lookCourse(scope.row.id)" title="预览">预览</el-button> -->
            <el-button type="text"   plain @click="editCourse(scope.row)" title="导入课程">使用此模板</el-button>

          </template>
        </el-table-column>
        </el-table>
        <page :page="form.page" @changed="getList"></page>

      </div>
      <!-- 修改课程 -->
      <!-- <div >
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div> -->
   <!-- <beatch-course ref="batch" @success="getList"></beatch-course> -->
</div>


</template>

<script>
  /**
 * @file 导入课程组件
 * @author 张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/04
 */
import { categoryList, complexityList, pageTeacherCourse } from '@/api/coursemanage/course'
// import BeatchCourse from './BeatchCourse'
import page from '@/components/page'
import { fetchItems } from '@/api/dictionary'
  export default {
    data() {
      return {
        form: {
          page: {
            current: 1,
            pageCount: 10,
            total: 0
          },
          search: '',
          orderBy: 'id desc',
          category: '',
          complexity: ''
        },
        visible: false,
        eid: '',

        courses: [],
        loading1: false,
        multipleSelection: [],
        value2: null,
        List: {
          complexityList: '', // 课程难易程度列表
          categoryList: '' // 课程类别列表
        }
      }
    },
    components: {
      page
      // BeatchCourse
    },
    created() {
      this.$nextTick(e => {
        this.init()
      })
    },
    methods: {
      init() {
        fetchItems(null, 'course_category').then(e => {
          this.coursecategory = e.data.data
        })
        categoryList().then(e => {
          this.List.categoryList = e.data.data
        })
        complexityList().then(e => {
          this.List.complexityList = e.data.data
        })
        this.getList()
      },
      save() {
        if (this.multipleSelection.length === 0) {
          this.$warnmsg('请选择要添加的课程')
          return
        } else {
          const ids = this.multipleSelection.map(b => b.id)
          console.log(ids)
          this.batchImportCourse(ids)
        }
      },

      getList() {
        this.loading1 = true
        const params = this.$formToParams(this.$refs.form)
        pageTeacherCourse(params).then(e => {
          const data = e.data.data
          // debugger
          this.courses = data.records
          this.form.page.total = data.total
          this.loading1 = false
        })
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },

      open(id) {
        this.visible = true
        this.$nextTick(e => {
          this.getList()
        })
      },
      close() {
        this.visible = false
        this.$refs.form.reset()
      },
      // 导入课程模板
      editCourse(row) {
        //   debugger
        this.$emit('ee', row)
      },
      getcategoryname: function(value) {
        var result = this.coursecategory.filter(m => m.code === value)
        if (result.length > 0) { return result[0].name } else { return '-' }
      }, // 重置搜索条件
      resetForm(formName) {
        this.$refs[formName].resetFields()
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
