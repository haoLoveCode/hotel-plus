<template>
  <div class="page-view">
    <el-drawer
        :visible.sync="showDrawer"
        :direction="direction"
        :with-header="false"
        :before-close="handleDrawerClose">
      <div class="address-data-view">
        <div class="address-text-view">
          <div class="address-text">
            我的收货地址
          </div>
        </div>
        <div class="address-item-view">
          <div class="user-address-item" v-for="(item,index) in takeAddressList" :key="index">
            <div class="address-left-item">
              <div class="select-item-view">
                <div class="select-item" >
                  <!-- `checked` 为 true 或 false -->
                  <el-checkbox v-model="item.selected" @change="addressSelect($event,item)"></el-checkbox>
                </div>
              </div>
              <div class="left-address-item">
                <div class="left-text-view">
                  {{item.addressDetailed}}
                </div>
              </div>
            </div>
            <div class="address-right-item">
              <div class="right-text-view">
                收货人姓名:{{item.takerName}}
              </div>
              <div class="right-text-view">
                手机号:{{item.takerPhone}}
              </div>
              <div class="right-btn-view">
                <div class="right-btn-item" @click="updateAddress(item)">
                  <el-link type="info" :underline="false">
                    <img src="/static/icons/update-icon.png"
                         alt=""
                         width="auto"
                         height="30px"
                         style="border-radius: 10px;"/>
                  </el-link>
                </div>
                <div class="right-btn-item" @click="deleteOneAddress(item)">
                  <el-link type="info" :underline="false">
                    <img src="/static/icons/delete-icon.png"
                         alt=""
                         width="auto"
                         height="25px"
                         style="border-radius: 10px;"/>
                  </el-link>
                </div>
              </div>
            </div>
          </div>
          <div class="bottom-btn-view">
            <el-link
                type="info"
                :underline="false"
                style="font-size: 20px;width: 100%">
              <div class="bottom-btn-div" @click="batchDeleteTakeAddress">
                <div class="bottom-btn-text">
                  批量删除
                </div>
              </div>
              <div class="add-btn-div" @click="toAddAddress">
                <div class="add-btn-text">
                  新增地址
                </div>
              </div>
            </el-link>
          </div>
        </div>
      </div>
      <!--收货地址编辑-->
      <EditAddress
          ref="editAddressRef"
          @handleSuccess="queryTakeAddress">
      </EditAddress>>
    </el-drawer>
  </div>
