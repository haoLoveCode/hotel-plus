<template>
  <el-dialog
      title="复制房间预订信息"
      :center="true"
      @close="handleCancel"
      :visible.sync="copyVisible">
    <div class="data-body">
      <el-form
          ref="submitForm"
          :model="submitData"
          :rules="validatorRules"
          label-width="130px"
          class="data-body-form"
      >
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="预定人" prop="subscriberId">
              <el-select
                  v-model="submitData.subscriberId"
                  :clearable="true"
                  placeholder="请选择-预定人信息">
                <el-option
                    v-for="(item,index) in authAppUserOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="data-item-view">
            <el-form-item label="房间信息" prop="roomDataId">
              <el-select
                  v-model="submitData.roomDataId"
                  :clearable="true"
                  placeholder="请选择-房间信息">
                <el-option
                    v-for="(item,index) in roomDataOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="data-item-view">
            <el-form-item label="预定状态" prop="bookingStatus">
              <el-select
                  v-model="submitData.bookingStatus"
                  :clearable="true"
                  placeholder="请选择-预定状态信息">
                <el-option
                    v-for="(item,index) in bookingStatusOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="预订单号" prop="bookingNo">
              <el-input
                  v-model="submitData.bookingNo"
                  placeholder="请填写-预订单号"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
          <div class="data-item-view">
            <el-form-item label="预订时间" prop="bookingTime">
              <el-date-picker
                  v-model="submitData.bookingTime"
                  type="datetime"
                  placeholder="请选择-预订时间"
                  align="right"
                  :value-format="timeFormat"
                  :picker-options="pickerOptions"
              >
              </el-date-picker>
            </el-form-item>
          </div>
        </div>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="入住开始时间" prop="checkInBegin">
              <el-date-picker
                  v-model="submitData.checkInBegin"
                  type="datetime"
                  placeholder="请填写-入住开始时间"
                  align="right"
                  :value-format="timeFormat"
                  :picker-options="pickerOptions"
              >
              </el-date-picker>
            </el-form-item>
          </div>
          <div class="data-item-view">
            <el-form-item label="入住结束时间" prop="checkInEnd">
              <el-date-picker
                  v-model="submitData.checkInEnd"
                  type="datetime"
                  placeholder="请填写-入住结束时间"
                  align="right"
                  :value-format="timeFormat"
                  :picker-options="pickerOptions"
              >
              </el-date-picker>
            </el-form-item>
          </div>
        </div>
        <div class="text-area-view">
          <div class="data-item-view">
            <el-form-item label="备注信息" prop="remark">
              <el-input
                  type="textarea"
                  :rows="3"
                  v-model="submitData.remark"
                  placeholder="请填写-备注信息"
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
      authAppUserOptions: [],
      roomDataOptions: [],
      bookingStatusOptions: [],
      pickerOptions:this.$bizConstants.pickerOptions,
      timeFormat:'yyyy-MM-dd HH:mm:ss', //时间格式
      //-----------------
      title: "编辑",
      copyVisible: false,
      submitData: {
        subscriberId: '',
        roomDataId: '',
        bookingStatus: '',
        bookingNo: '',
        remark: '',
        bookingTime: '',
        checkInBegin: '',
        checkInEnd: '',
      },
      validatorRules: {
        subscriberId: [
          {
            required: true,
            message: '请选择-预定人',
            trigger: 'change'
          }
        ],
        roomDataId: [
          {
            required: true,
            message: '请选择-房间',
            trigger: 'change'
          }
        ],
        bookingStatus: [
          {
            required: true,
            message: '请选择-预定状态',
            trigger: 'change'
          }
        ],
        bookingNo: [
          {
            required: true,
            message: '请规范填写-预订单号',
            trigger: 'blur'
          }
        ],
        remark: [
          {
            required: true,
            message: '请规范填写-备注信息',
            trigger: 'blur'
          }
        ],
        bookingTime: [
          {
            required: true,
            message: '请选择-预订时间',
            trigger: 'change'
          }
        ],
        checkInBegin: [
          {
            required: true,
            message: '请选择-入住开始时间',
            trigger: 'change'
          }
        ],
        checkInEnd: [
          {
            required: true,
            message: '请选择-入住结束时间',
            trigger: 'change'
          }
        ],
      }
    };
  },
  methods: {
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
                'text': item.roomTitle,
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
    async showCopy(data) {
      console.log('data:' + JSON.stringify(data))
      if (data) {
        this.submitData = {
          ...data
        }
      }
      await this.init(data);
      this.copyVisible = true;
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
        subscriberId: '',
        roomDataId: '',
        bookingStatus: '',
        bookingNo: '',
        remark: '',
        bookingTime: '',
        checkInBegin: '',
        checkInEnd: '',
      }
    },
    handleCancel() {
      this.copyVisible = false
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
          Api.addRoomBookingItem(params).then((res) => {
            if (res.success) {
              this.copyVisible = false;
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
.data-body {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-content: center;
  align-items: center;
  .data-body-form {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    align-items: flex-start;
    width: 100%;
    .data-body-div {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-content: center;
      align-items: center;
      width: auto;
      .data-item-view {

      }
      .upload-item-view {
        .upload-item {

        }
      }
    }
    .text-area-view {
      width: 100%;
      .data-item-view {

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
