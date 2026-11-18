<template>
      <el-form :model="entity" ref="form" label-width="100px" >
          <el-form-item prop="title"  label="帖子标题:" :rules="[ { required: true, message: '请填写帖子标题'},
              { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: ['blur', 'change'] }]">
            <el-input v-model.trim="entity.title" placeholder="请填写帖子标题"></el-input>
          </el-form-item>
            <!-- 引用markdown -->
          <el-form-item prop="content" style="min-height: 300px" label="内容:" :rules="[{required:true,message:'内容不能为空'}]">
              <vue-wangeditor ref="wangeditor" v-model="entity.content"></vue-wangeditor>
          </el-form-item>
          <el-form-item prop="plate" label="发布至:"  :rules="[ { required: true, message: '请选择发布到哪', trigger: 'blur' }]" >
              <el-radio-group  v-model="entity.plate">
                <el-radio v-for="(item,index) in CommunityPlateList" :key="index" :label="index">{{item}}</el-radio>
              </el-radio-group>
            </el-form-item>
        </el-form>
</template>
<script>
/*
帖子弹窗组件
@author 张浩杰
@date 2025/03/29 16:30
@version 1.0
*/
import { form } from '@/mixins/form'
import VueWangeditor from '@/components/Wangeditor'
import { getPlateByRole, getThemeById, remoteValid } from '@/api/community/theme'
export default {
  components: {
    VueWangeditor
  },
  data() {
    return {
      CommunityPlateList: []// 返回的板块集合
    }
  },
  mixins: [form],
  props: ['eid', 'isEdit'],
  created() {
    this.getPlateByRole()
  },
  methods: {
    // 是否显示主题弹窗
    open() {
      this.visible = true
    },
    // 判断是否为修改帖子信息
    load() {
      this.$nextTick(e => {
        if (this.isEdit && this.eid) {
          this.wangeditorReset()
          getThemeById(this.eid).then(e => {
            this.entity = e.data.data
          })
        } else {
          this.entity.content = ''
        }
      })
    },
    wangeditorReset() {
      this.$refs.wangeditor.reset()
    },
    // 校验帖子是否重复
    remoteValidate(name, msg) {
      return (rule, value, callback) => {
        const id = this.entity.id
        remoteValid(id, name, value).then(e => {
          if (e.data.data) {
            callback()
          } else {
            callback(new Error(msg))
          }
        })
      }
    },
    // 根据角色获取权限社区板块
    getPlateByRole() {
      getPlateByRole().then(response => {
        this.CommunityPlateList = response.data.data
      })
    }
  }
}

</script>

<style>

</style>
