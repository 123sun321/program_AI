<template>
  <div>
    <div class="titleInfo">
      当前角色名称：<el-tag style="width: 100px; text-align: center;margin-right: 30px;font-weight: bold;">{{roleInfo.displayName}}</el-tag>  角色成员数量：<el-tag>{{total}}</el-tag>
    </div>
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
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-edit">选择用户</el-button>
       <el-button class="filter-item" type="success" v-waves icon="el-icon-refresh" @click="handleFilter">刷新</el-button>
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
      <el-table-column align="center" :label="$t('table.actions')" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleModifyStatus(scope.row,'deleted')">移除</el-button>
        </template>
      </el-table-column>      
    </el-table>

    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageIndex" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
    </div>
  <el-dialog
  title="选择用户"
  :visible.sync="dialogFormVisible"
  width="70%"
  >
  <roleUserListAdd :id="listQuery.roleId"></roleUserListAdd>
</el-dialog>
  </div>

</template>

<script>
import { getRoleInfo, getUserListByRole, removeUser } from '@/api/permission/role'
import waves from '@/directive/waves' // 水波纹指令
import roleUserListAdd from './roleUserListAdd'
export default {
  name: 'roleUserList',
  directives: {
    waves
  },
  components: {
    roleUserListAdd
  },
  data() {
    return {
      id: undefined,
      roleInfo: {},
      selectDate: undefined,
      dialogFormVisible: false,
      tableKey: 0,
      list: null,
      total: null,
      listLoading: true,
      currentEditId: undefined,
      listQuery: {
        roleId: undefined,
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
  created() {
    const id = this.$route.params && this.$route.params.id
    this.listQuery.roleId = id
    // this.name = this.$route.params && this.$route.params.name
    this.getRoleInfo()
    this.getList()
    // if (this.isEdit) {
    //   const id = this.$route.params && this.$route.params.id
    //   this.fetchData(id)
    // } else {
    //   this.postForm = Object.assign({}, defaultForm)
    // }
  },
  methods: {
    getRoleInfo() {
      getRoleInfo(this.listQuery.roleId).then(response => {
        this.roleInfo = response.data.role
      })
    },
    getList() {
      this.listLoading = true
      if (this.selectDate) {
        this.listQuery.startDate = this.selectDate[0]
        this.listQuery.endDate = this.selectDate[1]
      } else {
        this.listQuery.startDate = undefined
        this.listQuery.endDate = undefined
      }
      getUserListByRole(this.listQuery).then(response => {
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
      this.$confirm('此操作将从角色中移除用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        removeUser(this.listQuery.roleId, row.id).then(response => {
          this.getList()
          this.$message({
            type: 'success',
            message: '移除成功!'
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消移除'
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
    }
  }
}
</script>
<style scope>
.titleInfo{
  background-color: beige;    
  line-height: 30px;
  padding: 10px;
  margin-bottom: 0px;
}
.userPic{
  width:30px;
  height:30px;
  border-radius: 50%;
  margin-right: 10px;
}
.userTable >>> .el-table__row td:nth-child(2) .cell{display: flex;line-height: 30px;}
.userTable >>> .el-table__row td:nth-child(7) .cell{padding: 0px;}
</style>


