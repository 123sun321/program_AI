<template>
      <el-form :model="entity" ref="form" label-width="100px" >
            <el-form-item prop="name"  label="姓名:" :rules="[ { required: true, message: '请填写教师姓名', trigger: 'blur' },
                { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: ['blur', 'change'] }]"       >
              <el-input v-model.trim="entity.name" placeholder="请填写教师姓名"></el-input>
            </el-form-item>
            <el-form-item prop="code"  label="工号:" :rules=" [{validator:remoteValidate('code','已存在该工号')},{ required: true, message: '请填写教师姓名', trigger: 'change' }]"   >
              <el-input v-model="entity.code" placeholder="请填写教师工号"></el-input>
            </el-form-item>
            <el-form-item prop="sex"  label="性别:"  :rules="[ { required: true, message: '请选择性别', trigger: 'blur' }]">
              <el-radio v-model="entity.sex" label="男">男</el-radio>
              <el-radio v-model="entity.sex" label="女">女</el-radio>
            </el-form-item>
            <el-form-item prop="carNo"  label="身份证号:" :rules="[ {required: true, message: '请填写证件号码', trigger: 'blur' },
                   {pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/, message: '证件号码格式有误！', trigger: 'blur'}
                   ]" >
              <el-input v-model="entity.carNo" placeholder="请填写身份证号"></el-input>
            </el-form-item>
            <el-form-item prop="contact"  label="手机号:"  :rules="[{validator:checkPhone(entity.contact,'手机号格式不正确')},{ required: true, message: '请填写教师手机号', trigger: 'change' }]">
              <el-input v-model="entity.contact" placeholder="请填写教师手机号"></el-input>
              <el-tag v-if="entity.contact!=='' && entity.phoneNumberConfirmed">已绑定</el-tag>
                  <el-tag v-if="entity.contact!=='' &&entity.contact!=null && !entity.phoneNumberConfirmed">未绑定</el-tag>
              <span></span>
            </el-form-item>
            <el-form-item prop="email"  label="邮箱:"  :rules="[{validator:validateEmail(entity.email,'邮箱格式不正确')},{ required: true, message: '请填写教师邮箱', trigger: 'change' }]">
              <el-input v-model="entity.email" placeholder="请填写教师邮箱"></el-input>
              <el-tag v-if="entity.email!=='' && entity.emailConfirmed">已绑定</el-tag>
                  <el-tag v-if="entity.email!=='' &&entity.email!=null && !entity.emailConfirmed">未绑定</el-tag>
            </el-form-item>
          </el-form>
</template>
<script>
/*
教师弹窗组件
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
} from '@/api/base/teacher'
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
    // 是否显示教师弹窗
    open() {
      this.visible = true
    },
    // 判断是否为修改教师信息
    load() {
      if (this.isEdit && this.eid) {
        fetchOne(this.eid).then(e => {
          this.entity = e.data.data
        })
      }
    },
    checkPhone(phone, msg) {
      return (rule, value, callback) => {
        if (!(/^1[34578]\d{9}$/.test(phone))) {
          callback(new Error(msg))
        } else {
          callback()
        }
      }
    },
    validateEmail(email, msg) {
      const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      return (rule, value, callback) => {
        if (re.test(email)) {
          callback()
        } else {
          callback(new Error(msg))
        }
      }
    },
    // 校验教师是否重复
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
