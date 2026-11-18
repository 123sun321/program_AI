<template>
 <div class="container customscroll">
    <div class="left">
      <el-table :data="tableData.assignmentResourceDtos"  v-loading='loading'>
        <el-table-column type="index" label="序号" align="center"></el-table-column>
        <el-table-column label="标题" align="center">
            <template slot-scope="scope"  >
             <div class="tit" style="margin:10px">
                  <div class="stu-img">
                      <img :src="file_type(scope.row)" >
                    </div>
                    <div class="stu-info">
                        <span class="stuName">{{scope.row.name}}</span>
                    </div>
             </div>
            </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
            <template slot-scope="scope">
               <el-button size="small" style="color:#1ABC9C" @click="handleCopy(scope.row.url,$event)">复制链接</el-button>
            </template>
        </el-table-column>
      </el-table>
      <!-- <page :page="form.page" @changed="getList"></page> -->
    </div>
 </div>
</template>

<script>
/**
 * @file 作业资源
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/05/5
 */
import page from '@/components/page'
import clip from '@/utils/clipboard' // use clipboard directly
import clipboard from '@/directive/clipboard/index.js'
import { fetchpageAssignmentResource } from '@/api/assignment/index'
export default {
  directives: {
    clipboard
  },
  components: {
    page },
  props: ['assignmentId'],
  data() {
    return {
      form: {
        page: {
          current: 1,
          pageCount: 10,
          total: 0
        },
        assignment_id: this.assignmentId
      },
      tableData: {
        assignmentid: this.assignmentId,
        assignmentResourceDtos: []
      },
      loading: false
    }
  },
  created() {
    this.$nextTick(e => {
      this.getList()
    })
  },
  methods: {
    file_type(item) {
      const index = item.url.lastIndexOf('.') + 1
      const ItemUrl = item.url.substring(index, item.url.length)
      if (ItemUrl === 'jpeg' || ItemUrl === 'jpg' || ItemUrl === 'png') {
        return item.url
      } else {
        return require('@/assets/images/files/file_' + ItemUrl + '.png')
      }
    },
    clipboardSuccess() {
      this.$message({
        message: '复制成功！',
        type: 'success',
        duration: 1500
      })
    },
    handleCopy(text, event) {
      clip(text, event)
    },
    // // 引入资源
    // addResources(item) {

    // },
    getList() {
      this.loading = true
      // const params = this.$formToParams(this.$refs.form)
      const params = this.form
      params.form = {
        assignment_id: this.assignmentId
      }
      fetchpageAssignmentResource(params).then(e => {
        const data = e.data.data
        this.tableData.assignmentResourceDtos = data.records
        this.form.page.total = data.total
        this.page = data.current
        this.loading = false
      })
    }
  }

}
</script>

<style lang="scss" scoped>
.container {
      height: 100%;
      overflow-y: auto;
}
.left{
    border: solid 1px #dfdfdf;
    height: 100%;
    padding:0 10px;
}
.stu-img{
    margin-right: 10px;
    }
.stu-img>img{
      height: 48px;
    width: 48px;
    }
.tit{
  display: flex;
  flex-wrap: row;
  align-items: center;
}
   .stu-info{
       height: 48px;
       text-align: left;
      display:inline-block;
    }
   .stuName{
     flex:1;
     color:#515151;
      //  overflow: hidden;
    }
</style>
