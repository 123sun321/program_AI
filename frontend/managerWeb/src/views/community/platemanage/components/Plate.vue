<template>
  <el-form :model="entity" ref="form" :inline="line" label-width="100px">
    <slot name="tip"></slot>

    <el-form-item prop="name" label="板块名称:" :rules="[{validator:remoteValidate('name','已存在同名名称'),trigger: ['blur', 'change']},{ required: true, message: '请填写名称', trigger: 'blur' },{ min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: ['blur', 'change']}]">
      <el-input v-model="entity.name" placeholder="请填写名称"></el-input>
    </el-form-item>
    <el-form-item prop="summary" label="板块简介:" :rules="[{ min: 1, max: 200, message: '长度在 1 到 200 个字符', trigger: ['blur', 'change']}]">
      <el-input :autosize="{ minRows: 2, maxRows: 6}" type="textarea" v-model="entity.summary" placeholder="请填写简介"></el-input>
    </el-form-item>
    <el-form-item prop="sort"  label="排序:" :rules="[ {validator:remoteValidate('sort','已存在相同排序')}, { required: true, message: '请填写排序', trigger: 'blur' }]" >
            <el-input-number v-model="entity.sort" controls-position="right"  ></el-input-number>
    </el-form-item>
     <el-form-item prop="role" label="分配角色:" >
         <div style="margin: 15px 0;"></div>
             <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange">
             <el-checkbox v-for="item in cities"  :label="item.id" :key="item.id" >{{item.displayName}}</el-checkbox>
      </el-checkbox-group>
    </el-form-item>
   </el-form>
</template>
<script>
/**
 * @file 板块基本form组件
 * @author 张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/28
 */
import { chooseList } from '@/api/base/label'

import {
  form
} from '@/mixins/form'
import {
  getById,
  remoteValid
} from '@/api/community/platemanage'
import {
  // getRoleInfo,
  fetchList
} from '@/api/permission/role'

export default {

  mixins: [form],
  data() {
    return {

      visible: false,
      loading: false,
      tagsOption: [],
      input: '',
      cities: [],
      checkedCities: [],
      checkAll: false,

      isIndeterminate: true
    }
  },
  methods: {
    init() {
    },
    open() {
      this.visible = true
    },
    load() {
      const data = {}
      fetchList(data).then(response => {
        this.cities = response.data.records
        this.bindData()
      })
    },
    bindData() {
      if (this.isEdit && this.eid) {
        getById(this.eid).then(e => {
          this.entity = e.data.data
          // // debugger
          // const aa = this.entity.roles
          // console.log(this.entity.roles + 'roles')
          // const checkedCities1 = this.entity.roles.split(',')
          this.checkedCities = []
          this.entity.roles.split(',').forEach(element => {
            this.checkedCities.push(Number(element))
          })
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
    editelist() {

    },
    remoteMethod(query) {
      var vm = this
      if (query !== '') {
        this.loading = true
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
    handleCheckedCitiesChange(value) {
      this.entity.roles = value.join(',')
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
