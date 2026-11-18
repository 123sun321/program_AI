<template>
    <div class="mycourse">
     <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
        <el-tab-pane label="历史学习" name="first">
            <history></history>
        </el-tab-pane>
        <el-tab-pane label="公开课程" name="second">
            <publicity></publicity>
        </el-tab-pane>
        <el-tab-pane label="私有课程" name="third">
            <privatecourse></privatecourse>
        </el-tab-pane>
        <el-tab-pane label="关注列表" name="fourth">
            <collect></collect>
        </el-tab-pane>
     </el-tabs>
    </div>
</template>

<script>
import history from './tabs/course/history'
import privatecourse from './tabs/course/private'
import publicity from './tabs/course/publicity'
import collect from './tabs/course/collect'

export default {
  components: {
    history,
    privatecourse,
    publicity,
    collect
  },
  data() {
    return {
      activeName: 'first'
    }
  },
  methods: {
    handleClick(tab, event) {
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
</style>
