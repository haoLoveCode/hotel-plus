<template>
  <div class="page-view" v-if="Object.keys(salesItemData).length > 0">
    <BasketView ref="basketViewRef" @handleBasketClose="handleBasketClose"></BasketView>
    <div class="title-view">
      <div class="title-text">
        商品下单
      </div>
    </div>
    <div class="platform-view">
      <div class="item-title-view">
        <div class="title-img-view">
          <img :src="handleImageUrl(salesItemData.mainImg)"
               alt=""
               width="auto"
               height="100%"
               class="main-img-view"
               style="border-radius: 10px;"/>
        </div>
        <div class="title-text-view">
          <div class="title-text" style="color: #00479d;font-size: 30px;">
            {{ salesItemData.itemName }}
          </div>
          <div class="title-text" style="color: #FF0000;font-size: 25px;font-weight: bold">
            ¥: {{ salesItemData.salePrice }}
          </div>
          <div class="title-text" style="color: #808080;font-size: 20px;
                                  text-decoration:line-through;">
            ¥: {{ salesItemData.originalPrice }}
          </div>
          <div class="title-text" style="color: #304156;font-size: 20px;">
            {{ salesItemData.itemTitle }}
          </div>
          <div class="basket-view">
            <div class="basket-btn-view">
              <el-link type="info" :underline="false" @click="handleBasketBiz(1)">
                <img src="/static/icons/add-icon.png"
                     alt=""
                     width="auto"
                     height="30px"
                     style="border-radius: 10px;"/>
              </el-link>
            </div>
            <div class="basket-num-view" v-if="Object.keys(baskItemData).length > 0 && baskItemData.itemNum > 0">
              <!--<el-input
                  size="medium"
                  style="width: 40px;border: 1px solid #304156"
                  autosize
                  v-model="addBasketData.itemNum">
              </el-input>-->
              {{baskItemData.itemNum}}
            </div>
            <div class="basket-num-view" v-else>
              0
            </div>
            <div class="basket-btn-view">
              <el-link type="info" :underline="false" @click="handleBasketBiz(2)">
                <img src="/static/icons/minus-icon.png"
                     alt=""
                     width="auto"
                     height="30px"
                     style="border-radius: 10px;"/>
              </el-link>
            </div>
          </div>
          <div class="buy-now-btn" @click="handleBuyNow">
            <el-link type="info" :underline="false" style="font-size: 20px;margin: 20px 20px">
              <div class="buy-btn-div">
                <div class="buy-btn">
                  立即购买
                </div>
              </div>
            </el-link>
          </div>
        </div>
      </div>
    </div>
    <el-divider content-position="center">收货地址选择</el-divider>
    <div class="address-data-view">
      <div class="address-item-view" v-for="(item,index) in takeAddressList" :key="index">
        <div class="select-item-view">
          <div class="select-item" >
            <!-- `checked` 为 true 或 false -->
            <el-checkbox v-model="item.selected"  @change="changeAddress($event,item)"></el-checkbox>
          </div>
        </div>
        <div class="address-right-view">
          <div class="address-text-view">
            {{item.addressDetailed}}
          </div>
          <div class="address-text-view">
            收货人姓名:{{item.takerName}}
          </div>
          <div class="address-text-view">
            手机号:{{item.takerPhone}}
          </div>
        </div>
      </div>
    </div>
    <div class="main-swiper-view">
      <div class="header-swiper-view">
        <el-carousel height="300px" :initial-index="0">
          <el-carousel-item v-for="(item,index) in mainSwiperList" :key="index">
            <div class="swiper-item-view">
              <img :src="item"
                   alt=""
                   width="auto"
                   height="100%"
                   class="swiper-image"
                   style="border-radius: 10px;"/>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
    <el-divider content-position="center">商品介绍</el-divider>
    <div class="platform-data-view">
      <div class="item-time-view">
        上架时间: {{ salesItemData.createTime }}
      </div>
      <div class="item-value-view" v-html="salesItemData.itemSummary">
      </div>
    </div>
  </div>
