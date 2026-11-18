<template>
  <div class="dataGridBox">
    <h4 class="dk-font400" v-if="title!=''"><i class="dk-mr5"><img src="/src/assets/images/icon/u30.png" class="dk-title-icon" alt=""></i>{{title}}</h4>
    <div class="content">
      <div v-for="(d,index) in data" :key="index" class="dk-flex">
        <div v-for="(c,index) in d.childArr" :key="index" class="dk-flex dk-flex-item">
          <div class="grid-content grid-one dk-flex-item">{{c.label}}</div>
          <div class="grid-content grid-two dk-flex-item">{{c.val}}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
/**
 * @file 班级详情组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/4/15
 **/
export default {
  props: ['data', 'title']
  // mounted() {
  //   var cArr1 = document.getElementsByClassName('grid-one')
  //   var cArr2 = document.getElementsByClassName('grid-two')
  //   for (var i = 0; i < cArr1.length; i++) {
  //     cArr1[i].style.height = cArr2[i].style.height
  //   }
  // }
}
</script>


<style lang="scss" scoped>
.grid-content{
    border:1px solid #E4E4E4;
    padding: 10px;
    font-size: 14px;

}
.grid-one{
        border-right: 0px;
        border-left: 0px;
        border-bottom: 0px;
        background-color:#F9F9F9;
        text-align: right;
        min-height: 37px;
    }
    .grid-two{
        border-bottom: 0px;
        min-height: 37px;
    }
    .content{
        border-bottom:1px solid #E4E4E4;
        border-left:1px solid #E4E4E4;
    }
</style>
