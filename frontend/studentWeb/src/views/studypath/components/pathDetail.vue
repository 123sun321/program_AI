<template>
 <div class="detail" v-loading="loading">
   <div  v-if="pathCategory.length>0 && details.length>0">
   <div   v-for="(item,index) in  pathCategory" :key="index">
    <div class="header" ><span class="line hidden-xs"></span>{{item.name}}<span class="line hidden-xs"></span></div>
    <div class="detail-main">
        <el-row :span="24" :gutter="40">
           <div v-for="(c,index) in details" :key="index"  >
        <el-col :span="6" v-if="item.code===c.category">
        <div class="box">
        <div class="course-img">
           <img class="imgs" :src="c.course.logo">
        </div>
        <div class="course-body">
		 <div class="course-name">{{c.course.name}}</div>
   <div class="course-desc">{{c.course.summary}}</div>
         <div class="course-footer">
				<span class="course-per-num pull-left">
          <img :src="require('@/assets/student/Courselibrary_Number.png')" >
            {{c.course.joinNumber}}
				</span>
        	<span class="but"  style="float:right;">
        <span class="c_btn"  v-if="c.exerciseJoinRecordList.length === 0"> <el-button  type="success" plain size="mini" @click="onbox(c)">开始学习</el-button></span>
      <span class="d_btn" v-else> <el-button  type="warning" plain size="mini" @click="onbox(c)">已学习</el-button> </span>
				</span>
    	    </div>
           <div style="clear:both"></div>
       	</div>
        </div>
        </el-col>
        </div>
    </el-row>
 </div>
 </div>
 <div style="text-align:center; margin-top:50px;">
    <el-button type="success"> 完成学习 </el-button></div>
    </div>
    <div  style="text-align:center;margin-bottom:12px;"  class='noneData' v-else>
            暂无数据
    </div>
 </div>
</template>
<script>
/**
 * @file 路径详情组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/28
 */
// import addManager from './addManager'
import {
  getJoinDetails
} from '@/api/studypath/pathmanage'
import { fetchItems } from '@/api/dictionary'
export default {
  data() {
    return {
      loading: false,
      pathCategory: [], // 路径名称
      details: [], // 课程
      finished: 0, // 已学课程
      flag: 0
    }
  },
  created() {
    fetchItems(null, 'path_category').then(e => {
      this.pathCategory = e.data.data
    })
    this.$nextTick(e => {
      this.getList()
    })
  },
  methods: {
    getList() {
      this.loading = true
      // const searParams = {
      //   form: {}
      // }
      getJoinDetails().then(e => {
        this.details = e.data.data
        this.details.forEach(element => {
          if (element.exerciseJoinRecordList.length !== 0) {
            this.finished += 1
          }
        })
        // console.log(this.finished)
        const param = {
          finishedLength: this.finished,
          detailLength: this.details.length
        }
        this.$emit('headCallBack', param)
        this.loading = false
      })
    },
    onbox(item) {
      this.$router.push({ name: 'courseDetail', query: { 'courseId': item.courseId }})
      // this.$router.push('/course/courseindex')
    }
  }
}
</script>
<style scoped lang="scss">
.detail{
  .but{
      display: none;
  }
   .c_btn{
            .el-button{
                color: #1fad4e;
                border-color:#ffffff;
                background:#e8f7ed;
                border-radius: 4px;
            }
            .el-button:hover{border-color:#1fad4e; }
        }
         .d_btn{
            .el-button{
                color: #E6A23C;
                border-color:#ffffff;
                background:#FDF6EC;
                border-radius: 4px;
            }
            .el-button:hover{border-color:#E6A23C; }
        }
  .header{
    font-size: 20px;
    color: #333;
      text-align: center;
    margin-top: 20px;
    .line {
    width: 12%;
    display: inline-block;
    border-top: 1px solid #ccc;
    margin-left: 5px;
    margin-right: 5px;
    margin-bottom: 5px;
}
    }
    .box{
      border: 1px solid #E3E2E2;
      height: auto;
      overflow: hidden;
      cursor: pointer;
      height:248px;
       position: relative;
    }
    .course-img{
      width: 100%;
    }
    .imgs{
    width: 100%;
    height: 180px;
    }
    .detail-main{
    width: 96%;
    margin: 0px auto;
    padding-bottom: 20px;
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
.box:hover{
  box-shadow: 0 4px 20px 4px #ddd;
    .course-body {
    bottom: 54.5px;
     .course-desc{
     height: 54.5px;
      }
      .but{
     display: block;
       }
}
}
.course-desc{
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
 .el-col {
  margin-top: 40px;
   width: 25%;
   height: auto;
   position: relative;
}
}
.noneData{
  height: 490px;
  text-align: center;
  width: 100%;
  padding-top:260px;
  background-repeat:no-repeat;
  background-image: url('~@/assets/images/c6.jpg');
  background-position:50% 30%;
}
</style>
