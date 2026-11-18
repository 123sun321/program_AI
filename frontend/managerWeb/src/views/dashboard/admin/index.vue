<template>
  <div class="dashboard-editor-container">
    <el-row>
      <panel-card title="常用功能">
        <el-row :gutter="20" class="contentBox">
          <el-col :span="6" class="itemBox">
            <div class="svgBox" @click="target('/shop/onlineretailers')"><svg-icon icon-class="shoppingCard"  class="svgSize"/></div>
            <div class="textBox" @click="target('/shop/onlineretailers')">
              <div class="title">电商</div>
              <div class="details">电商商品、运费等管理</div>
            </div>
          </el-col>
          <el-col :span="6" class="itemBox">
            <div class="svgBox" @click="target('/functions/carousel')"><svg-icon icon-class="dashboard"  class="svgSize"/></div>
            <div class="textBox" @click="target('/functions/carousel')">
              <div class="title">轮播</div>
              <div class="details">推荐商品、活动公告</div>
            </div>
          </el-col>
          <el-col :span="6" class="itemBox">
            <div class="svgBox" @click="target('/shop/freight')"><svg-icon icon-class="people" class="svgSize"/></div>
            <div class="textBox" @click="target('/shop/freight')">
              <div class="title">运费</div>
              <div class="details">运费模板、配送区域配置</div>
            </div>
          </el-col>
          <el-col :span="6" class="itemBox">
            <div class="svgBox" @click="target('/assets/index')"><svg-icon icon-class="example"  class="svgSize"/></div>
            <div class="textBox" @click="target('/assets/index')">
              <div class="title">账户资金</div>
              <div class="details">查看收支明细，交易一目了然</div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="contentBox">
          <el-col :span="6" class="itemBox">
            <div class="svgBox" @click="target('/marketing/coupon')"><svg-icon icon-class="money"  class="svgSize"/></div>
            <div class="textBox" @click="target('/marketing/coupon')">
              <div class="title">优惠券</div>
              <div class="details">根据使用场景设置优惠券规则</div>
            </div>
          </el-col>
          <el-col :span="6" class="itemBox">
            <div class="svgBox" @click="target('/marketing/memberCard')"><svg-icon icon-class="peoples"  class="svgSize"/></div>
            <div class="textBox" @click="target('/marketing/memberCard')">
              <div class="title">会员卡</div>
              <div class="details">设置并给用户发放会员卡</div>
            </div>
          </el-col>
          <el-col :span="6" class="itemBox">
            <div class="svgBox" @click="target('/marketing/points')"><svg-icon icon-class="star" class="svgSize"/></div>
            <div class="textBox" @click="target('/marketing/points')">
              <div class="title">积分</div>
              <div class="details">让客户用积分兑换商品</div>
            </div>
          </el-col>
          <el-col :span="6" class="itemBox">
            <div class="svgBox" @click="target('/marketing/salesman')"><svg-icon icon-class="clipboard" class="svgSize"/></div>
            <div class="textBox" @click="target('/marketing/salesman')">
              <div class="title">分销员</div>
              <div class="details">客户带客户，销量涨涨涨</div>
            </div>
          </el-col>
        </el-row>
      </panel-card>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <panel-card title="TOP商品销量排行" style="height:500px;">
          <div v-if="goodsRanking.length === 0" class='noneData'>暂无数据</div>
          <div v-else>
            <el-table :data="goodsRanking" style="width: 100%;background-color:#fbfbfb;">
              <el-table-column prop="goodsName" label="商品名称" width="180"></el-table-column>
              <el-table-column prop="goodsPrice" label="商品价格" width="180"></el-table-column>
              <el-table-column prop="goodsNumber" label="销量"></el-table-column>
              <el-table-column prop="profitMoney" label="利润额"></el-table-column>
            </el-table>
          </div>
        </panel-card>
      </el-col>
      <el-col :span="12">
        <panel-card title="TOP10用户购买排行"  style="height:500px;">
          <div v-if="userRanking.length === 0" class='noneData'>暂无数据</div>
          <div v-else>
            <el-table :data="userRanking" style="width: 100%;background-color:#fbfbfb;">
              <el-table-column prop="surname" label="用户昵称" width="180"></el-table-column>
              <el-table-column prop="userName" label="手机号" width="180"></el-table-column>
              <el-table-column prop="orderCount" label="订单总量"></el-table-column>
              <el-table-column prop="goodsCount" label="商品件数"></el-table-column>
            </el-table>
          </div>
        </panel-card>
      </el-col>
    </el-row>
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <!-- <line-chart :chart-data="lineChartData"></line-chart> -->
    </el-row>

  </div>
</template>

<script>

import PanelCard from '@/components/Panel/PanelCard.vue' // 面板组件
export default {
  name: 'dashboard-admin',
  components: {
    PanelCard
  },
  data() {
    return {
      lineChartData: null,
      refundData: [], // 退款商品
      userRanking: [],
      goodsRanking: []
    }
  },
  created() {

  },
  methods: {
    handleSetLineChartData() {
    },
    // 跳转
    target(url) {
      this.$router.push({ path: url })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard-editor-container {
  padding: 20px;
  background-color: rgb(240, 242, 245);
  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}
.contentBox{
  .itemBox{
    background-color: #f6f7fb;
    padding: 10px;
    .svgBox{
      float: left;
      background: #e3ecf3;
      // width: 80px;
      height: 80px;
      cursor: pointer;
      .svgSize{
        width: 40px;
        height: 40px;
        margin: 20px;
        color: #65b7f6;
      }
    }
    .textBox{
      float: left;
      padding: 10px 20px;
      width: 196px;
      height: 80px;
      cursor: pointer;
      .title{
        font-size: 14px;
        margin-bottom: 10px;
      }
      .details{
        font-size: 12px;
      }
    }
  }
}
.noneData{
  height: 500px;
  text-align: center;
  width: 100%;
  padding-top:260px;
  background-repeat:no-repeat;  
  background-image: url('~@/assets/images/c6.jpg');
  background-position:50% 30%; 
}
</style>
