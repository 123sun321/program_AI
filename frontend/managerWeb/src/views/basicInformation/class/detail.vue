<template>
    <div class="detailGradeCourseBox dk-box" v-loading="loading">
      <el-button @click="$router.go(-1)" class="dk-fr" plain>返回</el-button>
        <data-grid :data="gradecourseInfo" title="班级信息"></data-grid>
        <data-table :gcid="classId" :professionid="professionid"></data-table>
    </div>
</template>

<script>
/**
 * @file 班级详情组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/4/15
 **/
import dataGrid from './components/dataGrid'
import dataTable from './components/dataTable'
import { getClass, getAllProfession } from '@/api/base/class'
export default {
  components: { dataGrid, dataTable },
  created() {
    this.professionid = this.$route.params.professionId
    this.classId = this.$route.params.classId
    this.init()
  },
  data() {
    return {
      title: '',
      loading: true,
      classId: 0,
      professionid: 0,
      whetherClass: false,
      limits: [
        { limit: '二年制', id: '2' },
        { limit: '三年制', id: '3' },
        { limit: '四年制', id: '4' },
        { limit: '五年制', id: '5' }
      ], // 学制
      gradecourseInfo: [
        { 'childArr': [{ 'label': '专业', 'val': '' }, { 'label': '年级', 'val': '' }, { 'label': '班级名称', 'val': '' }] },
        { 'childArr': [{ 'label': '学制', 'val': '' }, { 'label': '在校状态', 'val': '' }, { 'label': '班级人数', 'val': '' }] },
        { 'childArr': [{ 'label': '导员', 'val': '' }, { 'label': '入校时间', 'val': '' }, { 'label': '毕业时间', 'val': '' }] }
      ],
      arrclass: []
    }
  },
  methods: {
    init() {
      const vm = this
      getClass(vm.classId).then(response => {
        var data = response.data.data
        // 班课信息
        getAllProfession().then(e => {
          const professionList = e.data.data
          for (let i = 0; i < professionList.length; i++) {
            if (professionList[i].id === data.professionId) {
              vm.gradecourseInfo[0].childArr[0].val = professionList[i].name
            }
          }
        })
        // vm.gradecourseInfo[0].childArr[0].val = data.professions
        vm.gradecourseInfo[0].childArr[1].val = data.year
        vm.gradecourseInfo[0].childArr[2].val = data.name
        vm.gradecourseInfo[1].childArr[0].val = data.schoolLength + '年制'
        vm.gradecourseInfo[1].childArr[1].val = data.inSchool ? '在校' : '离校'
        vm.gradecourseInfo[1].childArr[2].val = data.classSize
        vm.gradecourseInfo[2].childArr[0].val = data.counsellor
        vm.gradecourseInfo[2].childArr[1].val = data.startDate.substring(0, 11)
        vm.gradecourseInfo[2].childArr[2].val = data.graduateDate.substring(0, 11)
        this.loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>

</style>

