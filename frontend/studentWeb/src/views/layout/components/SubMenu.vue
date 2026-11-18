<template>
    <div class="left-menu clearfix">
       <div class="logo">
        <img :src="require('@/assets/student/logo.png')" />
       </div>
       <template v-for="item in filterRouter">  
          <!-- <router-link v-if="sceondRouter(item.children).length===1" :to="getpath(item)" :key="item.children[0].name"> -->
           <el-menu-item :index="item.name||item.path" v-if="sceondRouter(item.children).length===1" :key="item.children[0].name">
              <router-link :to="getpath(item)" style="display:block;">
                <svg-icon v-if="item.children[0].meta&&item.children[0].meta.icon" :icon-class="item.children[0].meta.icon"></svg-icon>
                <span v-if="item.children[0].meta&&item.children[0].meta.title">{{generateTitle(item.children[0].meta.title)}}</span>
              </router-link>
           </el-menu-item>
          <!-- </router-link> -->
          <el-submenu :index="item.name||item.path" :key="item.children[0].name" v-else>
            <template slot="title">{{generateTitle(item.name)}}</template>
            <template v-for="twoitem in sceondRouter(item.children)">  
              <router-link :to="item.path+'/'+twoitem.path" :key="twoitem.name">
                <el-menu-item :index="twoitem.name||twoitem.path">
                    <svg-icon v-if="twoitem.meta&&twoitem.meta.icon" :icon-class="twoitem.meta.icon"></svg-icon>
                    <span v-if="twoitem.meta&&twoitem.meta.title">{{generateTitle(twoitem.meta.title)}}</span>
                </el-menu-item>
              </router-link>
            </template>
          </el-submenu>
       </template>
       <el-submenu index="tools">
          <template slot="title">
            <svg-icon icon-class="tool"></svg-icon>
            <span>工具</span>
          </template>
          <el-menu-item index="Swagger Editer">Swagger Editer</el-menu-item>
          <router-link to="process.env.EASYMOCKHOST">
            <el-menu-item index="Easy Mock">Easy Mock</el-menu-item>
          </router-link>
       </el-submenu>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import { generateTitle } from '@/utils/i18n'
export default {
  computed: {
    ...mapGetters([
      'permission_routers'
    ]),
    filterRouter() {
      const router = this.permission_routers.filter(b => {
        if (b.hidden) {
          return false
        } else {
          if (b.children && b.children.filter(c => !c.hidden).length > 0) {
            return true
          } else {
            return false
          }
        }
      })
      return router
    }
  },
  created() {
  },
  methods: {
    generateTitle,
    ...mapMutations([
      'SHOW_SECONDMENU'
    ]),
    sceondRouter(srouter) {
      const router = srouter.filter(b => {
        if (b.hidden) {
          return false
        } else {
          return true
        }
      })
      return router
    },
    getpath(item) {
      if (item.children[0].children) { // 判断路由是否有第三级，若有则直接跳转第三级第一个
        return item.path + '/' + item.children[0].path + '/' + item.children[0].children[0].path
      } else { // 没有第三级默认跳转第二级
        return item.path + '/' + item.children[0].path
      }
    }
  }
}
</script>

<style lang="scss">
  .left-menu{
    float: left;
    .logo{height: 49px; float: left; width: 240px;padding: 8px 15px;}
    .el-menu-item{height: 49px;line-height: 49px;border:none;}
    .el-submenu .el-submenu__title{height: 49px;line-height: 49px;border:none;}
    .el-submenu>.el-menu{top: 50px;}
    .router-link-active>li{
      background-color: #3a3a3a!important;color: #fff!important;
    }
    .el-menu-item:hover{background-color: #3a3a3a!important;color: #fff!important;}
  }
</style>


