<template>
<div>
<el-form ref="form" :model="form">
        <el-form-item prop="name" fop="like" fcon="or" required>
            <el-input v-model="form.name" ></el-input>
        </el-form-item>
         <el-form-item prop="name1" required>
            <el-input v-model="form.name1" ></el-input>
        </el-form-item>
         <el-form-item prop="name21" fname="name2" fop="in" fcon="or" required>
            <el-input v-model="form.name21" ></el-input>
        </el-form-item>
        <el-form-item prop="name22" fname="name2"  required>
            <el-input v-model="form.name22" ></el-input>
        </el-form-item>
    <el-form-item>
        <el-button type="primary" @click="onSubmit">立即创建</el-button>
        <el-button>取消</el-button>
    </el-form-item>

    </el-form>
    
    <free-compose mname="fcom1"></free-compose>
    <free-compose mname="fcom2"></free-compose>
    <free-compose mname="fcom3"></free-compose>
    <fcom1></fcom1>
    <bmap></bmap>
</div>
    
    
</template>

<script>
import freeCompose from '@/components/freeCompose'
import fcom1 from './fcomp1'
import bmap from '@/components/BaiduMap'
export default {
  components: {
    freeCompose,
    fcom1,
    bmap
  },
  data() {
    return {
      form: {
        search: '',
        page: {
          pageCount: 10,
          pageSize: 1
        }
      }
    }
  },
  methods: {
    onSubmit() {
      const params = this.$formToParams(this.$refs.form)
      console.log(params)
      this.$refs.form.validate(e => {

      })
    }
  }

}
</script>

<style scoped lang="scss">

</style>
