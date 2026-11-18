/*
 * @Author: 黄鹏举
 * @Date: 2018-12-04 17:52:38 
 * @Last Modified time: 2018-12-04 19:26:11
 */
<template>
  <el-form :model="entity" ref="form" :inline="line">
    <slot name="tip"></slot>
    <el-form-item prop="type" label="类型:" :rules="[ { required: true, message: '请选择奖惩类型', trigger: 'change' }]">
        <el-select v-model="entity.type"   clearable filterable placeholder="请选择" style="width:100%">
            <el-option  v-for="(item,index) in type" :key="index" :label="item.name"  :value="item.name">
            </el-option>
        </el-select>
    </el-form-item>
    <el-form-item prop="level" label="级别:" :rules="[ { required: true, message: '请选择奖惩类型', trigger: 'change' }]">
        <el-select v-model="entity.level"   clearable filterable placeholder="请选择" style="width:100%">
            <el-option  v-for="(item,index) in level" :key="index" :label="item.name"  :value="item.name">
            </el-option>
        </el-select>
    </el-form-item>
    <el-form-item prop="name" label="名称:" :rules="[{validator:remoteValidate('name','已存在同名奖惩')}]">
      <el-input v-model="entity.name" clearable placeholder="请填写奖惩名称"></el-input>
    </el-form-item>
    <el-form-item prop="number" label="编号:" :rules="[{validator:remoteValidate('number','已存在相同的编号')}]">
      <el-input v-model="entity.number" clearable placeholder="请填写奖惩编号"></el-input>
    </el-form-item>
    <el-form-item prop="desc" label="备注:"  :rules="[{min:1,max:200,message:'长度1到200个字符', trigger: ['blur', 'change']}]">
      <el-input type="textarea" v-model="entity.desc" clearable placeholder="请填写备注"></el-input>
    </el-form-item>
    <el-form-item prop="logo" label="logo:">
        <upload ref="upload" @imgurl="imgurl" v-model="entity.logo" :img="img" ></upload>
    </el-form-item>
  </el-form>
</template>
<script>
/**
 * @description 奖惩维护公共表单
 * @author【黄鹏举】
 * @since 2018/12/4
 * @version 1.0
 */
import upload from '../upload'

import {
  form
} from '@/mixins/form'
import {
  fetchOne,
  remoteValid
} from '@/api/base/rewardPunishCategory'
export default {
  // form
  mixins: [form],
  // 组件
  components: {
    upload
  },
  // 父组件的参数
  props: ['eid', 'isEdit'],

  data() {
    return {
      // 类型
      type: [
        { 'number': 1, name: '奖励' },
        { 'number': 2, name: '处罚' }
      ],
      // 级别
      level: [
        { 'number': 1, name: '国家级' },
        { 'number': 2, name: '省级' },
        { 'number': 3, name: '地市级' },
        { 'number': 4, name: '校级' },
        { 'number': 5, name: '院系级' },
        { 'number': 6, name: '其他' }
      ],
      // logo路径
      img: undefined
    }
  },
  methods: {
    // 接收上传图片路径  url上传成功后图片路径
    imgurl(url) {
      this.entity.logo = url
      this.img = url
    },
    // 打开组件调用的方法
    load() {
      if (this.isEdit && this.eid) {
        fetchOne(this.eid).then(e => {
          this.entity = e.data.data
          this.img = e.data.data.logo
        })
      } else {
        this.img = ''
      }
    },
    // 重复验证 id数据id level级别 name 验证字段 msg提示信息,value 验证的值
    remoteValidate(name, msg) {
      return (rule, value, callback) => {
        if (!value) {
          console.log('内容为空已拒绝验证')
          if (name === 'number') {
            callback(new Error('请填写编号'))
          } else if (name === 'name') {
            callback(new Error('请填写名称'))
          }
          return
        }
        if (value.length > 20) {
          console.log('长度>20已拒绝验证')
          callback(new Error('输入内容超出20个字符'))
          return
        }
        const id = this.entity.id
        remoteValid(id, name, value).then(e => {
          if (e.data.data) {
            console.log('验证通过')
            callback()
          } else {
            callback(new Error(msg))
          }
        })
      }
    }
  }
}

</script>

<style>
</style>
