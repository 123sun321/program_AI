<template>
<div class="wrapper">
  <div class="content">
    <div class="header">
      <div class="title">
        <div>练习记录</div>
      </div>
      <el-button type="text" @click="dialogFormVisible = true" class="filter">高级筛选</el-button>
      <el-dialog :visible.sync="dialogFormVisible" width="55%">
        <el-form :model="form" :inline="true" ref="form">
          <!-- <el-row> -->
            <el-form-item label="学生编号" :label-width="formLabelWidth" prop="stuId">
              <el-input v-model="form.stuId" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="实验标题" :label-width="formLabelWidth" prop="title">
              <el-input v-model="form.title" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="学生姓名" :label-width="formLabelWidth" prop="studentName">
              <el-input v-model="form.studentName" auto-complete="off"></el-input>
            </el-form-item>
          <!-- </el-row> -->
          <!-- <el-row> -->
            <el-form-item label="提交时间" :label-width="formLabelWidth" prop="beginTime">
            <el-date-picker
              v-model="form.beginTime"
              type="date"
              placeholder="选择日期"
              popper-class="data_picker"
              :unlink-panels=true>
            </el-date-picker>
            </el-form-item>
            <el-form-item prop="endTime">
                <el-date-picker
                v-model="form.endTime"
                type="date"
                placeholder="选择日期"
                popper-class="data_picker">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button class="reset" @click="reset">重置</el-button>
              <el-button class="search" @click="search()">查询</el-button>
            </el-form-item>
          <!-- </el-row> -->
        </el-form>
      </el-dialog>
    </div>

    <el-table
      :data="tableData"
      :header-cell-style="{ background: '#eeeeee', color: '#333333' }"
      :row-style="{ height: '109px' }">
      <el-table-column
        label="编号"
        type="index"
        align="center">
      </el-table-column>
      <el-table-column
        label="学生信息"
        width="257">
        <template slot-scope="scope">
          <img class="avantor" @click="pathToUserInfo(scope.row.userId)" :src='scope.row.imgUrl' alt="" width="60px" height="60px" style="float: left; margin-right: 10px; margin-top: 10px;">
          <div class="stuInfo">
            <span class="stuName" @click="pathToUserInfo(scope.row.userId)">{{scope.row.studentName}}</span> <br>
            <span class="gradeName">{{scope.row.gradeName}}</span> <br>
            <span>
              <img class="icon" v-for="(imgLevel, index) in ScoreToList(scope.row.integralScore)" :key="index" :src="computeImgUrl(imgLevel)" alt="">
              <!-- <img class="icon" v-for="(imgLevel, index) in [3, 3, 3, 2, 2, 2, 1, 1, 1]" :key="index" :src="computeImgUrl(imgLevel)" alt=""> -->
            </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="实验标题">
        <template slot-scope="scope">
          <div :class="scope.row.privateOrNot? '' : 'titleLink'" @click="gotoCodeView(scope)"><span>{{ scope.row.title }}</span></div>
        </template>
      </el-table-column>
      <el-table-column label="难易程度">
        <template slot-scope="scope">
          <el-rate v-model="scope.row.difficultLevel" disabled text-color="#ff9900"></el-rate>
        </template>
      </el-table-column>
      <el-table-column label="提交时间">
        <template slot-scope="scope">
          <span>{{ scope.row.submitTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="查看状态">
        <template slot-scope="scope">
          <div :class="scope.row.privateOrNot ? 'private' : 'public'">{{scope.row.privateOrNot ? '私有' : '公开'}}</div>
        </template>
      </el-table-column>
      <el-table-column label="点赞" align="center" width="100">
        <template slot-scope="scope">
          <img :src="selectLikeImg(scope.row.isApprove)" alt="" style="margin-left: 10px;" class="like" @click="addLikeNumber(scope.row)"> <br>
          <span style="margin-left: 10px">{{ scope.row.likeNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="100">
        <template slot-scope="scope">
          <el-button type="text" @click="gotoProcessDemoAnalysis(scope.row)">详细</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pager-container">
      <page :page="form.page" @changed="bindData"></page>
    </div>
  </div>
</div>
</template>

<script>
/**
 * @file 所有人的练习记录分页
 * @author 宋乃乐
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/29
 */
import { getTrainingList, insertLike } from '../../api/training/index'
import page from '@/components/page'
import { mapGetters } from 'vuex'
export default {
  name: 'App',
  components: { page },
  data() {
    return {
      tableData: [],
      dialogFormVisible: false,
      form: {
        page: {
          current: 1, // 当前页码
          pageCount: 10, // 一页呈现的数据数
          total: 0 // 总的数据数
        },
        stuId: '',
        title: '',
        studentName: '',
        beginTime: '',
        endTime: '',
        orderBy: 'creation_time desc'
      },
      formLabelWidth: '120px'
    }
  },

  mounted() {
    this.bindData()
  },
  computed: {
    ...mapGetters(['unitStarScore'])
  },
  methods: {
    // 增加点赞
    addLikeNumber(row) {
      if (row.isApprove) {
        return
      } else {
        insertLike(row.id).then(data => {
          this.$message({
            message: '点赞成功',
            type: 'success'
          })
          row.isApprove = !row.isApprove
          row.likeNumber += 1
        }, () => {
          this.$message.error('点赞失败')
        })
      }
    },

    // 根据等级计算图片地址
    computeImgUrl(imgLevel) {
      const sunlight = 3
      const moon = 2
      const stars = 1

      if (imgLevel === sunlight) {
        return require('@/assets/student/Exercisenotes_sunlight.png')
      } else if (imgLevel === moon) {
        return require('@/assets/student/Exercisenotes_moon.png')
      } else if (imgLevel === stars) {
        return require('@/assets/student/Exercisenotes_stars.png')
      } else {
        return ''
      }
    },

    // 选择点赞图片资源
    selectLikeImg(isLiked) {
      if (isLiked) {
        return require('@/assets/student/Worksdisplay_fabulouss.png')
      } else {
        return require('@/assets/student/Worksdisplay_fabulousss.png')
      }
    },

    search() { // 搜索
      this.bindData()
    },

    reset() { // 重置
      this.$refs['form'].resetFields()
      this.dialogFormVisible = false
      this.bindData()
    },

    // 绑定数据 根据是否存在form表单来确定传何种参数
    bindData() {
      const vm = this
      // 发送请求 同步组件中的数据
      function getData(params) {
        // console.log(params)
        getTrainingList(params).then(data => {
          // console.log(data, '返回的数据')
          vm.tableData = data.data.data.records
          vm.form.page.total = data.data.data.total
          vm.form.page.current = data.data.data.current
        })
      }
      // 在该页面中需要判断表单是否存在
      if (!this.$refs.form) {
        const params = {}
        params.page = this.form.page
        params.form = {}
        getData(params)
      } else {
        const params = this.$formToParams(this.$refs.form)
        getData(params)
        this.dialogFormVisible = false
      }
    },

    // 跳转路由到详情页
    gotoCodeView(scope) {
      if (!scope.row.privateOrNot) {
        this.$router.push({ name: 'runtraining', query: { id: scope.row.id }})
      }
    },
    // 根据积分数返回列表
    ScoreToList(score) {
      // 将十进制转化成4进制数组
      const Q = 4
      const Unit = this.unitStarScore
      const totalstarnum = Math.floor(score / Unit)
      const arr = []
      // 通过计算对应的太阳,月亮,星星数 最后合成等级数组

      const sunnum = Math.floor(totalstarnum / (Q * Q))
      const moonnum = Math.floor((totalstarnum % (Q * Q)) / Q)
      const starnum = Math.floor(totalstarnum % Q)
      for (let i = 0; i < sunnum; i++) {
        arr.push(3)
      }
      for (let i = 0; i < moonnum; i++) {
        arr.push(2)
      }
      for (let i = 0; i < starnum; i++) {
        arr.push(1)
      }

      return arr
    },
    pathToUserInfo(id) {
      this.$router.push({ path: '/user/info/' + id })
    },
    gotoProcessDemoAnalysis(row) {
      this.$router.push({
        name: 'ProcessDemoAnalysis',
        query: { recordId: row.id }
      })
    }
  }
}
</script>

<style scoped>

.wrapper {
  padding-bottom: 50px;
  background-color: #ffffff;
  margin: 0 auto;
  width: 100%;
}
.content {
  /* width: 1280px; */
  width: 65%;
  height: 100%;
  margin: 0 auto;
  position: relative;
}
.content .header {
  width: 100%;
  min-height: 66px;
  position: relative;
}
.title {
  position: absolute;
  top: 18px;
}
.title:after {
  content: "";
  display: block;
  width: 4px;
  height: 20px;
  background: #1fad4e;
  margin-top: -24px;
}
.title div {
  margin-left: 10px;
  font-family: 'PingFang-SC-Regular';
  font-size: 22px;
  color: #333333;
}
.filter {
  display: block;
  width: 107px;
  height: 30px;
  border-radius: 15px;
  background: #e8f7ed;
  border: 0;
  color: #1fad4e;
  font-family: 'PingFang-SC-Regular';
  font-size: 16px;
  position: absolute;
  right: 10px;
  line-height: 0;
  top: 20px;
}
.filter:hover {
  cursor:pointer;
}

.stuName {
  font-size: 20px;
  color: 3333333;
  font-family: 'PingFang-SC-Medium';
  cursor: pointer;
}
.gradeName {
  font-size: 16px;
  font-family: 'PingFang-SC-Regular';
  color: #999999;
}
.like:hover {
  cursor: pointer;
}

.private {
  width: 70px;
  height: 28px;
  border: 0;
  border-radius: 14px;
  background: #eeeeee;
  line-height: 26px;
  text-align: center;
  font-family: 'SourceHanSansCN-Light';
  font-size: 16px;
  color: #666666;
}
.public {
  width: 70px;
  height: 28px;
  border: 0;
  border-radius: 14px;
  background: #e8f7ed;
  line-height: 26px;
  text-align: center;
  font-family: 'SourceHanSansCN-Light';
  font-size: 16px;
  color: #1fad4e;
}
.reset {
  width: 76px;
  height: 34px;
  border-radius: 15px;
  border: 2px solid #1fad4e;
  background-color: #fff;
  color: #1fad4e;
  font-size: 14px;
  line-height: 3px;
  margin-left: 60px;
}
.reset:hover {
  cursor:pointer;
}
.search {
  width: 76px;
  height: 34px;
  border-radius: 15px;
  border: 2px solid #1fad4e;
  background-color: #1fad4e;
  color: #fff;
  font-size: 14px;
  line-height: 3px;
  margin-left: 30px;
}
.search:hover {
  cursor:pointer;
}
.pagination {
  width: 100%;
  text-align: center;
  height: 72px;
  line-height: 200%;
}
.el-pagination {
  padding-top: 22px;
  font-family: 'SourceHanSansCN-Light';
  color: #999999;
}
.el-pagination .el-pager li.active {
  color: white;
  cursor: pointer;
  background: black;
  border-radius: 20px;
}
.el-pagination .el-pager li {
  min-width: 28px;
}
.data_picker td.current:not(.disabled) span{
  background-color: #1fad4e;
}
.data_picker td.today span {
  color: black;
}
.titleLink {
  cursor: pointer;
  text-decoration: underline;
}
.icon {
  width: 18px;
}
.avantor {
  display: block;
  background: url('../../assets/images/default.jpeg') no-repeat;
  background-position: 50% 50%;
  background-size:contain;
  cursor: pointer;
}
</style>
