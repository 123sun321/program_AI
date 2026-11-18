<template>
  <div class="app-wrapper" :class="{hideSidebar:!sidebar.opened}">
    <sidebar class="sidebar-container"></sidebar>
    <navbar></navbar>
    
    <div class="main-container" v-bind:style="{  marginLeft: sidebar.showSecondMenu?'210px':'100px' }">
     <div class="content-new-background">
       <mergely v-show="isShow"  class="mergely"></mergely>
       <app-main :class="{'widthdisplay':display}"></app-main>
      </div>
    </div>
  </div>
</template>

<script>
import { Navbar, Sidebar, SubMenu, AppMain } from './components'
import Mergely from '../assignmentmanage/mergely/globalMergely'
export default {
  name: 'layout',
  components: {
    Navbar,
    Sidebar,
    SubMenu,
    AppMain,
    Mergely
  },
  data() {
    return {
      isShow: false,
      display: false,
      assignmentList: []
    }
  },
  watch: {
    mergely: {
      handler(newVal, oldVal) {
        if (this.assignmentList.length > 0) {
          this.isShow = true
          this.display = true
        } else {
          this.isShow = false
          this.display = false
        }
      },
      deep: true
    }
  },
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    },
    mergely() {
      this.assignmentList = this.$store.getters.assignmentList
      return this.assignmentList
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
    padding-top: 50px;
    padding-bottom: 10px;
  }
  #app .app-main{
    padding-top: 0px;
  }
  .widthdisplay{
    width:83.5%;
  }
  .content-new-background{
    margin: 10px;
  }
</style>
<style>
  .content-new-background .app-main>div{ background-color: #fff;box-shadow: 0 0 1px 0 rgba(0,0,0,.2);}
</style>
