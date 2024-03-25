<template>
  <div class="page-view">
    <MainPageHeader ref="MainPageHeaderRef"></MainPageHeader>
    <div class="search-view">
      <el-input
          type="textarea"
          :rows="2"
          placeholder="请输入内容" v-model="queryParams.keyword" class="search-input">
      </el-input>
      <div class="search-btn">
        <el-button type="primary" icon="el-icon-search" @click="handleSearchData">搜索</el-button>
      </div>
    </div>
    <el-divider content-position="center" v-if="feedbackItemList.length > 0">投诉建议信息</el-divider>
    <div class="main-data-view" v-if="feedbackItemList.length > 0">
      <div class="main-item">
        <div class="main-item-view"
             @click="toNewsData(item)"
             @mouseover="itemActive($event)" @mouseout="removeActive($event)"
             v-for="(item,index) in feedbackItemList" :key="index">
          <div class="main-item-top">
            <div class="main-item-text" style="font-weight: bold;font-size: 20px;">
              {{ item.dataTitle }}
            </div>
          </div>
          <div class="item-text-view">
            <div class="item-text" style="font-size: 10px;">
              新闻来源:{{ item.newsSource }}
            </div>
            <div class="item-text" style="font-size: 10px;">
              备注:{{ item.remarkData }}
            </div>
          </div>
          <div class="main-item-bottom" style="font-weight: bold;">
            <div class="bottom-text-view">
              发布人署名:{{ item.publisherSign }}
            </div>
            <div class="bottom-text-view">
              发布时间:{{ item.createTime }}
            </div>
          </div>
          <div class="upvote-view" @click="userUpvote(item)" v-if="false">
            <div class="upvote-num">
              点赞数:{{ item.upvoteNum }}
            </div>
            <el-image class="upvote-img" v-if="!item.upvoteStatus" fit="fill" src="/static/images/no-upvote.png"
                      alt=""/>
            <el-image class="upvote-img" v-if="item.upvoteStatus" fit="fill" src="/static/images/upvote-on.png" alt=""/>
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
    <el-dialog append-to-body :visible.sync="previewVisible" title="图片预览">
      <img width="100%" :src="previewImageUrl" alt=""/>
    </el-dialog>
  </div>
</template>
<script>
import {mapGetters} from "vuex";
import moment from 'moment';
import Api from "@/services";
import MainPageHeader from "@/views/mainPage/MainPageHeader";

