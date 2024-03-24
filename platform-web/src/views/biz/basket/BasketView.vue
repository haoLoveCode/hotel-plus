<template>
  <div class="page-view">
    <div class="basket-flow-view" @click="handleShowDrawer">
      <div class="basket-data-view">
        <div class="basket-text-view">
          购物车
        </div>
      </div>
    </div>
    <!--地址选择-->
    <SelectAddress ref="selectAddressRef" @handleSelect="handleSelect"></SelectAddress>
    <el-drawer
        :visible.sync="showDrawer"
        :direction="direction"
        :with-header="false"
        :before-close="handleDrawerClose">
      <div class="basket-data-view">
        <div class="basket-text-view">
          <div class="basket-text">
            我的购物车
          </div>
        </div>
        <div class="basket-item-view">
          <div class="basket-sales-item" v-for="(item,index) in basketItemList" :key="index">
            <div class="basket-left-item">
              <div class="select-item-view">
                <div class="select-item" >
                  <el-checkbox v-model="item.selected"  @change="selectSalesItem($event,item)"></el-checkbox>
                </div>
              </div>
              <div class="left-sales-item">
                <div class="left-img">
                  <img :src="item.mainImg"
                       alt=""
                       width="auto"
                       height="80px"
                       style="border-radius: 5px;"/>
                </div>
                <div class="left-text-view">
                  {{item.itemName}}
                </div>
              </div>
            </div>
            <div class="basket-right-item">
              <div class="right-price-view">
                ¥{{item.salePrice}}元
              </div>
              <div class="right-btn-view">
                <div class="right-btn-item">
                  <el-link type="info" :underline="false" @click="handleBasketBiz(1,item)">
                    <img src="/static/icons/add-icon.png"
                         alt=""
                         width="auto"
                         height="30px"
                         style="border-radius: 10px;"/>
                  </el-link>
                </div>
                <div class="right-num-view">
                  {{item.itemNum}}
                </div>
                <div class="right-btn-item">
                  <el-link type="info" :underline="false" @click="handleBasketBiz(2,item)">
                    <img src="/static/icons/minus-icon.png"
                         alt=""
                         width="auto"
                         height="30px"
                         style="border-radius: 10px;"/>
                  </el-link>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="bottom-btn-view">
          <div>
            <el-button type="danger">批量删除</el-button>
            <el-button type="primary">新增地址</el-button>
          </div>
          <el-link type="info"
                   @click="handleSelectAddress"
                   :underline="false" style="font-size: 20px;width: 100%;color: #1f2d3d">
            <div class="address-div">
              <div class="address-text">
                收货地址: {{takeAddress.addressDetailed}}
              </div>
            </div>
          </el-link>
          <el-link
              type="info"
              @click="handleBuyNow"
              :underline="false"
              style="font-size: 20px;width: 100%">
            <div class="bottom-btn-div">
              <div class="bottom-btn-text">
                立即结算
              </div>
            </div>
          </el-link>
        </div>
        
      </div>
    </el-drawer>
    </div>
</template>
<script>
import {mapGetters} from "vuex";
import moment from 'moment';
import Api from "@/services";
import SelectAddress from "@/views/biz/basket/SelectAddress";

