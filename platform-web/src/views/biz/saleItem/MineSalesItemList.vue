<template>
  <div class="page-view">
    <div class="title-view">
      <div class="title-text">
        商品列表
      </div>
    </div>
    <div class="btn-flow-view" @click="addSalesItem">
      <div class="btn-flow-data">
        <div class="flow-text-view">
          发布商品
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
    <div class="sales-data-view" v-if="salesItemList && Object.keys(salesItemList).length > 0">
      <div class="sales-item-view"
           @mouseover="itemActive($event)" @mouseout="removeActive($event)"
           v-for="(item,index) in salesItemList" :key="index">
        <div class="item-title-view">
          <div class="title-img-view" @click="toSalesItemData(item)">
            <img :src="handleImageUrl(item.mainImg)"
                 alt=""
                 width="auto"
                 height="100%"
                 class="main-img-view"
                 style="border-radius: 10px;"/>
          </div>
          <div class="title-text-view">
            <div class="title-text">
              {{ item.itemName }}
            </div>
            <div class="title-text" style="font-weight: bold">
              商品状态: {{handleTypeByValue(item.itemStatus,yesOrNoOptions)}}
            </div>
            <div class="title-text" style="color: #304156;">
              {{ item.itemTitle }}
            </div>
            <div class="bottom-btn-view">
              <el-link type="info" :underline="false" style="font-size: 15px;">
                <div class="buy-again-div">
                  <div class="buy-again-text" @click="toSalesItemData(item)">
                    再次购买
                  </div>
                </div>
              </el-link>
              <el-link type="info" :underline="false" style="font-size: 15px;">
                <div class="bottom-btn-div" @click="editSalesItem(item)">
                  <div class="bottom-btn-text">
                    编辑商品
                  </div>
                </div>
              </el-link>
              <el-link type="info" :underline="false" style="font-size: 15px;">
                <div class="bottom-btn-div" @click="copySalesItem(item)">
                  <div class="bottom-btn-text">
                    复制商品
                  </div>
                </div>
              </el-link>
              <el-link type="info" :underline="false" style="font-size: 15px;">
                <div class="bottom-btn-div" @click="deleteOneSalesItem(item)">
                  <div class="bottom-btn-text">
                    删除商品
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
    <!--商品新增-->
    <SalesItemAdd
        ref="salesItemAddRef"
        @handleSuccess="querySalesItemByPage" />
    <!--商品编辑-->
    <SalesItemEdit
        ref="salesItemEditRef"
        @handleSuccess="querySalesItemByPage" />
    <!--商品新复制-->
    <SalesItemCopy
        ref="salesItemCopyRef"
        @handleSuccess="querySalesItemByPage" />
  </div>
</template>
<script>
import {mapGetters} from "vuex";
import moment from 'moment';
import Api from "@/services";
import BasketView from "@/views/biz/basket/BasketView";
import SalesItemAdd from "@/views/biz/saleItem/SalesItemAdd";
import SalesItemEdit from "@/views/biz/saleItem/SalesItemEdit";
import SalesItemCopy from "@/views/biz/saleItem/SalesItemCopy";

