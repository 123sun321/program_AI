<template>
    <div class="bottom">
        <div class="leftbtn pull-left">
            <a @click="back"><img :src="require('@/assets/student/tabber_left.png')"  alt=""></a>
            <a @click="next"><img :src="require('@/assets/student/Tabbar_right.png')"  alt=""></a>
            <a @click="refresh"><img :src="require('@/assets/student/Tabbar_Refresh.png')"  alt=""></a>
        </div>
        <span class="lefttext">© {{startyear}}-{{endyear}} 智训云枢 版权所有</span>
        <span class="righttext">{{currentTime}}</span>
    </div>
</template>

<script>
export default {
  data: function() {
    return {
      startyear: '2025',
      endyear: undefined,
      currentTime: ''
    }
  },
  created() {
    var date = new Date()
    this.endyear = date.getFullYear()
    this.updateTime()
    setInterval(this.updateTime, 1000)
  },
  methods: {
    updateTime() {
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      const day = String(now.getDate()).padStart(2, '0')
      const hours = String(now.getHours()).padStart(2, '0')
      const minutes = String(now.getMinutes()).padStart(2, '0')
      const seconds = String(now.getSeconds()).padStart(2, '0')
      this.currentTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    back() {
      this.$router.back(-1)
    },
    next() {
      this.$router.go(1)
    },
    refresh() {
      this.$router.go(0)
    }
  }
}
</script>

<style lang="scss" scoped> 
.bottom{
    background-color: #010101;
    height: 50px;
    color: #666666;
    line-height: 50px;
    position: fixed;
    width: 100%;
    z-index: 1001;
    bottom: 0px;
    padding: 0 15px;
    .leftbtn{
      background-color: #353535;
      height: 34px;
      width: 110px;
      margin: 8px 0;
      border-radius: 16px;
      padding: 0 5px;
      a{
        img{
          padding: 7px 5px;
        }
      }
    }
    .lefttext{
        float: left;
        margin-left: 15px;
    }
    .righttext{
        float:right;
    }
}
</style>
