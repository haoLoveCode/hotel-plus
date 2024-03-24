<template>
  <div class="main-header-view">
    <div class="header-left-view">
      <el-carousel height="400px" :initial-index="0">
        <el-carousel-item v-for="(item,index) in mainSwiperList" :key="index">
          <div class="swiper-item-view">
            <img :src="item.url"
                 alt=""
                 width="auto"
                 height="100%"
                 class="swiper-image"
                 style="border-radius: 10px;"/>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
    <div class="header-right-view">
      <div class="vertical-data-item">
        <div class="vertical-data-top">
          <div class="data-top-text" >
            系统公告信息
          </div>
        </div>
      </div>
      <div class="vertical-data-item"
           @mouseover="dataSetCssActive($event)" @mouseout="dataSetCssRemove($event)"
           @click="toNoticeDataView(item)"
           v-for="(item,index) in rightList" :key="index">
        <div class="vertical-data-content">
          <div class="vertical-data-title">
              {{ item.dataTitle }}
          </div>
          <div class="vertical-data-time">
              发布于:{{ item.createTime }}
          </div>
        </div>
        <div class="vertical-data-value" v-html="item.dataValue" v-if="false">
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {mapGetters} from "vuex";
import moment from 'moment';
import Api from "@/services";

export default {
  components: {

  },
  data() {
    return {
      //-------------------------
      rightList: [],
      //-------------------------
      mainSwiperList: [],
    };
  },
  computed: {},
  methods: {
    //跳转到公告详情界面
    toNoticeDataView(item) {
      this.$router.push({
        path: '/noticeDataView',
        query: {
          noticeDataId: item.noticeDataId,
        }
      })
    },
    // 拿到图片尺寸
    getImgSize (url) {
      return new Promise((resolve, reject) => {
        let imgObj = new Image()
        imgObj.src = url
        imgObj.onload = () => {
          resolve({
            width: imgObj.width,
            height: imgObj.height
          })
        }
      })
    },
    dataSetCssActive($event) {
      console.log($event)
      $event.currentTarget.className = 'vertical-data-item-active'
    },
    dataSetCssRemove($event) {
      console.log($event)
      $event.currentTarget.className = 'vertical-data-item'
    },
    itemActive($event) {
      console.log($event)
      $event.currentTarget.className = 'main-item-view-active'
    },
    removeActive($event) {
      console.log($event)
      $event.currentTarget.className = 'main-item-view'
    },
    //查询公告信息
    async queryNoticeData() {
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      let params = {
        //查询上架的公告
        dataStatus: 1
      }
      await Api.queryNoticeData({
        ...params
      }).then(async (res) => {
        this.rightList = new Array();
        //console.log('res:'+JSON.stringify(res))
        let data = res.data
        //console.log('data:'+JSON.stringify(data))
        if (!res.success) {
          this.$message.error(res.message || res.msg || "服务器异常");
          loading.close();
          return;
        } else {
          if (!data || Object.keys(data).length === 0) {
            loading.close();
            return;
          }
          let dataList = data;
          //console.log('dataList:' + JSON.stringify(dataList));
          if (!dataList || Object.keys(dataList).length === 0) {
            this.rightList = new Array();
            loading.close();
            return;
          }
          await dataList.map(async (item) => {
            this.rightList.push(item);
          })
          //console.log('rightList:' + JSON.stringify(this.rightList));
          loading.close();
        }
      }).catch((error) => {
        console.log(error);
        this.$message.error(error.message || error.msg || "服务器异常");
        loading.close();
      });
    },
    async setMainSwiper() {
      let itemList = await this.queryAllMainSwiper();
      if (!itemList || itemList.length === 0) {
        return;
      }
      this.mainSwiperList = new Array();
      await itemList.map(async (item) => {
        let mainUrl = await this.handleImageUrl(item.mainUrl);
        let sizeObject = await this.getImgSize(mainUrl);
        console.log('sizeObject:'+JSON.stringify(sizeObject));
        let imgItem = {
          url:mainUrl,
          width:sizeObject.width,
          height:sizeObject.height
        }
        // 打印
        this.mainSwiperList.push(imgItem);
      })
    },
    async queryAllMainSwiper(item) {
      let itemList = new Array();
      await Api.queryAllMainSwiper({}).then(async (res) => {
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
        itemList = data;
      });
      return itemList;
    },
    handlePreView(url) {
      if (this.$isNull(url)) {
        return;
      }
      this.previewImageUrl = url
      this.previewVisible = true
    },
    async init() {
      console.log('首页初始化')
      await this.setMainSwiper();
      await this.queryNoticeData();
    },
  },
  created() {
    this.init();
  },
  mounted() {

  },
};
</script>

<style scoped lang="scss">
.main-header-view {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: flex-start;
  width: 100%;
  margin-top: 20px;
  .header-left-view {
    width: 50%;
    margin: 10px 5px;
    .swiper-item-view{
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      height: 100%;
      width: 100%;
      border-radius: 10px;
      .swiper-image{
        border-radius: 10px;
      }
    }
  }
  .header-right-view {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
    width: 50%;
    height: 400px;
    overflow-y: scroll;
    overflow-x: hidden;
    margin: 10px 5px;
    .vertical-data-item-active {
      @extend .vertical-data-item;
      box-shadow: rgba(9, 30, 66, 0.25) 0px 4px 8px -2px, rgba(9, 30, 66, 0.08) 0px 0px 0px 1px;
    }
    .vertical-data-item {
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: center;
      width: 90%;
      margin: 5px 10px;
      border-radius: 5px;
      cursor: pointer;//悬浮时变手指
      .vertical-data-value {
        width: 90%;
        word-break: break-all;
        overflow: hidden;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
      }
      .vertical-data-top {
        @extend .vertical-data-content;
        background-color:#1772F6;
        border-radius: 5px;
        .data-top-text {
          margin: 10px 10px;
          font-size: 20px;
          color: #FFFFFF;
          font-weight: bold;
        }
      }
      .vertical-data-content {
        width: 95%;
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: center;
        margin: 10px 10px;
        .vertical-data-title {
          word-break: break-all;
          overflow: hidden;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          font-weight: bold;
          font-size: 20px;
        }
        .vertical-data-time {
          margin: 0px 10px;
        }
      }
    }
  }
}
</style>
