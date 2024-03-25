<template>
  <div class="page-view" v-if="Object.keys(roomItemData).length > 0" >
    <div class="title-view">
      <div class="title-text">
        房间详情
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
            楼层/床位数：{{ roomItemData.roomFloor }}楼，{{ roomItemData.bedNum }}个床位
          </div>
          <div class="title-text" style="color: #304156;font-size: 20px;">
            房间编号：{{ roomItemData.roomNo }}
          </div>
          <div class="title-text" style="color: #304156;font-size: 20px;">
            房间状态：<el-tag size="medium" type="danger">{{handleTypeByValue(roomItemData.roomStatus,itemStatusOptions)}}</el-tag>
          </div>
          <div class="basket-view">
            <div class="basket-btn-view">
            <!--<el-link type="info" :underline="false" @click="handleBasketBiz(1)">
                <img src="/static/icons/add-icon.png"
                     alt=""
                     width="auto"
                     height="20px"
                     style="border-radius: 10px;"/>
              </el-link>-->
              <el-date-picker
                  v-model="submitData.checkInBegin"
                  @change="handleTimeChange"
                  type="datetime"
                  placeholder="请填写-入住开始时间"
                  align="right"
                  :value-format="timeFormat"
                  :picker-options="pickerOptions"
              >
              </el-date-picker>
            </div>
            <div class="basket-num-view" v-if="Object.keys(submitData).length > 0">
              住 {{submitData.itemNum}} 天
            </div>
            <div class="basket-num-view" v-else>
              0
            </div>
            <div class="basket-btn-view">
              <!--<el-link type="info" :underline="false" @click="handleBasketBiz(2)">
                <img src="/static/icons/minus-icon.png"
                     alt=""
                     width="auto"
                     height="20px"
                     style="border-radius: 10px;"/>
              </el-link>-->
              <el-date-picker
                  v-model="submitData.checkInEnd"
                  @change="handleTimeChange"
                  type="datetime"
                  placeholder="请填写-入住结束时间"
                  align="right"
                  :value-format="timeFormat"
                  :picker-options="pickerOptions"
              >
              </el-date-picker>
            </div>
          </div>
          <div class="buy-now-btn" @click="roomOrdering(roomItemData)">
            <el-link type="info" :underline="false" style="font-size: 20px;margin: 20px 20px">
              <div class="buy-btn-div">
                <div class="buy-btn">
                  下单入住
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
    <el-divider content-position="center">房间介绍</el-divider>
    <div class="platform-data-view">
      <div class="item-time-view">
        上架时间: {{ roomItemData.createTime }}
      </div>
      <div class="item-value-view" v-html="roomItemData.briefData">
      </div>
    </div>
    <el-dialog
        append-to-body
        title="确认下单信息"
        :center="true"
        @close="orderingCancel"
        width="50%"
        :visible.sync="orderingShow">
      <div class="ordering-view">
        <div class="ordering-title">
          {{ roomItemData.roomTitle }}
        </div>
        <div class="ordering-text-view">
          <div class="ordering-text-title">
            预定住宿天数:
          </div>
          <div class="ordering-text-value" v-if="submitData.itemNum && submitData.itemNum > 0">
            {{submitData.itemNum}}天，({{submitData.checkInBegin}}-{{submitData.checkInEnd}})
          </div>
        </div>
        <div class="ordering-text-view">
          <div class="ordering-text-title">
            总价:
          </div>
          <div class="ordering-text-value">
            {{submitData.itemNum * roomItemData.unitPrice}}
          </div>
        </div>
        <div class="ordering-remark">
          <el-input
              type="textarea"
              :rows="3"
              v-model="submitData.remark"
              placeholder="请填写-下单备注"
              maxlength="30"
              show-word-limit>
          </el-input>
        </div>
        <div class="ordering-btn-view" @click="handleOrdering">
          <div class="ordering-btn-text">
            确认下单
          </div>
        </div>
      </div>
    </el-dialog>
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
      //-----------------
      pickerOptions:this.$bizConstants.pickerOptions,
      timeFormat:'yyyy-MM-dd HH:mm:ss', //时间格式
      //-----------------
      //下单弹窗
      orderingShow:false,
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
      submitData: {
        checkInBegin: '',
        checkInEnd: '',
        itemNum:0,
        remark:'',
        totalAmount: 0,
        roomDataId: '',
        subscriberId: '',
      },
      commentDataList: [],
      userData:{},
      paginationData: {
        itemsPerPage: 10,
        totalCount: 0,
        currentPage: 1
      },
    };
  },
  computed: {},
  methods: {
    //时间选择发生变化
    async handleTimeChange(value){
      console.log('checkInBeginChange:'+value);
      let checkInBegin = this.submitData.checkInBegin;
      let checkInEnd = this.submitData.checkInEnd;
      if(!checkInEnd || !checkInEnd){
          return;
      }
      console.log('checkInBegin:'+checkInBegin);
      console.log('checkInEnd:'+checkInEnd);
      let checkInBeginMoment = moment(checkInBegin);
      console.log(checkInBeginMoment);
      let checkInEndMoment = moment(checkInEnd);
      if(checkInEndMoment.isBefore(checkInBeginMoment)){
        this.$message.error('退房时间不可在入住时间之后');
        return;
      }
      let diffDays = checkInEndMoment.diff(checkInBeginMoment, 'days')
      console.log('diffDays:'+diffDays);
      if(diffDays && diffDays >0){
        this.submitData.itemNum = diffDays;
        this.submitData.totalAmount = diffDays * this.roomItemData.unitPrice
      }else{
        this.$message.error('住宿时间必须大于1天');
        return;
      }
    },
    //取消下单
    async orderingCancel(){
      this.orderingShow = false;
    },
    //跳转到购买界面
    roomOrdering(item) {
      let checkInBegin = this.submitData.checkInBegin;
      let checkInEnd = this.submitData.checkInEnd;
      if(!checkInEnd || !checkInEnd){
        this.$notify({
          title: '提示',
          dangerouslyUseHTMLString: true,
          message: '<div style="color:#FF0000">请选择入住开始和结束时间</div>',
          position: 'bottom-left',
          offset: 300
        });
        return;
      }
      console.log('checkInBegin:'+checkInBegin);
      console.log('checkInEnd:'+checkInEnd);
      let checkInBeginMoment = moment(checkInBegin);
      console.log(checkInBeginMoment);
      let checkInEndMoment = moment(checkInEnd);
      if(checkInEndMoment.isBefore(checkInBeginMoment)){
        this.$notify({
          title: '提示',
          dangerouslyUseHTMLString: true,
          message: '<div style="color:#FF0000">退房时间不可在入住时间之后</div>',
          position: 'bottom-left',
          offset: 300
        });
        return;
      }
      let itemNum = this.submitData.itemNum;
      if(itemNum <= 0){
        this.$notify({
          title: '提示',
          dangerouslyUseHTMLString: true,
          message: '<div style="color:#FF0000">住宿时间必须大于0</div>',
          position: 'bottom-left',
          offset: 300
        });
        return;
      }
     this.orderingShow = true;
    },
    //处理下单
    async handleOrdering(){
      const loading = this.$loading({
        lock: true,
        text: "正在提交...",
      });
      let params = {
        ...this.submitData,
        roomDataId:this.roomItemData.roomDataId,
        subscriberId:this.userData.authAppUserId
      }
      try {
        Api.addRoomBookingItem(params).then(async (res) => {
          if (res.success) {
            this.$message({
              message: "预定成功",
              type: "success",
            });
            let data = res.data;
            console.log('预定结果:' + JSON.stringify(data));
            this.clearAll();
          } else {
            this.$message.error({
              message: '预定失败'
            });
            this.clearAll();
          }
          await this.queryOneRoomData(this.roomItemData.roomDataId);
        });
        loading.close();
      } catch (error) {
        this.clearAll()
        loading.close();
        this.$message.error({
          message: error.message || error.msg || "服务器异常"
        });
        await this.queryOneRoomData(this.roomItemData.roomDataId);
      }
    },
    //处理新增和减少住宿天
    async handleBasketBiz(bizType){
      if(bizType === 1){
        this.submitData.itemNum += 1;
      }
      if(bizType === 2){
        if(this.submitData.itemNum <= 1){
          this.submitData.itemNum = 1;
        }else{
          this.submitData.itemNum -= 1;
        }
      }
    },
    clearAll() {
      console.log('触发清除所有')
      this.submitData = {
        checkInBegin: '',
        checkInEnd: '',
        itemNum:0,
        remark:'',
        totalAmount: 0,
        roomDataId: '',
        subscriberId: '',
      }
      this.orderingShow = false;
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
    async init() {
      let data = await this.$bizConstants.userMeta();
      this.userData = {...data};
      console.log('当前用户信息:' + JSON.stringify(this.userData));
      this.submitData.subscriberId = data.authAppUserId;
      this.currentTime = moment().format("YYYY-MM-DD HH:mm:ss");
    },
  },
  async created() {
    console.log(this.$route.query.roomDataId);
    let roomDataId = this.$route.query.roomDataId;
    this.submitData.roomDataId = roomDataId;
    await this.queryOneRoomData(roomDataId);
    await this.init();
  },
  mounted() {

  },
};
</script>

<style scoped lang="scss">
.ordering-view{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border-radius: 10px;
  //margin-top: 20px;
  width: 100%;
  border: 1px solid #909399;
  border-radius: 5px;
  .ordering-title{
    width: 100%;
    font-weight: bold;
    font-size: 20px;
    margin: 10px 0px;
    margin-left: 20px;
  }
  .ordering-remark{
    width: 90%;
  }
  .ordering-btn-view{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background-color: #FF0000;
    border-radius: 10px;
    width: 30%;
    cursor: pointer;//悬浮时变手指
    margin: 10px 0px;
    .ordering-btn-text{
      margin: 10px 10px;
      color: #FFFFFF;
      font-weight: bold;
      color: #FFFFFF;
    }
  }
  .ordering-text-view{
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    margin: 5px 0px;
    margin-left: 20px;
    width: 100%;
    font-size: 20px;
    .ordering-text-title{

    }
    .ordering-text-value{
      margin: 0px 20px;
    }
  }
}
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
