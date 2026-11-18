<template>
    <div class="app" v-loading="loading">
       <div class="content">
          <div class="navtit">评价</div>
            <div class="comments">
          <el-input  ref="input" type="textarea"  v-model.trim="value" :rows="4"  :placeholder="tex"></el-input>
              <div class="operation-button">
          <el-button class="c_btn"  @click="cancel">取消</el-button>
          <el-button type="success" @click="onPublish(value)">发表</el-button>
          </div>
          <div style="clear:both"></div>
          </div>
           <div class="grid">
           <span class="tit">最新评论</span>  </div>
           <div class="detail" v-for="(item, index) in comment" :key="index">
             <el-row  >
               <el-col :span="4">
               <img v-if="item.user" :src="item.user.userPic" :onerror="defaultImg">
              </el-col>
              <el-col :span="20">
                <div class="nickname-wrap">
                    <span class="name">{{item.userName}}<a v-if="item.replyUserName"><span style="color:#999;font-size:13px;">&nbsp;回复&nbsp;</span>{{item.replyUserName}}</a></span>
                    <div class="comment-text">{{item.content}}</div>
                </div>
              <div class="comment-content">
                 <span class="date">{{item.creationTime| formatTime('{y}-{m}-{d} {h}:{i}')}}</span>
                  <span class="replyAll" >
                   <span  @click="ClickReply(item)"><i class="el-icon-edit"></i>回复</span>&nbsp;
                   <span v-if="uId===item.userId" @click="del(item.id,index)">删除</span>
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
 * @file 课程详情评论
 * @author 宋乃乐
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/23
 */
import { dele, addcomment } from '@/api/studypath/pathmanage'
import { getCourseComment } from '../../api/course/courseDetail/index'
import store from '@/store'
import page from '@/components/page'
export default {
  components: {
    page
  },
  props: ['courseId'],
  data() {
    return {
      loading: false,
      defaultImg: 'this.src="' + require('../../assets/images/default.jpeg') + '"', // 默认头像图像
      form: {
        form: {
          linkId: this.courseId
        },
        page: {
          current: 1,
          pageCount: 5,
          total: 0
        },
        search: undefined,
        orderBy: 'creation_time desc'
      },
      uId: '', // 当前用户id
      tex: '输入你想说的话...', // 输入框文字
      value: '', // 输入的值
      texname: '', // 回复对象
      Id: '', // 回复id
      params: '',
      comment: []// 评论
    }
  },
  created() {
    this.bindData()
    this.uId = store.getters.userId
  },
  methods: {
    // 数据绑定
    bindData() {
      var vm = this
      this.loading = true
      getCourseComment(this.form).then(response => {
        const data = response.data.data
        vm.comment = data.records
        this.form.page.total = parseInt(data.total)
        this.page = data.current
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 回复评论
    ClickReply(item) {
      this.$refs.input.focus()
      this.tex = '回复' + item.userName + ':'
      this.texname = item.userName
      this.Id = item.userId
    },
    // 取消回复评论
    cancel() {
      this.params = ''
      this.value = ''
      this.itemname = ''
      this.tex = '输入你想说的话...'
    },
    onPublish(value) {
      if (value.length > 140) {
        this.$warnmsg('评论字符过长,请不要超过140个字符')
        return
      }
      if (this.Id) {
        const courseId = this.courseId
        this.params = {
          category: 2,
          content: value,
          linkId: courseId,
          reply: true,
          userName: this.texname,
          userId: this.Id

        }
        addcomment(this.params).then(e => {
          if (e.data.success) {
            this.$sucmsg('回复评论成功')
            this.bindData()
          } else {
            this.$errmsg('回复评论失败')
          }
        })
      } else {
        const courseId = this.courseId
        this.params = {
          category: 2,
          content: value,
          reply: false,
          linkId: courseId
        }
        addcomment(this.params).then(e => {
          if (e.data.success) {
            this.$sucmsg('发表评论成功')
            this.bindData()
          } else {
            this.$errmsg('发表评论失败')
          }
        })
      }
      this.cancel()
    },
    // 回复评论
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
    }
  }
}
</script>
<style scoped  lang="scss">
 .c_btn{
        color: #1fad4e;
        border-color:#e8f7ed;
        background:#e8f7ed;
        border-radius: 4px;
 }
    .c_btn:hover{border-color:#1fad4e; }

 .comments /deep/ .el-textarea__inner:focus{
    border: 1px solid #1fad4e;
    border-radius: 4px;
}
.detail /deep/ .el-col-4{
    min-width:80px;
 }
.detail /deep/ .el-col-20{
     width: calc(100% - 80px);
 }
.top{
    width:100%;
    height:250px;
}
.comment{
    margin:15px;
}
.operation-button{
    float: right;
    margin-top: 15px;
  }
  .grid{
    border-bottom: 1px solid #e3e3e3;
    font-size: 14px;
    padding-bottom: 10px;
    color: #999;
    /* position: relative; */
   }
   .navtit{
     color: #333;
     margin-bottom: 5px;
     font-size: 16px;
   }

 .text{
    font-size: 14px;
    position: relative;
    top:-23px;
    left:35px;
    margin-left:8px;
}
.content{
    margin:0px;
}
.detail{
    padding-left: 5px;
    padding-right: 5px;
    padding-top: 20px;
    padding-bottom: 10px;
    border-bottom: 1px solid #e3e3e3;
    // overflow: hidden;
}
.detail img{
    width:60px;
    height:60px;
    border-radius:50%
}
.nickname-wrap{
    vertical-align: middle;
}
.nickname-wrap .name{
    font-size:16px;
    color:#eca857;
    // display: block;
}
.comment-content .date{
    font-size:14px;
    color:#999999;
}
.replyAll{
    float: right;
    cursor: pointer;
    position: relative;
    font-size: 13px;
    line-height: 14px;
    color: #999;
}
.comment-content{
    // margin-left: 70px;
    font-size: 13px;
      margin-top: 8px;
}
.comment-text{
    word-wrap: break-word;
     width: 100%;
    color:#666;
    margin-top: 10px;
    line-height: 24px;
    font-size: 16px;
}
</style>

