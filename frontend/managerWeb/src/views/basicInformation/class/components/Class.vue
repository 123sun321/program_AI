<template>
    <el-form :model="entity" ref="form" label-width="100px" class="demo-dynamic">
        <el-form-item prop="professionId"  label="选择专业:" :rules=" [{ required: true, message: '请选择专业', trigger: 'change' }] " >
            <el-select v-model="entity.professionId" clearable filterable placeholder="请选择">
                <el-option  v-for="(item,index) in professionList"  :key="index" :label="item.name" :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="inSchool"  label="在校状态:" :rules=" [{ required: true, message: '请选择在校状态', trigger: 'change' }] " >
            <el-select v-model="entity.inSchool"  clearable filterable placeholder="请选择">
                <el-option  v-for="(item,index) in classStatus"  :key="index" :label="item.status" :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="schoolLength"  label="学制:" :rules=" [{ required: true, message: '请选择学制', trigger: 'change' }] " >
            <el-select v-model="entity.schoolLength"  clearable filterable placeholder="请选择">
            <el-option  v-for="(item,index) in limits"  :key="index" :label="item.limit" :value="item.id">
            </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="year"  label="年级:" :rules="[ { required: true, message: '请选择年级', trigger: 'change' } ]" >
            <el-select v-model="entity.year"  clearable filterable placeholder="请选择">
            <el-option  v-for="(item,index) in grades"  :key="index" :label="item.name" :value="item.code">
            </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="startDate"  label="入学时间:" :rules=" [{ required: true, message: '请选择入学时间', trigger: 'change' }]" >
            <el-date-picker  v-model="entity.startDate"  type="date"  placeholder="选择日期">
            </el-date-picker>
        </el-form-item>
        <el-form-item prop="graduateDate"  label="毕业时间:" :rules="[ { required: true, message: '请选择毕业时间', trigger: 'change' }] " >
            <el-date-picker  v-model="entity.graduateDate"  type="date"  placeholder="选择日期">
            </el-date-picker>
        </el-form-item>
         <el-form-item prop="counsellor"  label="导员:" :rules="[ { required: true, message: '请填写导员名称', trigger: 'blur' },
            { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: ['blur', 'change'] }
            ]"
        >
            <el-input v-model="entity.counsellor" placeholder="请输入导员名称"></el-input>
        </el-form-item>
        <el-form-item prop="name"  label="班级名称:" :rules="[{validator:remoteValidate('name','已存在同名班级')},{ required: true, message: '请填写专业名称', trigger: 'blur' },
            { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: ['blur', 'change'] }
            ]"
        >
            <el-input v-model="entity.name" placeholder="请输入班级名称"></el-input>
        </el-form-item>
        <el-form-item prop="classSize"  label="班级人数:" :rules="[ { required: true, message: '请输入班级人数', trigger: 'blur' } ]"
        >
            <el-input v-model="entity.classSize"  type= "Number" placeholder="请输入班级人数"></el-input>
        </el-form-item>
    </el-form> 
</template> 
<script>
 /**
 * @file 班级基本form组件
 * @author 赵雪丽
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/06
 */
import {
   form
} from '@/mixins/form'
import {
   getClass,
   getDepPro,
   remoteValid,
   fetchGrades,
   getAllProfession
} from '@/api/base/class'
export default {
   mixins: [form],
   props: ['eid', 'isEdit'],
   data() {
     return {
       grades: [], // 年级
       classStatus: [
         { status: '在校', id: 1 },
         { status: '离校', id: 0 }
       ], // 在校状态
       limits: [
         { limit: '二年制', id: '2' },
         { limit: '三年制', id: '3' },
         { limit: '四年制', id: '4' },
         { limit: '五年制', id: '5' }
       ], // 学制
       // 专业
       professionList: [], // 专业列表
       professions: {
         name: '',
         id: ''
       },
       // 表单绑定
       entity: {
         professionId: undefined,
         profession: {
           name: '',
           id: ''
         }
       }
     }
   },
   created() {
     this.getAllProfession()
   },
   // 监测院系是否改变，并实时更新专业
   watch: {
     newVal(val, oldVal) {
       if (val) {
         getDepPro().then(response => {
           const data = response.data.data
           for (let i = 0; i < data.length; i++) {
             if (data[i].id === val) {
               this.professions = data[i].professions
             }
           }
         })
       } else {
         this.professions = []
       }
     }
   },
   // 监测院系是否改变，返回院系的Id
   computed: {
     newVal() {
       return this.entity.department.id
     }
   },
   methods: {
     // 是否显示班级弹窗
     open() {
       this.visible = true
     },
     // 判断是否为修改班级状态
     load() {
       if (this.grades.length === 0) {
         this.getGrades()
       }
       if (this.isEdit && this.eid) {
         getClass(this.eid).then(e => {
           this.entity = e.data.data
           this.entity.professionId = e.data.data.professionId
           if (e.data.data.inSchool === true) {
             this.entity.inSchool = 1
           } else {
             this.entity.inSchool = 0
           }
         })
       }
     },
     // 校验班级是否重复
     remoteValidate(name, msg) {
       return (rule, value, callback) => {
         const yearName = this.entity.year
         const className = this.entity.name
         const id = this.entity.id
         const dto = {
           id: id,
           name: className,
           year: yearName
         }
         remoteValid(dto).then(e => {
           if (e.data) {
             callback()
           } else {
             callback(new Error(msg))
           }
         })
       }
     },
     // 获取所有专业
     getAllProfession() {
       getAllProfession().then(e => {
         this.professionList = e.data.data
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