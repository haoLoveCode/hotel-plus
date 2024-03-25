<template>
  <div class="page-view">
    <div class="page-top-view">
      <div class="left-view">
        {{ currentTime }}
      </div>
      <div class="right-view">
        <div class="right-menu-view">
          <el-menu :defaultActive="activeMenuIndex"
                   router
                   mode="horizontal"
                   @select="handleSelect"
                   background-color="#304156"
                   text-color="#FFFFFF"
                   active-text-color="#087fe7">
            <el-menu-item index="/mainPage">主页</el-menu-item>
            <el-menu-item index="/orderListView">我的预定订单</el-menu-item>
            <el-menu-item index="/mineFeedBackList">我的投诉建议</el-menu-item>
            <el-menu-item index="/feedBackList">用户投诉建议</el-menu-item>
            <el-menu-item index="/memberCenter">个人中心</el-menu-item>
          </el-menu>
        </div>
        <div class="biz-button-view">
          <div class="biz-button" @click="publishData">
            <el-button size="mini" type="primary">
              发布数据
            </el-button>
          </div>
        </div>
        <div class="user-data">
          <el-dropdown
              class="avatar-container"
              trigger="click">
            <span class="el-dropdown-link">
              <div class="avatar-view">
                <el-badge
                    type="primary">
                  <el-avatar
                      shape="square"
                      :size="30"
                      :src="handleImageUrl(userData.avatarUrl)">
                  </el-avatar>
                </el-badge>
              </div>
              <div class="user-name-view">
                 <el-link type="success" style="font-size: 15px">{{ userData.userName }}</el-link>
              </div>
            </span>
            <el-dropdown-menu
                class="user-dropdown"
                slot="dropdown">
              <el-dropdown-item v-if="token">
                <span
                    @click="updateProfile"
                    style="display:block;">
                  修改个人信息
                </span>
              </el-dropdown-item>
              <el-dropdown-item v-if="!token">
                <span
                    @click="handleUserReg"
                    style="display:block;">
                  用户注册
                </span>
              </el-dropdown-item>
              <el-dropdown-item v-if="!token">
                <span
                    @click="handleLogin"
                    style="display:block;">
                  用户登录
                </span>
              </el-dropdown-item>
              <el-dropdown-item v-if="token">
                <span
                    @click="logOut"
                    style="display:block;">
                  退出登录
                </span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <div class="main-container">
      <app-main></app-main>
    </div>
    <!--用户注册-->
    <AppUserReg ref="appUserReg"></AppUserReg>
    <!--用户登录-->
    <AppUserLogin ref="appUserLogin"></AppUserLogin>
    <!--用户编辑-->
    <AppUserEdit ref="appUserEdit"></AppUserEdit>
  </div>
</template>
<script>
import {mapGetters} from "vuex";
import moment from 'moment';
import {AppMain} from "views/layout";
import AppUserReg from "@/views/login/AppUserReg";
import AppUserLogin from "./AppUserLogin";
import AppUserEdit from "./AppUserEdit";

export default {
  components: {
    AppMain: AppMain,
    AppUserReg: AppUserReg,
    AppUserLogin: AppUserLogin,
    AppUserEdit: AppUserEdit,
  },
  data() {
    return {
      previewImageUrl: '',
      previewVisible: false,
      editorContent: '',
      currentTime: '',
      activeMenuIndex: '1',
      scheduleTimer:'',
    };
  },
  computed: {
    //设置当前时间
    async setCurrentTime(){
      console.log('设置时间')
      this.currentTime = moment().format("YYYY-MM-DD HH:mm:ss");
    },
    token(){
      let token = localStorage.getItem('token') || '';
      if (!this.$baseUtils.isNull(token)) {
        return token;
      }else{
        return '';
      }
    },
    userData(){
      let userData = localStorage.getItem('userData') || ''
      if (!this.$baseUtils.isNull(userData)) {
        return JSON.parse(userData);
      }else{
        return {
          userName:'请登录'
        }
      }
    }
  },
  methods: {
    //发布食谱
    publishData(){
      //this.$router.push("/publishNotes")
      //this.$refs.addCookBookRef.showAdd({});
    },
    //处理收货地址展示
    async showTakeAddress(){
      this.$refs.takeAddressRef.handleShowDrawer();
    },
    //处理用户注册
    handleUserReg() {
      this.$refs.appUserReg.showReg({});
    },
    //更新用户信息
    updateProfile() {
      this.$refs.appUserEdit.showEdit(this.userData);
    },
    //处理登录
    handleLogin(){
      this.$refs.appUserLogin.showLogin({});
    },
    //退出登录
    logOut() {
      this.$store.dispatch('LogOut').then(() => {
        localStorage.clear();
        this.$router.push({path: "/mainPage"});
        location.reload();
      });
    },
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    async init() {
      this.$router.push({path: "/mainPage"});
    },
  },
  created() {
    this.init();
  },
  mounted() {
    this.scheduleTimer = setInterval(() => {
      this.currentTime = moment().format("YYYY-MM-DD HH:mm:ss");
    },1000);
  },
  beforeDestroy() {
    //清楚定时器
    clearInterval(this.scheduleTimer);
  }
};
</script>

<style scoped lang="scss">
/deep/.el-menu .el-menu-item{
  outline: 0 !important;
  color: #000000 !important;
  background: linear-gradient(270deg, #FFFFFF 0%, #FFFFFF 100%)!important;
}
/deep/.el-menu .el-menu-item:hover{
  outline: 0 !important;
  color: #000000 !important;
  border-bottom-color: #1772F6 !important;
  background: linear-gradient(270deg, #F2F7FC 0%, #FEFEFE 100%)!important;
}
/deep/.el-menu .el-menu-item.is-active {
  color: #000000 !important;
  border-bottom-color: #1772F6 !important;;
  background: linear-gradient(270deg, #F2F7FC 0%, #FEFEFE 100%)!important;
}
.el-submenu /deep/.el-submenu__title:hover {
  color: #000000 !important;
  border-bottom-color: #1772F6 !important;;
  background: linear-gradient(270deg, #F2F7FC 0%, #FEFEFE 100%)!important;
}
.page-view {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  align-content: center;
  position: relative;
  .page-top-view {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    background-color: #FFFFFF;
    border-bottom: solid 1px #e6e6e6;
    position: fixed;
    top: 0;
    z-index: 10001;
    //height: 80px;
    .left-view {
      margin: 0px 10px;
      font-weight: bold;
      //color: #FFFFFF;
    }
    .right-view {
      margin: 0px 10px;
      font-weight: bold;
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-items: center;
      .right-menu-view {

      }
      .biz-button-view{
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        margin-right: 20px;
        .biz-button{
          color: #FFFFFF;
        }
      }
      .user-data {
        //border: 2px solid #45e519;
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: center;
        margin-right: 20px;
        .avatar-container {
          .el-dropdown-link {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-content: center;
            align-items: center;
            color: #ffffff;
            .avatar-view {

            }
            .user-name-view {
              color: #FFFFFF;
            }
          }
          .user-dropdown {
          }
        }
      }
    }
  }
  .main-container {
    width: 100%;
    margin-top: 5%;
    transition: all 0.28s ease-out;
  }
}
</style>
