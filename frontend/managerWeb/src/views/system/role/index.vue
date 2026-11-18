<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 250px;" class="filter-item" placeholder="按名称搜索" v-model="listQuery.keyword">
      </el-input>     
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-edit">添加角色</el-button>
    </div>

    <el-table class="userTable" :key='tableKey' :data="list" v-loading="listLoading" element-loading-text="给我一点时间" border fit highlight-current-row
      style="width: 100%">
      <el-table-column align="center" label="编号" width="65">
        <template slot-scope="scope">
          <span>{{scope.row.id}}</span>
        </template>
      </el-table-column>
      <el-table-column width="150px" align="left" label="角色名称">
        <template slot-scope="scope">
          <span>{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column min-width="90px" label="显示名称">
        <template slot-scope="scope">
            <span>{{scope.row.displayName}}</span>
        </template>
      </el-table-column>
      <el-table-column width="120px" align="center" label="系统角色">
        <template slot-scope="scope">
          <el-switch
          v-model="scope.row.isStatis"
          disabled>
        </el-switch>
        </template>
      </el-table-column>
      <el-table-column width="120px" align="center" label="默认角色">
        <template slot-scope="scope">
          <el-switch
          v-model="scope.row.isDefault"
          disabled>
        </el-switch>
        </template>
      </el-table-column>
       <el-table-column width="120px" align="center" label="禁用租户过滤">
        <template slot-scope="scope">
          <el-switch
          v-model="scope.row.disableTenantFilter"
          disabled>
        </el-switch>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('table.actions')" width="350" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button plain type="primary" size="mini" @click="handleUpdate(scope.row)">{{$t('table.edit')}}</el-button>
          <router-link  :to="{ path: '/systemManager/roleUser/'+scope.row.id, params: { name:scope.row.displayName}}">
           <el-button plain type="primary" style="width:70px" size="mini">角色成员</el-button>
          </router-link>
          <el-button plain type="primary" style="width:70px" size="mini" @click="openDataPermission(scope.row.id)">数据权限</el-button>
          
          <el-button plain size="mini" type="danger" @click="handleModifyStatus(scope.row,'deleted')">{{$t('table.delete')}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageIndex" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
    <el-dialog class="editRole"
      :title="currentEditId?'编辑角色':'添加角色'"
      :visible.sync="dialogFormVisible"
      width="50%"
      >
      <role-edit :id="currentEditId" @ok="dialogFormVisible=false;getList()" @cancel="dialogFormVisible=false"></role-edit>
    </el-dialog>
    <data-permission ref="dataPermission"></data-permission>
  </div>

</template>
<script>
import { fetchList, deleteRole } from '@/api/permission/role'
import waves from '@/directive/waves' // 水波纹指令
import roleEdit from './components/roleEdit'
import dataPermission from './components/dataPermission'

export default {
  name: 'roleManager',
  components: {
    roleEdit,
    dataPermission
  },
  directives: {
    waves
  },
  data() {
    return {
      selectDate: undefined,
      dialogFormVisible: false,
      dialogTabActive: 'base',
      dialogEditData: {},
      tableKey: 0,
      list: null,
      total: null,
      listLoading: true,
      currentEditId: undefined,
      listQuery: {
        pageIndex: 1,
        pageSize: 10,
        keyword: undefined,
        orderFiled: 'id',
        isAsc: true
      }
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
      if (row.isStatis) {
        this.$message({
          type: 'error',
          message: '系统角色不允许删除！'
        })
        return
      }
      this.$confirm('此操作将永久删除该角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRole(row.id).then(response => {
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
    handleCreate() {
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
    openDataPermission(id) {
      this.$refs.dataPermission.open(id)
    }

  }
}
</script>
<style scoped>
.editRole >>> .el-dialog__body{padding-top: 5px;}
</style>
