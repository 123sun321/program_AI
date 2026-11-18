<template>
  <el-dialog title="添加班级信息" :visible.sync="visible" width="30%">
    <Class ref="form" :is-edit="false" :init-entity="entity"></Class>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
/**
 * @file 导入班级组件
 * @author 赵雪丽
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/06
 */
import Class from './components/Class'
export default {
  components: {
    Class
  },
  data() {
    return {
      entity: {}, // 班级信息
      visible: false // 是否显示弹窗
    }
  },
  methods: {
    // 显示班级弹窗 entity [Object] 获取班级信息
    open(entity) {
      this.entity = entity
      if (this.entity.inSchool === true) {
        this.entity.inSchool = 1
      } else {
        this.entity.inSchool = 0
      }
      this.visible = true
    },
    // 保存班级信息
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          this.entity.success = true
          this.entity.tips = ''
          this.visible = false
          this.$emit('success', this.entity)
        } else {
          this.$errmsg('验证不通过')
        }
      })
    },
    // 关闭班级弹窗
    close() {
      this.visible = false
    }
  }
}

</script>

<style>

</style>
