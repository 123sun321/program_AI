<template>
  <div class="panel-tabs">
    <el-tabs v-model="activeName" :tab-position="position">
      <slot></slot>
    </el-tabs>
  </div>
</template>

<script>
export default {
  name: 'panel-tabs',
  props: {
    position: {
      type: String,
      default: 'top'
    },
    /* value: {
      type: String,
      default: true
    },*/
    Name: {
      type: String,
      default: 'first'
    }
  },
  data() {
    return {
      activeName: this.Name
    }
  }/*,
  watch: {
    activeName(val) {
      this.value = val
    }
  }*/
}
</script>

<style lang="scss" scoped>
.panel-tabs {
  border: 1px solid #ebebeb;
  background-color: #fff;
  border-radius: 3px;
  transition: .2s;
  padding: 15px;
  height: 100%;
}
</style>
