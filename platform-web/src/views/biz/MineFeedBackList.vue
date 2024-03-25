<template>
  <div class="page-view">
    <div class="submit-feedback" @click="submitFeedBack">
      <div class="submit-feedback-view">
        <div class="submit-feedback-text">
          提交反馈信息
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
    <el-divider content-position="center">投诉建议信息</el-divider>
    <div class="feedback-data-view" v-if="feedbackItemList.length > 0">
      <div class="feedback-view" v-for="(item,index) in feedbackItemList" :key="index">
        <div class="feedback-user-view">
          <div class="avatar-view">
            <img :src="handleImageUrl(item.mainImg)"
                 alt=""
                 width="auto"
                 height="100%"
                 class="avatar-img-view"
                 style="border-radius: 10px;"/>
          </div>
          <div class="user-name-view">
            反馈信息用户: {{item.realName}}
          </div>
        </div>
        <div class="feedback-text" style="font-weight: bold">
          反馈信息标题: {{item.dataTitle}}
        </div>
        <div class="feedback-text" @click="showRichText(item.dataValue)" style="margin-bottom: 10px">
          {{item.dataValue}}
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
    <!--富文本查看弹窗-->
    <el-dialog
        append-to-body
        title="反馈信息内容"
        :visible.sync="richTextVisible"
        width="50%">
      <div v-html="richText"></div>
    </el-dialog>
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
      noticeList: [],
      richTextVisible: false,
      richText: ``,
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
        submitterId: ''
      },
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
    //提交投诉和建议
    submitFeedBack() {
      this.$router.push({path: "/submitFeedBack"});
    },
    async showRichText(richText) {
      console.log(richText)
      this.richText = richText
      this.richTextVisible = true
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
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.paginationData.itemsPerPage = val
      this.queryFeedbackDataByPage();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.paginationData.currentPage = val
      this.queryFeedbackDataByPage();
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
    async queryFeedbackDataByPage() {
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
      await Api.queryFeedbackDataByPage({
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
            item.mainImg = item.avatarUrl;
            //item.mainImg = await this.handleImageUrl(item.avatarUrl);
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
      await this.queryFeedbackDataByPage();
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
      let userData = await this.$bizConstants.userMeta();
      this.userData = {...userData};
      console.log('当前用户信息:' + JSON.stringify(this.userData))
      this.queryParams.submitterId = userData.authAppUserId;
      await this.queryFeedbackDataByPage();
      this.currentTime = moment().format("YYYY-MM-DD HH:mm:ss");
    },
  },
  async created() {
    await this.init();
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
  .submit-feedback{
    position: fixed; //让元素跟随滚动
    z-index: 998;
    right:0;
    top: 50%;
    .submit-feedback-view{
      width: 150px;
      height: 50px;
      background-color: #FF0000;
      color: #FFFFFF;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      border-radius: 10px;
      .submit-feedback-text{
        font-size: 20px;
        font-weight: bold;
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
  .feedback-data-view{
    width: 90%;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    .feedback-view{
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-content: center;
      box-shadow: 0 0 1px 1px #409eff;
      margin-top: 10px;
      .feedback-user-view{
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: center;
        align-content: center;
        margin-left: 10px;
        .avatar-view{
          border-radius: 10px;
          margin: 10px 0px;
          width: 40px;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          .avatar-img-view{
            margin: 5px 0px;
            width: 20px;
          }
        }
        .user-name-view{

        }
      }
      .feedback-text{
        margin-left: 20px;
        word-break: break-all;
        overflow: hidden;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        font-size: 15px;
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
