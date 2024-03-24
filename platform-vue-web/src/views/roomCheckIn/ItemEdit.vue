<template>
  <el-dialog
      title="编辑客房入住信息"
      :center="true"
      @close="handleCancel"
      :visible.sync="editVisible">
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
            <el-form-item label="预定订单" prop="roomBookingId">
              <el-select
                  v-model="submitData.roomBookingId"
                  :clearable="true"
                  placeholder="请选择-预定订单信息">
                <el-option
                    v-for="(item,index) in roomBookingOptions"
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
            <el-form-item label="客户身份信息" prop="guestIdentifyId">
              <el-select
                  v-model="submitData.guestIdentifyId"
                  :clearable="true"
                  placeholder="请选择-客户身份信息信息">
                <el-option
                    v-for="(item,index) in guestentifyOptions"
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
            <el-form-item label="房间" prop="roomDataId">
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
        </div>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="房间编号" prop="roomNo">
              <el-input
                  v-model="submitData.roomNo"
                  placeholder="请填写-房间编号"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="备注信息" prop="remark">
              <el-input
                  v-model="submitData.remark"
                  placeholder="请填写-备注信息"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="入住时间" prop="checkInTime">
              <el-input
                  v-model="submitData.checkInTime"
                  placeholder="请填写-入住时间"
                  maxlength="10"
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
      roomBookingOptions: [],
      guestentifyOptions: [],
      roomDataOptions: [],
      //-----------------
      title: "编辑",
      editVisible: false,
      submitData: {
        roomBookingId: '',
        guestIdentifyId: '',
        roomDataId: '',
        roomNo: '',
        remark: '',
        checkInTime: '',
      },
      validatorRules: {
        roomBookingId: [
          {
            required: true,
            message: '请选择-预定订单',
            trigger: 'change'
          }
        ],
        guestIdentifyId: [
          {
            required: true,
            message: '请选择-客户身份信息',
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
        roomNo: [
          {
            required: true,
            message: '请规范填写-房间编号',
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
        checkInTime: [
          {
            required: true,
            message: '请规范填写-入住时间',
            trigger: 'blur'
          }
        ],
      }
    };
  },
  methods: {
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
        roomNo: '',
        remark: '',
        checkInTime: '',
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
          Api.editRoomCheckInItem(params).then((res) => {
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
