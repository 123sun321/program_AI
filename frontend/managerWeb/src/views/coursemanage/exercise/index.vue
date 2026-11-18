<template>
  <div class="maincontent">
    <div class="condition-container clearfix mg0">
      <el-form :inline="true" ref="form" :model="form" style="width:70%;" class="demo-form-inline pull-left">
         <el-form-item label="所属课程:" prop="courseId">
            <el-select v-model="form.courseId" clearable filterable placeholder="全部">
                <el-option  v-for="(item,index) in courseList" :key="index" :label="item.name"  :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="实验类型:" prop="type">
            <el-select v-model="form.type" clearable  placeholder="全部">
                <el-option  label="课程实验"  value="0"></el-option>
                <el-option  label="练习实验"  value="1"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="search">
          <el-input v-model="form.search" placeholder="请输入实验名称、知识点等"></el-input>
        </el-form-item>
        <el-form-item class="dk-button">
          <el-button type="primary" @click="search" icon="el-icon-search">查询</el-button>
          <el-button type="default" @click="reset" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="operation-button pull-right">
        <el-button type="primary" @click="add" icon="el-icon-plus">新建</el-button>
        <el-button type="danger" @click="batchDel" icon="el-icon-delete">批量删除</el-button>
      </div>
    </div>
    <div class="table-container">
      <el-table :data="gridData" v-loading='loading'  @selection-change="handleSelectionChange" @sort-change="sortChange">
        <el-table-column type="selection" width="30"></el-table-column>
        <el-table-column type="index" label="序号"  align="center"></el-table-column>
        <el-table-column label="名称" prop="name"></el-table-column>
        <el-table-column label="所属课程" prop="b.name" sortable="custom">
          <template slot-scope="scope">
            {{scope.row.course === null ? '---' : scope.row.course.name}}
          </template>
        </el-table-column>
        <el-table-column label="知识点" show-overflow-tooltip prop="knowledge"></el-table-column>
        <el-table-column prop="a.complexity" label="难易程度" align="center" width="150" sortable="custom">
          <template slot-scope="scope">
              <el-tag v-if="scope.row.complexity==1" >{{scope.row.complexity}}星</el-tag>
              <el-tag v-if="scope.row.complexity==2"  type="success">{{scope.row.complexity}}星</el-tag>
              <el-tag v-if="scope.row.complexity==3" type="info">{{scope.row.complexity}}星</el-tag>
              <el-tag v-if="scope.row.complexity==4"  type="warning">{{scope.row.complexity}}星</el-tag>
              <el-tag v-if="scope.row.complexity==5"  type="danger">{{scope.row.complexity}}星</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="实验类型" width="150" prop="a.type" sortable="custom">
            <template slot-scope="scope">
                <el-tag
                    :type="scope.row.type === 0 ? 'primary' : 'danger'" disable-transitions>
                    {{scope.row.type === 0 ? '课程实验' : '练习实验'}}
                </el-tag>
            </template>
        </el-table-column>
        <el-table-column label="实验人数" width="150" prop="a.join_number" sortable="custom">
          <template slot-scope="scope">
                <el-tag type="primary">
                    {{scope.row.joinNumber}}
                </el-tag>
            </template>
        </el-table-column>
        <el-table-column label="创建时间" width="150" prop="a.creation_time" sortable="custom">
          <template slot-scope="scope">
            {{scope.row.creationTime}}
          </template>
        </el-table-column>
        <el-table-column prop="zip" width="160" label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-edit"  @click="edit(scope.row)" title="编辑" plain></el-button>
            <el-button type="info" size="mini" icon="el-icon-search"  @click="joininfo(scope.row)" title="参与详情" plain></el-button>
            <el-button size="mini" type="danger" @click="del(scope.row)" icon="el-icon-delete" title="删除" plain></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pager-container">
      <page :page="form.page" @changed="bindData"></page>
    </div>
    <show ref="show"></show>
  </div>
</template>
<script>
/**
 * @file 实验列表
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/11
 */
import page from '@/components/page'
import show from './components/show'
import { managerpage as pagelist, del } from '@/api/coursemanage/exercise'
import { getNow } from '@/api/coursemanage/course'
export default {
  components: { page, show },
  data() {
    return {
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        search: undefined,
        orderBy: 'a.creation_time desc'
      },
      loading: false,
      // 数据列表
      gridData: [],
      // 课程列表
      courseList: [],
      // 选中记录
      multipleSelection: []
    }
  },
  created() {
    this.$nextTick(e => {
      this.init()
      this.bindData()
    })
  },
  methods: {
    init() {
      // 获取课程信息
      getNow().then(e => {
        this.courseList = e.data.data
      })
    },
    search() { // 搜索
      this.bindData()
    },
    // 改变排序
    sortChange(data) {
      console.log(data)
      var sortStr = 'a.creation_time desc'
      if (data.prop !== null) {
        sortStr = data.prop
        if (data.order === 'ascending') {
          sortStr += ' asc'
        } else {
          sortStr += ' desc'
        }
      }
      this.form.orderBy = sortStr
      this.bindData()
    },
    reset() { // 重置
      this.$refs['form'].resetFields()
      this.bindData()
    },
    // 新增实验
    add() {
      this.$router.push({ name: 'addexercise' })
    },
    // 编辑实验
    edit(item) {
      this.$router.push({ name: 'addexercise', query: { exerciseid: item.id }})
    },
    // 参与详情
    joininfo(item) {
      this.$router.push({ name: 'joinexercise', query: { exerciseid: item.id }})
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    show(row) {
      console.log(row)
      // this.$refs.show.open(row.)
    },
    // 数据绑定
    bindData() {
      var vm = this
      this.loading = true
      const params = this.$formToParams(this.$refs.form)
      pagelist(params).then(response => {
        const data = response.data.data
        vm.gridData = data.records
        this.form.page.total = data.total
        this.loading = false
        // this.page = data.current
      }).catch(() => {
        this.loading = false
      })
    },
    // 批量删除-------------------------
    batchDel() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择需要删除的实验！')
        return
      } else {
        const ids = this.multipleSelection.map(b => b.id)
        this.delete(ids, '确定删除选中得实验吗？')
      }
    },
    del(row) {
      this.delete([row.id], '确定删除实验【' + row.name + '】吗？')
    },
    delete(ids, title) {
      this.$cfm(title, e => {
        del(ids).then(e => {
          if (e.data.success) {
            this.$sucmsg('删除成功')
            this.bindData()
          } else {
            this.$errmsg('删除失败')
          }
        })
      })
    }
    // 删除结束-------------------------
  }
}
</script>
<style lang="scss" scoped>

</style>

