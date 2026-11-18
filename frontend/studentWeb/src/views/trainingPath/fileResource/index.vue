<template>
  <div class="maincontent profession-page">
      <div class="grid-content bg-purple-dark">
        <el-form :inline="true" :model="form" ref="form" class="demo-form-inline">
          <div class="operation-button">
            <el-button @click="createNewFile"><img style="width:12px;" src="../../../assets/images/files/build.png">&nbsp;<span style="color:#409EFF">新建文件夹</span></el-button>
            <!-- <el-button @click="downLoad"><a><img style="width:15px;" src="../../../assets/images/files/download.png">&nbsp;<span style="color:#409EFF">下载</span></a></el-button> -->
            <div class="level-button">
              <el-button v-if="this.oldParentId.length!==0" type="text" size="mini" @click="gotoBack()"><span style="color:#409EFF;">返回上一级</span></el-button>
              <span v-if="this.oldParentId.length!==0" style="color:#409EFF;">|</span>
              <el-button type="text" size="mini" @click="gotoHome()"><span style="color:#409EFF;">全部文件</span></el-button>
            </div>
          </div>
          <div class="file-button">
             &nbsp;&nbsp;&nbsp;<el-button  v-if="this.multipleSelection.length!== 0" @click="batchDeleted"><img src="../../../assets/images/files/del.png">&nbsp;&nbsp;<span style="color:#409EFF;">删除</span></el-button>
             <el-button v-if="this.multipleSelection.length!== 0" @click="removeFile"><img src="../../../assets/images/files/move.png">&nbsp;<span style="color:#409EFF;width:140px;">移动到</span></el-button>
             <el-button v-if="this.multipleSelection.length!== 0" :disabled="isAlert" @click="reName"><img src="../../../assets/images/files/rename.png">&nbsp;<span style="color:#409EFF;width:140px;">重命名</span></el-button>
             <!-- <el-button v-if="this.multipleSelection.length!== 0" :disabled="isDisabled" @click="shareResource"><img src="../../../assets/images/files/connect.png">&nbsp;<span style="color:#409EFF;width:140px;">复制链接</span></el-button> -->
          </div>
        </el-form>
      </div>
      <div v-if="this.tableData.length === 0">
           <!-- 新建文件夹 -->
           <div class="emptyFolder">
            <div v-show="isShow" ref="form" :model="form">
              <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="2" :span="2" style="position: relative;">
                <div class="course-box inp" style="margin-top:30px;">
                <span><img src="../../../assets/images/files/file.png"></span><br>
                <span>
                  <el-form :inline="true">
                    <el-form-item prop="newName" :rules="[{validator:remoteValidate(form.newName,'文件夹名不可为空！')}]">
                      <el-input v-model="form.newName" size="mini" class="inputFile"></el-input>
                    </el-form-item>
                  </el-form>
                  <span style="position:absolute;margin-left:66px;margin-top:-40%;">
                    <img class="cancel" @click="addFileResource" src="../../../assets/images/files/yes2.png">
                    <img class="cancel" style="top:8px;" @click="cancelCreateFile" src="../../../assets/images/files/no.png">
                  </span>
                </span>
                </div>
              </el-col>
            </div>
           </div>
           <div v-show="emptyShow">
              <empty-folder></empty-folder>
           </div>
      </div>
      <div v-else-if="this.tableData.length !== 0" class="profession-table" v-loading="false"
            element-loading-text="文件上传中..."
            element-loading-spinner="el-icon-loading" >
         <el-row>
           <!-- 新建文件夹 -->
           <div v-show="isShow" ref="form" :model="form">
            <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="2" :span="2" style="position: relative;">
               <div class="course-box inp" style="margin-top:30px;">
              <span><img src="../../../assets/images/files/file.png"></span><br>
              <span>
               <el-form :inline="true">
                <el-form-item prop="newName" :rules="[{validator:remoteValidate(form.newName,'文件夹名不可为空！')}]">
                  <el-input v-model="form.newName" size="mini" class="inputFile"></el-input>
                </el-form-item>
               </el-form>
                <span style="position:absolute;margin-left:18px;margin-top:-40%;">
                  <img class="cancel" @click="addFileResource" src="../../../assets/images/files/yes2.png">
                  <img class="cancel" style="top:8px;margin-left:-5px;"  @click="cancelCreateFile" src="../../../assets/images/files/no.png">
              </span>
              </span>
              </div>
            </el-col>
           </div>
           <!-- 文件夹显示 -->
           <div class="file-page" ref="form" :model="form" v-loading="loading">
             <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="2" :span="2" v-for="(item,index) in tableData" :key="index" style="position: relative;">
                <div v-if="item.checked === false" class="course-box1">
                    <div @click="selectFile(index)">
                        <span><img src="../../../assets/images/files/select.png"></span>
                    </div>
                    <div style="position:absolute;margin-left:31px;width:60px;height:50px;opacity:0">
                      <p v-if="item.type === 1" @click="gotoNext(index)">123</p>
                      <p v-else @click="gotoTraining(index)">123</p>
                    </div>
                </div>
                <div v-else-if="item.checked === true" class="course-box2">
                    <div @click="selectFile(index)">
                        <span><img src="../../../assets/images/files/selected.png"></span>
                    </div>
                </div>
                <div class="content">
                    <span><img style="height:55px;" :src="file_type(item)"></span>
                    <br>
                    <el-tooltip class="item" effect="light" placement="bottom">
                    <!-- <el-button style="" >上左</el-button> -->
                     <div v-if="item.type === 1" slot="content">
                       {{item.title}}<br/>
                       <!-- 修改日期：{{ item.creationTime }} -->
                     </div>
                     <div v-else-if="item.type !== 1" slot="content">
                       {{item.title}}.{{item.type}}<br/>
                       <!-- 大小：{{item.size}}<br/> -->
                       <!-- 上传日期：{{ item.creationTime }} -->
                     </div>
                        <span v-show="isReName" v-if="item.id === activeIndex" class="contentName">{{ item.title }}</span>
                        <span  v-else-if="item.id !== activeIndex" class="contentName">{{ item.title }}</span>
                    </el-tooltip>
                    <span v-show="reNaming" v-if="item.id === activeIndex">
                      <template>
                        <el-form>
                          <el-form-item prop="item.title" :rules="[{validator:remoteValidate(item.title,'文件夹名不可为空！')}]">
                           <el-input style="width:60%;margin-left:-35px;background-color:#FFFFFF" v-model="item.title"></el-input>
                          </el-form-item>
                        </el-form>
                      </template>
                      <span style="position:absolute;margin-left:30px;margin-top:-55px;">
                        <img class="cancel" @click="saveReName(item.id,item.title)" src="../../../assets/images/files/yes2.png">
                        <img class="cancel" @click="cancelReName" src="../../../assets/images/files/no.png">
                      </span>
                    </span>
                </div>
             </el-col>
            </div>
        </el-row>
        <remove ref="mov" @success="getList"></remove>
        <share ref="share" @success="getList"></share>
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
import EmptyFolder from './emptyFolder'
import page from '@/components/page'
import remove from './remove'
import share from './share'
import {
  getTrainingPathInfo,
  addFolder,
  editFolderName,
  delFilesByIds
} from '../../../api/trainingPath/index'
export default {
  components: {
    page,
    remove,
    share,
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
      form: {
        newName: '新建文件夹'
      },
      parentId: this.$route.query.parentid, // 父目录id
      oldParentId: [],
      isEmpty: false,
      isDisabled: false,
      activeIndex: '',
      isShow: false, // 是否选择新建文件夹
      emptyShow: true, // 文件夹是否为空
      isAlert: false, // 是否修改名字
      isReName: true, // 是否重命名
      reNaming: false, // 重命名ing
      tableData: [], // 资源信息
      tableData1: [],
      loading: false, // 加载
      multipleSelection: [], // 获取复选框被选中的
      fileType: null,
      type1Number: 0, // 选择文件夹的个数
      downLoadName: null, // 下载文件的文件名
      downLoadType: [
        { id: 'jpeg', type: 'image/jpeg' },
        { id: 'jpg', type: 'image/jpg' },
        { id: 'png', type: 'image/png' },
        { id: 'gif', type: 'image/gif' },
        { id: 'txt', type: 'application/txt' },
        { id: 'docx', type: 'application/msword;charset=UTF-8' },
        { id: 'xlsx', type: 'application/vnd.ms-excel' },
        { id: 'pptx', type: 'application/x-mspowerpoint' },
        { id: 'zip', type: 'application/x-zip-compressed' },
        { id: 'rar', type: 'application/x-zip-compressed' },
        { id: 'pdf', type: 'application/pdf' },
        { id: 'mp4', type: 'video/mp4' },
        { id: 'avi', type: 'video/avi' },
        { id: 'wmv', type: 'video/wmv' },
        { id: 'mp3', type: 'audio/mpeg' }
      ],
      action: process.env.BASE_API + process.env.SHOP_API_PREFIX + '/fileResource/upload'
    }
  },
  // 获取数据
  created() {
    this.$nextTick(e => {
      this.getList()
      // this.getDirectoryCount()
    })
  },
  computed: {
  },
  methods: {
    // 点击进入练习
    gotoTraining(index) {
      const id = this.tableData[index].id
      this.$router.push({ path: '/training/runtraining', query: { id }})
    },
    // 点击跳转子目录
    gotoNext(index) {
      if (this.oldParentId.length === 0) {
        this.oldParentId.push(0)
      }
      if (this.tableData[index].type !== 1) {
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
      this.$router.push({ name: 'trainingPath', query: { parentid: this.parentId }})
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
      this.$router.push({ name: 'trainingPath', query: { parentid: this.parentId }})
      this.oldParentId = []
      this.getList()
    },
    // 获取数据
    getList() {
      this.loading = true
      this.multipleSelection = []
      // const params = this.$formToParams(this.$refs.form)
      if (this.parentId === undefined) {
        this.parentId = 0
      }
      getTrainingPathInfo(this.parentId).then(e => {
        this.tableData = this.adornTableData(e)
        // for (var i = 0; i < this.tableData.length; i++) {
        //   Vue.set(this.tableData[i], 'checked', false)
        // }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    remoteValidate(newName, msg) {
      return (rule, value, callback) => {
        if (newName === '' || newName === undefined) {
          this.isEmpty = false
          callback(new Error(msg))
        } else {
          this.isEmpty = true
        }
      }
    },
    // 新建文件夹 显示弹窗
    createNewFile() {
      this.isShow = true
      this.isEmpty = true
      // getDirectoryCount(this.parentId, '新建文件夹').then(e => {
      //   const count = e.data.data
      //   if (count === 0) {
      //     this.form.newName = '新建文件夹'
      //   } else {
      //     this.form.newName = '新建文件夹(' + count + ')'
      //   }
      // })
    },
    // 取消创建文件夹
    cancelCreateFile() {
      this.isShow = false
      this.form.newName = '新建文件夹'
    },
    // 移动文件
    removeFile() {
      this.$refs.mov.open(this.multipleSelection)
    },
    // 重命名
    reName() {
      this.isReName = false
      this.reNaming = true
    },
    saveReName(id, rename) {
      if (rename.length < 0 || rename.length > 20) {
        this.$errmsg('目录名称为1-20个字符,请重试')
      }
      if (this.isEmpty) {
        editFolderName(id, rename).then(e => {
          if (e.data.success) {
            this.$message({
              type: 'success',
              message: '修改成功'
            })
            this.cancelReName()
          }
        })
      }
    },
    cancelReName() {
      this.isEmpty = false
      this.isReName = true
      this.reNaming = false
      this.activeIndex = ''
      this.getList()
    },
    shareResource() {
      if (this.multipleSelection.length > 1) {
        this.$message({
          type: 'info',
          message: '支持复制单条资源链接'
        })
      } else {
        this.$refs.share.open(this.multipleSelection)
      }
    },
    // 保存文件夹
    addFileResource() {
      const parentId = this.parentId
      const title = this.form.newName
      if (this.isEmpty) {
        addFolder(parentId, title).then(e => {
          if (e.data) {
            this.$message({
              type: 'success',
              message: '新建成功!'
            })
            this.isShow = false
            this.isEmpty = false
            this.getList()
          } else {
            this.$message({
              type: 'error',
              message: '新建失败!'
            })
          }
        })
      }
    },
    file_type(item) {
      if (item.type) {
        if (item.type === 'jpeg' || item.type === 'jpg' || item.type === 'png') {
          return item.path
        } else {
          return require('@/assets/images/files/file_' + item.type + '.png')
        }
      } else {
        return ''
      }
    },
    content(item) {
      return item.title + '.' + item.type
    },
    // 重置搜索条件
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.getList()
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
      } else {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          if (this.tableData[index].id === this.multipleSelection[i]) {
            this.multipleSelection.splice(i, 1)
            this.isReName = true
            this.reNaming = false
            if (this.tableData[index].type === '1') {
              this.type1Number--
            }
            if (this.type1Number === 0) {
              this.isDisabled = false
            }
          }
        }
      }
      // 判断所选文件数量，大于1不能执行重命名操作
      const le = this.multipleSelection.length
      if (le > 1) {
        this.isAlert = true
      } else {
        this.isAlert = false
        this.activeIndex = this.multipleSelection[0]
      }
    },
    // 下载文件
    // downLoad() {
    //   if (this.multipleSelection.length === 0) {
    //     this.$warnmsg('请选择要下载的资源')
    //     return
    //   } else if (this.multipleSelection.length > 1) {
    //     this.$warnmsg('不可以批量下载！')
    //     return
    //   } else {
    //     // const ids = this.multipleSelection.map(b => b.id)
    //     fecthOne(this.multipleSelection[0]).then(res => {
    //       const data = res.data.data
    //       if (data === null) {
    //         this.$message({
    //           type: 'warning',
    //           message: '文件夹不可下载！'
    //         })
    //       } else {
    //         for (let i = 0; i < this.downLoadType.length; i++) {
    //           if (this.downLoadType[i].id === data.type) {
    //           // 获取下载的文件类型
    //             this.fileType = this.downLoadType[i].type
    //           }
    //         }
    //         // 获取下载的文件名
    //         this.downLoadName = data.name + '.' + data.type
    //       }
    //     })
    //     setTimeout(() => {
    //       this.downloadFile(this.multipleSelection[0], this.downLoadName)
    //     }, 500)
    //   }
    // },
    // downloadFile(id, name) {
    //   downLoad(id).then(e => {
    //     const blob = new Blob([e.data], {
    //       type: this.fileType
    //     })
    //     const downloadUrl = window.URL.createObjectURL(blob)
    //     // 文件下载
    //     const link = document.createElement('a')
    //     link.href = downloadUrl
    //     link.setAttribute('download', name)
    //     document.body.appendChild(link)
    //     link.click()
    //     this.downloadUrl = ''
    //     this.downLoadName = null
    //     // const blobUrl = URL.createObjectURL(blob)
    //     // window.open(blobUrl)
    //     // window.location.href = blobUrl // 本窗口打开下载
    //     // window.open(blobUrl, '_blank')
    //   })
    // },
    // 获取复选框被选中的数据
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 批量删除选中的数据
    batchDeleted() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要删除的文件夹')
        return
      } else {
        // const ids = this.multipleSelection.map(b => b.id)
        // this.delete(this.multipleSelection)
        this.$cfm('确定删除文件夹或者资源吗？', response => {
          delFilesByIds(this.multipleSelection).then(e => {
            if (e.data) {
              this.$sucmsg('删除成功')
              this.getList()
            } else {
              this.$errmsg('删除失败！')
            }
          })
        })
      }
    },
    // 读取文件
    // handleAvatarSuccess(res, file) {
    //   const formData = res.data
    //   const le = this.oldParentId.length
    //   if (le !== 0) {
    //     formData.directoryId = this.oldParentId[le - 1]
    //   }
    //   addFileResource(formData).then(e => {
    //     if (e.data.data) {
    //       this.$message({
    //         type: 'success',
    //         message: '上传成功！'
    //       })
    //       this.loading = false
    //       this.getList()
    //     }
    //   })
    //   this.imageUrl = URL.createObjectURL(file.raw)
    // },
    // beforeAvatarUpload(file) {
    //   var id = file.name.lastIndexOf('.') + 1
    //   const resoourceType = file.name.substring(id, file.name.length)
    //   var isAllow = false
    //   for (let i = 0; i < this.downLoadType.length; i++) {
    //     if (resoourceType === this.downLoadType[i].id) {
    //       isAllow = true
    //       break
    //     }
    //   }
    //   const isLt2M = file.size / 1024 / 1024 < 200
    //   this.loading = true
    //   if (isAllow === false) {
    //     this.$message.error('不支持上传 ' + resoourceType + ' 类型的文件')
    //     this.loading = false
    //   }
    //   if (!isLt2M) {
    //     this.$message.error('上传文件大小不能超过 200MB!')
    //     this.loading = false
    //   }
    //   return isLt2M && isAllow
    // },
    // 处理返回的数据, 为每个元素添加一个checked属性
    adornTableData(data) {
      const folerdata = data.data.data[0]
      const filedata = data.data.data[1]
      var tabledata = []
      // filedata.forEach(item => {
      //   item.checked = false
      //   folderdata.push(item)
      // })
      for (let i = 0; i < folerdata.length; i++) {
        tabledata.push(folerdata[i])
      }
      for (let i = 0; i < filedata.length; i++) {
        tabledata.push(filedata[i])
      }
      tabledata.forEach(item => {
        item.checked = false
        if (!item.type) {
          item.type = 1
        }
      })
      return tabledata
    }
  }
}
</script>

