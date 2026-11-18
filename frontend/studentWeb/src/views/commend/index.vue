<template>
  <div class="detail2">
    <!-- 头部 -->
    <div class="header">
      <div style="margin: 0 auto;width:40%;">
        <el-input placeholder="输入关键字搜索作品" round size="large" v-model.trim="input4">
          <template slot="append">
            <img :src="require('@/assets/student/Worksdisplay_search.png')" @click="onSearch()">
          </template>
        </el-input>
      </div>
      <!-- <div class="c_btns">
      <el-button type="info" v-for="(item,index) in labels" :key="index"  v-bind:class="{ active:index === activeIndex}" @click="onclick(index)"  plain>{{item.name}}</el-button>
      </div>-->
    </div>
    <!-- 内容 -->
    <div v-loading="loading">
      <div
        style="text-align:center;margin-bottom:12px;"
        class="noneData"
        v-if="details.length===0"
      >暂无数据</div>
      <div class="detail-main" v-else>
        <el-row :span="24" :gutter="60">
          <div v-for="(c,index) in details" :key="index">
            <el-col :span="6" style="position: relative;">
              <div class="course-box">
                <ul>
                  <li @click="edit(c)">
                    <div>
                      <img :src="require('@/assets/student/Worksdisplay_edit.png')">
                    </div>
                  </li>
                  <li @click="look(c)">
                    <div>
                      <img :src="require('@/assets/student/Worksdisplay_search.png')">
                    </div>
                  </li>
                  <li v-if="c.flag" @click="approve()">
                    <div style="background-color:#999">
                      <img :src="require('@/assets/student/Worksdisplay_fabulous.png')">
                    </div>
                  </li>
                  <li v-if="c.flag === false" @click="like(c.id)">
                    <div>
                      <img :src="require('@/assets/student/Worksdisplay_fabulous.png')">
                    </div>
                  </li>
                </ul>
              </div>
              <div class="box">
                <div class="course-img">
                  <iframe
                    class="imgs"
                    :src="'/fstatic/codeeditor/index.html?appid='+appid+'#/show?cid='+c.codeId"
                    allowfullscreen="allowfullscreen"
                    frameborder="0"
                    scrolling="no">
                    </iframe>
                </div>
                <div class="detail">
                  <div>
                    <img :src="c.user.userPic" :onerror="defaultImg">
                  </div>
                  <div></div>
                  <div class="name">
                    <div class="content">
                      <div class="tit">{{c.student.name}}</div>
                      <div class="date" v-if="c.flag">
                        <img :src="require('@/assets/student/Worksdisplay_fabulousss.png')">
                        <span style="margin-top:4px;">{{c.approveCount}}</span>
                      </div>
                      <div class="date" v-else>
                        <img :src="require('@/assets/student/Worksdisplay_fabulouss.png')">
                        <span style="margin-top:4px;color:#1fad4e;">{{c.approveCount}}</span>
                      </div>
                    </div>
                    <div class="content">
                      <div style="margin-top:15px;" class="comment-text">{{c.title}}</div>
                      <div style="margin-top:15px;"
                        class="date">{{c.creationTime| formatTime('{y}-{m}-{d}')}}</div>
                    </div>
                  </div>
                </div>
              </div>
            </el-col>
          </div>
        </el-row>
        <page style="padding:30px 0;" :page="form.page" @changed="getList"></page>
      </div>
    </div>
    <el-dialog
      :title="title"
      :visible.sync="visible"
      style="margin-top: -10vh;"
      width="100%"
      height="100%">
      <iframe
        class="img2"
        :src="'/fstatic/codeeditor/index.html?appid='+appid+'#/show?cid='+currentCodeId"
        allowfullscreen="allowfullscreen"
        frameborder="0"
        scrolling="no"
      ></iframe>
    </el-dialog>
  </div>
</template>
<script>
/**
 * @file 作品展览组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/02
 */
