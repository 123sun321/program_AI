<!--添加班级-->  
<template>
    <el-dialog   title="新建班级" :visible.sync="visible"  width="30%">
      <Class ref="form" :is-edit="false"></Class>
      <div slot="footer" class="dialog-footer">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
</template>
 
<script>
/**
 * @file 添加班级组件
 * @author 赵雪丽
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/06
 */
import Class from './components/Class'
import {
  setClass
} from '@/api/base/class'
export default {
  data() {
    return {
      visible: false // 是否显示添加班级弹窗
    }
  },
  components: {
    Class
  },
  methods: {
    // 保存添加班级
    save() {
      this.$refs.form.validate(e => {
        if (e) {
          const value = this.$refs.form.getValue()
          value.professionId = value.professionId
          setClass(value).then(e => {
            if (e.data) {
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
    // 打开添加班级弹窗
    open() {
      this.visible = true
    },
    // 关闭添加班级弹窗
    close() {
      this.visible = false
    }
  }
}

</script>