</template>
<script>
import {mapGetters} from "vuex";
import moment from 'moment';
import Api from "@/services";
import EditAddress from "./EditAddress";

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
    EditAddress:EditAddress
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
      takeAddressList:[],
      takeAddressIdList:[],
    };
  },
  computed: {},
  methods: {
    //跳转新增收货地址
    async toAddAddress(){
      await this.handleDrawerClose();
      this.$router.push({
        path: '/addAddressView',
        query: {

        }
      })
    },
    //处理地址更新
    async updateAddress(item){
      this.$refs.editAddressRef.showEdit(item);
    },
    //处理收货地址选择
    async addressSelect(event, value) {
      console.log('addressSelect:'+event)
      //console.log('addressSelect:'+JSON.stringify(value));
      //放入结算商品集合
      if(value.selected){
        this.takeAddressIdList.push(value.takeAddressId);
      }else{
        this.takeAddressIdList = this.takeAddressIdList.filter((item) => {
          return item != value.takeAddressId
        });
        let data = this.takeAddressIdList.filter((item) => {
          return item != value.takeAddressId
        });
        console.log('执行删除')
        console.log('执行删除:'+JSON.stringify(data))
      }
      console.log('this.takeAddressIdList:'+JSON.stringify(this.takeAddressIdList))
      console.log('this.takeAddressIdList.length:'+this.takeAddressIdList.length);
    },
    //处理展示购物车信息
    async handleShowDrawer(){
      this.showDrawer = !this.showDrawer;
      if(this.showDrawer){
        let userData = await this.$bizConstants.userMeta();
        this.userData = {...userData};
        await this.queryTakeAddress();
      }
    },
    //单个删除
    async deleteOneAddress(item){
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
      mainIdList.push(item.takeAddressId);
      try {
        Api.batchDeleteTakeAddress({
          mainIdList:mainIdList
        }).then(async (res) => {
          await this.queryTakeAddress();
        });
        loading.close();
      } catch (error) {
        this.clearAll()
        loading.close();
        this.$message.error(error.message || error.msg || "服务器异常");
      }
    },
    //批量删除
    async batchDeleteTakeAddress(){
      const loading = this.$loading({
        lock: true,
        text: "正在提交...",
      });
      let takeAddressIdList = this.takeAddressIdList;
      if(!takeAddressIdList || takeAddressIdList.length == 0){
        this.$message.error('请选择收货地址');
        loading.close();
        return;
      }
      let mainIdList = this.takeAddressIdList;
      try {
        Api.batchDeleteTakeAddress({
          mainIdList:mainIdList
        }).then(async (res) => {
          await this.queryTakeAddress();
        });
        loading.close();
      } catch (error) {
        this.clearAll()
        loading.close();
        this.$message.error(error.message || error.msg || "服务器异常");
      }
    },
    //查询用户收货地址
    async queryTakeAddress(){
      const loading = this.$loading({
        lock: true,
        text: "正在提交...",
      });
      let params = {}
      try {
        Api.queryTakeAddress(params).then(async (res) => {
          this.takeAddressList = new Array();
          if (res.success) {
            let dataList = res.data;
            if(!dataList || dataList.length == 0){
              return;
            }
            await dataList.map(async (item) => {
              this.takeAddressList.push(item);
            })
            console.log('收货地址:'+JSON.stringify(this.takeAddressList))
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
      this.showDrawer = false;
    },
    clearAll() {
      console.log('触发清除所有')
      this.submitData = {}
      this.takeAddressList = new Array();
      this.takeAddressIdList = new Array();
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
  .el-radio{
    ::v-deep.el-radio__inner{
      width: 30px;
      height: 30px;
      border: 2px solid #A7A889;
      background-color: transparent;
      &::after{
        width: 16px;
        height: 16px;
        background: linear-gradient(0deg, #DD2E9B 0%, #F47039 99%);
      }
    }
    ::v-deep.el-radio__label{
      font-size: 30px;
      color: #FFFFFF;
    }
  }
  .address-data-view{
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    align-content: center;
    .address-text-view{
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      align-content: center;
      width: 100%;
      border-bottom: 1px solid #909399;
      .address-text{
        font-size: 20px;
        margin: 20px 10px;
        font-weight: bold;
      }
    }
    .address-item-view{
      width: 100%;
      overflow-y: auto;
      overflow-x: hidden;
      height: 700px;
      .user-address-item{
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        align-content: center;
        width: 95%;
        margin: 10px 10px;
        box-shadow: 0 0 2px 2px #409eff;
        border-radius: 10px;
        .address-left-item{
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
          .left-address-item{
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            align-content: center;
            margin: 10px 0px;
            width: 200px;
            .left-text-view{
              font-size: 15px;
              word-break: break-all;
            }
          }
        }
        .address-right-item{
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: flex-start;
          align-content: center;
          margin-top: 10px;
          .right-text-view{
            font-size: 15px;
          }
          .right-btn-view{
            display: flex;
            flex-direction: row;
            justify-content: space-around;
            align-items: center;
            align-content: center;
            margin-top: 10px;
            width: 100%;
            .right-btn-item{
              margin: 0px 10px;
            }
          }
        }
      }
      .bottom-btn-view {
        font-weight: bold;
        width: 100%;
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: flex-start;
        margin-bottom: 50px;
        .bottom-btn-div {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          background-color: #FF0000;
          border-radius: 10px;
          width: 300px;
          margin: 10px 0px;
          .bottom-btn-text {
            margin: 5px 10px;
            color: #FFFFFF;
            font-weight: bold;
          }
        }
        .add-btn-div{
          @extend .bottom-btn-div;
          background-color: #409EFF;
          .add-btn-text{
            margin: 5px 10px;
            color: #FFFFFF;
            font-weight: bold;
          }
        }
      }
    }
  }
}
/deep/ .select-item .el-radio__inner {
  width: 20px !important;
  height: 20px !important;
}
/deep/ .select-item .el-checkbox__inner {
  width: 20px !important;
  height: 20px !important;
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
