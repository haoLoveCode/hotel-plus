<template>
  <el-dialog title="编辑购物车信息"
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
            <el-form-item label="用户信息" prop="authAppUserId">
              <el-select
                  v-model="submitData.authAppUserId"
                  :clearable="true"
                  placeholder="请选择用户信息">
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
            <el-form-item label="商品信息" prop="salesItemId">
              <el-select
                  v-model="submitData.salesItemId"
                  :clearable="true"
                  placeholder="请选择商品信息">
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
            <el-form-item label="购买数量" prop="itemNum">
              <el-input
                  v-model="submitData.itemNum"
                  placeholder="请填写-购买数量"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="update-body-div">
          <div class="update-item-view">
            <el-form-item label="操作类型" prop="bizType">
              <el-select
                  v-model="submitData.bizType"
                  :clearable="true"
                  placeholder="请选择操作类型">
                <el-option
                    v-for="(item,index) in bizTypeOptions"
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
            <el-form-item label="备注信息" prop="remarkData">
              <el-input
                  type="textarea"
                  :rows="5"
                  v-model="submitData.remarkData"
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
      authAppUserOptions:[],
      salesItemOptions: [],
      bizTypeOptions: [
        {
          'text':'新增',
          'value': 1
        },
        {
          'text':'减少',
          'value': 2
        },
      ],
      //-----------------
      title: "编辑",
      editVisible: false,
      submitData: {
        authAppUserId: '',
        salesItemId: '',
        remarkData: '',
        itemNum: '',
        bizType: '',
      },
      validatorRules: {
        authAppUserId: [
          {
            required: true,
            message: '请选择用户信息',
            trigger: 'change'
          }
        ],
        salesItemId: [
          {
            required: true,
            message: '请选择商品信息',
            trigger: 'change'
          }
        ],
        remarkData: [
          {
            required: true,
            message: '请规范填写备注信息',
            trigger: 'blur'
          }
        ],
        itemNum: [
          {
            required: true,
            message: '请规范填写购买数量',
            pattern: new RegExp(baseUtils.numberPattern(), "g"),
            trigger: 'blur'
          }
        ],
        bizType: [
          {
            required: true,
            message: '请选择操作类型',
            trigger: 'change'
          }
        ],
      }
    };
  },
  methods: {
    async querySalesItem() {
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      try {
        Api.querySalesItem({}).then((res) => {
          if (res.success) {
            console.log('res:' + JSON.stringify(res))
            if (this.$isNull(res)) {
              return;
            }
            let data = res.data
            if (this.$isNull(data)) {
              return;
            }
            this.salesItemOptions = new Array();
            data.map((item) => {
              let options = {
                'text': item.itemName,
                'value': item.salesItemId
              }
              this.salesItemOptions.push(options)
            })
            console.log('this.salesItemOptions:' + JSON.stringify(this.salesItemOptions))
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
    showEdit(data) {
      console.log('data:' + JSON.stringify(data))
      if (data) {
        this.submitData = {
          ...data
        }
      }
      this.init();
      this.editVisible = true;
    },
    async setOtherData() {
      this.authAppUserOptions = await this.$bizConstants.queryAuthAppUser();
      this.querySalesItem();
    },
    //处理初始化
    async init() {
      await this.setOtherData();
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
        authAppUserId: '',
        salesItemId: '',
        remarkData: '',
        itemNum: '',
        bizType: '',
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
          Api.editBasketDataItem(params).then((res) => {
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
