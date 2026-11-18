<template>
<div class="maincontent">
    <el-form :model="entity" ref="form" label-width="100px">
        <el-row>
          <el-col :offset="6" :span="8">
              <el-form-item prop="type" label="实验类型:" v-if="!isTeacher" :rules="[{required:true,message:'实验类型不能为空'}]">
                <el-select v-model="entity.type" :disabled="courseid !== undefined">
                    <el-option value="0" label="课程实验"></el-option>
                    <el-option value="1" label="练习实验"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="courseId" v-if="entity.type === '0'" label="所属课程:" :rules="[{required:true,message:'所属课程不能为空'}]">
                <el-select v-model="entity.courseId" filterable :disabled="courseid !== undefined">
                    <el-option v-for="(course,index) in courseList" :key="index" :value="course.id" :label="course.name"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="name" label="实验名称:" :rules="[{required:true,message:'实验名称不能为空'},{ min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }]">
              <el-input type="text" v-model="entity.name" placeholder="请填写实验名称"></el-input>
            </el-form-item>
            <!-- 判断是否教师用户 -->
            <el-form-item prop="author" label="作者:" v-if="!isTeacher" :rules="[{required:true,message:'作者不能为空'},{ min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur' }]">
              <el-input type="text" v-model="entity.author" placeholder="请填写作者"></el-input>
            </el-form-item>
            <el-form-item prop="category" label="技术类型:" :rules="[{required:true,message:'技术类型不能为空'}]">
                <el-select v-model="entity.category">
                    <el-option v-for="(category,index) in exerciseCategory" :key="index" :value="category.code" :label="category.name"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="tags" label="实验标签:" :rules="[{required:true,message:'实验标签不能为空'}]">
              <el-select v-model="entity.tags" :multiple-limit='3' multiple allow-create filterable default-first-option remote reserve-keyword placeholder="请输入实验标签，最多3个"
                :remote-method="remoteMethod"
                :loading="loading" style="width:100%;">
                <el-option v-for="item in tagsOption" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="complexity" label="难易程度:" :rules="[{required:true,message:'难易程度不能为空'}]">
              <div style="padding-top:6px;">
                <el-rate v-model="entity.complexity"></el-rate>
              </div>
            </el-form-item>
            <el-form-item prop="knowledge" label="知识点:" :rules="[{required:true,message:'知识点不能为空'},{max: 500, message: '长度小于 500 个字符', trigger: 'blur' }]">
              <el-input type="textarea" :rows="4" v-model="entity.knowledge" placeholder="请填写知识点"></el-input>
            </el-form-item>
            <el-form-item prop="logo" label="封面:" :rules="[{required:true,message:'请上传封面'}]">
              <el-upload
                class="avatar-uploader"
                :action="action"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
                <img v-if="entity.logo" :src="entity.logo" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item prop="sort" label="排序:" :rules="[{required:true,message:'排序不能为空'}]">
              <el-input type="number" v-model="entity.sort" style="width:120px;"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
    </el-form>
    <div style="text-align:center;padding-top:10px;">
      <el-button type="primary" @click="onNext">下一步</el-button>
    </div>
</div>
</template>

<script>
/**
 * @file 实验基本信息
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/12
 */
import { chooseList } from '@/api/base/label'
import { getNow } from '@/api/coursemanage/course'
export default {
  props: ['entity', 'exerciseCategory', 'courseid', 'isTeacher'],
  data() {
    return {
      action: process.env.BASE_API + '/AppFile/file/upload2',
      loading: false,
      // 标签列表
      tagsOption: [],
      // 课程
      courseList: []
    }
  },
  created() {
    this.$nextTick(e => {
      if (this.isTeacher) {
        this.$set(this.entity, 'type', '0')
      }
      this.init()
    })
  },
  methods: {
    init() {
      this.getCourse()
      if (this.courseid !== undefined) {
        this.$set(this.entity, 'type', '0')
        this.$set(this.entity, 'courseId', this.courseid)
      }
    },
    getCourse() {
      // 获取课程信息
      getNow().then(e => {
        this.courseList = e.data.data
      })
    },
    onNext() {
      for (var i = 0; i < this.entity.tags.length; i++) {
        if (this.entity.tags[i].length > 20) {
          this.$message.error('单个标签长度不能大于20字符!')
          return
        }
      }
      this.$refs.form.validate(e => {
        if (e) {
          this.$emit('next')
        }
      })
    },
    remoteMethod(query) {
      if (query !== '') {
        this.tagsOption = []
        this.loading = true
        chooseList(query).then(e => {
          e.data.data.forEach(element => {
            this.tagsOption.push({ value: element.name, label: element.name })
          })
          this.loading = false
        })
      } else {
        this.tagsOption = []
      }
    },
    // LOGO上传
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

<style scoped lang="scss">
.borbtm{
    border:none;
    border-bottom:0.5px #e4e4e4 dotted;
}
</style>
