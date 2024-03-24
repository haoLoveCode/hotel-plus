<template>
  <el-dialog
      title="选择收货地址"
      :center="true"
      @close="handleClose"
      :visible.sync="showSelect">
    <div class="page-view">
      <div class="add-btn-view" @click="toAddAddress">
        <div class="add-btn-div">
          <el-link type="info" :underline="false" style="font-size: 20px;width: 100%">
            <div class="add-btn-text">
              新增收货地址
            </div>
          </el-link>
        </div>
      </div>
      <div class="address-data-view">
        <div class="address-item-view" v-for="(item,index) in takeAddressList" :key="index">
          <div class="select-item-view">
            <div class="select-item">
              <!-- `checked` 为 true 或 false -->
              <el-checkbox v-model="item.selected" @change="changeAddress($event,item)"></el-checkbox>
            </div>
          </div>
          <div class="address-right-view">
            <div class="address-text-view">
              {{ item.addressDetailed }}
            </div>
            <div class="address-text-view">
              收货人姓名:{{ item.takerName }}
            </div>
            <div class="address-text-view">
              手机号:{{ item.takerPhone }}
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-btn-view" @click="confirmAddress">
        <el-link type="info" :underline="false" style="font-size: 20px;width: 100%">
          <div class="bottom-btn-div">
            <div class="bottom-btn-text">
              确认地址
            </div>
          </div>
        </el-link>
      </div>
    </div>
  </el-dialog>
</template>
<script>
import Api from "@/services";
export default {
  components: {

  },
  name: "SelectAddress",
  data() {
    return {
      //-----------------
      userData: {},
      //是否展示收货地址选择
      showSelect: false,
      takeAddressList: [],
      //-----------------
    };
  },
  methods: {
    //跳转新增收货地址
    async toAddAddress(){
      this.$router.push({
        path: '/addAddressView',
        query: {

        }
      })
    },
    //处理关闭
    async handleClose() {
      let selectAddress = this.takeAddressList.find((item) => item.selected === true);
      if(!selectAddress || !selectAddress.selected){
        this.$message.error("请选择收货地址");
        return;
      }
      this.showSelect = false;
      this.$emit("handleSelect",selectAddress);
    },
    //处理选择地址
    async confirmAddress() {
      console.log('confirmAddress');
      let selectAddress = this.takeAddressList.find((item) => item.selected === true);
      if(!selectAddress || !selectAddress.selected){
        this.$message.error("请选择收货地址");
        return;
      }
      this.showSelect = false;
      this.$emit("handleSelect",selectAddress);
    },
    //处理收货地址选择
    async changeAddress(event, value) {
      console.log('changeAddress:' + event)
      console.log('changeAddress:' + JSON.stringify(value));
      this.takeAddressList.map(async (item) => {
        //其他的如果选择了则取消选择
        if (item.takeAddressId != value.takeAddressId) {
          if (item.selected) {
            item.selected = false;
          }
        }
      })
    },
    //处理展示收货地址选择信息
    async handleShowSelect() {
      this.showSelect = !this.showSelect;
      if (this.showSelect) {
        let data = await this.$bizConstants.userMeta();
        this.userData = {...data};
        console.log('当前用户信息:' + JSON.stringify(this.userData))
        await this.queryTakeAddress();
      }
    },
    //查询用户收货地址
    async queryTakeAddress() {
      const loading = this.$loading({
        lock: true,
        text: "正在提交...",
      });
      let params = {
        authAppUserId: this.userData.authAppUserId
      }
      try {
        Api.queryTakeAddress(params).then(async (res) => {
          this.takeAddressList = new Array();
          if (res.success) {
            let dataList = res.data;
            if (!dataList || dataList.length == 0) {
              return;
            }
            await dataList.map(async (item) => {
              item.selected = false;
              this.takeAddressList.push(item);
            })
            console.log('收货地址:' + JSON.stringify(this.takeAddressList))
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
  .add-btn-view {
    font-weight: bold;
    width: 90%;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: flex-start;
    margin-bottom: 20px;
    .add-btn-div {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      background-color: #409EFF;
      border-radius: 10px;
      width: 200px;
      .add-btn-text {
        margin: 5px 10px;
        color: #FFFFFF;
        font-weight: bold;
      }
    }
  }
  .address-data-view {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
    width: 90%;
    margin-top: 20px;
    border: 1px solid #909399;
    border-radius: 10px;
    overflow-y: auto;
    overflow-x: hidden;
    height: 300px;
    .address-item-view {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-items: center;
      width: 50%;
      border-bottom: 1px solid #909399;
      margin: 10px 0px;
      .select-item-view {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        margin: 10px 10px;
        .select-item {

        }
      }
      .address-right-view {
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: flex-start;
        width: 100%;
        .address-text-view {

        }
      }
    }
  }
  .bottom-btn-view {
    font-weight: bold;
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    margin-bottom: 20px;
    .bottom-btn-div {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      background-color: #FF0000;
      border-radius: 10px;
      width: 400px;
      .bottom-btn-text {
        margin: 5px 10px;
        color: #FFFFFF;
        font-weight: bold;
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
