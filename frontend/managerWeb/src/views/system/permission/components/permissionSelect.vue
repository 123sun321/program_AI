<template>
  <div class="permissionTable">
    <zk-table
      ref="table"
      sum-text="sum"
      index-text="#"
      :data="data"
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
        <el-switch
        v-model="scope.row.isGrantedByDefault"
        disabled>
      </el-switch>
      </template>
      <template slot="grantTemplete" slot-scope="scope">
        
        <el-checkbox @change="grantCheckClick(scope.row)" v-model="scope.row.grant"></el-checkbox>
      </template>
      <template slot="rejectTemplete" slot-scope="scope">
        <el-checkbox @change="rejectCheckClick(scope.row)" v-model="scope.row.reject"></el-checkbox>
      </template>
      <template slot="dataTemplate" slot-scope="scope">
        <el-select v-model="scope.row.dataType" :disabled="!scope.row.grant" @change="onDatatypeChange(scope.row)">
          <el-option :value="0" label="全部"></el-option>
          <el-option :value="1" label="全校"></el-option>
          <el-option :value="2" label="本院系"></el-option>
          <el-option :value="3" label="自己"></el-option>
        </el-select>
      </template>
    </zk-table>  
  </div>
</template>
<script>
import ZkTable from 'vue-table-with-tree-grid'
import { fetchPermissions } from '@/api/permission'
import Vue from 'vue'
export default {
  name: 'permissionSelect',
  components: {
    [ZkTable.name]: ZkTable
  },
  model: {
    prop: 'selectPermissions',
    event: 'change'
  },
  props: {
    selectPermissions: Array
  },
  data() {
    return {
      props: {
        stripe: true,
        border: true,
        showHeader: true,
        showSummary: false,
        showRowHover: true,
        showIndex: false,
        treeType: true,
        isFold: false,
        expandType: false,
        selectionType: false
      },
      data: [],
      columns: [
        {
          label: '权限名称',
          prop: 'name',
          width: '300px'
        },
        {
          label: '显示名称',
          prop: 'displayName',
          minWidth: '130px'
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
          label: '授予',
          prop: 'grant',
          type: 'template',
          template: 'grantTemplete'
        },
        {
          label: '拒绝',
          prop: 'reject',
          type: 'template',
          template: 'rejectTemplete'
        }
        // {
        //   label: '数据权限',
        //   prop: 'dataType',
        //   type: 'template',
        //   minWidth: '130px',
        //   template: 'dataTemplate'
        // }
      ]
    }
  },
  watch: {
    selectPermissions() {
      // this.bindRoleInfo()
      this._bindPermission(this.selectPermissions)
    }
  },
  computed: {
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      fetchPermissions().then(Response => {
        this.data = Response.data.data
        this._buildChildData(null, this.data)
        this._bindPermission(this.selectPermissions)
      })
    },
    _buildChildData(parent, children) {
      if (children === null) {
        return
      }
      for (var i = 0; i < children.length; i++) {
        children[i].parent = parent
        children[i].grant = false
        children[i].reject = false
        children[i].dataType = undefined
        this._buildChildData(children[i], children[i].children)
      }
    },
    _selectChildPermission(data, selectValue) {
      if (data) {
        data.forEach(item => {
          var bindData = this.$refs.table.bodyData.find((value, index, arr) => {
            return value.id === item.id
          })
          if (selectValue) {
            if (!bindData.reject) {
              bindData.grant = true
              bindData.dataType = 3
            }
          } else {
            bindData.grant = false
            bindData.dataType = undefined
          }
          this._selectChildPermission(item.children, selectValue)
        })
      }
    },
    grantCheckClick(data) {
      if (data.grant) {
        data.reject = !data.grant
        data.dataType = 3
        // 级联选中下级权限
        console.log(this.$refs.table.bodyData)
        this._selectChildPermission(data.children, true)
      } else {
        data.dataType = undefined
        this._selectChildPermission(data.children, false)
      }
      this._changePermission()
    },
    rejectCheckClick(data) {
      if (data.reject) {
        data.dataType = undefined
        data.grant = !data.reject
      }
      this._changePermission()
    },
    onDatatypeChange(row) {
      this._changePermission()
    },
    _changePermission() {
      var newPermissions = []
      this.$refs.table.bodyData.forEach(item => {
        if (item.grant || item.reject) {
          var newItem = { id: item.id, name: item.name }
          if (item.grant) {
            newItem.grant = true
            newItem.dataType = item.dataType
          } else if (item.reject) {
            newItem.grant = false
            newItem.dataType = undefined
          }
          newPermissions.push(newItem)
        }
      })
      // this.selectPermissions.clear()
      var deletePermission = []
      this.selectPermissions.forEach(item => {
        var bindData = newPermissions.find((value, index, arr) => {
          return value.id === item.id
        })
        if (bindData) {
          item.grant = bindData.grant
          Vue.set(item, 'dataType', bindData.dataType)
        } else {
          // 删除权限
          deletePermission.push(item)
        }
        newPermissions.splice(newPermissions.indexOf(bindData), 1)
      })
      deletePermission.forEach(item => {
        this.selectPermissions.splice(this.selectPermissions.findIndex(v => v.id === item.id), 1)
      })
      newPermissions.forEach(item => {
        this.selectPermissions.push(item)
      })
      // this.selectPermissions = newPermissions
      this.$emit('change', this.selectPermissions)
    },
    _bindPermission(permissions) { // 绑定权限方法
      this.$nextTick(e => {
        this.$refs.table.bodyData.forEach(item => {
          item.grant = false
          item.reject = false
          item.dataType = undefined
        })
        if (permissions) {
          permissions.forEach(item => {
            var bindData = this.$refs.table.bodyData.find((value, index, arr) => {
              return value.id === item.id
            })

            if (bindData) {
              if (item.grant) {
                bindData.grant = true
                bindData.dataType = item.dataType
              } else {
                bindData.reject = true
              }
            }
          })
        }
      })
      setTimeout(() => {
        // 先清除权限的选择

      }, 1000)
    }

  }
}
</script>
<style scoped>
.permissionTable >>> .zk-checkbox__icon{
border: 1px solid #858996;
}
.permissionTable >>> .zk-table__body-row td:nth-child(2) .zk-table__cell-inner{font-weight: bold;}
.permissionTable >>> .zk-table__body-row td:nth-child(2) .zk-table--tree-icon{font-weight: normal;}
.permissionTable >>> .zk-table__body-row{height: 35px;}
</style>
