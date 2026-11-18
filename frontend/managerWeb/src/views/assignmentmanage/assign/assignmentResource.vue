<template>
 <div class="container">
    <div class="condition-container clearfix">
        <div class="operation-button pull-right">
         <el-button type="primary" @click="add" icon="el-icon-plus">新建</el-button>
         <el-button type="primary" @click="save" icon="el-icon-plus">保存所有</el-button>
        </div>
    </div>
    <div class="table-container">
       <el-form :model="tableData" ref="ruleForm">
      <el-table :data="tableData.assignmentResourceDtos"  v-loading='loading'>

        <el-table-column type="index" label="序号" align="center"></el-table-column>
        <el-table-column label="资源标题">
            <template slot-scope="scope">
               <el-form-item :prop="'assignmentResourceDtos.'+ scope.$index +'.name'" :rules="[{required:true,message:'资源标题不能为空'},{ min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }]">
                  <el-input  type="text" v-model="scope.row.name"></el-input>
                </el-form-item>
            </template>
        </el-table-column>
        <el-table-column label="地址" prop="url"></el-table-column>
        <el-table-column label="排序" sortable>
            <template slot-scope="scope">
               <el-form-item :prop="'assignmentResourceDtos.'+ scope.$index +'.sort'" :rules="[{required:true,message:'排序不能为空'}]">
                  <el-input type="number" v-model="scope.row.sort" style="width:120px;"></el-input>
                </el-form-item>
            </template>
        </el-table-column>
        <el-table-column prop="zip" width="60" label="操作" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" @click="del(scope.row,scope.$index)" icon="el-icon-delete" title="删除" plain></el-button>
            </template>
        </el-table-column>
      </el-table>
       </el-form>
      <page :page="form.page" @changed="getList"></page>
    </div>

    <!-- <div style="text-align:center">
        <el-button @click="onBack">
            上一步
        </el-button>
        <el-button type="primary" @click="onNext">下一步</el-button>
    </div> -->
    <resource ref="add" v-on:chooseList="addList"></resource>
 </div>
</template>

<script>
/**
 * @file 实验资源
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/12
 */
import page from '@/components/page'
import resource from '@/components/resource'
import { fetchpageAssignmentResource, saveAssignmentResource } from '@/api/assignmentmanage/assgin'
export default {
  components: { resource,
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
      this.init()
    })
  },
  methods: {
    // 添加资源
    init() {
      this.getList()
    },
    addList(data) {
      // debugger
      data.forEach((element, index) => {
        this.tableData.assignmentResourceDtos.push({ name: element.title, url: element.sharePath, sort: index + 1 })
      })
    },
    // 添加资源
    add() {
      this.$refs.add.open()
    },
    // 删除资源
    del(item, index) {
      // debugger
      this.tableData.assignmentResourceDtos.splice(index, 1)
    },
    getList() {
      this.loading = true
      // const params = this.$formToParams(this.$refs.form)
      const params = this.form
      params.form = {
        assignment_id: this.assignmentId
      }
      fetchpageAssignmentResource(params).then(e => {
        // debugger
        const data = e.data.data
        this.tableData.assignmentResourceDtos = data.records
        this.form.page.total = data.total
        this.page = data.current
        this.loading = false
      })
    },
    save() {
      this.$refs.ruleForm.validate(e => {
        if (e) {
          saveAssignmentResource(this.tableData).then(e => {
            if (e.data.success) {
              this.$sucmsg('保存成功!')
            } else {
              this.$errmsg('保存失败!')
            }
          })
        }
      })
    }
    // onBack() {
    //   this.$emit('back')
    // },
    // onNext() {
    //   this.$emit('next')
    // }
  }

}
</script>

<style lang="scss" scoped>
.container {
    width: 100%;margin:0 auto;
    .table-container{margin-bottom: 15px;}
}
</style>
