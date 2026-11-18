<template>
  <div  class="wrap">
    <div class="course">
      <div class="searchForm">
        <div class="searchFormWrap" :style="showAllTag ? 'height: auto;' : 'height: 300px'">
          <SearchForm :form="this.form.form" @changeSearchForm="getList"/>
        </div>
        <div :class="showAllTag ? 'triangle_border_up' : 'triangle_border_down'" @click="showAllTag = !showAllTag"></div>
      </div>
      <div class="totalCourse clearfix" v-loading="loading" v-if="datalist.length > 0">
        <SortForm @changeValue="getList" :form="this.form.form"/>
        <div class="item" v-for="(data, index) in datalist" :key="index">
          <img :src="data.logo" alt="" @click="pathToDetail(data)" class="logo">
          <div style="width: 100%;">
            <div class="info">
              <div style="margin-top: 37px">
                <span class="title" @click="pathToDetail(data)">{{data.name}}</span>
                <span class="hardstar">
                  <img src="../../assets/student/Courselibrary_difficulty.png" alt="" v-for="(n, index) in parseInt(data.complexity)" :key="index">
                </span>
              </div>
              <div class="number">{{data.joinNumber}}</div>
            </div>
            <div class="summary">{{data.summary}}</div>
            <div class="tags">
              <ul><li v-for="(tag, index) in data.tags" :key="index" @click="tagClick(tag)">{{ tag }}</li></ul>
            </div>
          </div>
          <a :class="data.courseId === '1' ? 'collected' : 'collect'" @click="addOrCancel(data.courseId, data.id)">{{data.courseId === '1' ? '已收藏' : '收藏'}}</a>
        </div>
        <div class="pager-container">
          <page :page="form.page" @changed="getList"></page>
        </div>
      </div>
      <div v-else class="noneData">
        暂无数据
      </div>
    </div>
  </div>
</template>

<script>
/**
 * @file 课程库
 * @author 宋乃乐
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/19
 */
import SearchForm from './searchForm'
import page from '@/components/page'
import SortForm from './sortForm'
import {
  getCoursePage,
  addCollect,
  delCollect
} from '../../api/course/index'
// import collectPage from '../../api/mycourse/course'
export default {
  name: 'App',
  components: {
    SearchForm,
    page,
    SortForm
  },
  data() {
    return {
      form: {
        form: {
          courseChooseType: 'basic',
          searchContent: '',
          isPublic: 0,
          joinCategory: null,
          techType: '',
          difficultLevel: 0,
          tag: '',
          orderBy: '',
          isAsc: '0'
        },
        page: {
          current: 1, // 当前页码
          pageCount: 6, // 一页呈现的数据数
          total: 0 // 总的数据数
        }
      },
      datalist: [],
      loading: false,
      showAllTag: false
    }
  },
  created() {
    this.getList()
  },

  methods: {
    getList() {
      this.loading = true
      getCoursePage(this.form).then(data => {
        this.datalist = data.data.data.records
        this.form.page.total = data.data.data.total
        this.loading = false
      }, () => {
        this.loading = false
      })
    },
    // 点击每个课程的标签
    tagClick(tag) {
      this.form.form.tag = tag
      this.getList()
    },
    // 点击图片进入课程详情
    pathToDetail(data) {
      this.$router.push({ name: 'courseDetail', query: { courseId: data.id, isCollected: data.courseId }})
    },
    addOrCancel(courseId, id) {
      if (courseId === '1') {
        // 已收藏
        this.cancalCollectCourse(id)
      } else {
        this.collectCourse(id)
      }
    },
    // 收藏课程
    collectCourse(id) {
      addCollect(id).then(data => {
        this.$sucmsg('收藏成功')
        // this.getList()
        const index = this.datalist.findIndex(item => item.id === id)
        this.datalist[index].courseId = '1'
      }, () => {
        this.$message.error('啊哦 服务器君正在开小差!')
      })
    },
    cancalCollectCourse(id) {
      delCollect(id).then(data => {
        this.$sucmsg('取消收藏成功')
        const index = this.datalist.findIndex(item => item.id === id)
        this.datalist[index].courseId = '0'
      }, () => {
        this.$message.error('啊哦 服务器君正在开小差!')
      })
    }
  }
}
</script>

