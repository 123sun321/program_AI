<template>
  <div class="main">

    <div  class="container" >
      <el-form :inline="true" ref="form" :model="form" style="width:70%;" class="demo-form-inline pull-left">
        <el-form-item  prop="search" fop="like">
            <el-input v-model="form.search" placeholder="按姓名或学号查询"  prefix-icon="el-icon-search">
            </el-input>

          </el-form-item>
          <el-form-item label="提交时间:" prop="creation_time" fop="like">
            <el-date-picker
              v-model="form.creation_time"
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="选择日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="是否提交作业:" prop="value">
             <el-select v-model="form.value" clearable placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
          </el-form-item>
        <el-form-item class="dk-button">
          <el-button type="primary"  @click="search1()" icon="el-icon-search">查询</el-button>
          <el-button type="default" @click="reset" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="operation-button2 pull-right">
        <!-- <div>最近查重时间{{this.recenttime}}</div> -->

        <div v-if="hide==='false'">  <el-button style="color:#1ABC9C" type="button" @click="looklike()">作业查重</el-button></div>
       <div v-else-if="hide==='true'">  <el-button style="color:#1ABC9C"  disabled type="button" @click="looklike()">作业查重</el-button></div>
      </div>
     <div class="table-container">
      <el-table :data="gridData"  v-loading='loading'>
        <el-table-column type="index" label="序号" align="center" width="20"></el-table-column>
      <el-table-column  label="学生" align="center"  min-width="100"   >
            <template slot-scope="scope"  >
             <div style="margin:10px">
                  <div class="stu-img">
                      <img :src="scope.row.student?scope.row.student.imgUrl:'null'" >
                    </div>
                    <div class="stu-info">
                        <p><span class="stuName">{{scope.row.student?scope.row.student.name:'null'}}</span></p>
                        <p><span class="stuCode">{{scope.row.student?scope.row.student.code:'null'}}</span></p>
                    </div>
             </div>
            </template>
        </el-table-column>
        <el-table-column label="班级" prop="grade.name" align="center"></el-table-column>
        <el-table-column label="作业标题" prop="assignment.title" align="center"></el-table-column>
         <el-table-column label="是否提交作业" prop="recommend" align="center">
           <template slot-scope="scope">
             <div v-if="scope.row.id===null">未提交</div>
             <div v-else>已提交</div>
             <!-- <el-button type="text" v-if="scope.row.id===null">未提交</el-button> -->
            <!-- <el-button type="text" v-else>已提交</el-button> -->
           </template>
         </el-table-column>
        <el-table-column label="评分" prop="score" align="center" width="200" sortable>
           <template slot-scope="scope" >
             <div class="td-opc">
              <span v-if="scope.row.checked===true" style="display:block"><el-input  v-model="scope.row.score"  size="mini"></el-input>
              <span class="butt"><el-button type="primary" size="mini" @click="cofirm(scope.row)">确定</el-button><el-button size="mini" @click="cancel(scope.row)" plain>取消</el-button></span></span>
                <div v-else @click="checke(scope.row)">
                   <div v-if="scope.row.id!=null">{{scope.row.whetherScore?scope.row.score+"分":"未评分"}}</div>
                  <!-- <div class="badges" >{{scope.row.whetherscore?scope.row.score+"分":"未评分"}}</div> -->
                  <div v-else >等待提交作业</div>
                  </div>
                <!-- <div class="op"></div> -->
             </div>
              </template>
          </el-table-column>
          <el-table-column label="代码相似度" prop="codeSimilarity" align="center">
              <template slot-scope="scope">
               <el-button style="color:#1ABC9C" type="text" @click="lookSimilarity(scope.row)">{{scope.row.codeSimilarity}}</el-button>
             </template>
          </el-table-column>
        <el-table-column label="参与时间" prop="creationTime"  align="center" sortable>
         <template slot-scope="scope">
                {{scope.row.creationTime !=null ? scope.row.creationTime :'-' }}
             </template>
        </el-table-column>
        <el-table-column prop="zip" width="300" label="操作" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.id===null" disabled style="color:#1ABC9C" type="button" @click="look(scope.row)">预览代码</el-button>
            <el-button v-if="scope.row.id!=null"  style="color:#1ABC9C" type="button" @click="look(scope.row)">预览代码</el-button>

            <span v-if="scope.row.id!=null">
            <el-button style="color:#1ABC9C" v-if="scope.row.recommend===0" type="button" @click="set(scope.row)">设为推荐</el-button>
            <el-button v-else style="color:#FF8000" type="button" @click="canc(scope.row)" >已设为推荐</el-button>
            </span>
            <span v-else>
              <el-button style="color:#1ABC9C" disabled v-if="scope.row.recommend===0" type="button" @click="set(scope.row)">设为推荐</el-button>
              <el-button v-else style="color:#FF8000" type="button" @click="canc(scope.row)" >已设为推荐</el-button>
            </span>
            <el-button type="primary" size="mini" :disabled="scope.row.mergely || scope.row.id===null"  title="对比" plain @click="addMergely(scope.row,scope.$index)">+对比</el-button>
          </template>
        </el-table-column>
      </el-table>
         </div>
      <!-- <div class="pager-container"> -->
        <page :page="form.page" @changed="bindData"></page>
        <!-- <code-detail ref="codedetail" :sid="sid" :tid="tid"  @back="bindData"></code-detail> -->
      <!-- </div> -->
    </div>
  </div>
