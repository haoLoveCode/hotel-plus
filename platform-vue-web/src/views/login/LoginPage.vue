<template>
  <div class="page-view">
    <div class="login-bg">
      <div class="contact-us-view" v-if="$showMoreStatus">
        <div class="contact-us-content-view" v-if="showContactUsView">
          <div class="contact-us-img">
            <img width="100%" src="~@static/images/contact-us-qrCode.png" alt=""/>
          </div>
          <div class="contact-us-text-view">
            <div class="contact-us-text-content">
              <div class="contact-us-icon">
                <i class="el-icon-phone"></i>
              </div>
              <div class="contact-us-text">
                联系微信：见下方
              </div>
            </div>
            <div class="contact-us-text-content">
              <div class="contact-us-icon">
                <i class="el-icon-s-comment"></i>
              </div>
              <div class="contact-us-text">
                联系微信：SkyWalkingPro
              </div>
            </div>
          </div>
        </div>
        <div class="contact-us-button-view" @click="handleShowContactUs">
          <div class="contact-us-button-icon">
            <i class="el-icon-phone"></i>
          </div>
          <div class="contact-us-button-text">
            联系我们
          </div>
        </div>
      </div>
      <div class="login-form-view">
        <el-form
            autoComplete="on"
            :model="loginForm"
            :rules="loginRules"
            ref="loginForm"
            label-position="left"
            label-width="0px"
            class="login-form"
        >
          <h3 class="title-view">酒店后台业务管理系统</h3>
          <el-form-item prop="userName">
            <span class="svg-container">
              <i class="user-name-icon"></i>
            </span>
            <el-input
                name="userName"
                type="text"
                v-model="loginForm.userName"
                autoComplete="on"
                placeholder="用户名"
                trigger-on-focus="false"
                @blur="handleUserNameBlur()"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <span class="svg-container">
              <i class="password-icon"></i>
            </span>
            <el-input
                name="password"
                type="password"
                @keyup.enter.native="handleLogin"
                v-model="loginForm.password"
                autoComplete="on"
                placeholder="密码"
            >
            </el-input>
          </el-form-item>
          <el-form-item
              prop="verifyCode"
              v-if="showVerifyCode"
          >
            <span class="svg-container">
              <i class="verifycode-icon"></i>
            </span>
            <el-input
                name="identifyCode"
                type="text"
                style="width: 60%;"
                maxlength="4"
                @keyup.enter.native="handleLogin"
                v-model="loginForm.verifyCode"
                placeholder="验证码"
            >
            </el-input>
            <el-image
                class="returnCode"
                :src="photoCode"
                fit="contain"
                :loading="codeLoading"
                @click="clickReload"
            />

          </el-form-item>
          <el-button
              type="primary"
              style="width: 100%;"
              :loading="loading"
              @click.native.prevent="handleLogin"
          >
            登 录
          </el-button>
          <!-- icon="el-icon-s-custom" -->
          <div class="other-handle-view" v-if="false">
            <div class="button-view">
              <el-button
                  type="text"
                  style="width: 100%; color: #fff;"
                  @click="handleRegister"
              >还没有账号?立即注册
              </el-button>
            </div>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import MixinWx from "@/mixin/authUser";
import store from '@/store'
import Api from "@/services";

