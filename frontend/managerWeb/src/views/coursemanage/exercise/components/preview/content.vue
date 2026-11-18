<template>
    <div class="run-left">
        <vue-markdown v-highlight ref="markdowncontent" :source="content" v-show="false"></vue-markdown>
        <div ref="mySidenav" class="sidenav">
            <a v-for="(c,index) in lists" :key="index"  @click="closeNav(index)">{{c.title}}</a>
        </div>
       <!-- 文档 -->
       <div ref="myleft" class="left">
        <div class="step header" ref="border">
            <a style="left:10px;" @click="lastpage()"><img :src="require('@/assets/images/Experiment_leftarrow.png')" ></a>
            <span> {{currentData.title}}</span>
            <a style="right:10px;"  @click="nextpage()"><img :src="require('@/assets/images/Experiment_rightarrow.png')" ></a>
        </div>
          <div class="list markdown-body"><span v-html="currentData.content"></span></div>
      </div>
         <!-- 底部 -->
         <div ref="bot" class="step bottom">
            <a style="left:10px;"   @click="openNav()"><img :src="require('@/assets/images/Experiment_more.png')" ></a>
            <span>&nbsp;</span>
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
import VueMarkdown from 'vue-markdown' // 直接组件引入
export default {
  components: {
    page,
    VueMarkdown
  },
  props: ['content'],
  data() {
    return {
      lists: [],
      currentData: {},
      currentIndex: 0,
      flag: 0
    }
  },
  watch: {
    content(val) {
      setTimeout(this.inithtml, 500)
    }
  },
  created() {
    // this.$nextTick(e => {
    //   setTimeout(this.inithtml, 500)
    // })
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
      if (this.currentIndex >= this.lists.length - 1) {
        this.$sucmsg('已经是最后一页了！')
      } else {
        this.currentIndex++
        this.init(this.currentIndex)
      }
    },
    // 上一页
    lastpage() {
      if (this.currentIndex > 0) {
        this.currentIndex--
        this.init(this.currentIndex)
      } else {
        this.$sucmsg('已经是第一页了！')
      }
    },
    // 取数据
    init(i) {
      this.currentData = this.lists[i]
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
        this.currentIndex = i
        this.init(i)
      }
      this.$refs.mySidenav.style.left = '-351px'
      this.$refs.border.style.borderColor = '#dfdfdf'
      this.$refs.myleft.style.background = '#fff'
      this.flag = 0
    }
  }
}
</script>
<style scoped lang="scss">
.run-left{
    position: relative;
    height: 500px;
    width: 100%;
    overflow: hidden;
}
.stepbox{
    height: 100%;
}
.list{
    padding:10px;
    font-size:14px;
    color:#666;
    line-height:20px;
    // height: auto;
    height: calc(100% - 110px);
    overflow-y:auto;
      }
      .list::-webkit-scrollbar {/*滚动条整体样式*/
        width: 8px;     /*高宽分别对应横竖滚动条的尺寸*/
        height: 1px;
      }
      .list::-webkit-scrollbar-thumb {/*滚动条里面小方块*/
        border-radius: 8px;
        background: #B9B9B9;
     }
      .list::-webkit-scrollbar-track {/*滚动条里面轨道*/
        border-radius: 8px;
        background: #EDEDED;
      }
.sidenav {
    height: 444px;
    margin: 1px;
    width: 350px;
    position:absolute;
    z-index: 1001;
    top: 0;
    left: -351px;
    background-color: #fff;
    overflow-x: hidden;
    transition: 0.5s;
    text-align:center;
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
            .el-button{
                color: #fff;
                border-color:#65668F;
                border-radius: 4px;
                background:#65668F;
            }
            .el-button:hover{border-color:#2D2E3F;background:#2D2E3F; }
        }
.left{
    border: solid 1px #dfdfdf;
    height: 100%;
}
.nav{
    height:  50px;
    overflow: hidden;
    background-color:#2D2E3F;
    .pic{
        display: block;
        // padding-top: 5px;
    }
    .tit{
            display: block;
        margin-left: 25px;
         margin-top: -22px;
         font-size: 18px;
    }
    padding: 15px 10px;
    color: #fff;
    // vertical-align: middle;
    box-sizing: border-box;
}
.header{
     padding: 15px 10px;
      border-bottom: solid 1px #dfdfdf;
}
.bottom{
    z-index: 1000;
// position: absolute;
overflow: hidden;
bottom: 55px;
left: 0;
width: 100%;
 border: solid 1px #dfdfdf;
//  border-top: none;
 padding:12px 10px;
 height: 55px;
}
.operation-button{
    /* display: inline-block; */
    margin-top: -25px;
    float: right;
  }
.step{
    position: relative;
    color: #000000;
    font-size: 20px;
    height: 55px;
    text-align: center;
    a{
        position: absolute;
    }
}
</style>

