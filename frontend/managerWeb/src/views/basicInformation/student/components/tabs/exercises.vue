<template>
    <div class="exercises">
      <div class="nodata" v-if="tableData.length===0 && !loading">
        <img :src="require('@/assets/images/Privatecourses_Illustration.png')">
         <div class="text">他还没参与过实验</div>
      </div>
      <el-row :gutter="20">
          <el-col :span="6" v-for="item in tableData" :key="item.id">
              <div class="item">
                  <img class="exercises-logo" :src="item.exercise.logo">
                  <div class="title">{{item.exercise.name}}</div>
                  <div class="bottom clearfix">
                      <div class="joinbumber pull-left"><img :src="require('@/assets/images/Courselibrary_Number.png')" > {{item.exercise.joinNumber}}</div>
                      <div class="pull-right">
                          <!-- <el-button type="success" @click="start(item.exerciseId)" plain round>参与</el-button> -->
                      </div>
                  </div>
              </div>
          </el-col>
      </el-row>
      <page v-if="this.form.page.total>12" :page="form.page" @changed="getList" style="margin-top:20px;"></page>
    </div>
</template>

<script>
import { getStudent } from '@/api/base/student'
import { getCurrentPage } from '@/api/mycourse/exercise'
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
        orderBy: 'a.creation_time desc',
        userId: ''
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
        getCurrentPage(this.form).then(e => {
          const data = e.data.data
          this.tableData = data.records
          this.form.page.total = data.total
          this.loading = false
        })
      })
    },
    start(exerciseid) {
      // this.$router.push({ name: 'running', query: { exerciseid: exerciseid }})
    }
  }
}
</script>

<style lang="scss" scoped>
.exercises{
      padding: 20px;
    .item{
        color: #666666;
        border: 1px solid #dbdada;
        margin-bottom: 20px;
        .exercises-logo{width: 100%;height: 170px;}
        .title{font-size: 16px;line-height: 30px;padding: 5px 10px; overflow:hidden;
  text-overflow: ellipsis;
  white-space: nowrap;}
        .bottom{
            padding: 5px 10px;
            .joinbumber{font-size: 14px;line-height: 26px;}
            .el-button{
                color: #1fad4e;
                border:none;
                background:#e8f7ed;
                padding:7px 10px;
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
