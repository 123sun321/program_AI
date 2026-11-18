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
         <el-table-column prop="code" label="学号" align="center">
        </el-table-column>
        <el-table-column prop="name" label="姓名" align="center">
        </el-table-column>
        <el-table-column prop="sex" label="性别" align="center">
        </el-table-column>
        <el-table-column prop="professionName" label="专业名称" align="center">
        </el-table-column>
        <el-table-column prop="gradeName" label="班级" align="center">
        </el-table-column>
        <el-table-column prop="email" label="邮箱" align="center">
        </el-table-column>
        <el-table-column prop="contact" label="手机号" align="center">
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" @click="editStudent(scope.row)"></el-button>
            <el-button type="danger" icon="el-icon-delete" @click="deleteStudent(scope)"></el-button>
          </template>
        </el-table-column>
      </el-table>
      <page :page="page"></page>

    </el-dialog>
    <import-student ref="edit" @success="onSuccess"></import-student>
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
import importStudent from './importStudent'
import {
  createBatches,
  delSingleStudent
} from '@/api/base/student'

export default {
  components: {
    page,
    importStudent
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
    editStudent(entity) {
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
        createBatches(this.source).then(e => {
          if (e.data) {
            this.$sucmsg(e.data)
          } else {
            this.$errmsg(e.data)
          }
          this.close()
        })
      })
    },
    onSuccess(entity) {
      const len = this.source.filter(b => b.code === entity.code).length
      if (len > 1) {
        entity.success = false
        entity.tips = '列表中存在相同的' + entity.code + '学生学号'
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
    // 删除单条学生数据
    deleteStudent(index) {
      this.$confirm('很重要的信息，你确定删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        delSingleStudent(index).then(e => {
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
    }
  }

}

</script>

<style scoped lang="scss">
  .btns {
    padding-bottom: 5px;
  }

</style>
