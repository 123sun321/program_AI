<template>
  <el-dialog title="修改板块信息" :visible.sync="visible" width="55%">
    <plate ref="form" :eid="eid" :is-edit="true"></plate>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  /**
 * @file 编辑板块组件
 * @author 张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/4/1
 */
  import Plate from './components/Plate'
  import {
    edite
  } from '@/api/community/platemanage'
  export default {
    data() {
      return {
        visible: false,
        eid: ''
      }
    },
    components: {
      Plate
    },
    methods: {
      save() {
        this.$refs.form.validate(e => {
          if (e) {
            const value = this.$refs.form.getValue()
            console.log(value)
            edite(value).then(e => {
              if (e.data.success) {
                this.$sucmsg('保存成功')
                this.$emit('success')
                this.close()
              } else {
                this.$errmsg('保存失败')
              }
            })
          }
        })
      },
      open(id) {
        this.eid = id
        this.visible = true
      },
      close() {
        this.eid = ''
        this.visible = false
        this.$refs.form.reset()
      }
    }
  }

</script>

<style>

</style>
