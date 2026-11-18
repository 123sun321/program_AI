<template>
    <el-form :model="entity" ref="form" label-width="130px" class="demo-dynamic" :inline="true">
        <el-form-item prop="professionId"  label="选择专业:" :rules="[{ required: true, message: '请选择专业', trigger: 'change' }] " >
            <el-select v-model="entity.professionId" :disabled="isDisabled" clearable filterable placeholder="请选择">
                <el-option  v-for="(item,index) in professionList"  :key="index" :label="item.name" :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="gradeId"  label="选择班级:" :rules=" [{ required: true, message: '请选择班级', trigger: 'change' } ]" >
            <el-select v-model="entity.gradeId" :disabled="isDisabled" clearable filterable placeholder="请选择">
              <el-option  v-for="(item,index) in classInfo"  :key="index" :label="item.name" :value="item.id">
            </el-option>
            </el-select>
        </el-form-item>
        <!-- <el-form-item prop="year"  label="年级:" :rules=" { required: true, message: '请选择年级', trigger: 'change' } " >
            <el-select v-model="entity.year" clearable filterable placeholder="请选择">
              <el-option  v-for="(item,index) in grades"  :key="index" :label="item.name" :value="item.code">
              </el-option>
            </el-select>
        </el-form-item> -->
        <el-form-item prop="name"  label="学生姓名:" :rules="[ { required: true, message: '请填写学生姓名', trigger: 'blur' },
            { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: ['blur', 'change'] }]" >
            <el-input v-model="entity.name" placeholder="请填写学生姓名"></el-input>
        </el-form-item>
        <el-form-item prop="code"  label="学生学号:" :rules="[ {validator:remoteValidate('code','已存在该学号')},{ required: true, message: '请填写学生号', trigger: 'change' }]"   >
            <el-input v-model="entity.code" placeholder="请填写学生学号"></el-input>
        </el-form-item>
        <el-form-item prop="birthday"  label="出生日期:">
            <el-date-picker  v-model="entity.birthday"  type="date"  placeholder="选择日期">
            </el-date-picker>
        </el-form-item>
        <el-form-item prop="sex"  label="学生性别:"   :rules="[ { required: true, message: '请选择学生性别', trigger: 'change' }]">
            <el-radio v-model="entity.sex" label="男">男</el-radio>
            <el-radio v-model="entity.sex" label="女">女</el-radio>
        </el-form-item>
        <el-form-item prop="nation"  label="所属民族:">
            <el-input v-model="entity.nation" placeholder="请填写民族"></el-input>
        </el-form-item>
         <el-form-item prop="nationality"  label="所属籍贯:">
            <el-input v-model="entity.nationality" placeholder="请填写籍贯"></el-input>
        </el-form-item>
         <el-form-item prop="politics"  label="政治面貌:">
            <el-input v-model="entity.politics" placeholder="请填写政治面貌"></el-input>
        </el-form-item>
        <el-form-item prop="idnumber"  label="身份证号:" :rules="[ {required: true, message: '请填写证件号码', trigger: 'blur' },
                   {pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/, message: '证件号码格式有误！', trigger: 'blur'}
                   ]"   >
            <el-input v-model="entity.idnumber" placeholder="请填写学生身份证号"></el-input>
        </el-form-item>
        <!-- <el-form-item prop="schoolingLength"  label="学制:">
          <el-select v-model="entity.schoolingLength" placeholder="请填写学制">
            <el-option v-for="(item,index) in EducationalYears" :key="index" :label="item.name" :value="item.schoolingLength"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="education"  label="学历:">
          <el-select v-model="entity.education" placeholder="请填写学历">
            <el-option v-for="(item,index) in EducationLevel" :key="index" :label="item.education" :value="item.education" ></el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item prop="email"  label="电子邮件:" :rules="[{validator:validateEmail(entity.email,'邮箱格式不正确')},{ required: true, message: '请填写邮箱', trigger: 'change' }]" >
            <el-input v-model="entity.email" placeholder="请填写电子邮件"></el-input>
            <el-tag v-if="entity.email!=='' && entity.emailConfirmed">已绑定</el-tag>
            <el-tag v-if="entity.email!=='' &&entity.email!=null && !entity.emailConfirmed">未绑定</el-tag>
        </el-form-item>
        <el-form-item prop="contact"  label="手机号码:"  :rules="[{validator:checkPhone(entity.contact,'手机号格式不正确')},{ required: true, message: '请填写手机号', trigger: 'change' }]" >
            <el-input v-model="entity.contact" placeholder="请填写联系方式"></el-input>
            <el-tag v-if="entity.contact!=='' && entity.phoneNumberConfirmed">已绑定</el-tag>
            <el-tag v-if="entity.contact!=='' &&entity.contact!=null && !entity.phoneNumberConfirmed">未绑定</el-tag>
        </el-form-item>
        <el-form-item prop="address"  label="家庭地址:">
            <el-input v-model="entity.address" placeholder="请填写家庭地址"></el-input>
        </el-form-item>
        <el-form-item prop="linkman"  label="紧急联系人:" >
            <el-input v-model="entity.linkman" placeholder="请填写紧急联系人"></el-input>
        </el-form-item>
         <el-form-item prop="linkmanContact"  label="紧急联系电话:" >
            <el-input v-model="entity.linkmanContact" placeholder="请填写紧急联系电话"></el-input>
        </el-form-item>
        <el-form-item prop="linkmanRelation"  label="与紧急联系人关系:"  >
            <el-input v-model="entity.linkmanRelation" placeholder="请填写与紧急联系人关系"></el-input>
        </el-form-item>
    </el-form> 