</template>
<script>
import {mapGetters} from "vuex";
import moment from 'moment';
import Api from "@/services";
import BasketView from "@/views/biz/basket/BasketView";

export default {
  name: "SalesItemDataView",
  components: {
    BasketView:BasketView
  },
  data() {
    return {
      mainSwiperList: [],
      previewImageUrl: '',
      previewVisible: false,
      currentTime: '',
      salesItemData: {},
      submitData: {},
      commentDataList: [],
      takeAddressList: [],
      userData:{},
      paginationData: {
        itemsPerPage: 10,
        totalCount: 0,
        currentPage: 1
      },
      addBasketData: {
        itemNum: 1
      },
      baskItemData:{}
    };
  },
  computed: {},
  methods: {
    //处理收货地址选择
    async changeAddress(event,value){
      console.log('changeAddress:'+event)
      console.log('changeAddress:'+JSON.stringify(value));
      this.takeAddressList.map(async (item) => {
        //其他的如果选择了则取消选择
        if(item.takeAddressId != value.takeAddressId){
          if(item.selected){
            item.selected = false;
          }
        }
      })
    },
    //查询用户收货地址
    async queryTakeAddress(){
      const loading = this.$loading({
        lock: true,
        text: "正在提交...",
      });
      let params = {
        authAppUserId:this.userData.authAppUserId
      }
      try {
        Api.queryTakeAddress(params).then(async (res) => {
          this.takeAddressList = new Array();
          if (res.success) {
            let dataList = res.data;
            if(!dataList || dataList.length == 0){
              return;
            }
            await dataList.map(async (item) => {
              item.selected = false;
              this.takeAddressList.push(item);
            })
            console.log('收货地址:'+JSON.stringify(this.takeAddressList))
            this.clearAll()
          } else {
            this.$message.error('操作失败');
            this.clearAll()
          }
        });
        loading.close();
      } catch (error) {
        this.clearAll()
        loading.close();
        this.$message.error(error.message || error.msg || "服务器异常");
      }
    },
    //查询当前用户信息
    async currentUserMeta() {
      await Api.currentUserMeta({

      }).then(async (res) => {
        if (!res.success) {
          return;
        }
        if (this.$isNull(res)) {
          return;
        }
        let data = res.data
        if (this.$isNull(data)) {
          return;
        }
        data.avatarUrl = await this.handleImageUrl(data.avatarUrl);
        this.userData = {...data};
        console.log('当前用户信息:' + JSON.stringify(this.userData))
      });
    },
    //处理新增和减少购物车商品
    async handleBasketBiz(bizType){
      const loading = this.$loading({
        lock: true,
        text: "正在提交...",
      });
      let params = {
        bizType:bizType,
        itemNum:1,
        remarkData:"无",
        salesItemId:this.salesItemData.salesItemId,
        authAppUserId:this.userData.authAppUserId,
      }
      try {
        Api.handleBasketBiz(params).then(async (res) => {
          if (res.success) {
            this.$message({
              message: "操作购物车成功",
              type: "success",
            });
            let data = res.data;
            console.log('添加购物车结果:' + JSON.stringify(data));
            await this.queryOneBasketItem(this.salesItemData.salesItemId);
            this.clearAll()
          } else {
            this.$message.error('操作失败');
            this.clearAll()
          }
        });
        loading.close();
      } catch (error) {
        this.clearAll()
        loading.close();
        this.$message.error(error.message || error.msg || "服务器异常");
      }
    },
    //处理立即购买
    async handleBuyNow() {
      let selectAddress = this.takeAddressList.find((item) => item.selected === true);
      if(!selectAddress || !selectAddress.selected){
        this.$message.error("请选择收货地址");
        return;
      }
      let basketDataId = this.baskItemData.basketDataId;
      if(!basketDataId){
        //设置收货地址ID
        this.salesItemData.takeAddressId = selectAddress.takeAddressId;
        await this.orderingOne(this.salesItemData);
      }else{
        //设置收货地址ID
        this.baskItemData.takeAddressId = selectAddress.takeAddressId;
        await this.basketItemOrdering(this.baskItemData);
      }
      let salesItemId = this.salesItemData.salesItemId;
      await this.queryOneSalesItem(salesItemId);
      await this.queryOneBasketItem(salesItemId);
    },
    //购物车下单
    async orderingOne(item){
      await Api.orderingOne({
        itemId: item.salesItemId,
        takeAddressId: item.takeAddressId
      }).then(async (res) => {
        if (!res.success) {
          return;
        }
        if (this.$isNull(res)) {
          return;
        }
        console.log('单个商品下单结果:' + JSON.stringify(res))
        let data = res.data
        if (this.$isNull(data)) {
          return;
        }
        this.$message({
          message: "下单成功",
          type: "success",
        });
      });
    },
    //购物车下单
    async basketItemOrdering(item){
      let basketDataIdList = new Array();
      basketDataIdList.push(item.basketDataId)
      await Api.basketItemOrdering({
        basketDataIdList: basketDataIdList,
        takeAddressId: item.takeAddressId,
      }).then(async (res) => {
        if (!res.success) {
          return;
        }
        if (this.$isNull(res)) {
          return;
        }
        console.log('购物车下单结果:' + JSON.stringify(res))
        let data = res.data
        if (this.$isNull(data)) {
          return;
        }
        this.$message({
          message: "下单成功",
          type: "success",
        });
      });
    },
    clearAll() {
      console.log('触发清除所有')
      this.submitData = {}
    },
    //查询单个其他信息
    async queryOneSalesItem(salesItemId) {
      await Api.queryOneSalesItem({
        salesItemId: salesItemId
      }).then(async (res) => {
        if (!res.success) {
          return;
        }
        if (this.$isNull(res)) {
          return;
        }
        let data = res.data
        console.log('data:' + JSON.stringify(data))
        if (this.$isNull(data)) {
          return;
        }
        this.salesItemData = {...data}
        this.mainSwiperList = new Array();
        let imgList = data.imgList;
        await imgList.map(async (item) => {
          let mainImg = await this.handleImageUrl(item);
          this.mainSwiperList.push(mainImg);
        });
        //console.log('mainSwiperList:' + JSON.stringify(this.mainSwiperList))
      });
    },
    //查询单个购物车商品信息
    async queryOneBasketItem(salesItemId) {
      await Api.queryOneBasketItem({
        salesItemId: salesItemId
      }).then(async (res) => {
        if (!res.success) {
          return;
        }
        if (this.$isNull(res)) {
          return;
        }
        //console.log('queryOneBasketItem:data:' + JSON.stringify(res))
        let data = res.data
        if (this.$isNull(data)) {
          return;
        }
        if(!data.basketDataId){
          return;
        }
        this.baskItemData = {...data}
      });
    },
    handlePreView(url) {
      if (this.$isNull(url)) {
        return;
      }
      this.previewImageUrl = url
      this.previewVisible = true
    },
    handleTypeByValue(value, optionList) {
      if (this.$isNull(value)) {
        console.log('返回空')
        return '无'
      }
      if (this.$isNull(optionList)) {
        console.log('optionList返回空')
        return '';
      }
      let result =
          optionList.find(item => item.value == value);
      if (!this.$isNull(result)) {
        return result.text
      } else {
        return '无'
      }
    },
    //处理关闭购物车
    async handleBasketClose(){
      await this.currentUserMeta();
      let salesItemId = this.salesItemData.salesItemId;
      await this.queryOneSalesItem(salesItemId);
      await this.queryOneBasketItem(salesItemId);
    },
    async init() {
      this.currentTime = moment().format("YYYY-MM-DD HH:mm:ss");
    },
  },
  created() {
    console.log(this.$route.query.salesItemId);
    let salesItemId = this.$route.query.salesItemId;
    this.currentUserMeta();
    this.queryTakeAddress();
    this.queryOneSalesItem(salesItemId);
    this.queryOneBasketItem(salesItemId);
    this.init();
  },
  mounted() {

  },
};
</script>

