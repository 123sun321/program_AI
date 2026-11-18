<template>
    <div class="maincontent">
         <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="基本信息" name="baseinfo">
               <user-base-info></user-base-info>
            </el-tab-pane>
            <el-tab-pane label="设置头像" name="avatar">
                <user-avatar-setting></user-avatar-setting>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>
<script>
/**
 * @file 用户个人资料修改组件
 * @author 张向宇
 * @copyright zhengzhouchuanghzi
 * @since 2025/01/10
 */
import userAvatarSetting from './components/userAvatarSetting'
import userBaseInfo from './components/userBaseInfo'
export default {
  components: {
    userAvatarSetting, userBaseInfo
  },
  data() {
    return {
      activeName: 'baseinfo'
    }
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event)
    }
  }
}
</script>
