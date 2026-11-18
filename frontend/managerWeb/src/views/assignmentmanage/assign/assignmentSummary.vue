<template>
  <div>
    <div><el-row :gutter="20">
  <el-col :span="6"><div class="grid-content bg-purple">标题：{{coursedata.title}}</div></el-col>
  <el-col :span="6"><div class="grid-content bg-purple">提交作业人数：{{coursedata.submitNumber}}</div></el-col>
  <el-col :span="6"><div class="grid-content bg-purple">未评分人数：{{coursedata.noScore}}</div></el-col>
  <el-col :span="6"><div class="grid-content bg-purple">发布作业时间：{{coursedata.creationTime}}</div></el-col>
</el-row>
</div>
    <div class="summary">
      <el-row>
        <el-col :span="24"><div class="grid-content bg-purple"><span>作业要求：</span></div></el-col>
        <el-col :span="24"><div class="summary" v-html="coursedata.summary"></div></el-col>
      </el-row>
      
      
      </div>
    
    
    
  </div>
</template>
<script>
export default {
  props: ['coursedata'],
  data() {
    return {
      // assignmentdata: ''

    }
  },
  created() {
    this.$nextTick(e => {
      this.init()
    })
  },
  methods: {
    init() {
      debugger
      this.coursedata
    }
  }
}
</script>
<style lang="scss" scoped>
.summary{
  margin-top: 20px
}
</style>