<style scoped>
/* .profession-page{
min-height: 630px;
} */
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
  .profession-page .grid-content {
    border-radius: 4px;
    min-height: 36px;
    margin-bottom: 10px;
  }
  .profession-page .operation-button{
    display: inline-block;
    float: left;
  }
  .operation-search{
      float:right;
  }
  .profession-table{
      margin-top: 40px;
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
    margin-top: 5px;
    text-align: left;
    font-size: 10px;
  }
  .inp /deep/ .el-input__inner {
   padding:0px 0px 0px 0px;
   width: 80%;
}
  .content {
      padding-top: 30px;
      text-align: center;
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
      margin-left: 5px;
      /* width:7.5%; */
      background: rgba(246, 245, 245, 0.5);
      height: 130px;
      left: 0;
      right: 0;
      margin: auto;
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
      margin-left: 5px;
      border: 1px solid skyblue;
      border-radius: 5%;
      /* width:7.5%; */
      background: rgba(246, 245, 245, 0.5);
      height: 130px;
      left: 0;
      right: 0;
      margin: auto;
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
  max-width: 140px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow:ellipsis;
}
.emptyFolder{
  position: absolute ;
    left:50px;
    right: 0;
    margin: auto;
     margin-top: 15px;
}
.reNameInput{
  width: -10%
}
.item {
  position: relative;
  z-index: 100;
  /* margin-left: -45px; */
}
.left .el-tooltip__popper,
.right .el-tooltip__popper {
  padding: 8px 10px;
}
.maincontent {
  -webkit-box-shadow: 0 0 1px 0 rgba(0, 0, 0, 0);
  box-shadow: 0 0 1px 0 rgba(0, 0, 0, 0);
  margin: 0;
}
</style>
