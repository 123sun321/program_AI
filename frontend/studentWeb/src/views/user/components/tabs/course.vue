<template>
    <div class="courselist">
     <div class="nodata" v-if="tableData.length===0 && !loading">
         <img :src="require('@/assets/student/Privatecourses_Illustration.png')">
         <div class="text">他还没学习过课程</div>
     </div>
     <el-row v-else>
        <el-col :span="12" v-for="(item,index) in tableData" :key="index">
            <div class="item">
                <el-row :gutter="10">
                    <el-col :span="8">
                        <img class="logo" :src="item.course.logo">
                    </el-col>
                    <el-col :span="11">
                        <div class="c_title"><a href="#">{{item.course.name}}</a></div>
                        <div class="c_info">
                            <span>参与{{item.joinExercise}}篇实验</span>
                        </div>
                        <div class="c_info">
                            <span>{{item.creationTime}}</span>
                        </div>
                    </el-col>
                    <el-col :span="5" class="c_btn">
                        <el-button type="success" @click="gocourse(item.courseId)" plain round >学习课程</el-button>
                    </el-col>
                </el-row>
            </div>
        </el-col>
     </el-row>
     <page v-if="this.form.page.total>10" :page="form.page" @changed="getList" style="margin-top:20px;"></page>
    </div>
</template>

<script>
import { studentJoinCourse } from '@/api/mycourse/course'
import page from '@/components/page'
export default {
  components: { page },
  data() {
    return {
      form: {
        page: {
          current: 1,
          pageCount: 9,
          total: 0
        },
        search: '',
        userId: this.$route.params.id,
        orderBy: 'a.creation_time desc'
      },
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
      studentJoinCourse(this.form).then(e => {
        const data = e.data.data
        this.tableData = data.records
        this.form.page.total = data.total
        this.loading = false
      })
    },
    gocourse(courseid) {
      this.$router.push({ name: 'courseDetail', query: { 'courseId': courseid }})
    }
  }
}
</script>

<style lang="scss" scoped>
.courselist{
    .item{
        border-right:1px solid #eeeeee;
        border-bottom:1px solid #eeeeee;
        padding: 15px 10px;
        .logo{width: 100%;height: 90px;}
        .c_title{font-size: 16px;color: #333333;line-height: 30px;margin-bottom: 10px;font-weight: bolder;}
        .c_summary{font-size: 12px;color: #666666;margin-bottom: 20px;}
        .c_info{font-size: 12px;color: #666666;line-height: 20px;}
        .c_btn{
            .el-button{
                color: #1fad4e;
                border-color:#ffffff;
                background:#e8f7ed;
            }
            .el-button:hover{border-color:#1fad4e; }
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
.courselist>.el-row>.el-col:nth-child(2n){
    .item{border-right: none;}
}
</style>
