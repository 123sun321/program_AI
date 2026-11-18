<template>
  <!-- <div class="operation-button">
    <el-button type="primary" @click="addCourse" icon="el-icon-plus" >新建</el-button>
    <el-button type="primary" @click="importcourse" icon="el-icon-upload2" >导入课程</el-button>

  </div> -->
<el-dialog
  title="添加课程信息"
  :visible.sync="outerVisible"
  width="55%" :before-close="closeDialog">
  <div v-if="visible" class="twobutton">
    <!-- <div class="block">
    <el-image
      style="width: 100px; height: 100px"
      :src="require('@/assets/images/course/add_bg.png')"
      :fit="contain"></el-image>
  </div> -->
  <!-- <div class="block">
    <el-image
      style="width: 100px; height: 100px"
      :src="require('@/assets/images/course/add_bg.png')"
      :fit="fill"></el-image>
  </div> -->
    <div class="onebutton"> 
      <div class="box" @click="addCourse" >
         <img   :src="require('@/assets/images/course/add_bg.png')"> 
          <div class="importbutton" > <img    style="width:21px; height:19px " :src="require('@/assets/images/course/popup_icon_add.png')">&nbsp;&nbsp;<i >空白模板创建</i></div>
      </div>
      <div class="box" style="margin-left:20px;"  @click="importcourse">
         <img    :src="require('@/assets/images/course/add_bg.png')" >
           <div class="importbutton"> <img style="width:21px; height:19px " :src="require('@/assets/images/course/popup_icon_import.png')">&nbsp;&nbsp;<i>学校模板创建</i></div>
      </div>
    
    </div>
 
  </div>
  <!-- 添加课程 -->
  <div v-if="visibleadd==true" class="p">
     <course ref="form1" :is-edit="false"></course>
    <div slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="addsave">确 定</el-button>
    </div>
  </div>
  <!-- 编辑 -->
  <div v-if="visibleedit==true" class="p">
    <course ref="form2" :eid="eid" :is-edit="true"  :coursedata="coursedata" ></course>
    <div slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="editsave">确 定</el-button>
    </div>
  </div>
  <div v-if="visibleimport==true" class="p">
   <import-course  ref="import" @ee="aa"></import-course>
   <div slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <!-- <el-button type="primary" @click="addsave">确 定</el-button> -->
    </div>
  </div>
</el-dialog>
  
