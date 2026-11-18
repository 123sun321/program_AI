<template>
  <el-dialog title="修改学习小组" :visible.sync="visible" width="30%">
    <detail ref="form" :eid="eid" :is-edit="true"></detail>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
 
import detail from './detail'
import { edit } from '@/api/coursemanage/courseGroup'
export default {
   data() {
     return {
       visible: false, // 修改专业信息 弹窗
       eid: 0
     }
   },
   components: {
     detail
   },
   methods: {
     // 保存新建专业
     save() {
       this.$refs.form.validate(e => {
         if (e) {
           const value = this.$refs.form.getValue()
           edit(value).then(e => {
             this.$sucmsg('修改成功')
             this.$emit('success')
             this.close()
           })
         }
       })
     },
     // 打开新建专业弹窗
     open(id) {
       this.eid = id
       this.visible = true
     },
     // 关闭新建专业弹窗
     close() {
       this.eid = 0
       this.$refs.form.reset()
       this.visible = false
     }
   }
}

</script>

<style>

</style>
