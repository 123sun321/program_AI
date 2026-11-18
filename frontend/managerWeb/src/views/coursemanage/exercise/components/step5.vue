<template>
<div class="container">
    <p class="bot-border">基本信息</p>
    <div class="show_content">
      <el-row>
        <el-col :span="12">
          <el-row>
            <el-col :span="6" class="title">实验类型：</el-col>
            <el-col :span="18" class="content">{{entity.type===0 ? '课程实验' : '练习实验'}}</el-col>
          </el-row>
        </el-col>
        <el-col :span="12">
          <el-row>
            <el-col :span="6" class="title">实验名称：</el-col>
            <el-col :span="18" class="content">{{entity.name}}</el-col>
          </el-row>
        </el-col>
        <el-col :span="12">
            <el-row>
            <el-col :span="6" class="title">技术类型：</el-col>
            <el-col :span="18" class="content">
                <el-select v-model="entity.category" disabled>
                    <el-option v-for="(category,index) in exerciseCategory" :key="index" :value="category.code" :label="category.name"></el-option>
                </el-select>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="12">
            <el-row>
            <el-col :span="6" class="title">实验标签：</el-col>
            <el-col :span="18">
              <el-select disabled v-model="entity.tags" multiple></el-select>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="12">
            <el-row>
            <el-col :span="6" class="title">难易程度：</el-col>
            <el-col :span="18" class="content">
              <el-rate v-model="entity.complexity" disabled style="margin-top:3px;"></el-rate>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="12">
            <el-row>
            <el-col :span="6" class="title">知识点：</el-col>
            <el-col :span="18" class="content">{{entity.knowledge}}</el-col>
          </el-row>
        </el-col>
        <el-col :span="12">
            <el-row>
            <el-col :span="6" class="title">封面：</el-col>
            <el-col :span="18" class="content">
              <img :src="entity.logo" class="exercise_logo" alt="">
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="12">
            <el-row>
            <el-col :span="6" class="title">排序：</el-col>
            <el-col :span="18" class="content">{{entity.sort}}</el-col>
          </el-row>
        </el-col>
      </el-row>
    </div>
    <p class="bot-border">初始数据</p>
    <div class="show_content">
      <el-row>
        <el-col :span="24">
          <el-row>
            <el-col :span="3" class="title">初始数据：</el-col>
            <el-col :span="21">
              <el-select v-model="entity.haveCode" disabled>
                <el-option value="0" label="无初始数据"></el-option>
                <el-option value="1" label="有初始数据"></el-option>
              </el-select>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
      <el-row v-if="entity.haveCode==='1'">
        <el-col :span="6">
          <el-row>
            <el-col :span="12" class="title">Html配置：</el-col>
            <el-col :span="12" class="content">{{entity.codeDto.htmlConfig}}</el-col>
          </el-row>
        </el-col>
        <el-col :span="18">
          <el-row>
            <el-col :span="6" class="title">html代码：</el-col>
            <el-col :span="18" class="content">{{entity.codeDto.html}}</el-col>
          </el-row>
        </el-col>
      </el-row>
      <el-row v-if="entity.haveCode==='1'">
          <el-col :span="6">
          <el-row>
            <el-col :span="12" class="title">CSS配置：</el-col>
            <el-col :span="12" class="content">{{entity.codeDto.cssConfig}}</el-col>
          </el-row>
        </el-col>
        <el-col :span="18">
          <el-row>
            <el-col :span="6" class="title">CSS源码：</el-col>
            <el-col :span="18" class="content">{{entity.codeDto.css}}</el-col>
          </el-row>
        </el-col>
      </el-row>
      <el-row v-if="entity.haveCode==='1'">
          <el-col :span="6">
          <el-row>
            <el-col :span="12" class="title">JavaScript配置：</el-col>
            <el-col :span="12" class="content">{{entity.codeDto.jsConfig}}</el-col>
          </el-row>
        </el-col>
        <el-col :span="18">
          <el-row>
            <el-col :span="6" class="title">JavaScript源码：</el-col>
            <el-col :span="18" class="content">{{entity.codeDto.js}}</el-col>
          </el-row>
        </el-col>
      </el-row>
    </div>
    <p class="bot-border">实验详情</p>
    <div class="show_content" style="width:600px;margin:0 auto;">
      <!-- <vue-markdown v-highlight :source="entity.content"></vue-markdown> -->
        <preview :content="entity.content"></preview>
    </div>
    <p class="bot-border">实验资源</p>
    <div class="show_content">
      <el-table :data="entity.exerciseResourceDtoList">
        <el-table-column type="index" label="序号" align="center"></el-table-column>
        <el-table-column label="资源标题" prop="name"></el-table-column>
        <el-table-column label="地址" prop="url"></el-table-column>
        <el-table-column label="排序" prop="sort"></el-table-column>
        <el-table-column prop="zip" width="60" label="操作" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" @click="delSource(scope.row,scope.$index)" icon="el-icon-delete" title="删除" plain></el-button>
            </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="text-align:center;margin-top:30px;">
        <el-button @click="onBack">上一步</el-button>
        <el-button type="primary" @click="onSave" :loading="btnLoading">保存</el-button>
        <el-button type="warning" @click="onCancel">取消</el-button>
    </div>
</div>
</template>

<script>
/**
 * @file 实验新增/编辑预览
 * @author Juci
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/12
 */
import VueMarkdown from 'vue-markdown' // 直接组件引入
import preview from './preview/content'
export default {
  components: {
    VueMarkdown, preview // 声明组件
  },
  props: ['entity', 'exerciseCategory'],
  data() {
    return {
      btnLoading: false
    }
  },
  methods: {
    // 删除资源
    delSource(item, index) {
      this.entity.exerciseResourceDtoList.splice(index, 1)
    },
    onBack() {
      this.$emit('back')
    },
    onCancel() {
      this.$cfm('确定要取消这次创建吗?取消后所填数据将丢失！', e => {
        // this.$router.push({ name: 'exercise' })
        this.$router.go(-1)
      })
    },
    onSave() {
      this.btnLoading = true
      this.$emit('save')
    },
    clearBtnLoading() {
      this.btnLoading = false
    },
    remoteValidate(col, msg) {
      return (rule, value, callback) => {
        callback()
      }
    }
  }

}
</script>

<style lang="scss" scoped>
.container {
    width: 90%;margin:0 auto;
    .bot-border {
        border-bottom: 0.5px dashed #e4e4e4;
        padding-bottom: 10px;
    }
    .detail {
        width: 100%;
        tr {
            td {
                height: 40px;
                font-size: 14px;
            }

            .left {
                font-weight: bold;
                color: #606266;
            }

        }
    }
  .show_content{ font-size: 14px;
  .title{color: #666;line-height: 40px;text-align: right;padding-right: 10px;}
  .content{
    line-height: 25px;padding-top:8px;
    .exercise_logo{
      max-height: 120px;
    }
  }
  }
}
</style>
