<template>
  <el-form :model="entity" ref="form">
    <el-form-item prop="name" label="字典名称" :rules="[{validator:remoteValidate('name','名称已存在')},{required:true,message:'字典名称不能为空'},{min:1,max:50,message:'字典名称长度在1-50个字符之间'}]">
      <el-input v-model="entity.name"></el-input>
    </el-form-item>
    <el-form-item prop="code" label="字典编号" :rules="[{validator:remoteValidate('code','编号已存在')},{required:true,message:'编号名称不能为空'},{min:1,max:50,message:'编号长度在1-50个字符之间'}]">
      <el-input v-model="entity.code"></el-input>
    </el-form-item>    
    <el-form-item prop="sort" label="序号" :rules="[{required:true,message:'序号不能为空'}]">
      <el-input v-model.number="entity.sort"></el-input>
    </el-form-item>    
  </el-form>
</template>

<script>
  import {
    form
  } from '@/mixins/form'
  import {
    remoteValidItem,
    fetchItem
  } from '@/api/dictionary'
  export default {
    mixins: [form],
    props: ['dictionaryId'],
    methods: {
      load() {
        if (this.eid && this.isEdit) {
          fetchItem(this.eid).then(e => {
            this.entity = e.data.data
          })
        }
      },
      remoteValidate(name, msg) {
        return (rule, value, callback) => {
          const id = this.entity.id
          const dictionaryId = this.dictionaryId
          remoteValidItem(id, dictionaryId, name, value).then(e => {
            if (e.data.data) {
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
