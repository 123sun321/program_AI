<template>
    <div class="usercenter">
        <div class="leftmenu customscroll">
              <userinfo></userinfo>
             <leftnav></leftnav>  
        </div>
        <div class="content customscroll">
            <rightmain></rightmain>
        </div>
    </div>
</template>

<script>
import rightmain from './rightmain'
import leftnav from '../mycourse/components/leftnav'
import userinfo from '../mycourse/components/userinfo'
export default {
  components: {
    leftnav,
    rightmain,
    userinfo
  }
}
</script>

<style lang="scss" scoped>
.usercenter{
    display: flex;
    flex-flow: row wrap;
    height: 100%;
    .leftmenu{
        height: 100%;
        overflow-y: auto;
        width: 240px;
        background-color: #459d92;
    }
    .content{height:100%;flex: 1;overflow-x: hidden;overflow-y: auto;}
}
</style>
