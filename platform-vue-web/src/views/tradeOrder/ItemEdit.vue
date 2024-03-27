<template>
  <el-dialog title="编辑交易订单信息"
             :center="true"
             @close="handleCancel"
             :visible.sync="editVisible">
    <div class="update-body">
      <el-form
          ref="submitForm"
          :model="submitData"
          :rules="validatorRules"
          label-width="130px"
          class="update-body-form"
      >
        <div class="update-body-div">
          <div class="update-item-view">
            <el-form-item label="商品信息" prop="itemId">
              <el-select
                  v-model="submitData.itemId"
                  :clearable="true"
                  placeholder="请选择商品信息">
                <el-option
                    v-for="(item,index) in roomDataOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="update-item-view">
            <el-form-item label="下单用户信息" prop="authAppUserId">
              <el-select
                  v-model="submitData.authAppUserId"
                  :clearable="true"
                  @change="handleAppUserChange"
                  placeholder="请选择下单用户信息">
                <el-option
                    v-for="(item,index) in authAppUserOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="update-item-view">
            <el-form-item label="订单类型" prop="orderType">
              <el-select
                  v-model="submitData.orderType"
                  :clearable="true"
                  placeholder="请选择订单类型">
                <el-option
                    v-for="(item,index) in orderTypeOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        <div class="update-body-div">
          <div class="update-item-view">
            <el-form-item label="支付订单号" prop="outTradeNo">
              <el-input
                  v-model="submitData.outTradeNo"
                  placeholder="请填写-支付订单号"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
          <div class="update-item-view">
            <el-form-item label="订单价格" prop="orderAmount">
              <el-input
                  v-model="submitData.orderAmount"
                  placeholder="请填写-订单价格"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
          <div class="update-item-view">
            <el-form-item label="支付方式" prop="payType">
              <el-select
                  v-model="submitData.payType"
                  :clearable="true"
                  placeholder="请选择支付方式">
                <el-option
                    v-for="(item,index) in payTypeOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        <div class="update-body-div">
          <div class="update-item-view">
            <el-form-item label="订单状态" prop="orderStatus">
              <el-select
                  v-model="submitData.orderStatus"
                  :clearable="true"
                  placeholder="请选择支付方式">
                <el-option
                    v-for="(item,index) in orderStatusOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        <div class="text-area-view">
          <div class="update-item-view">
            <el-form-item label="订单额外数据" prop="extraData">
              <el-input
                  type="textarea"
                  :rows="3"
                  v-model="submitData.extraData"
                  placeholder="请填写-订单额外数据"
                  maxlength="200"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="text-area-view">
          <div class="update-item-view">
            <el-form-item label="订单备注" prop="orderRemark">
              <el-input
                  type="textarea"
                  :rows="3"
                  v-model="submitData.orderRemark"
                  placeholder="请填写-订单备注"
                  maxlength="200"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button
        @click="handleCancel"
        size="mini">
        取 消
      </el-button>
      <el-button
        type="primary"
        @click="handleSubmit"
        size="mini">
        提 交
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import Api from "@/services";
import baseUtils from '@/utils/baseUtils'
import UploadImg from "@/components/UploadImg";
export default {
  components: {
    UploadImg: UploadImg
  },
  name: "ItemEdit",
  data() {
    return {
      //-----------------
      orderStatusOptions:this.$bizConstants.orderStatusOptions,
      payTypeOptions:this.$bizConstants.payTypeOptions,
      orderTypeOptions:this.$bizConstants.orderTypeOptions,
      authAppUserOptions:[],
      roomDataOptions: [],
      takeAddressList: [],
      //-----------------
      title: "编辑",
      editVisible: false,
      submitData: {
        itemId: '',
        authAppUserId: '',
        orderType: '',
        orderRemark: '',
        outTradeNo: '',
        orderAmount: '',
        payType: '',
        extraData: '',
        orderStatus: '',
      },
      validatorRules: {
        itemId: [
          {
            required: true,
            message: '请选择商品信息',
            trigger: 'change'
          }
        ],
        authAppUserId: [
          {
            required: true,
            message: '请选择下单用户信息',
            trigger: 'change'
          }
        ],
        orderType: [
          {
            required: true,
            message: '请选择订单类型',
            trigger: 'change'
          }
        ],
        orderRemark: [
          {
            required: true,
            message: '请规范填写订单备注',
            trigger: 'blur'
          }
        ],
        outTradeNo: [
          {
            required: true,
            message: '请规范填写支付订单号',
            trigger: 'blur'
          }
        ],
        orderAmount: [
          {
            required: true,
            message: '请规范填写订单价格',
            pattern: new RegExp(baseUtils.numberPattern(), "g"),
            trigger: 'blur'
          }
        ],
        payType: [
          {
            required: true,
            message: '请选择支付方式',
            trigger: 'change'
          }
        ],
        extraData: [
          {
            required: true,
            message: '请规范填写订单额外数据',
            trigger: 'blur'
          }
        ],
        orderStatus: [
          {
            required: true,
            message: '请选择订单状态',
            trigger: 'change'
          }
        ],
      }
    };
  },
  methods: {
    //选择的用户信息发生改变
    async handleAppUserChange(value){
      console.log('handleAppUserChange:'+value);
    },
    async queryRoomData() {
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      try {
        Api.queryRoomData({}).then((res) => {
          if (res.success) {
            console.log('res:' + JSON.stringify(res))
            if (this.$isNull(res)) {
              return;
            }
            let data = res.data
            if (this.$isNull(data)) {
              return;
            }
            this.roomDataOptions = new Array();
            data.map((item) => {
              let options = {
                'text': item.roomNo,
                'value': item.roomDataId
              }
              this.roomDataOptions.push(options)
            })
            console.log('this.roomDataOptions:' + JSON.stringify(this.roomDataOptions))
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
    //处理展示
    async showEdit(data) {
      console.log('data:' + JSON.stringify(data))
      if (data) {
        this.submitData = {
          ...data
        }
      }
      await this.init(data);
      this.editVisible = true;
    },
    async setOtherData(data) {
      this.authAppUserOptions = await this.$bizConstants.queryAuthAppUser();
      await this.queryRoomData();
    },
    //处理初始化
    async init(data) {
      await this.setOtherData(data);
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      loading.close();
    },
    clearAll() {
      console.log('触发清除所有')
      this.submitData = {
        itemId: '',
        authAppUserId: '',
        orderType: '',
        orderRemark: '',
        outTradeNo: '',
        orderAmount: '',
        payType: '',
        extraData: '',
        orderStatus: '',
      }
    },
    handleCancel() {
      this.editVisible = false
      this.clearAll();
    },
    //处理提交
    handleSubmit() {
      this.$refs.submitForm.validate((valid) => {
        const params = {};
        if (valid) {
          Object.assign(params, this.submitData);
          console.log("params", params);
        } else {
          this.$message.error('验证异常');
          return;
        }
        const loading = this.$loading({
          lock: true,
          text: "正在提交...",
        });
        try {
          Api.editTradeOrderItem(params).then((res) => {
            if (res.success) {
              this.editVisible = false;
              this.$emit("handleSuccess");
              this.$message({
                message: "操作成功!",
                type: "success",
              });
              this.clearAll()
            } else {
              this.$message.error('服务器异常');
            }
          });
          loading.close();
        } catch (error) {
          this.clearAll()
          loading.close();
          this.$message.error(error.message || error.msg || "服务器异常");
        }
      });
    },
  },
};
</script>
<style scoped lang="scss">
.update-body {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-content: center;
  align-items: center;
  .update-body-form {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    align-items: flex-start;
    width: 100%;
    .update-body-div {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-content: center;
      align-items: center;
      width: auto;
      .update-item-view {

      }
      .upload-item-view {
        .upload-item {

        }
      }
    }
    .text-area-view {
      width: 100%;
      .update-item-view {

      }
    }
    .dynamic-body-div {
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: flex-start;
      align-content: center;
      width: auto;
      border: 1px solid #575e57;
      margin: 2px 0px;
      width: 100%;
      .dynamic-row-view {
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: flex-start;
        align-content: center;
        width: auto;
        .dynamic-item-view {

        }
        .remove-button-view {
          margin: 10px 10px;
        }
      }
    }
  }
}
</style>
