<template>
    <el-dialog   title="修改标签信息" :visible.sync="visible"  width="30%">
        <Label ref="form" :eid="eid" :is-edit="true"></Label>
        <div slot="footer" class="dialog-footer">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </div>
    </el-dialog>
</template>
  
<script>
import Label from './components/Label'
import {
  updateLabel
} from '@/api/base/label'
export default {
  data() {
    return {
      visible: false, // 是否显示修改标签弹窗
      eid: 0 // 所修改标签的Id
    }
  },
  components: {
    Label
  },
  methods: {
    // 保存修改标签
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          updateLabel(value).then(e => {
            if (e.data.data) {
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
    // 打开修改标签弹窗 id [String] 获取所修改的标签弹窗Id
    open(id) {
      this.eid = id
      this.visible = true
    },
    // 关闭修改标签弹窗
    close() {
      this.eid = 0
      this.$refs.form.reset()
      this.visible = false
    }
  }
}
</script>
