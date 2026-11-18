<template>
      <div class="report-card">
        <div class="title">相关报告</div>
        <div class="correlation-report">
            <div class="report-item" v-for="item in reports" :key="item.id">
              <div @click="onclick(item.id)"> “{{item.exerciseJoinDto.exercise.name}}”实验报告</div>
            </div>
        </div>
      </div>
</template>

<script>
export default {
  props: ['reports'],
  created() {
  },
  methods: {
    onclick(id) {
      this.$emit('headCallBack', id)
    }
  }
}
</script>

<style lang="scss" scoped>
.report-card{
    background-color: #ffffff;
    border-radius: 5px;
    padding: 10px 20px 20px 20px;
    .title{
        line-height: 50px;
        font-size: 22px;
        color: #666666;
        border-bottom:1px solid #eeeeee;
    }
    .correlation-report{
        padding: 10px 0;
        font-size: 14px;
        .report-item{
            padding: 12px;
            color: #4c5157;
        }
        .report-item:hover{
            cursor: pointer;
            color: #4c5157;
            background-color: #f9f9f9;
        }
    }
}
</style>
