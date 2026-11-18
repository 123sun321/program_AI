<template>
    <div>
        <home-top></home-top>
        <home-center></home-center>
        <home-bottom></home-bottom>
    </div>
</template>

<script>
import homeTop from './components/homeTop'
import homeCenter from './components/homeCenter'
import homeBottom from './components/homeBottom'
export default {
  components: { homeTop, homeCenter, homeBottom }
}
</script>

<style lang="scss" scoped>

</style>

