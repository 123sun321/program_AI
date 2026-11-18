<template>
  <div>
    <el-dialog title="批量导入" :visible.sync="visiable" width="40%" @close="onColse">
      <table cellpadding="0" cellspacing="0" class="export-table">
        <tr>
          <td>上传文件</td>
          <td style="padding: 10px;">
            <el-upload class="upload-demo" ref="upload" :action="action" :limit="1" :auto-upload="false" :on-change="handleChange"
              :file-list="fileList">
              <el-button size="small">选择文件</el-button>
            </el-upload>
            <el-button size="small" @click="importData">导入</el-button>
          </td>
        </tr>
        <tr>
          <td style="color:red;">模板下载</td>
          <td style="padding: 10px;">
            <el-button size="mini" @click="downTemplate">下载导入模板</el-button>
          </td>
        </tr>
        <tr>
          <td>导入功能说明</td>
          <td>
            <p>1.请先下载专业导入模板</p>
            <p>2.根据模板内容样式填写账号相关信息</p>
            <p>3.选择要导入的账号文件，点击导入</p>
          </td>
        </tr>
      </table>
    </el-dialog>
    <imp-data-detail ref="detail"  @success="onSuccess"></imp-data-detail>
  </div>

</template>

<script>
/*
批量导入专业组件
@author 袁延延
@date 2025/11/28 14:30
@version 1.0
*/
import {
  getQueryParams
} from '@/utils/urlUtil'
import impDataDetail from './importDataDetail'
export default {
  components: {
    impDataDetail
  },
  data() {
    return {
      visiable: false, // 是否显示批量导入弹窗
      fileList: [], // 上传文件列表
      action: process.env.BASE_API + process.env.SHOP_API_PREFIX + '/profession/import'// 端口
    }
  },
  // 获取地址
  created() {
    const params = getQueryParams()
    this.action += '?app_id=' + params.appid
  },
  methods: {
    // 显示弹窗
    open() {
      this.visiable = true
    },
    // 下载模板
    downTemplate() {
      window.open(process.env.HOST + '/static/template/professionImportTemplate.xlsx')
    },
    // 导入数据
    importData() {
      this.$refs.upload.submit()
    },
    // 读取文件
    handleChange(file, fileList) {
      const response = file.response
      if (response === undefined) {
        return
      }
      if (response.data.length === 0) {
        this.$warnmsg('未读到任何行,请规范excel')
      } else {
        this.$refs.detail.open(response.data)
      }
    },
    // 判断是否上传成功
    onSuccess(e) {
      if (e) {
        this.isReload = e
      }
    },
    // 关闭上传窗口
    onColse() {
      this.$emit('success', this.isReload)
    }
  }
}
</script>

<style>

</style>
