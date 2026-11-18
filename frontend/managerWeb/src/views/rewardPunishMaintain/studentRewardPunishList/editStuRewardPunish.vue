/*
 * @Author: 黄鹏举
 * @Date: 2018-12-04 17:52:38 
 * @Last Modified time: 2018-12-04 19:26:11
 */
<template>
  <el-dialog title="修改奖惩" :visible.sync="visible"  width="635px">
     <reward-punish ref="form"  :eid="eid" :is-edit="true"></reward-punish>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
/**
 * @description 修改学生奖惩列表
 * @author【黄鹏举】
 * @since 2018/12/4
 * @version 1.0
 */
import rewardPunish from './components/rewardPunish'
import {
  edit
} from '@/api/base/studentRewardPunish'
export default {
  data() {
    return {
      visible: false,
      eid: 0
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
          console.log(value)
          edit(value).then(e => {
            if (e.data.success) {
              this.$sucmsg('修改成功')
              this.$emit('success')
              this.close()
            } else {
              this.$errmsg('修改失败')
            }
          })
        }
      })
    },
    // 打开组件 id需要修改数据的id
    open(id) {
      this.eid = id
      this.visible = true
    },
    // 关闭组件并重置表单
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
