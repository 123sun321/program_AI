<template>
  <el-form :model="entity" ref="form" :inline="line" label-width="100px" class="demo-dynamic">
    <el-form-item prop="name"  label="标签名称:" :rules="[ {validator:remoteValidate('name','已存在同名标签')},{ required: true, message: '请填写标签名称', trigger: 'blur' },
              { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: ['blur', 'change'] } ]">
          <el-input v-model="entity.name"></el-input>
    </el-form-item>
    <!-- <el-form-item prop="type"  label="标签类型:">
          <el-input v-model="entity.type" placeholder="请填写不超过5个字标签类型"></el-input>
    </el-form-item> -->
  </el-form>
</template>
<script>

import {
  form
} from '@/mixins/form'
import {
  fetchOne, remoteValid
} from '@/api/base/label'
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
    // 是否显示标签弹窗
    open() {
      this.visible = true
    },
    // 判断是否为修改标签信息
    load() {
      if (this.isEdit && this.eid) {
        fetchOne(this.eid).then(e => {
          this.entity = e.data.data
        })
      }
    },
    // 校验标签是否重复
    remoteValidate(name, msg) {
      return (rule, value, callback) => {
        const id = this.entity.id
        // eslint-disable-next-line no-undef
        remoteValid(id, name, value).then(e => {
          console.log(e.data.data)
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
