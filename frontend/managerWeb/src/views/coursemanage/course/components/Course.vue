<template>
  <el-form :model="entity" ref="form" :inline="line" label-width="100px">
    <slot name="tip"></slot>
       <el-form-item prop="category"  label="课程类别:" :rules=" { required: true, message: '请选择课程类别', trigger: 'change' } " >
          <el-select v-model="entity.category" placeholder="请选择">
            <el-option  v-for="(item,index) in coursecategory"  :key="index" :label="item.name" :value="item.code">
            </el-option>
          </el-select>
        </el-form-item>
    <el-form-item prop="name" label="名称:" :rules="[{validator:remoteValidate('name','已存在同名名称')},{ required: true, message: '请填写名称', trigger: 'blur' },{ min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: ['blur', 'change']}]">
      <el-input v-model="entity.name" placeholder="请填写名称"></el-input>
    </el-form-item>
    <el-form-item prop="complexity" label="难易程度:" :rules="[{required:true,message:'难易程度不能为空'}]">
        <el-rate v-model="entity.complexity" style="margin-top:6px;"></el-rate>
    </el-form-item>
    <el-form-item prop='type' label="课程类型:">
      <el-select  v-model="entity.type" disabled>
        <el-option value='0' label ="公开课"></el-option>
        <el-option value='1' label="教师课程"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item   prop='author' label="作者:">
      <template>
           <el-input type="text"  v-model="entity.author" placeholder="请填写作者"></el-input>
      </template>
    </el-form-item>
     <el-form-item   prop='recommendDegree' label="课程推荐度:">
      <template>
           <el-input type="number" onkeypress="return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )"  v-model="entity.recommendDegree" placeholder="请填写推荐度"></el-input>
      </template>
    </el-form-item>

    <el-form-item prop="summary" label="课程简介:">
      <el-input type="textarea"  :autosize="{ minRows: 3}" v-model="entity.summary" placeholder="请填写简介"></el-input>
    </el-form-item>
    <el-form-item prop="tags" label="课程标签:" :rules="[{required:true,message:'课程标签不能为空'}]">
      <el-select v-model="entity.tags" :multiple-limit='3' multiple allow-create filterable default-first-option remote reserve-keyword placeholder="请输入实验标签，最多3个"
        :remote-method="remoteMethod"
        :loading="loading" style="width:100%;">
        <el-option v-for="item in tagsOption" :key="item.value" :label="item.label" :value="item.value"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item prop="logo" label="课程logo:" :rules="[{ required: true, message: '请选择logo', trigger: 'blur' }]">
      <el-upload
            class="avatar-uploader"
            :action="action"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload" >
        <img v-if="entity.logo" :src="entity.logo" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
       <div>上传图片宽*高需小于380*240px</div>
    </el-form-item>
   </el-form>
</template>
<script>
/**
 * @file 课程基本form组件
 * @author 刘彦杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/16
 */
import { chooseList } from '@/api/base/label'
import { fetchItems } from '@/api/dictionary'
import {
  form
} from '@/mixins/form'
import {
  fetchOne,
  remoteValid
} from '@/api/coursemanage/course'
export default {
  mixins: [form],
  data() {
    return {
      logo: '',
      coursecategory: [],
      loading: false,
      tagsOption: [],
      input: '',
      action: process.env.BASE_API + '/AppFile/file/upload2'
    }
  },
  methods: {
    init() {
      fetchItems(null, 'course_category').then(e => {
        this.coursecategory = e.data.data
      })
    },
    open() {
      this.visible = true
    },
    load() {
      if (this.isEdit && this.eid) {
        fetchOne(this.eid).then(e => {
          this.entity = e.data.data
          this.entity.type = this.entity.type + ''
          if (this.entity.tags === null || this.entity.tags === undefined) {
            this.entity.tags = []
          }
        })
      } else {
        this.entity.type = '0'
      }
    },
    remoteValidate(name, msg) {
      return (rule, value, callback) => {
        const id = this.entity.id
        const userid = this.entity.userId
        remoteValid(id, userid, name, value).then(e => {
          if (e.data.data) {
            callback()
          } else {
            callback(new Error(msg))
          }
        })
      }
    },
    remoteMethod(query) {
      var vm = this
      if (query !== '') {
        this.loading = true
        this.tagsOption = []
        chooseList(query).then(e => {
          e.data.data.forEach(element => {
            vm.tagsOption.push({ value: element.name, label: element.name })
          })
          this.loading = false
        })
      } else {
        this.tagsOption = []
      }
    },
    handleAvatarSuccess(res, file) {
      var vm = this
      this.$set(vm.entity, 'logo', res.data)
    },
    beforeAvatarUpload(file) {
      const isJPG = (file.type === 'image/jpeg' || file.type === 'image/png')
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG、PNG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    }

  }
}

</script>

<style>
  .el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }

</style>
