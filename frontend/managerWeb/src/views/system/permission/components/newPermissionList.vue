<template>
<div>
    <el-dialog :visible.sync="visible" title="新增权限列表">
        <el-table :data="permissions">
            <!-- <el-table-column type="selection"></el-table-column> -->
            <el-table-column type="index"></el-table-column>
            <el-table-column prop="name" label="权限名称">
                <template slot-scope="scope">
                    <div class="tip-container">
                      <span class="tip el-icon-check" v-if="scope.row.displayName"></span>  {{scope.row.name}}        
                    </div>   
                </template>
            </el-table-column>
            <el-table-column prop="displayName" label="显示名称">
                <template slot-scope="scope">
                    <div v-if="showIndex===scope.$index">
                        <el-input v-model="scope.row.extension.displayName"></el-input>
                    </div>
                    <div v-else>
                        {{scope.row.displayName}}
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="description" label="权限描述">
                <template slot-scope="scope">
                    <div v-if="showIndex===scope.$index">
                        <el-input v-model="scope.row.extension.description"></el-input>
                    </div>
                    <div v-else>
                        {{scope.row.description}}
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="oper">
                <template slot-scope="scope">
                    <el-button plain v-if="showIndex!==scope.$index" @click.native.prevent="edit(scope)" type="primary" icon="el-icon-edit"></el-button>
                    <template v-else>
                        <el-button plain @click.native.prevent="sure(scope)" type="success" icon="el-icon-check"></el-button>
                        <el-button plain @click.native.prevent="cancel(scope)" type="warning" icon="el-icon-close"></el-button>                       
                    </template>
                </template>
            </el-table-column>

        </el-table>
        <span slot="footer">
                <el-button @click="visible=false">取消</el-button>
                <el-button type="primary" @click="save">确定</el-button>
        </span>

    </el-dialog>

</div>
</template>

<script>
import {
  batchPermissions
} from '@/api/permission'
export default {
  props: [''],
  data() {
    return {
      visible: false,
      permissions: [],
      showIndex: -1

    }
  },
  methods: {
    open(permissions) {
      this.visible = true
      this.init(permissions)
    },
    init(permissions) {
      this.permissions = []
      permissions.forEach(e => {
        const obj = {
          name: e,
          displayName: undefined,
          description: undefined,
          extension: {
            displayName: undefined,
            description: undefined
          }

        }
        this.permissions.push(obj)
      })
    },
    edit(scope) {
      this.showIndex = scope.$index
    },
    sure(scope) {
      this.showIndex = -1
      scope.row.displayName = scope.row.extension.displayName
      scope.row.description = scope.row.extension.description
    },
    cancel(scope) {
      this.showIndex = -1
      scope.row.extension.displayName = undefined
      scope.row.extension.description = undefined
    },
    find(name) {
      return this.permissions.find(b => b.extension.name === name)
    },
    show(row) {
      return row.extension.edit
    },
    save() {
      const params = []
      this.permissions.forEach(e => {
        const data = Object.assign({}, e)
        delete data.extension
        if (data.displayName) {
          params.push(data)
        }
      })
      if (params.length === 0) {
        this.$errmsg('暂无可添加的数据，请完善权限的显示名称')
        return
      }
      batchPermissions(params).then(e => {
        if (e.data.success) {
          this.$sucmsg('添加成功')
          this.visible = false
          this.$emit('success')
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
    .tip-container{
        display: flex;
    }
    .tip{
        width: 20px;
        height: 20px;
        display: block;
        border-radius: 20px;
        background-color: green;
        font-size: 14px;
        line-height: 20px;
        text-align: center;
        color: #fff;        
        margin-right: 5px;
    }
</style>
