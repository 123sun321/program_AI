<template>
  <!-- 上传图片组件 -->
  <div class="imgSum">
    <!-- <el-button type="text" @click="dialogVisible = true">点击打开 Dialog</el-button> -->
    <div class="btn">
      <slot name="btn"> </slot>
    </div>

    <el-dialog :visible.sync="dialogVisible" width="60%">
      <el-tabs v-model="tabsActiveName" style="height:420px;margin-top:-50px;margin-right:10px;">
        
        <el-tab-pane label="我的图片" name="first">
          <!-- <el-row class="tac"> -->
          <el-row>
            <el-col :span="4"  class="menuBox">
              <el-menu defaultActive="1"  class="el-menu-demo">
                <template v-for="(item,index) in imagesGroup" >
                    <el-menu-item :key="item.id" :index="index+1+'-'" v-if="item.children.length==0" @click="filter(item)">{{item.name}}</el-menu-item> 
                    <el-submenu v-else :key="item.id" :index="index+1+'-'">
                       <template slot="title">{{item.name}}</template>
                       <el-menu-item class="el-menu-item-hover" :index="item.id+'-'+child.id" v-for="child in item.children" :key="child.id" @click="filter(child)">
                        <i class="el-icon-close icon-inmenu" style="left:0px;" @click="deleteGroup(child.id)"></i>
                         {{child.name}}
                        <i style="right:0px;" class="el-icon-edit icon-inmenu" @click="addOrModifyGroupBefore(child.id)"></i>
                      </el-menu-item>                      
                    </el-submenu>
                </template>  
               
              </el-menu>
            </el-col>
            <el-col :span="20"  class="menuBox" style="background-color:rgb(232,232,240)">
              <template v-for="item in groupImgs">
                <template v-if="!group">                 
                    <div :class="[item.isSelect?'imgBoxGroup':'imgBox']"  @click="selectImg(item)" :key="item.id">
                      <img :src="item.path" width="85px" height="85px" :alt="item.id">
                    </div>                 
                </template>
                <template v-else>
                  <div class="imgBox" :key="item.id">
                    <img :src="item.path" style="width:85px;height:85px;" :alt="item.id" @click="selectCropImgBefore(item)">
                    <div class="deleteImg" @click="deleteFile(item.id,item.name)">删除</div>
                  </div>
                </template>
              </template>
            </el-col>
          </el-row>
        </el-tab-pane>


        <!-- <el-tab-pane label="图片库" name="second">
           <el-row>

            <el-col :span="4"  class="menuBox">
              <el-menu defaultActive="1"  class="el-menu-demo" >
                <template v-for="item in sysGroup">
                  <el-menu-item  :key="item.id" :index="item.id+'-'">
                    <span>{{item.name}}</span>
                  </el-menu-item>
                </template>
              </el-menu>
            </el-col>

            <el-col :span="20"  class="menuBox"  style="background-color:rgb(232,232,240)">
              <template v-for="item in imgList">
                <div  class="imgBox" :key="item.id">
                  <img :src="item.url" width="85px" height="85px">
                </div>
              </template>
            </el-col>

          </el-row>
        </el-tab-pane> -->

      </el-tabs>

      <el-row slot="footer" v-if="(this.tabsActiveName === 'first')">
        <template v-if="group">
          <el-col :span="3">
            <co-upload :action="postUrl" @success="uploadSuccess" >
               <el-button type="primary" size="mini">上传至当前分组</el-button>
            </co-upload>           
          </el-col>
          <el-col :span="2" :offset="16">
            <el-button type="primary" size="mini" plain @click="addOrModifyGroupBefore">新建分组</el-button>
          </el-col>
          <el-col :span="3">
            <el-button type="primary" size="mini" plain @click="group = false">批量处理</el-button>
          </el-col>
        </template>
        <template v-else>
          <el-col>
            <span style="font-size:12px;line-height:26px;color: rgba(0, 0, 0, 0.65);">已选中{{ selectImgNumber }}张 移动到</span>
            <el-select v-model="groupSelect" placeholder="请选择" size="mini" style="width:110px;">
              <el-option v-for="item in groupsbyUser" :key="item.id" :value="item.id" :label="item.name"></el-option>         
             
            </el-select>
            <el-button type="primary" size="mini" :disabled="selectImgNumber==0 || !groupSelect" @click="someRemoveTo">确定</el-button>
            <el-button type="primary" plain size="mini" :disabled="selectImgNumber==0" @click="deleteSelectFile">删除选中的图片</el-button>
            <el-button type="primary" plain size="mini" @click="group = true">取消</el-button>
          </el-col>
        </template>
      </el-row>
      <img-crop ref="crop" @success="cropSuccess"></img-crop>   
    </el-dialog>    
    
  </div>
</template>

<script>

import filemixin from '@/views/functions/file/fileMixin'
import coUpload from '@/components/Uploader'
import imgCrop from './imgCrop'

