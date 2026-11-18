<template>
    <div class="run-left">
      <vue-markdown v-highlight ref="markdowncontent" :source="content" v-show="false"></vue-markdown>
      <div ref="mySidenav" class="sidenav">
          <a v-for="(c,index) in lists" :key="index" v-bind:class="{active:index===currentIndex}"  @click="closeNav(index)">{{c.title}}</a>
      </div>
      <!-- 左侧文档 -->
      <div ref="myleft" class="left">
      <div class="step header" ref="border">
        <div class="btn-left" @click="lastpage()"><img :src="require('@/assets/student/Experiment_leftarrow.png')" ></div>
        <span> {{currentSetup.title}}</span>
        <div class="btn-right" @click="nextpage()"><img :src="require('@/assets/student/Experiment_rightarrow.png')" ></div>
      </div>
      <div class="list customscroll"><span v-html="currentSetup.content"></span></div>
      </div>
      <!-- 左侧底部 -->
      <div ref="bot" class="step bottom">
        <a style="left:10px;"   @click="openNav()"><img :src="require('@/assets/student/Experiment_more.png')" ></a>
        <a style="right:10px;" class="c_btn"><el-button @click="nextpage()">下一步</el-button></a>
      </div>
    </div>
</template>
<script>
/**
 * @file 实验简介组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/9
 */
// import { get } from '@/api/mycourse/exercise'
import page from '@/components/page'
import VueMarkdown from 'vue-markdown'
export default {
  components: {
    page,
    VueMarkdown
  },
  props: ['content'],
  data() {
    return {
      lists: [],
      currentSetup: {},
      currentIndex: 0,
      flag: 0
    }
  },
  created() {
    this.$nextTick(e => {
      setTimeout(this.inithtml, 1000)
    })
  },
  watch: {
    content(val) {
      setTimeout(this.inithtml, 500)
    }
  },
  methods: {
    inithtml() {
      this.lists = this.$markdownFormat(this.$refs.markdowncontent)
      if (this.lists.length > 0) {
        this.currentIndex = 0
        this.init(0)
      }
    },
    // 下一页
    nextpage() {
      if (this.currentIndex !== this.lists.length - 1) {
        this.currentIndex++
        this.init(this.currentIndex)
      } else {
        this.$warnmsg('已经是最后一步了！')
      }
    },
    // 上一页
    lastpage() {
      if (this.currentIndex !== 0) {
        this.currentIndex--
        this.init(this.currentIndex)
      } else {
        this.$warnmsg('已经是第一步了！')
        this
      }
    },
    // 取数据
    init(i) {
      this.currentIndex = i
      this.currentSetup = this.lists[i]
    },
    // 打开目录
    openNav() {
      if (this.flag === 0) {
        this.$refs.mySidenav.style.left = '0px'
        this.$refs.myleft.style.backgroundColor = '#B2B2B2'
        this.$refs.border.style.borderColor = '#B2B2B2'
        this.$refs.bot.style.background = '#fff'
        this.flag = 1
      } else {
        this.closeNav(null)
      }
    },
    // 关闭目录
    closeNav(i) {
      if (i !== null) {
        this.init(i)
      }
      this.$refs.mySidenav.style.left = '-350px'
      this.$refs.border.style.borderColor = '#dfdfdf'
      this.$refs.myleft.style.background = '#fff'
      this.flag = 0
    }
  }
}
</script>
<style scoped lang="scss">
.run-left {
    height: 100%;
    overflow: hidden;
}
.stepbox {
    height: 100%;
}
  .list{
    padding:8px;
    height: calc(100% - 110px);
    overflow-y:auto;
  }
  .sidenav {
    width: 350px;
    position: fixed;
    margin-top: 100px;
    margin-bottom: 105px;
    z-index: 1001;
    top: 0;
    left: -350px;
    bottom: 0;
    background-color: #fff;
    overflow-x: hidden;
    transition: 0.5s;
    padding-top: 50px;
    text-align:center;
    .active{
      color: #08bf91;
    }
}

.sidenav a {
    padding: 8px 8px 8px 32px;
    text-decoration: none;
    font-size: 25px;
    color: #818181;
    display: block;
    transition: 0.3s;

}

.sidenav a:hover{
    color: #999;
}
.sidenav .closebtn {
    position: absolute;
    top: 0;
    right: 25px;
    font-size: 36px;
    margin-left: 50px;
}
@media screen and (max-height: 450px) {
  .sidenav {padding-top: 15px;}
  .sidenav a {font-size: 18px;}
}
.c_btn{
  .el-button {
      color: #fff;
      border-color:#65668F;
      border-radius: 4px;
      background:#65668F;
    }
  .el-button:hover{border-color:#2D2E3F;background:#2D2E3F; }
  }
.left{
    border: solid 1px #dfdfdf;
    border-top: none;
    height: 100%;
}
.nav{
    height:  50px;
    overflow: hidden;
    background-color:#2D2E3F;
    .pic{
        display: block;
    }
    .tit{
            display: block;
        margin-left: 25px;
         margin-top: -22px;
         font-size: 18px;
    }
    padding: 15px 10px;
    color: #fff;
    box-sizing: border-box;
}
.header{
    border-bottom: solid 1px #dfdfdf;
    line-height: 55px;
    .btn-left{
      float: left;
      padding: 0 10px;
      cursor: pointer;
    }
    .btn-right{
      float: right;
      padding: 0 10px;
      cursor: pointer;
    }
}
.bottom{
  z-index: 1000;
  overflow: hidden;
  bottom: 55px;
  left: 0;
  width: 100%;
  border: solid 1px #dfdfdf;
  padding:12px 10px;
}
.operation-button{
    margin-top: -25px;
    float: right;
  }
.step{
    position: relative;
    color: #000000;
    font-size: 20px;
    text-align: center;
    height: 55px;
    a{
      position: absolute;
    }
}
</style>

