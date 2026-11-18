<template>
    <div class="lists">
      <el-row :span="24" class="nav" style="margin-bottom:15px;">
        <el-col :span="1" class="navLabel" style="margin-top: 4px;color: #333;">搜索实验</el-col>
        <el-col :span="4" style="margin-left: 9px;"><el-input v-model="input" placeholder="请输入实验名称"></el-input></el-col>
      </el-row>
      <el-row :span="24">
        <el-col :span="1">技术类型</el-col>
          <el-col :span="22"><ul v-for="(category,index) in exerciseCategory" :key="index" ><li v-bind:class="{ active:index === activeIndex}" @click="onClick(category,index)">{{category.name}}</li></ul></el-col>
      </el-row>
      <el-row :span="24" class="nav">
        <el-col :span="1">难易程度</el-col>
          <el-col :span="22"><ul v-for="(item,index) in complexitys" :key="index"><li v-bind:class="{ active:index === active2Index}" @click="onClick2(item,index)">{{item.name}}</li></ul></el-col>
      </el-row>
      <el-row :span="24" class="label">
        <el-col :span="1">标&nbsp;&nbsp;&nbsp;&nbsp;签</el-col>
          <el-col :span="22"><ul v-for="(item,index) in labelList" :key="index"><li v-bind:class="{ active:index === active3Index}" @click="onClick3(item,index)">{{item}}</li></ul></el-col>
      </el-row>
    </div>
</template>

<script>
/**
 * @file 实验nav组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/29
 */
import { getExerciseLabel } from '@/api/mycourse/exercise'
import { fetchItems } from '@/api/dictionary'
export default {
  data() {
    return {
      complexitys: [
        { complexity: 0, name: '全部' },
        { complexity: 1, name: '一星' },
        { complexity: 2, name: '二星' },
        { complexity: 3, name: '三星' },
        { complexity: 4, name: '四星' },
        { complexity: 5, name: '五星' }
      ], // 难易程度
      activeIndex: 0,
      active2Index: 0,
      active3Index: 0,
      exerciseCategory: '',
      searchParam: {
        complexity: '',
        category: '',
        name: ''
      },
      check: false,
      input: '',
      category: '',
      labelList: [], // 标签列表
      tableData: [], // 数据列表
      loading: false
    }
  },
  created() {
    fetchItems(null, 'exercise_category').then(e => {
      this.exerciseCategory = e.data.data
      const labelName = {
        name: '全部'
      }
      this.exerciseCategory.unshift(labelName)
    })
    getExerciseLabel().then(e => {
      this.labelList = e.data.data
      const labelName = '全部'
      this.labelList.unshift(labelName)
    })
  },
  watch: {
    input(newVal, oldVal) {
      this.searchParam.pragramName = newVal
      this.$emit('chooseIndex', this.searchParam)
    }
  },
  // computed: {
  //   Change() {
  //     this.searchParam.pragramName = this.input
  //     this.$emit('chooseIndex', this.searchParam)
  //     return this.searchParam
  //   }
  // },
  methods: {
    // 选中技术类型
    onClick(category, Index) {
      this.activeIndex = Index
      if (category.name === '全部') {
        this.searchParam.category = ''
        getExerciseLabel().then(e => {
          this.labelList = e.data.data
          const labelName = '全部'
          this.labelList.unshift(labelName)
        })
      } else {
        this.searchParam.category = 'exercise_category_' + category.name.toLowerCase()
        this.category = this.searchParam.category
        getExerciseLabel(this.category).then(e => {
          this.labelList = e.data.data
          const labelName = '全部'
          this.labelList.unshift(labelName)
        })
      }
      this.$emit('chooseIndex', this.searchParam)
    },
    // 选中难易程度
    onClick2(item, Index) {
      this.active2Index = Index
      if (item.name === '全部') {
        this.searchParam.complexity = ''
      } else {
        this.searchParam.complexity = item.complexity
      }
      this.$emit('chooseIndex', this.searchParam)
    },
    // 选中标签类型
    onClick3(item, Index) {
      this.active3Index = Index
      if (item === '全部') {
        this.searchParam.name = ''
      } else {
        this.searchParam.name = item
      }
      // const param = {
      //   type: 'label',
      //   name: item
      // }
      this.$emit('chooseIndex', this.searchParam)
    }
  }
}
</script>

<style lang="scss" scoped>
.active{
       color: #1fad4e !important;
       border-radius: 4px;
      border-color:#ffffff;
      background:#e8f7ed;
}
.lists /deep/ .el-col-1{
    min-width:80px;
 }
.lists /deep/ .el-col-22{
     width: calc(100% - 80px);
 }
.lists{
 ul{
      li{
        margin: -5px 10px;
        cursor: pointer;
        font-size: 14px;
        color: #666;
      padding: 5px 10px;
      float: left;
   }
   }
   .nav{
     margin-top: 25px;
     font-size: 16px;
     color: #333;
     .navLabel {
       font-size: 16px;
     }
   }
  .label{
     margin-top: 25px;
     font-size: 16px;
     color: #333;
   }
}
</style>
