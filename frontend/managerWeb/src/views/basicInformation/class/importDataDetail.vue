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
        <el-table-column prop="name" label="班级名称" align="center">
        </el-table-column>
        <el-table-column prop="professionName" label="专业" align="center">
        </el-table-column>
        <el-table-column prop="year" label="年级" align="center">
        </el-table-column>
        <el-table-column prop="schoolLength" label="学制" align="center">
        </el-table-column>
        <el-table-column prop="counsellor" label="导员" align="center">
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" @click="editClass(scope.row)"></el-button>
            <el-button type="danger" icon="el-icon-delete" @click="deleteClass(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <page :page="page"></page>

    </el-dialog>
    <import-class ref="edit" @success="onSuccess"></import-class>
  </div>



</template>

<script>
/**
 * @file 导入数据基本组件
 * @author 赵雪丽
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/06
 */
import page from '@/components/page'
import importClass from './importClass'
import {
  createBatches,
  deleteSingleClass
} from '@/api/base/class'

export default {
  components: {
    page,
    importClass
  },
  data() {
    return {
      ource: [], // 元数据
      importData: [], // 导入数据
      bindData: [], // 绑定数据
      visiable: false, // 可见
      checks: [], // 选中
      status: 0, // 搜索条件
      // 分页
      page: {
        pageCount: 10,
        current: 1,
        total: 0
      }
    }
  },
  computed: {
    // 分页绑定数据
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
    // 打开
    open(importData) {
      this.visiable = true
      this.source = importData
      this.getList(this.source)
    },
    // 获取列表
    getList(data) {
      this.importData = data
      this.page.total = data.length
    },
    // 关闭
    close() {
      this.status = 0
      this.visiable = false
    },
    // 编辑
    editClass(entity) {
      this.$refs.edit.open(entity)
    },
    // 保存
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
      this.$cfm(msg, e => {
        for (let i = 0; i < this.source.length; i++) {
          this.source[i].professionId = this.source[i].professionId
        }
        createBatches(this.source.filter(b => b.success)).then(e => {
          if (e.data.success) {
            this.$sucmsg('添加成功！')
          } else {
            this.$errmsg('添加失败！')
          }
          this.close()
        })
      })
    },
    onSuccess(entity) {
      const len = this.source.filter(b => b.name === entity.name).length
      if (len > 1) {
        entity.success = false
        entity.tips = '列表中存在相同的' + entity.name + '课程名称'
      }
    },
    // 搜索条件改变（下拉框）
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
    },
    // 删除班级单条数据
    deleteClass(index) {
      this.$confirm('很重要的信息，你确定删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        deleteSingleClass(index).then(e => {
          if (e.data) {
            this.tableData3.splice(index, 1)
            this.$message({
              type: 'success',
              message: '删除成功！'
            })
            this.getList()
          } else {
            this.$message({
              type: 'error',
              message: '对应班级下还有学生数据，删除失败！'
            })
          }
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  }

}

</script>

<style scoped lang="scss">
  .btns {
    padding-bottom: 5px;
  }

</style>
