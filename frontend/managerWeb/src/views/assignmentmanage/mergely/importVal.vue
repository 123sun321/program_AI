<template>
  <el-dialog title="导入优秀作品" :visible.sync="visible" width="40%">
    <recommend ref="form"></recommend>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCodeWithId } from '@/api/coursemanage/recommendWork'
import recommend from './components/recommendPage'
export default {
  data() {
    return {
      visible: false,
      code: null
    }
  },
  components: {
    recommend
  },
  methods: {
    // 保存添加标签
    save() {
      const value = this.$refs.form.getValue()
      getCodeWithId(value).then(e => {
        this.code = e.data.data.codeDto
        this.$emit('getCode', this.code)
      })
      this.close()
    },
    // 打开添加标签弹窗
    open() {
      this.visible = true
    },
    // 关闭添加标签弹窗
    close() {
      this.visible = false
    }
  }
}
</script>

<style>

</style>
