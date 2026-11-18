/*
 * @Author: 黄鹏举
 * @Date: 2018-12-04 17:52:38 
 * @Last Modified time: 2018-12-04 19:26:11
 */
<template>
  <el-form :model="entity" ref="form" :inline="line" >
    <slot name="tip"></slot>
    <!-- 添加 -->
     <el-form-item v-if="!isEdit" >
        <el-input v-model="search" clearable  placeholder="请输入学生姓名或者学号或者身份证号搜索"  prefix-icon="el-icon-search" style="width:84%" >
        </el-input>
        <el-button icon="el-icon-search" type="primary" style="margin-left:10px;height:32px;" @click="searchStu">搜索</el-button>
    </el-form-item>
    <el-form-item v-if="!isEdit" class="student-info"  prop="ids" label="学生:" :rules="[{validator:remoteValidate('请先查找学生')}]"  v-loading="loading">
         <div v-if="studentStatus === true" style="text-align:center;color:red;">
            <p>
                请先按照搜索条件查询学生
            </p>
            <!-- :rules="[ { required: true, message: '请注意是否选择学生',   trigger: ['blur', 'change'] }]" -->
        </div>
        <div v-if="studentStatus === 2" class="student-block" v-for="(item,index) in studentList" :key="index">
            <p class="student-img">
                <img :src="item.imgUrl" alt="">
            </p>
            <el-checkbox-group v-model="checkList" @change="handleCheckedCitiesChange"  >
              <el-checkbox :label="item.id">{{item.name}}</el-checkbox>
            </el-checkbox-group>
        </div>
        <el-checkbox v-if="studentStatus === 2" :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
        <div v-if="studentStatus === false" style="text-align:center;">
            <p>
                未找到学生信息
            </p>
        </div>
    </el-form-item>
    <!-- 添加 -->
    <!-- 修改 -->
    <el-form-item v-if="isEdit" class="student-info">
        <el-input v-model="search" disabled   placeholder="请输入学生姓名或者学号或者身份证号搜索"  prefix-icon="el-icon-search" style="width:84%">
        </el-input>
        <el-button disabled icon="el-icon-search" type="primary" style="margin-left:10px;height:32px;" @click="searchStu">搜索</el-button>
    </el-form-item>
    <el-form-item v-if="isEdit" >
        <div class="student-block" >
            <p class="student-img">
                <img :src="stuImg" alt="">
            </p>
             <el-checkbox v-model="checked" disabled>{{studentName}}</el-checkbox>
        </div>
    </el-form-item>
      <!-- 修改 -->
    <el-form-item prop="rewardPunishCategoryId" label="奖惩种类:" :rules="[ { required: true, message: '请选择奖惩种类', trigger: 'change' }]">
        <el-select v-model="select"   clearable filterable placeholder="请选择" style="width:100%"  @change="getRewardPunish">
            <el-option-group v-for="(group,key) in rewardPunish" :key="key" :label="group.label">
                <el-option  v-for="(item) in group.options" :key="item.id" :label="item.name"  :value="item.id">
                </el-option>
            </el-option-group>
        </el-select>
    </el-form-item>
    <el-form-item prop="schoolYear" label="学年:">
        <!-- <el-date-picker style="width:100%;"  v-model="entity.schoolYear"  type="daterange" unlink-panels value-format="yyyy-MM-dd"  range-separator="至" start-placeholder="开始日期"   end-placeholder="结束日期">
        </el-date-picker> -->
        <el-date-picker style="width:100%;" v-model="valetime" type="year" value-format="yyyy" @change="getTime" placeholder="选择年"></el-date-picker>
    </el-form-item>
    <el-form-item prop="abstracts" label="备注:"  :rules="[{min:1,max:20,message:'长度1到200个字符', trigger: ['blur', 'change']}]">
      <el-input type="textarea" v-model="entity.abstracts" placeholder="请填写备注"></el-input>
    </el-form-item>
    <el-form-item prop="masterUrl" label="附图:  ">
        <upload ref="upload" @imgurl="imgurl" v-model="entity.logo" :img="img" ></upload>
    </el-form-item>
  </el-form>
</template>
<script>
/**
 * @description 公共表单
 * @author【黄鹏举】
 * @since 2018/12/4
 * @version 1.0
 */
import upload from '../upload'

