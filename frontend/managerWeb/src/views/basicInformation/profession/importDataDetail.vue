<template>
  <div>

    <el-dialog title="批量导入" :visible.sync="visiable" width="90%">
      <el-form :inline="true">
        <el-form-item label="验证状态">
          <el-select v-model="status" @change="statusChange">
            <el-option :value="0" label="全部"></el-option>
            <el-option :value="1" label="验证成功"></el-option>
            <el-option :value="2" label="验证失败"></el-option>
          </el-select>
          <el-form-item>
            <el-button @click="save" type="primary">确定导入</el-button>
          </el-form-item>
        </el-form-item>
      </el-form>
      <el-table ref="multipleTable" :data="pageList" tooltip-effect="dark" style="width: 100%">

        <el-table-column type="index" width="50">
        </el-table-column>
        <el-table-column type="tips">
          <template slot-scope="scope">
            <div style="color:red;">{{scope.row.tips}}</div>
          </template>
        </el-table-column>
            <el-table-column
              prop="name"
              label="专业名称"
              align="center"
              >
            </el-table-column>
            <el-table-column
              prop="code"
              label="专业编号"
              align="center"
              >
            </el-table-column>
             <el-table-column
              label="操作"
              align="center"
              >
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" @click="editProfession(scope.row)"></el-button>
            <el-button type="danger" icon="el-icon-delete" @click="singleDelete(scope)"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <page :page="page"></page>

    </el-dialog>
    <import-profession ref="edit" @success="onSuccess"></import-profession>
  </div>

</template>

<script>
/*
批量导入专业验证信息组件
@author 袁延延
@date 2025/11/28 13:30
@version 1.0
*/
import page from '@/components/page'
import importProfession from './importProfession'
import {
  multipleAdd
} from '@/api/base/professions'

export default {
  components: {
    page,
    importProfession
  },
  data() {
    return {
      source: [], // 原始数据
      importData: [], // 导入数据
      visiable: false, // 批量导入弹框
      status: 0,
      // 获取分页数据
      page: {
        pageCount: 10,
        current: 1,
        total: 0
      }
    }
  },
  computed: {
    // 绑定的分页数据
    pageList() {
      const skip = (this.page.current - 1) * this.page.pageCount
      const rtn = []
      for (let i = skip, j = 0; i < this.importData.length; i++, j++) {
        rtn[j] = this.importData[i]
      }
      return rtn
    }
  },
  methods: {
    // 打开导入数据
    open(importData) {
      this.visiable = true
      this.source = importData
      this.getList(this.source)
    },
    // 获取导入数据
    getList(data) {
      this.importData = data
      this.page.total = data.length
    },
    // 关闭导入数据
    close() {
      this.status = 0
      this.visiable = false
    },
    // 修改专业
    editProfession(entity) {
      this.$refs.edit.open(entity)
    },
    // 删除单条专业
    singleDelete(scope) {
      const curData = scope.row
      const index = this.source.indexOf(curData)

      this.$cfm('确定删除当前这一项吗？', e => {
        this.source.splice(index, 1)
        this.getList(this.source)
      })
    },
    // 保存导入数据
    save() {
      const len_suc = this.source.filter(b => b.success).length
      let msg = ''
      if (len_suc === 0) {
        this.$warnmsg('当前无可导入的数据')
        return
      }
      const len_fail = this.source.filter(b => !b.success).length
      if (len_fail > 0) {
        msg = '有' + len_fail + '条数据验证未通过,继续导入吗？'
      } else {
        msg = '确定导入' + len_suc + '条数据吗？'
      }
      this.source.forEach(e => {
        e.gradeList = []
      })
      this.$cfm(msg, e => {
        multipleAdd(this.source.filter(b => b.success)).then(e => {
          const data = e.data
          if (data.success) {
            this.$sucmsg(data.data)
          } else {
            this.$errmsg(data.data)
          } this.$emit('success', data.data)
          this.close()
        })
      })
    },
    // 获取保存导入数据验证信息
    onSuccess(entity) {
      let len = this.source.filter(b => b.name === entity.name).length
      if (len > 1) {
        entity.success = false
        entity.tips = '列表中存在相同的' + entity.name + '专业名称'
      }
      len = this.source.filter(b => b.code === entity.code).length
      if (len > 1) {
        entity.success = false
        entity.tips = '列表中存在相同的' + entity.code + '专业名称'
      }
    },
    // 获取验证状态
    statusChange() {
      let data
      if (this.status === 1) {
        data = this.source.filter(b => b.success === true)
      } else if (this.status === 2) {
        data = this.source.filter(b => b.success === false)
      } else {
        data = this.source
      }
      this.getList(data)
    }
  }

}

</script>

<style scoped lang="scss">
  .btns {
    padding-bottom: 5px;
  }

</style>
