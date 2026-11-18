<template>
  <el-form :model="entity" ref="form" :inline="line" label-width="100px">
    <slot name="tip"></slot>
    <!-- <el-form-item prop="name" label="课程名称:" :rules="[{validator:remoteValidate('name','已存在同名课程')},{ required: true, message: '请填写课程名称', trigger: 'blur' },{ min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: ['blur', 'change']}]">
      <el-input v-model="entity.name" placeholder="请填写课程名称"></el-input>
    </el-form-item> -->
     <el-form-item prop="code" label="编号:" :rules="[{validator:remoteValidate('code','已存在相同的编号')},{ required: true, message: '请填写编号', trigger: 'change' },{min:1,max:20,message:'长度1到20个字符'}]">
      <el-input v-model="entity.code" placeholder="请填写编号"></el-input>
    </el-form-item>
    <el-form-item prop="code" label="用户编号:" :rules="[{validator:remoteValidate('code','已存在相同的编号')},{ required: true, message: '请填写用户编号', trigger: 'change' },{min:1,max:20,message:'长度1到20个字符'}]">
      <el-input v-model="entity.code" placeholder="请填写用户编号"></el-input>
    </el-form-item>
    <el-form-item prop="nature" label="课程类别:" :rules="[ { required: true, message: '请填写课程类别', trigger: 'change' }]">
      <el-select v-model="entity.nature">
        <el-option v-for="(nature,index) in natures" :key="index" :value="nature.code" :label="nature.name"></el-option>
      </el-select>      
    </el-form-item>
    <el-form-item prop="code" label="名称:" :rules="[{validator:remoteValidate('code','已存在相同的编号')},{ required: true, message: '请填写名称', trigger: 'change' },{min:1,max:20,message:'长度1到20个字符'}]">
      <el-input v-model="entity.code" placeholder="请填写名称"></el-input>
    </el-form-item>



    <el-form-item prop="score" label="学分:" :rules="[{required:true,message:'学分不能为空'}]">
      <el-input type="number" v-model="entity.score" placeholder="请填写学分"></el-input>
    </el-form-item>
    <el-form-item prop="summary" label="描述:">
      <el-input type="textarea" v-model="entity.summary" placeholder="请填写描述"></el-input>
    </el-form-item>
    <el-form-item prop="useBook" label="使用教材">
      <el-input v-model="entity.useBook" placeholder="请填写使用的教材"></el-input>
    </el-form-item>
    <el-form-item prop="recommendBook" label="推荐教材">
      <el-input v-model="entity.recommendBook" placeholder="请填写推荐的教材"></el-input>
    </el-form-item>
  </el-form>
</template>
<script>
/**
 * @file 课程基本form组件
 * @author 陈乐乐
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/04
 */
import {
  form
} from '@/mixins/form'
import {
  fetchOne,
  remoteValid,
  fetchNature
} from '@/api/coursemanage/course'
export default {
  mixins: [form],
  data() {
    return {
      natures: []
    }
  },
  methods: {
    open() {
      this.visible = true
    },
    load() {
      if (this.natures.length === 0) {
        this.getNatrue()
      }
      if (this.isEdit && this.eid) {
        fetchOne(this.eid).then(e => {
          this.entity = e.data.data
        })
      }
    },
    remoteValidate(name, msg) {
      return (rule, value, callback) => {
        const id = this.entity.id
        remoteValid(id, name, value).then(e => {
          if (e.data.data) {
            callback()
          } else {
            callback(new Error(msg))
          }
        })
      }
    },
    getNatrue() {
      fetchNature().then(e => {
        this.natures = e.data.data
      })
    }
  }
}

</script>

<style>
</style>
