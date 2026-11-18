<template>
<div class="page-mergely">
   <div class="mergely-top">
       <span>代码对比</span>
   </div>
   <el-table
    :data="assignmentList"
    :show-header="false"
    style="width: 100%;color:#5973A8;">
    <el-table-column
      prop="name"
      label="日期">
    </el-table-column>
    <el-table-column
      prop="title"
      label="标题">
    </el-table-column>
  </el-table>
  <div class="operation-button">
      <el-button type="warning" @click="gotoMergely()" :disabled="isDisable">开始对比</el-button>
      <el-button style="margin-left:60px;" type="primary" @click="cancelMergely()">取消</el-button>
  </div>
</div>
</template>

<script>
export default {
  data() {
    return {
      assignment: { },
      assignmentList: [
        // { id: 1, name: '张三', title: '20190620作业：制作一个小黄人' }
        // { id: 1, name: '李四', title: 'HTML5第一个作业' }
      ],
      dialogTableVisible: true,
      isDisable: true
    }
  },
  watch: {
    newAssignmentVal: {
      handler(newV, oldV) {
        if (this.assignmentList.length >= 2) {
          this.isDisable = false
        }
      },
      deep: true
    }
  },
  computed: {
    newAssignmentVal() {
      return this.assignmentList
    }
  },
  created() {
    this.$nextTick(e => {
      this.getList()
    })
  },
  methods: {
    getList() {
      //   this.assignment = { id: '1137955082526568450', name: '李四', title: 'HTML5第一个作业' }
      // this.$store.getters.assignmentList.push()
      this.assignmentList = this.$store.getters.assignmentList
      if (this.assignmentList.length >= 2) {
        this.isDisable = false
      }
    },
    gotoMergely() {
      this.assignmentList = this.$store.getters.assignmentList
      this.$router.push({ path: '/assignmentmanage/mergely', query: { id1: this.assignmentList[0].id, id2: this.assignmentList[1].id }})
    },
    cancelMergely() {
      this.$store.getters.assignmentList.splice(0, this.assignmentList.length)
      // this.$router.go(0)
    }
  }
}
</script>

<style scoped>
 .page-mergely{
    position:fixed; 
    width: 16%;
    /* border: 1px solid silver; */
    margin-left:78%;
    /* min-height: 600px; */
    height: 100%;
    z-index: 1000;
    box-shadow: 0 0 3px 0 rgba(0,0,0,.2);
    background-color: #F9FDFF;
 }
 .mergely-top{
    padding-top: 5px;
    padding-bottom: 5px;
    text-align: center;
    font-size: 18px;
    font-weight: 500;
    border-bottom: 1px solid #EBEEF5;
 }
 .operation-button{
   margin: 20px 20px 20px 20px;
   
 }
</style>


