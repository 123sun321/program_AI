<template>
<div class="maincontent">
    <p class="callback"><el-button @click="callback">返回</el-button></p>
    <el-steps align-center :active="activeStep" finish-status="success" process-status="success">
        <el-step title="基本信息"></el-step>
        <el-step title="初始数据"></el-step>
        <el-step title="实验详情"></el-step>
        <el-step title="实验资源"></el-step>
        <el-step title="创建实验"></el-step>
    </el-steps>
    <div style="margin-top:30px;">
        <step1 :entity="entity" v-show="activeStep===0" :courseid="courseid" :isTeacher="isTeacher" :exerciseCategory="exerciseCategory" @next="activeStep=1"></step1>
        <step2 :entity="entity" v-show="activeStep===1" @next="activeStep=2" @back="activeStep=0"></step2>
        <step3 :entity="entity" v-show="activeStep===2" @next="activeStep=3" @back="activeStep=1"></step3>
        <step4 :entity="entity" v-show="activeStep===3" @next="activeStep=4" @back="activeStep=2"></step4>
        <step5 :entity="entity" v-show="activeStep===4" :exerciseCategory="exerciseCategory" @back="activeStep=3" @save="onSave" ref="step5"></step5>
    </div>
</div>
</template>

<script>
/**
 * @file 实验编辑修改
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/12
 */
import step1 from './step1'
import step2 from './step2'
import step3 from './step3'
import step4 from './step4'
import step5 from './step5'
import { add, edit, get } from '@/api/coursemanage/exercise'
import { fetchItems } from '@/api/dictionary'
export default {
  components: {
    step1,
    step2,
    step3,
    step4,
    step5
  },
  data() {
    return {
      // 步骤条
      activeStep: 0,
      isTeacher: this.$store.getters.permissions.indexOf('course:teacherPage') > -1,
      courseid: this.$route.query.courseid,
      // 实验编号
      exerciseid: this.$route.query.exerciseid,
      // 实验分类
      exerciseCategory: [],
      // 实验数据实体
      entity: {
        codeDto: { jsLibrary: '', cssLibrary: '' },
        exerciseResourceDtoList: []
      }
    }
  },
  created() {
    this.$nextTick(e => {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      setTimeout(() => {
        loading.close()
      }, 2000)
      this.init()
    })
  },
  methods: {
    // 数据加载
    init() {
      var vm = this
      fetchItems(null, 'exercise_category').then(e => {
        this.exerciseCategory = e.data.data
      })
      // 判断实验编号是否为空，若不为空则修改
      if (this.exerciseid !== undefined) {
        get(this.exerciseid).then(e => {
          vm.entity = e.data.data
          vm.entity.haveCode = vm.entity.haveCode ? '1' : '0'
          vm.entity.type += ''
        })
      }
    },
    onSave() {
      if (this.exerciseid) {
        edit(this.entity).then(e => {
          if (e.data.success) {
            this.$sucmsg('修改成功')
            this.callback()
          } else {
            this.$errmsg('修改失败')
            this.$refs.step5.clearBtnLoading()
          }
        }).catch(() => {
          this.$errmsg('修改失败')
          this.$refs.step5.clearBtnLoading()
        })
      } else {
        add(this.entity).then(e => {
          if (e.data.success) {
            this.$sucmsg('添加成功')
            this.callback()
          } else {
            this.$errmsg('添加失败')
            this.$refs.step5.clearBtnLoading()
          }
        }).catch(() => {
          this.$errmsg('添加失败')
          this.$refs.step5.clearBtnLoading()
        })
      }
    },
    callback() {
      // this.$router.push({ name: 'exercise' })
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.callback{margin:0px; right: 25px;text-align: right;}
</style>
