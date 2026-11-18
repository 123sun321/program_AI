<template>
    <div class="contents">
        <div class="new">
          <el-button class="button" icon="el-icon-share" @click="share">发布</el-button>
        </div>
        <el-tabs v-model="activeName" type="border-card">
          <el-tab-pane label=" 编辑" name="baseinfo">
            <mark-down v-model="report.content"></mark-down>
          </el-tab-pane>
        </el-tabs>
    </div>
</template>
<script>
/**
 * @file 实验报告组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/11
 */
import { add, edit, getByExerciseId } from '@/api/mycourse/report'
import markDown from '@/components/markDown'
export default {
  components: {
    markDown
  },
  props: ['exerciseid'],
  data() {
    return {
      content: undefined,
      // report: { exerciseJoinDto: { exercise: {}, student: {}}}
      report: {
        content: '',
        exerciseId: this.exerciseid,
        privateReport: ''
      },
      activeName: 'baseinfo'
    }
  },
  created() {
    this.$nextTick(e => {
      this.init()
    })
  },
  methods: {
    init() {
      getByExerciseId(this.exerciseid).then(response => {
        if (response.data.data) {
          this.report = response.data.data
        }
      })
    },
    share() {
      if (this.report.content) {
        if (this.report.content.length <= 50) {
          this.report.privateReport = true
        } else {
          this.report.privateReport = false
        }
        if (this.report.id) {
          edit(this.report).then(e => {
            if (e.data.success) {
              this.$sucmsg('修改成功！')
            }
          })
        } else {
          add(this.report).then(e => {
            if (e.data.success) {
              this.$sucmsg('发布成功！')
            }
          })
        }
      } else {
        this.$sucmsg('报告不能为空！')
      }
    }
    // 判断报告字数是否满五十字，若不满五十字默认为私有报告
  }
}
</script>
<style lang="scss" scoped>
.contents{
  position: relative;
  height: 100%;
}
.contents /deep/.el-tabs--border-card {
    height: 100%;
}
.contents /deep/ .el-tabs__content {
    position: relative;
}
.button{
   border: 1px solid #2D2E3F;
   background-color: #2D2E3F;
  color: #fff;
}
.contents /deep/ .el-tabs--border-card>.el-tabs__header .el-tabs__item:hover {
    color: #2D2E3F;
}
.contents /deep/.el-tabs--border-card>.el-tabs__header .el-tabs__item.is-active {
    color: #2D2E3F;
    background-color: #fff;
    border-right-color: #dcdfe6;
    border-left-color: #dcdfe6;
}
.new{
  float: right;
  z-index: 100;
   right: 15px;
   top: 3px;
  position: relative;
}
</style>
