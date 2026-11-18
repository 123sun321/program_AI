<template>
    <div class="mycourse" v-loading="loading">
     <el-tabs v-model="activeName" type="card" @tab-click="handleClick" v-if="this.notSubmit.group!== undefined && this.notSubmit.grad !== undefined">
        <el-tab-pane name="first">
          <span slot="label">班级课程 <i class="messageCount" v-if="this.notSubmit.grad>0">{{this.notSubmit.grad}}</i></span>
          <grad-assignment></grad-assignment>
        </el-tab-pane>
        <el-tab-pane name="second">
          <span slot="label">小组课程 <i class="messageCount" v-if="this.notSubmit.group>0">{{this.notSubmit.group}}</i></span>
          <group-assignment></group-assignment>
        </el-tab-pane>
     </el-tabs>
    </div>
</template>

<script>
import gradAssignment from './gradAssignment'
import groupAssignment from './groupAssignment'
import { groupPage, gradPage } from '@/api/assignment/index'
export default {
  components: {
    gradAssignment,
    groupAssignment
  },
  data() {
    return {
      activeName: 'first',
      form: {
        page: {
          current: 1,
          pageCount: 9,
          total: 0
        }
      },
      loading: false,
      searchParam: {},
      notSubmit: {
        grad: undefined,
        group: undefined
      }
    }
  },
  mounted() {
    this.getNotSubmit()
    // this.groupNotSubmit = 10
  },
  methods: {
    handleClick(tab, event) {
    },
    getNotSubmit() {
      const vm = this
      vm.loading = true
      vm.searchParam.page = vm.form.page
      vm.searchParam.status = -1
      gradPage(vm.searchParam).then(e => {
        const data = e.data.data
        this.$set(this.notSubmit, 'grad', data.total)
        vm.loading = false
      })
      groupPage(vm.searchParam).then(e => {
        const data = e.data.data
        this.$set(this.notSubmit, 'group', data.total)
        vm.loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.mycourse{
    padding: 10px 20px;
}
</style>
<style lang="scss">
.mycourse{
    .el-tabs__nav{
        border:none!important;
        padding:10px 0;
        .el-tabs__item{
            padding: 0 10px!important;
            margin-right:10px;
            text-align: center;
            height: 30px;
            font-size:16px;
            line-height:30px;
            border:none!important;
        }
        .el-tabs__item.is-active,.el-tabs__item:hover{
            transition:initial;
            background-color: #e8f7ed;
            border-radius: 15px;
            color:#1fad4e;
        }
    }
}
.messageCount{
    border-radius: 15px;
    background-color: red;
    min-width: 25px;
    height: 20px;
    display: inline-grid;
    /* float: left; */
    margin-top: 10p;
    line-height: 22px;
    text-align: center;
    color: #fff;
    margin-left: 5px;}
</style>