const sysGroup = [
  { name: '全部', id: 0, children: [] },
  { name: '背景', id: 1, children: [] },
  { name: '节日', id: 2, children: [] },
  { name: '图标', id: 3, children: [] },
  { name: '素材', id: 4, children: [] },
  { name: '边框', id: 5, children: [] },
  { name: '品牌', id: 6, children: [] },
  { name: 'GIF', id: 7, children: [] }
]

export default {
  mixins: [filemixin],
  components: {
    coUpload,
    imgCrop
  },
  data() {
    return {
      dialogVisible: false, // 图片弹窗
      tabsActiveName: 'first', // 面板选择
      sysGroup,
      group: true,
      groupSelect: undefined, // 批量选中
      classStyle: 'imgBox',
      innerVisible: false,
      selectCropImgBlob: undefined
    }
  },
  computed: {
    groupList() {
    },
    selectImgNumber() {
      const selectitem = this.groupImgs.filter(e => {
        return e.isSelect
      })
      return selectitem.length
    },
    groupsbyUser() {
      return this.imagesGroup[1].children
    }
  },
  created() {
  },
  methods: {
    deleteImg(item) { // 获取要删除图片的数据
      console.log(item)
      this.hintDialog = true
      this.deleteItem = item
    },
    sureDeleteImg() { // 确定删除图片
      var item = this.deleteItem
      this.hintDialog = false
      if (item.type === 'allImg') {
        this.allImg.splice(this.allImg.findIndex(function(val, index, arr) {
          return val.id === item.id
        }), 1)
      }
      this.$message({
        message: '删除成功',
        type: 'success'
      })
    },

    addOrModifyGroupBefore(id) {
      const param = {}
      if (typeof id === 'number') {
        param.id = id
      } else {
        param.moduleId = 'file'
      }
      this.$prompt('请输入组名称', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[\S]*/,
        inputErrorMessage: '组名称不能为空'
      }).then(({ value }) => {
        param.name = value
        this.addOrModify(param)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        })
      })
    },

    dialogVisibleIsTure() { // 打开图片弹窗
      this.dialogVisible = true
    },

    selectImg(item) { // 批量操作---图片选中
      item.isSelect = !item.isSelect
    },
    someRemoveTo() {
      const selectItems = this.groupImgs.filter(e => { return e.isSelect })
      if (selectItems.length === 0) {
        this.$notify({
          message: '请选择图片',
          type: 'error'
        })
      } else {
        this.removeToSuccess(this.groupSelect, selectItems.map(e => { return e.id }))
      }
    },
    upload() {
      if (this.selectType !== 4) {
        this.$notify({
          message: '请选择分组'
        })
      }
    },
    selectCropImgBefore(item) {
      this.$refs['crop'] && this.$refs['crop'].open(item)
    },
    cropSuccess(e) {
      this.dialogVisible = false
      this.$emit('selected', e)
    }

  }

}

</script>

<style lang="scss" scoped>
.el-menu-item-hover:hover > .icon-inmenu{
  display: inline;
}
.icon-inmenu{
  position: absolute;
  top:12px;
  display: none;
}
.icon-inmenu:hover{
  display: inline;
}
.imgBox{
  width: 85px;
  height: 85px;
  float: left;
  margin: 10px;
  background-color: #ffffff;
  //padding-top: 17px;
  position: relative;
  .deleteImg{
    background-color: rgb(231, 155, 155);
    color: #ffffff;
    text-align: center;
    font-size: 12px;
    position: absolute;
    bottom: 0px;
    width: 100%;
    opacity: 0.0;
    cursor:pointer;
  }
  &:hover{
    background-color: #dcdcdc;
    .deleteImg {
      opacity: 1.0;
    }
  }
}
.imgBoxGroup{
  width: 85px;
  height: 85px;
  float: left;
  margin: 10px;
  background-color: #dcdcdc;
  // padding-top: 17px;
  position: relative;
  border: 1px solid blue;
  .deleteImg{
    background-color: rgb(231, 155, 155);
    color: #ffffff;
    text-align: center;
    font-size: 12px;
    position: absolute;
    bottom: 0px;
    width: 100%;
    opacity: 0.0;
    cursor:pointer;
  }
}
.el-dialog__body {
  padding: 0px 20px!important;
}
.menuBox{
  height: 390px;
  overflow:auto; 
  .menuItem{
    .iconLeft{
      float:left;
      line-height:45px;
      margin-left:-25px;
      opacity: 0.0;
    }
    .iconRight{
      float:right;
      line-height:45px;
      opacity: 0.0;
    }
    &:hover{
      .iconLeft{
        opacity: 1.0;
      }
      .iconRight{
        opacity: 1.0;
      }
    }
  }
}

.deleteImg :hover{
  opacity: 1.0;
}
::-webkit-scrollbar {display:none}
</style>