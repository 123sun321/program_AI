<template>
    <div class="run-main">
      <!-- 顶部 -->
       <div class="operation-button">
        <el-button @click="callback">返回</el-button>
      </div>
      <el-row class="run-content">
       <el-col :span="24">
        <web-ide v-if="entity.codeId" :codeId="entity.codeId"></web-ide>
       </el-col>
     </el-row>
    </div>
</template>
<script>
/**
 * @file 作业编辑组件
 * @author 袁延延
 * @copyright zhengzhouchuanghzi
 * @since 2025/04/29
 */
import {
  getCodeWithId
} from '@/api/coursemanage/recommendWork'
import page from '@/components/page'
import WebIde from '@/components/WebIde'
export default {
  components: {
    page,
    WebIde
  },
  data() {
    return {
      baseHost: process.env.BASE_API,
      recommendId: this.$route.query.id,
      // 实验数据实体
      entity: {
        codeDto: {},
        exerciseResourceDtoList: []
      },
      form: {
        page: {
          current: 1,
          pageCount: 9,
          total: 0
        },
        courseId: '',
        trainingId: undefined,
        search: '',
        orderBy: 'creation_time desc'
      },
      content: '',
      trainId: 0,
      flag: 0
    }
  },
  computed: {
    newTraiing() {
      return this.form.trainingId
    }
  },
  created() {
    this.bindData()
    this.$nextTick(e => {
      this.getlist()
    })
  },
  methods: {
    // 绑定表格数据
    bindData() {
    },
    getlist() {
      getCodeWithId(this.recommendId).then(e => {
        this.entity = e.data.data
        console.log(this.entity)
      })
    },
    callback() {
      this.$router.push({ name: 'recommendManage' })
    },
    onrun() {
      this.$sucmsg('运行！')
    },
    onsave() {
      this.$sucmsg('保存！')
    },
    onload() {
      this.$sucmsg('格式刷！')
    },
    join() {
      this.entity.codeDto = ''
      this.getCodeByTrainingId(this.form.trainingId)
    }
  }
}
</script>
<style scoped lang="scss">
  .operation-button{
    padding: 15px;
    text-align: right;
  }
  .run-content{
    padding: 0 15px 60px 15px;
  }
</style>

