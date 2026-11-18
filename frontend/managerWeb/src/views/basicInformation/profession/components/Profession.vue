<template>
  <el-form :model="entity" ref="form" :inline="line" label-width="100px" class="demo-dynamic">
    <el-form-item prop="code"  label="专业编号:" :rules="[ {validator:remoteValidate('code','已存在该编号')},{ required: true, message: '请填写专业名称', trigger: 'blur' },
              { min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: ['blur', 'change'] } ]"><el-input v-model="entity.code" placeholder="请填写不超过10个字专业编号"></el-input>
        </el-form-item>
        <el-form-item prop="name"  label="专业名称:" :rules="[ {validator:remoteValidate('name','已存在同名专业')},{ required: true, message: '请填写专业名称', trigger: 'blur' },
              { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: ['blur', 'change'] } ]"><el-input v-model="entity.name" placeholder="请填写不超过20个字专业名称"></el-input>
        </el-form-item>
  </el-form>
</template>
<script>
/*
专业弹窗组件
@author 袁延延
@date 2025/11/26 15:00
@version 1.0
*/
import {
  form
} from '@/mixins/form'
import Vue from 'vue'
import {
  fetchOne, remoteValid
} from '@/api/base/professions'
export default {
  data() {
    return {
    }
  },
  mixins: [form],
  props: ['eid', 'isEdit'],
  created() {

  },
  methods: {
    // 是否显示专业弹窗
    open() {
      this.visible = true
    },
    // 判断是否为修改专业信息
    load() {
      if (this.isEdit && this.eid) {
        fetchOne(this.eid).then(e => {
          this.entity = e.data.data
        })
      }
    },
    // 校验专业是否重复
    remoteValidate(name, msg) {
      return (rule, value, callback) => {
        const id = this.entity.id
        remoteValid(id, name, value).then(e => {
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
        Vue.set(this.entity, 'departmentsName', department.name)
      }
    }
  }
}
</script>

<style>
</style>
