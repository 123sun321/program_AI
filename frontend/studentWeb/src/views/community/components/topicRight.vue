<template>
<div>
  <el-button type="success" style="margin-bottom:15px;"  @click="addTheme()">我要发帖</el-button>
      <div class="report-card">
        <div class="title">相关帖子</div>
        <div class="correlation-report">
           <ul class="report-item" style="padding-left:20px;" v-for="item in list" :key="item.id">
           <li @click="onclick(item.id)">{{item.title}}</li>
         </ul>
        </div>
      </div>
      <add-Theme ref="add"></add-Theme>
</div>
</template>

<script>
/**
 * @file  发帖右侧盒子组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/17
 */
import addTheme from './addTheme'
export default {
  props: ['list'],
  components: {
    addTheme

  },
  data() {
    return {
    }
  },
  created() {
  },
  methods: {
    // 添加帖子
    addTheme() {
      this.$refs.add.open()
    },
    onclick(id) {
      this.$emit('headCallBack', id)
    }
  }
}
</script>

<style lang="scss" scoped>
.el-button {
    width: 100%;
    height: 70px;
}
.el-button--small {
    font-size: 22px;
}
ul li{
  list-style: disc;
}
.c_btn{
  margin: 15px 0px;
        color: #1fad4e;
        border-color:#1fad4e;
        background:#fff;
        border-radius: 4px;
 }
    .c_btn:hover{border-color:#1fad4e; }
.report-card{
    background-color: #ffffff;
    border-radius: 5px;
    padding: 10px 20px 20px 20px;
    .title{
        line-height: 50px;
        font-size: 22px;
        color: #666666;
        border-bottom:1px solid #eeeeee;
    }
    .correlation-report{
        padding: 10px 0;
        font-size: 15px;
        .report-item{
          // margin-left: -10px;
            padding: 12px 0px;
            color: #4c5157;
            position: relative;
            .time{
               position:absolute;
               right: 0px;
             color: #1fad4e;
            }
        }
        .report-item:hover{
            cursor: pointer;
            color: #4c5157;
            background-color: #f9f9f9;
        }
    }
}
</style>
