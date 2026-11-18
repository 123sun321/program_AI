<template>
    <div class="run-main">
    <!-- 顶部 -->
    <el-row>
     <el-col :span="7" >
        <div class="left-content">
          <div class="nav">
            <span class="pic" @click="callback"><img  :src="require('@/assets/student/Experiment_return.png')" ></span>
            <span class="tit">
              <div class="tit-tab" v-bind:class="{active:show}" @click="show=true">作业要求</div>
              <div class="tit-tab" v-bind:class="{active:!show}" @click="show=false">作业资源</div>
            </span>
          </div>
          <div class="content">
              <runleft v-show="show" :assignmentId="assignmentId"></runleft>
              <assignment-resource v-show="!show" :assignmentId="assignmentId"></assignment-resource>
          </div>
        </div>
        </el-col>
        <el-col :span="17">
        <!-- <ul class="but">
           <li @click="onrun"><span class="img"><img :src="require('@/assets/student/Experiment_play.png')"></span><span class="type"> 运行</span></li>
           <li @click="onsave"><span class="img"><img :src="require('@/assets/student/Experiment_Preservation.png')"></span><span class="type"> 保存</span></li>
           <li @click="onload"><span class="img"><img :src="require('@/assets/student/Experiment_Formatbrush.png')"></span><span class="type"> 格式刷</span> </li>
        </ul> -->
          <!-- <div class="but2">
            <el-form :inline="true" :model="form" ref="form">
            <el-form-item label="练习:" prop="trainingId">
              <el-select v-model="form.trainingId" filterable placeholder="全部">
                  <el-option  v-for="(item,index) in trainingData" :key="index" :label="item.title"  :value="item.id">
                  </el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
               <el-button icon="el-icon-download" type="danger" @click="join()">导入</el-button>
            </el-form-item>
             </el-form>
          </div> -->
          <div class="right-content">
            <div class="content">
              <web-ide :id="assignmentId" :codeId="entity.codeId" redirectUrl="/mycourse/course/assignment" type="assignment"></web-ide>
            </div>
          </div>
      </el-col>
     </el-row>
    </div>
</template>
<script>
/**
 * @file 作业编辑组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/29
 */
import runleft from './submit/runleft'
import assignmentResource from './submit/assignmentResource'
import { submitAssignment, resetSubmitAssignment } from '@/api/assignment'
import { TrainingList, getTrainAndCodeById } from '@/api/assignment/index'
import page from '@/components/page'
import WebIde from '@/components/WebIde'
import markDown from '@/components/markDown'
export default {
  components: {
    page,
    WebIde,
    runleft,
    assignmentResource,
    markDown
  },
  data() {
    return {
      baseHost: process.env.BASE_API,
      assignmentId: this.$route.query.id,
      trainingData: [],
      show: true,
      // 实验数据实体
      entity: {
        codeDto: {},
        exerciseResourceDtoList: []
      },
      form: {
        page: {
          current: 1,
          pageCount: 9,
          total: 0
        },
        courseId: '',
        trainingId: undefined,
        search: '',
        orderBy: 'creation_time desc'
      },
      content: '',
      trainId: 0,
      flag: 0
    }
  },
  // watch: {
  //   newTraiing(newVal, oldVal) {
  //     if (newVal) {
  //       this.form.trainingId = newVal
  //       this.getCodeByTrainingId(this.form.trainingId)
  //     }
  //   }
  // },
  computed: {
    newTraiing() {
      return this.form.trainingId
    }
  },
  created() {
    this.bindData()
    this.$nextTick(e => {
      this.getlist()
    })
  },
  methods: {
    // 绑定表格数据
    bindData() {
    },
    getlist() {
      TrainingList().then(response => {
        this.trainingData = response.data.data
      })
    },
    getCodeByTrainingId(id) {
      getTrainAndCodeById(id).then(e => {
        this.entity.codeDto = e.data.data.code
      })
    },
    // 提交作业
    submit() {
      // this.$router.push({ name: 'runtraining', query: { id: item.id }})
      const param = {
        assignment_id: this.assignmentId,
        code: {
          html: this.entity.codeDto.html,
          htmlConfig: this.entity.codeDto.htmlConfig,
          css: this.entity.codeDto.css,
          cssConfig: this.entity.codeDto.cssConfig,
          js: this.entity.codeDto.js,
          jsConfig: this.entity.codeDto.jsConfig
        }
      }
      submitAssignment(param).then(e => {
        if (e.data.success) {
          this.$sucmsg('提交成功!')
          // this.getList()
          this.$router.push({ name: 'assignment' })
        }
      })
    },
    // 重新提交作业
    reSubmit() {
      const param = {
        assignment_id: this.assignmentId,
        code: {
          html: this.entity.codeDto.html,
          htmlConfig: this.entity.codeDto.htmlConfig,
          css: this.entity.codeDto.css,
          cssConfig: this.entity.codeDto.cssConfig,
          js: this.entity.codeDto.js,
          jsConfig: this.entity.codeDto.jsConfig
        }
      }
      resetSubmitAssignment(param).then(e => {
        if (e.data.success) {
          this.$sucmsg('提交成功!')
          // this.getList()
          this.$router.push({ name: 'assignment' })
        }
      })
    },
    callback() {
      this.$router.push({ name: 'assignment' })
    },
    onrun() {
      this.$sucmsg('运行！')
    },
    onsave() {
      this.$sucmsg('保存！')
    },
    onload() {
      this.$sucmsg('格式刷！')
    },
    join() {
      this.entity.codeDto = ''
      this.getCodeByTrainingId(this.form.trainingId)
    }
  }
}
</script>
<style scoped lang="scss">
 .imgs{
      width: 100%;
      height: 348px;
    }
.run-main{
  .el-row{
    height: 100%;
    .el-col{
      height: 100%;
    }
  }
  height: 100%;
  background-color: #fff;
}
.but{
  height: 50px;
      // float: left;
      // padding-top:7px;
    li{
      cursor: pointer;
      padding-right:30px;
      padding-left: 10px;
      float: left;
      display: flex;
      align-items:center;
      line-height: 50px;
      img{
        float: left;
      }
 .type{
   padding-left: 5px;
       color: #ededed;
    }
   }
}
.but2{
   padding-top: 9px;
  /deep/ .el-form-item__label {
    color: #ededed;
    font-size: 16px;
  }
}
.left-content{
  height: 100%;
  display: flex;
  flex-flow: column;
  .nav{
      height:  50px;
      line-height: 50px;
      width: 100%;
      background-color:#2D2E3F;
      position: relative;
      .pic{
        cursor: pointer;
        float: left;
        padding: 0px 10px;
      }
    .tit{
        font-size: 16px;
        color: #fff;
        z-index: 110;
      }
    .tit-tab{
      cursor: pointer;
      padding: 0px 10px 10px 10px;
      float: left;
    }
    .tit-tab:hover,.active{
      background: url('~@/assets/images/console-tab-bg.png') no-repeat;
      background-size: 100% 100%;
    }
  }
  .content {
    flex: 1;
    overflow: auto;
  }
}
.right-content{
  height: 100%;
  display: flex;
  flex-flow: column;
  .operation-button{
    background-color: #666666;
    height:  50px;
    padding: 0 10px;
    line-height: 50px;
    text-align: right;
    color:#fff;
      span{
        font-size:20px;
        margin-right:10px;
      }
    }
    .content {
     flex: 1;
    }
}

</style>

