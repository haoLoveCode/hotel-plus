<template>
  <div class="page-view" v-if="noticeData && Object.keys(noticeData).length > 0">
    <div class="title-view">
      <div class="title-text">
        公告详情信息
      </div>
    </div>
    <div class="platform-view">
      <div class="platform-item-view">
        <div class="item-title-view">
          {{noticeData.dataTitle}}
        </div>
        <div class="item-time-view">
          发布时间: {{noticeData.createTime}}
        </div>
        <div class="item-value-view" v-html="noticeData.dataValue">
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
  name:"noticeData",
  components: {

  },
  data() {
    return {
      mainSwiperList:[],
      previewImageUrl: '',
      previewVisible: false,
      currentTime: '',
      noticeData:{},
      submitData:{

      },
      commentDataList:[],
      paginationData: {
        itemsPerPage: 10,
        totalCount: 0,
        currentPage: 1
      },
    };
  },
  computed: {

  },
  methods: {
    clearAll() {
      console.log('触发清除所有')
      this.submitData = {

      }
    },
    //查询单个其他信息
    async queryOneNoticeData(noticeDataId) {
      await Api.queryOneNoticeData({
        noticeDataId:noticeDataId
      }).then(async (res) => {
        if (!res.success) {
          return;
        }
        if (this.$isNull(res)) {
          return;
        }
        let data = res.data
        // console.log('data:' + JSON.stringify(data))
        if (this.$isNull(data)) {
          return;
        }
        this.noticeData = {...data}
        /*this.mainSwiperList = new Array();
        await imgList.map(async (item) => {
          let mainImg = await this.handleImageUrl(item);
          this.mainSwiperList.push(mainImg);
        });
        console.log('imgList:' + JSON.stringify(this.mainSwiperList))*/
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
      this.currentTime = moment().format("YYYY-MM-DD HH:mm:ss");
    },
  },
  created() {
    console.log(this.$route.query.noticeDataId);
    let noticeDataId = this.$route.query.noticeDataId;
    this.queryOneNoticeData(noticeDataId);
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
  .title-view{
    width: 90%;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-content: center;
    font-size: 20px;
    margin-top: 10px;
    .title-text{
      margin: 10px 10px;
      font-weight: bold;
    }
  }
  .platform-view {
    width: 90%;
    margin: 10px 0px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    box-shadow: 0 0 8px #dedede;
    .platform-item-view {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      border-radius: 10px;
      margin-top: 20px;
      .item-title-view {
        border-radius: 10px;
        width: 80%;
        color: #00479d;
        font-size: 30px;
        border-bottom: 2px solid #dedede;
      }
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
}
</style>
