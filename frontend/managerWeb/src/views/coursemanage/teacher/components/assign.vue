<template>
      <el-form :model="entity" ref="form" :inline="line"  label-width="100px" >
        <el-form-item label="所属课程:" prop="courseId" :rules="[{required:true,message:'所属课程不能为空'}]">
            <el-select v-model="entity.courseId" disabled  clearable filterable placeholder="全部">
                <el-option  v-for="(item,index) in List.courseList" :key="index" :label="item.name"  :value="item.id ">
                </el-option>
            </el-select>
            </el-form-item>
            <el-form-item  label="集体类型:" prop="teamCategory" :rules="[{required:true,message:'集体类型不能为空'}]">
            <el-select v-model="entity.teamCategory" placeholder="请选择" @change="changeteamCategory">
                <el-option  v-for="(item,index) in teamCategorys" :key="index" :label="item.name"  :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
          <div v-if="entity.teamCategory===1">
            <el-form-item label="学年:" prop="year" :rules="[{required:true,message:'学年不能为空',trigger: 'blur'}]">
            <el-select v-model="entity.year" placeholder="请选择" @change="changeYear">
                <el-option  v-for="(item,index) in List.grades" :key="index" :label="item.name"  :value="item.code">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="班级:" prop="gradeId" :rules="[{required:true,message:'班级不能为空',trigger: 'blur'}]">
            <el-select v-model="entity.gradeId" placeholder="请选择">
              <el-option  v-for="(item,index) in List.classInfo" :key="index" :label="item.name"  :value="item.id"></el-option>
            </el-select>
            </el-form-item>
         </div>

           <el-form-item v-if="entity.teamCategory===2" label="小组:" prop="groupId" :rules="[{required:true,message:'小组不能为空'}]">
            <el-select v-model="entity.groupId" placeholder="请选择" >
                <el-option  v-for="(item,index) in List.groups" :key="index" :label="item.name"  :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>

            <el-form-item prop="title"  label="标题:" :rules="[ { required: true, message: '请填写标题' }, {validator:remoteValidate('title','已存在相同标题')},{ min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: ['blur', 'change']}]" >
              <el-input v-model.trim="entity.title" placeholder="请填写标题"></el-input>
            </el-form-item>
              <!-- <el-form-item prop="summary"  style="min-height: 300px" label="内容:" :rules="[{required:true,message:'内容不能为空'}]">
                  <vue-wangeditor ref="wangeditor" v-model.trim="entity.summary"></vue-wangeditor>

            </el-form-item> -->
            <!-- 引用markdown -->
          <el-form-item prop="summary" style="min-height: 300px" label="内容:" :rules="[{required:true,message:'内容不能为空'}]">
              <vue-wangeditor ref="wangeditor" v-model="entity.summary"></vue-wangeditor>
          </el-form-item>
              <el-form-item prop="sort"  label="排序:" :rules="[{ required: true, message: '请填写排序', trigger: 'blur' }, {validator:remoteValidate('sort','已存在相同排序')}]" >
            <el-input-number v-model="entity.sort" controls-position="right"  :min="0"></el-input-number>
            </el-form-item>
          </el-form>
</template>
<script>
/*
作业弹窗组件
@author 袁延延
@date 2025/3/22 15:00
@version 1.0
*/
import VueWangeditor from '@/components/Wangeditor'
import {
  form
} from '@/mixins/form'
import Vue from 'vue'

import {
  fetchOne, assignmentValidate
} from '@/api/assignmentmanage/assgin'
import {
  getClassByYear
} from '@/api/base/student'
export default {
  mixins: [form],
  props: ['eid', 'isEdit', 'List', 'id'],
  components: {
    VueWangeditor

  },
  data() {
    return {
      entity: {
        year: '',
        gradeId: '',
        courseId: this.id

      },
      visiable: false,
      teamCategorys: [
        {
          id: 1,
          name: '班级'
        },
        {
          id: 2,
          name: '小组'
        }
      ]
    }
  },
  methods: {

    // 判断是否为修改作业信息
    load() {
      this.$nextTick(e => {
        if (this.isEdit && this.eid) {
          this.wangeditorReset()
          fetchOne(this.eid).then(e => {
            this.entity = e.data.data
            getClassByYear(this.entity.year).then(e => {
              this.List.classInfo = e.data.data
            })
          })
        } else {
          this.entity.summary = ''
        }
      })
    },
    changeteamCategory() {
      if (this.entity.teamCategory === 2) {
        if (this.List.groups.length === 0) {
          this.$alert('请到课程小组添加', '暂无小组', {
            confirmButtonText: '确定'
            // callback: action => {
            // }
          })
        }
        this.visiable = true
      } else {
        this.visiable = false
      }
    },
    wangeditorReset() {
      this.$refs.wangeditor.reset()
    },
    // 改变学年
    changeYear() {
      this.entity.gradeId = undefined
      getClassByYear(this.entity.year).then(e => {
        this.List.classInfo = e.data.data
      })
    },
    // 校验专业是否重复
    remoteValidate(name, msg) {
      return (rule, value, callback) => {
        const aid = this.entity.id
        assignmentValidate(aid, name, value, this.id).then(e => {
          console.log(e.data.data)
          if (e.data.data) {
            callback()
          } else {
            callback(new Error(msg))
          }
        })
      }
    },
    // 修改返回参数为院系名称
    onChange(e) {
      const department = this.departments.find(b => b.id === e)
      if (department !== null) {
        Vue.set(this.entity, 'departmentName', department.name)
      }
    }
  }
}

</script>

<style>
</style>
