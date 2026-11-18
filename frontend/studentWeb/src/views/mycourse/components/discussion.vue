<template>
    <div class="mycourse">
     <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
        <el-tab-pane label="发布的讨论" name="first">
            <topic></topic>
        </el-tab-pane>
        <el-tab-pane label="回复的讨论" name="second">
            <replay :avatar="avatar" :userId="userId"></replay>
        </el-tab-pane>
     </el-tabs>
    </div>
</template>

<script>
/**
 * @file  讨论组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/22
 */

import replay from './tabs/discussion/replay'
import store from '@/store'
import topic from './tabs/discussion/topic'
export default {
  components: {
    replay,
    topic
  },
  data() {
    return {
      activeName: 'first',
      userId: '',
      avatar: ''// 当前登录用户头像
    }
  },
  created() {
    this.avatar = store.getters.avatar
    this.userId = store.getters.userId
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
