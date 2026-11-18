<template>
  <div class="panel-card">
    <div v-if="isVisible" class="panel-header">
      <span>{{title}}</span>
      <span class="panel-info"><slot name="info"></slot></span>
      <div class="panel-button"><slot name="actionButton"></slot></div>
    </div>
    <div class="panel-body">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    title: String,
    isVisible: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    // isVisible: function() {
    //   if (this.title) {
    //     return true
    //   } else {
    //     return false
    //   }
    // }
  }
}
</script>

<style lang="scss" scoped>
.panel-card {
  border-radius: 4px;
  border: 1px solid #ebeef5;
  background-color: #fff;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
  color: #303133;
  // position: absolute;
  // bottom: 20px;
  // top: 70px;
  // right: 20px;
  // left: 170px;
  .panel-header {
    padding: 18px 20px;
    border-bottom: 1px solid #ebeef5;
    box-sizing: border-box;
    position: relative;
    .panel-info {
      margin-left: 10px;
      font-size: 14px;
    }
    .panel-button {
      float: right;
      position: absolute;
      right: 14px;
      top: 14px;
    }
  }
  .panel-body {
    padding: 20px;
  }
}
</style>


