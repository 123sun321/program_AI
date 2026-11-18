<template>
<div class="panel-menu">
  <el-menu default-active="1001">
    <template v-for="item in data">
    
      <el-submenu v-if="item.children.length!==0" :index="item.id + ''" :key="item.id">
        <template slot="title">
          <!-- <span>{{item.name}}({{item.count}})</span> -->
            <span>{{item.name}}</span>
        </template>

        <template v-for="child in item.children">
          <el-menu-item :index="child.id + ''" :key="child.id" @click="filterCustomer(child)">
            <el-tooltip class="item" effect="dark" :content="child.name" placement="top">
              <span class="name">{{child.name | ellipsis}}</span>
            </el-tooltip>
            <!-- <span>({{child.count}})</span> -->
            <i class="el-icon-edit" @click.stop="handleUpdate(child)"></i>
          </el-menu-item>
        </template>        

         <div class="add-group"> 
            <slot></slot>
            <el-button @click.stop="handleCreate" size="mini">添加分组</el-button>
         </div>      
      
      </el-submenu>
      <el-submenu v-else-if="item.name.indexOf('全部分组')>-1" :index="item.id + ''" :key="item.id">

        <template slot="title">
          <!-- <span>{{item.name}}({{item.count}})</span> -->
            <span>{{item.name}}</span>
        </template>

        <el-menu-item :index="'0'">
          <div class="add-group"> 
            <slot></slot>
            <el-button @click.stop="handleCreate" size="mini">添加分组</el-button>
         </div>
        </el-menu-item>
      </el-submenu>

      <el-menu-item v-else :key="item.id" :index="item.id + ''" @click="filterCustomer(item)">
        <!-- <span>{{item.name}}({{item.count}})</span> -->
         <span>{{item.name}}</span>
      </el-menu-item>

    </template>
  </el-menu>
  <!-- 模态窗口--添加分组 -->
  <panel-dialog :title="dialogTitle" :isVisible="dialogVisible">
    <el-form ref="postform" :model="postForm" label-width="80px">
      <el-form-item prop="name" label="分组名称" :rules="[{ required: true, message: '请输入分组名称', trigger: 'blur' },
            { min: 1, max: 5, message: '长度在 1 到 5 个字符', trigger: 'blur' }]">
        <el-input v-model="postForm.name" ></el-input>
      </el-form-item>
    </el-form>
    <template slot="footerBtn">
      <el-button v-if="isDeleteGroup" icon="el-icon-setting" type="danger" plain size="mini" @click="deleteGroup">删除此分组</el-button>
      <el-button v-if="dialogTitle == '添加分组'" slot="footerBtn" @click="handleGroup" type="primary" size="mini">{{$t('table.confirm')}}</el-button>
      <el-button v-else slot="footerBtn" @click="handleGroup" type="primary" size="mini">{{$t('table.confirm')}}</el-button>
      <el-button slot="footerBtn" @click="dialogVisible = false" size="mini">{{$t('table.cancel')}}</el-button>
    </template>
  </panel-dialog>
</div>
</template>

<script>

/**
 * props： data
 *
 * event addOrUpdate, filter, deleteGroup
 *
 */

import PanelDialog from '@/components/Panel/PanelDialog.vue' // 弹窗
import { createGroup } from '@/api/group' // 分组管理API
export default {
  components: { PanelDialog },
  data() {
    return {
      dialogTitle: '', // dialog标题
      dialogVisible: false, // 对话框是否可见
      isDeleteGroup: false, // 是不是要删除分组
      postForm: {
        id: 0,
        name: '',
        moduleId: ''
      }
    }
  },
  props: {
    data: Array,
    groupType: String
    // select: Function
    // filterFunc: Function,
    // handleGroupFunc: Function,
    // deleteGroupFunc: Function,
    // filterArgs: Object
  },
  methods: {
    resetForm() { // 初始化数据
      this.postForm = {
        id: undefined,
        name: '',
        moduleId: this.groupType
      }
    },
    handleUpdate(data) {
      this.postForm = Object.assign({}, data) // copy obj
      this.dialogTitle = '编辑分组'
      this.dialogVisible = true
      this.isDeleteGroup = true
    },
    handleCreate() {
      this.resetForm()
      this.dialogTitle = '添加分组'
      this.dialogVisible = true
      this.isDeleteGroup = false
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
    filterCustomer(item) { // 筛选分组用户
      // const func = this.filterFunc
      // console.log('筛选用户分组：'+item.id)
      this.$emit('filter', item)
      // func(item)
    },
    handleGroup() { // 新增或修改分组
      // 判断分组重名
      createGroup(this.postForm).then(response => {
        if (!response.data.data) {
          this.$message({
            message: '分组名已存在',
            type: 'warning'
          })
          return
        } else {
          this.$refs['postform'].validate(e => {
            if (e) {
              this.dialogVisible = false
              this.$emit('addOrUpdate', this.postForm)
            }
          })
        }
      })
    },
    deleteGroup() {
      this.dialogVisible = false
      // const func = this.deleteGroupFunc

      this.$emit('deleteGroup', this.postForm.id)

      // func(this.postForm.id)
    }
  },
  filters: {
    ellipsis(value) {
      if (!value) return ''
      if (value.length > 5) {
        return value.substring(0, 5) + '...'
      }
      return value
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
        span{
          font-size: 12px;
        }
      }
      .el-menu-item {
        background-color: #fff;
        .name {
          font-size: 12px;
          width: 40px;
          // overflow: hidden;
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
    .add-group {
      height: 45px;
      line-height: 45px;
      text-align: center;
      .el-button {
        border: 1px dashed #49a9ee;
      }
    }
  }
}
.panel-menu /deep/ .el-submenu .el-menu-item {
  width: 172px;
  min-width: 172px;
}
</style>