export default {
  components: {
    MainPageHeader:MainPageHeader,
  },
  data() {
    return {
      //-------------------------
      noticeList: [],
      //-------------------------
      previewImageUrl: '',
      previewVisible: false,
      editorContent: '',
      mainSwiperList: [],
      dataList: [],
      mainDataTypeList: [],
      feedbackItemList: [],
      currentTime: '',
      mainDataActiveIndex: 0,
      queryParams: {
        keyword: '',
        newsTypeId: ''
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
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.paginationData.itemsPerPage = val
      this.queryFeedbackData();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.paginationData.currentPage = val
      this.queryFeedbackData();
    },
    //跳转到公告详情界面
    toNoticeDataView(item) {
      this.$router.push({
        path: '/noticeDataView',
        query: {
          noticeDataId: item.noticeDataId,
        }
      })
    },
    //跳转到其他信息界面
    toNewsData(item) {
      this.$router.push({
        path: '/newsDataView',
        query: {
          newsDataId: item.newsDataId,
        }
      })
    },
    //分页查询其他信息集合
    async queryFeedbackData() {
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      let params = {
        ...this.queryParams,
        itemsPerPage: this.paginationData.itemsPerPage,
        currentPage: this.paginationData.currentPage,
      }
      await Api.queryFeedbackData({
        ...params
      }).then(async (res) => {
        this.feedbackItemList = new Array();
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
          //console.log('data:'+JSON.stringify(data))
          this.paginationData.totalCount = data.totalCount
          this.paginationData.itemsPerPage = data.itemsPerPage
          this.paginationData.currentPage = data.currentPage
          let dataList = data.data;
          //console.log('dataList:'+JSON.stringify(dataList));
          if (!dataList || Object.keys(dataList).length === 0) {
            this.feedbackItemList = new Array();
            loading.close();
            return;
          }
          await dataList.map(async (item) => {
            /*let imgList = item.imgList;
            if (!imgList || imgList.length === 0) {
              return;
            }
            let firstItem = imgList.at(0);
            let mainImg = await this.handleImageUrl(firstItem);
            item.mainImg = mainImg;*/
            this.feedbackItemList.push(item);
          })
          console.log('feedbackItemList:'+JSON.stringify(this.feedbackItemList));
          loading.close();
        }
      }).catch((error) => {
        console.log(error);
        this.$message.error(error.message || error.msg || "服务器异常");
        loading.close();
      });
    },
    //跳转到详情数据
    toMainData(item) {
      this.$router.push({
        path: '/mainData',
        query: {
          newsDataId: item.newsDataId,
        }
      })
    },
    //处理搜索
    async handleSearchData() {
      await this.queryFeedbackData();
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
      await this.queryFeedbackData();
      this.currentTime = moment().format("YYYY-MM-DD HH:mm:ss");
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
.page-view {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  align-content: center;
  position: relative;
  .page-top-view {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    //border: 1px solid #dbdbdb;
    background-color: #304156;;
    .left-view {
      margin: 0px 10px;
      font-weight: bold;
      color: #FFFFFF;
    }
    .right-view {
      margin: 0px 10px;
      font-weight: bold;
    }
  }
  .platform-view {
    width: 100%;
    margin: 10px 0px;
    .platform-item-view {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      border-radius: 10px;
      .image-view {
        border-radius: 10px;
        width: 80%;
        height: 300px;
      }
    }
  }
  .search-view {
    width: 90%;
    margin-top: 10px;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    .search-input {
      width: 90%;
    }
    .search-btn {
      margin: 0px 10px;
    }
  }
  .main-data-view {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 90%;
    .main-item {
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: center;
      flex: 1;
      width: 100%;
      background-color: #FFFFFF;
      border: 1px solid #dbdbdb;
      //border: 2px solid #0ae54f;
      border-radius: 10px;
      flex-wrap: wrap;
      .main-item-view-active {
        @extend .main-item-view;
        box-shadow: rgba(9, 30, 66, 0.25) 0px 4px 8px -2px, rgba(9, 30, 66, 0.08) 0px 0px 0px 1px;
      }
      .main-item-view {
        width: 90%;
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: flex-start;
        margin: 10px 0;
        cursor: pointer;//悬浮时变手指
        //border: 2px solid #1482F0;
        border-bottom: 2px solid #dbdbdb;
        .main-item-top {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          margin: 5px 10px;
          //border: 2px solid #e50a4c;
          .main-item-text {
            border-radius: 10px;
          }
          .main-item-img {
            margin-top: 10px;
            width: 200px;
            height: 200px;
            border: solid 1px #e6e6e6;
            border-radius: 10px;
          }
        }
        .item-text-view {
          //border: 2px solid #e50a4c;
          word-break: break-all;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          align-items: flex-start;
          margin: 0px 10px;
          .item-text {
            width: 100%;
          }
        }
        .main-item-bottom {
          width: 80%;
          display: flex;
          flex-direction: row;
          justify-content: space-between;
          align-items: flex-start;
          word-break: break-all;
          margin: 10px 10px;
          .bottom-text-view{

          }
        }
        .upvote-view {
          display: flex;
          flex-direction: row;
          justify-content: space-between;
          align-items: center;
          width: 200px;
          .upvote-num {
            font-size: 15px;
          }
          .upvote-img {
            width: 30px;
            height: 30px;
          }
        }
      }
    }
  }
  .main-data-type {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    width: 90%;
    //border: 1px solid #dbdbdb;
    background-color: #FFFFFF;
    border: 1px solid #dbdbdb;
    border-radius: 10px;
    margin-top: 20px;
    .main-item-type {
      font-weight: bold;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      background-color: #FFFFFF;
      .spot-item-text {
        margin: 10px 20px;
      }
    }
    .main-type-active {
      @extend .main-item-type;
      background-color: #087fe7;
      color: #FFFFFF;
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
