<template>
    <my-dialog @onsoure="onsoure" ref="mydialog" mywidth="30%">
        <div class="joinbox">
            <div class="title">加入私有课程</div>
            <div class="summary">请输入教师提供的私有课程码加入教师的私有课程</div>
            <div class="text">
                <el-input size="big" v-model="invitationCode" placeholder="请输入邀请码"></el-input>
            </div>
        </div>
    </my-dialog>
</template>

<script>
import MyDialog from '@/components/MyDialog'
import { joincourse } from '@/api/mycourse/course'
export default {
  components: { MyDialog },
  data() {
    return {
      invitationCode: undefined
    }
  },
  methods: {
    open() {
      this.$refs.mydialog.open()
    },
    onsoure() {
      if (!this.invitationCode) {
        this.$warnmsg('请输入邀请码')
      } else {
        joincourse(this.invitationCode).then(e => {
          this.$sucmsg('加入成功')
          this.invitationCode = undefined
          this.$refs.mydialog.close()
          this.$emit('success')
        })
      }
    }
  }
}
</script>

<style lang="scss">
.joinbox{
    text-align: center;
    padding: 25px 50px;
    .title{
        margin-bottom: 40px;
        font-size: 24px;
        color: #333333;
    }
    .summary{
        margin-bottom: 30px;
        font-size: 18px;
        color: #999999;
    }
}
</style>
