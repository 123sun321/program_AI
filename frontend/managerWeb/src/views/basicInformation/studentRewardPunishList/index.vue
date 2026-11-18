/*
 * @Author: 黄鹏举
 * @Date: 2018-12-04 17:52:38 
 * @Last Modified time: 2018-12-04 19:26:11
 */
<template>
    <el-row>
        <el-col :span="23" class="RewardPhunishList">
            <div class="grid-content bg-purple-dark">
                <el-form :inline="true" :model="searchForm" ref="searchForm" > 
                    <div>
                        <el-form-item label="院系:" prop="department" >
                            <el-select v-model="searchForm.departmentId"   clearable filterable placeholder="全部">
                                <el-option  v-for="(item,index) in department" :key="index" :label="item.name"  :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="专业:" prop="profession" >
                            <el-select v-model="searchForm.professionId"   clearable filterable placeholder="全部">
                                <el-option  v-for="(item,index) in profession" :key="index" :label="item.name"  :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="年级:" prop="grade" >
                            <el-select v-model="searchForm.grade"   clearable filterable placeholder="全部">
                                <el-option  v-for="(item,index) in grade" :key="index" :label="item.name"  :value="item.name">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </div>
                    <div>
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
                        <el-form-item label="姓名:"  prop="studentName" fop="like">
                            <el-input v-model="searchForm.studentName"  placeholder="请输入学生姓名"  prefix-icon="el-icon-search"  >
                            </el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button icon="el-icon-search" type="primary"  @click="getList()">搜索</el-button>
                              <el-button plain @click="resetForm('searchForm')">重置</el-button>
                        </el-form-item>
                        <div class="operation-button">
                            <el-button type="primary" icon="el-icon-plus" @click="addStuRewardPunish">新建</el-button>
                            <el-button type="primary" icon="el-icon-delete" @click="batchDeleted">批量删除</el-button>
                        </div>
                    </div>
                    
                    
                </el-form>
            </div>
            <div class="student-table">
                <el-table ref="multipleTable" :data="rewardList" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange" v-loading="loading">
                    <el-table-column  type="selection"  align="center" >
                    </el-table-column>
                    <el-table-column width="180" prop="code" label="学号" align="center">
                    </el-table-column>
                        <el-table-column width="150" prop="studentName" label="学生" align="center">
                    </el-table-column>
                    <el-table-column prop="schoolYear" label="学年" align="center" width="200">
                    </el-table-column>
                    <el-table-column  label="类型" align="center" width="120">
                         <template slot-scope="scope">
                          <el-tag type="danger" v-if="scope.row.type === '处罚'">{{scope.row.type}}</el-tag>
                          <el-tag type="success" v-else>{{scope.row.type}}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column  label="级别" align="center" width="180">
                         <template slot-scope="scope">
                            <el-tag type="danger" v-if="scope.row.type === '处罚'">{{scope.row.level}}</el-tag>
                            <el-tag type="success" v-else>{{scope.row.level}}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="rewardPunishCategoryName" label="奖惩种类" align="center" width="200">
                    </el-table-column>
                    <el-table-column label="附图" align="center" width="250">
                        <template slot-scope="scope">
                            <img :src="scope.row.masterUrl" alt="" width="160" height="70">
                        </template>
                    </el-table-column>
                    <el-table-column prop="abstracts" label="备注" align="center"  show-overflow-tooltip>
                    </el-table-column>
                    <el-table-column width="150" label="操作" align="center">
                        <template slot-scope="scope">
                            <el-button type="primary" icon="el-icon-edit" plain @click="editStuRewardPunish(scope.row.id)"></el-button>
                            <el-button type="danger" icon="el-icon-delete" plain  @click="deleteReward(scope.row.id)"></el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <page :page="searchForm.page" @changed="getList"></page>
            </div>
           <!-- 添加修改学生奖惩 -->
           <add-stu-reward-punish ref="add" @success="getList"></add-stu-reward-punish>
           <edit-stu-reward-punish ref="edit" @success="getList"></edit-stu-reward-punish>
        </el-col>
    </el-row>
</template>

<script>
/**
 * @description 学生奖惩列表
 * @author【黄鹏举】
 * @since 2018/12/4
 * @version 1.0
 */
import addStuRewardPunish from './addStuRewardPunish'
import editStuRewardPunish from './editStuRewardPunish'
import page from '@/components/page'
import {
  fetchList,
  dels,
  departmentList,
  professionList
} from '@/api/base/studentRewardPunish'
export default {
  // 组件
  components: {
    addStuRewardPunish,
    editStuRewardPunish,
    page
  },
  data() {
    return {
      // 院系
      department: [],
      // 专业
      profession: [],
      // 年级
      grade: [
        { name: '2017级' },
        { name: '2016级' },
        { name: '2015级' }
      ],
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
        department: undefined, // 院系
        profession: undefined, // 专业
        grade: undefined, // 年级
        type: undefined, // 奖惩类型
        level: undefined, // 级别
        studentName: undefined // 学生名称
      },
      multipleSelection: [],
      loading: false,
      // 列表
      rewardList: []
    }
  },
  created() {
    // 获取所有的院系
    departmentList().then(e => {
      this.department = e.data.data
    })
    // 获取所有的专业
    professionList().then(e => {
      this.profession = e.data.data
    })
    // 获取所有的学生奖惩列表
    this.$nextTick(e => {
      this.getList()
    })
  },
  methods: {
    // 1.0 获取数据
    getList() {
      this.loading = true
      // const params = this.$formToParams(this.$refs.searchForm)
      const params = this.searchForm
      console.log(params)
      fetchList(params).then(e => {
        console.log(e)
        const data = e.data.data
        this.rewardList = data.records
        this.searchForm.page.total = data.total
        this.loading = false
      })
    },
    // 1.1 重置搜索条件 formName表单
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    // 2.0 添加类型
    addStuRewardPunish() {
      this.$refs.add.open()
    },
    // 3.0 批量删除
    batchDeleted() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要删除的课程')
        return
      } else {
        const ids = this.multipleSelection.map(b => b.id)
        this.delete(ids)
      }
    },
    // 3.1获取复选框被选中的 val选中的数据id
    handleSelectionChange(val) {
      this.multipleSelection = val
      //  console.log(val)
    },
    // 4.1 删除单个 id需要删除的数据id
    deleteReward(id) {
      this.delete([id])
    },
    // 4.2 删除方法 ids需要删除数据的id数组
    delete(ids) {
      this.$confirm('都是很重要的信息，你确定删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        dels(ids).then(e => {
          if (e.data.success) {
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
    editStuRewardPunish(id) {
      this.$refs.edit.open(id)
    }
  }
}
</script>

<style scoped>
.RewardPhunishList{
    border-radius: 4px;
    /* border: 1px solid red; */
    margin-left: 20px;
    margin-right: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
    /* border: 1px solid #f5f7fa; */
}
  .el-col {
    border-radius: 4px;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
    margin-bottom: 10px;
  }
  .operation-button{
    float: right;
  }
</style>
