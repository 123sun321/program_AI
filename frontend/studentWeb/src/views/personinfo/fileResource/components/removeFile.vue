<template>
    <div class="page-move">
        <el-form
        ref="form"
        v-loading="loading"
        :model="entity"
        :inline="line"
        label-width="100px"
        class="demo-dynamic">
            <el-tree
                :data="tabelData"
                icon-class="el-icon-success"
                node-key="id"
                @node-click="handleNodeClick"
                :render-content="renderContent"
                :props="defaultProps">
            </el-tree>
      </el-form>
    </div>
</template>

<script>
/*
移动文件窗口
@author 马勇超
@date 2025/03/19 14:00
@version 1.0
*/
import {
  form
} from '@/mixins/form'
import {
  getTreeList // 获取文件夹树形列表
} from '@/api/file/file'
export default {
  mixins: [form],
  data() {
    return {
      loading: false,
      tabelData: [], // 数据
      entity: {},
      data2: [
        {
          id: 1,
          label: '一级 1',
          className: 'iconfont icon-wenjianjiaguanbi',
          children: [{
            id: 4,
            label: '二级 1-1',
            children: [{
              id: 9,
              label: '三级 1-1-1'
            }, {
              id: 10,
              label: '三级 1-1-2'
            }]
          }]
        }
      ],
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      getTreeList().then(res => {
        this.tabelData = res.data.data
        this.loading = false
      })
    },
    handleNodeClick(data) {
      this.entity = data
      // console.log(data)
    },
    renderContent(h, { node, data, store }) {
      return (
        <span>
          <i class={data.className}></i>
          <span>{node.label}</span>
        </span>
      )
    }

  }
}
</script>

<style>
.page-move{
 position: relative;
 border: 1px solid #D0DFE7;
 overflow-y: scroll;
 height: 300px;
}
</style>
