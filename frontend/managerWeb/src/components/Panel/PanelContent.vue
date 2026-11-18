<template> 
  <div class="content-card">
    <div class="content-header">      
      <div class="content-button"><slot name="actionButton"></slot></div>
    </div>
    <div class="content-body">
      <el-container width="200px">
        <el-aside> 
          <slot name="leftList"></slot>                             
        </el-aside>
        <el-main>          
          <slot name="rightList"></slot>
        </el-main>
      </el-container>
      <slot></slot>
    </div>
  </div>     
</template>

<script>
export default {
  props: {
    title: String,
    isVisible: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    // isVisible: function() {
    //   if (this.title) {
    //     return true
    //   } else {
    //     return false
    //   }
    // }
  }
}
</script>