import { getQueryParams } from '@/utils/urlUtil'
import { recommendWorkPage } from '@/api/recommendWork'
import { addApprove } from '@/api/approve'
import Vue from 'vue'
import page from '@/components/page'
export default {
  components: {
    page
  },
  data() {
    return {
      form: {
        page: {
          current: 1,
          pageCount: 9,
          total: 0
        },
        search: '',
        orderBy: 'creation_time desc'
      },
      currentCodeId: undefined,
      baseHost: process.env.BASE_API,
      searParams: {},
      defaultImg: 'this.src="' + require('@/assets/images/default.jpeg') + '"', // 默认头像图像
      input4: '', // 输入内容
      loading: false,
      visible: false,
      approved: true,
      title: '', // 弹框名字
      imgs: '', // 弹框图片
      details: '', // 课程
      activeIndex: '',
      labels: [
        {
          name: '最新作品'
        },
        { name: '学校推荐' },
        { name: '教师推荐' }
      ]
    }
  },
  computed: {
    appid: function() {
      return getQueryParams().appid
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
      this.searParams.page = this.form.page
      recommendWorkPage(this.searParams).then(e => {
        this.details = e.data.data.records
        this.form.page.total = e.data.data.total
        for (var i = 0; i < this.details.length; i++) {
          Vue.set(this.details[i], 'checked', false)
        }
        this.loading = false
      })
    },
    // 搜索
    onSearch() {
      if (this.input4 !== '') {
        this.searParams.form = {
          title: this.input4
        }
        this.searParams.op = {
          title: 'like'
        }
        this.getList()
        this.searParams.page = this.form.page
      } else {
        this.searParams.form = {
          title: ''
        }
        this.searParams.op = {
          title: 'like'
        }
        this.getList()
        this.searParams.page = this.form.page
      }
    },
    // 选中标签
    onclick(index) {
      this.activeIndex = index
      this.getList()
    },
    // 修改
    edit(c) {
      this.$router.push({
        path: 'commendrun',
        query: { id: c.id }
      })
    },
    // 查看
    look(c) {
      this.currentCodeId = c.codeId
      this.title = c.title
      this.visible = true
    },
    // 点赞
    like(id) {
      addApprove(id).then(e => {
        if (e.data.success) {
          this.$sucmsg('点赞作品！')
          this.getList()
        } else {
          this.$errmsg('点赞失败！')
        }
      })
    },
    approve() {
      this.$errmsg('不可重复点赞！')
    }
  }
}
</script>
<style scoped lang="scss">
/deep/ .el-dialog {
  height: 100%;
  background-color: #000;
}
/deep/ .el-dialog__wrapper {
  position: fixed;
  height: 105%;
  overflow: hidden;
}
/deep/ .el-dialog__body {
  padding: 0px;
  line-height: 0px;
  display: flex;
  flex-wrap: column;
  align-items: center;
  height: 100%;
  .img2 {
    vertical-align: center;
    flex: 1;
    width: 100%;
    height: 100%;
  }
}

/deep/ .el-dialog__title {
  color: #fff;
}
.active {
  color: #1fad4e !important;
  border-color: #e8f7ed !important;
  background: #e8f7ed !important;
}
.detail2 {
  margin: 10px;
  background-color: #fff;
  .c_btns {
    text-align: center;
    margin: 0 auto;
    .el-button {
      margin: 20px 2.4% 0px 2.4%;
      color: #9e9d9d;
      font-size: 14px;
      border-color: #9e9d9d;
      border-radius: 10px;
      background: #fff;
    }
  }
  .header {
    text-align: center;
    padding-top: 30px;
    /deep/ .el-input__inner {
      font-size: 16px;
      color: #9e9d9d;
      border-radius: 35px 0px 0px 35px;
      height: 55px;
    }
    /deep/ .el-input__inner:focus {
      border: 1px solid #1fad4e !important;
    }
    /deep/ .el-input-group__append {
      border-radius: 0px 35px 35px 0px;
      background-color: #1fad4e;
      border: none;
      cursor: pointer;
      width: 15%;
      img {
        padding: 13px 30px;
      }
    }
  }
  .detail {
    display: flex;
    flex-flow: row;
    padding-left: 5px;
    padding-right: 5px;
    padding-top: 10px;
    padding-bottom: 10px;
    .name {
      flex: 1;
      overflow: hidden;
      font-size: 18px;
      color: #333;
      margin-left: 10px;
      margin-top: 5px;
      margin-bottom: 12px;
      .content {
        display: flex;
        flex-flow: row;
        .tit {
          flex: 1;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        .comment-text {
          color: #666;
          flex: 1;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          font-size: 15px;
        }
      }
    }
    .date {
      display: flex;
      align-items: center;
      font-size: 14px;
      color: #999999;
      img {
        width: 24px;
        height: 24px;
      }
    }
  }
  .detail img {
    width: 60px;
    height: 60px;
    border-radius: 50%;
  }
  .box {
    border: 1px solid #e3e2e2;
    border-radius: 6px;
    height: auto;
    overflow: hidden;
    position: relative;
  }
  .course-img {
    width: 100%;
  }
  .imgs {
    width: 100%;
    height: 250px;
  }
  .detail-main {
    width: 94%;
    margin: 0 auto;
    text-align: left;
  }
  .course-box {
    opacity: 0;
    position: absolute;
    box-sizing: border-box;
    overflow: hidden;
    //  width: 100%;
    width: 86.8%;
     left: 0;
      right: 0;
      margin: auto;
    background: rgba(255, 255, 255, 0.7);
    height: 100%;
    z-index: 100;
    text-align: center;
    ul {
      margin-top: 30%;
      display: inline-block;
      height: 60px;
    }
    li {
      float: left;
      cursor: pointer;
      font-size: 36px;
      margin: 0px 10px;
      color: #fff;
      div {
        height: 60px;
        width: 60px;
        background-color: #1fad4e;
        border-radius: 50%;
        img {
          margin-top: 28%;
        }
      }
    }
  }
  .add {
    font-size: 48pt;
    color: #999;
    text-align: center;
    padding: 30% 0;
    cursor: pointer;
    overflow: hidden;
    font-weight: 900;
  }
  .el-col .course-box:hover,
  .el-col .course-box:active {
    opacity: 1;
    cursor: pointer;
  }
  .pull-left {
    float: left !important;
  }
  .el-col {
    margin-top: 40px;
    width: 25%;
    height: auto;
    position: relative;
  }
}
.noneData {
  height: 490px;
  text-align: center;
  width: 100%;
  padding-top: 260px;
  background-repeat: no-repeat;
  background-image: url("~@/assets/images/c6.jpg");
  background-position: 50% 30%;
}
</style>
