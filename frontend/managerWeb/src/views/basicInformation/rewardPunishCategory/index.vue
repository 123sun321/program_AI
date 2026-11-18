/*
 * @Author: 黄鹏举
 * @Date: 2018-12-04 17:52:38 
 * @Last Modified time: 2018-12-04 19:26:11
 */
<template>
    <el-row>
        <el-col :span="23" class="reward">
            <div class="grid-content bg-purple-dark">
                <el-form :inline="true" :model="searchForm" ref="searchForm" > 
                    <el-form-item label="类型:" prop="type" >
                        <el-select v-model="searchForm.type"   clearable filterable placeholder="全部">
                            <el-option  v-for="(item,index) in type" :key="index" :label="item.name"  :value="item.name">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="级别:" prop="level" >
                        <el-select v-model="searchForm.level"   clearable filterable placeholder="全部">
                            <el-option  v-for="(item,index) in level" :key="index" :label="item.name"  :value="item.name">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item  prop="name" fop="like">
                        <el-input v-model="searchForm.name"  placeholder="请输入奖罚名称"  prefix-icon="el-icon-search"  >
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button icon="el-icon-search" type="primary" @click="getList()">搜索</el-button>
                        <el-button plain @click="resetForm('searchForm')">重置</el-button>
                    </el-form-item>
                     <div class="operation-button">
                        <el-button type="primary" icon="el-icon-plus" @click="addReward">新建</el-button>
                        <el-button type="primary" icon="el-icon-delete" @click="batchDeleted">批量删除</el-button>
                    </div>
                </el-form>
            </div>
            <div class="student-table">
                <el-table ref="multipleTable" :data="rewardList" tooltip-effect="dark" style="width: 100%"  @selection-change="handleSelectionChange" v-loading="loading">
                    <el-table-column  type="selection"  align="center" >
                    </el-table-column>
                    <el-table-column width="100" prop="number" label="编号" align="center">
                    </el-table-column>
                    <el-table-column label="logo" align="center" width="300">
                         <template slot-scope="scope">
                             <img :src="scope.row.logo" alt="" width="160" height="70">
                        </template>
                    </el-table-column>
                    <el-table-column prop="name" label="名称" align="center" width="250">
                    </el-table-column>
                    <el-table-column label="类型" align="center" width="150">
                        <template slot-scope="scope">
                          <el-tag type="danger" v-if="scope.row.type === '处罚'">{{scope.row.type}}</el-tag>
                          <el-tag type="success" v-else>{{scope.row.type}}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="级别" align="center" width="200">
                         <template slot-scope="scope">
                            <el-tag type="danger" v-if="scope.row.type === '处罚'">{{scope.row.level}}</el-tag>
                            <el-tag type="success" v-else>{{scope.row.level}}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="desc" label="备注" align="center"  show-overflow-tooltip>
                    </el-table-column>
                    <el-table-column width="150" label="操作" align="center">
                        <template slot-scope="scope">
                            <el-button type="primary" icon="el-icon-edit" plain @click="editReward(scope.row.id)"></el-button>
                            <el-button type="danger" icon="el-icon-delete" plain @click="deleteReward(scope.row.id)"></el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <page :page="searchForm.page" @changed="getList"></page>
            </div>
            <!-- 添加修改奖惩类型 -->
            <add-reward ref="add" @success="getList"></add-reward>
            <edit-reward ref="edit" @success="getList"></edit-reward>
        </el-col>
    </el-row>
</template>

<script>
/**
 * @description 奖惩维护列表
 * @author【黄鹏举】
 * @since 2018/12/4
 * @version 1.0
 */
import addReward from './addRewardPunishCategory'
import editReward from './editRewardPunishCategory'
import page from '@/components/page'
import {
  fetchList,
  del,
  dels
} from '@/api/base/rewardPunishCategory'
export default {
  // 组件
  components: {
    addReward,
    editReward,
    page
  },
  data() {
    return {
      // 类型
      type: [
        { 'number': 1, name: '奖励' },
        { 'number': 2, name: '处罚' }
      ],
      // 级别
      level: [
        { 'number': 1, name: '国家级' },
        { 'number': 2, name: '省级' },
        { 'number': 3, name: '地市级' },
        { 'number': 4, name: '校级' },
        { 'number': 5, name: '院系级' }
      ],
      // 搜索条件
      searchForm: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        orderBy: 'id',
        type: undefined, // 奖惩类型
        level: undefined, // 级别
        name: undefined // 奖罚名称
      },
      multipleSelection: [],
      loading: false,
      // 列表
      rewardList: []
    }
  },
  created() {
    // 加载奖惩类型列表
    this.$nextTick(e => {
      this.getList()
    })
  },
  methods: {
    // 1.0 获取数据
    getList() {
      this.loading = true
      const params = this.$formToParams(this.$refs.searchForm)
      fetchList(params).then(e => {
        const data = e.data.data
        this.rewardList = data.records
        this.searchForm.page.total = data.total
        this.loading = false
      })
    },
    // 1.1 重置搜索条件  formName表单
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    // 2.0 添加类型
    addReward() {
      this.$refs.add.open()
    },
    // 3.0 批量删除
    batchDeleted() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要删除的课程')
        return
      } else {
        const ids = this.multipleSelection.map(b => b.id)
        this.$confirm('选中的都是很重要的信息，你确定删除吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(_ => {
          dels(ids).then(e => {
            this.$message({
              type: 'success',
              message: '删除成功！'
            })
            this.getList()
          })
        }).catch(_ => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      }
    },
    // 3.1获取复选框被选中的数据  val获取到数据id
    handleSelectionChange(val) {
      this.multipleSelection = val
      //  console.log(val)
    },
    // 4.1 删除单条数据 id需要删除数据的id
    deleteReward(id) {
      this.$confirm('都是很重要的信息，你确定删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        del(id).then(e => {
          if (e) {
            this.$message({
              type: 'success',
              message: '删除成功！'
            })
            this.getList()
          } else {
            this.$errmsg('删除失败')
          }
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 4.0 修改类型 id需要修改数据的id
    editReward(id) {
      this.$refs.edit.open(id)
    }

  }
}
</script>

<style scoped>
.reward{
    border-radius: 4px;
    /* border: 1px solid red; */
    margin-left: 20px;
    margin-right: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
}
  .el-col {
    border-radius: 4px;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
    margin-bottom: 10px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
  .operation-button{
    float: right;
  }
</style>
