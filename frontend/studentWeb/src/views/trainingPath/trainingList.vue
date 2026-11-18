<template>
  <div>
    <div class="table-container" style="padding: 0 20px;background: #fff;">
      <el-button type="danger" class="delBtn" @click="delTraining" style="margin: 10px;">删除</el-button>
      <el-table :data="trainingData"  v-loading='loading' @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column type="index" label="序号" align="center" width="100"></el-table-column>
        <el-table-column label="名称" align="left">
          <template slot-scope="scope">
          <i class="el-icon-document"></i>
          <span style="margin-left: 10px;cursor:pointer;" @click="pathToRuntraining(scope.row.id)">{{ scope.row.title }}</span>
        </template>
        </el-table-column>
        <el-table-column label="简介" prop="summary" align="center"></el-table-column>
        <el-table-column label="创建时间" prop="creationTime" align="center">
        </el-table-column>
      </el-table>
      <div class="pager-container">
        <page :page="page" @changed="bindData"></page>
      </div>
    </div>

  </div>
</template>

<script>
/**
 * @file 练习列表
 * @author 宋乃乐
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/19
 */
import {
  getStuAllPractices,
  delFilesByIds
} from '../../api/trainingPath/index'
import page from '@/components/page'
export default {
  data() {
    return {
      page: {
        current: 1, // 当前页码
        pageCount: 10, // 一页呈现的数据数
        total: 0 // 总共的数据数
      },
      srearchParams: {},
      trainingData: [],
      loading: false,
      multipleSelection: []
    }
  },
  components: {
    page
  },
  mounted() {
    this.bindData()
  },

  methods: {
    // 绑定表格数据
    bindData() {
      this.loading = true
      this.srearchParams.page = this.page
      this.srearchParams.orderBy = 'creation_time desc'
      getStuAllPractices(this.srearchParams).then(data => {
        this.trainingData = data.data.data.records
        this.page.total = data.data.data.total
        this.loading = false
      }, () => {
        this.loading = false
      })
    },
    // 处理表格选择后同步选中的数组
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 批量删除练习
    delTraining() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要删除的文件夹')
        return
      } else {
        const dellist = []
        this.multipleSelection.forEach(item => {
          dellist.push(item.id)
        })
        this.$cfm('确定删除文件夹或者资源吗？', response => {
          delFilesByIds(dellist).then(e => {
            if (e.data) {
              this.$sucmsg('删除成功')
              this.bindData()
            } else {
              this.$errmsg('删除失败！')
            }
          })
        })
      }
    },
    // 跳转练习路由
    pathToRuntraining(id) {
      this.$router.push({ path: '/training/runtraining', query: { id }})
    }
  }
}
</script>

<style scoped>
.delBtn {
  margin: 10px;
}
</style>
