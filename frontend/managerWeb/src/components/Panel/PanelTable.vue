<template>
  <div class="panel-table">
    <el-table :data="data" :height="height" @selection-change="handleSelectionChange" style="width: 100%">
      <slot></slot>
    </el-table>
  </div>
    
  </template>

  <script>
    export default {
      props: {
        data: {
          type: [Array, Object],
          required: true
        },
        height: String
      },
      data() {
        return {
          // tableData: [{
          //   date: '2016-05-02',
          //   name: '王小虎',
          //   address: '上海市普陀区金沙江路 1518 弄'
          // }, {
          //   date: '2016-05-04',
          //   name: '王小虎',
          //   address: '上海市普陀区金沙江路 1517 弄'
          // }, {
          //   date: '2016-05-01',
          //   name: '王小虎',
          //   address: '上海市普陀区金沙江路 1519 弄'
          // }, {
          //   date: '2016-05-03',
          //   name: '王小虎',
          //   address: '上海市普陀区金沙江路 1516 弄'
          // }]
        }
      },
      methods: {
        handleSelectionChange(val) {
          this.$emit('selection-change', val)
        }
      }
    }
  </script>

<style lang="scss" scoped>
.el-table {
  font-size: 12px;
}
.el-table th {
  background: #f6f4f4;
  // background-color: #f5f7fa;
}
</style>
  <style>
  /* .el-table th {
      background: #f6f4f4;
    } */
  </style>
  
  