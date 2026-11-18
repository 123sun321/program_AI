<template>
  <div class="profession-page">
     <div class="level-btn">
        <div class="level-button">
          <el-button v-if="this.oldParentId.length!==0" type="text" size="mini" @click="gotoBack()"><span style="color:#409EFF;">返回上一级</span></el-button>
          <span v-if="this.oldParentId.length!==0" style="color:#409EFF;">|</span>
          <el-button type="text" size="mini" @click="gotoHome()"><span style="color:#409EFF;">全部文件</span></el-button>
        </div>
     </div>
      <div v-if="this.tableData.length === 0">
           <div v-show="emptyShow">
              <empty-folder></empty-folder>
           </div>
      </div>
      <div ref="form" :model="entity" v-else-if="this.tableData.length !== 0" class="profession-table" v-loading="loading"
            element-loading-text="文件上传中..."
            element-loading-spinner="el-icon-loading"
            :multipleSelection="multipleSelection" >
         <el-row>
           <!-- 文件夹显示 -->
           <div class="file-page">
             <el-col :xs="8" :sm="6" :md="4" :lg="3"  :span="2" style="position: relative;" v-for="(item,index) in tableData" :key="index">
                <div v-if="item.checked === false" class="course-box1">
                    <div v-if="item.type !== '1'" @click="selectFile(index)">
                        <span><img src="../../../../assets/images/files/select.png"></span>
                    </div>
                    <div v-if="item.type === '1'" style="position:absolute;margin:35px;width:60px;height:50px;opacity:0">
                      <p @click="gotoNext(index)">123</p>
                    </div>
                </div>
                <div v-else-if="item.checked === true" class="course-box2">
                    <div @click="selectFile(index)">
                        <span><img src="../../../../assets/images/files/selected.png"></span>
                    </div>
                </div>
                <div class="content">
                    <span><img style="height:55px;" :src="file_type(item)"></span>
                    <br>
                    <span v-show="isReName" v-if="item.id === activeIndex" class="contentName">{{ item.name }}</span>
                    <span  v-else-if="item.id !== activeIndex" class="contentName">{{ item.name }}</span>
                    <span v-show="reNaming" v-if="item.id === activeIndex">
                      <template>
                        <el-input style="width:60%;margin-left:-35px;background-color:#FFFFFF" v-model="item.name"></el-input>
                      </template>
                      <span style="position:absolute;margin-left:1px;margin-top:-5px;">
                        <img class="cancel" @click="saveReName(item.id,item.name)" src="../../../../assets/images/files/yes2.png">
                        <img class="cancel" @click="cancelReName" src="../../../../assets/images/files/no.png">
                      </span>
                    </span>
                </div>
             </el-col>
            </div>
        </el-row>
      </div>
  </div>
</template>

