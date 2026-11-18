<template>
    <div class="assignment">
        <!-- 搜索 -->
     <div class="new">
        <el-form :inline="true" :model="form" ref="form">
        <el-form-item label="所属课程:" prop="courseId">
            <el-select v-model="form.courseId"  filterable placeholder="全部">
                <el-option  v-for="(item,index) in courseList" :key="index" :label="item.course.name"  :value="item.courseId">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-button icon="el-icon-search" type="success" @click="getList">搜索</el-button>
            <el-button class="c_btn" @click="resetForm('form')">重置</el-button>
        </el-form-item>
        </el-form>
     </div>
          <!-- 选项卡 -->
     <el-tabs  type="card" @tab-click="handleClick">
        <el-tab-pane label="全部">
        </el-tab-pane>
        <el-tab-pane label="已提交">
        </el-tab-pane>
        <el-tab-pane label="未提交" >
        </el-tab-pane>
     </el-tabs>
     <!-- 主体 -->
      <div class="courselist">
     <div class="nodata" v-if="tableData.length===0 && !loading">
         <img :src="require('@/assets/student/Privatecourses_Illustration.png')">
         <div class="text">没有作业</div>
     </div>
     <div v-else>
      <el-row>
          <el-col :span="6" v-for="(item,index) in tableData" :key="index">
              <div class="item">
                  <el-row>

                      <el-col :span="19" style="width: calc(100% - 75px);">
                          <div class="c_title"><a href="#">{{item.title}}</a></div>
                            <div class="c_info">
                              <span>所属课程：{{item.course.name}}</span>
                          </div>
                          <div style="margin-top:10px;" class="c_info">
                              <span>创建时间：{{item.creationTime| formatTime('{y}-{m}-{d} {h}:{i}')}}</span>
                          </div>
                      </el-col>
                      <el-col :span="5" class="btn">
                        <div v-if="item.assignmentSubmitDto" class="but"> <span style="color:#1FAD4E" v-if="item.assignmentSubmitDto.whetherScore">{{item.assignmentSubmitDto.score}}</span> <span style="color:#666666;font-size: 13px;" v-else>未评分</span> </div>
                        <div v-else class="but"><span style="color:#666666;font-size: 13px;">未提交</span> </div>
                        <el-button class="c_btn" type="success" @click="submit(item)" v-if="!item.assignmentSubmitDto" plain round>提交</el-button>
                        <el-button type="info"  v-else  @click="submit(item)" round>已提交</el-button>
                      </el-col>
                  </el-row>
              </div>
          </el-col>
      </el-row>
     </div>
     <page v-if="this.form.page.total>10" :page="form.page" @changed="getList" style="margin-top:20px;"></page>
    </div>
    </div>
</template>

<script>
/**
 * @file  讨论组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/22
 */
import page from '@/components/page'
import { getStudentPrivateCourses } from '@/api/mycourse/course'
import { groupPage } from '@/api/assignment/index'
export default {
  components: { page },
  data() {
    return {
      loading: false,
      form: {
        page: {
          current: 1,
          pageCount: 9,
          total: 0
        },
        courseId: '',
        search: '',
        orderBy: 'g.creation_time desc'
      },
      choose: 0,
      groupNotSubmit: 0,
      courseList: '', // 课程列表
      tableData: [], // 数据列表
      tableData1: [], // 已提交作业数据列表
      tableData2: []// 未提交作业数据列表
    }
  },
  created() {
    this.$nextTick(e => {
      this.getList()
      this.getGroupNotSubmit()
    })
  },

  methods: {
    getGroupNotSubmit() {
      const vm = this
      const params = vm.$formToParams(this.$refs.form)
      params.status = -1
      // 获取学生的私有课程作业
      groupPage(params).then(e => {
        const data = e.data.data
        vm.groupNotSubmit = data.total
        vm.$emit('groupNotSubmit', vm.groupNotSubmit)
      })
    },
    // 重置搜索条件
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.searchParams.form = {}
      this.getList()
    },
    // 提交作业
    submit(item) {
      this.$router.push({ name: 'submitassignment', query: { id: item.id }})
    },
    getList() {
      this.loading = true
      const params = this.$formToParams(this.$refs.form)
      params.status = this.choose
      // 获取学生的私有课程作业
      groupPage(params).then(e => {
        const data = e.data.data
        this.tableData = data.records
        this.form.page.total = data.total
        this.loading = false
      })
      // 获取学生的私有课程列表
      const param = {}
      getStudentPrivateCourses(param).then(response => {
        this.courseList = response.data.data
      })
    },
    handleClick(tab, event) {
      if (tab.label === '全部') {
        this.choose = 0
      } else if (tab.label === '已提交') {
        this.choose = 1
      } else {
        this.choose = -1
      }
      this.getList()
    }
  }
}
</script>
<style lang="scss" scoped>
.assignment{
  padding: 0px 0px 20px 0px;
}
.new{
  float: right;
  position: relative;
  z-index: 100;
  .el-form-item{
      margin-bottom: 0px;
  .c_btn{
        color: #1fad4e;
        border-color:#e8f7ed;
        background:#e8f7ed;
        border-radius: 4px;
    }
    .c_btn:hover{border-color:#1fad4e; }
    .el-select-dropdown__item /deep/.selected {
        color: #1fad4e;
        font-weight: 700;
    }
    /deep/ .el-select .el-input.is-focus .el-input__inner {
        border-color: #1fad4e;
    }
  }
}
.assignment /deep/ .el-tabs--card>.el-tabs__header{
    border:none!important;
    margin: 0px;
}
  .assignment /deep/  .el-tabs__nav{
    border:none!important;
    padding: 0;
  }
  .assignment /deep/ .el-tabs__item{
        padding: 0 10px!important;
        margin-right:10px;
        text-align: center;
        height: 30px;
        font-size:16px;
        line-height:30px;
        color:#303133;
        border:none!important;
    }
    .assignment /deep/ .el-tabs__item.is-active,.el-tabs__item:hover{
        transition:initial;
        background-color: #e8f7ed;
        border-radius: 15px;
        color:#1fad4e;
    }
.courselist{
    .item{
        border-right:1px solid #eeeeee;
        border-bottom:1px solid #eeeeee;
        padding: 15px 10px;
        .logo{width: 100%;height: 90px;}
        .c_title{font-size: 16px;color: #333333;line-height: 20px;margin-bottom: 20px;font-weight: bolder; overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;}
        .c_summary{font-size: 12px;color: #666666;margin-bottom: 20px;}
        .c_info{font-size: 13px;color: #666666;line-height: 20px; overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;}
        .btn{ text-align: right;
              width: 75px;
              .c_btn{
        color: #1fad4e;
        border-color:#e8f7ed;
        background:#e8f7ed;
        // border-radius: 4px;
 }
    .c_btn:hover{border-color:#1fad4e; }
        .but{
            cursor: pointer;
            margin-bottom: 40px;
          }
        }
    }
    .el-pagination{text-align: center;padding: 15px 0;}
    .nodata{
        padding: 15px 0;
        text-align: center;
        .text{
          font-size: 18px;
          color: #999999;
        }
    }
}
.courselist>.el-row>.el-col:nth-child(3n){
    .item{border-right: none;}
}
</style>
