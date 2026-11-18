<template>
    <div style="margin:20px;">
        <el-row style="margin:20px 0px;">
            <el-col :span="10">
                头像预览：
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="4">
                 <img class="userPicMax" :src="avatar"/>
            </el-col>
        </el-row>
        <el-row style="margin:20px 0px;">
            <el-col :span="10">
               <el-button type="primary"  size="medium" style="width:130px;" @click="imagecropperShow=true">重新设置</el-button>
            </el-col>
        </el-row>
        <image-cropper
      v-show="imagecropperShow"
      :width="300"
      :height="300"
      :key="imagecropperKey"
      url="^/account/changehead"
      lang-type="zh"
      :noRotate="false"
      @close="close"
      @crop-upload-success="cropSuccess"/>
    </div>
</template>

<script>
/**
 * @file 用户个人资料头像修改组件
 * @author 张向宇
 * @copyright zhengzhouchuanghzi
 * @since 2025/01/10
 */
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'
import { mapGetters } from 'vuex'
import { updateUserPersonInfo } from '@/api/person/userInfo'
export default {
  components: {
    ImageCropper,
    PanThumb
  },
  data() {
    return {
      dialogVisible: false,
      imagecropperShow: false,
      imagecropperKey: 0
    }
  },
  computed: {
    ...mapGetters([
      'avatar'
    ])
  },
  props: {

  },
  created() {
  },
  methods: {
    cropSuccess(resData) {
      this.imagecropperShow = false
      this.imagecropperKey = this.imagecropperKey + 1
      this.image = resData.newFileName
      // 更新vuex
      this.$store.commit('SET_AVATAR', resData.newFileName)
      // 保存到数据库
      updateUserPersonInfo({ avatar: resData.newFileName }).then(response => {
        this.$message({
          message: '头像更新成功',
          type: 'success'
        })
      }).catch(error => {
        this.$message({
          message: '头像更新失败',
          type: 'error'
        })
      })
    },
    close() {
      this.imagecropperShow = false
    }
  }
}
</script>
<style scoped>
 .userInfo .el-row{margin:20px 0px;}
 .userPicMax{
     width:200px;
  height:200px;
  border-radius: 50%;
  border: solid 1px #c3c4c7;
  }
   .userPicNomal{
     width:100px;
  height:100px;
  border-radius: 50%;
  border: solid 1px #c3c4c7;
  }
     .userPicMin{
     width:50px;
  height:50px;
  border-radius: 50%;
  border: solid 1px #c3c4c7;
  }

</style>
