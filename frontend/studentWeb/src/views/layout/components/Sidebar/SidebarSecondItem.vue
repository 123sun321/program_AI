<template>
  <div class="menu-wrapper">
    <template v-for="item in routes" v-if="!item.hidden">

      <router-link v-if="show(item)" :to="generate(item)" :key="item.name">
        <el-menu-item :index="generate(item)" :class="{'submenu-title-noDropdown':!isNest}">
          <svg-icon v-if="item.meta&&item.meta.icon" :icon-class="item.meta.icon"></svg-icon>
          <span v-if="item.meta&&item.meta.title">{{generateTitle(item.meta.title)}}</span>
        </el-menu-item>
      </router-link>

      <el-submenu v-else :index="item.name||item.path" :key="item.name">
        <template slot="title">
          <svg-icon v-if="item.meta&&item.meta.icon" :icon-class="item.meta.icon"></svg-icon>
          <span v-if="item.meta&&item.meta.title">{{generateTitle(item.meta.title)}}</span>
        </template>

        <template v-for="child in item.children" v-if="!child.hidden">
          <sidebar-second-item :is-nest="true" class="nest-menu" v-if="child.children&&child.children.length>0" :routes="[child]" :key="child.path"></sidebar-second-item>
          <router-link v-else :to="generate(child)" :key="child.name">
            <el-menu-item :index="generate(child)">
              <svg-icon v-if="child.meta&&child.meta.icon" :icon-class="child.meta.icon"></svg-icon>
              <span v-if="child.meta&&child.meta.title">{{generateTitle(child.meta.title)}}</span>
            </el-menu-item>
          </router-link>
        </template>
      </el-submenu>

    </template>
  </div>
</template>

<script>
import { generateTitle } from '@/utils/i18n'
import { mapGetters } from 'vuex'

export default {
  name: 'SidebarSecondItem',
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
    generate(router) {
      if (router.redirct) {
        const rRouter = router.children.find(b => router.redirct === b.path)
        if (rRouter) {
          router = rRouter
        }
      }
      return this.generateRouterPath(router)
    },
    generateRouterPath(router) {
      if (router == null) {
        return ''
      }
      if (router.path != null && router.path.substring(0, 1) === '/') {
        return router.path
      } else {
        // 获取上级路由的路径
        return this.generateRouterPath(router.parent) + '/' + router.path
      }
    },
    show(item) {
      return (!item.children || item.children.filter(b => !b.hidden).length === 0) && !item.alwaysShow
    }
  },
  computed: {
    ...mapGetters([
      'permission_routers'
    ])
  }
}
</script>


