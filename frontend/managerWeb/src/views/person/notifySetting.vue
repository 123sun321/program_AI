<template>
    <div class="maincontent notifySetting">
        <!-- <el-row justify="end" class="actionBtns" >
            <el-button>恢复默认</el-button>
        </el-row> -->
        <el-table
        :data="setting"
        style="width: 100%" :default-expand-all="true">
        <el-table-column
            prop="groupName"
            label="消息类型"
            width="280">
        </el-table-column>
        <el-table-column
            prop="siteMsg"
            label="站内信"
            >
            <template slot-scope="scope">
                <span v-if="scope.row.settingItems.sitemail!=null">
                         <el-checkbox @change="selectChange(scope,'sitemail')" v-model="scope.row.settingItems.sitemail.userSelect"></el-checkbox>    
                </span>                    
            </template>
        </el-table-column>
        <el-table-column
            prop="email"
            label="邮箱">
            <template slot-scope="scope">
              <span v-if="scope.row.settingItems.email!=null">
                         <el-checkbox @change="selectChange(scope,'email')" v-model="scope.row.settingItems.email.userSelect"></el-checkbox>      
              </span>                  
                    </template>
        </el-table-column>
        <el-table-column
            prop="sms"
            label="短信">
            <template slot-scope="scope">
              <span v-if="scope.row.settingItems.sms!=null">
                         <el-checkbox @change="selectChange(scope,'sms')" v-model="scope.row.settingItems.sms.userSelect"></el-checkbox>     
              </span>                   
                    </template>
        </el-table-column>
        <el-table-column
            prop="weiXin"
            label="微信">
            <template slot-scope="scope">
              <span v-if="scope.row.settingItems.wx!=null">
                    <el-checkbox @change="selectChange(scope,'wx')" v-model="scope.row.settingItems.wx.userSelect"></el-checkbox>   
              </span>                     
            </template>
        </el-table-column>
        <el-table-column type="expand">
        <template slot-scope="props">
            <div class="subTable">
                <el-table
                :data="props.row.children"
                style="width: 100%" :show-header="false">
                <el-table-column
                    prop="displayName"
                    label="消息类型"
                    width="280">
                </el-table-column>
                <el-table-column
                    prop="siteMsg"
                    label="站内信"
                    >
                     <template slot-scope="scopeChildren">
                       <span v-if="scopeChildren.row.settingItems.sitemail!=null">
                         <span v-if="!userSelectIsNull(scopeChildren.row.settingItems.sitemail)">
                          <el-checkbox @change="childSelectChange(scopeChildren,'sitemail')" v-model="scopeChildren.row.settingItems.sitemail.userSelect" :disabled="!scopeChildren.row.settingItems.sitemail.userCanSetting"></el-checkbox>     
                         </span>
                        <span v-if="userSelectIsNull(scopeChildren.row.settingItems.sitemail)">
                          <el-checkbox @change="childSelectChange(scopeChildren,'sitemail')" v-model="scopeChildren.row.settingItems.sitemail.defaultSelect" :disabled="!scopeChildren.row.settingItems.sitemail.userCanSetting"></el-checkbox>     
                         </span>
                       </span>                   
                    </template>
                </el-table-column>
                <el-table-column
                    prop="email"
                    label="邮箱">
                    <template slot-scope="scopeChildren">
                       <span v-if="scopeChildren.row.settingItems.email!=null">
                        <span v-if="!userSelectIsNull(scopeChildren.row.settingItems.email)">
                          <el-checkbox @change="childSelectChange(scopeChildren,'email')" v-model="scopeChildren.row.settingItems.email.userSelect" :disabled="!scopeChildren.row.settingItems.email.userCanSetting"></el-checkbox>     
                         </span>
                        <span v-if="userSelectIsNull(scopeChildren.row.settingItems.email)">
                          <el-checkbox @change="childSelectChange(scopeChildren,'email')" v-model="scopeChildren.row.settingItems.email.defaultSelect" :disabled="!scopeChildren.row.settingItems.email.userCanSetting"></el-checkbox>     
                         </span>
                       </span>                  
                    </template>
                </el-table-column>
                <el-table-column
                    prop="sms"
                    label="短信">
                    <template slot-scope="scopeChildren">
                      <span v-if="scopeChildren.row.settingItems.sms!=null">
                         <span v-if="!userSelectIsNull(scopeChildren.row.settingItems.sms)">
                          <el-checkbox @change="childSelectChange(scopeChildren,'sms')" v-model="scopeChildren.row.settingItems.sms.userSelect" :disabled="!scopeChildren.row.settingItems.sms.userCanSetting"></el-checkbox>     
                         </span>
                        <span v-if="userSelectIsNull(scopeChildren.row.settingItems.sms)">
                          <el-checkbox @change="childSelectChange(scopeChildren,'sms')" v-model="scopeChildren.row.settingItems.sms.defaultSelect" :disabled="!scopeChildren.row.settingItems.sms.userCanSetting"></el-checkbox>     
                         </span>
                       </span> 
                                           
                    </template>
                </el-table-column>
                <el-table-column
                    prop="weiXin"
                    label="微信">
                    <template slot-scope="scopeChildren">
                      <span v-if="scopeChildren.row.settingItems.wx!=null">
                        <span v-if="!userSelectIsNull(scopeChildren.row.settingItems.wx)">
                          <el-checkbox @change="childSelectChange(scopeChildren,'wx')" v-model="scopeChildren.row.settingItems.wx.userSelect" :disabled="!scopeChildren.row.settingItems.wx.userCanSetting"></el-checkbox>     
                         </span>
                       <span v-if="userSelectIsNull(scopeChildren.row.settingItems.wx)">
                          <el-checkbox @change="childSelectChange(scopeChildren,'wx')" v-model="scopeChildren.row.settingItems.wx.defaultSelect" :disabled="!scopeChildren.row.settingItems.wx.userCanSetting"></el-checkbox>     
                         </span>
                       </span> 
                   
                    </template>
                </el-table-column>
                <el-table-column
                    prop="id"
                    label="" width="48px;">
                    <template slot-scope="scopeChildren">
                                 <span></span>          
                    </template>
                </el-table-column>
                </el-table>
            </div>
        </template>
        </el-table-column>
        </el-table>
    </div>
