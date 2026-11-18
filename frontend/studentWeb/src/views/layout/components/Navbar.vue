<template>
  <el-menu class="navbar el-menu-demo" mode="horizontal" 
  background-color="#1c1c1c"
  text-color="#9e9d9d"
  active-text-color="#fff">
     <div class="logo">
      <img :src="require('@/assets/student/logo.png')" style="height: 40px; width: auto;" />
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
    <div class="right-menu">
      <el-dropdown class="avatar-container right-menu-item" trigger="click">
        <div class="avatar-wrapper">
          <el-badge style="line-height: 0px;" :value="this.$store.state.user.noReadMsgCount" :hidden="(this.$store.state.user.noReadMsgCount)<=0" class="item"><img class="user-avatar" :src="avatar || require('@/assets/images/team-logo.png')"></el-badge>
          <i class="el-icon-caret-bottom"></i>
        </div>
        <el-dropdown-menu slot="dropdown" class="topdownmenu" style="width:220px;">
          <el-dropdown-item class="userInfoItem">
            <router-link  to="/personinfo/personinfo/userinfo">
            <el-row>
              <el-col :span="17">
                <div class="userDisplayName">{{this.$store.getters.name}}</div>
                <div class="schoolName">{{this.$store.state.user.schoolName}}</div>
              </el-col>
              <el-col :span="7">
                <div class="userpic">
                  <img class="user-avatar" :src="avatar || require('@/assets/images/team-logo.png')">
                </div>
              </el-col>
            </el-row>
            </router-link>
          </el-dropdown-item>
          <el-dropdown-item divided>
            <router-link  to="/personinfo/personinfo/message"  style="display:block;"><span class="message">消息通知</span><span class="messageCount" v-if="this.$store.state.user.noReadMsgCount>0">{{this.$store.state.user.noReadMsgCount}}</span></router-link>
          </el-dropdown-item>
          <el-dropdown-item >
             <router-link  to="/personinfo/personinfo/userinfo"> <span class="userInfo" style="display:block;">个人资料</span></router-link>
          </el-dropdown-item>
            <!-- <el-dropdown-item >
           <router-link  to="/person/notifySetting">  <span class="notifySetting" style="display:block;">消息设置</span></router-link>
          </el-dropdown-item>  -->
          <el-dropdown-item >
           <router-link  to="/personinfo/personinfo/password"> <span class="changePassword" style="display:block;">修改密码</span></router-link>
          </el-dropdown-item>
          <el-dropdown-item divided>
            <span class="logOut" @click="logout" style="display:block;">{{$t('navbar.logOut')}}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>     
    </div>
  </el-menu>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import { generateTitle } from '@/utils/i18n'
import SubMenu from './SubMenu'
export default {
  components: {
    SubMenu
  },
  data() {
    return {
      message: {
        count: 0
      }
    }
  },
  created() {
    this.$nextTick(e => {
      this.getNoReadCount()
    })
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'name',
      'avatar',
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
  methods: {
    generateTitle,
    ...mapMutations([
      'SHOW_SECONDMENU'
    ]),
    toggleSideBar() {
      // this.$store.dispatch('toggleSideBar')
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload()// In order to re-instantiate the vue-router object to avoid bugs
      })
    },
    // 获取用户的未读消息数据
    getNoReadCount() {
      this.$store.dispatch('GetNoReadMsgCount').then(() => {
      })
    },
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

<style rel="stylesheet/scss" lang="scss" scoped>
 .el-dropdown-menu--small .el-dropdown-menu__item{line-height: 40px;}
 .userInfoItem{margin: 5px 0px 15px 0px;}
 .userInfoItem .userDisplayName{line-height: 30px; color: #000; font-weight: bold;font-size: 14px;}
 .userInfoItem .schoolName{line-height: 25px; color: #928c8c; }
 .userInfoItem .userpic .user-avatar{width: 50px; height: 50px;border-radius: 50%;}
 .messageCount{border-radius: 15px;
    background-color: red;
    min-width: 25px;
    height: 20px;
    display: inline-grid;
    /* float: left; */
    margin-top: 10p;
    line-height: 22px;
    text-align: center;
    color: #fff;
    margin-left: 5px;}
 .navbar {
  border:none;
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  position: fixed;
  width: 100%;
  z-index: 1001;
  .logo{height: 49px; float: left; width: auto; padding: 8px 15px;}
  .el-menu-item{height: 49px;line-height: 49px;border:none;}
  /deep/ .el-submenu .el-submenu__title{height: 49px;line-height: 49px;border:none;}
  .el-submenu>.el-menu{top: 50px;}
  .router-link-active>li{
    background-color: #3a3a3a!important;color: #fff!important;
  }
  .el-menu-item:hover{background-color: #3a3a3a!important;color: #fff!important;}
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .breadcrumb-container{
    float: left;
  }
  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }
  .right-menu {
    float: right;
    height: 100%;
    &:focus{
     outline: none;
    }
    .right-menu-item {
      display: inline-block;
      margin: 0 8px;
    }
    .screenfull {
      height: 20px;
    }
    .international{
      vertical-align: top;
    }
    .theme-switch {
      vertical-align: 15px;
    }
    .avatar-container {
      height: 50px;
      margin-right: 30px;
      .avatar-wrapper {
        cursor: pointer;
        margin-top: 0px;
        position: relative;
        .user-avatar {
          width: 35px;
          height: 35px;
          border-radius: 10px;
        }
        .el-icon-caret-bottom {
          position: absolute;
          right: -20px;
          top: 20px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