<script>
/*
资源文件列表
@author 马勇超
@date 2025/03/19 14:00
@version 1.0
*/
import EmptyFolder from '../../fileResource/emptyFolder'
import page from '@/components/page'
import Vue from 'vue'
import {
  getDirectoryWithFileList // 获取文件夹列表
} from '@/api/file/file'
export default {
  components: {
    page,
    EmptyFolder
  },
  name: 'file',
  filter: {
    ellipsis(value) {
      if (!value) return ''
      if (value.length > 8) {
        return value.slice(0, 8) + '...'
      }
      return value
    }
  },
  data() {
    return {
      // 分页和模糊查询
      form: {
        newName: '新建文件夹'
      },
      entity: [],
      parentId: 0, // 父目录id
      oldParentId: [],
      isDisabled: false,
      activeIndex: '',
      emptyShow: true, // 文件夹是否为空
      tableData: [], // 资源信息
      loading: false, // 加载
      multipleSelection: [], // 获取复选框被选中的
      choose: {},
      multipleChoose: [],
      fileType: null,
      type1Number: 0 // 选择文件夹的个数
    }
  },
  // 获取数据
  created() {
    this.$nextTick(e => {
      this.getList()
    })
  },
  computed: {
  },
  methods: {
    // 点击跳转子目录
    gotoNext(index) {
      // debugger
      if (this.oldParentId.length === 0) {
        this.oldParentId.push(0)
      }
      if (this.tableData[index].type !== '1') {
        const le = this.oldParentId.length
        if (this.oldParentId[le - 1] === this.parentId) {
          if (this.parentId === 0) {
            this.oldParentId = []
          }
          this.getList()
        } else { this.oldParentId.splice(le - 1, 1) }
      } else {
        this.oldParentId.push(this.tableData[index].id)
        this.parentId = this.tableData[index].id
      }
      this.getList()
    },
    // 返回上一级
    gotoBack() {
      const sum = this.oldParentId.length
      this.parentId = this.oldParentId[sum - 2]
      this.getList()
      this.oldParentId.splice(sum - 1, 1)
      if (sum - 1 === 1) {
        this.oldParentId = []
      }
    },
    // 返回首页
    gotoHome() {
      this.parentId = 0
      this.oldParentId = []
      this.getList()
    },
    // 获取数据
    getList() {
      // this.loading = true
      this.multipleSelection = []
      // const params = this.$formToParams(this.$refs.form)
      if (this.parentId === undefined) {
        this.parentId = 0
      }
      getDirectoryWithFileList(this.parentId).then(e => {
        this.tableData = e.data.data
        for (var i = 0; i < this.tableData.length; i++) {
          Vue.set(this.tableData[i], 'checked', false)
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    file_type(item) {
      if (item.type === 'jpeg' || item.type === 'jpg' || item.type === 'png') {
        return item.path
      } else {
        return require('@/assets/images/files/file_' + item.type + '.png')
      }
    },
    // 选中文件夹
    selectFile(index) {
      this.tableData[index].checked = !this.tableData[index].checked
      if (this.tableData[index].checked) {
        if (this.tableData[index].type === '1') {
          this.type1Number++
          this.isDisabled = true
        }
        this.multipleSelection.push(this.tableData[index].id)
        this.choose = {
          title: this.tableData[index].name,
          sharePath: process.env.HOST + this.tableData[index].path
        }
        this.multipleChoose.push(this.choose)
        // console.log(this.multipleChoose)
      } else {
        // debugger
        for (let i = 0; i < this.multipleSelection.length; i++) {
          if (this.tableData[index].id === this.multipleSelection[i]) {
            this.multipleSelection.splice(i, 1)
            this.multipleChoose.splice(i, 1)
          }
        }
      }
      // 判断所选文件数量，大于1不能执行重命名操作
      this.entity = this.multipleChoose
    },
    getValue() {
      return this.entity
      // console.log(this.multipleChoose)
    }
  }
}
</script>

<style scoped>
.profession-page{
  min-height: 600px;
}
  .profession-page .el-col {
    border-radius: 4px;
    /* border:1px solid red; */
    margin:5px;
  }
  .profession-page .bg-purple {
    background: #d3dce6;
  }
  .profession-page .bg-purple-light {
    background: #e5e9f2;
  }
  .profession-page .operation-button{
    display: inline-block;
    float: left;
  }
  .operation-search{
      float:right;
  }
  .profession-table{
      /* margin-top: 40px;*/
      border-top: 1px solid rgb(243, 241, 241);
      text-align: center;
      line-height: 32px;
      font-size: 13px;
      /* overflow-y: scroll;
      height: 800px; */
      color:rgb(139, 135, 135);
  }
  .file-page{
    width: 100%;
    height: auto;
  }
  .level-button{
    border: none;
    text-align: left;
    font-size: 10px;
  }
  .inp /deep/ .el-input__inner {
   padding:0px 0px 0px 0px;
   width: 80%;
}
  .content {
      padding-top: 30px;
  }
  .course-box{
      position: absolute;
      left: 0;
      right: 0;
      margin: auto;
      height: 130px;
  }
  .course-box1{
      /* background-color: #F1F4F9; */
      opacity: 0;
      position: absolute;
      overflow: hidden;
      border-radius: 5%;
      margin-left: -3px;
      left: 0;
      right: 0;
      margin: auto;
      background: rgba(246, 245, 245, 0.5);
      height: 130px;
      /* z-index:100;  */
  }
  .course-box1 img{
    margin-left: -70%;
    margin-top: 5%;
  }
  .course-box2{
      /* background-color: #F1F4F9; */
      opacity: 1;
      position: absolute;
      /* overflow: hidden; */
      margin-left: -3px;
      border: 1px solid skyblue;
      border-radius: 5%;
      left: 0;
      right: 0;
      margin: auto;
      background: rgba(246, 245, 245, 0.5);
      height: 130px;
      /* z-index:100;  */
  }
  .course-box2 img{
    margin-left: -70%;
    margin-top: 5%;
  }
  .el-col .course-box1:hover,.el-col .course-box1:active{
    opacity: 1;
    cursor: pointer;
}
/* 新建文件夹输入框样式 */
.inputFile{
  width: 61%;
  font-size: 3px;
  text-align: left;
}
.cancel{
  position: relative;
  top: 7px;
}
.contentName{
  padding:5px 10px;
  display: inline-block;
  max-width: 120px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow:ellipsis;
}
.emptyFolder{
  position: absolute ;
  width: 90%;
  margin-top: -290px;
}
.reNameInput{
  width: -10%
}
</style>
