<template>
<div class="maincontent">
        <el-form :model="entity" label-position="top" ref="form">
          <el-row>
              <el-col :offset="1" :span="22">
                <div class="preview_btn"><el-button @click="preview">分步预览</el-button></div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :offset="1" :span="22">
                <el-form-item style="min-height: 300px" prop="content" label="实验详情:" :rules="[{required:true,message:'实验详情不能为空'}]">
                   <mark-down v-model="entity.content"></mark-down>
                </el-form-item>
              </el-col>
            </el-row>
        </el-form>
        <preview :content="entity.content" ref="preview"></preview>
        <div style="text-align:center;padding-top:10px;">
          <el-button  @click="onBack">上一步</el-button>
          <el-button type="primary" @click="onNext">下一步</el-button>
        </div>
</div>
</template>

<script>
/**
 * @file 实验详情
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/12
 */
import preview from './preview'
import markDown from '@/components/markDown'
export default {
  props: ['entity'],
  components: { markDown, preview },
  data() {
    return {
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      if (this.entity.content === undefined || this.entity.content === '') {
        this.entity.content = '# 标题一 \n实验说明 \n# 标题二 \n实验说明 \n# 标题三 \n实验说明'
      }
    },
    preview() {
      this.$refs.preview.open()
    },
    onBack() {
      this.$emit('back')
    },
    onNext() {
      this.$refs.form.validate(e => {
        if (e) {
          this.$emit('next')
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.el-textarea textarea{height: 200px;}
.preview_btn{
  float: right;
  margin-bottom: -50px;
  position: relative;
  z-index: 1001;
}
</style>
