<template>
  <div class="maincontent">
    <div class="condition-container clearfix mg0">
      <el-form :inline="true" ref="form" :model="form" style="width:70%;" class="demo-form-inline pull-left">
        <el-form-item label="年级:" prop="year">
            <el-select v-model="form.year" @change="changeYear" placeholder="请选择">
                <el-option  v-for="(item,index) in grades" :key="index" :label="item.name"  :value="item.code">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="班级:" prop="gradeId">
            <el-select v-model="form.gradeId" placeholder="请选择">
              <el-option  v-for="(item,index) in classInfo" :key="index" :label="item.name"  :value="item.id"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="search">
          <el-input v-model="form.search" placeholder="请输入学号、姓名等"></el-input>
        </el-form-item>
        <el-form-item class="dk-button">
          <el-button type="primary" @click="search" icon="el-icon-search">查询</el-button>
          <el-button type="default" @click="reset" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="operation-button pull-right">
        <el-button @click="callback">返回</el-button>
      </div>
    </div>
    <div class="table-container">
      <el-table :data="gridData"  v-loading='loading'>
        <el-table-column type="index" label="序号" align="center"></el-table-column>
        <el-table-column label="班级" prop="grade.name"></el-table-column>
        <el-table-column label="学号" prop="student.code"></el-table-column>
        <el-table-column label="姓名" prop="student.name"></el-table-column>
        <el-table-column label="实验名称" prop="exercise.name"></el-table-column>
        <el-table-column label="创建时间" width="150">
          <template slot-scope="scope">
            {{scope.row.creationTime}}
          </template>
        </el-table-column>
        <el-table-column prop="zip" width="160" label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-search"  @click="edit(scope.row)" title="查看详情" plain></el-button>
            <el-button type="info" size="mini" icon="el-icon-view"  @click="look(scope.row)" title="预览" plain></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pager-container">
      <page :page="form.page" @changed="bindData"></page>
    </div>
     <quite ref="quite" :codeId="codeid"></quite>
  </div>
</template>
<script>
/**
 * @file 实验参与详情
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/15
 */
import quite from './components/preview/quite'
import page from '@/components/page'
import { pageByExerciseId } from '@/api/coursemanage/exercisejoin'
import {
  fetchGrades, getClassByYear
} from '@/api/base/student'

export default {
  components: { page, quite },
  data() {
    return {
      codeid: undefined,
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        gradeId: undefined,
        search: undefined,
        orderBy: 'creation_time desc'
      },
      loading: false,
      // 所有年级
      grades: [],
      // 所有班级
      classInfo: [],
      gridData: [],
      multipleSelection: []
    }
  },
  created() {
    this.$nextTick(e => {
      this.init()
      this.bindData()
    })
  },
  methods: {
    init() {
      // 获取年级
      fetchGrades().then(e => {
        this.grades = e.data.data
      })
    },
    search() { // 搜索
      this.bindData()
    },
    reset() { // 重置
      this.$refs['form'].resetFields()
      this.bindData()
    },
    // 编辑
    edit(item) {
      this.$router.push({ name: 'exerciseinfo', query: { exercisejoinid: item.id }})
    },
    look(item) {
      this.codeid = item.training.codeId
      this.$refs.quite.open()
    },
    callback() {
      this.$router.go(-1)
    },
    changeYear() {
      this.form.gradeId = undefined
      getClassByYear(this.form.year).then(e => {
        this.classInfo = e.data.data
      })
    },
    // 数据绑定
    bindData() {
      var vm = this
      this.loading = true
      const params = this.$formToParams(this.$refs.form)
      params.exerciseId = this.$route.query.exerciseid
      pageByExerciseId(params).then(response => {
        vm.loading = false
        const data = response.data.data
        console.log(data.records)
        vm.gridData = data.records
        this.form.page.total = data.total
        this.loading = false
        this.page = data.current
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>
<style lang="scss" scoped>

</style>

