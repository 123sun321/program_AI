<template>
 <div class="container">
    <div class="condition-container clearfix">
        <div class="operation-button pull-right">
         <el-button type="primary" @click="add" icon="el-icon-plus">从我的资源中选择</el-button>
        </div>
    </div>
    <div class="table-container">
      <el-form :model="entity" ref="ruleForm">
        <el-table :data="entity.exerciseResourceDtoList"  v-loading='loading'>
          <el-table-column type="index" label="序号" align="center"></el-table-column>
          <el-table-column label="资源标题">
              <template slot-scope="scope">
                <el-form-item :prop="'exerciseResourceDtoList.'+ scope.$index +'.name'" :rules="[{required:true,message:'资源标题不能为空'},{ min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }]">
                  <el-input  type="text" v-model="scope.row.name"></el-input>
                </el-form-item>
              </template>
          </el-table-column>
          <el-table-column label="地址" prop="url"></el-table-column>
          <el-table-column label="排序">
              <template slot-scope="scope">
                <el-form-item :prop="'exerciseResourceDtoList.'+ scope.$index +'.sort'" :rules="[{required:true,message:'排序不能为空'}]">
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
    </div>
    <div style="text-align:center">
        <el-button @click="onBack">
            上一步
        </el-button>
        <el-button type="primary" @click="onNext">下一步</el-button>
    </div>
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
import resource from '@/components/resource'
export default {
  components: { resource },
  props: ['entity'],
  data() {
    return {
      loading: false
    }
  },
  created() {
  },
  methods: {
    // 添加资源
    addList(data) {
      data.forEach((element, index) => {
        this.entity.exerciseResourceDtoList.push({ name: element.title, url: element.sharePath, sort: index + 1 })
      })
    },
    // 添加资源
    add() {
      this.$refs.add.open()
    },
    // 删除资源
    del(item, index) {
      this.entity.exerciseResourceDtoList.splice(index, 1)
    },
    onBack() {
      this.$emit('back')
    },
    onNext() {
      this.$refs.ruleForm.validate(e => {
        if (e) {
          this.$emit('next')
        }
      })
    }
  }

}
</script>

<style lang="scss" scoped>
.container {
    width: 90%;margin:0 auto;
    .table-container{margin-bottom: 15px;}
}
</style>