<style scoped>
  .clearfix:after{visibility:hidden;display:block;font-size:0;content: " ";clear:both;height:0;}
  .clearfix{*zoom:1;}
  .wrap {
    width: 100%;
    height: 100%;
  }
  .course {
    width: 100%;
    padding: 10px;
    height: 100%;
  }
  .searchFormWrap {
    overflow: hidden;
    padding-bottom: 14px;
  }
  .searchForm {
    width: 100%;
    background: #fff;
    border-radius: 5px;
    position: relative;
  }
  .triangle_border_down {
    position: absolute;
    bottom: -14px;
    left: 50%;
    margin-left: -30px;
    border-width: 14px 20px 0;
    border-style: solid;
    border-color: #fff transparent transparent;
    z-index: 100;
    cursor: pointer;
  }
  .triangle_border_up {
    width: 0;
    height: 0;
    border-width: 0px 20px 14px;
    border-style: solid;
    border-color: transparent transparent #ddd;
    left: 50%;
    position: absolute;
    margin-left: -30px;
    bottom: 0px;
    cursor: pointer;
  }
  .totalCourse {
    width: 100%;
    background: #fff;
    margin-top: 15px;
    border-radius: 5px;
  }
  .noneData{
    height: 490px;
    text-align: center;
    width: 100%;
    padding-top:260px;
    background-color: #fff;
    background-repeat:no-repeat;
    background-image: url('~@/assets/images/c6.jpg');
    background-position:50% 30%;
    margin-top: 14px;
    color: #666;
  }
  .item {
    width: 49%;
    height: 205px;
    float: left;
    border-bottom: 0.5px solid #eee;
    position: relative;
  }
  .item:nth-child(odd) {
    border-right: 0.5px solid #eee;
  }
  .item .info {
    padding-left: 280px;
  }
  .item .summary {
    padding-left: 278px;
    font-family: "PingFang-SC-Regular";
    font-size: 12px;
    color: #666666;
    height: 40px;
    padding-right: 20px;
    margin-top: 20px;
  }
  .tags {
    padding-left: 265px;
  }
  .tags ul li{
    margin: 0 10px;
    font-size: 14px;
    padding: 5px 10px;
    float: left;
    color: #1fad4e !important;
    border-radius: 4px;
    border-color:#ffffff;
    background:#e8f7ed;
    cursor: pointer;
  }
  .title {
    font-size: 16px;
    font-family: "PingFang-SC-Medium";
    color: #333333;
    cursor: pointer;
  }
  .number {
    font-size: 14px;
    font-family: "PingFang-SC-Regular";
    color: #666666;
    margin-top: 10px;
  }
  .collected {
    position: absolute;
    width: 90px;
    height: 28px;
    top: 60px;
    right: 20px;
    background: #e8f7ed;
    color: #1fad4e;
    border-radius: 13px;
    font-size: 14px;
    font-family: "PingFang-SC-Regular";
    border: 0;
    cursor: pointer;
    line-height: 28px;
    text-align: center;
  }
  .collect {
    position: absolute;
    width: 90px;
    height: 28px;
    top: 60px;
    right: 20px;
    border: 0;
    color: #666;
    background-color: #dddeee;
    border-radius: 13px;
    font-family: "PingFang-SC-Regular";
    font-size: 14px;
    cursor: pointer;
    line-height: 28px;
    text-align: center;
  }
  .pager-container {
    margin-top: 8px;
    float: left;
    width: 100%;
  }
  .logo {
    width: 230px;
    height: 130px;
    display: block;
    float: left;
    margin-top: 37px;
    margin-left: 30px;
    cursor: pointer;
    background: #eee;
    background-size: cover;
    background-position: 50% 50%;
  }
  .hardstar {
    padding-left: 10px;
  }
  .hardstar img {
    margin: 0 2px;
  }
</style>