<style scoped lang="scss">
.page-view {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  align-content: center;
  position: relative;
  width: 100%;
  .title-view {
    width: 90%;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-content: center;
    font-size: 20px;
    margin-top: 10px;
    .title-text {
      margin: 10px 10px;
      font-weight: bold;
    }
  }
  .basket-flow-view{
    position: fixed; //让元素跟随滚动
    z-index: 998;
    right:0;
    top: 50%;
    .basket-data-view{
      width: 150px;
      height: 50px;
      background-color: #FF0000;
      color: #FFFFFF;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      border-radius: 10px;
      .basket-text-view{
        font-size: 20px;
        font-weight: bold;
      }
    }
  }
  .address-data-view{
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
    width: 90%;
    margin-top: 20px;
    border: 1px solid #909399;
    border-radius: 10px;
    .address-item-view{
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-items: center;
      width: 50%;
      border-bottom: 1px solid #909399;
      margin: 10px 0px;
      .select-item-view{
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        margin: 10px 10px;
        .select-item{

        }
      }
      .address-right-view{
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: flex-start;
        width: 100%;
        .address-text-view{

        }
      }
    }
  }
  .main-swiper-view {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    margin-top: 20px;
    .header-swiper-view {
      width: 90%;
      margin: 10px 5px;
      //border: 1px solid #909399;
      border-radius: 10px;
      .swiper-item-view {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        height: 100%;
        width: 100%;
        border-radius: 10px;
        //此处不应当设置宽度和高度
        .swiper-image {
          border-radius: 10px;
        }
      }
    }
  }
  .platform-view {
    width: 90%;
    margin: 10px 0px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    .item-title-view {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-items: center;
      width: 100%;
      .title-img-view {
        border-radius: 10px;
        margin: 10px 10px;
        width: 230px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        .main-img-view {
          margin: 5px 0px;
          width: 200px;
        }
      }
      .title-text-view {
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: center;
        margin: 10px 30px;
        width: 60%;
        //border: 1px solid #304156;
        .title-text {
          margin-left: 20px;
          font-weight: bold;
          word-break: break-all;
          width: 100%;
        }
        .basket-view {
          margin-left: 20px;
          font-weight: bold;
          width: 100%;
          //border: 1px solid #304156;
          display: flex;
          flex-direction: row;
          justify-content: flex-start;
          align-items: center;
          margin-top: 10px;
          .basket-num-view {
            margin: 10px 10px;
          }
          .basket-btn-view{
            margin: 0px 10px;
          }
        }
        .buy-now-btn {
          margin-left: 10px;
          font-weight: bold;
          width: 100%;
          //border: 1px solid #304156;
          display: flex;
          flex-direction: row;
          justify-content: flex-start;
          align-items: center;
          .buy-btn-div {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            background-color: #FF0000;
            border-radius: 10px;
            .buy-btn {
              margin: 10px 10px;
              color: #FFFFFF;
              font-weight: bold;
            }
          }
        }
      }
    }
  }
  .platform-data-view {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    border-radius: 10px;
    //margin-top: 20px;
    width: 90%;
    .item-time-view {
      border-radius: 10px;
      width: 80%;
      color: #00479d;
      font-size: 20px;
    }
    .item-value-view {
      border-radius: 10px;
      width: 80%;
      margin-top: 20px;
      word-break: break-all;
    }
  }
}
/deep/ .select-item .el-checkbox__inner{
  width: 20px!important;
  height: 20px!important;
}
// 修改中心对号大小以及位置
/deep/ .select-item .el-checkbox__inner::after {
  left: 6px;
  height: 10px;
  width: 5px;
  top: 3px;
  font-size: 10px;
}
</style>
