<template>
  <div class="studypathBox">
    <div class="header-container dk-box">
      <exercise-top v-on:chooseIndex="getActiveIndex"></exercise-top>
    </div>
    <div class="body-container box dk-box" style="padding:0px;" v-loading="loading">
       <!-- <div class="details"> -->
         <!-- 主体 -->
              <div v-if="this.details.length > 0" class="detail-main">
                <div style="margin-bottom:20px;margin-left:-20px;">
                  <sort-form  v-on:changeValue="getChange"></sort-form>
                  </div>
                <el-row :span="24" :gutter="40">
                 <div v-for="(c,index) in details" :key="index"  >
                  <el-col :span="4">
                  <div class="box2">
                  <div class="course-img">
                    <img class="imgs" :src="c.logo">
                  </div>
                  <div class="course-body">
                <div class="course-name">{{c.name}}</div>
                <div class="course-desc">{{c.knowledge}}</div>
                  <div class="course-footer">
                  <span class="course-per-num pull-left">
                    <img :src="require('@/assets/student/Courselibrary_Number.png')" >
                      {{c.joinNumber}}
                  </span>
                    <span class="c_btn"  style="float:right;">
                  <el-button type="success"  plain size="mini" @click="onbox(c)">开始学习</el-button>
                  </span>
                    </div>
                      <div style="clear:both"></div>
                  </div>
                  </div>
                  </el-col>
                  </div>
                </el-row>
                <div class="pager-container">
                <page v-if="this.form.page.total>12" :page="form.page" @changed="getList" style="margin-top:20px;"></page>
              </div>
              </div>
              <div  style="text-align:center;margin-bottom:12px;"  class='noneData' v-else>
                      暂无数据
          </div>
     </div>
  </div>
</template>

<script>
/**
 * @file 实验库组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/29
 */
import {
  exerciseList
} from '@/api/mycourse/exercise'
import exerciseTop from './exerciseTop'
import page from '@/components/page'
import sortForm from './sortForm'
export default {
  components: {
    page,
    exerciseTop,
    sortForm
  },
  data() {
    return {
      form: {
        page: {
          current: 1, // 当前页码
          pageCount: 12, // 一页呈现的数据数
          total: 0 // 总的数据数
        }
      },
      orderBy: '',
      details: [], // 课程
      chooseParams: {},
      searParams: {},
      pragramName: null,
      loading: false,
      student: null // 学生信息
    }
  },
  created() {
    this.$nextTick(e => {
      this.getList()
    })
  },
  watch: {
    isShow: { // 深度监听，可监听到对象、数组的变化
      handler(newV, oldV) {
        this.chooseParams = newV
      },
      deep: true
    }
  },
  // 监测
  computed: {
    isShow() {
      return this.chooseParams
    }
  },
  methods: {
    reset() { // 重置
      this.$refs['form'].resetFields()
    },
    getActiveIndex(params) {
      if ((params.category === '' && params.complexity === '' && params.name === '')) {
        this.chooseParams = {}
      } else if (params.category !== '' && params.complexity === '' && params.name === '') {
        this.chooseParams = {
          category: params.category
        }
      } else if (params.complexity !== '' && params.category === '' && params.name === '') {
        this.chooseParams = {
          complexity: params.complexity
        }
      } else if (params.name !== '' && params.category === '' && params.complexity === '') {
        this.chooseParams = {
          name: params.name
        }
      } else if (params.category !== '' && params.complexity !== '' && params.name === '') {
        this.chooseParams = {
          category: params.category,
          complexity: params.complexity
        }
      } else if (params.category !== '' && params.complexity === '' && params.name !== '') {
        this.chooseParams = {
          category: params.category,
          name: params.name
        }
      } else if (params.complexity !== '' && params.category !== '' && params.name === '') {
        this.chooseParams = {
          complexity: params.complexity,
          category: params.category
        }
      } else if (params.complexity !== '' && params.category === '' && params.name !== '') {
        this.chooseParams = {
          complexity: params.complexity,
          name: params.name
        }
      } else if (params.name !== '' && params.category !== '' && params.complexity === '') {
        this.chooseParams = {
          category: params.category,
          name: params.name
        }
      } else if (params.name !== '' && params.category === '' && params.complexity !== '') {
        this.chooseParams = {
          complexity: params.complexity,
          name: params.name
        }
      } else if (params.name !== '' && params.category !== '' && params.complexity !== '') {
        this.chooseParams = {
          category: params.category,
          complexity: params.complexity,
          name: params.name
        }
      }
      // if (params.pragramName !== '') {
      this.pragramName = params.pragramName
      this.getList()
    },
    getChange(val) {
      this.orderBy = val
      this.getList()
    },
    getList() {
      this.loading = true
      this.searParams = {
        form: this.chooseParams,
        page: this.form.page,
        orderBy: this.orderBy,
        pragramName: this.pragramName
      }
      exerciseList(this.searParams).then(e => {
        const data = e.data.data
        this.details = data.records
        this.form.page.total = data.total
        this.loading = false
      })
    },
    // 开始学习
    onbox(c) {
      this.$router.push({ path: 'running', query: { exerciseid: c.id, type: 'start' }})
    }
  }
}
</script>
<style lang="scss" scoped>
.studypathBox{
  background-color: #E4E7ED;
  padding:10px;
 box-sizing: border-box;
}
.body-container {
  margin-top: 10px;
}
 .grid{
    border-bottom: 1px solid #e3e3e3;
    font-size: 14px;
    padding-bottom: 10px;
   }
