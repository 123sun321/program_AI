<template>
  <div class="app-wrapper">
      <navbar></navbar>
      <div class="main-container">
        <app-main class="customscroll"></app-main>
      </div>
      <bottom-info></bottom-info>
  </div>
</template>

<script>
import { Navbar, Sidebar, AppMain, bottomInfo } from './components'

export default {
  name: 'layout',
  components: {
    Navbar,
    Sidebar,
    AppMain,
    bottomInfo
  },
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    }
  },
  created() {
    if (window.localStorage.getItem('redirect')) {
      this.$router.push({
        path: window.localStorage.getItem('redirect')
      })
      window.localStorage.removeItem('redirect')
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/mixin.scss";
  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;
  }
  #app .main-container{
    padding:50px 0;
    position: absolute;
    width: 100%;
    height: 100%;
  }
  #app .app-main{
    padding-top: 0px;
    overflow-y: auto;
    overflow-x: hidden;
  }
</style>

