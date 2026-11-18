<template>
    <div class="resource">
     <el-table
      :data="resource"
      style="width: 100%">
      <el-table-column
        label="文件名">
         <template slot-scope="scope">
            <span class="resource-logo"><img :src="getIcon(scope.row)" alt=""></span>
            <span class="resource-name">{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作" width="120">
        <template slot-scope="scope">
            <el-button @click="handleCopy(scope.row.url,$event)">复制链接</el-button>
        </template>
      </el-table-column>
    </el-table>
    </div>
</template>

<script>
import clip from '@/utils/clipboard' // use clipboard directly
import clipboard from '@/directive/clipboard/index.js'
export default {
  props: ['resource'],
  directives: {
    clipboard
  },
  methods: {
    handleCopy(text, event) {
      clip(text, event)
    },
    clipboardSuccess() {
      this.$message({
        message: '复制成功！',
        type: 'success',
        duration: 1500
      })
    },
    getIcon(row) {
      var strArr = row.url.split('.')
      var type = strArr[strArr.length - 1]
      const filter = this.$options.filters['isImg']
      if (filter(type)) {
        return row.url
      } else {
        return require('@/assets/images/files/file_' + type + '.png')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.resource{
    border: solid 1px #dfdfdf;
    height: 100%;
    /deep/ .el-table tbody .cell{
        line-height: 30px;
    }
    .resource-logo{
        img{
            float: left;
            padding: 0 5px 0 0;
            height: 30px;
        }
    }
}
</style>

