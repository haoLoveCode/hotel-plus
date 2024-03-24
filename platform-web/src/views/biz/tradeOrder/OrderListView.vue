<template>
  <div class="page-view" v-if="orderDataList && Object.keys(orderDataList).length > 0" >
    <div class="title-view">
      <div class="title-text">
        订单列表
      </div>
    </div>
    <div class="order-data-view">
      <div class="order-item-view"
           @mouseover="itemActive($event)" @mouseout="removeActive($event)"
           v-for="(item,index) in orderDataList" :key="index">
        <div class="item-title-view">
          <div class="title-img-view">
            <img :src="handleImageUrl(item.mainImg)"
                 alt=""
                 width="auto"
                 height="100%"
                 class="main-img-view"
                 style="border-radius: 10px;"/>
          </div>
          <div class="title-text-view">
            <div class="title-text" style="color: #00479d;font-size: 20px;">
              {{ item.itemName }}
            </div>
            <div class="title-text" style="color: #FF0000;font-weight: bold">
              {{handleTypeByValue(item.orderStatus,orderStatusOptions)}}
            </div>
            <div class="title-text">
              支付方式：{{handleTypeByValue(item.payType,payTypeOptions)}}
            </div>
            <div class="title-text">
              {{handleTypeByValue(item.orderType,orderTypeOptions)}}
            </div>
            <div class="title-text" style="color: #304156;font-size: 20px;">
              订单编号：{{ item.outTradeNo }}
            </div>
            <div class="title-text" style="color: #304156;font-size: 20px;">
              {{ item.itemTitle }}
            </div>
            <div class="bottom-btn-view">
              <el-link type="info" :underline="false" style="font-size: 20px;margin: 20px 20px">
                <div class="bottom-btn-div" v-if="item.orderStatus != 'FINISH'">
                  <div class="bottom-btn-text" @click="setOrderStatus(item)">
                    确认收货
                  </div>
                </div>
              </el-link>
              <el-link type="info" :underline="false" style="font-size: 20px;margin: 20px 20px">
                <div class="buy-again-div" @click="toSalesItemData(item)">
                  <div class="bottom-btn-text">
                    再次购买
                  </div>
                </div>
              </el-link>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="pagination-view">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :background="true"
          :current-page="paginationData.currentPage"
          :page-sizes="[5, 10, 60, 80, 100, 200, 300]"
          :page-size="paginationData.itemsPerPage"
          layout="total, sizes, prev, pager, next, jumper"
          :total="paginationData.totalCount">
      </el-pagination>
    </div>
  </div>
</template>
<script>
import {mapGetters} from "vuex";
import moment from 'moment';
import Api from "@/services";
import BasketView from "@/views/biz/basket/BasketView";

export default {
  name: "OrderListView",
  components: {
    BasketView:BasketView
  },
  data() {
    return {
      //-----------------
      orderStatusOptions:this.$bizConstants.orderStatusOptions,
      payTypeOptions:this.$bizConstants.payTypeOptions,
      orderTypeOptions:this.$bizConstants.orderTypeOptions,
      //-----------------
      orderDataList: [],
      previewImageUrl: '',
      previewVisible: false,
      currentTime: '',
      submitData: {},
      userData:{},
      queryParams: {
        keyword: '',
      },
      paginationData: {
        itemsPerPage: 10,
        totalCount: 0,
        currentPage: 1
      },
    };
  },
  computed: {},
  methods: {
    //修改订单状态（确认收货）
    async setOrderStatus(item) {
      const loading = this.$loading({
        lock: true,
        text: "正在提交...",
      });
      let params = {
        ...item,
        orderStatus:'FINISH'
      }
      await Api.setOrderStatus({
        ...params
      }).then(async (res) => {
        if (!res.success) {
          loading.close();
          return;
        }
        if (this.$isNull(res)) {
          loading.close();
          return;
        }
        loading.close();
        this.$message({
          message: "操作成功!",
          type: "success",
        });
        await this.queryOrderListByPage();
      });
    },
    //跳转商品信息界面
    toSalesItemData(item) {
      this.$router.push({
        path: '/salesItemDataView',
        query: {
          salesItemId: item.salesItemId,
        }
      })
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.paginationData.itemsPerPage = val
      this.queryOrderListByPage();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.paginationData.currentPage = val
      this.queryOrderListByPage();
    },
    clearAll() {
      console.log('触发清除所有')
      this.submitData = {}
    },
    //查询订单信息
    async queryOrderListByPage() {
      let params = {
        authAppUserId:this.userData.authAppUserId,
        ...this.queryParams,
        itemsPerPage: this.paginationData.itemsPerPage,
        currentPage: this.paginationData.currentPage,
      }
      await Api.queryOrderListByPage({
        ...params
      }).then(async (res) => {
        if (!res.success) {
          return;
        }
        if (this.$isNull(res)) {
          return;
        }
        let bizData = res.data;
        if (this.$isNull(bizData)) {
          return;
        }
        this.paginationData.totalCount = bizData.totalCount
        this.paginationData.itemsPerPage = bizData.itemsPerPage
        this.paginationData.currentPage = bizData.currentPage
        let dataList = bizData.data;
        //console.log('dataList:' + JSON.stringify(dataList))
        if (!dataList || dataList.length === 0) {
          return;
        }
        this.orderDataList = new Array();
        await dataList.map(async (item) => {
          //let mainImg = await this.handleImageUrl(item.mainImg);
          this.orderDataList.push(item);
        });
        //console.log('orderDataList:' + JSON.stringify(this.orderDataList))
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
    async init() {
      let userData = await this.$bizConstants.userMeta();
      this.userData = {...userData};
      console.log('当前用户信息:' + JSON.stringify(this.userData))
      this.currentTime = moment().format("YYYY-MM-DD HH:mm:ss");
    },
    itemActive($event) {
      console.log($event)
      $event.currentTarget.className = 'order-item-view-active'
    },
    removeActive($event) {
      console.log($event)
      $event.currentTarget.className = 'order-item-view'
    },
  },
  created() {
    this.queryOrderListByPage();
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
      margin: 15px 10px;
      font-weight: bold;
    }
  }
  .order-data-view{
    width: 90%;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    .order-item-view-active {
      @extend .order-item-view;
      box-shadow: rgba(9, 30, 66, 0.25) 0px 4px 8px -2px, rgba(9, 30, 66, 0.08) 0px 0px 0px 1px;
    }
    .order-item-view {
      width: 90%;
      margin: 10px 0px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      border-radius: 5px;
      cursor: pointer;//悬浮时变手指
      border: solid 1px #e6e6e6;
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
            width: 100px;
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
          .bottom-btn-view {
            margin-left: 10px;
            font-weight: bold;
            width: 100%;
            //border: 1px solid #304156;
            display: flex;
            flex-direction: row;
            justify-content: flex-start;
            align-items: center;
            .bottom-btn-div {
              display: flex;
              flex-direction: column;
              justify-content: center;
              align-items: center;
              background-color: #FF0000;
              border-radius: 10px;
              .bottom-btn-text {
                margin: 5px 10px;
                color: #FFFFFF;
                font-weight: bold;
              }
            }
            .buy-again-div {
              @extend .bottom-btn-div;
              background-color: #409EFF;
              color: #FFFFFF;
            }
          }
        }
      }
    }
  }
  .pagination-view {
    margin-top: 20px;
    margin-bottom: 20px;
    margin-left: 20px;
    //border: 2 px solid #15de0f;
  }
}
</style>
