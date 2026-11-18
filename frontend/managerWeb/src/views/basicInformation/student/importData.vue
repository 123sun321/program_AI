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
            <p>1.请先下载院系导入模板</p>
            <p>2.根据模板内容样式填写账号相关信息</p>
            <p>3.选择要导入的账号文件，点击导入</p>
          </td>
        </tr>
      </table>
    </el-dialog>
    <imp-data-detail ref="detail" @success="onSuccess"></imp-data-detail>
  </div>

</template>

<script>
/**
 * @file 导入数据弹窗
 * @author 赵雪丽
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/06
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
      visiable: false, // 是否显示弹窗
      fileList: [], // 上传的文件列表
      action: process.env.BASE_API + process.env.SHOP_API_PREFIX + '/student/upload', // 端口
      isReload: false // 是否重新加载
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
      window.open(process.env.HOST + '/static/template/studentImportTemplate.xlsx')
    },
    // 导入数据
    importData() {
      this.$refs.upload.submit()
    },
    // 文件状态改变
    handleChange(file, fileList) {
      if (file.response === undefined) {
        return
      }
      if (file.response.length === 0) {
        this.$warnmsg('未读到任何行,请规范excel')
      } else {
        this.$refs.detail.open(file.response.data)
      }
    },
    // 文件上传状态 e [Boolean] 判断是否上传成功
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
