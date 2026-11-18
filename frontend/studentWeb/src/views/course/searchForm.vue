<template>
    <div  class="lists">
      <el-row :span="24" class="types">
        <ul><li v-for="(type, index) in types" :key="index" :class="{typeactive: index === typeActiveIndex}" @click="clickType(type, index)">{{type.title}}</li></ul>
      </el-row>
      <el-row :span="24" class="nav">
        <el-col :span="1" class="navLabel" style="margin-top: 4px;">搜索项目</el-col>
        <el-col :span="4" style="margin-left: 9px;"><el-input v-model="input" placeholder="请输入内容"></el-input></el-col>
      </el-row>
      <el-row :span="24" class="nav">
        <el-col :span="1" class="navLabel">参与类型</el-col>
        <el-col :span="22"><ul v-for="(category,index) in joinCategory" :key="index" ><li v-bind:class="{ active:index === joinCategoryActiveIndex}" @click="clickJoinCategory(category,index)">{{category.name}}</li></ul></el-col>
      </el-row>
      <el-row :span="24" class="nav">
        <el-col :span="1" class="navLabel">技术类型</el-col>
        <el-col :span="22"><ul v-for="(category,index) in exerciseCategory" :key="index" ><li v-bind:class="{ active:index === categoryActiveIndex}" @click="onClick(category,index)">{{category.name}}</li></ul></el-col>
      </el-row>
      <el-row :span="24" class="nav">
        <el-col :span="1" class="navLabel">难易程度</el-col>
        <el-col :span="22"><ul v-for="(item,index) in complexitys" :key="index"><li v-bind:class="{ active:index === complexitysActiveIndex}" @click="onClick2(item,index)">{{item.name}}</li></ul></el-col>
      </el-row>
      <el-row :span="24" class="nav">
        <el-col :span="1" class="navLabel">标签</el-col>
        <el-col :span="22"><ul><li v-for="(label, index) in labelList" :key="index" :class="{ active:index === labelActiveIndex}" @click="clickLabel(label, index)">{{ label }}</li></ul></el-col>
      </el-row>
    </div>
</template>

<script>
/**
 * @file 课程库nav组件
 * @author 宋乃乐
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/19
 */
import { fetchItems } from '@/api/dictionary'
import { getLabelList } from '../../api/course/index'
export default {
  props: ['form'],
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
      types: [
        { title: '所有课程', val: 'basic' },
        // { title: '进阶课程', val: 'advanced' },
        { title: '我的收藏', val: 'myMark' }
      ], // 类型
      joinCategory: [
        { name: '全部', val: null },
        { name: '已参与', val: 1 },
        { name: '未参与', val: 0 }
      ], // 参与类型
      typeActiveIndex: 0,
      classTypeActiveIndex: 0,
      categoryActiveIndex: 0,
      complexitysActiveIndex: 0,
      labelActiveIndex: 0,
      joinCategoryActiveIndex: 0,
      exerciseCategory: '', // 技术类型
      labelList: [], // 标签列表
      check: false,
      tableData: [], // 数据列表
      loading: false,
      input: '',
      searchCode: '%'
    }
  },
  created() {
    fetchItems(null, 'course_category').then(e => {
      this.exerciseCategory = e.data.data
      const labelName = {
        name: '全部'
      }
      this.exerciseCategory.unshift(labelName)
    })
    // 顶部选好后, 关联下面的标签,初步渲染全部选择
    this.getLabel('%')
  },
  methods: {
    // 选中类型
    clickType(type, index) {
      if (index === this.typeActiveIndex) {
        return
      }
      this.typeActiveIndex = index
      this.form.courseChooseType = type.val
      this.$emit('changeSearchForm')
    },
    // 选中课程类型
    // clickClassType(classtype, index) {
    //   this.classTypeActiveIndex = index
    //   this.form.techType = classtype.val
    //   this.$emit('changeSearchForm')
    // },
    // 选中技术类型
    onClick(category, Index) {
      if (Index === this.categoryActiveIndex) {
        return
      }
      this.categoryActiveIndex = Index
      this.getLabel(category.name === '全部' ? '' : category.code)
      this.$emit('changeSearchForm')
    },
    // 选中难易程度
    onClick2(item, Index) {
      if (Index === this.complexitysActiveIndex) {
        return
      }
      this.complexitysActiveIndex = Index
      this.form.difficultLevel = item.complexity
      this.$emit('changeSearchForm')
    },
    // 选中标签
    clickLabel(name, index) {
      if (index === this.labelActiveIndex) {
        return
      }
      this.labelActiveIndex = index
      this.form.tag = name
      this.$emit('changeSearchForm')
    },
    // 选中参与类型
    clickJoinCategory(category, index) {
      if (index === this.joinCategoryActiveIndex) {
        return
      }
      this.joinCategoryActiveIndex = index
      this.form.joinCategory = category.val
      this.$emit('changeSearchForm')
    },
    // 获取标签列表 ,根据code 进行查询
    getLabel(code) {
      // const code = this.form.code
      console.log('this.code : ' + code)
      getLabelList(code).then(data => {
        this.labelList = data.data.data
        this.labelList.unshift('全部')
      })
    },
    handleInputChange(val) {
      this.form.searchContent = val
      this.$emit('changeSearchForm')
    },
    handleTagChange(val) {
      const activeIndex = this.labelList.findIndex(item => item === val)
      this.labelActiveIndex = activeIndex
    }
  },
  watch: {
    'input': 'handleInputChange',
    'form.tag': 'handleTagChange'
  }
}
</script>

<style lang="scss" scoped>
.courseLabel {
    border: 1px solid #e8f7ed;
    padding: 5px 10px;
    background: #e8f7ed;
    border-radius: 5px;
    margin-right: 10px;
    color: #1fad4e;
    font-size: 14px;
}
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
        margin: 0 10px;
        cursor: pointer;
        font-size: 14px;
        color: #666;

         padding: 5px 10px;
      float: left;
   }
   }
   .nav{
     margin-top: 20px;
     margin-left: 27px;
     font-size: 16px;
     color: #333;
     .navLabel {
       font-size: 18px;
       color: #666666;
       font-family: "PingFang-SC-Regular";
     }
   }
}
.types{
  ul{
    li{
      font-size: 22px;
      color: #666;
      font-family: "PingFnag-SC-Medium";
      margin: 22px 27px 0 17px;
    }
    .typeactive {
      color: #333;
      font-weight: bolder;
    }
  }
}
</style>
