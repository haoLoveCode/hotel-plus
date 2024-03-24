<template>
  <el-menu
    class="navbar-view"
    mode="horizontal">
    <!--面包屑-->
    <hamburger class="hamburger-container"
               :toggleClick="toggleSideBar"
               :isActive="sidebar.opened">
    </hamburger>
    <el-dropdown
      class="avatar-container"
      trigger="click">
      <span class="el-dropdown-link">
        <div class="user-name-view">
           <el-link type="primary">{{userName}}</el-link>
        </div>
        <div class="avatar-view">
          <el-badge
            :value="1"
            class="item"
            type="primary">
            <el-avatar
              shape="square"
              :size="30"
              :src="handleImageUrl(userData.avatarUrl)">
            </el-avatar>
          </el-badge>
        </div>
      </span>
      <el-dropdown-menu
        class="user-dropdown"
        slot="dropdown">
        <el-dropdown-item>
          <span
            @click="updateProfile"
            style="display:block;">
            修改信息
          </span>
        </el-dropdown-item>
        <el-dropdown-item>
          <span
            @click="logOut"
            style="display:block;">
            退出登录
          </span>
        </el-dropdown-item>
      </el-dropdown-menu>
      <!--更新用户信息-->
      <EditUserData ref="editUserData"></EditUserData>
    </el-dropdown>
  </el-menu>
</template>
<script>
  import { mapGetters } from "vuex";
  import TabsView from "./TabsView";
  import Hamburger from "components/Hamburger";
  import ErrorLog from "components/ErrLog";
  import errLogStore from "store/errLog";
  import EditUserData from "./auth-user/EditUserData";
  import bizConstants from "@/utils/bizConstants";
  export default {
    components: {
      TabsView,
      Hamburger,
      ErrorLog,
      EditUserData,
    },
    data() {
      return {
        userData: "",
      };
    },
    computed: {
      ...mapGetters(["sidebar", "userName", "groupType"]),
    },
    methods: {
      //更新用户信息
      updateProfile() {
        let userData = JSON.parse(localStorage.getItem("userData"));
        this.$refs.editUserData.showEdit(userData);
      },
      toggleSideBar() {
        console.log('ToggleSideBar')
        this.$store.dispatch("ToggleSideBar");
      },
      logOut() {
        this.$store.dispatch("LogOut").then(() => {
          localStorage.clear();
          this.$router.push(bizConstants.LOGIN_PAGE);
          location.reload();
        });
      },
    },
    mounted() {
      this.userData = JSON.parse(localStorage.getItem("userData"));
    },
    created() {},
  };
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar-view {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 50px;
  }
  .avatar-container {
    height: 50px;
    display: inline-block;
    position: fixed;
    right: 35px;
    z-index: 999;
    top: 6px;
    .avatar-wrapper {
      cursor: pointer;
      margin-top: 5px;
      position: relative;
      .user-avatar {
        width: 40px;
        height: 40px;
        border-radius: 10px;
      }
      .el-icon-caret-bottom {
        position: absolute;
        right: -20px;
        top: 25px;
        font-size: 12px;
      }
    }
  }
  .el-dropdown-link {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-content: center;
    align-items: flex-start;
    color: #ffffff;
    .avatar-view {
      padding: 10px 10px;
    }
    .user-name-view {

    }
  }
}
</style>



