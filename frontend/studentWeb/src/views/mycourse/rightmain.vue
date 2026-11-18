<template>
    <div class="rightmain">
        <div class="statistics">
            <statistics></statistics>
        </div>
        <div class="content">
          <router-view></router-view>
        </div>
    </div>
</template>

<script>
import statistics from './components/statistics'
export default {
  components: {
    statistics
  }
}
</script>

<style lang="scss" scoped>
.rightmain{
    // display: flex;
    // flex-flow: column wrap;
    .statistics{
        height: 270px;
    }
    .content{
        // flex: 1;
        min-height: 400px;
        background-color: #ffffff;
    }
}
</style>
