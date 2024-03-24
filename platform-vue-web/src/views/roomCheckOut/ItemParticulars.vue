<template>
  <el-dialog
    title=""
    width="45%"
    :center="true"
    :visible.sync="particularsVisible">
    <div class="particulars-view">
      <div class="descriptions-title-view">
        客房退房信息详情
      </div>
      <div class="descriptions-view">
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              预定订单:
            </div>
            <div class="descriptions-value">
              {{handleTypeByValue(particularsData.roomBookingId,roomBookingOptions)}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              客户身份信息:
            </div>
            <div class="descriptions-value">
              {{handleTypeByValue(particularsData.guestIdentifyId,guestIdentifyOptions)}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              退房时间:
            </div>
            <div class="descriptions-value">
              {{particularsData.checkOutTime}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              备注信息:
            </div>
            <div class="descriptions-value">
              {{particularsData.remark}}
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog append-to-body :visible.sync="previewVisible" title="图片预览">
      <img width="100%" :src="previewImageUrl" alt=""/>
    </el-dialog>
    <span slot="footer" class="dialog-footer">
      <el-button
        @click="handleCancel"
        size="mini">
        关闭
      </el-button>
    </span>
  </el-dialog>
</template>
<script>
import Api from "@/services";
import baseUtils from '@/utils/baseUtils'

export default {
  name: "ItemParticulars",
  components: {
    Api: Api,
    baseUtils: baseUtils,
  },
  data() {
    return {
      //-----------------
      roomBookingOptions: [],
      guestIdentifyOptions: [],
      //-----------------
      authUserOptions: [],
      particularsVisible: false,
      particularsData: {},
      size: '',
      previewImageUrl: '',
      previewVisible: false,
    }
  },
  methods: {
    async queryGuestIdentify() {
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      try {
        Api.queryGuestIdentify({}).then((res) => {
          if (res.success) {
            console.log('res:' + JSON.stringify(res))
            if (this.$isNull(res)) {
              return;
            }
            let data = res.data
            if (this.$isNull(data)) {
              return;
            }
            this.guestIdentifyOptions = new Array();
            data.map((item) => {
              let options = {
                'text': item.realName,
                'value': item.guestIdentifyId
              }
              this.guestIdentifyOptions.push(options)
            })
            console.log('this.guestIdentifyOptions:' + JSON.stringify(this.guestIdentifyOptions))
            loading.close();
          } else {
            loading.close();
            this.$message.error('服务器异常');
          }
        });
      } catch (error) {
        loading.close();
        this.$message.error(error.message || error.msg || "服务器异常");
      }
    },
    async queryRoomBooking() {
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      try {
        Api.queryRoomBooking({}).then((res) => {
          if (res.success) {
            console.log('res:' + JSON.stringify(res))
            if (this.$isNull(res)) {
              return;
            }
            let data = res.data
            if (this.$isNull(data)) {
              return;
            }
            this.roomBookingOptions = new Array();
            data.map((item) => {
              let options = {
                'text': item.bookingNo,
                'value': item.roomBookingId
              }
              this.roomBookingOptions.push(options)
            })
            console.log('this.roomBookingOptions:' + JSON.stringify(this.roomBookingOptions))
            loading.close();
          } else {
            loading.close();
            this.$message.error('服务器异常');
          }
        });
      } catch (error) {
        loading.close();
        this.$message.error(error.message || error.msg || "服务器异常");
      }
    },
    handlePreView(url) {
      if (this.$isNull(url)) {
        return;
      }
      this.previewImageUrl = url
      this.previewVisible = true
    },
    handleTypeByValue(value, optionList) {
      //console.log('optionList:'+JSON.stringify(optionList))
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
      //console.log('result:'+JSON.stringify(result))
      if (!this.$isNull(result)) {
        return result.text
      } else {
        return '无'
      }
    },
    showParticulars(data) {
      console.log('data:' + JSON.stringify(data))
      if (!this.$isNull(data)) {
        this.particularsData = {
          ...data
        }
      }
      this.particularsVisible = true;
    },
    async init(data) {
      this.authUserOptions = await this.$bizConstants.authUserOptions();
      await this.queryRoomBooking();
      await this.queryGuestIdentify();
      this.showParticulars(data);
    },
    handleCancel() {
      this.particularsVisible = false
      this.clearAll();
    },
    clearAll() {
      this.particularsVisible = false
    },
  },
  created() {

  }
}
</script>

<style scoped lang="scss">
.particulars-view {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  align-content: center;
  .descriptions-title-view {
    font-weight: bold;
    font-size: 20px;
    margin-bottom: 10px;
  }
  .descriptions-view {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
    align-content: center;
    width: 100%;
    .descriptions-item {
      display: flex;
      flex-direction: row;
      align-items: flex-start;
      justify-content: space-around;
      .descriptions-item-view {
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: center;
        font-size: 15px;
        .descriptions-title {
          font-weight: bold;
          padding: 10px 5px;
        }
        .descriptions-value {
          font-weight: bold;
          padding: 10px 5px;
        }
      }
    }
    .table-view {
      width: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: flex-start;
      flex-wrap: wrap;
      .table-data-view {
        width: 100%;
      }
      .table-footer-view {

      }
    }
  }
}
</style>
