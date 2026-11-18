<template>
    <div class="exercisesstep">
        <div class="labdoc-header">
            <span>{{entity.name}}</span>
            <div class="pull-right">
                <el-button @click="callback">返回</el-button>
            </div>
        </div>
        <div class="labdoc-bottom">
            <el-row>
                <el-col :span="5">
                    <div class="labdoc-nav customscroll">
                       <ul>
                           <li v-for="(item,index) in menuList" :key="index" @click="jump(index)" v-bind:class="[item.rank,{active:index===activeStep}]">{{item.name}}</li>
                       </ul>
                    </div>
                </el-col>
                <el-col :span="19">
                    <div class="labdoc-content customscroll" ref="labdoccontent" @scroll="handleScroll ">
                        <div>
                            <vue-markdown class="markdown-body" v-highlight ref="markdowncontent" :source="entity.content"></vue-markdown> 
                        </div>
                    </div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
/**
 * 实验步奏
 * @author Juci
 */
import { get } from '@/api/mycourse/exercise'
import VueMarkdown from 'vue-markdown'
export default {
  components: {
    VueMarkdown
  },
  data() {
    return {
      exerciseid: this.$route.query.exerciseid, // 练习编号
      scrollTop: 0,
      activeStep: 0,
      entity: {},
      menuList: []
    }
  },
  created() {
    this.$nextTick(e => {
      this.init()
    })
  },
  methods: {
    init() {
      var vm = this
      if (this.exerciseid !== undefined) {
        get(this.exerciseid).then(e => {
          vm.entity = e.data.data
          setTimeout(this.initHtml, 500)
        })
      }
    },
    // 解析dom，添加对应属性
    initHtml() {
      const markdowndom = this.$refs.markdowncontent
      const hdoms = markdowndom.$el.querySelectorAll('h1,h2')
      hdoms.forEach(element => {
        this.menuList.push({ name: element.innerText, rank: element.tagName === 'H1' ? 'step1' : 'step2' })
        element.classList.add('step-jump')
      })
    },
    // 主内容滚动
    handleScroll() {
      this.scrollTop = this.$refs.labdoccontent.scrollTop
      this.onScroll()
    },
    onScroll() {
      const _article = document.querySelectorAll('.step-jump')
      _article.forEach((item, index) => {
        if (this.scrollTop >= item.offsetTop) {
          this.activeStep = index
        }
      })
    },
    jump(index) {
      const vm = this
      this.activeStep = index
      const jump = document.querySelectorAll('.step-jump')
      const total = jump[index].offsetTop
      let distance = this.scrollTop
      let step = total / 50 >> 0
      // 如果选中元素的高度大于已滚动的高度则下移,否则上移
      if (total > distance) {
        smoothDown()
      } else {
        const newTotal = distance - total
        step = newTotal / 50 >> 0
        smoothUp()
      }
      // 往下移动
      function smoothDown() {
        if (distance < total) {
          distance += step
          vm.viewScroll(distance)
          setTimeout(smoothDown, 10)
        } else {
          vm.viewScroll(total)
        }
      }
      // 往上移动
      function smoothUp() {
        if (distance > total) {
          distance -= step
          vm.viewScroll(distance)
          setTimeout(smoothUp, 10)
        } else {
          vm.viewScroll(total)
        }
      }
    },
    viewScroll(data) {
      this.$refs.labdoccontent.scrollTop = data > 10 ? data - 10 : data
    },
    // 返回
    callback() {
      this.$router.back(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.exercisesstep{
    height: 100%;
    .labdoc-header{
        position: fixed;
        top: 50px;
        height: 55px;
        line-height: 55px;
        width: 100%;
        padding: 0 18px;
        color: #666;
        background: #fff;
        font-size: 16px;
        border-bottom: solid 1px #eee;
        z-index: 10;
    }
    .labdoc-bottom{
        height: 100%;
        padding-top: 55px;
        .el-row{height: 100%;
        .el-col{height: 100%;}}
        .labdoc-nav{
            height: 100%;
            background-color: #f7f7f7;
            overflow: auto;
            padding: 10px 15px;
            ul{
                li{
                    font-size: 16px;
                    padding: 20px 0 10px 0;
                    color: #666;
                    cursor: pointer;
                }
                li:hover,li.active{
                 color: #08bf91;
                }
                li.step2{
                     font-size: 14px;
                    padding-left: 15px;
                }
                li.step3{
                    padding-left: 30px;
                }
            }
        }
        .labdoc-content{
            height: 100%;
            background-color: #ffffff;
            overflow-y: auto;
            padding: 0 30px;
        }
    }
}
</style>

