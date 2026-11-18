<template>
  <el-menu class="navbar" mode="horizontal">
    <hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened" v-bind:style="{  marginLeft: sidebar.showSecondMenu?'200px':'100px' }"></hamburger>

    <breadcrumb class="breadcrumb-container"  ></breadcrumb>

    <div class="right-menu">
      <!-- <error-log class="errLog-container right-menu-item"></error-log> -->

      <el-tooltip effect="dark" :content="$t('navbar.screenfull')" placement="bottom">
        <screenfull class="screenfull right-menu-item"></screenfull>
      </el-tooltip>

      <!-- <lang-select class="international right-menu-item"></lang-select> -->

      <!-- <el-tooltip effect="dark" :content="$t('navbar.theme')" placement="bottom">
        <theme-picker class="theme-switch right-menu-item"></theme-picker>
      </el-tooltip> -->
       

      <el-dropdown class="avatar-container right-menu-item" trigger="click">
        <div class="avatar-wrapper">
          <el-badge :value="this.$store.state.user.noReadMsgCount" :hidden="(this.$store.state.user.noReadMsgCount)<=0" class="item"><img class="user-avatar" :src="avatar || require('@/assets/images/team-logo.png')"></el-badge>
          <i class="el-icon-caret-bottom"></i>
        </div>
        <el-dropdown-menu slot="dropdown" class="topdownmenu" style="width:220px;">
          <el-dropdown-item class="userInfoItem">
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
          </el-dropdown-item>
          <el-dropdown-item divided>
            <router-link  to="/person/message"  style="display:block;"><span class="message">消息通知</span><span class="messageCount" v-if="this.$store.state.user.noReadMsgCount>0">{{this.$store.state.user.noReadMsgCount}}</span></router-link>
          </el-dropdown-item>
          <el-dropdown-item >
             <router-link  to="/person/userInfo"> <span class="userInfo" style="display:block;">个人资料</span></router-link>
          </el-dropdown-item>
          <!-- <el-dropdown-item >
           <router-link  to="/person/notifySetting">  <span class="notifySetting" style="display:block;">消息设置</span></router-link>
          </el-dropdown-item> 
          <el-dropdown-item >
           <router-link  to="/community/personalScore">  <span class="index" style="display:block;">个人积分</span></router-link>
          </el-dropdown-item>  -->
          <el-dropdown-item >
           <router-link  to="/person/changePassword"> <span class="changePassword" style="display:block;">修改密码</span></router-link>
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
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import ErrorLog from '@/components/ErrorLog'
import Screenfull from '@/components/Screenfull'
import LangSelect from '@/components/LangSelect'
import ThemePicker from '@/components/ThemePicker'

export default {
  components: {
    Breadcrumb,
    Hamburger,
    ErrorLog,
    Screenfull,
    LangSelect,
    ThemePicker

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
      'avatar'
    ])
  },
  methods: {
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
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  position: fixed;
  width: 100%;
  z-index: 1001;
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
        margin-top: 10px;
        position: relative;
        .user-avatar {
          width: 35px;
          height: 35px;
          border-radius: 10px;
        }
        .el-icon-caret-bottom {
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
  .logOut{
    
  }

}
</style>