export default {
  name: "BasketView",
  props: {
    /*showDrawer: {
      type: Boolean,
      default: () => {
        return true;
      }
    },*/
  },
  components: {
    SelectAddress:SelectAddress
  },
  data() {
    return {
      userData:{},
      //是否展示购物车
      showDrawer:false,
      direction:'rtl',
      addBasketData: {
        itemNum: 1
      },
      basketItemList:[],
      //结算时候的收货地址
      takeAddress:{},
      //等待结算的商品
      basketDataIdList:[],
    };
  },
  computed: {},
  methods: {
    //处理地址选择结果
    handleSelect(data){
      console.log('selectAddress:'+JSON.stringify(data))
      this.takeAddress = data;
    },
    //处理选择地址
    async handleSelectAddress(){
      this.$refs.selectAddressRef.handleShowSelect();
    },
    //处理立即购买
    async handleBuyNow() {
      let takeAddress = this.takeAddress;
      if(!takeAddress || !takeAddress.selected){
        this.$message.error("请选择收货地址");
        return;
      }
      let basketDataIdList = this.basketDataIdList;
      if(!basketDataIdList || basketDataIdList.length === 0){
        this.$message.error("请选择需求结算的商品");
        return;
      }
      //设置收货地址ID
      let orderParams = {
        takeAddressId:takeAddress.takeAddressId,
        basketDataIdList:basketDataIdList
      }
      await this.basketItemOrdering(orderParams);
    },
    //购物车下单
    async basketItemOrdering(data){
      await Api.basketItemOrdering({
        ...data
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
        await this.handleDrawerClose();
      });
    },
    //处理购物车里面的商品选择
    async selectSalesItem(event,value){
      console.log('selectSalesItem:'+event)
      //console.log('selectSalesItem:'+JSON.stringify(value));
      //放入结算商品集合
      if(value.selected){
        this.basketDataIdList.push(value.basketDataId);
      }else{
        this.basketDataIdList = this.basketDataIdList.filter((item) => {
          return item != value.basketDataId
        });
        let data = this.basketDataIdList.filter((item) => {
          return item != value.basketDataId
        });
        console.log('执行删除')
        console.log('执行删除:'+JSON.stringify(data))
      }
      console.log('this.basketDataIdList:'+JSON.stringify(this.basketDataIdList))
      console.log('this.basketDataIdList.length:'+this.basketDataIdList.length);
    },
    //处理新增和减少购物车商品
    async handleBasketBiz(bizType,item){
      const loading = this.$loading({
        lock: true,
        text: "正在提交...",
      });
      let params = {
        bizType:bizType,
        itemNum:1,
        remarkData:"无",
        salesItemId:item.salesItemId,
        authAppUserId:this.userData.authAppUserId,
      }
      try {
        Api.handleBasketBiz(params).then(async (res) => {
          if (res.success) {
            this.$message({
              message: "操作购物车成功",
              type: "success",
            });
            await this.queryBasketItem();
            let data = res.data;
            console.log('处理购物车结果:' + JSON.stringify(data));
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
    //处理展示购物车信息
    async handleShowDrawer(){
      this.showDrawer = !this.showDrawer;
      if(this.showDrawer){
        let data = await this.$bizConstants.userMeta();
        this.userData = {...data};
        console.log('当前用户信息:' + JSON.stringify(this.userData))
        await this.queryBasketItem();
      }
    },
    //查询当前用户购物车的商品信息
    async queryBasketItem(){
      const loading = this.$loading({
        lock: true,
        text: "正在提交...",
      });
      let params = {}
      try {
        Api.queryBasketItem(params).then(async (res) => {
          this.basketItemList = new Array();
          if (res.success) {
            let dataList = res.data;
            if(!dataList || dataList.length == 0){
              return;
            }
            await dataList.map(async (item) => {
              let mainImg = await this.handleImageUrl(item.mainImg);
              item.mainImg = mainImg;
              this.basketItemList.push(item);
            })
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
    //处理抽屉关闭
    async handleDrawerClose(){
      this.clearAll();
      this.showDrawer = false;
      this.$emit("handleBasketClose");
    },
    clearAll() {
      console.log('触发清除所有')
      this.userData = {};
      this.takeAddress = {}
      this.basketItemList = new Array();
      this.basketDataIdList = new Array();
    },
    async init() {
      this.currentTime = moment().format("YYYY-MM-DD HH:mm:ss");
    },
  },
  created() {
    console.log(this.$route.query.salesItemId);
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
  .basket-data-view{
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    align-content: center;
    .basket-text-view{
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      align-content: center;
      width: 100%;
      //border-bottom: 1px solid #909399;
      .basket-text{
        font-size: 20px;
        margin: 20px 10px;
        font-weight: bold;
      }
    }
    .basket-item-view{
      width: 100%;
      overflow-y: auto;
      overflow-x: hidden;
      height: 700px;
      .basket-sales-item{
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        align-content: center;
        width: 95%;
        margin: 10px 10px;
        box-shadow: 0 0 2px 2px #409eff;
        border-radius: 10px;
        .basket-left-item{
          display: flex;
          flex-direction: row;
          justify-content: flex-start;
          align-items: center;
          align-content: center;
          margin-left: 10px;
          .select-item-view{
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            align-content: center;
            .select-item{

            }
          }
          .left-sales-item{
            display: flex;
            flex-direction: row;
            justify-content: flex-start;
            align-items: center;
            align-content: center;
            margin: 10px 10px;
            width: 200px;
            .left-img{

            }
            .left-text-view{
              margin-left: 10px;
              font-size: 10px;
              word-break: break-all;
            }
          }
        }
        .basket-right-item{
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          align-content: center;
          margin-top: 10px;
          .right-text-view{
            font-weight: bold;
            font-size: 20px;
          }
          .right-price-view{
            font-weight: bold;
            font-size: 20px;
            color: #FF0000;
          }
          .right-btn-view{
            display: flex;
            flex-direction: row;
            justify-content: space-around;
            align-items: center;
            align-content: center;
            margin-top: 10px;
            .right-btn-item{
              margin: 0px 10px;
            }
            .right-num-view{
            }
          }
        }
      }
    }
    .bottom-btn-view {
      font-weight: bold;
      display: flex;
      flex-direction: column;
      justify-content: center;
      margin: 20px 10px;
      .address-div{
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: center;
        border-radius: 10px;
        width: 400px;
        margin: 20px 0;
        .address-text{
            word-break: break-all;
        }
      }
      .bottom-btn-div {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        background-color: #FF0000;
        border-radius: 10px;
        width: 400px;
        .bottom-btn-text {
          margin: 5px 10px;
          color: #FFFFFF;
          font-weight: bold;
        }
      }
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
}
/deep/ .select-item .el-radio__inner{
  width: 20px!important;
  height: 20px!important;
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
