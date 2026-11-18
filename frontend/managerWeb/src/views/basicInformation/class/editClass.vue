<template>
    <!-- 修改班级 -->
    <el-dialog   title="修改班级" :visible.sync="visible"  width="30%">
        <Class ref="form" :eid="eid" :is-edit="true"></Class>
        <div slot="footer" class="dialog-footer">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </div>
    </el-dialog>
</template>
  
<script>
/**
 * @file 编辑班级组件
 * @author 赵雪丽
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/06
 **/
import Class from './components/Class'
import {
  updateClass
} from '@/api/base/class'
export default {
  data() {
    return {
      visible: false, // 是否显示修改班级弹窗
      eid: 0 // 所修改班级的Id
    }
  },
  components: {
    Class
  },
  methods: {
    // 保存修改班级
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          updateClass(value).then(e => {
            if (e.data) {
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
    // 打开修改班级弹窗 id [String] 获取所修改的班级弹窗Id
    open(id) {
      this.eid = id
      this.visible = true
    },
    // 关闭修改班级弹窗
    close() {
      this.eid = 0
      this.$refs.form.reset()
      this.visible = false
    }
  }
}
</script>
