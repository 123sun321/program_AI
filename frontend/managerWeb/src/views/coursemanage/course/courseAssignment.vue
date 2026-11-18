<template>
<div class="maincontent student-page">
      <div class="grid-content">
          <el-form :inline="true" :model="form" ref="form" class="demo-form-inline">
               <el-form-item label="作业类型:" prop="team_category">
            <el-select v-model="form.team_category" clearable  placeholder="请选择">
                <el-option  v-for="(item,index) in List.teams" :key="index" :label="item.name"  :value="item.value">
                </el-option>
            </el-select>
        </el-form-item>
           <el-form-item label="年级:" prop="year">
            <el-select v-model="form.year" @change="changeYear" clearable placeholder="请选择">
                <el-option  v-for="(item,index) in List.grades" :key="index" :label="item.name"  :value="item.code">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="班级:" prop="gradeId">
            <el-select v-model="form.gradeId" clearable placeholder="请选择">
              <el-option  v-for="(item,index) in List.classInfo" :key="index" :label="item.name"  :value="item.id"></el-option>
            </el-select>
        </el-form-item>
          <!-- <el-form-item label="所属课程:" prop="courseId">
            <el-select v-model="form.courseId" clearable filterable placeholder="全部">
                <el-option  v-for="(item,index) in List.courseList" :key="index" :label="item.name"  :value="item.id">
                </el-option>
            </el-select>
        </el-form-item> -->
            <el-form-item label="作业标题:" prop="title" fop="like">
              <el-input v-model="form.title"  placeholder="请输入作业标题"  prefix-icon="el-icon-search"  >
               </el-input>
            </el-form-item>
            <el-form-item>
               <el-button icon="el-icon-search" type="primary" @click="getList">搜索</el-button>
                <el-button plain @click="resetForm('form')">重置</el-button>
            </el-form-item>
             <!-- <div class="detail">
              <el-button type="info" plain icon="el-icon-document"  @click="detailStudent">查看</el-button>
            </div> -->
            <!-- <div class="detail">
              <el-button type="primary" @click="addAssign" icon="el-icon-plus">新建</el-button>

              <el-button type="danger" @click="batchDeleted" icon="el-icon-delete">批量删除</el-button>
          </div> -->
          </el-form>

      </div>
      <div class="course-table">
   <el-table
    :data="tableData"
    style="width: 100%"
    @selection-change="handleSelectionChange">
     <!-- <el-table-column
        type="selection"
        align="center"
        >
      </el-table-column> -->
  <el-table-column type="index" label="序号" width="50">
  </el-table-column>
   <el-table-column

      label="作业类型"
      width="180">
       <template slot-scope="scope">
           {{scope.row.teamCategory===1?"班级":"小组"}}
          </template>
    </el-table-column>
    <el-table-column
      prop="title"
      label="标题"
      width="180">
    </el-table-column>
    <el-table-column
      prop="summary"
      label="作业要求"
      width="180">
    </el-table-column>
    <el-table-column
      prop="grade.year"
      label="学年">
    </el-table-column>
    <el-table-column
      prop="grade.name"
      label="班级">
    </el-table-column>
    <el-table-column
      prop="submitNumber"
      label="提交人数">
    </el-table-column>
    <el-table-column
      prop="course.name"
      label="所属课程">
    </el-table-column>
    <el-table-column
      prop="creationTime"
      label="创建时间">
    </el-table-column>
    <!-- <el-table-column prop="zip" width="160" label="操作" align="center" >
          <template slot-scope="scope">
            <el-button type="info" size="mini" icon="el-icon-search"  @click="submit(scope.row.id)" title="提交详情" plain v-permission="'assignmentsubmit'"></el-button>
          </template>
        </el-table-column> -->
    </el-table>
      <!-- <add-assign ref="add" @success="getList"></add-assign>
      <edit-assign ref="edit" @success="getList"></edit-assign> -->
      <page :page="form.page" @changed="getList"></page>
 </div>
</div>

</template>
<script>
/**
 * @file 作业管理组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/22
 */
import page from '@/components/page'
import {
  fetchGrades, getClassByYear
} from '@/api/base/student'
import { getNow } from '@/api/coursemanage/course'
import { fetchPage, del, delnum } from '@/api/assignmentmanage/assgin'
// import addAssign from './addAssign'
// import editAssign from './editAssign'
export default {
  components: {
    page
    // addAssign,
    // editAssign

  },
  props: ['courseid'],
  data() {
    return {
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        gradeId: '',
        year: '',
        team_category: '',
        // courseId: this.courseid,
        title: ''
      },
      tableData: [],
      loading: false,
      // form 数据
      List: {
        grades: '', // 所有年级
        classInfo: '', // 所有班级
        courseList: '', // 课程列表
        teams: [
          {
            value: 1,
            name: '班级'
          },
          {
            value: 2,
            name: '小组'
          }
        ]
      },

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
      // 获取年级
      fetchGrades().then(e => {
        this.List.grades = e.data.data
      })
      // 获取课程信息
      getNow().then(e => {
        this.List.courseList = e.data.data
      })
    },
    getList() {
      this.loading = true
      const params = this.$formToParams(this.$refs.form)
      params.form.course_id = this.courseid
      fetchPage(params).then(e => {
        const data = e.data.data
        this.tableData = data.records
        this.form.page.total = data.total
        this.page = data.current
        this.loading = false
      })
    },
    // 重置搜索条件
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.getList()
    },
    // 添加作业
    addAssign() {
      this.$refs.add.open(this.List)
    },
    // 提交详情
    submit(row) {
      // this.$router.push('submit')
      // debugger
      this.$router.push({ path: 'assignmentDetail', query: { assignmentId: row }})
    },
    // 修改作业信息
    editAssign(id) {
      this.$refs.edit.open(id, this.List)
    },
    // 改变学年
    changeYear() {
      this.form.gradeId = undefined
      getClassByYear(this.form.year).then(e => {
        this.List.classInfo = e.data.data
      })
    },
    // 获取复选框被选中的作业数据

    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 删除作业
    singleDelete(id) {
      this.$cfm('确定删除吗？', e => {
        del(id).then(response => {
          if (response) {
            this.$sucmsg('删除成功!')
            // this.gridData.splice(index, 1)
            this.getList()
          } else {
            this.$errmsg('有学生删除失败!')
          }
        })
      })
    },
    // 批量删除选中的作业
    batchDeleted() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要删除的作业')
        return
      } else {
        const ids = this.multipleSelection.map(b => b.id)
        this.delete(ids)
        this.getList()
      }
    },
    // 删除方法
    delete(ids) {
      this.$cfm('确定删除作业记录吗？', e => {
        delnum(ids).then(e => {
          if (e.success) {
            this.$sucmsg('删除成功')
            this.getList()
          } else {
            this.$errmsg('删除失败')
          }
        })
      })
    }

  }

}

</script>

<style lang="scss" scoped>
.el-dropdown-link {
    cursor: pointer;
    color: rgb(240, 241, 243);
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
  .detail{
      float: right;
       /* margin-top: 40px; */
      margin-left: 20px;
     /* margin-bottom: 10px; */
    }
</style>
