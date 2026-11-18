<template>
  <div class="main">
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
        <el-button @click="message" type="primary">统计信息</el-button>
      </div>
     <div class="table-container">
      <el-table :data="gridData"  v-loading='loading'>
        <el-table-column type="index" label="序号" align="center"></el-table-column>
        <el-table-column label="班级" prop="gradeName" align="center"></el-table-column>
        <el-table-column label="姓名" prop="studentName" align="center"></el-table-column>
        <el-table-column label="签到时间" prop="signTime" align="center">
        </el-table-column>
        <el-table-column prop="zip" width="160" label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" icon="el-icon-delete" v-permission="'signRecord:del'"  @click="del(scope.row.id)" title="删除" plain></el-button>
          </template>
        </el-table-column>
        </el-table>
      </div>
        <page :page="form.page" @changed="bindData"></page>
      </div>
  </div>
</template>
<script>
/**
 * @file 签到管理组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/20
 */
import page from '@/components/page'
import { findPage, dele } from '@/api/community/signmanager'
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
        search: undefined
      },
      value: false, // 开启签到
      loading: false,
      grades: [], // 所有年级
      classInfo: [], // 所有班级
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
    message() {
      this.$router.push({ name: 'signDetail' })
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
        this.form.page.total = data.total
        this.loading = false
        this.page = data.current
      }).catch(() => {
        this.loading = false
      })
    },
    // 删除签到记录
    del(index) {
      this.$cfm('确定删除吗？', e => {
        dele(index).then(e => {
          if (e) {
            this.$sucmsg('删除成功!')
            this.gridData.splice(index, 1)
            this.bindData()
          } else {
            this.$errmsg('删除失败!')
            this.bindData()
          }
        })
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

