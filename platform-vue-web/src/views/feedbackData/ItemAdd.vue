<template>
  <el-dialog title="新增投诉建议信息"
             :center="true"
             @close="handleCancel"
             :visible.sync="addVisible">
    <div class="add-body">
      <el-form
        ref="submitForm"
        :model="submitData"
        :rules="validatorRules"
        label-width="130px"
        class="add-body-form"
      >
        <div class="text-area-view">
          <div class="add-item-view">
            <el-form-item label="内容标题" prop="dataTitle">
              <el-input
                  type="textarea"
                  :rows="5"
                  v-model="submitData.dataTitle"
                  placeholder="请填写-内容标题"
                  maxlength="200"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="text-area-view">
          <div class="add-item-view">
            <el-form-item label="内容" prop="dataValue">
              <el-input
                  type="textarea"
                  :rows="5"
                  v-model="submitData.dataValue"
                  placeholder="请填写-内容"
                  maxlength="200"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="add-body-div">
          <div class="add-item-view">
            <el-form-item label="提交人" prop="submitterId">
              <el-select
                  v-model="submitData.submitterId"
                  :clearable="true"
                  placeholder="请选择提交人">
                <el-option
                    v-for="item in authAppUserOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="add-item-view">
            <el-form-item label="处理状态" prop="handleStatus">
              <el-select
                  v-model="submitData.handleStatus"
                  :clearable="true"
                  placeholder="请选择处理状态">
                <el-option
                    v-for="item in handleOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        <div class="text-area-view">
          <div class="add-item-view">
            <el-form-item label="备注" prop="remarkData">
              <el-input
                  type="textarea"
                  :rows="5"
                  v-model="submitData.remarkData"
                  placeholder="请填写-备注"
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
  name: "ItemAdd",
  data() {
    return {
      //-----------------
      authAppUserOptions:[],
      handleOptions: [
        {
          'text':'已提交',
          'value':1
        },
        {
          'text':'已处理',
          'value':2
        },
      ],
      //-----------------
      title: "新增",
      addVisible: false,
      submitData: {
        dataTitle: '',
        dataValue: '',
        submitterId: '',
        handleStatus: '',
        remarkData: '',
      },
      validatorRules: {
        dataTitle: [
          {
            required: true,
            message: '请规范填写内容标题',
            trigger: 'blur'
          }
        ],
        dataValue: [
          {
            required: true,
            message: '请规范填写内容',
            trigger: 'blur'
          }
        ],
        submitterId: [
          {
            required: true,
            message: '请选择提交人',
            trigger: 'change'
          }
        ],
        handleStatus: [
          {
            required: true,
            message: '请选择处理状态',
            trigger: 'change'
          }
        ],
        remarkData: [
          {
            required: true,
            message: '请规范填写备注',
            trigger: 'blur'
          }
        ],
      }
    };
  },
  methods: {
    //处理展示
    showAdd(data) {
      console.log('data:' + JSON.stringify(data))
      if (data) {

      }
      this.init();
      this.addVisible = true;
    },
    async setOtherData() {
      this.authAppUserOptions = await this.$bizConstants.queryAuthAppUser();
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
        dataTitle: '',
        dataValue: '',
        submitterId: '',
        handleStatus: '',
        remarkData: '',
      }
    },
    handleCancel() {
      this.addVisible = false
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
          Api.addFeedbackDataItem(params).then((res) => {
            if (res.success) {
              this.addVisible = false;
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
.add-body {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-content: center;
  align-items: center;
  .add-body-form {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    align-items: flex-start;
    width: 100%;
    .add-body-div {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-content: center;
      align-items: center;
      width: auto;
      .add-item-view {

      }
      .upload-item-view {
        .upload-item {

        }
      }
    }
    .text-area-view {
      width: 100%;
      .add-item-view {
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
