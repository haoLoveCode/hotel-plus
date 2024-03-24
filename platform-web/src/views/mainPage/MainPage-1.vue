<template>
  <div class="page-view">
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
        <div class="data-set-item"
             @click="toNoticeDataView(item)"
             v-for="(item,index) in noticeList" :key="index">
          <div class="data-set-content">
            <div class="data-set-title">
              {{ item.dataTitle }}
            </div>
            <div class="data-set-time">
              发布于:{{ item.createTime }}
            </div>
          </div>
          <div class="data-set-value" v-html="item.dataValue" v-if="false">
          </div>
        </div>
      </div>
    </div>
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
    <div class="main-data-type">
      <el-link :class="index == mainDataActiveIndex ? 'main-type-active' : 'main-item-type'"
               @click="handleMainTypeSelect(item,index)"
               v-for="(item,index)  in mainDataTypeList" :key="index">
        <div class="spot-item-text">
          {{ item.typeName }}
        </div>
      </el-link>
    </div>
    <el-divider content-position="center" v-if="dataList.length > 0">数据列表</el-divider>
    <div class="main-data-view" v-if="dataList.length > 0">
      <div class="main-item">
        <div class="main-item-view"
             @click="toMainData(item)"
             v-for="(item,index) in dataList" :key="index">
          <div class="main-item-top">
            <el-image class="main-item-img" fit="fill" :src="item.mainImg" alt=""/>
          </div>
          <div class="main-item-bottom">
            {{ item.dataName }}
          </div>
        </div>
      </div>
    </div>
    <el-divider content-position="center" v-if="dataList.length > 0">其他信息介绍</el-divider>
    <div class="main-data-view" v-if="dataList.length > 0">
      <div class="main-item">
        <div class="main-item-view"
             v-for="(item,index) in otherDataList" :key="index">
          <div class="main-item-top" @click="toOtherData(item)">
            <el-image class="main-item-img" fit="fill" :src="item.mainImg" alt=""/>
          </div>
          <div class="main-item-bottom">
            {{ item.notesTitle }}
          </div>
          <div class="upvote-view" @click="userUpvote(item)">
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
    <div class="data-set-view" v-if="false">
      <div class="data-set-item"
           @click="toNoticeDataView(item)"
           v-for="(item,index) in noticeList" :key="index">
        <div class="data-set-content">
          <div class="data-set-title">
            {{ item.dataTitle }}
          </div>
          <div class="data-set-time">
            发布于:{{ item.createTime }}
          </div>
        </div>
        <div class="data-set-value" v-html="item.dataValue">
        </div>
      </div>
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