</template>
<script>
/**
 * @file 个人消息接收设置管理
 * @author 张向宇
 * @copyright zhengzhouchuanghzi
 * @since 2018/12/25
 */
import { getUserSetting, setUserSetting } from '@/api/person/notificationSetting'
export default {
  components: {
  },
  data() {
    return {
      setting: []
    }
  },
  created() {
    getUserSetting().then(e => {
      // console.log(e.data)
      if (e.data.success) {
        this.setting = e.data.data
      } else {
        this.$message.error('获取数据错误，请稍后重试')
      }
    })
  },
  computed: {
    isSelect() {
      return function(value) {
        if (value === null) {
          return false
        } else {
          return value.userSelect
        }
      }
    }
  },
  methods: {
    // 父类选择发生变化处理
    selectChange(scope, type) {
      console.log('parent change')
      // console.log(scope.row[type])
      // debugger
      scope.row.children.forEach(element => {
        // 判断子平台是否允许自定义设置
        if (element.settingItems[type] != null) {
          if (element.settingItems[type].userCanSetting) {
            // element.settingItems[type].userSelect = scope.row.settingItems[type].userSelect
            this.$set(element.settingItems[type], 'userSelect', scope.row.settingItems[type].userSelect)
            this.setStatus(element, type, scope.row.settingItems[type].userSelect)
          }
        }
      })
    },
    // 子类选择发生变化处理
    childSelectChange(scope, type) {
      console.log('child change')
      for (let i = 0; i < this.setting.length; i++) {
        if (this.setting[i].groupName === scope.row.groupName) {
          if (this.getSelectStatus(scope.row.settingItems[type])) {
            // 如果当前子项为新选中，需要判断是否所有子项都为选中状态，如果是，把父项设置为选中状态。
            let selectCount = 0
            this.setting[i].children.forEach(item => {
              if (this.getSelectStatus(item.settingItems[type])) {
                selectCount++
              }
            })
            if (selectCount === this.setting[i].children.length) {
              this.setting[i].settingItems[type].userSelect = true
            }
            this.setStatus(scope.row, type, true)
          } else {
            // 当前子项为取消选中，则把父项也取消选中
            this.setting[i].settingItems[type].userSelect = false
            this.setStatus(scope.row, type, false)
          }
        }
      }
    },
    // 更新服务器端设置状态
    setStatus(module, type, status) {
      if (module.settingItems[type].userCanSetting) {
        const loading = this.$loading({
          lock: true,
          text: '正在提交设置',
          spinner: 'el-icon-loading'
        })
        setUserSetting({ moduleName: module.moduleName, platformName: type, userSelect: status }).then(e => {
          loading.close()
          this.$message.success('设置成功！')
        })
      }
    },
    getSelectStatus(data) {
      if (data.userSelect) {
        return true
      } else if (data.userSelect === false) {
        return false
      } else if (data.userSelect === null || data.userSelect === '') {
        return data.defaultSelect
      }
    },
    userSelectIsNull(data) {
      if (data.userSelect == null) {
        return true
      }
      if (data.userSelect === '') {
        return true
      }
    }

  }
}
</script>
<style scoped>
.actionBtns{text-align: right;padding: 10px 0px;}
.notifySetting >>> .el-table__expanded-cell{border-bottom: 0px;padding:0px;}
.notifySetting >>> .el-table__row.expanded{background-color: #dae5f3;
    font-weight: bold;}
</style>
