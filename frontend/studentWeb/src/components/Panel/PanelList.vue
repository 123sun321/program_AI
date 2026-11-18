<template>
<div class="panel-menu">
  <el-menu default-active="1" @open="handleOpen" @close="handleClose">
    <template v-for="item in data">
      <el-menu-item v-if="item.children.length === 0" :key="item.id" :index="item.id">
        <span>{{item.name}}</span>
      </el-menu-item>

      <el-submenu v-else :index="item.id" :key="item.id">
        <template slot="title">
          <span>{{item.name}}</span>
        </template>        
        <template v-for="child in item.children">          
          <el-menu-item :index="child.id" :key="child.id">
            <span class="name">{{child.name}}</span>           
          </el-menu-item>          
        </template>                
      </el-submenu>
    </template>
  </el-menu>  
</div>
</template>

<script>
export default {
  data() {
    return {
      postForm: {
        id: 0,
        name: ''
      }
    }
  },
  props: {
    data: Array
  },
  methods: {
    resetForm() { // 初始化数据
      this.postForm = {
        id: undefined,
        name: ''
      }
    }
  }
}
</script>


<style lang="scss" scoped>
.panel-menu {
  .el-menu {
    width: 174px;
    height: 100%;
    border: 1px solid #e4e2e2;
    .el-menu-item {
      font-size: 12px;
      background-color: #f6f4f4;
      border-bottom: 1px solid #e4e2e2;
      height: 42px!important;
      line-height: 42px!important;
      &:after {
        content: "";
        position: absolute;
        right: 0;
        top: 0;
        bottom: 0;
        border-right: 3px solid #108ee9;
        transform: scaleY(0.0001);
        opacity: 0;
        transition: transform 0.15s cubic-bezier(0.215, 0.61, 0.355, 1), opacity 0.15s cubic-bezier(0.215, 0.61, 0.355, 1);
      }
    }
    .el-menu-item.is-active {
      background-color: #ecf6fd;
      &:after {
          transition: transform 0.15s cubic-bezier(0.645, 0.045, 0.355, 1), opacity 0.15s cubic-bezier(0.645, 0.045, 0.355, 1);
          opacity: 1;
          transform: scaleY(1);
      }
    }
    .el-submenu {//子菜单
      background-color: #f6f4f4;
      border-bottom: 1px solid #e4e2e2;
      .el-submenu__title{
        width: 174px;
        height: 402px;
        line-height: 42px;
      }
      .el-menu-item {
        background-color: #fff;
        .name {          
          overflow: hidden;
          text-overflow: ellipsis;
          display: inline-block;
          // line-height: 16px;
          padding-top: -11px;
          transform: translateY(1px);
        }
        .el-icon-edit {
          font-size: 16px;
          margin-left: 30px;
        }
      }
      .el-menu-item.is-active {
        background-color: #c8e9ff;
      }
    }    
  }
}

</style>

