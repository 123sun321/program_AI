<template>
  <el-dialog title="添加字典信息" :visible.sync="visible" width="30%">
    <dictionary ref="form" :is-edit="false"></dictionary>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import Dictionary from './components/Dictionary'
  import {
    add
  } from '@/api/dictionary'
  export default {
    data() {
      return {
        visible: false
      }
    },
    components: {
      Dictionary
    },
    methods: {
      save() {
        this.$refs.form.validate(e => {
          if (e) {
            const value = this.$refs.form.getValue()
            console.log(value)
            add(value).then(e => {
              if (e.data.success) {
                this.$sucmsg('保存成功')
                this.$emit('success')
                this.$refs.form.reset()
                this.close()
              } else {
                this.$errmsg('保存失败')
              }
            })
          }
        })
      },
      open() {
        this.visible = true
      },
      close() {
        this.visible = false
      }
    }
  }

</script>

<style>

</style>
