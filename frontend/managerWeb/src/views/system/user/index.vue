<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select clearable style="width: 170px" class="filter-item" v-model="listQuery.userType" placeholder="用户类型">
         <el-option v-for="item in userTypeOptions" :key="item.key" :label="item.label" :value="item.key">
        </el-option>
      </el-select>
      <el-input @keyup.enter.native="handleFilter" style="width: 250px;" class="filter-item" placeholder="按姓名、手机号、邮箱搜索" v-model="listQuery.keyword">
      </el-input>
      <span class="filter-item" style="color: #656363;">创建时间：</span>
      <el-date-picker class="filter-item" style="display:inline-flex"
          v-model="selectDate"
          type="daterange"
          text="111"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-edit">添加用户</el-button>
    </div>

    <el-table class="userTable" :key='tableKey' :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row
      style="width: 100%">
      <el-table-column align="center" label="编号" width="65">
        <template slot-scope="scope">
          <span>{{scope.row.id}}</span>
        </template>
      </el-table-column>
      <el-table-column width="200px" align="left" label="账户">
        <template slot-scope="scope">

          <img :src="scope.row.userPic" class="userPic"/>
          <span>{{scope.row.userName}}</span>
        </template>
      </el-table-column>
      <el-table-column min-width="90px" label="姓名">
        <template slot-scope="scope">
            <span>{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column width="80px" align="center" label="性别">
        <template slot-scope="scope">
          <span>{{scope.row.sex |sixFilter}}</span>
        </template>
      </el-table-column>
      <el-table-column width="120px" align="center" label="手机">
        <template slot-scope="scope">
          <span>{{scope.row.phoneNumber}}</span>
        </template>
      </el-table-column>
      <el-table-column width="200px" label="邮箱">
        <template slot-scope="scope">
          {{scope.row.emailAddress}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" width="100">
        <template slot-scope="scope">          
          <el-tag v-if="scope.row.active">激活</el-tag>
          <el-tag v-if="!scope.row.active" type="info" >未激活</el-tag>
          <el-tag v-if="scope.row.lockoutEnabled" type="danger">锁定</el-tag>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="创建时间" width="100">
        <template slot-scope="scope">
          {{scope.row.creationTime | formatTime('{y}-{m}-{d}')}}
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="上次登录" width="150">
        <template slot-scope="scope">
          {{scope.row.lastLoginTime | formatTime('{y}-{m}-{d} {h}:{i}')}}
        </template>
      </el-table-column>
      <el-table-column align="center" :label="$t('table.actions')" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">{{$t('table.edit')}}</el-button>
          <el-button type="primary" size="mini" @click="handleRest(scope.row)"> 重置 </el-button>
          <el-button size="mini" type="danger" @click="handleModifyStatus(scope.row,'deleted')">{{$t('table.delete')}}

          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageIndex" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
      <el-dialog class="mydia"
  :title="currentEditId?'编辑用户':'添加用户'"
  :visible.sync="dialogFormVisible"
  width="50%"
  >
  <userEdit :id="currentEditId" @ok="dialogFormVisible=false;getList()" @cancel="dialogFormVisible=false"></userEdit>

</el-dialog>
  </div>

</template>

<script>
// import { fetchList, fetchPv, createArticle, updateArticle } from '@/api/article'
import { fetchList, deleteUser, resetPassword } from '@/api/permission/user'
import waves from '@/directive/waves' // 水波纹指令
// import { parseTime } from '@/utils'
import userEdit from './components/userEdit'

export default {
  name: 'userManager',
  components: {
    userEdit
  },
  directives: {
    waves
  },
  data() {
    return {
      selectDate: undefined,
      dialogFormVisible: false,
      tableKey: 0,
      list: null,
      total: null,
      listLoading: true,
      currentEditId: undefined,
      listQuery: {
        pageIndex: 1,
        pageSize: 10,
        userType: undefined,
        keyword: undefined,
        startDate: undefined,
        endDate: undefined,
        orderFiled: 'id',
        isAsc: true
      },
      userTypeOptions: [{ label: '前台用户', key: '1' }, { label: '店铺管理用户', key: '2' }, { label: '后台运营管理用户', key: '4' }]
    }
  },
  filters: {
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      if (this.selectDate) {
        this.listQuery.startDate = this.selectDate[0]
        this.listQuery.endDate = this.selectDate[1]
      } else {
        this.listQuery.startDate = undefined
        this.listQuery.endDate = undefined
      }
      fetchList(this.listQuery).then(response => {
        this.list = response.data.records
        this.total = Number(response.data.total)
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.pageIndex = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.pageIndex = val
      this.getList()
    },
    handleModifyStatus(row, status) {
      // this.$message({
      //   message: '操作成功',
      //   type: 'success'
      // })
      // row.status = status
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteUser(row.id).then(response => {
          this.getList()
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        status: 'published',
        type: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.currentEditId = null
      this.currentEditId = undefined
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        // this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
          this.temp.author = 'vue-element-admin'
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.currentEditId = row.id
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        // this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          /* updateArticle(tempData).then(() => {
            for (const v of this.list) {
              if (v.id === this.temp.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, this.temp)
                break
              }
            }
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })*/
        }
      })
    },
    handleDelete(row) {
      this.$notify({
        title: '成功',
        message: '删除成功',
        type: 'success',
        duration: 2000
      })
      const index = this.list.indexOf(row)
      this.list.splice(index, 1)
    },
    handleFetchPv(pv) {

    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
        const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
        const data = this.formatJson(filterVal, this.list)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'table-list'
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          // return parseTime(v[j])
          return v[j]
        } else {
          return v[j]
        }
      }))
    },
    handleRest(row) {
      this.$cfm('确定重置密码吗？', e => {
        resetPassword(row.userName).then(e => {
          if (e.data) {
            this.$sucmsg('重置成功')
          } else {
            this.$sucmsg('重置失败')
          }
        })
      })
    }
  }
}
</script>
<style scoped>
.userPic{
  width:30px;
  height:30px;
  border-radius: 50%;
  margin-right: 10px;
}
.userTable >>> .el-table__row td:nth-child(2) .cell{display: flex;line-height: 30px;}
.userTable >>> .el-table__row td:nth-child(7) .cell{padding: 0px;}
.mydia >>> .el-dialog__body{
  padding: 0px 15px 15px 15px;  
}
</style>
