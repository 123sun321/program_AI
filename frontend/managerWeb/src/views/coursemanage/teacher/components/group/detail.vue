<template>
  <el-form :model="entity" ref="form" :inline="line" label-width="100px" class="demo-dynamic">
        <el-form-item prop="name"  label="小组名称:" :rules="[{ required: true, message: '请填写名称', trigger: 'blur' },
              { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: ['blur', 'change'] } ]">
              <el-input v-model="entity.name" placeholder="请填写不超过30个字名称"></el-input>
        </el-form-item>
  </el-form>
</template>
<script>

import { form } from '@/mixins/form'
import { get } from '@/api/coursemanage/courseGroup'
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
    open() {
      this.visible = true
    },
    load() {
      if (this.isEdit && this.eid) {
        get(this.eid).then(e => {
          this.entity = e.data.data
        })
      }
    }
  }
}
</script>

<style>
</style>
