<template>
<div class="elTryChangeCss">
<div class="panel-category">
  <el-menu default-active="2" @open="handleOpen" @close="handleClose">
    <div class="category">分类</div>
    <template v-for="item in data">

      <template v-if="item.children">
        <el-submenu :key="item.id" :index="item.id">
          <template slot="title">{{item.name}}</template>
          <template   v-for="itemChild in item.children">
            <el-menu-item :key="itemChild.id" :index="itemChild.id" @click="filterGoods(itemChild)">
              <el-tooltip class="item" effect="dark" :content="itemChild.name" placement="top">
                <span>{{itemChild.name}}</span>
              </el-tooltip>
            </el-menu-item>
          </template>
        </el-submenu>
      </template>

      <template v-else>
        <el-menu-item :key="item.id" :index="item.id" @click="filterGoods(item)">
          <el-tooltip class="item" effect="dark" :content="item.name" placement="top">
            <span>{{item.name}}</span>
          </el-tooltip>
        </el-menu-item>
      </template>

    </template>
  </el-menu>
</div>
</div>
</template>

<script>
export default {
  data() {
    return {
      dialogTitle: '', // dialog标题
      dialogVisible: false,
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
    },
    handleUpdate(data) {
      this.postForm = Object.assign({}, data) // copy obj
      this.dialogTitle = '编辑分组'
      this.dialogVisible = true
    },
    handleCreate() {
      this.resetForm()
      this.dialogTitle = '添加分组'
      this.dialogVisible = true
    },
    createData() {
      // 添加数据到数据库
      this.dialogVisible = false
      this.$message('添加数据成功')
    },
    updateData() {
      // 修改数据到数据库
      this.dialogVisible = false
      this.$message('修改数据成功')
    },
    handleOpen() {
    },
    handleClose() {
    },
    filterGoods(item) {
      this.$emit('filter', item.id)
    }
  }
}
</script>


<style scoped lang="scss">
.elTryChangeCss{
  .el-submenu__title {
    height: 45px !important;
    line-height: 45px !important;
    min-width: 145px;
    border-bottom: 1px solid #e4e2e2;
  }
  .el-submenu__title.is-active {
    background-color: #ecf6fd;
    &:after {
        transition: transform 0.15s cubic-bezier(0.645, 0.045, 0.355, 1), opacity 0.15s cubic-bezier(0.645, 0.045, 0.355, 1);
        opacity: 1;
        transform: scaleY(1);
    }
  }
  .el-submenu.is-active .el-submenu__title {
    border-bottom-color: #e4e2e2;
  }
  .panel-category {
    .el-menu {
      width: 184px!important;
      min-width: 150px;
      height: 100%;
      border: 1px solid #e4e2e2;
      .category {
        font-size: 12px;
        height: 45px;
        line-height: 45px;
        background-color: #f6f4f4;
        border-bottom: 1px solid #e4e2e2;
        padding-left: 20px;
      }
      .el-menu-item{
        font-size: 12px;
        // background-color: #f6f4f4;
        border-bottom: 1px solid #e4e2e2;
        min-width: 148px!important;
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
      // .el-submenu {//子菜单
      //   background-color: #f6f4f4;
      //   border-bottom: 1px solid #e4e2e2;
      //   .el-menu-item {
      //     background-color: #fff;
      //     .name {
      //       width: 40px;
      //       overflow: hidden;
      //       text-overflow: ellipsis;
      //       display: inline-block;
      //       line-height: 16px;
      //       padding-top: -11px;
      //       transform: translateY(1px);
      //     }
      //     .el-icon-edit {
      //       font-size: 16px;
      //       margin-left: 30px;
      //     }
      //   }
      //   .el-menu-item.is-active {
      //     background-color: #c8e9ff;
      //   }
      // }
      
    }
  }
}


</style>