export default {
  components: {},
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
      otherDataList: [],
      currentTime: '',
      mainDataActiveIndex: 0,
      queryParams: {
        keyword: '',
        mainDataId: ''
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
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.paginationData.itemsPerPage = val
      this.queryDataByPage();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.paginationData.currentPage = val
      this.queryDataByPage();
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
    toOtherData(item) {
      this.$router.push({
        path: '/notesData',
        query: {
          notesDataId: item.notesDataId,
        }
      })
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
        this.noticeList = new Array();
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
            this.noticeList = new Array();
            loading.close();
            return;
          }
          await dataList.map(async (item) => {
            this.noticeList.push(item);
          })
          //console.log('noticeList:' + JSON.stringify(this.noticeList));
          loading.close();
        }
      }).catch((error) => {
        console.log(error);
        this.$message.error(error.message || error.msg || "服务器异常");
        loading.close();
      });
    },
    //分页查询其他信息集合
    async queryDataByPage() {
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
      await Api.queryDataByPage({
        ...params
      }).then(async (res) => {
        this.otherDataList = new Array();
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
            this.otherDataList = new Array();
            loading.close();
            return;
          }
          await dataList.map(async (item) => {
            let imgList = item.imgList;
            if (!imgList || imgList.length === 0) {
              return;
            }
            let firstItem = imgList.at(0);
            let mainImg = await this.handleImageUrl(firstItem);
            item.mainImg = mainImg;
            this.otherDataList.push(item);
          })
          //console.log('otherDataList:'+JSON.stringify(this.otherDataList));
          loading.close();
        }
      }).catch((error) => {
        console.log(error);
        this.$message.error(error.message || error.msg || "服务器异常");
        loading.close();
      });
    },
    //跳转到攻略数据
    toMainData(item) {
      this.$router.push({
        path: '/mainData',
        query: {
          mainDataId: item.mainDataId,
        }
      })
    },
    async setMainDataType() {
      let itemList = await this.queryMainDataType();
      //console.log('itemList:' + JSON.stringify(itemList));
      if (!itemList || itemList.length === 0) {
        return;
      }
      this.mainDataTypeList = itemList;
      let firstItem = itemList.at(0);
      this.queryParams.mainDataId = firstItem.mainDataId;
      await this.queryMainDataList(
          {
            ...this.queryParams
          }
      );
    },
    //处理搜索
    async handleSearchData() {
      await this.queryMainDataList({
        ...this.queryParams
      })
    },
    //查询通用系统方案
    async queryMainDataList(param) {
      this.dataList = new Array();
      await Api.queryMainDataList({
        ...param
      }).then(async (res) => {
        if (!res.success) {
          return;
        }
        if (this.$isNull(res)) {
          return;
        }
        let dataList = res.data
        if (this.$isNull(dataList)) {
          return;
        }
        await dataList.map(async (item) => {
          let mainImg = await this.handleImageUrl(item.mainImg);
          item.mainImg = mainImg;
          this.dataList.push(item);
        })
        //console.log('dataList:' + JSON.stringify(this.dataList))
      });
    },
    async queryMainDataType() {
      let itemList = new Array();
      await Api.queryMainDataType({}).then(async (res) => {
        if (!res.success) {
          return;
        }
        if (this.$isNull(res)) {
          return;
        }
        let data = res.data
        //console.log('data:' + JSON.stringify(data))
        if (this.$isNull(data)) {
          return;
        }
        itemList = data;
      });
      return itemList;
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
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    async handleMainTypeSelect(item, index) {
      //console.log(item);
      this.mainDataActiveIndex = index;
      this.queryParams.mainDataId = item.mainDataId;
      //查询通用系统方案信息
      await this.queryMainDataList(
          {
            ...this.queryParams
          }
      );
    },
    //处理点赞
    async userUpvote(item) {
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      item.upvoteStatus = !item.upvoteStatus;
      await Api.userUpvote({
        ...item
      }).then(async (res) => {
        console.log('点赞结果:' + JSON.stringify(res));
        loading.close();
        this.$message.success("处理成功");
        await this.queryDataByPage();
      }).catch((error) => {
        console.log(error);
        this.$message.error(error.message || error.msg || "服务器异常");
        loading.close();
      });
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
      //await this.setMainDataType();
      //await this.queryDataByPage();
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

      .data-set-item {
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: center;
        width: 100%;
        border-bottom: 2px solid #dedede;
        margin: 5px 10px;
        border-radius: 5px;

        .data-set-value {
          width: 90%;
          word-break: break-all;
          overflow: hidden;
          display: -webkit-box;
          -webkit-line-clamp: 3;
          -webkit-box-orient: vertical;
        }

        .data-set-content {
          width: 90%;
          display: flex;
          flex-direction: row;
          justify-content: flex-start;
          align-items: center;
          margin: 10px 10px;

          .data-set-title {
            word-break: break-all;
            overflow: hidden;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            font-weight: bold;
            font-size: 20px;
          }

          .data-set-time {
            margin: 0px 10px;
          }
        }
      }
    }
  }

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
      flex-direction: row;
      // justify-content: space-between;
      align-items: flex-start;
      flex: 1;
      width: 100%;
      background-color: #FFFFFF;
      border: 1px solid #dbdbdb;
      //border: 2px solid #0ae54f;
      border-radius: 10px;
      flex-wrap: wrap;

      .main-item-view {
        width: 16.6%;
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: center;
        margin: 10px 0;
        // border: 2px solid #e50a4c;
        .main-item-top {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          // border: 2px solid #e50a4c;
          .main-item-img {
            width: 200px;
            height: 200px;
          }
        }

        .main-item-bottom {
          //border: 2px solid #0aa3e5;
          width: 200px;
          margin-top: 10px;
          word-break: break-all;
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
