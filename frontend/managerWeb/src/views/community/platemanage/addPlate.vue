<template>
<el-dialog title="新增板块信息" :visible.sync="visible" width="40%">
    <plate ref="form" :is-edit="false"></plate>
    <div slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
    </div>
</el-dialog>
</template>

<script>
/**
 * @file 新增板块信息
 * @author 张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/4/01
 */
import Plate from './components/Plate'
import {
  add
} from '@/api/community/platemanage'
export default {
  data() {
    return {
      visible: false
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
