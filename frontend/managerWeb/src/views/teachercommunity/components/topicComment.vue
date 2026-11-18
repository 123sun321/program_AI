<template>
     <div class="report-card" style=" margin-top: 20px;">
        <div class="title">{{form.page.total}} 条回答</div>
        <div v-if="comment.length===0"><p>还没有人回复，沙发空缺中~</p></div>
        <div v-else>
        <div  class="comment-item" v-for="item in comment" :key="item.id">
            <el-row>
                <el-col :span="2">
                    <div class="user-logo"  @click="touser(item.user.id)"><img :src="item.user.userPic" :onerror="defaultImg"></div>
                </el-col>
                <el-col :span="22">
                    <div class="user-info"><a class="username"  @click="touser(item.user.id)">{{item.user.name}}</a><span v-if="item.reply"> 回复 <a class="replyname"  @click="touser(item.replyUserId)">{{item.replyUserName}}</a></span></div>
                    <div class="comment-content">
                        {{item.content}}
                    </div>
                    <div class="comment-operation">
                        <el-row>
                            <el-col :span="12">
                                <div class="btn">
                                    <el-button type="success" size="mini" @click="recommend(item)" :disabled="item.isAppreciate" round><svg-icon icon-class="recommend" /> {{item.likeNumber}}</el-button>
                                    <a @click="reply(item)">回复</a>
                                     <a  v-if="userId===item.user.id" @click="del(item.id,index)">删除</a>
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
        <page v-if="form.page.total>10" :page="form.page" @changed="bindData" style="padding:15px 0;"></page>
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
                    <el-button type="success" @click="saveComment" size="medium">发表</el-button>
                </el-col>
                </el-row>
            </div>
        </div>
    </div>
</template>

<script>
import { fetchPage, dele } from '@/api/community/theme'
import page from '@/components/page'
import { add, recommend } from '@/api/comment'
import { mapGetters } from 'vuex'
export default {
  props: ['communityId'],
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
        orderBy: 'creation_time desc',
        form: {
          link_id: this.communityId
        }
      },
      comment: [],
      submitComment: {
        reply: false,
        linkId: undefined,
        category: 1
      }

    }
  },
  created() {
    this.$nextTick(e => {
      this.bindData()
    })
  },
  watch: {
    communityId(newValue, oldValue) {
      if (newValue) {
        this.bindData()
      }
    }
  },
  methods: {
    // 数据绑定
    bindData() {
      var vm = this
      this.loading = true
      this.form.form.link_id = this.communityId
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
    touser(id) {
      this.$router.push({ path: '/user/info/' + id })
    },

    // 删除评论
    del(id, index) {
      this.$cfm('确定删除吗？', e => {
        dele(id).then(e => {
          if (e) {
            this.$sucmsg('删除成功!')
            this.comment.splice(index, 1)
            this.bindData()
            this.$emit('success')
          } else {
            this.$errmsg('删除失败!')
            this.bindData()
            this.$emit('success')
          }
        })
      })
    },

    reply(item) {
      this.initSubmitComment(true, item.user.id, item.user.name)
      this.$refs.commentcontent.focus()
    },
    // 评论点赞
    recommend(item) {
      recommend(item.id).then(e => {
        if (e.data.success) {
          item.likeId = 1
          item.likeNumber++
          this.bindData()
        }
      })
    },
    // 发布评价
    saveComment() {
      this.submitComment.linkId = this.communityId
      add(this.submitComment).then(e => {
        if (e.data.success) {
          this.$sucmsg('发布评价')
          this.submitComment.content = ''
          this.initSubmitComment(false, undefined, undefined)
          this.$emit('success')
          this.bindData()
        }
      })
    },
    // 评论内容初始化
    initSubmitComment(reply, replyUserId, replyUserName, content) {
      this.submitComment.reply = reply
      this.submitComment.replyUserId = replyUserId
      this.submitComment.replyUserName = replyUserName
      // this.submitComment.content = content
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
    .comment-item /deep/ .el-col-2{
    min-width:120px;
 }
 .comment-item /deep/ .el-col-22{
    width: calc(100% - 120px);
 }
    .comment-item{
        margin-top: 15px;
        border-bottom: 1px solid #eeeeee;
        .user-logo{
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
