<template>
  <div class="mypage">
    <el-pagination @size-change="sizeChange" @current-change="pageChange" :current-page.sync="page.current" background
      :page-size="page.pageCount" layout="prev, pager, next" :total="page.total" prev-text="上一页" next-text="下一页">
    </el-pagination>
  </div>

</template>

<script>
  export default {
    props: ['page'],
    methods: {
      pageChange(cur) {
        this.page.current = cur
        this.$emit('changed')
      },
      sizeChange(size) {
        this.page.pageCount = size
        this.$emit('changed')
      }
    }
  }

</script>

<style lang="scss">
  .mypage {
    padding: 5px 0px;
    text-align: center;
    .el-pagination.is-background .el-pager li.active{
      background-color: #2d2e3f;
    }
    .el-pagination.is-background .el-pager li:hover{
      background-color: #2d2e3f;
      color: #ffffff;
    }
    .el-pagination.is-background .btn-next, .el-pagination.is-background .btn-prev, .el-pagination.is-background .el-pager li{
      background: none;
      border-radius: 50%;
    }
  }

</style>
