<template>
<div class="trainingPath" style="position: relative;">
  <FileResource v-if="showFolder"></FileResource>
  <TrainingList v-if="!showFolder" style=""/>
  <el-button type="primary" class="showAllBtn" @click="showFolder = !showFolder">{{showFolder ? '全部显示' : '显示文件夹'}}</el-button>
</div>
</template>

<script>
/**
 * @file 练习
 * @author 宋乃乐
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/12
 */
import FileResource from './fileResource'
import TrainingList from './trainingList'
export default {
  name: 'trainingPath',
  components: {
    FileResource,
    TrainingList
  },

  data() {
    return {
      showFolder: true
    }
  }
}
</script>

<style lang="scss" scoped>
.showAllBtn {
  position: absolute;
  right: 30px;
  top: 10px;
}
</style>
