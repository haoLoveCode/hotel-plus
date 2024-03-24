<template>
  <el-dialog title="用户登录"
             :center="true"
             :visible.sync="loginVisible"
             width="40%">
    <div class="login-body">
      <el-form
          ref="submitForm"
          :model="submitData"
          :rules="validatorRules"
          label-width="130px"
          class="login-body-form"
      >
        <div class="login-body-div">
          <div class="login-item-view">
            <el-form-item label="用户名" prop="uniqueNo">
              <el-input
                  v-model="submitData.uniqueNo"
                  placeholder="请填写-用户名"
                  maxlength="30"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
          <div class="login-item-view">
            <el-form-item label="登录密码" prop="password">
              <el-input
                  type="password"
                  v-model="submitData.password"
                  placeholder="请填写-登录密码"
                  maxlength="30"
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
      //--------------------------------
      pickerOptions: this.$commonOptions.pickerOptions,
      yesOrNoOptions: this.$bizConstants.yesOrNoOptions,
      timeFormat: 'yyyy-MM-dd HH:mm:ss', //时间格式
      title: "新增",
      loginVisible: false,
      submitData: {
        uniqueNo: '',
        password: '',
      },
      validatorRules: {
        uniqueNo: [
          {
            required: true,
            message: '请规范填写用户名',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: false,
            message: '请规范填写登录密码',
            trigger: 'blur'
          }
        ],
      }
    };
  },
  methods: {
    //处理展示
    showLogin(data) {
      console.log('开始处理登录')
      //console.log('data:' + JSON.stringify(data))
      if (data) {

      }
      this.init();
      this.loginVisible = true;
    },
    async setOtherData() {

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
        uniqueNo: '',
        password: '',
      }
    },
    handleCancel() {
      this.loginVisible = false
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
          Api.userLogin(params).then((res) => {
            if (res.success) {
              this.loginVisible = false;
              this.$emit("handleSuccess");
              this.$message({
                message: "操作成功!",
                type: "success",
              });
              let data = res.data;
              console.log('登录结果:'+JSON.stringify(data));
              this.$store.dispatch('setLoginMeta',data).then(() => {
                this.$message.success('登录成功');
                location.reload();
              });
              this.clearAll()
            } else {
              this.$message.error('登录失败:'+res.message);
              this.clearAll()
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
.login-body {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-content: center;
  align-items: center;
  .login-body-form {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    align-items: flex-start;
    width: 100%;
    .login-body-div {
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-content: center;
      align-items: center;
      width: 80%;
      .login-item-view {
        width: 80%;
      }
      .upload-item-view {
        .upload-item {

        }
      }
    }
  }
}
</style>
