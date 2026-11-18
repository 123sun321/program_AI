<template >
<el-row>
    <div class="maincontent student-page">
      <template>
        <span v-if="this.entity.score!=null" ><span v-if="checked ===false">{{entity.score+"分"}}
          <el-button type="primary" @click="toscore()">重新评分</el-button><span v-if="message!=null">相似度{{message}}</span> </span>
          <span class="td-opc" v-else > <span class="butt">
            <el-input v-model="entity.score" size="mini"></el-input>
            </span>
            <el-button type="primary" size="mini" @click="cofirm()">确定</el-button>
            <el-button size="mini"   @click="cancel()" plain>取消</el-button>
        </span> </span>
        <span v-else><span v-if="checked===false"><el-button type="primary" @click="toscore()">请评分</el-button><span v-if="message!=null" class="similarity">相似度{{message}}</span> </span>
        <span class="td-opc" v-else>
           <span class="butt" >
            <el-input v-model="entity.score" name="score"   size="mini"></el-input>
            </span>
            <el-button type="primary" size="mini" @click="cofirm()">确定</el-button>
            <el-button size="mini"   @click="cancel()" plain>取消</el-button>
        </span> </span>
        <span class="detail" v-if="this.entity.recommend===0"><el-button type="primary" @click="set()">设为推荐</el-button> <el-button @click="callback">返回</el-button></span>
        <span class="detail" v-else  ><el-button type="primary"  @click="canc()" disabled>已设为推荐</el-button> <el-button @click="callback">返回</el-button></span>
      </template>
      <div class="webEdtor">
        <iframe
          class="img2"
          :src="'/fstatic/codeeditor/index.html?appid='+appid+'#/showassignment?assignmentid='+id"
          allowfullscreen="allowfullscreen"
          frameborder="0"
          scrolling="no"
        ></iframe>
      </div>
  </div>

</el-row>
</template>

<script>
/**
 * @file 代码详情组件
 * @author 张人杰
 * @copyright zhengzhouchuanghzi
 * @since 2025/03/25
 */
import { updatescore, updaterecommend, getSubmitByID } from '@/api/assignmentmanage/assgin'
export default {
  // props: ['tid', 'sid'],
  data() {
    return {
      checked: false, // 评分校验
      flag: false,
      id: this.$route.query.id,
      trainingId: this.tid,
      entity: {},
      appid: this.$store.getters.appid,
      shopId: '',
      message: undefined
    }
  },
  created() {
    this.$nextTick(e => {
      this.getlist()
    })
  },
  destroyed: { // 离开页面生命周期函数
    // this.websocketclose()
  },
  methods: {
    // 返回
    callback() {
      this.$router.go(-1)
    },
    // 确定
    cofirm() {
      // const aa = 46
      updatescore(this.id, this.entity.score).then(response => {
        if (response.data.success) {
          this.$sucmsg('编辑成功!')
        } else {
          this.$errmsg('编辑失败!')
        }
      })
      this.checked = false
    },
    // 取消
    cancel() {
      this.checked = false
    },
    // 重新评分
    toscore() {
      this.checked = true
    },
    showRecord() {
      this.$refs.webide.showRecord()
    },
    // 设为推荐
    set() {
      // const tuijian = 1
      updaterecommend(this.id).then(response => {
        if (response.data.success) {
          this.$sucmsg('推荐成功!')
          // this.gridData.splice(index, 1)
          this.getlist()
        } else {
          this.$errmsg('推荐失败!')
        }
      })
    },
    getlist() {
      getSubmitByID(this.id).then(response => {
        this.loading = false
        const data = response.data.data
        this.entity = data
      })
    }

  }
}

</script>

<style lang="scss" scoped>
.el-dropdown-link {
    cursor: pointer;
    color: rgb(240, 241, 243);
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
  .td-opc{
        position: relative;
        margin-left: 5%;
  }
   .butt{
            position: absolute;
           margin-top:  2.4%;
           margin-left: -60%;
        }
  .td-opc  /deep/.el-input{
    display:block;
    // position: relative;
        }
.td-opc  /deep/.el-input__inner {
    //  padding:0px 8px;
    //   margin-left: -80%;
        width: 50px;
        // width: 45% !important;
        }
  .detail{
      float: right;
       /* margin-top: 40px; */
    //   margin-left: 20px;
     /* margin-bottom: 10px; */
    }
    .similarity{

       color: #FF0000;
    }
.webEdtor{
  width: 100%;
  height: 600px;
  border: 1px solid #cac6c6;
  margin-top: 30px;
  iframe{
    border: none;
    height: 100%;
    width: 100%;
  }
}
</style>
