<template>
  <div class="main">
  <div class="maincontent-top" > <span class="nav">累计加入学习路径人数： {{sum}} 人</span></div>
    <div  class="container" >
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
          <el-button type="primary" @click="search()" icon="el-icon-search">查询</el-button>
          <el-button type="default" @click="reset" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="operation-button pull-right">
        <el-button @click="callback">返回</el-button>
      </div>
     <div class="table-container">
      <el-table :data="gridData"  v-loading='loading'>
        <el-table-column type="index" label="序号" align="center"></el-table-column>
        <el-table-column label="班级" prop="gradename" align="center"></el-table-column>
        <el-table-column label="学号" prop="code" align="center"></el-table-column>
        <el-table-column label="姓名" prop="name" align="center"></el-table-column>
        <el-table-column label="学习进度" prop="studynum" align="center">
             <template slot-scope="scope">
             <el-progress v-if="scope.row.percentage===100" :percentage="scope.row.studynum" :stroke-width="12" status="success"></el-progress>
             <el-progress v-else :percentage="scope.row.studynum" :stroke-width="12" ></el-progress>
        </template>
        </el-table-column>
        <el-table-column label="加入时间" prop="creationTime" align="center">
        </el-table-column>
        <!-- <el-table-column prop="zip" width="160" label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="info" size="mini" icon="el-icon-search"  @click="look(scope.row)" title="详情" plain></el-button>
          </template>
        </el-table-column> -->
      </el-table>
         </div>
      <!-- <div class="pager-container"> -->
        <page :page="form.page" @changed="bindData"></page>
      <!-- </div> -->
    </div>
  </div>
</template>
<script>
/**
 * @file 路径学习详情组件
 * @author myc
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/15
 */
import page from '@/components/page'
import { findPage, personcount, studycount } from '@/api/coursemanage/pathmanage'
import {
  fetchGrades, getClassByYear
} from '@/api/base/student'

export default {
  components: { page },
  data() {
    return {
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        year: '',
        gradeId: undefined,
        search: undefined,
        orderBy: 'creation_time desc'
      },
      sum: '', // 总人数
      num: '', // 总课数
      loading: false,
      grades: [], // 所有年级
      classInfo: [], // 所有班级
      gridData: [],
      multipleSelection: []
    }
  },
  created() {
    personcount().then(e => {
      this.sum = e.data.data
    })
    studycount().then(e => {
      this.num = e.data.data
    })
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
    callback() {
      this.$router.push({ name: 'manageindex' })
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
      findPage(params).then(response => {
        vm.loading = false
        const data = response.data.data
        vm.gridData = data.records
        for (let i = 0; i < vm.gridData.length; i++) {
          this.$set(vm.gridData[i], 'studynum', Math.round((vm.gridData[i].finishNumber) * 100 / this.num))
        }
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
.main .maincontent-top{
height: 80px;
/deep/
.nav{
   line-height: 70px;
    padding: 20px;
    font-weight: bold;
}
}
.content-new-background .app-main>div {
   background-color: #fff;
    padding-bottom: 20px;
    margin-bottom: 20px;
    border: none;
   -webkit-box-shadow: none;
    box-shadow: none;
}
.main{
  height:70px;
}
.main /deep/ .container{
  padding: 20px;
  margin:0;
  background-color: #FFFFFF;
}
</style>

