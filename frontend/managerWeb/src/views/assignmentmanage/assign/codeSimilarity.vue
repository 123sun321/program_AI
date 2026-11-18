<template>
<div class="maincontent student-page">
      <div class="grid-content">
          <el-form :inline="true" :model="form" ref="form" class="demo-form-inline">



            <el-form-item label="是否抄袭可能:" prop="plagiarize" >
              <el-select v-model="form.plagiarize" clearable placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
               <el-button icon="el-icon-search"  type="primary" @click="getList">搜索</el-button>
                <el-button plain @click="resetForm('form')">重置</el-button>
            </el-form-item>
             <!-- <div class="detail">
              <el-button type="info" plain icon="el-icon-document"  @click="detailStudent">查看</el-button>
            </div> -->
            <div class="detail">
              <!-- <el-button type="primary" @click="addAssign" icon="el-icon-plus">新建</el-button> -->
              <!-- <el-button type="info" @click="importStudent" icon="el-icon-upload2" >批量导入</el-button> -->
              <!-- <el-button type="success" @click="exportStudent" icon="el-icon-download">导出</el-button> -->
              <!-- <el-button type="danger" @click="batchDeleted" icon="el-icon-delete">批量删除</el-button> -->
          </div>
          </el-form>

      </div>
      <div class="course-table">
   <el-table
    :data="tableData"
    style="width: 100%"
    @selection-change="handleSelectionChange">

     <el-table-column
        type="selection"
        align="center"
        >
      </el-table-column>
  <el-table-column type="index" label="序号" width="50">

  </el-table-column>
  <el-table-column
      prop="student.name"
      label="学生姓名"
      >
    </el-table-column>
    <el-table-column
      prop="similarity"
      label="代码相似度"
      sortable
      >
    </el-table-column>
     <el-table-column

      label="是否有抄袭可能"
      >
      <template  slot-scope="scope">
        <div v-if="scope.row.plagiarize"> 是</div>
         <div v-else> 否</div>
                <!-- {{scope.row.creationTime !=null ? scope.row.creationTime :'-' }} -->
             </template>
    </el-table-column>
    <el-table-column
      prop="assignmentSubmit.creationTime"
      label="提交代码时间"
      sortable>
    </el-table-column>
    <el-table-column prop="zip" width="160" label="操作" align="center">
          <template slot-scope="scope">
             <!-- <el-button type="primary" size="mini" icon="el-icon-edit" plain @click="editAssign(scope.row.id)" title="编辑"></el-button> -->
            <el-button type="info" size="mini" icon="el-icon-search"  @click="submit(scope.row.comparatorWorkId)" title="作业详情" plain></el-button>
            <!-- <el-button type="danger" size="mini" icon="el-icon-delete" plain @click="singleDelete(scope.row.id)" title="删除"></el-button> -->
          </template>
        </el-table-column>
    </el-table>

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

import { fetchPage } from '@/api/assignmentmanage/codeSimilar'
import { delnum } from '@/api/assignmentmanage/assgin'

export default {
  components: {
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

        submitter_work_id: this.$route.query.assignmentId.id,
        plagiarize: undefined,

        orderBy: 'creationTime desc'
      },
      codeSimilarty: this.$route.query.assignmentId,
      tableData: [],
      loading: false,
      options: [{
        value: true,
        label: '是'
      }, {
        value: false,
        label: '否'
      }],
      // form 数据
      List: {
        grades: '', // 所有年级
        classInfo: '', // 所有班级
        courseList: '' // 课程列表
      },

      multipleSelection: [],
      value2: false
    }
  },
  created() {
    this.$nextTick(e => {
      this.init()
      this.getList()
      console.log(this.codeSimilarty.creationTime)
    })
  },
  methods: {
    init() {

    },
    getList() {
      this.loading = true

      const params = this.$formToParams(this.$refs.form)
      params.form.submitter_work_id = this.$route.query.assignmentId
      if (this.plagiarize != null) {
        params.form.plagiarize = this.form.plagiarize
      }
      fetchPage(params).then(e => {
        const data = e.data.data
        this.tableData = data.records
        // if (data.records.assignmentSubmit.creationTime > this.$route.query.assignmentId.creationTime) {
        //   this.value2 = true
        // }
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

    // 提交详情
    submit(row) {
      // this.$router.push('submit')
      this.$router.push({ path: 'mergely', query: { id1: row, id2: this.codeSimilarty }})
    },
    // 修改作业信息
    editAssign(id) {
      this.$refs.edit.open(id, this.List)
    },

    // 获取复选框被选中的作业数据

    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    // 删除方法
    delete(ids) {
      this.$cfm('确定删除作业记录吗？', e => {
        delnum(ids).then(e => {
          if (e.success) {
            this.$sucmsg('删除成功')
            this.getList()
          } else {
            this.$errmsg('有学生提交作业')
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
