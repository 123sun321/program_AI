<template>
  <el-container class="my-container">
    <el-aside width="400px" style="padding-bottom:20px;">
      <div class="dict-title ">字典组</div>
      <el-tree
        :data="treeSource"
        :props="props"        
        node-key="id"
        default-expand-all
        :expand-on-click-node="false"
        :render-content="renderContent"
        @node-click="onNodeClick">
      </el-tree>     
    </el-aside>
    <el-main style="padding-bottom:20px;">
      <div class="dict-title opc">
        字典项 <el-button type="primary" @click="onAddItem">添加项</el-button>
      </div>
      <el-table stripe style="width: 100%" :data="tableSource">         
        <el-table-column prop="code" label="代码"></el-table-column> 
        <el-table-column prop="name" label="名称"></el-table-column>   
        <el-table-column prop="sort" label="排序"></el-table-column>     
        <el-table-column prop="oper" label="操作">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" @click="onEditItem(scope.row)"></el-button>
            <el-button type="danger" icon="el-icon-delete" @click="onDelItem(scope.row.id)"></el-button>
          </template>
        </el-table-column>       
      </el-table>     
    </el-main>
    <add-dictionary ref="add" @success="getList"></add-dictionary>
    <edit-dictionary ref="edit" @success="getList"></edit-dictionary>
    <add-dictionary-item ref="addItem" @success="getList"></add-dictionary-item>
    <edit-dictionary-item ref="editItem" @success="getList"></edit-dictionary-item>
  </el-container>
</template>

<script>
import { fetchList, del, delItem } from '@/api/dictionary'
import addDictionary from './addDictionary'
import editDictionary from './editDictionary'
import addDictionaryItem from './addDictionaryItem'
import editDictionaryItem from './editDictionaryItem'
/**
 * 字典
 */
export default {
  components: {
    addDictionary,
    editDictionary,
    addDictionaryItem,
    editDictionaryItem

  },
  data() {
    return {
      source: [], // 元数据
      treeSource: [], // 字典树的值,
      selectTreeId: 0, // 选中的树,

      // 树形配置
      props: {
        label: 'name'
      }
    }
  },
  created() {
    this.getList()
  },
  computed: {
    tableSource() {
      const selectTree = this.source.find(b => this.selectTreeId === b.id)
      if (selectTree) { return selectTree.items } else { return [] }
    }
  },
  methods: {
    onNodeClick(data, node, tree) {
      if (data.id !== 0) { this.selectTreeId = data.id }
    },
    getList() {
      fetchList().then(e => {
        this.source = e.data.data
        const rtn = [
          {
            id: 0,
            code: 0,
            name: '数据字典',
            children: this.source
          }
        ]
        if (this.source.length > 0) {
          const len = this.source.filter(b => b.id === this.selectTreeId).length
          if (len === 0) { this.selectTreeId = this.source[0].id }
        }
        this.treeSource = rtn
      })
    },
    onAdd() {
      this.$refs.add.open()
    },
    onEdit(id) {
      this.$refs.edit.open(id)
    },
    onDel(id) {
      this.$cfm('确定删除字典和字典项吗？', e => {
        del(id).then(e => {
          this.$sucmsg('删除成功')
          this.getList()
        })
      })
    },
    onAddItem() {
      if (this.selectTreeId === 0) {
        this.$wranmsg('请选择字典分类')
        return
      } else {
        this.$refs.addItem.open(this.selectTreeId)
      }
    },
    onEditItem(data) {
      this.$refs.editItem.open(data.id, data.dictionaryId)
    },
    onDelItem(id) {
      this.$cfm('确定删除该字典项吗？', e => {
        delItem(id).then(e => {
          this.$sucmsg('删除成功')
          this.getList()
        })
      })
    },
    renderContent(h, { node, data, store }) {
      return (
        <span class='customer-tree-item' style={this.selectTreeId === data.id ? 'color:#409EFF' : ''}>
          <span>
            <span><i class='el-icon-tickets'></i> {node.label}{data.id === 0 ? '(代号)' : '(' + data.code + ')'}</span>
          </span>
          {data.id === 0
            ? <span>
              <el-button style='font-size: 12px;' type='text' on-click={ () => this.onAdd() }><i class='el-icon-circle-plus'></i></el-button>
            </span>
            : <span>
              <el-button style='font-size: 12px;' type='text' on-click={ () => this.onEdit(data.id) }><i class='el-icon-edit'></i></el-button>
              <el-button style='font-size: 12px;' type='text' on-click={ () => this.onDel(data.id) }><i class='el-icon-delete'></i></el-button>
            </span>
          }
        </span>)
    }
  }
}
</script>

<style scoped lang="scss">
.el-aside{margin-right: 10px;}
.el-aside,.el-main{
    background-color: #fff;
    -webkit-box-shadow: 0 0 1px 0 rgba(0,0,0,.2);
    box-shadow: 0 0 1px 0 rgba(0,0,0,.2);
    padding: 0px 20px;
}
.dict-title {
  padding: 15px 0px;
}
.my-container /deep/ .el-main{
  padding-top:0px; 
}
.opc{
  display: flex;
  justify-content:space-between;
}
.my-container /deep/ .customer-tree-item{
  flex: 1; 
  display: flex;
  align-items: center; 
  justify-content: space-between;
  padding-right: 8px;
  font-size: 16px;
}
</style>
