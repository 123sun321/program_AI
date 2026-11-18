<template>
    <div class="courselist">
     <div class="nodata" v-if="message.length===0 && !loading">
         <img :src="require('@/assets/student/Privatecourses_Illustration.png')">
         <div class="text">还没发表过话题</div>
     </div>
     <div v-else>
      <div  class="top-info" v-for="(c,index) in message" :key="index">
          <el-row>
              <!-- <el-col :span="1"><div class="detail"><img :src="avatar"></div></el-col> -->
            <el-col :span="21"> <div class="course-body">
              <div  class="c_info" @click="onLook(c.id)">【{{c.plateName}}】{{c.title}}</div>
               <div style="padding-top:15px;padding-left:10px;" class="name">{{c.creationTime | formatTime('{y}-{m}-{d} {h}:{i}')}}</div> 
                 <!-- <div class="detail">
               <img :src="defaultImg" :onerror="defaultImg">
                <div class="nickname-wrap">
                <div class="name">{{c.name}}小风</div> 
                </div>  
                </div>       -->
           </div>
           </el-col> 
               <el-col :span="1"><ul style="color:#1fad4e;text-align:center;"><li class="number">{{c.commentNumber}}</li><li class="tit">回复</li></ul></el-col>
            <el-col :span="1"><ul style="color:#999999;text-align:center;"><li class="number">{{c.click}}</li><li class="tit">查看</li></ul></el-col>
            <el-col :span="1"><ul style="color:#999999;text-align:center;"><li style="cursor: pointer;"><img @click="del(c.id)" :src="require('@/assets/student/BIGMycourse_delete.png')"></li></ul></el-col>
          </el-row>
          </div>
           <page :page="form.page" @changed="getList"></page></div>
    </div>
</template>

<script>
import { getStudentPage, addClick, del } from '@/api/community/theme'
import page from '@/components/page'
export default {
  components: { page },
  data() {
    return {
      message: '',
      defaultImg: 'this.src="' + require('@/assets/images/default.jpeg') + '"', // 默认头像图像
      // 分页和模糊查询
      form: {
        form: {
        },
        page: {
          current: 1,
          pageCount: 5,
          total: 0
        },
        search: ''
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
    // 获取帖子数据
    getList() {
      this.loading = true
      const params = this.form
      getStudentPage(params).then(e => {
        const data = e.data.data
        this.message = data.records
        this.form.page.total = data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    del(id) {
      this.delete([id], '确定删除该帖子吗？')
    },
    delete(ids, title) {
      this.$cfm(title, e => {
        del(ids).then(e => {
          if (e.data.success) {
            this.$sucmsg('删除成功')
            this.getList()
          } else {
            this.$errmsg('删除失败')
          }
        })
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
