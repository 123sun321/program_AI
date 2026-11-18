<template>
  <div class="mergely-page">
    <h4>代码对比</h4>
    <div>
      <span class="btn"><el-button @click="goback()" plain>返回</el-button></span>
    </div>
    <div v-if="this.student1 !== null || this.student2 !== null">
      <el-row :gutter="10" class="student-page">
        <el-col :span="12">
          <span>{{student1.student.name}}:&nbsp;&nbsp;{{student1.assignment.title}}</span>
        </el-col>
        <el-col :span="12">
          <span>{{student2.student.name}}:&nbsp;&nbsp;{{student2.assignment.title}}</span>
        </el-col>
      </el-row>
    </div>
    <div>
      <el-form>
        <div 
        v-loading="loading"
        element-loading-text="代码对比中"
        element-loading-spinner="el-icon-loading">
          <el-row :gutter="10">
            <el-col :span="12">
              <el-form-item label="html：">
                <el-input
                  v-model="entity1.html"
                  type="textarea"
                  :autosize="{minRows: 2, maxRows: 15}"
                  placeholder="请输入旧数据"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="html：">
                <el-input
                  v-model="entity2.html"
                  type="textarea"
                  :autosize="{minRows: 2, maxRows: 15}"
                  placeholder="请输入新数据"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="12">
              <el-form-item label="js：">
                <el-input
                  v-model="entity1.js"
                  type="textarea"
                  :autosize="{minRows: 2, maxRows: 15}"
                  placeholder="请输入旧数据"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="js：">
                <el-input
                  v-model="entity2.js"
                  type="textarea"
                  :autosize="{minRows: 2, maxRows: 15}"
                  placeholder="请输入新数据"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="12">
              <el-form-item label="css：">
                <el-input
                  v-model="entity1.css"
                  type="textarea"
                  :autosize="{minRows: 2, maxRows: 15}"
                  placeholder="请输入旧数据"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="css：">
                <el-input
                  v-model="entity2.css"
                  type="textarea"
                  :autosize="{minRows: 2, maxRows: 15}"
                  placeholder="请输入新数据"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
      <div
        v-loading="loading"
        element-loading-text="代码对比中"
        element-loading-spinner="el-icon-loading">
        <code-diff
          :old-string="entity1.html"
          :new-string="entity2.html"
          :context="context"
          :output-format="outputFormat"
        />
        <code-diff
          :old-string="entity1.js"
          :new-string="entity2.js"
          :context="context"
          :output-format="outputFormat"
        />
        <code-diff
          :old-string="entity1.css"
          :new-string="entity2.css"
          :context="context"
          :output-format="outputFormat"
        />
      </div>
   </div>
  </div>
</template>

<script>
import codeDiff from './lib/index.js'
import importVal from './importVal'
// import mergely from '@/utils/mergely'
import { getCodeByAssignmentSubmitId, getStudentAndAssignmentById } from '@/api/assignmentmanage/assgin'
export default {
  components: {
    importVal,
    codeDiff
  },
  data() {
    return {
      id1: this.$route.query.id1,
      id2: this.$route.query.id2,
      oldStr: '',
      newStr: '',
      fotmat: false,
      context: 10,
      code: null,
      loading: false,
      student1: null,
      student2: null,
      // mergely: mergely,
      type: 0,
      entity1: {
        html: '',
        css: '',
        js: ''
      },
      entity2: {
        html: '',
        css: '',
        js: ''
      }
    }
  },
  computed: {
    outputFormat() {
      return this.fotmat ? 'line-by-line' : 'side-by-side'
    }
  },
  watch: {
    oldStr(v) {
      localStorage.setItem('entity1.html', v)
    },
    newStr(v) {
      localStorage.setItem('entity2.html', v)
    }
  },
  created() {
    this.entity1.html = localStorage.getItem('entity1.html') || ''
    this.entity2.html = localStorage.getItem('entity2.html') || ''
    this.$nextTick(e => {
      this.getData()
      this.getStudent()
    })
  },
  methods: {
    handleClearLocalStorage() {
      this.entity1.html = ''
      this.entity2.html = ''
      localStorage.setItem('entity2.html', '')
      localStorage.setItem('entity1.html', '')
    },
    goback() {
      this.$router.go(-1)
    },
    getData() {
      this.loading = true
      getCodeByAssignmentSubmitId(this.id1, this.id2).then(e => {
        this.code = e.data.data
        this.entity1 = this.code.codeDto1
        this.entity2 = this.code.codeDto2
        this.loading = false
      })
    },
    getStudent() {
      getStudentAndAssignmentById(this.id1).then(e => {
        this.student1 = e.data.data
      })
      getStudentAndAssignmentById(this.id2).then(e => {
        this.student2 = e.data.data
      })
    }
  }
}
</script>


<style scoped>
 .mergely-page{
     width: 100%;
     padding: 15px;
     min-height: 800px;
 }
 #mergely-content{
   width: 100%;
   /* height: 150px; */
   border: 1px solid silver;
   border-radius: 5%;
 }
 .btn{
  margin-left: 96%;
 }
 .student-page{
   font-size: 15px;
   padding-bottom: 10px;
 }
</style>

