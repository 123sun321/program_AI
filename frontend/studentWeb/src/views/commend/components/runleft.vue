<template>
  <div class="runleft">
    <!-- 左侧文档 -->
    <div class="left">
      <div class="step header" id="border">
        <span>作品作者</span>
      </div>
      <div class="customscroll" style="font-size:14px;color:#666;">
        <div class="course-body">
          <div class="detail">
            <el-row>
              <el-col :span="4">
                <img :src="entity.user.userPic" :onerror="defaultImg">
              </el-col>
              <el-col :span="20">
                <div class="nickname-wrap">
                  <div class="name">{{entity.student.name}}</div>
                  <div class="comment-text">{{entity.student.code}}</div>
                </div>
                <user-star :imgsize="16" :userid="entity.user.id"></user-star>
              </el-col>
            </el-row>
          </div>
        </div>
        <div class="step header">
          <span>作品信息</span>
        </div>
        <div style="padding:0 5px;" class="comment-text">{{entity.title}}</div>
      </div>
    </div>
  </div>
</template>
<script>
/**
 * @file 作品介绍组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/11
 */
import userStar from '@/views/user/userStar'
import page from '@/components/page'
import markDown from '@/components/markDown'
export default {
  components: {
    page,
    markDown,
    userStar
  },
  props: ['entity'],
  data() {
    return {
      defaultImg:
        'this.src="' + require('../../../assets/images/default.jpeg') + '"', // 默认头像图像
      look: false,
      like: false,
      flag: 0
    }
  },
  created() {},
  methods: {
    // 关注
    Look() {
      this.look = true
    },
    // 收藏
    Like() {
      this.like = true
    }
  }
}
</script>
<style scoped lang="scss">
.runleft {
  height: 100%;
}
.stepbox {
  height: 100%;
}
.buttons {
  border: 1px solid #2d2e3f;
  border-radius: 4px;
  background-color: #fff;
}
.buttons:hover {
  border: 1px solid #2d2e3f;
  background-color: #2d2e3f;
  color: #fff;
}
.detail /deep/ .el-col-4 {
  min-width: 100px;
}
.detail /deep/ .el-col-20 {
  width: calc(100% - 100px);
}
.button {
  border: 1px solid #2d2e3f;
  background-color: #2d2e3f;
  color: #fff;
}
.list {
  font-size: 14px;
  color: #666;
  line-height: 20px;
  height: calc(100% - 110px);
  overflow-y: auto;
}
.course-body {
  position: relative;
  background: #fff;
  height: 80px;
}
.detail img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
}
.nickname-wrap {
  vertical-align: middle;
  margin-top: 20px;
}
.nickname-wrap .name {
  font-size: 18px;
  color: #333;
  display: block;
  margin-bottom: 10px;
}
.date {
  float: right;
  font-size: 14px;
  color: #999999;
}
.c_btn {
  .el-button {
    color: #fff;
    border-color: #65668f;
    border-radius: 4px;
    background: #65668f;
  }
  .el-button:hover {
    border-color: #2d2e3f;
    background: #2d2e3f;
  }
}
.left {
  border: solid 1px #dfdfdf;
  height: 100%;
  padding: 0 10px;
}
.header {
  padding: 15px 5px 10px 5px;
  border-bottom: solid 1px #dfdfdf;
  margin-bottom: 10px;
}
.operation-button {
  float: right;
}
.step {
  position: relative;
  color: #333;
  font-size: 16px;
  text-align: left;
  a {
    position: absolute;
  }
}
</style>

