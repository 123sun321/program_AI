/*
 * @Author: 黄鹏举
 * @Date: 2018-12-04 17:52:38 
 * @Last Modified time: 2018-12-04 19:26:11
 */
<template>
  <el-dialog title="添加奖惩" :visible.sync="visible" width="635px" @close="close">
    <reward-punish ref="form" :is-edit="false"></reward-punish>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
/**
 * @description 添加学生奖惩列表
 * @author【黄鹏举】
 * @since 2018/12/4
 * @version 1.0
 */
import rewardPunish from './components/rewardPunish'
import {
  add
} from '@/api/base/studentRewardPunish'
export default {
  data() {
    return {
      visible: false
    }
  },
  // 组件
  components: {
    rewardPunish
  },
  methods: {
    // 保存方法
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
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
    // 打开组件
    open() {
      this.visible = true
    },
    // 关闭组件并重置表单
    close() {
      this.$refs.form.reset()
      this.$refs.form.load()
      this.visible = false
    }
  }
}

</script>

<style>

</style>
