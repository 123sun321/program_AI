<template>
<div class="maincontent class-page">
      <div class="grid-content bg-purple-dark">
          <el-form :inline="true" :model="form" ref="form" class="demo-form-inline pull-left">
            <div class="search-left">
              <el-form-item label="作品名称:" prop="title">
                <el-input v-model="form.title" placeholder="请输入内容"></el-input>
              </el-form-item>
              <el-form-item label="推荐人姓名:" prop="recommenderName">
                <el-input v-model="form.recommenderName" placeholder="请输入内容"></el-input>
              </el-form-item>
              <el-form-item label="推荐日期:" prop="creationTime">
                <el-date-picker
                  v-model="form.creationTime"
                  type="date"
                  placeholder="选择日期"
                  value-format="yyyy-MM-dd">
                </el-date-picker>
              </el-form-item>
              <el-form-item>
               <el-button icon="el-icon-search" type="primary" @click="getList()">搜索</el-button>
                <el-button plain  @click="resetForm">重置</el-button>
            </el-form-item>
            </div>

          </el-form>
      </div>
     
      <div class="class-table">
          <el-table
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading" 
            :data="tableData"
            tooltip-effect="dark"
            style="width: 100%"
            @selection-change="handleSelectionChange">
            <el-table-column
              type="selection"
              align="center"
              >
            </el-table-column>
            <el-table-column 
            type="index" 
            :index="indexMethod"
            width="50"
            label="序号"
            >
          </el-table-column>
            <el-table-column
              prop="title"
              label="作品名称"
              align="center"
              >
            </el-table-column>
            <el-table-column
              prop="student.name"
              label="作者姓名"
              align="center"
              >
            </el-table-column>
            <el-table-column
              prop="recommenderName"
              label="推荐人姓名"
              align="center"
              >
            </el-table-column>
            <el-table-column
              prop="creationTime"
              label="推荐时间"
              align="center"
              >
            </el-table-column>
             <!-- <el-table-column
              label="操作"
              align="center"
              >
               <template slot-scope="scope">
                <el-button type="primary" size="mini" @click="(choose(scope.row.id))">选中</el-button>
               </template>
            </el-table-column> -->
          </el-table>
          <page :page="form.page" @changed="getList"></page>
      </div>
