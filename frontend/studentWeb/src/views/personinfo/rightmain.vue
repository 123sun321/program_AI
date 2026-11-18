<template>
    <div class="rightmain">
        <div class="content">
          <router-view></router-view>
        </div>
    </div>
</template>
<script>
export default {
}
</script>

<style lang="scss" scoped>
.rightmain{
    // height: 100%;
    // display: flex;
    // flex-flow: column wrap;
    .content{
        // flex: 1;
        // overflow: auto;
    }
}
</style>
