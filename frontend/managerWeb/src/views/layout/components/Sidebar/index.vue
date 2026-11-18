<template>
  <div class="silder-menu-warpper">
    <scroll-bar class="first-scroll-container">
      <el-menu class="firstMenu" mode="vertical" :default-active="getCurrentFirstMenu" :collapse="isCollapse" @select="onMenuSelect"  active-text-color="#000000">
        <sidebar-item :routes="permission_routers" @firstMenuChange="onFirstMenuChange"></sidebar-item>
      </el-menu>
    </scroll-bar>
    <template v-for="item in permission_routers" v-if="!item.hidden && item.children">
      <div class="second-menu-warpper" v-if="firstMenuName!=null && ( firstMenuName===item.path || firstMenuName===item.name )" :key="item.path+'_wapper'">
        <div class="second-sidebar-title">{{generateTitle(item.meta.title)}}</div>
        <el-menu :default-active="getCurrentSecondMenu" class="el-menu-vertical-demo">
          <SidebarSecoedItem  class="nest-menu" :routes="item.children" :key="item.path"></SidebarSecoedItem>
        </el-menu>
      </div>     
    </template> 
  </div>
</template>

<style scoped>
.second-menu-warpper{
  position: fixed;
  top: 0;
  bottom: 0;
  left: 100px;
  width: 110px;
  background: #fff;
}
.second-menu-warpper .second-sidebar-title {
    padding-left: 20px;
    font-size: 14px;
    height: 50px;
    line-height: 50px;
    border-bottom: 1px solid #f2f2f2;
}
.second-menu-warpper, .second-menu-warpper .second-sidebar-title {
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
}
</style>


<script>
import { mapGetters } from 'vuex'
import { mapMutations } from 'vuex'
import SidebarItem from './SidebarItem'
import SidebarSecoedItem from './SidebarSecondItem'
import ScrollBar from '@/components/ScrollBar'
import { generateTitle } from '@/utils/i18n'
import { routerWarpper } from '@/utils/routerWarpper'

export default {
  components: { SidebarItem, ScrollBar, SidebarSecoedItem },
  data: function() {
    return {
      firstMenuName: null
    }
  },
  created: function() {
    routerWarpper(this.permission_routers)
    var menuName = this.getCurrentFirstMenu
    var currentRouter = this.permission_routers.find((value, index, arr) => {
      return value.path === menuName || value.name === menuName
    })
    if (currentRouter != null && (currentRouter.children == null || currentRouter.children.filter((item, index, arr) => !item.hidden).length === 1)) {
      console.log('没有子菜单')
      this.SHOW_SECONDMENU(false)
    } else if (currentRouter.hidden) {
      this.SHOW_SECONDMENU(false)
    } else {
      console.log('有子菜单')
      this.firstMenuName = menuName
      // 判断是否不要显示的子菜单
      var hasShow = false
      currentRouter.children.forEach(element => {
        if (!element.hidden) {
          hasShow = true
        }
      })
      this.SHOW_SECONDMENU(hasShow)
    }
  },
  methods: {
    ...mapMutations([
      'SHOW_SECONDMENU'
    ]),
    onFirstMenuChange: function(firstMenuName) {
      console.log(firstMenuName)
    },
    onMenuSelect: function(index, indexPath) {
      console.log('菜单index:' + index)
      console.log('菜单indexpath:' + indexPath)
      this.firstMenuName = index
      var currentRouter = this.permission_routers.find((value, index, arr) => {
        return value.path === this.firstMenuName || value.name === this.firstMenuName
      })
      // console.log(currentRouter)
      if (currentRouter != null && (currentRouter.children == null || currentRouter.children.filter((item, index, arr) => !item.hidden).length === 1)) {
        this.firstMenuName = null
        this.SHOW_SECONDMENU(false)
      } else {
        this.SHOW_SECONDMENU(true)
      }
    },
    generateTitle
  },
  computed: {
    ...mapGetters([
      'permission_routers',
      'sidebar'
    ]),
    isCollapse() {
      return !this.sidebar.opened
    },
    getCurrentFirstMenu() {
      var path = this.$route.path
      if (path.substring(0, 1) === '/') {
        path = path.substring(1)
        var firstSplit = path.indexOf('/')
        if (firstSplit > 0) {
          path = path.substring(0, firstSplit)
        }
      } else {
        var firstSplit2 = path.indexOf('/')
        if (firstSplit2 > 0) {
          path = path.substring(0, firstSplit2)
        }
      }
      return path
    },
    getCurrentSecondMenu() {
      var path = this.$route.path
      if (path.substring(0, 1) === '/') {
        if (path.split('/').length >= 4) {
          path = path.substring(0, path.lastIndexOf('/'))
        }
      } else {
        if (path.split('/').length >= 3) {
          path = path.substring(0, path.lastIndexOf('/'))
        }
      }
      return path
    }
  }
}
</script>
