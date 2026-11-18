<template>
    <div class="report">
        <div class="nodata" v-if="tableData.length===0 && !loading">
          <img :src="require('@/assets/images/Privatecourses_Illustration.png')">
          <div class="text">他还没编写过报告</div>
        </div>
        <el-row :gutter="20">
            <el-col :span="6" v-for="item in tableData" :key="item.id">
                <div class="report-item">
                  <a class="item-top" @click="show(item.id)">
                    <div class="report-item-title"><span v-if="item.privateReport" class="private">私有</span> {{item.exerciseJoinDto.exercise.name}}</div>
                    <div class="report-item-count">
                        <span>{{item.wordNumber}} 字</span>
                        <div class="report-item-arrow">
                            <div class="report-item-arrow-line"></div>
                        </div>
                    </div>
                    <div class="report-item-content">{{item.exerciseJoinDto.exercise.knowledge}}</div>     
                  </a>
                  <div class="report-author">
                    <el-row>
                      <el-col :span="18">
                        <a @click="touser(item.exerciseJoinDto.student.userId)">
                          <div class="avatar-img pull-left">
                            <img class="pull-left" :src="item.exerciseJoinDto.student.imgUrl"  alt="">
                          </div>
                          <div class="user-name pull-left">{{item.exerciseJoinDto.student.name}}</div>
                        </a>
                      </el-col>
                      <el-col :span="6">
                        <div class="comment">
                          <span class="pull-right">{{item.commentNumber}}</span>
                          <img class="pull-right" style="margin-right:2px;" :src="require('@/assets/images/Navigationbar_Community.png')" >
                        </div> 
                      </el-col>
                    </el-row>
                  </div>
                  <div class="report-item-time">{{item.creationTime}}</div>
                </div>
            </el-col>
        </el-row>
        <page v-if="this.form.page.total>12" :page="form.page" @changed="getList" style="margin-top:20px;"></page>
    </div>
</template>

<script>
import { getStudent } from '@/api/base/student'
import { pageList } from '@/api/mycourse/report'
import page from '@/components/page'
export default {
  components: { page },
  data() {
    return {
      form: {
        page: {
          current: 1,
          pageCount: 12,
          total: 0
        },
        search: '',
        userId: '',
        orderBy: 'a.creation_time desc'
      },
      studentId: this.$route.params.id,
      tableData: [], // 数据列表
      loading: false
    }
  },
  created() {
    this.$nextTick(e => {
      this.getList()
    })
  },
  methods: {
    getList() {
      this.loading = true
      getStudent(this.studentId).then(response => {
        const userid = response.data.data.userId
        this.form.userId = userid
        pageList(this.form).then(e => {
          const data = e.data.data
          this.tableData = data.records
          this.form.page.total = data.total
          this.loading = false
        })
      })
    },
    show(id) {
      // this.$router.push({ name: 'reportdetail', query: { reportid: id }})
    },
    touser(id) {
      // this.$router.push({ path: '/user/info/' + id })
    }
  }
}
</script>

<style lang="scss" scoped>
.report{
    .report-item:hover {
        box-shadow: 0 5px 15px #e5e5e5;
        transition: box-shadow .6s ease;
    }
    .report-item {
      border-radius: 3px;
      padding: 6px 20px 20px;
      a.item-top{
        margin: 15px 0;
        display: block;
        position: relative;
        height: 150px;
        padding: 25px 15px;
        color: #565a61;
        background: #f7f8fa;
        border: 1px solid #eee;
      }
      a.item-top:before{
          content: "";
          position: absolute;
          top: -1px;
          right: -1px;
          width: 0;
          height: 0;
          border-style: solid;
          border-width: 0 16px 16px 0;
          border-color: #fff #fff #e3e8eb #e3e8eb;
      }
      .report-item-title{
          color: #4c5157;
          font-size: 15px;
          font-weight: 700;
          cursor: pointer;
          line-height: 20px;
          .private{
              background-color: #459d92;
              color: #ffffff;
              font-weight:initial;
              font-size: 12px;
              padding: 3px 5px;
              margin-right: 5px;
          }
          .private:after{
              content:"";
              width:0;
              height:0;
              position:absolute;
              left:49px;
              top:25px;
              border-top:solid 10px transparent;
              border-left:solid 5px #459d92;  
              border-bottom:solid 10px transparent;
          }
      }
      .report-author{
        .avatar-img{
          width: 20%; 
          img{
            margin-left: 4px;
            width: 24px;
            height: 24px;
            border-radius: 50%;
          }
        }
        .user-name{
          width: 80%;
          line-height: 24px;
          color: #999;
          font-size: 14px;
          max-width: 100%;
          display: inline-block;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          vertical-align: -5px;
        }
      }
      .comment{
        text-align: right;
        color: #9b9da2;
        font-size: 13px;
        line-height: 24px;
      }
      .report-item-content{
          font-size: 12px;
          color: #666;
          line-height: 16px;
          margin: 5px 0;
          height: 50px;
          overflow: hidden;
      }
      .report-item-time{
          font-size: 12px;
          color: #666;
          text-align: right;
          margin-top: 5px;
      }
      .report-item-count {
          position: absolute;
          left: -4px;
          bottom: 20px;
          padding: 0 24px 0 12px;
          height: 24px;
          line-height: 22px;
          background: #fff;
          border: 1px solid #e3e8eb;
          font-size: 12px;
          .report-item-arrow {
              top: -1px;
              right: -1px;
              border-top: 12px solid transparent;
              border-right: 12px solid #e3e8eb;
              border-bottom: 12px solid transparent;
              .report-item-arrow-line {
                  top: -11px;
                  right: -12px;
                  border-top: 11px solid transparent;
                  border-right: 11px solid #f7f8fa;
                  border-bottom: 11px solid transparent;
              }
          }
          .report-item-arrow, .report-item-arrow-line {
              position: absolute;
              width: 0;
              height: 0;
              font-size: 0;
              line-height: 0;
          }
      }
    }
    .nodata{
      padding: 15px 0;
      text-align: center;
      .text{
        font-size: 18px;
        color: #999999;
      }
    }
}
</style>
