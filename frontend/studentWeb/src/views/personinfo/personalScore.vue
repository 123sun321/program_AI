<template>
  <div  class="scoreBox">
    <div class="header-container dk-box">
      <el-row :gutter="20" style="margin-top:15px;margin-bottom: 15px;">
        <el-col :span="6">
          <div class="home-center">
            <div class="center-info-notice">
              <p class="info-num-cc1">当前可用积分</p>
              <p class="info-num-c1"><span>{{scoreAll}}</span><span>分</span></p>  
            </div>  
          </div>
        </el-col> 
        <el-col :span="6">
          <div class="home-center">
            <div class="center-info-notice">
                <p class="info-num-cc2">本周新增积分</p>
                <p class="info-num-c2"><span>{{scoreWeek}}</span><span>分</span></p>
            </div>  
          </div>
        </el-col>
        <el-col :span="6">
          <div class="home-center">
            <div class="center-info-notice">
                <p class="info-num-cc3">本月新增积分</p>
                <p class="info-num-c3"><span>{{scoreMonth}}</span><span>分</span></p>
            </div>  
          </div>
        </el-col>
         <el-col :span="6">
             <div class="home-center" v-show="timeSearch">
            <div class="center-info-notice">
          <p class="info-num-cc2">时间段积分</p>
          <p class="info-num-c2"><span>{{sum1}}</span><span>分</span></p>
        </div>
        </div>
         </el-col>
      </el-row>
    </div>
    <div class="body-container dk-box">
      <div class="search-Scoretime">
        <span class="filter-item" style="color: #656363;">积分查询：</span>
        <el-date-picker class="filter-item" style="display:inline-flex"
          v-model="selectDate"
          type="daterange"
          text="111"
          range-separator="至"
          :picker-options="pickerOptions2"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
        <el-button class="filter-item" style="margin-left:10px" type="primary"  icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
        <el-button plain @click="resetForm('form')">重置</el-button>
      </div>
      <div class="class-table">
        <el-table :inline="true"    style="width: 100%" :data="scoreList">
          <el-table-column label="积分类型" align="center" prop="name"></el-table-column>
          <el-table-column label="发放积分" align="center" prop="num"></el-table-column>
          <el-table-column label="发放时间" align="center" prop="issueTime"></el-table-column>
          <!-- <el-table-column label="到期时间" align="center" prop="endTime"></el-table-column>     -->
          <el-table-column label="备注" align="center" prop="remark" width="200"></el-table-column>  
        </el-table>
      </div> 
      <div class="page">
          <el-pagination  @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageIndex" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
      </div> 
    </div>
    
  </div>
</template>

<script>
import {
  fetchPage, selectCount
} from '@/api/person/personalscore'
import page from '@/components/page'

export default {
  components: {
    page
  },

  data() {
    return {
      listQuery: {
        pageIndex: 1,
        pageSize: 10,
        startDate: undefined,
        endDate: undefined,
        orderFiled: 'issueTime',
        isAsc: false
      },
      scoreList: [],
      total: 0,
      loading: false,
      timeSearch: false,
      scoreAll: 0,
      pickerOptions2: { // 时间选择限制
        disabledDate(time) {
          return time.getTime() >= Date.now() - 8.64e6
        } },
      searchScore: 0,
      scoreMonth: 0,
      scoreWeek: 0,
      selectDate: undefined
    }
  },
  computed: {
    sum1() {
      let sum = 0
      for (let i = 0; i < this.scoreList.length; i++) {
        sum += this.scoreList[i].num
      }
      return sum
    }
  },
  created() {
    this.$nextTick(e => {
      this.getList()
      this.getScore()
    })
  },
  methods: {
    getList() {
      this.timeSearch = false
      this.loading = true
      this.searchScore = 0
      if (this.selectDate) {
        this.listQuery.startDate = this.selectDate[0]
        this.listQuery.endDate = this.selectDate[1]
        this.timeSearch = true
      } else {
        this.listQuery.startDate = undefined
        this.listQuery.endDate = undefined
      }
      fetchPage(this.listQuery).then(response => {
        this.scoreList = response.data.data.records
        this.total = Number(response.data.data.total)
      })
      this.getSearchScore()
    },
    getScore() {
      selectCount().then(e => {
        this.scoreAll = e.data.data.scoreAll
        this.scoreMonth = e.data.data.scoreMonth
        this.scoreWeek = e.data.data.scoreWeek
      })
    },
    getSearchScore() {

    },
    handleFilter() {
      this.listQuery.pageIndex = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.pageIndex = val
      this.getList()
    },
    // 重置搜索条件
    resetForm(formName) {
      // this.$refs[formName].resetFields()
      // this.$refs['listQuery'].resetFields()
      this.listQuery = {
        pageIndex: 1,
        pageSize: 10,
        startDate: undefined,
        endDate: undefined,
        orderFiled: 'issueTime',
        isAsc: false
      }
      this.selectDate = undefined
      // fetchPage(newlistQuery).then(response => {
      //   this.scoreList = response.data.data.records
      //   this.total = Number(response.data.data.total)
      // })
      this.getList()
    }
  }
}
</script>

<style scoped>
.page {
    padding: 5px 0px;
    text-align: right;
  }
.export-table{
  margin: 20px;
}
.scoreBox{
  background-color: #E4E7ED;
  padding:10px;
 box-sizing: border-box;
}
.content-new-background .app-main>div {
    background-color: #E4E7ED;
    box-shadow: 0 0 0px 0 rgba(0,0,0,.2);
}
.header-container{
 height: 180px;
  overflow: hidden;
}
.dk-box{
    background-color: #fff;
    /* box-shadow: 0 0 1px 0 rgba(0,0,0,.2); */
}
.body-container {
   /* padding: 20px; */
  margin-top: 10px;
  border-top: 1px solid #e8e8e8;
}
.search-Scoretime{
  margin-left: 2%;
  margin-top: 20px;
  margin-bottom: 20px;
}
.home-center{
   height: 100px;
   min-width: 280px;
   align-content: center; 
   background: #fff;
}
.center-info-notice{
    /* border:1px solid blue; */
    height: 100%;
    width: 64%;
    float: left;
    text-align: center;
}
.home-center img{
        margin-top: 27px;
}
.info-num-c1,.info-num-c2,.info-num-c3,.info-num-c4{
      margin: 20px 0px 2px 0px;
}
.info-num-c1 span:first-child,.info-num-c2 span:first-child,.info-num-c3 span:first-child,.info-num-c4 span:first-child{
    font-size: 30px;
}
.info-num-c1 span:last-child,.info-num-c2 span:last-child,.info-num-c3 span:last-child,.info-num-c4 span:last-child{
    font-size: 20px;
}
.info-num-c1{
    color:#7D7DDF;
}
.info-num-c2{
    color:#E3C141;
}
.info-num-c3{
    color:#1FB2AA;
}
.info-num-c4{
    color:#1ABEE1;
}
.info-num-cc1,.info-num-cc2,.info-num-cc3,.info-num-cc4{
  margin-top: 20px;
  font-size: 20px;
  color:#333333;
}
</style>
