<template>
  <el-dialog title="修改字典项信息" :visible.sync="visible" width="30%">
    <dictionary ref="form" :dictionary-id="dictionaryId" :eid="eid" :is-edit="true"></dictionary>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import Dictionary from './components/DictionaryItem'
  import {
    editItem
  } from '@/api/dictionary/index'
  export default {
    data() {
      return {
        visible: false,
        dictionaryId: 0,
        eid: 0
  
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
            editItem(value).then(e => {
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
      open(id, dictionaryId) {
        this.eid = id
        this.dictionaryId = dictionaryId
        this.visible = true
      },
      close() {
        this.eid = 0
        this.visible = false
        this.$refs.form.reset()
      }
    }
  }

</script>

<style>

</style>
