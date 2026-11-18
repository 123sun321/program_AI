<template>
<el-dialog :visible.sync="visible" title="数据权限" v-loading="loading" @close="onClose">
    <div class="container">
        <el-table :data="data">
            <el-table-column type="index" width="50">
            </el-table-column>
            <el-table-column prop="table" label="表名称" width="120"></el-table-column>
            <el-table-column prop="name" label="数据范围">
                <template slot-scope="scope">
                    <el-radio-group v-model="types[scope.$index].type">
                        <el-radio-button :label="0">全部</el-radio-button>
                        <el-radio-button :label="1">本校</el-radio-button>
                        <el-radio-button :label="2">本院系</el-radio-button>
                        <el-radio-button :label="3">自己</el-radio-button>
                    </el-radio-group>
                    <el-button icon="el-icon-close" type="text" @click="clear(scope.$index)">清除</el-button>
                </template>
            </el-table-column>
            <el-table-column prop="condition" label="匹配表达式">
                <template slot-scope="scope">
                    {{getCondition(scope.row.table,scope.$index)}}
                </template>
            </el-table-column>
        </el-table>
    </div>

    <template slot="footer">
        <el-button @click="visible=false">取消</el-button>
        <el-button type="primary" @click="save" :loading="loading">保存</el-button>
    </template>
</el-dialog>
</template>

<script>
import {
  fetchCondition,
  fetch,
  add
} from '@/api/permission/data'
export default {
  data() {
    return {
      visible: false,
      conditions: [],
      data: [],
      dataPermission: {},
      types: [],
      roleId: 0,
      loading: false

    }
  },
  created() {
    this.getConditions()
  },
  methods: {
    open(id) {
      this.visible = true
      this.roleId = id
      this.getDataPermission(id)
    },
    getConditions() {
      fetchCondition().then(e => {
        const tmp = []
        e.data.data.forEach(e => {
          const count = tmp.filter(b => b.table === e.table).length
          if (count === 0) {
            tmp.push(e)
            this.types.push({ table: e.table, type: undefined })
          }
        })
        this.conditions = e.data.data
        this.data = tmp
      })
    },
    getDataPermission() {
      this.loading = true
      fetch(this.roleId).then(e => {
        this.loading = false
        this.dataPermission = e.data.data
        this.dataPermission.tables.forEach(e => {
          const type = this.types.find(b => b.table === e.tableName)
          if (type) {
            Object.assign(type, { table: e.tableName, type: e.dataType })
          }
        })
      })
    },
    getCondition(tableName, index) {
      if (this.types[index].type === undefined) {
        return '无匹配'
      } else {
        const type = this.types[index].type
        let condition = this.conditions.find(b => b.table === tableName && b.dataType === type)
        if (!condition) {
          condition = this.conditions.find(b => b.table === tableName && b.def)
        }
        if (!condition) {
          return '无匹配'
        } else {
          return condition.condition
        }
      }
    },
    getTypeName(type) {
      switch (type) {
        case 0:
          return '全部'
        case 1:
          return '本学校'
        case 2:
          return '本院系'
        case 3:
          return '自己'
      }
    },
    clear(index) {
      this.$cfm('确定清除吗?', e => {
        this.types[index].type = undefined
      })
    },
    save() {
      this.$cfm('确定保存当前的数据权限吗?', e => {
        const data = {
          roleId: this.roleId,
          tables: []
        }
        data.tables = this.types.filter(b => b.type).map(b => {
          return {
            tableName: b.table,
            dataType: b.type
          }
        })
        add(data).then(e => {
          this.loading = true
          if (e.data.success) {
            this.$sucmsg('保存成功')
            this.visible = false
          } else {
            this.$sucmsg('保存失败')
          }
          this.loading = false
        })
      })
    },
    onClose() {
      this.types.forEach(b => {
        b.type = undefined
      })
    }
  }

}
</script>

<style scoped lang="scss">
.container{
    position: relative;
}

</style>
