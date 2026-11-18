<template>
  <el-dialog title="选择分组" :visible.sync="isVisible" width="30%" :before-close="handleClose">
    <span>
      <el-radio-group v-model="selectedGroup" size="mini">
        <el-radio v-for="group in data" :key="group.id" :label="group.id" border>{{group.name}}</el-radio>
      </el-radio-group>
    </span>
    <span slot="footer" class="dialog-footer">                    
      <el-button type="primary" @click="addToGroup" size="mini">确 定</el-button>              
      <el-button @click="handleClose" size="mini">取 消</el-button>            
    </span>
  </el-dialog>
</template>

<script>
export default {
  props: {
    isVisible: Boolean,
    data: Array
  },
  data() {
    return {
      selectedGroup: undefined,
      inputVisible: false
    }
  },
  methods: {
    addToGroup() {
      if (this.selectedGroup) {
        // this.isVisible = false // 关闭弹窗
        this.$emit('addToGroup', this.selectedGroup)
      } else {
        this.$notify({
          title: '提示',
          message: '请选择分组',
          type: 'info',
          duration: 2000
        })
      }
    },
    showInput() {
      this.inputVisible = true
      this.$nextTick(_ => { // 显示分组输入文本框
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    handleClose() {
      this.$emit('ievent', false)
    }
  }
}
</script>

