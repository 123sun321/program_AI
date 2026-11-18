<template>
  <div class="maincontent profession-page">
      <div class="grid-content bg-purple-dark">
        <el-form :inline="true" :model="form" ref="form" class="demo-form-inline">
          <div class="operation-button">
            <el-upload
              class="operation-button"
              :action="action"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <el-button type="primary" icon="el-icon-upload">上传</el-button>
            </el-upload>&nbsp;&nbsp;
            <el-button @click="createNewFile"><img style="width:12px;" src="../../../assets/images/files/build.png">&nbsp;<span style="color:#409EFF">新建文件夹</span></el-button>
            <el-button @click="downLoad"><a><img style="width:15px;" src="../../../assets/images/files/download.png">&nbsp;<span style="color:#409EFF">下载</span></a></el-button>
            <div class="level-button">
              <el-button v-if="this.oldParentId.length!==0" type="text" size="mini" @click="gotoBack()"><span style="color:#409EFF;">返回上一级</span></el-button>
              <span v-if="this.oldParentId.length!==0" style="color:#409EFF;">|</span>
              <el-button type="text" size="mini" @click="gotoHome()"><span style="color:#409EFF;">全部文件</span></el-button>
            </div>
            <!-- <el-breadcrumb separator-class="el-icon-arrow-right" class="myBreadcrumb">
              <el-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index" :to="{ meta: item.path, query: {path: item.query.path} }">{{item.title}}</el-breadcrumb-item>
            </el-breadcrumb> -->
          </div>
          <div class="file-button">
             &nbsp;&nbsp;&nbsp;<el-button  v-if="this.multipleSelection.length!== 0" @click="batchDeleted"><img src="../../../assets/images/files/del.png">&nbsp;&nbsp;<span style="color:#409EFF;">删除</span></el-button>
             <el-button v-if="this.multipleSelection.length!== 0" @click="removeFile"><img src="../../../assets/images/files/move.png">&nbsp;<span style="color:#409EFF;width:140px;">移动到</span></el-button>
             <el-button v-if="this.multipleSelection.length!== 0" :disabled="isEditing" @click="reName"><img src="../../../assets/images/files/rename.png">&nbsp;<span style="color:#409EFF;width:140px;">重命名</span></el-button>
             <el-button v-if="this.multipleSelection.length!== 0" :disabled="isDisabled" @click="shareResource"><img src="../../../assets/images/files/connect.png">&nbsp;<span style="color:#409EFF;width:140px;">复制链接</span></el-button>
          </div>
        </el-form>
      </div>
      <div v-if="this.tableData.length === 0">
           <!-- 新建文件夹 -->
           <div class="emptyFolder">
            <div v-show="isShow" ref="form" :model="form">
              <el-col :xs="8" :sm="6" :md="4" :lg="2" :span="2" style="position: relative;">
                <div class="course-box inp" style="margin-top:30px;">
                <span><img src="../../../assets/images/files/file.png"></span><br>
                <span>
                  <el-form :inline="true">
                    <el-form-item prop="name" :rules="[{validator:remoteValidate(form.name,'文件夹名不可为空！')}]">
                      <el-input v-model="form.name" size="mini" class="inputFile"></el-input>
                    </el-form-item>
                  </el-form>
                  <span style="position:absolute;margin-left:66px;margin-top:-40%;">
                    <img class="cancel" @click="addFileResource" src="../../../assets/images/files/yes2.png">
                    <img class="cancel" style="top:8px;"  @click="cancelCreateFile" src="../../../assets/images/files/no.png">
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
      <div v-else-if="this.tableData.length !== 0" class="profession-table" v-loading="loading"
            element-loading-text="文件上传中..."
            element-loading-spinner="el-icon-loading" >
         <el-row>
           <!-- 新建文件夹 -->
           <div v-show="isShow" ref="form" :model="form">
            <el-col :xs="8" :sm="6" :md="4" :lg="2"  :span="2" style="position: relative;">
               <div class="course-box inp" style="margin-top:30px;">
              <span><img src="../../../assets/images/files/file.png"></span><br>
              <span>
               <el-form :inline="true">
                <el-form-item prop="name" :rules="[
                {validator:remoteValidate('name','已存在同名文件夹名！')},
                { required: true, message: '请填写文件夹名称', trigger: 'blur' },
                { min: 1, max: 20, message: '长度在 1 到 50 个字符', trigger: ['blur', 'change'] }]">
                  <el-input v-model="form.name" size="mini" class="inputFile"></el-input>
                </el-form-item>
               </el-form>
                <span style="position:absolute;margin-left:20px;margin-top:-40%;">
                  <img class="cancel" @click="addFileResource" src="../../../assets/images/files/yes2.png">
                  <img class="cancel" style="top:8px;"  @click="cancelCreateFile" src="../../../assets/images/files/no.png">
              </span>
              </span>
              </div>
            </el-col>
           </div>
           <!-- 文件夹显示 -->
           <div class="file-page" ref="form" :model="form">
             <el-col :xs="8" :sm="6" :md="4" :lg="2"  :span="2" style="position: relative;" v-for="(item,index) in tableData" :key="index">
                <div v-if="item.checked === false" class="course-box1">
                    <div @click="selectFile(index)">
                        <span><img src="../../../assets/images/files/select.png"></span>
                    </div>
                    <div v-if="item.type === '1'" style="position:absolute;margin-left:31px;width:60px;height:50px;opacity:0">
                      <p @click="gotoNext(index)">123</p>
                    </div>
                </div>
                <div v-else-if="item.checked === true" class="course-box2">
                    <div @click="selectFile(index)">
                        <span><img src="../../../assets/images/files/selected.png"></span>
                    </div>
                </div>
                <div class="content">
                    <span><img style="max-width:60px;height:55px;" :src="file_type(item)"></span>
                    <br>
                    <el-tooltip class="item" effect="light" placement="bottom">
                    <!-- <el-button style="" >上左</el-button> -->
                     <div v-if="item.type === '1'" slot="content">
                       {{item.name}}<br/>
                       修改日期：{{ item.creationTime }}
                     </div>
                     <div v-else-if="item.type !== '1'" slot="content">
                       {{item.name}}.{{item.type}}<br/>
                       大小：{{item.size}}<br/>
                       上传日期：{{ item.creationTime }}
                     </div>
                        <span v-show="isReName" v-if="item.id === activeIndex" class="contentName">{{ item.name }}</span>
                        <span  v-else-if="item.id !== activeIndex" class="contentName">{{ item.name }}</span>
                    </el-tooltip>
                    <span v-show="reNaming" v-if="item.id === activeIndex">
                      <template>
                        <el-form>
                          <el-form-item prop="item.name" :rules="[
                          {validator:remoteValidateSet(item.id,'name',item.name,'已存在同名文件夹名！')},
                          { required: true, message: '请填写文件夹名称', trigger: 'blur' },
                          { min: 1, max: 20, message: '长度在 1 到 50 个字符', trigger: ['blur', 'change'] }]">
                           <el-input style="width:60%;margin-left:-35px;background-color:#FFFFFF" v-model="item.name"></el-input>
                          </el-form-item>
                        </el-form>
                      </template>
                      <span style="position:absolute;margin-left:30px;margin-top:-55px;">
                        <img class="cancel" @click="saveReName(item.id,item.name)" src="../../../assets/images/files/yes2.png">
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
import {
  form
} from '@/mixins/form'
import EmptyFolder from './emptyFolder'
import page from '@/components/page'
import remove from './remove'
import share from './share'
import Vue from 'vue'
import {
  downLoad,
  remoteValid, // 远程验证文件夹
  getDirectoryCount,
  reName, // 文件重命名
  fecthOne, // 获取单条数据
  delBatchesDirectory,
  getDirectoryWithFileList, // 获取文件夹列表
  addDirectory, // 新建文件夹
  addFileResource // 添加资源（上传文件）
} from '@/api/file/file'
export default {
  components: {
    page,
    remove,
    share,
    EmptyFolder
  },
  mixins: [form],
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
        name: '新建文件夹'
      },
      breadcrumbList: [{ title: '全部文件', meta: this.$route.path, query: { path: '/' }, id: 0 }],
      parentId: this.$route.query.parentid, // 父目录id
      oldParentId: [],
      isEmpty: false,
      isDisabled: false,
      activeIndex: '',
      isShow: false, // 是否选择新建文件夹
      emptyShow: true, // 文件夹是否为空
      isEditing: false, // 是否修改名字
      isReName: true, // 是否重命名
      reNaming: false, // 重命名ing
      tableData: [], // 资源信息
      tableData1: [],
      loading: false, // 加载
      currentParentId: 0,
      multipleSelection: [], // 获取复选框被选中的
      fileType: null,
      type1Number: 0, // 选择文件夹的个数
      downLoadName: null, // 下载文件的文件名
      downLoadType: [
        { id: 'jpeg', type: 'image/jpeg' },
        { id: 'jpg', type: 'image/jpg' },
        { id: 'png', type: 'image/png' },
        { id: 'js', type: 'text/javascript' },
        { id: 'css', type: 'text/css' },
        { id: 'scss', type: 'text/css' },
        { id: 'otf', type: 'text/otf' },
        { id: 'eot', type: 'text/eot' },
        { id: 'svg', type: 'text/svg' },
        { id: 'ttf', type: 'text/ttf' },
        { id: 'woff', type: 'text/woff' },
        { id: 'woff2', type: 'text/woff2' }
      ],
      action: process.env.BASE_API + process.env.SHOP_API_PREFIX + '/fileResource/upload'
    }
  },
  // 获取数据
  created() {
    this.$nextTick(e => {
      this.getList()
    })
  },
  methods: {
    // 点击跳转子目录
    gotoNext(index) {
      const vm = this
      if (vm.oldParentId.length === 0) {
        vm.oldParentId.push(0)
      }
      if (vm.tableData[index].type !== '1') {
        const le = vm.oldParentId.length
        if (vm.oldParentId[le - 1] === vm.parentId) {
          if (vm.parentId === 0) {
            vm.oldParentId = []
          }
          // vm.isDisabled = false
          vm.getList()
        } else { vm.oldParentId.splice(le - 1, 1) }
      } else {
        vm.oldParentId.push(vm.tableData[index].id)
        vm.parentId = vm.tableData[index].id
        // vm.isDisabled = false
      }
      vm.$router.push({ name: 'fileResource', query: { parentid: vm.parentId }})
      vm.getList()
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
      this.$router.push({ name: 'fileResource', query: { parentid: this.parentId }})
      this.oldParentId = []
      this.getList()
    },
    // 获取数据
    getList() {
      const vm = this
      // this.loading = true
      vm.multipleSelection = []
      vm.isDisabled = false
      // const params = this.$formToParams(this.$refs.form)
      if (vm.parentId === undefined) {
        vm.parentId = 0
      }
      getDirectoryWithFileList(vm.parentId).then(e => {
        vm.tableData = e.data.data
        for (var i = 0; i < vm.tableData.length; i++) {
          Vue.set(vm.tableData[i], 'checked', false)
        }
        vm.loading = false
      }).catch(() => {
        vm.loading = false
      })
    },
    // 新建文件夹 显示弹窗
    createNewFile() {
      getDirectoryCount(this.parentId, '新建文件夹').then(e => {
        const count = e.data.data
        if (count === 0) {
          this.form.name = '新建文件夹'
        } else {
          this.form.name = '新建文件夹(' + count + ')'
        }
        this.isShow = true
      })
    },
    remoteValidate(name, msg) {
      const vm = this
      return (rule, value, callback) => {
        if (vm.form.name === '') {
          callback()
        } else if (vm.form.name.length > 50) {
          callback('长度在 1 到 50 个字符')
        } else {
          const id = null
          let parentId = null
          if (vm.parentId !== 0) {
            parentId = vm.parentId
          }
          remoteValid(id, parentId, name, vm.form.name).then(e => {
            console.log(e.data.data)
            if (e.data.data) {
              this.isEmpty = true
            // callback()
            } else {
              this.isEmpty = false
              callback(new Error(msg))
            }
          })
        }
        // if (newName === '') {
        //   this.isEmpty = false
        //   callback(new Error(msg))
        // } else {
        //   this.isEmpty = true
        // }
      }
    },
    remoteValidateSet(id, name, vlaue, msg) {
      const vm = this
      return (rule, value, callback) => {
        if (vlaue === '') {
          callback()
        } else if (vlaue.length > 50) {
          callback('长度在 1 到 50 个字符')
        } else {
          let parentId = null
          if (vm.parentId !== 0) {
            parentId = vm.parentId
          }
          debugger
          remoteValid(id, parentId, name, vlaue).then(e => {
            console.log(e.data.data)
            if (e.data.data) {
              this.isEmpty = true
            // callback()
            } else {
              this.isEmpty = false
              callback(new Error(msg))
            }
          })
        }
      }
    },
    // 取消创建文件夹
    cancelCreateFile() {
      this.isShow = false
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
      if (this.isEmpty) {
        reName(id, rename).then(e => {
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
      const params = {
        parentId: this.parentId,
        name: this.form.name
      }
      if (this.isEmpty) {
        addDirectory(params).then(e => {
          if (e.data) {
            this.$message({
              type: 'success',
              message: '新建成功!'
            })
            this.isShow = false
            this.isEmpty = false
            this.form.name = '新建文件夹'
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
      if (item.type === 'jpeg' || item.type === 'jpg' || item.type === 'png') {
        return item.path
      } else {
        return require('@/assets/images/files/file_' + item.type + '.png')
      }
    },
    content(item) {
      return item.name + '.' + item.type
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
        this.isEditing = true
      } else {
        this.isEditing = false
        this.activeIndex = this.multipleSelection[0]
      }
    },
    // 下载文件
    downLoad() {
      if (this.multipleSelection.length === 0) {
        this.$warnmsg('请选择要下载的资源')
        return
      } else if (this.multipleSelection.length > 1) {
        this.$warnmsg('不可以批量下载！')
        return
      } else {
        // const ids = this.multipleSelection.map(b => b.id)
        fecthOne(this.multipleSelection[0]).then(res => {
          const data = res.data.data
          if (data === null) {
            this.$message({
              type: 'warning',
              message: '文件夹不可下载！'
            })
          } else {
            for (let i = 0; i < this.downLoadType.length; i++) {
              if (this.downLoadType[i].id === data.type) {
              // 获取下载的文件类型
                this.fileType = this.downLoadType[i].type
              }
            }
            // 获取下载的文件名
            this.downLoadName = data.name + '.' + data.type
          }
        })
        setTimeout(() => {
          this.downloadFile(this.multipleSelection[0], this.downLoadName)
        }, 500)
      }
    },
    downloadFile(id, name) {
      downLoad(id).then(e => {
        const blob = new Blob([e.data], {
          type: this.fileType
        })
        const downloadUrl = window.URL.createObjectURL(blob)
        // 文件下载
        const link = document.createElement('a')
        link.href = downloadUrl
        link.setAttribute('download', name)
        document.body.appendChild(link)
        link.click()
        this.downloadUrl = ''
        this.downLoadName = null
        // const blobUrl = URL.createObjectURL(blob)
        // window.open(blobUrl)
        // window.location.href = blobUrl // 本窗口打开下载
        // window.open(blobUrl, '_blank')
      })
    },
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
          delBatchesDirectory(this.multipleSelection).then(e => {
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
    handleAvatarSuccess(res, file) {
      const formData = res.data
      const le = this.oldParentId.length
      if (le !== 0) {
        formData.directoryId = this.oldParentId[le - 1]
      }
      addFileResource(formData).then(e => {
        if (e.data.data) {
          this.$message({
            type: 'success',
            message: '上传成功！'
          })
          this.loading = false
          this.getList()
        }
      })
      this.imageUrl = URL.createObjectURL(file.raw)
    },
    beforeAvatarUpload(file) {
      var id = file.name.lastIndexOf('.') + 1
      const resoourceType = file.name.substring(id, file.name.length)
      var isAllow = false
      for (let i = 0; i < this.downLoadType.length; i++) {
        if (resoourceType === this.downLoadType[i].id) {
          isAllow = true
          break
        }
      }
      const isLt2M = file.size / 1024 / 1024 < 200
      this.loading = true
      if (isAllow === false) {
        this.$message.error('不支持上传 ' + resoourceType + ' 类型的文件')
        this.loading = false
      }
      if (!isLt2M) {
        this.$message.error('上传文件大小不能超过 200MB!')
        this.loading = false
      }
      return isLt2M && isAllow
    }
  }
}
</script>

<style scoped>
.profession-page{
min-height: 630px;
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
      margin-left: 5px;
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
  max-width: 140px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow:ellipsis;
}
.emptyFolder{
  position: absolute ;
    left: 150px;
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
</style>
