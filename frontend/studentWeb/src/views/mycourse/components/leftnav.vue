<template>
    <div class="leftnav">
      <ul>
        <li v-for="item in navlist" :key="item.name">
            <router-link :to="currentRouter.path+'/'+currentRouter.children[0].path+'/'+item.path">
            {{generateTitle(item.meta.title)}}
            </router-link>
        </li>
      </ul>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { generateTitle } from '@/utils/i18n'
export default {
  data() {
    return {
      currentUrl: this.$route.path,
      currentRouter: undefined,
      navlist: []
    }
  },
  created() {
    this.init()
  },
  methods: {
    generateTitle,
    init() {
      var menuName = this.getCurrentFirstMenu
      this.currentRouter = this.permission_routers.find((value, index, arr) => {
        return value.path === menuName || value.name === menuName
      })
      this.navlist = this.currentRouter.children[0].children
    }
  },
  computed: {
    ...mapGetters([
      'permission_routers'
    ]),
    getCurrentFirstMenu() {
      var path = this.currentUrl
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
    }
  }
}
</script>

<style lang="scss" scoped>
.leftnav{
    ul{
      padding: 0px;
      margin: 0px;
        li{
          list-style: none;
          a{ display: block; }
          a.router-link-active,a:hover{background-color: #ffffff;color: #459d92;}
          height: 64px;
          line-height: 64px;
          color: #ffffff;
          font-size: 18px;
          text-align: center;
        }
    }
}
</style>