import {
  form
} from '@/mixins/form'
import {
  getStudent,
  rewardPunishList,
  fetchOne
  // remoteValid
} from '@/api/base/studentRewardPunish'
export default {
  // form表达
  mixins: [form],
  // 组件
  components: {
    upload
  },
  // 父组件的参数
  props: ['eid', 'isEdit'],

  data() {
    return {
      valetime: undefined, // 学年
      select: undefined, // 下拉选项
      search: undefined, // 搜索
      loading: false,
      searchStatus: false, // 是否启用搜索
      studentList: [], // 学生列表
      studentStatus: undefined, // 学生是否找到:2标识
      checkAll: false, // 是否全选
      checkList: [], // 选中列表
      isIndeterminate: true, //  checkbox 的不确定状态
      stuImg: undefined, // 学生头像
      studentName: undefined, // 姓名
      // studentId: undefined, // 学生id
      checked: true,
      // 类型
      rewardPunish: [
        {
          label: '奖励', options: []
        }, {
          label: '处罚', options: []
        }
      ],
      // 级别
      level: [
        { 'number': 1, name: '国家级' },
        { 'number': 2, name: '省级' },
        { 'number': 3, name: '地市级' },
        { 'number': 4, name: '校级' },
        { 'number': 5, name: '院系级' },
        { 'number': 6, name: '其他' }
      ],
      // logo路径
      img: undefined
    }
  },
  created() {
    // 获取所有奖惩列表
    rewardPunishList().then(e => {
      for (var j = 0; j < e.data.data.length; j++) {
        if (e.data.data[j].type === '奖励') {
          this.rewardPunish[0].options.push({ id: e.data.data[j].id, name: e.data.data[j].name })
        } else {
          this.rewardPunish[1].options.push({ id: e.data.data[j].id, name: e.data.data[j].name })
        }
      }
      console.log(this.rewardPunish)
    })
  },
  methods: {
    // 接收上传图片  url返回的图片路径
    imgurl(url) {
      console.log(url)
      this.entity.masterUrl = url
      this.img = url
    },
    // 打开组件调用的方法
    load() {
      var vm = this
      if (this.isEdit && this.eid) {
        fetchOne(this.eid).then(e => {
          console.log(e)
          vm.entity.abstracts = e.data.data.abstracts
          vm.entity.id = e.data.data.id
          vm.entity.masterUrl = e.data.data.masterUrl
          vm.entity.rewardPunishCategoryId = e.data.data.rewardPunishCategoryId
          vm.select = vm.entity.rewardPunishCategoryId
          vm.entity.schoolYear = e.data.data.schoolYear
          vm.valetime = vm.entity.schoolYear

          vm.img = e.data.data.masterUrl
          vm.search = e.data.data.student.code
          vm.stuImg = e.data.data.student.imgUrl
          vm.studentName = e.data.data.student.name
        })
      } else {
        this.img = undefined // 初始化
        this.valetime = undefined // 初始化学年
        this.select = undefined // 初始化下拉选项
        this.studentList = [] //
        this.checkList = [] //
        this.searchStatus = false
        this.search = ''
        this.isIndeterminate = true // 点击了全选
        this.studentStatus = undefined
      }
    },
    // 搜索学生
    searchStu() {
      if (this.search === '') {
        this.$warnmsg('请填写搜索条件')
        return
      } else {
        this.loading = true
        this.searchStatus = true
        const params = { name: this.search, code: this.search, idnumber: this.search }
        getStudent(params).then(e => {
          console.log(e)
          if (e.data.data.length !== 0) {
            const list = e.data.data.map(e => {
              return {
                id: e.id,
                imgUrl: e.imgUrl,
                name: e.name
              }
            })
            this.studentList = list
            this.studentStatus = 2
          } else {
            this.studentStatus = false
          }
          this.loading = false
        })
      }
    },
    // 全选 val是否全选 true或者false
    handleCheckAllChange(val) {
      const arr = this.studentList.map(({ id }) => id) // 选中所有id
      this.checkList = val ? arr : [] // 点击了全选把所有id赋值给变量数组
      this.isIndeterminate = false // 点击了全选
      this.entity.ids = this.checkList
    },
    // 获取复选框的值 value当前选中值
    handleCheckedCitiesChange(value) {
      this.entity.ids = this.checkList
      const checkedCount = value.length // 已选数组的长度
      const arr = this.studentList.map(({ id }) => id) // 选中所有id
      this.checkAll = checkedCount === arr.length // 和全部学生数组长度 对比
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.studentList.length // 不是全选
    },
    // 获取下拉奖惩种类
    getRewardPunish(val) {
      // console.log(this.checkList.length)
      this.entity.rewardPunishCategoryId = val
      if (this.checkList.length === 0) {
        this.studentStatus = true
      }
    },
    // 获取年
    getTime() {
      this.entity.schoolYear = this.valetime
    },
    // 自定义验证 msg 提示信息
    remoteValidate(msg) {
      return (rule, value, callback) => {
        if (this.checkList.length === 0) {
          callback(new Error(msg))
        } else {
          callback()
        }
      }
    }
  }
}

</script>

<style>
p{
    margin: 0px;
    padding: 0px;
}
.student-block{
    text-align: center;
    display: inline-block;
    margin: 2px 12px;
}
.student-img{
    height: 60px;
    width: 60px;
    border-radius: 50%;
    overflow: hidden;
    vertical-align: top;
    position: relative;
    border: 1px solid #f5f7fa;
}
.student-img>img{
    width: 100%;
}
.student-info .el-form-item__content{
    clear: both;
}
</style>
