<template>
  <div class="page-view" v-if="Object.keys(roomItemData).length > 0" >
    <div class="title-view">
      <div class="title-text">
        商品详情
      </div>
    </div>
    <div class="platform-view">
      <div class="item-title-view">
        <div class="title-img-view">
          <img :src="handleImageUrl(roomItemData.mainImg)"
               alt=""
               width="auto"
               height="100%"
               class="main-img-view"
               style="border-radius: 10px;"/>
        </div>
        <div class="title-text-view">
          <div class="title-text" style="color: #00479d;font-size: 30px;">
            {{ roomItemData.roomTitle }}
          </div>
          <div class="title-text" style="color: #FF0000;font-size: 25px;font-weight: bold">
            ¥: {{ roomItemData.unitPrice }}
          </div>
          <div class="title-text" style="color: #304156;font-size: 20px;">
            房间编号：{{ roomItemData.roomNo }}
          </div>
          <div class="title-text" style="color: #304156;font-size: 20px;">
            房间状态：<el-tag size="medium">{{handleTypeByValue(roomItemData.roomStatus,itemStatusOptions)}}</el-tag>
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
            <div class="basket-num-view" v-if="Object.keys(baskItemData).length > 0">
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
          <div class="buy-now-btn" @click="toSalesItemBuy(roomItemData)">
            <el-link type="info" :underline="false" style="font-size: 20px;margin: 20px 20px">
              <div class="buy-btn-div">
                <div class="buy-btn">
                  立即下单
                </div>
              </div>
            </el-link>
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
        上架时间: {{ roomItemData.createTime }}
      </div>
      <div class="item-value-view" v-html="roomItemData.itemSummary">
      </div>
    </div>
  </div>
</template>
<script>
import {mapGetters} from "vuex";
import moment from 'moment';
import Api from "@/services";

export default {
  name: "roomItemDataView",
  components: {

  },
  data() {
    return {
      itemStatusOptions: [
        {
          'text':'闲置',
          'value': 1
        },
        {
          'text':'已预订',
          'value': 2
        },
        {
          'text':'维护中',
          'value': 3
        },
        {
          'text':'已入住',
          'value': 4
        },
        {
          'text':'已退住',
          'value': 5
        },
      ],
      mainSwiperList: [],
      previewImageUrl: '',
      previewVisible: false,
      currentTime: '',
      roomItemData: {},
      submitData: {},
      commentDataList: [],
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
    //跳转到购买界面
    toSalesItemBuy(item) {
      this.$router.push({
        path: '/salesItemBuyView',
        query: {
          roomDataId: item.roomDataId,
        }
      })
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
        roomDataId:this.roomItemData.roomDataId,
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
    clearAll() {
      console.log('触发清除所有')
      this.submitData = {}
    },
    //查询单个其他信息
    async queryOneRoomData(roomDataId) {
      await Api.queryOneRoomData({
        roomDataId: roomDataId
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
        this.roomItemData = {...data}
        this.mainSwiperList = new Array();
        let imgList = data.imgList;
        await imgList.map(async (item) => {
          let mainImg = await this.handleImageUrl(item);
          this.mainSwiperList.push(mainImg);
        });
        //console.log('mainSwiperList:' + JSON.stringify(this.mainSwiperList))
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
      let roomDataId = this.roomItemData.roomDataId;
      this.queryOneRoomData(roomDataId);
    },
    async init() {
      this.currentTime = moment().format("YYYY-MM-DD HH:mm:ss");
    },
  },
  created() {
    console.log(this.$route.query.roomDataId);
    let roomDataId = this.$route.query.roomDataId;
    this.currentUserMeta();
    this.queryOneRoomData(roomDataId);
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
          margin-top: 10px;
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
</style>
