<template>
  <div class="menu-wrapper">
     <a class="team-logo-wrap" href="#">
       <div class="team-logo"><img :src="img_teamlogo"/></div>
     </a>
    <template v-for="item in filterRouter">  
    
        <router-link v-if="item.children.length===1 && !item.children[0].children&&!item.alwaysShow" :to="item.path+'/'+item.children[0].path" :key="item.children[0].name">
        <el-menu-item :index="item.name||item.path" :class="{'submenu-title-noDropdown':!isNest}">
          <svg-icon v-if="item.children[0].meta&&item.children[0].meta.icon" :icon-class="item.children[0].meta.icon"></svg-icon>
          <span v-if="item.children[0].meta&&item.children[0].meta.title">{{generateTitle(item.children[0].meta.title)}}</span>
        </el-menu-item>
      </router-link>
 
        <el-menu-item  v-else :index="item.name||item.path" :key="item.name" :class="{'submenu-title-noDropdown':!isNest}">
          <svg-icon v-if="item.meta&&item.meta.icon" :icon-class="item.meta.icon"></svg-icon>
          <span v-if="item.meta&&item.meta.title">{{generateTitle(item.meta.title)}}</span>
        </el-menu-item>
    
    </template>
    
  </div>

</template>

<style scope>
.team-logo-wrap{margin-bottom: 20px;}
.team-logo{
    margin: 14px auto 0;
    width: 50px;
    height: 50px;
    border-radius: 50%;
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
    border: 1px solid #fff;
    background-size: cover;
    background-position: 50% 50%;
    background-color: #fff;
    text-align: center;
}
.team-logo img{
  width:40px; 
  height:40px;
}
</style>


<script>
import { generateTitle } from '@/utils/i18n'
import img_teamlogo from '@/assets/images/team-logo.png'

export default {
  name: 'SidebarItem',
  data() {
    return {
      img_teamlogo
    }
  },
  computed: {
    filterRouter() {
      const router = this.routes.filter(b => {
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

  props: {
    routes: {
      type: Array
    },
    isNest: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    generateTitle,
    firstMenuChange() {
      this.$emit('firstMenuChange', 12)
    }
  }
}
</script>


