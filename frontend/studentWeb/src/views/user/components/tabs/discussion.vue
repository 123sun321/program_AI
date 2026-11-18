<template>
    <div class="courselist">
     <div class="nodata" v-if="message.length===0">
         <img :src="require('@/assets/student/Privatecourses_Illustration.png')">
         <div class="text">他还没发表过话题</div>
     </div>
     <div v-else>
      <div  class="top-info" v-for="(c,index) in message" :key="index">
          <el-row>
            <el-col :span="22"> <div class="course-body">
              <div  class="c_info" @click="onLook(c.id)">【{{c.plateName}}】{{c.title}}</div>
               <div style="padding-top:15px;padding-left:10px;" class="name">{{c.creationTime | formatTime('{y}-{m}-{d} {h}:{i}')}}</div> 
           </div>
           </el-col> 
            <el-col :span="1"><ul style="color:#1fad4e;text-align:center;"><li class="number">{{c.commentNumber}}</li><li class="tit">回复</li></ul></el-col>
            <el-col :span="1"><ul style="color:#999999;text-align:center;"><li class="number">{{c.click}}</li><li class="tit">查看</li></ul></el-col>
          </el-row>
          </div>
           <page :page="form.page" @changed="getList"></page></div>
    </div>
</template>

<script>
import { getStudentPage, addClick } from '@/api/community/theme'
import page from '@/components/page'
export default {
  props: ['avatar'],
  components: { page },
  data() {
    return {
      message: '',
      defaultImg: 'this.src="' + require('@/assets/images/default.jpeg') + '"', // 默认头像图像
      // 分页和模糊查询
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        search: '',
        userId: this.$route.params.id,
        orderBy: 'creation_time desc'
      },
      tableData: [] // 数据列表
    }
  },
  created() {
    this.$nextTick(e => {
      this.getList()
    })
  },
  methods: {
    // 获取帖子数据
    getList() {
      const params = this.form
      getStudentPage(params).then(e => {
        const data = e.data.data
        this.message = data.records
        this.form.page.total = data.total
      })
    },
    onLook(id) {
      addClick(id).then(e => {})
      this.$router.push({ path: '/community/topic', query: { communityid: id }})
    }
  }
}
</script>

<style lang="scss" scoped>
.courselist{
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
 .course-body {
    position: relative;
    background: #fff;
    margin: 15px 0;
    height: 50px;
}  
.c_info{
   cursor: pointer;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 16px;
  color: #07111b;
   }
.detail img{
    width:50px;
    height:50px;
    border-radius:50%
}
.nickname-wrap{
    vertical-align: middle;
    position: relative;
    margin-top: -32px;
    margin-left: 40px;
}
 .name{
    font-size:15px;
    color:#999999;
}
.top-info .el-row{
     display: flex;
     flex-flow: row;
     align-items:center;
}
.top-info /deep/ .el-col-1{
    min-width:65px;
 }
 .top-info /deep/ .el-col-21{
    width: calc(100% - 195px);
 }
.top-info{
  border-bottom: 1px solid #eeeeee;
    .tit{
        font-size: 14px;
        }
        .number{
            font-size: 22px;
        }
}
</style>
