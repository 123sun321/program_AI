<template>
  <div class="moreinfo">
    <div class="content">
        <el-tabs v-model="activeName">
            <el-tab-pane label="发布的讨论" name="community">
                <discussion ></discussion>
            </el-tab-pane>
            <el-tab-pane label="参与的讨论" name="reply">
                <reply :avatar="avatar"></reply>
            </el-tab-pane>
        </el-tabs>
    </div>
  </div>
</template>

<script>
import discussion from './tabs/discussion'
import reply from './tabs/reply'
export default {
  components: {
    discussion, reply
  },
  props: ['avatar'],
  data() {
    return {
      activeName: 'community'
    }
  }
}
</script>

<style lang="scss">
.moreinfo{
  .content{
    margin: 0 0 15px 0;
    padding: 20px;
    background: #fff;
    border: 1px solid #eee;
    .el-tabs__item.is-active,.el-tabs__item:hover{
      color: #459d92;
    }
    .el-tabs__active-bar{
      background-color:#459d92;
    }
  }
}
</style>
