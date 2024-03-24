<template>
  <div class="page-view">
    <div class="data-main-view">
      <div class="left-data-view">
        <el-image
                  class="left-img-view"
                  fit="fill" src="/static/images/login-logo.png" alt=""/>
        <div class="left-title-view">
          请登录系统
        </div>
        <div class="left-text-view">
          {{$platformText}}
        </div>
      </div>
      <div class="right-view">
        <div class="right-data-view">
          <div class="right-title-view">
            <div class="left-title">
              登录
            </div>
            <div class="right-title" @click="handleUserReg">
              <el-link style="font-size: 15px;color: #466ce0;">没有账号？点击注册</el-link>
            </div>
          </div>
          <div class="right-item-view">
            <div class="right-item-value">
              <el-input
                  v-model="submitData.uniqueNo"
                  placeholder="请填写-用户名"
                  maxlength="30"
                  show-word-limit>
              </el-input>
            </div>
          </div>
          <div class="right-item-view">
            <div class="right-item-value">
              <el-input
                  type="password"
                  v-model="submitData.password"
                  placeholder="请填写-登录密码"
                  maxlength="30"
                  show-word-limit>
              </el-input>
            </div>
          </div>
          <div class="right-item-view">
            <div class="right-item-value">
              <el-button
                  type="primary"
                  style="width: 100%;"
                  :loading="loading"
                  @click.native.prevent="handleLogin"
              >
                登 录
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--用户注册-->
    <AppUserReg ref="appUserReg"></AppUserReg>
  </div>
</template>
<script>
import {mapGetters} from "vuex";
import moment from 'moment';
import Api from "@/services";
import AppUserReg from "@/views/login/AppUserReg";

export default {
  components: {
    AppUserReg:AppUserReg
  },
  data() {
    return {
      previewImageUrl: '',
      previewVisible: false,
      editorContent: '',
      currentTime: '',
      genderOptions: this.$bizConstants.genderOptions,
      submitData: {
        uniqueNo: '',
        password: '',
      },
      loading:false,
    };
  },
  computed: {
    ...mapGetters(["permission_routers"]),
  },
  methods: {
    //处理用户注册
    handleUserReg() {
      this.$refs.appUserReg.showReg({});
    },
    //处理提交
    handleLogin() {
      try {
        this.loading = true;
        Api.userLogin({
          ...this.submitData
        }).then((res) => {
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
              this.$router.push({path: "/mainPage"});
              this.loading = false;
            });
            this.loading = false;
          } else {
            this.$message.error('登录失败:'+res.message);
            this.loading = false;
          }
        });
      } catch (error) {
        this.loading = false;
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
      if (!this.$isNull(result)) {
        return result.text
      } else {
        return '无'
      }
    },
    async init() {
      this.currentTime = moment().format("YYYY-MM-DD HH:mm:ss");

    },
  },
  created() {
    this.init();
  },
  mounted() {

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
  width: 100%;
  height: 100Vh;
  .data-main-view{
    width: 60%;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: flex-start;
    box-shadow: 0 0 8px #dedede;
    border-radius: 10px;
    .left-data-view{
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      align-content: center;
      background: linear-gradient(220.55deg, #373b44 0%, #4286f4 100%);
      width: 50%;
      height: 600px;
      .left-img-view{
        width: 60px;
        height: 60px;
        border-radius: 20px;
      }
      .left-title-view{
        border-radius: 20px;
        color: #FFFFFF;
        font-weight: bold;
        word-break: break-all;
        margin: 10px 20px;
        font-size: 30px;
      }
      .left-text-view{
        border-radius: 20px;
        color: #FFFFFF;
        font-weight: bold;
        word-break: break-all;
        margin: 10px 20px;
      }
    }
    .right-view{
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      align-content: center;
      width: 50%;
      height: 600px;
      .right-data-view{
        display: flex;
        flex-direction: column;
        justify-content: flex-end;
        align-items: flex-start;
        margin-left: 20px;
        margin-top: 10px;
        .right-title-view{
          display: flex;
          flex-direction: row;
          justify-content: space-between;
          align-items: center;
          width: 100%;
          .left-title{
            font-weight: bold;
            font-size: 25px;
          }
          .right-title{
            font-size: 15px;
          }
        }
        .right-item-view{
          display: flex;
          flex-direction: row;
          justify-content: flex-start;
          align-items: center;
          margin: 10px 0px;
          width: 100%;
          .right-item-title{
            font-size: 20px;
          }
          .right-item-value{
            margin: 0px 10px;
            font-size: 15px;
            width: 300px;
          }
        }
      }
    }
  }
}
</style>