export default {
  name: "MineSalesItemList",
  components: {
    BasketView:BasketView,
    SalesItemAdd:SalesItemAdd,
    SalesItemEdit:SalesItemEdit,
    SalesItemCopy:SalesItemCopy,
  },
  data() {
    return {
      //-----------------
      yesOrNoOptions:[
        {
          'text':'上架',
          'value': 1
        },
        {
          'text':'下架',
          'value': 2
        },
      ],
      //-----------------
      salesItemList: [],
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
    //处理搜索
    async handleSearchData() {
      await this.querySalesItemByPage()
    },
    //单个删除
    async deleteOneSalesItem(item){
      const loading = this.$loading({
        lock: true,
        text: "正在提交...",
      });
      if(!item){
        this.$message.error('请选择收货地址');
        loading.close();
        return;
      }
      let mainIdList = new Array();
      mainIdList.push(item.salesItemId);
      try {
        Api.batchDeleteSalesItem({
          mainIdList:mainIdList
        }).then(async (res) => {
          await this.querySalesItemByPage();
        });
        loading.close();
      } catch (error) {
        this.clearAll()
        loading.close();
        this.$message.error(error.message || error.msg || "服务器异常");
      }
    },
    //新增商品
    addSalesItem(){
      console.log('addSalesItem:')
      this.$refs.salesItemAddRef.showAdd({});
    },
    //复制商品
    copySalesItem(item){
      console.log('copySalesItem:')
      this.$refs.salesItemCopyRef.showCopy(item);
    },
    //复制商品
    editSalesItem(item){
      console.log('editSalesItem:')
      this.$refs.salesItemEditRef.showEdit(item);
    },
    //修改商品
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
        await this.querySalesItemByPage();
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
      this.querySalesItemByPage();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.paginationData.currentPage = val
      this.querySalesItemByPage();
    },
    clearAll() {
      console.log('触发清除所有')
      this.submitData = {}
    },
    //查询订单信息
    async querySalesItemByPage() {
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      let params = {
        authAppUserId:this.userData.authAppUserId,
        ...this.queryParams,
        itemsPerPage: this.paginationData.itemsPerPage,
        currentPage: this.paginationData.currentPage,
      }
      await Api.querySalesItemByPage({
        ...params
      }).then(async (res) => {
        this.salesItemList = new Array();
        if (!res.success) {
          loading.close();
          return;
        }
        if (this.$isNull(res)) {
          loading.close();
          return;
        }
        let bizData = res.data;
        if (this.$isNull(bizData)) {
          loading.close();
          return;
        }
        this.paginationData.totalCount = bizData.totalCount
        this.paginationData.itemsPerPage = bizData.itemsPerPage
        this.paginationData.currentPage = bizData.currentPage
        let dataList = bizData.data;
        //console.log('dataList:' + JSON.stringify(dataList))
        if (!dataList || dataList.length === 0) {
          loading.close();
          return;
        }
        await dataList.map(async (item) => {
          //let mainImg = await this.handleImageUrl(item.mainImg);
          this.salesItemList.push(item);
        });
        loading.close();
        //console.log('salesItemList:' + JSON.stringify(this.salesItemList))
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
      console.log('当前用户信息:' + JSON.stringify(this.userData));
      await this.querySalesItemByPage();
      this.currentTime = moment().format("YYYY-MM-DD HH:mm:ss");
    },
    itemActive($event) {
      console.log($event)
      $event.currentTarget.className = 'sales-item-view-active'
    },
    removeActive($event) {
      console.log($event)
      $event.currentTarget.className = 'sales-item-view'
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
  width: 100%;
  .btn-flow-view{
    position: fixed; //让元素跟随滚动
    z-index: 998;
    right:0;
    top: 50%;
    .btn-flow-data{
      width: 150px;
      height: 50px;
      background-color: #FF0000;
      color: #FFFFFF;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      border-radius: 10px;
      .flow-text-view{
        font-size: 20px;
        font-weight: bold;
      }
    }
  }
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
  .sales-data-view{
    width: 90%;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    .sales-item-view-active {
      @extend .sales-item-view;
      box-shadow: rgba(9, 30, 66, 0.25) 0px 4px 8px -2px, rgba(9, 30, 66, 0.08) 0px 0px 0px 1px;
    }
    .sales-item-view {
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
              border-radius: 10px;
              background-color: #338be7;

              margin: 10px 10px;
              .bottom-btn-text {
                margin: 5px 10px;
                color: #FFFFFF;
                font-weight: bold;
              }
            }
            .buy-again-div {
              @extend .bottom-btn-div;
              background-color: #FF0000;
              color: #FFFFFF;
              .buy-again-text {
                margin: 5px 10px;
                color: #FFFFFF;
                font-weight: bold;
              }
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
