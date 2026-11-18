<template>
  <div style="padding-top:15px;">
    <ul class="clearfix">
      <li ref="li" :class="{ activeDown: index === sortItemActiveIndex }" @click="handleClick(index)" v-for="(sortItem, index) in this.sortItemList" :key="index">{{sortItem.name}}</li>
    </ul>
  </div>
</template>
<script>
export default {
  data() {
    return {
      orderBy: '',
      sortItemList: [
        { name: '推荐度', val: 'recommendDegree' },
        { name: '难度', val: 'complexity' },
        { name: '创建时间', val: 'creationTime' },
        { name: '学习人数', val: 'joinNumber' }
      ],
      sortItemActiveIndex: 0
    }
  },
  methods: {
    handleClick(index) {
      const li = this.$refs.li[index]
      if (index === this.sortItemActiveIndex) {
        li.className = li.className === 'activeUp' ? 'activeDown' : 'activeUp'
      }
      this.sortItemActiveIndex = index
      const orderByName = this.sortItemList[index].val
      const isAsc = li.className === 'activeUp' ? 'asc' : 'desc'
      this.orderBy = 'a.' + orderByName + ' ' + isAsc
      this.$emit('changeValue', this.orderBy)
    }
  }
}
</script>
<style lang="scss" scoped>
ul{
  padding-left: 10px;
  z-index: 100;
  li{
    float: left;
    margin: 5px 10px 0 10px;
    padding: 3px 10px;
    cursor: pointer;
    color: #666;
  }
  .activeUp{
    color: #1fad4e;
    border-radius: 4px;
    border-color: #ffffff;
    background: #e8f7ed;
    &:after {
      content: " ↑";
    }
  }
  .activeDown {
    color: #1fad4e;
    border-radius: 4px;
    border-color: #ffffff;
    background: #e8f7ed;
    &:after {
      content: " ↓";
    }
  }
}
</style>
