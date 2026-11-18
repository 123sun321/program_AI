<template>
    <div id="app">
       <div class="test_empty" v-if="this.comment.length===0">暂无评价</div>
       <div v-else class="content">

           <div  class="detail" v-for="(item, index) in comment" :key="index">
              <div class="nickname-wrap">
                  <span class="name">{{item.userName}}<a v-if="item.reply"><span style="color:#000;">&nbsp;回复&nbsp;</span>{{item.replyUserName}}</a></span>
                  <span class="comment-text">{{item.content}}</span>
              </div>
              <div class="comment-content">
                 <span class="date">{{item.creationTime}}</span>
              </div>
           </div>
           <page :page="form.page" @changed="bindData" style="margin-top:20px;"></page>
       </div>

    </div>
</template>

<script>
/**
 * @file 评论课程组件
 * @author 张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/21
 */
import { coursePage } from '@/api/coursemanage/course'
import page from '@/components/page'
export default {
  props: ['courseid'],
  components: {
    page
  },
  data() {
    return {
      value: '',
      texname: '',
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        search: undefined,
        orderBy: 'creation_time desc'
      },
      comment: []// 评论
    }
  },
  created() {
    this.$nextTick(e => {
      this.bindData()
    })
  },
  methods: {
    // 数据绑定
    bindData() {
      var vm = this
      this.loading = true
      this.form.form = { 'link_id': this.courseid }
      coursePage(this.form).then(response => {
        vm.loading = false
        const data = response.data.data
        // debugger
        console.log('评论：')
        console.log(data)
        // debugger
        vm.comment = data.records
        this.form.page.total = parseInt(data.total)
        this.loading = false
        this.page = data.current
      }).catch(() => {
        this.loading = false
      })
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
    border-bottom: 2px solid #e3e3e3;
    font-size: 14px;
    padding-bottom: 10px;
   }
.comment .text{
    font-size: 14px;
    position: relative;
    top:-23px;
    left:35px;
    margin-left:8px;
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
.nickname-wrap{
    display: inline-block;
    vertical-align: middle;
    position: relative;
    top:-20px;
    margin-left: 10px;
    padding-top: 10px;
}
.nickname-wrap .name{
    font-size:14px;
    color:#999999;
    display: block;
}
.nickname-wrap .date{
    font-size:12px;
    color:#999999;
    margin-top:2px;
}
.comment-content{
    color:#999999;
    margin-left: 10px;
    font-size: 13px;
}
.comment-text{
    display: block;
    color:#2b2b2b;
    margin-top: 10px;
    font-size: 15px;
}
.test_empty{
   background-color: #eee;
   height:45px;
   line-height: 45px;
   font-size:14px;
   padding: 0 15px;
   text-align: center;
 }
</style>