</div>
</template>
<script>
import page from '@/components/page'
import {
  recommendWorkPage
} from '@/api/coursemanage/recommendWork'
// import { fetchPage } from '@/api/assignmentmanage/assgin'
export default {
  name: 'recommendWork',
  components: {
    page
  },
  data() {
    return {
      tableData: [], // 标签信息
      multipleSelection: [], // 全选
      // 分页和模糊查询
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        recommenderName: '',
        title: '',
        creationTime: ''
      },
      loading: false, // 加载
      check: false, // 查看
      searchParams: {},
      restaurants: [],
      labelList: [],
      chooseIndex: null
    }
  },
  // 获取列表标签数据
  mounted() {

  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      this.searchParams.page = this.form.page
      if (this.form.title !== '' && this.form.recommenderName === '' && this.form.creationTime === '') {
        this.searchParams.form = {
          title: this.form.title
        }
      }
      if (this.form.title !== '' && this.form.recommenderName !== '' && this.form.creationTime === '') {
        this.searchParams.form = {
          title: this.form.title,
          recommenderName: this.form.recommenderName
        }
      }
      if (this.form.title !== '' && this.form.recommenderName === '' && this.form.creationTime !== '') {
        this.searchParams.form = {
          title: this.form.title,
          creationTime: this.form.creationTime
        }
      }
      if (this.form.recommenderName !== '' && this.form.title === '' && this.form.creationTime === '') {
        this.searchParams.form = {
          recommenderName: this.form.recommenderName
        }
      }
      if (this.form.recommenderName !== '' && this.form.title !== '' && this.form.creationTime === '') {
        this.searchParams.form = {
          title: this.form.title,
          recommenderName: this.form.recommenderName
        }
      }
      if (this.form.recommenderName !== '' && this.form.title === '' && this.form.creationTime !== '') {
        this.searchParams.form = {
          recommenderName: this.form.recommenderName,
          creationTime: this.form.creationTime
        }
      }
      if (this.form.title === '' && this.form.recommenderName === '' && this.form.creationTime !== '') {
        this.searchParams.form = {
          creationTime: this.form.creationTime
        }
      }
      if (this.form.title !== '' && this.form.recommenderName === '' && this.form.creationTime !== '') {
        this.searchParams.form = {
          title: this.form.title,
          creationTime: this.form.creationTime
        }
      }
      if (this.form.title === '' && this.form.recommenderName !== '' && this.form.creationTime !== '') {
        this.searchParams.form = {
          recommenderName: this.form.recommenderName,
          creationTime: this.form.creationTime
        }
      }
      if (this.form.title !== '' && this.form.recommenderName !== '' && this.form.creationTime !== '') {
        this.searchParams.form = {
          title: this.form.title,
          recommenderName: this.form.recommenderName,
          creationTime: this.form.creationTime
        }
      }
      // const params = this.$formToParams(this.$refs.form)
      // 获取所有的优秀作品数据
      recommendWorkPage(this.searchParams).then(e => {
        this.tableData = e.data.data.records
        this.form.page.total = parseInt(e.data.data.total)
        this.loading = false
      })
    },
    // 获取优秀作品 index [Int] 返回班级序号
    indexMethod(index) {
      return index + 1 + this.form.page.pageCount * (this.form.page.current - 1)
    },
    // 删除标签单条数据
    choose(index) {
      this.chooseIndex = index
    },
    // 获取复选框被选中的标签数据
    handleSelectionChange(val) {
      if (val.length > 1) {
        this.$message({
          type: 'error',
          message: '仅能选中一条'
        })
      } else {
        this.chooseIndex = val[0].id
      }
      console.log(val)
    },
    // 重置搜索条件
    resetForm() {
      this.searchParams.form = {
      }
      this.$refs.form.resetFields()
      this.getList()
    },
    getValue() {
      return this.chooseIndex
      // console.log(this.multipleChoose)
    }
  }
}
</script>
<style scoped>
  p{
    margin:0px;
  }
  .class-page .el-row {
    margin-bottom: 20px;
  }
  .class-page .el-col {
    border-radius: 4px;
    /* border:1px solid red; */
    margin-left: 20px;
    margin-right: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  /* .class-page .bg-purple-dark {
    background: #99a9bf;
  } */
  .class-page .bg-purple-dark>.el-input {
    width:20%;
  }
  /* .class-page-search .el-form-item{
     margin-bottom: 0px;
  }
  .class-page-search .search-left{
    margin-bottom: 10px;
  } */
  .class-page .bg-purple {
    background: #d3dce6;
  }
  .class-page .bg-purple-light {
    background: #e5e9f2;
  }
  .class-page .grid-content {
    border-radius: 4px;
    min-height: 36px;
    margin-bottom: 10px;
  }
  .class-page .grid-content span{
    color: #606266;
    font-size: 14px;
  }
  .class-page .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
  .class-page .operation-button{
    /* display: inline-block; */
    float: right;
  }
  .el-dialog{
    border-radius: 4px;
  }
  .class-page .el-tooltip{
     cursor: pointer;
     color:#409eff;
   }
   .class-page .profession-pagination{
     margin-top:20px;
     text-align: right;
   }
</style>

<style>
 .export-table{
     border-right: 1px solid #DCDFE6;
     border-bottom: 1px solid #DCDFE6;
   }
   .export-table td{
     border-left: 1px solid #DCDFE6;
     border-top: 1px solid #DCDFE6;
   }
   .export-table{
     width: 100%;
   }
   .export-table td:first-child{
     width: 20%;
     text-align: center;
   }
   .export-table td:last-child{
     width: 80%;
    }
    .export-table tr:last-child>td:last-child{
      padding: 35px;
    }
   .export-table .upload-demo{
      width: 80%;
      display: inline-block;
    }
  
    .export-table .el-upload{
      margin-right: 10px;
    }
    
    .export-table  .el-upload-list__item:first-child{
      margin-top:3px;
    }
    .export-table .upload-demo .el-upload-list{
      border:1px solid #DCDFE6;
      height: 32px;
      display: inline-block;
      width: 78%;
      vertical-align: top;
    }

</style>

