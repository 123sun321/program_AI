<template>
<div class="app-container common-layout">
    <div class="common-form-operation">
        <div class="commmon-from">
            <!-- <div  class="course-page-search"> -->
            <el-form :inline="true" :model="form" ref="form" class="demo-form-inline">              
                <el-form-item label="教师:" prop="search">
                    <el-input v-model="form.search" placeholder="按课程名称查询" prefix-icon="el-icon-search">
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button icon="el-icon-search" type="primary" @click="getList()">搜索</el-button>
                </el-form-item>
            </el-form>
        </div>
        <!-- <div class="common-operation">
            <el-button type="primary">新建</el-button>
        </div> -->
    </div>
    <div class="common-table">
      <zk-table ref="table" 
        sum-text="sum" 
        index-text="#" 
        :data="permissions" 
        :columns="columns" 
        :stripe="props.stripe" 
        :border="props.border" 
        :show-header="props.showHeader" 
        :show-summary="props.showSummary" 
        :show-row-hover="props.showRowHover" 
        :show-index="props.showIndex" 
        :tree-type="props.treeType" 
        :is-fold="props.isFold" 
        :expand-type="props.expandType" 
        :selection-type="props.selectionType">
            <template slot="grant" slot-scope="scope">
                <el-switch v-model="scope.row.isGrantedByDefault" disabled>
                </el-switch>
            </template>
            <template slot="action" slot-scope="scope">
              
              <el-button type="primary" size="mini" @click="edit(scope.row.id,false)">{{$t('table.edit')}}</el-button>
                <!-- 
                <el-button type="primary" size="mini" @click="showDialog(scope.row,true)">添加子权限</el-button>
                <el-button size="mini" type="danger" @click="handleModifyStatus(scope.row,'deleted')">{{$t('table.delete')}}</el-button> -->
            </template>
        </zk-table>        
        <new-permission-list ref="newPermissionList" :permissions="newPermissions" @success="getList"></new-permission-list>
        <permisision ref="permission" @success="getList"></permisision>
    </div>
</div>
</template>

<script>
import page from '@/components/page'
import newPermissionList from './components/newPermissionList'
import permisision from './components/permission'
import {
  fetchPermissions,
  syncPermission

} from '@/api/permission'
import ZkTable from 'vue-table-with-tree-grid'
import {
  search
} from '@/mixins'
export default {
  mixins: [search],
  components: {
    page,
    newPermissionList,
    [ZkTable.name]: ZkTable,
    permisision
  },
  data() {
    return {
      permissions: [],
      newPermissions: [],
      props: {
        stripe: true,
        border: true,
        showHeader: true,
        showSummary: false,
        showRowHover: true,
        showIndex: false,
        treeType: true,
        isFold: true,
        expandType: false,
        selectionType: false
      },
      columns: [
        {
          label: '权限名称',
          prop: 'name',
          width: '400px'
        },
        {
          label: '显示名称',
          prop: 'displayName',
          minWidth: '50px'
        },
        {
          label: '默认授予',
          prop: 'isGrantedByDefault',
          type: 'template',
          template: 'grant'
        },
        {
          label: '关联特性',
          prop: 'featureDependency'
        },
        {
          label: '操作',
          prop: 'id',
          minWidth: '200px',
          type: 'template',
          template: 'action'
        }
      ]
    }
  },
  created() {
    this.check()
    this.getList()
  },
  methods: {
    getList() {
      fetchPermissions().then(e => {
        this.permissions = e.data.data
      })
    },
    check() {
      syncPermission().then(e => {
        this.newPermissions = e.data.data
        this.syncTip()
      })
    },
    syncTip() {
      if (this.newPermissions.length > 0) {
        this.$cfm('检测到新的权限,是否导入数据库？', e => {
          this.$refs.newPermissionList.open(this.newPermissions)
        })
      }
    },
    edit(id) {
      this.$refs.permission.open(id)
    }
  }
}
</script>

<style scoped lang="scss">

</style>
