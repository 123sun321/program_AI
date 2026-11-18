/*
 * @Author: 黄鹏举
 * @Date: 2018-12-04 17:52:38 
 * @Last Modified time: 2018-12-04 19:26:11
 */
<template>
    <el-upload class="avatar-uploader"
        :action="action"
        :show-file-list="false"
        :on-error="uploadError"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload">
        <!-- <img v-if="logoForm.imageUrl" :src="host+logoForm.imageUrl" class="avatar"> -->
        <img v-if="img" :src="img" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
     </el-upload> 
</template>

<script>
/**
 * @description 上传图片
 * @author【黄鹏举】
 * @since 2018/12/4
 * @version 1.0
 */
export default {
  // 父组件参数
  props: ['img'],
  data() {
    return {
      logoForm: { // 上传图片
        imageUrl: ''
      }
    }
  },
  // 上传图片使用 用来监控自己定义的变量
  computed: {
    action() {
      return process.env.BASE_API + '/AppFile/file/upload2'
    },

    host() {
      return process.env.BASE_API
    }
  },
  methods: {
    // 限制用户上传的图片格式或者大小。 file上传的文件
    beforeAvatarUpload(file) {
      const isJPG = (file.type === 'image/jpeg') || (file.type === 'image/png')
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error('上传logo图片只能是 JPG或者PNG!')
      }
      if (!isLt2M) {
        this.$message.error('上传logo图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    //  文件上传成功时的钩子 res返回的数据 file上传的文件
    handleAvatarSuccess(res, file) {
      this.$emit('imgurl', res.data)
    },
    //  文件上传失败时
    uploadError() {
      this.$message.error('上传logo失败!')
    }
  }
}
</script>

<style>
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    /* margin:80px; */
  }
 .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
   .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
   .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