export default {
  components: {},
  name: "login",
  mixins: [MixinWx],
  data() {
    const validateEmail = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入用户名"));
      } else {
        callback();
      }
    };
    const validatePass = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error("密码不能小于6位"));
      } else {
        callback();
      }
    };
    const verifyCode = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入验证码"));
      } else {
        callback();
      }
    };
    return {
      //是否显示联系我们
      showContactUsView: true,
      showVerifyCode: false,
      photoCode: "",
      codeLoading: false,
      loginForm: {
        userName: "",
        password: "",
        verifyCode: "",
      },
      loginRules: {
        userName: [
          {required: true, trigger: "blur", validator: validateEmail},
        ],
        password: [
          {required: true, trigger: "blur", validator: validatePass},
        ],
        verifyCode: [
          {required: true, trigger: "blur", validator: verifyCode},
        ],
      },
      loading: false,
    };
  },
  methods: {
    //处理联系我们
    handleShowContactUs() {
      if (this.showContactUsView === true) {
        this.showContactUsView = false
        return
      }
      if (this.showContactUsView === false) {
        this.showContactUsView = true
        return
      }
    },
    //用户名输入框失去焦点
    async handleUserNameBlur() {
      let userName = this.loginForm.userName;
      if (userName) {
        await this.getCode();
        this.showVerifyCode = true;
      } else {
        this.$message.warning("请输入用户名");
      }
      console.log(userName);
    },
    //点击刷新
    clickReload() {
      this.getCode();
    },
    //获取验证码
    async getCode() {
      let userName = this.loginForm.userName;
      if (!userName) {
        return;
      }
      this.codeLoading = true;
      const getCodeData = {
        userName: userName,
        height: 40,
        width: 90,
      };
      const res = await Api.base64captchaCode(getCodeData);
      this.codeLoading = false;
      console.log(res);
      this.photoCode = res.data.base64Value;
    },
    //处理注册
    handleRegister() {
      this.$refs.registerView.showRegister({});
    },
    async handleRegisterSuccess() {
      console.log("注册成功");
    },
    handleLogin() {
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          this.loading = true;
          try {
            // 登录
            const loginResp = await this.$store.dispatch(
                "Login",
                this.loginForm
            );
            // 生成可访问的路由表
            //let menuPermissionList = store.getters.menuPermissionList;
            await this.$store.dispatch("setRouter", loginResp).then(() => {
              let addRouters = store.getters.addRouters;
              this.$router.addRoutes(addRouters);
              this.$router.push({path: "/mainPage/index"});
            });
            this.loading = false;
          } catch (error) {
            this.loading = false;
            console.log(error);
            this.$message.error(error.message);
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
  },
  created() {
    this.getCode();
  },
  destroyed() {
  },
};
</script>
<style lang="scss" scoped>
.page-view {
  position: relative;
  // height: 100vh;
  .login-bg {
    //先隐藏图片
    background-image: url("~@static/images/login-bg.png");
    //background: linear-gradient(98.72deg, #58a3ff 13.3%, #0d94d0 113.3%);
    //background-color: #065d85;
    background-repeat: no-repeat;
    background-size: 100% 100%;
    -moz-background-size: 100% 100%;
    width: 100%;
    position: absolute;
    z-index: 1;
    height: 100vh;

    .contact-us-view {
      //border: 2px solid #15de0f;
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-items: flex-end;
      float: right;
      margin-top: 50vh;
      //width: 600px;
      .contact-us-content-view {
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: flex-end;
        margin: 10px 10px;
        border-radius: 10px;
        background-color: #e3e9f9;

        .contact-us-img {
          margin: 10px 10px;
          width: 80px;
          height: 80px;
        }

        .contact-us-text-view {
          display: flex;
          flex-direction: column;
          justify-content: flex-start;
          align-items: flex-start;
          margin: 0px 10px;
          font-size: 15px;

          .contact-us-text-content {
            display: flex;
            flex-direction: row;
            justify-content: flex-start;
            align-items: flex-end;
            //border: 2px solid #15de0f;
            .contact-us-text {
              padding: 0px 5px;
            }

            .contact-us-icon {
              color: #000000;
            }
          }
        }
      }

      .contact-us-button-view {
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        align-items: center;
        font-size: 12px;
        background-color: #ffffff;
        color: #3e6af3;
        border-radius: 10px;
        margin: 10px 10px;
        width: 80px;
        height: 100px;

        .contact-us-button-icon {
          font-size: 30px;
        }

        .contact-us-button-text {
        }
      }
    }

    .login-form-view {
      .other-handle-view {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: flex-end;
        margin: 10px 10px;
      }

      .login-form {
        position: absolute;
        left: 0;
        right: 0;
        width: 400px;
        padding: 20px 20px 20px 20px;
        margin: 80px auto;
        border-radius: 20px;
        box-shadow: 2px 2px 2px #888888;
        background-color: #ffffff;
        //border: 2px solid #15de0f;
        .title-view {
          text-align: center;
          font-size: 28px;
          color: #58a3ff;
          letter-spacing: 2.658662px;
          //text-shadow: 0px 3px 0px rgba(0, 0, 0, 0.5);
        }

        .svg-container {
          position: absolute;
          top: 0;
          left: 0;
          height: 100%;
          z-index: 3;
          padding: 0px 5px 6px 15px;
          color: #889aa4;

          .business-icon {
            background: url("~@static/icons/business-icon.svg") center no-repeat;
            background-size: cover;
          }

          .business-icon:before {
            content: "替";
            font-size: 16px;
            visibility: hidden;
          }

          .user-name-icon {
            background: url("~@static/icons/username-icon.svg") center no-repeat;
            background-size: cover;
          }

          .user-name-icon:before {
            content: "替";
            font-size: 16px;
            visibility: hidden;
          }

          .password-icon {
            background: url("~@static/icons/password-icon.svg") center no-repeat;
            background-size: cover;
          }

          .password-icon:before {
            content: "替";
            font-size: 16px;
            visibility: hidden;
          }

          .verifycode-icon {
            background: url("~@static/icons/verifycode-icon.svg") center no-repeat;
            background-size: cover;
          }

          .verifycode-icon:before {
            content: "替";
            font-size: 16px;
            visibility: hidden;
          }
        }

        .el-input {
          display: inline-block;
          height: 47px;
          width: 100%;
        }

        /deep/ .el-input__inner {
          padding: 0 40px;
        }

        .button-view .el-button--text {
          // border-bottom: 1px solid #fff;
          // padding-bottom: 0 !important;
          text-shadow: 0px 3px 0px rgba(0, 0, 0, 0.3);
        }

        .returnCode {
          position: absolute;
          top: 0;
          right: 0;
          width: 90px;
          height: 40px;
          background-color: #fff;
          border-radius: 4px;
        }
      }
    }
  }
}

</style>
