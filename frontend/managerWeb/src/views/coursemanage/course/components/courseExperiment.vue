<template>
 <div class= 'test'>
   <div class = 'test_box' >
      <div > <span class="add_button" v-if="typenumber===0"> <el-button  @click="skipexerciseadd" >添加实验</el-button></span>
      <span class="add_button" v-else></span></div>
          <div class="test_empty" v-if="experimentlist.length===0">暂无实验</div>
    <div v-else class="test_nav" ref='test1' v-for="(item,index) in experimentlist" :key="index">
      <div class="test_top" >
        <span class="experiment_index">实验{{index+1}}：</span>
        <span class = 'white'>{{item.name}}</span>
        <i class='el-icon-delete ico' title="删除实验" @click = "singleDelete(item.id)"></i>
        <i class='el-icon-view ico' title="查看实验" @click="checkexercise(item.id)" ></i>
        <i class='el-icon-edit ico' v-if="typenumber===0" title="编辑实验" @click="editxercise(item.id)" ></i>
        <i v-if="index != experimentlist.length-1" class='el-icon-arrow-down ico' title="下移" @click ="downexercise(experimentlist[index],experimentlist[index+1])" ></i>
        <i v-if="index != 0"  class='el-icon-arrow-up ico' title="上移" @click ="downexercise(experimentlist[index-1],experimentlist[index])"></i>
     </div>
     <div class="test_bottom">
        知识点：{{item.knowledge}}
     </div>
    </div>
   </div>
 </div>
</template>
<script>
 /**
 * @file 教师端课程实验组件
 * @author  张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/4/11
 */
import { del, pageByCourseId, moveExercise } from '@/api/coursemanage/exercise'
 export default {
   props: ['courseid', 'type'],
   data() {
     return {
       input: '',
       typenumber: this.type,
       formdata: {
         page: {
           current: 1,
           pageCount: 10,
           total: 0
         },
         form: {
           courseId: this.courseid // 课程id
         },
         search: '',
         orderBy: 'sort asc'// 根据sort排序
       },
       experimentlist: []// 所有实验数据
     }
   },
   created() {
     this.$nextTick(e => {
       this.init()
       console.log(this.typenumber)
     })
   },
   methods: {
     // 初始化实验
     init() {
       pageByCourseId(this.formdata).then(e => {
         const data = e.data.data
         this.experimentlist = data.records
       }
       )
     },
     // 删除实验
     singleDelete(id) {
     //  debugger
       this.delete([id])
     },
     // 查看课程实验
     checkexercise(id) {
       this.$router.push({ name: 'joinexercise', query: { exerciseid: id }})
     },
     // 上下移动实验顺序
     downexercise(id1, id2) {
       // debugger
       console.log(id1.id)
       console.log(id2.id)
       moveExercise(id1.id, id2.id).then(e => {
         if (e.data.success) {
           this.$sucmsg('移动成功')
           this.init()
         }
       })
     },
     // 删除方法
     delete(ids) {
       this.$cfm('确定删除课程实验吗？', e => {
         del(ids).then(e => {
           if (e.data.success) {
             this.$sucmsg('删除成功')
             this.init()
           }
         })
       })
     },
     // 跳转实验添加页面
     skipexerciseadd() {
       // alert('成功调用')
       // debugger
       this.$router.push({ name: 'addexercise', query: { courseid: this.courseid }})
     },
     editxercise(id) {
       // alert('成功调用')
       // debugger
       this.$router.push({ name: 'addexercise', query: { exerciseid: id }})
     }
   }
 }
</script>
<style scoped lang="scss">
.test{
  margin-right:15px;
}
.test_nav{
 border:1px solid #eee;
 margin-top:32px;
}
.test_top{
   background-color: #eee;
   height:45px;
   line-height: 45px;
   font-size:14px;
   padding: 0 15px;
   .experiment_index{
   }
   .white{
   }
 }
 .test_empty{margin-top: 15px;
   background-color: #eee;
   height:45px;
   line-height: 45px;
   font-size:14px;
   padding: 0 15px;
   text-align: center;
 }
.test_nav .ico{
  float:right;
   line-height: 45px;
   font-size:16px;
   color:#6ea6ed;
   margin:0 6px;
   overflow:hidden;
   font-weight: bolder;
   cursor: pointer;
}

.test_bottom{
  font-size:14px;
  color:#a4a4a4;
  padding: 5px 15px;
  line-height:25px;
}
.add_button{
  //  float: right;
  margin-left: 93%;
   margin-top: -32px;
    // text-align: right;


}
</style>
