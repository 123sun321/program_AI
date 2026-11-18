<template>
  <div class="container" :style="{height:mapOption.style.height,width:mapOption.style.width}">
    <div>
      <el-form :model="form" ref="form" :inline="true">
        <el-form-item prop="name" :rules="[{required:true,message:'请填写地址'}]">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div id="container" :style="mapOption.style">
    </div>
    <div :style="modalStyle" class="modal-position" v-loading="infoloading">
      <div class="close">
        <div class="head-l">
          坐标信息
        </div>
        <div class="head-r">
          <i class="el-icon-circle-close" @click.stop="close"></i>
        </div>
      </div>
      <div class="info">
        <span class="tit">地址信息:</span><br>
        <span class="cot">{{point.address}}</span><br>
        <span class="tit">经纬度:</span> <br>
        <span class="cot">[{{point.longitude.lng}},{{point.longitude.lat}}]</span>
      </div>
      <div class="btom">
        <el-button type="primary" @click="seleted">选中</el-button>
      </div>
      <div>
      </div>
    </div>
  </div>
</template>

<script>
  import BMap from 'BMap'
  export default {
    props: ['option'],
    data() {
      return {
        form: {
          name: undefined
        },
        mapInstance: undefined,
        modalStyle: {
          top: '0px',
          left: '0px',
          display: 'none'
        },
        point: {
          address: '',
          longitude: {
            lng: 0,
            lat: 0
          }
        },
        infoloading: false

      }
    },
    computed: {
      mapOption() {
        return Object.assign({}, this.option, {
          style: {
            height: '500px',
            width: '100%'
          },
          startPoint: [116.404, 39.915]
        })
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.mapInit()
      })
    },
    methods: {
      mapInit() {
        this.mapInstance = new BMap.Map('container')
        const startPoint = this.mapOption.startPoint
        var point = new BMap.Point(startPoint[0], startPoint[1]) // 创建点坐标
        this.mapInstance.centerAndZoom(point, 15)
        this.mapInstance.enableScrollWheelZoom(true)
        var marker = new BMap.Marker(point) // 创建标注
        this.mapInstance.addOverlay(marker) // 将标注添加到地图中
        /**
         * 添加控件
         */
        this.mapInstance.addControl(new BMap.NavigationControl())
        this.mapInstance.addControl(new BMap.ScaleControl())
        this.mapInstance.addControl(new BMap.OverviewMapControl())

        this.mapInstance.addEventListener('click', this.selectPoint)
      },
      selectPoint(e) {
        const point = new BMap.Point(e.point.lng, e.point.lat)
        const marker = new BMap.Marker(point)
        this.mapInstance.addOverlay(marker)

        this.modalStyle.left = e.offsetX + 10 + 'px'
        this.modalStyle.top = e.offsetY + 'px'
        this.modalStyle.display = 'block'

        const myGeo = new BMap.Geocoder()
        const self = this

        this.infoloading = true
        myGeo.getLocation(new BMap.Point(e.point.lng, e.point.lat), function(result) {
          self.point.address = result.address
          self.point.longitude.lat = e.point.lat
          self.point.longitude.lng = e.point.lng
          self.infoloading = false
        })
      },
      close() {
        this.modalStyle.display = 'none'
      },
      search() {
        this.$refs.form.validate(e => {
          if (e) {
            var local = new BMap.LocalSearch(this.mapInstance, {
              renderOptions: {
                map: this.mapInstance
              }
            })
            local.search(this.form.name)
          }
        })
      },
      seleted() {
        this.$emit('seletepoint', this.point)
        this.modalStyle.display = 'none'
      },
      // 定位
      theLocation(longitude, latitude) {
        this.mapOption.startPoint = [longitude, latitude]
        this.mapInit()
      }
    }
  }

</script>

<style scoped lang="scss">
  .modal-position {
    position: absolute;
    width: 200px;
    height: 200px;
    border: 1px solid #eeeeee;
    background-color: #f8f8f8;
    transition: all .3s ease-in;
    display: none;


    .close {
      text-align: right;
      font-size: 16px;
      height: 25px;
      border-bottom: 1px solid #eeeeee;

      .head-l {
        float: left;
        width: 80%;
        text-align: left;
        line-height: 25px;
        font-weight: bold;
        font-size: 14px;

      }

      .head-r {
        width: 20%;
        float: right;
      }


    }

    .info {
      font-size: 14px;
      line-height: 25px;
      padding: 0px 5px;

      .tit {
        color: #888;
        font-weight: bolder;

      }

      .cot {

        color: #888;
      }
    }

    .btom {
      position: absolute;
      bottom: 0px;
      width: 100%;
      height: 39px;
      border-top: 1px solid #eeeeee;
      padding-right: 5px;
      padding-top: 5px;
      text-align: right;
    }
  }

  .container {
    position: relative;
  }

</style>
