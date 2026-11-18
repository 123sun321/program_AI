<template>
    <div class="reportdetail">
      <el-row :gutter="20">
        <el-col :span="18">
            <div class="report-info">
                <div class="top-info">
                    <el-row>
                        <el-col :span="18" class="top-name">
                              <div class="pic" @click="callback"><img  :src="require('@/assets/student/Experiment_return.png')" ></div> <div class="title">{{detail.title}}</div>
                        </el-col>
                        <el-col :span="6">
                            <div class="right-info">
                                <span class="click">{{detail.click}} 查看</span>
                                <span class="comment">{{detail.commentNumber}} 评论</span>
                            </div>
                        </el-col>
                    </el-row>
                </div>
                <div class="user-info clearfix">
                    <div class="user-logo pull-left">
                        <a  @click="touser(detail.userId)"><img :src="detail.user.userPic"  :onerror="defaultImg"></a>
                    </div>
                    <a class="user-name pull-left" @click="touser(detail.userId)">{{detail.user.name}}</a>
                    <div class="time pull-left">{{detail.creationTime| formatTime('{y}-{m}-{d} {h}:{i}')}}</div>
                </div>
                <div class="report-body">
                   <span class="content" v-html="detail.content"></span>
                </div>
            </div>
           <topic-comment  :communityId="communityId" :init="init"></topic-comment>
        </el-col>
        <el-col :span="6">
            <topic-right :list="list" v-on:headCallBack="headCall"></topic-right>
        </el-col>
       </el-row>
    </div>
</template>

<script>
/**
 * @file  发帖组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/17
 */
import Vue from 'vue'
import { getThemeById } from '@/api/community/theme'
import topicComment from './components/topicComment'
import { findPage } from '@/api/community/theme'
import topicRight from './components/topicRight'
import { mapGetters } from 'vuex'
export default {
  components: {
    topicComment, topicRight// 声明组件
  },
  data() {
    return {
      message: '', // 帖子
      list: [], // 相关帖子
      defaultImg: 'this.src="' + require('@/assets/images/default.jpeg') + '"', // 默认头像图像
      correlationreports: [],
      editReport: false,
      content: undefined,
      // 分页和模糊查询
      form: {
        form: {
        },
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        plateName: '',
        search: ''
      },
      communityId: this.$route.query.communityid,
      detail: []
    }
  },
  computed: {
    ...mapGetters([
      'userId'
    ]),
    isShow() {
      return this.communityId
    }
  },
  created() {
    this.init()
  },
  methods: {
    // 获取帖子数据
    init() {
      getThemeById(this.communityId).then(e => {
        this.detail = e.data.data
        this.getList()
      })
    },
    touser(id) {
      this.$router.push({ path: '/user/info/' + id })
    },
    callback() {
      this.$router.go(-1)
    },
    // 获取帖子数据
    getList() {
      this.loading = true
      Vue.set(this.form.form, 'plate', this.detail.plate)
      const params = this.form
      findPage(params).then(e => {
        const data = e.data.data
        this.message = data.records
        const len = this.message.filter(b => b.id !== this.communityId)
        this.list = len.slice(0, 10)
        this.form.page.total = data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    headCall(id) { // 回调方法，接收子组件传的参数
      this.communityId = id
      this.$router.push({ name: 'topic', query: { communityid: id }})
      this.init()
    }

  }
}
</script>

<style lang="scss" scoped>
.top-name{
  display: flex;
flex-wrap: row;
}
.pic{
      cursor: pointer;
      padding: 0px 5px;
      margin-top: 3px;
      margin-left: -15px;
      // margin-top: -13px;
    }
.reportdetail{
    margin: 15px;
    .report-info{
        background-color: #ffffff;
        border-radius: 5px;
        padding: 20px;
        .top-info{
            .title{font-size: 24px;color: #07111b;}
            .right-info{
                text-align: right;
                .click{
                    color: #666666;
                }
                .comment{
                    margin-left: 15px;
                    color: #1fad4e;
                }
            }
        }
        .user-info{
            margin: 30px 0;
            .user-logo{
                img{
                    width: 50px;height: 50px;border-radius: 50%;
                }
            }
            .user-name{
                line-height: 50px;
                margin-left: 15px;
                font-size: 20px;
                color: #eca875;
            }
            .time{
                line-height: 50px;
                font-size: 16px;
                color: #999999;
                margin-left: 20px;
            }
        }
        .report-body{
            padding: 10px 15px;
            .report-ctrl{
                text-align: right;
                padding-bottom: 5px;
                border-bottom: 1px solid #efefef;
                min-height: 40px;
                overflow: hidden;
                .el-button{
                    font-size: 15px;
                }
            }
            .report-detail{
                margin: 30px 20px;
            }
            .report-edit{
                margin-top: 15px;
                .item{margin-top: 10px;}
                .btn{
                    padding: 15px 0;
                }
            }
        }
        .report-show{
            padding: 10px 15px;
        }
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
    }
}
.content{
  word-wrap : break-word ;
}
</style>