.header-container{
 height: 200px;
 overflow: hidden;
}
.content-new-background .app-main>div {
    background-color: #E4E7ED;
    box-shadow: 0 0 0px 0 rgba(0,0,0,.2);
}
.dk-box{
    background-color: #fff;
    padding: 0px 20px;
    border-radius:4px;
    box-shadow: 0 0 1px 0 rgba(0,0,0,.2);
}
</style>
<style scoped lang="scss">
   .c_btn{
     display: none;
            .el-button{
                color: #1fad4e;
                border-color:#ffffff;
                border-radius: 4px;
                background:#e8f7ed;
            }
            .el-button:hover{border-color:#1fad4e; }
        }
.box2{
      height: auto;
          border: 1px solid #E3E2E2;
      overflow: hidden;
      cursor: pointer;
      height:250px;
       position: relative;
       margin-bottom: 40px;
    }
    .course-img{
      width: 100%;
    }
    .imgs{
    width: 100%;
    height: 180px;
    }
    .detail-main{
    width: 98%;
    margin: 0 auto;
    // margin-top: 20px;
    text-align: left;
    }
   .course-body {
      position: relative;
    bottom: 0;
    // box-shadow: 0 2px 2px 0 #eee;
    background: #fff;
    transition: bottom .3s;
    height: 100px;
}
.box2:hover{
  box-shadow: 0 4px 20px 4px #ddd;
    .course-body {
    bottom: 54.5px;
     .course-desc{
     height: 54.5px;
      }
      .c_btn{
     display: block;
       }
}
}
.course-desc {
    display: -webkit-box;
    height: 0;
    bottom: 0;
    padding: 0 10px;
    color: #666;
    font-size: 13px;
    line-height: 1.4;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    word-wrap: break-word;
    transition: height .3s;
}
 .course-footer {
    width: 100%;
    color: #9b9da2;
    padding: 0px 10px;
    overflow: hidden;
   margin-bottom: 5px;
}
 .course-footer .course-per-num {
    display: inline-block;
    padding: 4px 0 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    text-align: center;
}
.pull-left {
    float: left !important;
}
.course-name{
    display: block;
    width: 100%;
    padding: 5px 10px ;
    color: #666;
    line-height: 20px;
    font-size: 16px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}
.noneData{
  height: 470px;
  text-align: center;
  width: 100%;
  padding-top:260px;
  background-repeat:no-repeat;
  background-image: url('~@/assets/images/c6.jpg');
  background-position:50% 30%;
}
</style>