</template>
<script>
/**
 * @file 提交详情组件
 * @author 张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/22
 */
import page from '@/components/page'
import codeDetail from './codeDetail'
import Vue from 'vue'
import { fetchpageAssignmentSubmit, updatescore, updaterecommend, editdelRecommend, CheckSimilarity } from '@/api/assignmentmanage/assgin'
// import { findPage, personcount, studycount } from '@/api/coursemanage/pathmanage'
export default {
  components: { page,
    fetchpageAssignmentSubmit,
    updatescore,
    updaterecommend,
    codeDetail },
  props: ['assignmentId'],
  data() {
    return {
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },

        search: '',
        creation_time: null,
        value: undefined

      },
      checked: false,
      flag: false,
      // listMistakeSum: [
      // ],
      sum: '', // 总人数
      num: '', // 总课数
      loading: false,
      grades: [], // 所有年级
      classInfo: [], // 所有班级
      gridData: [],
      assignment_id: this.assignmentId,
      noScore: this.$route.query.score,
      multipleSelection: [],
      assignmentList: [],
      hide: 'false',
      recenttime: undefined,
      options: [{
        value: '0',
        label: '未提交作业'
      }, {
        value: '1',
        label: '已提交作业'
      }]
      // sid: 0,
      // tid: 0
    }
  },
  watch: {
    mergely: {
      handler(newVal, oldVal) {
        if (this.assignmentList.length === 0) {
          this.gridData.forEach(element => {
            element.mergely = false
          })
        }
      },
      deep: true
    }
  },
  computed: {
    mergely() {
      this.assignmentList = this.$store.getters.assignmentList
      return this.gridData
    }
  },
  created() {
    this.$nextTick(e => {
      this.bindData()
      this.initWebSocket()
      this.gethide()
    })
  },
  methods: {
    search1() { // 搜索
      this.bindData()
    },
    // 是否显示评分框
    checke(scope) {
      scope.checked = true
    },
    // 评分确认
    cofirm(score) {
      if (score.id != null) {
        updatescore(score.id, score.score).then(response => {
        // debugger
          if (response.data.success) {
            this.$sucmsg('评分成功!')
            // this.gridData.splice(index, 1)
            this.bindData()
            this.$emit('success')
          } else {
            this.$errmsg('评分失败!')
          }
        })
      } else {
        this.$errmsg('学生未提交作业评分失败!')
      }

      score.checked = false
    },
    // 设为推荐
    set(scope) {
      // const recommend = 1
      this.$cfm('确定要把作品设为推荐吗?', e => {
        updaterecommend(scope.id).then(response => {
          if (response.data.success) {
            this.$sucmsg('推荐成功!')
            // this.gridData.splice(index, 1)
            this.bindData()
          } else {
            this.$errmsg('推荐失败!')
          }
        })
      }

      )
    },
    // 取消设为推荐
    canc(scope) {
      this.$cfm('确定要把作品取消推荐吗?', e => {
        editdelRecommend(scope.id).then(response => {
          if (response.data.success) {
            this.$sucmsg('取消成功!')
            // this.gridData.splice(index, 1)
            this.bindData()
          } else {
            this.$errmsg('取消失败!')
          }
        })
      })
    },
    // 取消评分
    cancel(scope) {
      scope.checked = false
    },
    addMergely(row, index) {
      const assignment = {
        id: row.id,
        name: row.student.name,
        title: row.assignment.title
      }
      if (this.$store.getters.assignmentList.length < 2) {
        this.gridData[index].mergely = true
        this.$store.commit('ADD_ASSIGNMENT', assignment)
        this.assignmentList = this.$store.getters.assignmentList
      } else {
        this.$message({
          type: 'warning',
          message: '最多添加两条数据！'
        })
      }
    },
    // 重置
    reset() {
      this.$refs['form'].resetFields()
      this.bindData()
    },
    // 返回
    callback() {
      this.$router.push({ name: 'assign' })
    },
    // 查看代码
    look(row) {
      this.$router.push({ path: 'codeDetail', query: { id: row.id, trainingId: row.trainingId }})
    },

    // 数据绑定
    bindData() {
      var vm = this
      this.loading = true
      // debugger

      const params = this.$formToParams(this.$refs.form)
      if (this.noScore === 0) {
        params.form = {
          score: this.noScore
        }
      } else {
        let assignmentsubmentid = this.assignment_id
        if (!assignmentsubmentid) {
          assignmentsubmentid = this.$route.query.assignmentId
        }
        params.form = {

          id: assignmentsubmentid
          // grade_id: this.$route.query.gradeid
          // course_id: this.$route.query.courseid
          // submit: this.form.value
        }
      }

      params.search = '%' + this.form.search + '%'
      params.submit = this.form.value
      params.courseID = this.$route.query.courseid
      params.gradeID = this.$route.query.gradeid
      params.groupID = this.$route.query.groupid
      if (this.form.creation_time != null) {
        params.form.creation_time = '%' + this.form.creation_time + '%'
      }
      fetchpageAssignmentSubmit(params).then(response => {
        vm.loading = false
        const data = response.data.data
        vm.gridData = data.records
        console.log(vm.gridData)
        for (var i = 0; i < vm.gridData.length; i++) {
          Vue.set(vm.gridData[i], 'checked', false)
          Vue.set(vm.gridData[i], 'flag', false)
          Vue.set(vm.gridData[i], 'mergely', false)
        }
        vm.recenttime = data.records.map(e => e.assignment.duplicateChecking)[0]
        this.form.page.total = data.total
        this.loading = false
        this.page = data.current
      }).catch(() => {
        this.loading = false
      })
    },
    initWebSocket() { // 初始化weosocket
      const Host = process.env.HOST
      const wsuri = 'ws' + Host.substring(4) + '/mywebsocket/DPS007'// 这个地址由后端童鞋提供
      this.websock = new WebSocket(wsuri)
      this.websock.onmessage = this.websocketonmessage
      this.websock.onopen = this.websocketonopen
      this.websock.onerror = this.websocketonerror
      this.websock.onclose = this.websocketclose
    },
    websocketonopen() { // 连接建立之后执行send方法发送数据
      // alert('连接成功')
      // this.websocketsend(444)
      // console.log(111)
    },
    websocketonerror() { // 连接建立失败重连
      this.initWebSocket()
    },
    websocketonmessage(e) {
      // debugger
      var da = e.data
      console.log(da)
      if (da === '1') {
        this.$sucmsg('查重完成!')
        var d = new Date()
        d.setTime(d.getTime() + (1 * 24 * 60 * 60 * 1000))
        var expires = ';expires=' + d.toUTCString()
        var xuanze = false
        document.cookie = 'looklike=' + xuanze + expires + ';path=/'
        this.gethide()

        // alert('查重完成')
        this.bindData()
      }
    },
    websocketsend(Data) { // 数据发送
      this.websock.send(Data)
    },
    websocketclose(e) { // 关闭
      console.log('断开连接', e)
    },
    gethide() {
      var cookies = document.cookie

      var arr1 = cookies.split(';')
      if (arr1.length > 1) {
        for (let index = 0; index < arr1.length; index++) {
          const element = arr1[index]

          var blank = element.replace(/\s*/g, '')
          var arr2 = blank.split('=')
          for (let i = 0; i < arr2.length; i++) {
            const app = arr2[i]
            if (app === 'looklike') {
              this.hide = arr2[1]
            }
          }
        }
      }
    },
    looklike() {
      if (this.recenttime != null) {
        this.$cfm('你已经在' + this.recenttime + '查重，确定再次查重么', e => {
          const ids = this.gridData.map(b => b.id)

          CheckSimilarity(ids).then(e => {
            var d = new Date()
            d.setTime(d.getTime() + (1 * 24 * 60 * 60 * 1000))
            var expires = ';expires=' + d.toUTCString()
            var xuanze = true
            document.cookie = 'looklike=' + xuanze + expires + ';path=/'
            this.gethide()

            this.$sucmsg(e.data.data)
          })
        })
      } else {
        const ids = this.gridData.map(b => b.id)

        CheckSimilarity(ids).then(e => {
          var d = new Date()
          d.setTime(d.getTime() + (1 * 24 * 60 * 60 * 1000))
          var expires = ';expires=' + d.toUTCString()
          var xuanze = true
          document.cookie = 'looklike=' + xuanze + expires + ';path=/'
          this.gethide()
          this.$sucmsg(e.data.data)
        })
      }
    },
    lookSimilarity(row) {
      this.$router.push({ name: 'codeSimilarity', query: { assignmentId: row.id }})
    }

  }
}
</script>
<style lang="scss" scoped>
.main .maincontent-top{
height: 100px;
/deep/
.nav{
   line-height: 90px;
    padding: 20px;
    font-weight: bold;
}
}
.td-opc  /deep/.el-input{
  margin-left: 18%;
    display:block;
    // position: relative;
        }
