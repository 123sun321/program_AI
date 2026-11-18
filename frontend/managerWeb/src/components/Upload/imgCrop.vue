<template>
  <el-dialog width="40%" title="裁剪图片" :visible.sync="innerVisible" append-to-body id="crop-main">
    <div class="crop-container">
      <div class="left">
        <img id="image" :src="base64Url">
      </div>
      <div class="right">
        <ul class="ratio">
          <li>
            <el-radio v-model="aspectRatio" label="old">原图比例</el-radio>           
          </li>
          <li>
             <el-radio v-model="aspectRatio" label="1">1:1</el-radio>
          </li>
          <li>
             <el-radio v-model="aspectRatio" label="3/4">3:4</el-radio>
          </li>
          <li>
             <el-radio v-model="aspectRatio" label="4/3">4:3</el-radio>
          </li>
          <li>
             <el-radio v-model="aspectRatio" label="16/9">16:9</el-radio>
          </li>
          <li>
             <el-radio v-model="aspectRatio" label="free">自由比例</el-radio>
          </li>
        </ul>
      </div>
    </div>
    <div slot="footer">
      <el-button type="primary" size="mini" @click="noCrop">不裁剪</el-button>
      <el-button type="default" size="mini" @click="crop">裁剪</el-button>
      <el-button type="default" size="mini" @click=" innerVisible=false ">取消</el-button>
    </div>
  </el-dialog>
</template>
<script>
  import 'cropperjs/dist/cropper.css'
  import Cropper from 'cropperjs'
  import { getToken } from '@/utils/auth'
  import axios from 'axios'

  import requestByAxios from '@/utils/request'
  import { Loading } from 'element-ui'
  
  export default {
    data() {
      return {
        cropperInstance: undefined,
        odiv: undefined,
        base64Url: '',
        innerVisible: false,
        aspectRatio: 'old',
        thisOpenHadBuild: false,
        loadingInstance: undefined,
        file: undefined,
        cropblob: undefined,
        host: process.env.BASE_API,
        opt: {
          aspectRatio: 1,
          viewMode: 1,
          background: true,
          zoomable: true
        }
      }
    },
    updated() {
      const odiv = document.getElementById('image')
      if (odiv.attributes.src.value !== '' && this.innerVisible && !this.thisOpenHadBuild) {
        this.reBuild()
      }
    },
    methods: {
      crop() {
        this.openLoading()
        this.changeLoadingText('上传中')
        this.cropperInstance.getCroppedCanvas().toBlob(blob => {
          const formdata = new FormData()
          formdata.append('file', blob, 'file.' + blob.type.split('/')[1])
          const config = {
            headers: { 'Content-Type': 'multipart/form-data' }
          }
          this.cropblob = blob
          requestByAxios.post('^/AppFile/file/upload', formdata, config).then(res_upload => {
            const path = res_upload.data.data
            this.cropSuccess(path)
          }).catch(res_upload => {
            this.closeLoading()
          })
        })
      },
      cropSuccess(path) {
        this.changeLoadingText('同步中')
        const extension = this.getExtension(this.file.name)
        const nfilename = this.file.name.replace('.' + extension, '') + '_crop.' + this.cropblob.type.split('/')[1]
        requestByAxios.post('^/AppFile/basefile/add', {
          name: nfilename,
          type: 4,
          path: path,
          size: this.cropblob.size,
          extension: this.cropblob.type,
          groupId: null
        }).then(res_add => {
          this.file = res_add.data
          this.closeLoading()
          this.$notify({
            message: '同步成功',
            type: 'success'
          })
          this.save()
        }).catch(res_add => {
          this.closeLoading()
        })
      },
      noCrop() {
        this.save()
      },
      save() {
        this.innerVisible = false
        this.$emit('success', this.file)
      },
  
      open(file) {
        this.file = file
        this.url = file.path
        const canCropImgType = ['image/jpeg', 'image/png']
        if (canCropImgType.indexOf(file.extension) === -1) {
          this.$confirm('该图片无法裁剪是否直接使用?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(e => {
            this.noCrop()
          }).catch(e => {
          })
          return
        }
        this.innerVisible = true
        this.getBase64Data()
      },
      getBase64Data() {
        const url = this.url.replace('/upload', '')
        const params = {
          url: this.host + '/AppFile/file/down',
          method: 'post',
          params: {
            path: url
          },
          responseType: 'blob'
        }
        if (this.host) {
          params.headers = {
            Authorization: `${getToken()}`
          }
        }
        axios(params).then(e => {
          // const data = btoa(e.data)
          this.base64Url = URL.createObjectURL(e.data)
        }).catch(e => {
          console.log(e)
        })
      },
      reBuild() {
        this.odiv = document.getElementById('image')
        if (this.cropperInstance !== undefined) {
          this.cropperInstance.destroy()
        }
        const option = this.getOption()
        this.cropperInstance = new Cropper(this.odiv, option)
        this.thisOpenHadBuild = true
      },
      getOption() {
        this.opt.aspectRatio = this.getRatio()
        return this.opt
      },
      getRatio() {
        let aspectRatio
        if (this.aspectRatio === 'old') {
          const odiv = document.getElementById('image')
          const width = odiv.width
          const height = odiv.height
          aspectRatio = width / height
        } else if (this.aspectRatio === 'free') {
          aspectRatio = NaN
        } else {
          aspectRatio = parseFloat(this.aspectRatio)
        }
        return aspectRatio
      },
      openLoading(text) {
        this.loadingInstance = Loading.service({
          fullscreen: true,
          target: document.getElementById('crop-mian')
        })
      },
      closeLoading() {
        this.loadingInstance.close()
      },
      changeLoadingText(text) {
        this.loadingInstance.setText(text)
      },
      getExtension(name) {
        const splits = name.split('.')
        return splits[splits.length - 1]
      }
  
    },
    watch: {
      innerVisible(nv, ov) {
        if (!nv) {
          this.base64Url = ''
          this.aspectRatio = 'old'
          this.thisOpenHadBuild = false
        }
      },
      aspectRatio(nv, ov) {
        this.cropperInstance.setAspectRatio(this.getRatio())
      }
    }
  }
  // public method for decoding
  
</script>
<style lang="scss" scoped>
  img {
    max-width: 100%;
    /* This rule is very important, please do not ignore this! */
  }
  .crop-container{
    display: flex;
    flex-flow: row nowrap;

    .left{
      flex: 1;
      
    }   
    .right{
      width: 30%;
    }
  }
  .ratio{
    list-style-type: none;
    li{
      height: 30px;
      line-height: 30px;
    }        
  }
  
  
</style>