</template>
<script>
 /**
 * @file 学生基本form组件
 * @author 赵雪丽
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/06
 */
import {
   form
} from '@/mixins/form'
import {
   getStudent,
   remoteValid,
   fetchGrades,
   getProfessionWithGrade
} from '@/api/base/student'
export default {
   mixins: [form],
   props: ['eid', 'isEdit', 'professionid', 'gcid', 'flag'],
   data() {
     return {
       grades: [], // 年级
       classStatus: [], // 在校状态
       limits: [], // 学制
       professionList: [], // 所有专业
       classInfo: [], // 所有班级
       isDisabled: false,
       // 专业
       professions: {
         name: '',
         id: ''
       },
       // 表单绑定
       entity: {
         profession: {
           name: '',
           id: ''
         }
       },
       EducationalYears: [
         { schoolingLength: '2', name: '2年制' },
         { schoolingLength: '3', name: '3年制' },
         { schoolingLength: '4', name: '4年制' },
         { schoolingLength: '5', name: '5年制' }
       ],
       EducationLevel: [
         { education: '大专' },
         { education: '本科' },
         { education: '硕士' },
         { education: '博士' },
         { education: '博士后' }
       ]
     }
   },
   // 初始化数据
   created() {
     if (this.flag) {
       this.$set(this.entity, 'professionId', this.professionid)
       this.$set(this.entity, 'gradeId', this.gcid)
       this.isDisabled = true
     }
     this.getProfessionWithGrade()
   },
   // 监测院系是否改变，并实时更新专业;监测专业是否改变，并实时更新班级
   watch: {
     newProfession(val, oldVal) {
       if (val) {
         getProfessionWithGrade().then(response => {
           this.professionList = response.data.data
           for (let i = 0; i < this.professionList.length; i++) {
             if (this.professionList[i].id === this.entity.professionId) {
               this.classInfo = this.professionList[i].gradeList
             }
           }
         })
       }
     }
   },
   // 监测院系是否改变，返回院系的Id;监测专业是否改变，返回专业的Id
   computed: {
     newProfession() {
       return this.entity.professionId
     }
   },
   methods: {
     // 是否显示学生弹窗
     open() {
       this.visible = true
     },
     // 判断是否为修改学生状态
     load() {
       if (this.grades.length === 0) {
         this.getGrades()
       }
       if (this.isEdit && this.eid) {
         getStudent(this.eid).then(e => {
           this.entity = e.data.data
         })
       }
     },
     checkPhone(phone, msg) {
       return (rule, value, callback) => {
         if (!(/^1[34578]\d{9}$/.test(phone))) {
           callback(new Error(msg))
         } else {
           callback()
         }
       }
     },
     validateEmail(email, msg) {
       const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
       return (rule, value, callback) => {
         if (re.test(email)) {
           callback()
         } else {
           callback(new Error(msg))
         }
       }
     },
     // 校验学生是否重复
     remoteValidate(name, msg) {
       return (rule, value, callback) => {
         // const code = this.entity.code
         // const idnumber = this.entity.idnumber
         const id = this.entity.id
         //  const dto = {
         //    id: id,
         //    code: code,
         //    idnumber: idnumber
         //  }
         remoteValid(id, name, value).then(e => {
           if (e.data.data) {
             callback()
           } else {
             callback(new Error(msg))
           }
         })
       }
     },
     // 根据专业获取班级
     getProfessionWithGrade() {
       getProfessionWithGrade().then(response => {
         this.professionList = response.data.data
         for (let i = 0; i < this.professionList.length; i++) {
           if (this.professionList[i].id === this.entity.professionId) {
             this.classInfo = this.professionList[i].gradeList
           }
         }
       })
     },
     // 获取年级
     getGrades() {
       fetchGrades().then(e => {
         this.grades = e.data.data
       })
     }
   }
}

</script>
