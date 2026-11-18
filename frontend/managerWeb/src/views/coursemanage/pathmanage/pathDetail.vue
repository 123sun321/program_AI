<template>
 <div class="detail">
   <div v-for="(item,index) in  pathCategory" :key="index">
    <div class="header" ><span class="line hidden-xs"></span>{{item.name}}<span class="line hidden-xs"></span></div>
    <div class="detail-main">
        <el-row :span="24" :gutter="60">
           <div v-for="(c,index) in details" :key="index"  >
        <el-col :span="6" style="position: relative;" v-if="item.code===c.category" >
        <div class="course-box">
        <ul>
            <li @click="addnum(c.category,c.id,index)"><i class="el-icon-upload2"></i></li>
            <li @click="reduce(c.category,c.id,index)"><i class="el-icon-download"></i></li>
            <li @click="deletes(c.id,index)"><i class="el-icon-delete"></i></li>
        </ul>
        </div>
        <div class="box">
        <div class="course-img">
           <img class="imgs" :src="c.course.logo">
        </div>
        <div class="course-body">
		 <div class="course-name">{{c.course.name}}</div>
         <div class="course-footer">
				<span class="course-per-num pull-left">
            <img :src="require('@/assets/images/Courselibrary_Number.png')" >
            {{c.course.joinNumber}}
				</span>
    	    </div>
       	</div>
        </div>
        </el-col>
        </div>
        <el-col :span="6">
           <div style="border: 1px dashed #999 ;border-radius: 5%" @click.stop="addpath(item.code)" class="add"><i class="el-icon-plus" ></i></div>
        <!-- <img     alt="新手指南之玩转实验楼" @click="add" width="100%" height="90%" src="../../../assets/images/upImg.png"> -->
        </el-col>
    </el-row>
 </div>
 </div>
 <add-manager ref="mup" :getList="getList"></add-manager>
 </div>
</template>
<script>
/**
 * @file 路径详情组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/13
 */
import addManager from './addManager'
import {
  showAll,
  pathup,
  del
} from '@/api/coursemanage/pathmanage'
import { fetchItems } from '@/api/dictionary'
export default {
  components: {
    addManager
  },
  data() {
    return {
      pathCategory: '', // 路径名称
      details: '', // 课程
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
      const searParams = {
        form: {}
      }
      showAll(searParams).then(e => {
        this.details = e.data.data
      })
    },
    //   打开添加课程弹窗
    addpath(code) {
      this.$refs.mup.open(code)
    },
    //   课程上升排序
    addnum(code, id, index) {
      const params = { id1: id, sort1: '', id2: '', sort2: '' }
      for (let i = index - 1; i >= 0; i--) {
        if (this.details[i].category === code) {
          params.id2 = this.details[i].id
          this.flag = 1
          break
        }
      }
      if (this.flag !== 0) {
        this.$cfm('确定上移吗？', e => {
          pathup(params).then(e => {
            // debugger
            if (e.data.success) {
              this.$sucmsg('上移成功!')
              this.getList()
            } else {
              this.$errmsg('上移失败!')
              this.getList()
            }
          })
        })
      } else {
        this.$warnmsg('第一个不能上移!')
      }
      this.flag = 0
    },
    //   课程下移排序
    reduce(code, id, index) {
      const params = { id1: id, sort1: '', id2: '', sort2: '' }
      for (let i = index + 1; i < this.details.length; i++) {
        if (this.details[i].category === code) {
          params.id2 = this.details[i].id
          this.flag = 1
          break
        }
      }
      if (this.flag !== 0) {
        this.$cfm('确定下移吗？', e => {
          pathup(params).then(e => {
            if (e.data.success) {
              this.$sucmsg('下移成功!')
              this.getList()
            } else {
              this.$errmsg('下移失败!')
              this.getList()
            }
          })
        })
      } else {
        this.$warnmsg('最后一个不能下移!')
      }
      this.flag = 0
    },
    // 删除
    deletes(id, index) {
      this.$cfm('确定删除吗？', e => {
        del(id).then(e => {
          if (e.data.success) {
            this.$sucmsg('删除成功!')
            this.details.splice(index, 1)
            // this.getList()
          } else {
            this.$errmsg('删除失败!')
            // this.getList()
          }
        })
      })
    }
  }
}
</script>
<style scoped lang="scss">
.detail{
 .header{
    font-size: 20px;
    color: #787878;
      text-align: center;
    margin-top: 30px;
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
      height: auto;
      position: relative;
    }
    .course-img{
    width: 100%;
    }
    .imgs{
      width: 100%;
      height: 159px;
    }
    .detail-main{
    width: 80%;
    margin: 0 auto;
    text-align: left;
    }
    .course-box{
    opacity: 0;
    position: absolute;
    overflow: hidden;
    width:82.5%;
     left: 0;
      right: 0;
      margin: auto;
    background: rgba(0,0,0,0.5);
    height: 100%;
    z-index:100;
    text-align: center;
      ul{
         margin-top: 30%;
        display:inline-block;
        height: 60px;
      }
      li{
          float: left;
          cursor: pointer;
          font-size: 36px;
          margin: 0px 10px;
          color: #fff;
      }
}
.add{
     font-size: 48pt;
      color: #999;
      text-align: center;
      padding:30% 0;
       cursor: pointer;
       overflow: hidden;
      font-weight: 900;
}
.el-col .course-box:hover,.el-col .course-box:active{
    opacity: 1;
   cursor: pointer;
}
.course-body {
    position: relative;
    box-shadow: 0 2px 2px 0 #eee;
    background: #fff;
    height: 80px;
}
 .course-footer {
    width: 100%;
    height: 43px;
    color: #9b9da2;
    padding: 5px 10px;
    overflow: hidden;
    margin-top: 10px;
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
    padding: 10px 10px 0;
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
</style>
