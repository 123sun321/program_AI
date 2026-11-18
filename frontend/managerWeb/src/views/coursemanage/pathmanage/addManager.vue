<template>
<div class="maincontent class-page">
   <el-dialog title="添加路径课程" :visible.sync="visiable" width="60%">
      <div class="grid-content bg-purple-dark">
          <el-form :inline="true" :model="form" ref="form" class="demo-form-inline pull-left">
              <el-form-item label="课程类别:"  prop="category" >
                <el-select v-model="form.code" filterable placeholder="所有">
                  <el-option  v-for="(item,index) in categorylist" :key="index" :label="item.name"  :value="item.code">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="难易程度:" prop="complexity">
                <el-select v-model="form.complexity" filterable placeholder="所有">
                  <el-option  v-for="(item,index) in complexitys" :key="index" :label="item.name"  :value="item.complexity">
                  </el-option>
                </el-select>
              </el-form-item>
            <el-form-item label="课程名称:" prop="name" fop="like">
               <el-input  v-model="form.name"  placeholder="请输入名称"  prefix-icon="el-icon-search" >
              </el-input>
            </el-form-item>
            <el-form-item>
               <el-button icon="el-icon-search" type="primary" @click="getLists()">搜索</el-button>
                <el-button plain @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
      </div>
      <div class="class-table">
          <el-table
            ref="multipleTable"
            :data="courses"
            tooltip-effect="dark"
            style="width: 100%"
            @selection-change="handleSelectionChange">
            <el-table-column
              type="selection"
              align="center"
              >
            </el-table-column>
            <el-table-column
            type="index"
            :index="indexMethod"
            width="50"
            label="序号"
            >
          </el-table-column>
            <el-table-column
              prop="name"
              label="课程名称"
              align="center"
              >
            </el-table-column>
            <el-table-column
              prop="summary"
              label="课程简介"
              align="center"
              show-overflow-tooltip
            >
            </el-table-column>
             <el-table-column
              prop="category"
              label="课程类别"
              align="center"
              >
            </el-table-column>
            <el-table-column prop="complexity" label="难易程度" align="center" width="150">
           <template slot-scope="scope">
            <el-rate v-model="scope.row.complexity" disabled text-color="#ff9900"></el-rate>
          </template>
            </el-table-column>
             <el-table-column
              prop="creationTime"
              label="添加时间"
              align="center"
              >
            </el-table-column>
          </el-table>
          <page :page="form.page" @changed="getLists"></page>
      </div>
       <div class="operation-button">
        <el-button type="primary" @click="join"  >加入路径</el-button>
          <el-button   plain @click="close"  >取消</el-button>
      </div>
      <div style="clear:both"></div>
    </el-dialog>
</div>
</template>

<script>
/**
 * @file 添加路径组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/13
 */
import page from '@/components/page'
import { setCoursepathadds } from '@/api/coursemanage/pathmanage'
import { pageTeacherCourse } from '@/api/coursemanage/course'
import { fetchItems } from '@/api/dictionary'
export default {
  name: 'class',
  components: {
    page
  },
  props: ['getList'],
  data() {
    return {
      visiable: false, // 弹窗
      categorylist: [], // 所有课程类别
      choose: [], // 选中的课程
      courses: [], // 课程信息
      complexitys: [{ complexity: 1, name: '一星' },
        { complexity: 2, name: '二星' },
        { complexity: 3, name: '三星' },
        { complexity: 4, name: '四星' },
        { complexity: 5, name: '五星' }
      ], // 难易程度
      multipleSelection: [], // 全选
      // 分页和模糊查询
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        search: '',
        orderBy: 'id',
        category: '',
        complexity: ''
      },
      code: '', // 路径code
      loading: false// 加载
    }
  },
  // 获取列表课程数据
  created() {
    this.getAllcategory()
  },
  methods: {
    // 打开弹窗
    open(code) {
      this.visiable = true
      this.code = code
      this.$nextTick(function() {
        this.getLists()
      })
    },
    // 获取课程序号 index [Int] 返回课程序号
    indexMethod(index) {
      return index + 1 + this.form.page.pageCount * (this.form.page.current - 1)
    },
    // 获取课程类别
    getAllcategory() {
      fetchItems(null, 'course_category').then(e => {
        this.categorylist = e.data.data
        console.log(this.categorylist)
      })
    },
    // 全部公开课程
    getLists() {
      this.loading = true
      const params = this.$formToParams(this.$refs.form)
      pageTeacherCourse(params).then(e => {
        const data = e.data.data
        this.courses = data.records
        this.form.page.total = data.total
        this.loading = false
      })
    },
    // 关闭添加课程弹窗
    close() {
      this.visiable = false
    },
    // 导出课程数据
    join() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要加入的课程')
        return
      } else {
        const ids = this.multipleSelection.map(b => b.id)
        // const item = {
        //   courseId: ids,
        //   category: this.code
        // }
        this.$cfm('确定加入课程记录吗？', e => {
          setCoursepathadds(ids, this.code).then(e => {
            if (e.data.success) {
              this.$sucmsg('添加成功')
              this.getList()
              this.close()
            } else {
              this.$errmsg('添加失败，该课程已经加入路径!')
              this.close()
            }
          })
        })
      }
    },
    // 获取复选框被选中的课程数据
    handleSelectionChange(val) {
      this.multipleSelection = val
      console.log(val)
    },
    // 重置搜索条件
    resetForm() {
      this.$refs.form.resetFields()
      this.getLists()
    }
  }
}
</script>
<style scoped>
  p{
    margin:0px;
  }
  .class-page .el-row {
    margin-bottom: 20px;
  }
  .class-page .el-col {
    border-radius: 4px;
    /* border:1px solid red; */
    margin-left: 20px;
    margin-right: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  .class-page .bg-purple-dark>.el-input {
    width:20%;
  }
  .class-page .bg-purple {
    background: #d3dce6;
  }
  .class-page .grid-content {
    border-radius: 4px;
    min-height: 36px;
    margin-bottom: 10px;
  }
  .class-page .grid-content span{
    color: #606266;
    font-size: 14px;
  }
 .operation-button{
    /* display: inline-block; */
    float: right;
    margin-top: 20px;

  }
  .el-dialog{
    border-radius: 4px;
  }


</style>
