<template>
     <div class="report-card" style=" margin-top: 20px;">
        <div class="title">{{form.page.total}} 条评论</div>
          <div v-if="comment.length===0"><p>还没有人回复，沙发空缺中~</p></div>
        <div v-else>
        <div class="comment-item" v-for="item in comment" :key="item.id">
            <el-row>
                <el-col :span="2">
                    <div class="user-logo" @click="touser(item.userId)"><img :src="item.user.userPic" :onerror="defaultImg"></div>
                </el-col>
                <el-col :span="22">
                    <div class="user-info"><a class="username" @click="touser(item.userId)">{{item.user.name}}</a><span v-if="item.reply"> 回复 <a class="replyname" @click="touser(item.replyUserId)">{{item.replyUserName}}</a></span></div>
                    <div class="comment-content">
                        {{item.content}}
                    </div>
                    <div class="comment-operation">
                        <el-row>
                            <el-col :span="12">
                                <div class="btn">
                                    <el-button type="success" size="mini" @click="recommend(item)" :disabled="item.likeId!==null" round><svg-icon icon-class="recommend" /> {{item.likeNumber}}</el-button>
                                    <a @click="reply(item)">回复</a>
                                     <a  v-if="userId===item.userId" @click="del(item.id,index)">删除</a>
                                </div>
                            </el-col>
                            <el-col :span="12">
                                <div class="time">{{item.creationTime | formatTime('{y}-{m}-{d} {h}:{i}')}}</div>
                            </el-col>
                        </el-row>
                    </div>
                </el-col>
            </el-row>
         </div>
        </div>
        <page v-if="form.page.total>10" :page="form.page" @changed="initComment" style="padding:15px 0;"></page>
        <div class="title">你的回复</div>
        <div class="comment-form">
            <el-input ref="commentcontent" type="textarea" :rows="5" v-model="submitComment.content" :placeholder="submitComment.reply ? '回复 ' + submitComment.replyUserName : '请输入评价内容'"></el-input>
            <div class="submit-btn">
                <el-row>
                <el-col :span="12" class="user-info">
                    <img :src="avatar || require('@/assets/images/team-logo.png')" >
                    <span>{{name}}</span>
                </el-col>
                <el-col :span="12" class="submit">
                    <el-button type="success" @click="saveComment" icon="el-icon-edit" size="medium"> 提交</el-button>
                </el-col>
                </el-row>
            </div>
        </div>
    </div>
</template>

<script>
import { dele } from '@/api/community/theme'
import page from '@/components/page'
import { reportPage, add, recommend } from '@/api/comment'
import { mapGetters } from 'vuex'
export default {
  props: ['reportid'],
  computed: {
    ...mapGetters([
      'name',
      'avatar',
      'userId'
    ])
  },
  components: {
    page // 声明组件
  },
  data() {
    return {
      defaultImg: 'this.src="' + require('@/assets/images/default.jpeg') + '"', // 默认头像图像
      replyText: undefined,
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        search: undefined,
        orderBy: 'a.creation_time desc'
      },
      comment: [],
      submitComment: {
        reply: false,
        linkId: undefined,
        category: 4
      }
    }
  },
  created() {
    this.$nextTick(e => {
      this.initComment()
    })
  },
  watch: {
    reportid(newValue, oldValue) {
      if (newValue) {
        this.initComment()
      }
    }
  },
  methods: {
    // 初始化评价
    initComment() {
      this.submitComment.reply = false
      const parame = this.form
      parame.form = { 'linkId': this.reportId }
      reportPage(parame).then(e => {
        const data = e.data.data
        this.comment = data.records
        this.form.page.total = data.total
      })
    },
    // 回复
    reply(item) {
      this.initSubmitComment(true, item.userId, item.user.name, undefined)
      this.$refs.commentcontent.focus()
    },
    // 评论点赞
    recommend(item) {
      recommend(item.id).then(e => {
        if (e.data.success) {
          item.likeId = 1
          item.likeNumber++
        }
      })
    },
    // 发布评价
    saveComment() {
      this.submitComment.linkId = this.reportid
      add(this.submitComment).then(e => {
        if (e.data.success) {
          this.$sucmsg('发布评价')
          this.initSubmitComment(false, undefined, undefined, undefined)
          this.initComment()
          this.$emit('success')
        }
      })
    },
    // 删除评论
    del(id, index) {
      this.$cfm('确定删除吗？', e => {
        dele(id).then(e => {
          if (e) {
            this.$sucmsg('删除成功!')
            this.comment.splice(index, 1)
            this.initComment()
            this.$emit('success')
          } else {
            this.$errmsg('删除失败!')
            this.initComment()
            this.$emit('success')
          }
        })
      })
    },
    // 评论内容初始化
    initSubmitComment(reply, replyUserId, replyUserName, content) {
      this.submitComment.reply = reply
      this.submitComment.replyUserId = replyUserId
      this.submitComment.replyUserName = replyUserName
      this.submitComment.content = content
    },
    touser(id) {
      this.$router.push({ path: '/user/info/' + id })
    }
  }
}
</script>

<style lang="scss" scoped>
.comment-form /deep/ .el-textarea__inner:focus{
    border: 1px solid #1fad4e;
    border-radius: 4px;
}
.report-card{
    background-color: #ffffff;
    border-radius: 5px;
    padding: 10px 20px 20px 20px;
    .title{
        line-height: 50px;
        font-size: 22px;
        color: #666666;
        border-bottom:1px solid #eeeeee;
    }
    .comment-item{
        margin-top: 15px;
        border-bottom: 1px solid #eeeeee;
        .user-logo{
          cursor: pointer;
          text-align: center;
          img{
              border-radius: 50%;
              width: 70px;
              height: 70px;
          }
        }
        .user-info{
            line-height: 40px;    
            font-size: 18px;
            color: #666666;
            .username{
                color: #eca875;
            }
            .replyname{
                color: #eca875;
            }
        }
        .comment-content{
            font-size: 16px;
            color: #333333;
            line-height: 25px;
        }
        .comment-operation{
            margin: 10px 0;
            .btn{
                a{ color: #666666;margin-left: 10px;}
            }
            .time{
                text-align: right;
                color: #666666;
            }
        }
    }
    .correlation-report{
        padding: 10px 0;
        font-size: 14px;
        .report-item{
            padding: 12px;
            color: #4c5157;
        }
        .report-item:hover{
            cursor: pointer;
            color: #4c5157;
            background-color: #f9f9f9;
        }
    }
    .comment-form{
        margin: 15px 0;
        .submit-btn{
            padding-top: 15px;
            .user-info{
                img{
                    width: 35px;height: 35px;border-radius: 50%;float: left;
                }
                span{line-height: 35px;margin-left: 10px;color: #333;}
            }
            .submit{
                text-align: right;
            }
        }
    }
}
</style>
