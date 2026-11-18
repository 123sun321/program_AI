<template>
<div class="maincontent class-page">
      <div class="grid-content bg-purple-dark">
          <el-form :inline="true" :model="form" ref="form" class="demo-form-inline pull-left">
            <div class="search-left">
              <el-form-item label="标签名称:" prop="name">
                <el-select
                v-model="form.name"
                filterable
                remote
                reserve-keyword
                placeholder="请输入关键词"
                :remote-method="remoteMethod"
                :loading="loading">
                <el-option
                  v-for="item in options4"
                  :key="item.name"
                  :label="item.label"
                  :value="item.name">
                </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
               <el-button icon="el-icon-search" type="primary" @click="getList()">搜索</el-button>
                <el-button plain @click="resetForm">重置</el-button>
            </el-form-item>
          </div>

          </el-form>
          <div class="operation-button pull-right">
            <el-button type="primary" @click="addLabel" v-permission="'label:add'" icon="el-icon-plus">新建</el-button>
            <!-- <el-button type="primary" @click="importClass" icon="el-icon-upload2" >批量导入</el-button>
            <el-button type="primary" @click="exportClass" icon="el-icon-download">导出</el-button> -->
            <el-button type="danger"  plain @click="batchDeleted" v-permission="'label:del'" icon="el-icon-delete">批量删除</el-button>
          </div>
      </div>
     
      <div class="class-table">
          <el-table
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading" 
            ref="multipleTable"
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
              prop="name"
              label="标签名称"
              align="center"
              >
            </el-table-column>
             <el-table-column
              label="操作"
              align="center"
              >
               <template slot-scope="scope">
                <el-button type="primary" size="mini" v-permission="'label:edit'" icon="el-icon-edit" plain @click="editLabel(scope.row.id)"></el-button>
                <el-button type="danger" size="mini" v-permission="'label:del'" icon="el-icon-delete" plain @click="(deleteLabel(scope.row.id))"></el-button>
               </template>
            </el-table-column>
          </el-table>
          <page :page="form.page" @changed="getList"></page>
      </div>
    <!--修改班级-->
      <add-label ref="add" @success="getList"></add-label>
      <edit-label ref="edit" @success="getList"></edit-label>
</div>
</template>

<script>
import addLabel from '../label/addLabel'
import editLabel from '../label/editLabel'
import page from '@/components/page'
import {
  pageWithLabel, deleteSingleLabel, getAllList, delBatches, chooseList
} from '@/api/base/label'
export default {
  name: 'label',
  components: {
    page,
    addLabel,
    editLabel
  },
  data() {
    return {
      options4: [],
      value9: [],
      list: [],
      states: [],
      tableData: [], // 标签信息
      multipleSelection: [], // 全选
      // 分页和模糊查询
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        name: '',
        search: '',
        orderBy: 'id'
      },
      loading: false, // 加载
      check: false, // 查看
      searchParams: {},
      restaurants: [],
      labelList: []
    }
  },
  // 获取列表标签数据
  mounted() {
    // this.list = this.states.map(item => {
    //   return { value: item, label: item }
    // })
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      this.searchParams.page = this.form.page
      if (this.form.name !== '') {
        this.searchParams.form = {
          name: this.form.name
        }
      }
      // 获取所有的标签数据
      pageWithLabel(this.searchParams).then(e => {
        this.tableData = e.data.data.records
        this.form.page.total = parseInt(e.data.data.total)
        this.loading = false
      })
      getAllList().then(response => {
        this.labelList = response.data.data
        for (let i = 0; i < this.labelList.length; i++) {
          this.restaurants[i] = {
            value: this.labelList[i].name
          }
        }
      })
    },
    // 获取标签序号 index [Int] 返回班级序号
    indexMethod(index) {
      return index + 1 + this.form.page.pageCount * (this.form.page.current - 1)
    },
    // 新建标签
    addLabel() {
      this.$refs.add.open()
    },
    // 修改标签信息
    editLabel(id) {
      this.$refs.edit.open(id)
    },
    // 删除标签单条数据
    deleteLabel(index) {
      this.$confirm('很重要的信息，你确定删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        deleteSingleLabel(index).then(e => {
          if (e.data.data) {
            this.tableData.splice(index, 1)
            this.$message({
              type: 'success',
              message: '删除成功！'
            })
            this.getList()
          } else {
            this.$message({
              type: 'error',
              message: '删除失败！'
            })
          }
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 获取复选框被选中的标签数据
    handleSelectionChange(val) {
      this.multipleSelection = val
      console.log(val)
    },
    // 批量删除选中的标签数据
    batchDeleted() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要删除的标签')
        return
      } else {
        const ids = this.multipleSelection.map(b => b.id)
        this.delete(ids)
      }
    },
    // 删除方法
    delete(ids) {
      this.$cfm('确定删除标签记录吗？', e => {
        delBatches(ids).then(e => {
          if (e.data.data) {
            this.$sucmsg('删除成功')
            this.getList()
          } else {
            this.$errmsg('删除失败')
            this.getList()
          }
        })
      })
    },
    // 重置搜索条件
    resetForm() {
      this.searchParams.form = {
      }
      this.$refs.form.resetFields()
      this.getList()
    },
    querySearch(queryString, cb) {
      var restaurants = this.restaurants
      var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0)
      }
    },
    loadAll() {
      return this.labelList
    },
    handleSelect(item) {
      console.log(item)
    },
    remoteMethod(query) {
      if (query !== '') {
        this.loading = true
        setTimeout(() => {
          this.loading = false
          chooseList(query).then(e => {
            this.options4 = e.data.data
            console.log(this.options4)
          })
          // this.options4 = this.list.filter(item => {
          //   return item.label.toLowerCase()
          //     .indexOf(query.toLowerCase()) > -1
          // })
        }, 200)
      } else {
        this.options4 = []
      }
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

