<template>
  <div class="maincontent profession-page">
    <el-button type="primary" @click="select">选择资源</el-button>
    <resource ref="res" v-on:chooseList="getData"></resource>
    <div style="margin-top:10px;">
       <el-table :data="chooseList">
         <el-table-column label="标题" align="center" prop="title"></el-table-column>
         <el-table-column label="分享链接" align="center" prop="sharePath"></el-table-column>
       </el-table>
    </div>
  </div>
</template>

<script>
/*
资源文件列表
@author 马勇超
@date 2025/03/19 14:00
@version 1.0
*/
import Resource from './resource'
export default {
  components: {
    Resource
  },
  data() {
    return {
      chooseList: []
    }
  },
  // 获取数据
  created() {
    this.$nextTick(e => {
      this.getData()
    })
  },
  methods: {
    select() {
      this.$refs.res.open()
    },
    getData(data) {
      this.chooseList = data
      console.log(this.chooseList)
    }
  }
}
</script>

<style scoped>
 .profession-page{
    min-height: 50px;
  }
</style>