.td-opc  /deep/.el-input__inner {
    //  padding:0px px;
     margin-left: -30%;
     width: 30% !important;
        }
  .td-opc {
        position: relative;
        color:  #1ABC9C;
        .op {
            visibility: hidden;
            position: absolute;
            width: 50px;
            height: 40px;
            top:12px;
            right: 0px;
            font-size: 16px;
        }
        .badges{
    margin-left: 37%;
     position: absolute;
      width: 8px;
      height: 8px;
       margin-top: 2.9%;
   background: inherit;
    box-sizing: border-box;
     border-width: 1px;
      border-style: solid;
     border-color: #008000;
    border-radius: 13px;
    -moz-box-shadow: none;
    -webkit-box-shadow: none;
    box-shadow: none;
    font-family: '微软雅黑';
    // font-weight: 400;
    font-style: normal;
    // font-size: 12px;
   background-color:#008000;
}
    }

.stu-img{
    height: 60px;
    width: 60px;
    border-radius: 50%;
    overflow: hidden;
    vertical-align: top;
    position: relative;
    display:inline-block;
    border: 1px solid #f5f7fa;
    margin-right: 10px;
    }
.stu-img>img{
      width: 100%;
    }
   .stu-info{
       height: 60px;
       text-align: left;
      display:inline-block;
    }
    .stu-info>p{
      margin: 5px 0px 0px 0px;
    }
   .stuName{
       font-weight: 600;
       font-size:  large;
       color:#515151;
    }
   .stuCode{
      font-size: 14px;
    }
.content-new-background .app-main>div {
   background-color: #fff;
    padding-bottom: 20px;
    margin-bottom: 20px;
    border: none;
   -webkit-box-shadow: none;
    box-shadow: none;
}
.operation-button2{float: right;}
.operation-button{float: right; margin-right: 20px;}
.operation-button ul{
   list-style-type: none;
  //  margin-top: -5px;
  margin-top: 15px;
  }
  .operation-button li{
    float: left;
    text-align: center;
    padding: 5px;
  }
  .operation-button li:first-child{
      border-left: 1px solid #D7D7D7;
 }
  .operation-button li{
      border-right: 1px solid #D7D7D7;
 }
   .nav-title {
       color: #999999;
       line-height: 24px;
       margin-top: 5px;
       font-size: 14px;
       margin-bottom: 10px;
       padding: 20px;
     }
      .nav-number{
      font-size: 18px;
      margin-top: 5px;
      font-weight: bold;
      color: #606266;
      margin: 0 auto;
   }
.main{
  height:100%;
}
.main /deep/ .container{
  padding: 0px;
  margin:0;
  background-color: #FFFFFF;
}
.el-button {
     margin-left: 0px;
}
</style>

