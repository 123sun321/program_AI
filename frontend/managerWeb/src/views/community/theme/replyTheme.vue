<template>
    <div id="app">
       <div class="content">
          <div class="grid">
            <span class="tit"> 最新评论</span>
            <span > <el-button class="titback" type="button" @click="back()">返回</el-button></span>
          </div>
           <div class="detail" v-for="(item, index) in comment" :key="index">
               <el-row>
               <el-col :span="4">
               <img :src="item.user.userPic" >
                </el-col>
              <el-col :span="20">
                <div class="nickname-wrap">
                    <span class="name">{{item.user.name}}<a v-if="item.replyUserName"> <span style="color:#000;">&nbsp;回复&nbsp;</span>{{item.replyUserName}}</a></span>
                    <span  class="replyAll"><img v-if="item.likeNumber==0" style="" :src="require('@/assets/images/Worksdisplay_fabulousss.png')">
                    <img v-else  :src="require('@/assets/images/Worksdisplay_fabulouss.png')">点赞数&nbsp;{{item.likeNumber}}</span>
                    <div class="comment-text">{{item.content}}</div>
                </div>

              <div class="comment-content">
                 <span class="date">{{item.creationTime}}</span>
                  <span class="replyAll" >

                   <span @click="del(item.id,index)">删除</span>
                 </span>

              </div>
                </el-col>
              </el-row>
           </div>
       </div>
           <page :page="form.page" @changed="bindData" style="margin-top:20px;"></page>
    </div>
</template>

<script>
/**
 * @file 社区帖子标题回复组件
 * @author 张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/1
 */
import { fetchPage, dele } from '@/api/community/theme'
import page from '@/components/page'
export default {
  components: {
  //  add,
    page
  },
  data() {
    return {
      defaultImg: 'this.src="' + require('../../../assets/images/default.jpeg') + '"', // 默认头像图像
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        search: undefined,
        form: {
          link_id: this.$route.query.link_id
        }
      },
      comment: [] // 评论
    }
  },
  created() {
    this.bindData()
  },
  methods: {
    // 数据绑定
    bindData() {
      var vm = this
      this.loading = true
      //   const params = this.$formToParams(this.$refs.form)
      fetchPage(this.form).then(response => {
        vm.loading = false
        const data = response.data
        vm.comment = data.data.records
        this.form.page.total = Number(data.data.total)
        this.loading = false
        this.pages = data.data.records
        this.$emit('headCallBack', this.form.page.total)
      }).catch(() => {
        this.loading = false
      })
    },

    // 删除评论
    del(id, index) {
      this.$cfm('确定删除吗？', e => {
        dele(id).then(e => {
          if (e) {
            this.$sucmsg('删除成功!')
            this.comment.splice(index, 1)
            this.bindData()
          } else {
            this.$errmsg('删除失败!')
            this.bindData()
          }
        })
      })
    },
    back() {
      this.$router.push({ name: 'themeDetail' })
    }
  }
}
</script>
<style scoped>
#app{
    margin-bottom:20px;
}
.top{
    width:100%;
    height:250px;
}
.comment{
    margin:15px;
}
  .grid{
    border-bottom: 5px solid #e3e3e3;
    font-size: 14px;
    padding-bottom: 10px;
   }
.content{
    margin:10px 15px;
}
.detail{
    padding-left: 5px;
    padding-right: 5px;
    padding-top: 20px;
    padding-bottom: 10px;
    border-bottom: 1px solid #e3e3e3;
    overflow: hidden;
}
.detail /deep/ .el-col-4{
    width:80px;
 }
.detail /deep/ .el-col-20{
     width: calc(100% - 80px);
 }
.detail img{
    width:30px;
    height:30px;
    border-radius:50%
}
.nickname-wrap{
    vertical-align: middle;
}
.nickname-wrap .name{
    font-size:15px;
    color:#999999;
     /* display: block;   */
}
.comment-content .date{
    font-size:14px;
    color:#999999;
    margin-top:2px;
}
.replyAll{
    float: right;
    cursor: pointer;
    position: relative;
    margin-top: 5px;
    font-size: 13px;
    line-height: 14px;
    color: #999;
}
.comment-content{
  margin-top: 8px;
    /* margin-left: 65px; */
    font-size: 13px;
}
.comment-text{
    word-wrap: break-word;
    width: 100%;
    color:#2b2b2b;
    line-height: 24px;
    margin-top: 8px;
    font-size: 16px;
}
.titback{
  margin-top: 5px;
  margin-left: 96.5%;

}
.tit{
  /* line-height: 24px; */
  /* top:18px ; */
  position: absolute;
  font-size: 16px;
   padding-top: 8px;
    margin-top: 5px;



}
</style>