</template>
<script>
import editCourse from './editCourse'
import Course from './components/Course'
import importCourse from './importCourse'
import page from '@/components/page'
import {
  addTeacherCourse, teacheredit, batchImport
} from '@/api/coursemanage/course'
export default {
  components: {
    importCourse,
    Course,
    editCourse,
    page
  },
  // props: ['getList'],
  data() {
    return {
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        search: '',
        orderBy: 'id desc',
        category: '',
        complexity: ''
      },
      visible: true, // 添加课程和创建课程信息框
      outerVisible: false, // 添加课程信息框
      visibleadd: false, // 手动添加课程信息
      visibleimport: false, // 导入课程
      visibleedit: false, // 导入课程编辑页面
      eid: '',
      coursedata: undefined,
      // image: require('@assets/images/course/add_bg.png'),
      courses: [],
      loading1: false,
      value2: null,
      List: {
        complexityList: '', // 课程难易程度列表
        categoryList: '' // 课程类别列表
      }
    }
  },
  created() {

  },
  methods: {
    batchImportCourse(ids) {
      // this.$cfm('确定导入课程吗？', e => {
      const value = this.$refs.form2.getValue()
      batchImport(ids).then(e => {
        if (e.data.success) {
          debugger
          value.id = e.data.data
          debugger
          teacheredit(value).then(e => {
            if (e.data.success) {
              this.$sucmsg('保存成功')
              this.$emit('success')
              this.close()
            } else {
              this.$errmsg('保存失败')
            }
          })
          // debugger
          // this.$sucmsg('添加成功')
          // this.getList()
          // this.$emit('success')

          // this.eid = e.data.data
        }
      })
    },
    // 导入课程按钮
    importcourse() {
      // debugger
      this.visible = false
      this.visibleimport = true
      this.visibleadd = false
      this.visibleedit = false
    },
    // 编辑课程按钮
    aa(row) {
      debugger
      this.coursedata = row
      this.eid = row.id
      // this.batchImportCourse([id])

      this.visibleimport = false
      this.visibleedit = true
    },
    // 打开本页面
    open() {
      this.outerVisible = true
    },
    // 所有的关闭方法
    close() {
      this.outerVisible = false
      this.visible = true
      this.visibleadd = false// 添加
      this.visibleedit = false// 编辑
      this.visibleimport = false// 导入
      this.$refs.form2.reset()
      this.$refs.form1.reset()
    },

    closeDialog() {
      this.close()
    },

    addCourse() {
      this.visible = false
      this.visibleadd = true
      this.visibleimport = false
      this.visibleedit = false
    },

    addsave() {
      this.$refs.form1.validate(e => {
        if (e) {
          const value = this.$refs.form1.getValue()
          console.log(value)
          addTeacherCourse(value).then(e => {
            if (e.data.data) {
              this.$sucmsg('保存成功')

              this.$emit('success')
              this.$refs.form1.reset()
              this.close()
            } else {
              this.$errmsg('保存失败')
            }
          })
        }
      })
    },
    editsave() {
      this.$refs.form2.validate(e => {
        if (e) {
          this.batchImportCourse([this.eid])
        }
      })
    }
  }
}
</script>
<style scoped>
  .p {
    margin: 0px;
    margin-top: 30px
  }
  .twobutton{
    position: relative;
    background-image: url(~@/assets/images/course/Privatecourses_Windowbg.png);
    background-repeat: no-repeat;
    height: 300px;
   
  }
  .onebutton{
    display: flex;
    flex-wrap: row;
    align-items: center;
    justify-content:center;
       /* position: absolute; */
       /* padding-left: 30%;   */
      padding-top: 70px;  
       }    
  
  .addbutton{
      position: absolute;
      padding-left: 32%;
       padding-top: 12.5%;
       font-size: 19px;
       color: #F5F5F5;
  }
  .importbutton{
     position: absolute;
     cursor: pointer;
      /* padding-left: 50%; */
      /* margin-top: -12.5%;   */
      text-align: center;
      margin-top: -60px;
      margin-left: 20px;
       font-size: 19px;
       color: #F5F5F5;
  }
  
  .addCourse{
    position: relative;
    margin-left: 85%;
  }
  .course-page .el-row {
    margin-bottom: 20px;
  }

  .course-page .el-col {
    border-radius: 4px;
    /* border:1px solid red; */
    margin-left: 20px;
    margin-right: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
  }
  .course-page .bg-purple-dark>.el-input {
    width: 20%;
  }

  .course-page-search .el-form-item {
    margin-bottom: 0px;
  }

  .course-page .bg-purple {
    background: #d3dce6;
  }

  .course-page .bg-purple-light {
    background: #e5e9f2;
  }

  .course-page .grid-content {
    border-radius: 4px;
    min-height: 36px;
    /* margin-bottom: 10px; */
  }

  .course-page .grid-content span {
    color: #606266;
    font-size: 14px;
  }

  .course-page .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }

  .course-page .operation-button {
    /* display: inline-block; */
    float: right;
  }

  .el-dialog {
    border-radius: 4px;
  }

  .course-page .el-tooltip {
    cursor: pointer;
    color: #409eff;
  }

  .course-page .profession-pagination {
    margin-top: 20px;
    text-align: right;
  }
  .dialog-footer{
    text-align: right;
  }

</style>

<style>
  .export-table {
    border-right: 1px solid #DCDFE6;
    border-bottom: 1px solid #DCDFE6;
  }

  .export-table td {
    border-left: 1px solid #DCDFE6;
    border-top: 1px solid #DCDFE6;
  }

  .export-table {
    width: 100%;
  }

  .export-table td:first-child {
    width: 20%;
    text-align: center;
  }

  .export-table td:last-child {
    width: 80%;
  }

  .export-table tr:last-child>td:last-child {
    padding: 35px;
  }

  .export-table .upload-demo {
    width: 80%;
    display: inline-block;
  }

  .export-table .el-upload {
    margin-right: 10px;
  }

  .export-table .el-upload-list__item:first-child {
    margin-top: 3px;
  }

  .export-table .upload-demo .el-upload-list {
    border: 1px solid #DCDFE6;
    height: 32px;
    display: inline-block;
    width: 78%;
    vertical-align: top;
  }

</style>
