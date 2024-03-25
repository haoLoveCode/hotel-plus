<template>
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
      <div class="bottom-div">
        <el-button
            type="primary"
            style="width: 100%;"
            @click.native.prevent="handleSubmit"
        >
          发布
        </el-button>
      </div>
    </div>
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
      userData:{},
      //-----------------
      title: "新增",
      submitData: {
        dataTitle: '',
        dataValue: '',
        submitterId: '',
        handleStatus: 1,
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
  async created() {
    await this.init();
  },
  methods: {
    async setOtherData() {

    },
    //处理初始化
    async init() {
      await this.setOtherData();
      //验证Token
      await this.$baseUtils.verifyToken();
      let userData = await this.$bizConstants.userMeta();
      this.userData = {...userData};
      this.submitData.submitterId = userData.authAppUserId;
      console.log('当前用户信息:' + JSON.stringify(this.userData))
    },
    clearAll() {
      console.log('触发清除所有')
      this.submitData = {
        dataTitle: '',
        dataValue: '',
        submitterId: '',
        handleStatus: 1,
        remarkData: '',
      }
    },
    handleCancel() {
      this.clearAll();
    },
    //处理提交
    handleSubmit() {
      this.$refs.submitForm.validate((valid) => {
        const params = {};
        if (valid) {
          this.submitData.submitterId = this.userData.authAppUserId;
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
          this.$router.push({path: "/mainPage"});
        } catch (error) {
          this.clearAll()
          loading.close();
          this.$message.error(error.message || error.msg || "服务器异常");
          this.$router.push({path: "/mainPage"});
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
  width: 100%;
  margin-top: 10px;
  .bottom-div{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-content: center;
    align-items: center;
    width: 60%;
  }
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
