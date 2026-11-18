<template>
    <div class="initial-page" 
      v-loading="loading" 
      :element-loading-text="loadingText"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)">
      <div class="initial-contentpage">
        <div class="initial-cntent">
          <el-col :span="12">
            <el-button class="initial-btn" @click="InitializationJSON"><a><span style="color:#409EFF">基础信息初始化打包</span></a></el-button>
          </el-col>
          <el-col :span="12">
            <el-button class="initial-btn" @click="importInitial"><a><span style="color:#409EFF">资源数据初始打包</span></a></el-button>
          </el-col>
        <div class="initial-cntent">
          <el-col :span="12">
            <el-upload
              class="operation-button"
              :action="action"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <el-button class="initial-btn" type="primary" icon="el-icon-upload">基础信息数据导入</el-button>
            </el-upload>
          </el-col>
          <el-col :span="12">
            <el-upload
              class="operation-button"
              :action="action2"
              :show-file-list="false"
              :on-success="handleSuccess"
              :before-upload="handleBefore">
              <el-button class="initial-btn" type="primary" icon="el-icon-upload">资源数据包导入</el-button>
            </el-upload>
            <!-- <input type="file" id="file" name="myfile" />
            <input type="button" @click="UpladFile()" value="上传" /> -->
          </el-col> 
        </div>
        </div>
      </div>
        <!-- <div>
            <el-input
            type="textarea"
            :rows="12"
            placeholder="请输入内容"
            v-model="content">
            </el-input>
        </div> -->
    </div>
</template>

<script>
import {
  decompressionZip,
  importInitialization,
  InitializationJSON,
  uploadInitialization
} from '@/api/file/file'
export default {
  data() {
    return {
      downLoadType: [
        { id: 'txt', type: 'text/plain' },
        { id: 'json', type: 'application/json' },
        { id: 'zip', type: 'application/x-zip-compressed' }
      ],
      loading: false,
      loadingText: '拼命加载中',
      content: null,
      action: '',
      action2: process.env.BASE_API + process.env.SHOP_API_PREFIX + '/fileResource/upload',
      isAllow: false,
      isLt2M: false
    }
  },
  watch: {
    content(newVal, oldVal) {
      const vm = this
      vm.loading = true
      vm.loadingText = '数据上传中...'
      if (newVal) {
        const data = vm.content
        const formData = data.substring(1, data.length - 1)
        // const json = JSON.stringify(formData)
        uploadInitialization((formData)).then(e => {
          if (e.data.success) {
            this.$message({
              type: 'success',
              message: '初始化数据上传成功！'
            })
            this.loading = false
          } else {
            this.$message({
              type: 'danger',
              message: '初始化数据上传失败！'
            })
            this.loading = false
          }
        })
      }
    }
  },
  methods: {
    // 资源图片初始化打包
    importInitial() {
      this.$confirm('您确定要进行资源打包吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        this.loading = true
        this.loadingText = '资源数据打包中...'
        importInitialization().then(e => {
          const blob = new Blob([e.data], {
            type: 'application/zip'
          })
          const blobUrl = URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = blobUrl
          link.setAttribute('download', '资源图片包')
          document.body.appendChild(link)
          link.click()
          this.loading = false
          document.body.removeChild(link)
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        })
      })
    },
    // 基础信息初始化打包
    InitializationJSON() {
      this.$confirm('您确定要进行基础信息打包吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(_ => {
        this.loading = true
        this.loadingText = '基础信息数据打包中...'
        InitializationJSON().then(e => {
          const blob = new Blob([e.data], {
            type: 'application/json'
          })
          const blobUrl = URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = blobUrl
          link.setAttribute('download', '初始化数据')
          document.body.appendChild(link)
          link.click()
          this.loading = false
          document.body.removeChild(link)
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消操作'
        })
      })
    },
    // 读取文件
    handleAvatarSuccess(res, file) {
      const formData = res.data
      this.loading = true
      this.loadingText = '基础信息导入中...'
      uploadInitialization(formData).then(e => {
        if (e.data.data) {
          this.$message({
            type: 'success',
            message: '基础信息导入成功！'
          })
          this.loading = false
        }
      })
    },
    beforeAvatarUpload(file) {
      var id = file.name.lastIndexOf('.') + 1
      const vm = this
      const resoourceType = file.name.substring(id, file.name.length)
      for (let i = 0; i < vm.downLoadType.length; i++) {
        if (resoourceType === vm.downLoadType[i].id) {
          vm.isAllow = true
          break
        }
      }
      vm.isLt2M = file.size / 1024 / 1024 < 10
      vm.loading = true
      if (vm.isAllow === false) {
        this.$message.error('不支持上传 ' + resoourceType + ' 类型的文件')
        vm.loading = false
      }
      if (!vm.isLt2M) {
        this.$message.error('上传文件大小不能超过 10MB!')
        vm.loading = false
      }
      if (vm.isLt2M && vm.isAllow) {
      // 文件类型
        if (file.length === 0) {
          return
        }
        const reader = new FileReader()
        if (typeof FileReader === 'undefined') {
          this.$message({
            type: 'info',
            message: '您的浏览器不支持FileReader接口'
          })
          return
        }
        reader.readAsText(file)
        reader.onload = e => {
        //   this.$set(this.textarea, 'content', e.target.result)
          vm.content = e.target.result
          console.log(1)
        }
      }
      return vm.isLt2M && vm.isAllow
    },
    handleBefore(file) {
      var id = file.name.lastIndexOf('.') + 1
      const vm = this
      const resoourceType = file.name.substring(id, file.name.length)
      for (let i = 0; i < vm.downLoadType.length; i++) {
        if (resoourceType === vm.downLoadType[i].id) {
          vm.isAllow = true
          break
        }
      }
      vm.isLt2M = file.size / 1024 / 1024 < 30
      vm.loading = true
      if (vm.isAllow === false) {
        this.$message.error('不支持上传 ' + resoourceType + ' 类型的文件')
        vm.loading = false
      }
      if (!vm.isLt2M) {
        this.$message.error('上传文件大小不能超过 30MB!')
        vm.loading = false
      }
      return vm.isLt2M && vm.isAllow
    },
    handleSuccess(res, file) {
      debugger
      const path = res.data.path
      this.loading = true
      this.loadingText = '资源导入中...'
      decompressionZip(path).then(e => {
        if (e.data.success) {
          this.$message({
            type: 'success',
            message: '资源导入成功！'
          })
          this.$router.push({ path: '/myfileresource/fileResource' })
          this.loading = false
        }
      })
    }
  }
}
</script>

<style  lang="scss" scoped>
  *{
    margin: 0;
    padding: 0;
  }
  .initial-page {
    height: 860px;
    min-height: 640px;
  }
  .initial-contentpage {
    position: absolute;
    top: 50%;
    left: 55%;
    transform: translate(-50%, -50%);
    width: 30%;
    height: 30%;
  }
  .initial-cntent {
    width: 100%;
    // margin-top: 20px;
    // height: 200px;
    // background-image: url('../../assets/images/course/add_bg.png');
    // background-repeat: no-repeat;
  }
  .initial-btn {
    margin-top: 20px;
    // background-color: #327CC8;
  }
  /deep/ .el-button--small, .el-button--small.is-round {
    padding: 43px 42px;
    border-radius: 5%;
  }
</style>
