<template>
  <el-menu
      mode="vertical"
      theme="dark"
      :default-active="active"
      :title="$route.path"
      :default-openeds="default_opened"
      :background-color="$mainThemeColor"
      text-color="#FFFFFF"
      active-text-color="#409EFF"
  >
    <sidebar-item :routes="permission_routers"></sidebar-item>
  </el-menu>
</template>
<script>
import { mapGetters } from "vuex";
import SidebarItem from "./SidebarItem";
export default {
  components: { SidebarItem },
  computed: {
    ...mapGetters(["permission_routers", "default_opened"]),
    active() {
      let pathArr = this.$route.path.split("/");
      let newArr = "";
      for (let i = 0; i < pathArr.length; i++) {
        newArr = newArr + pathArr[i] + "/";
      }
      newArr = newArr.substring(0, newArr.length - 1);
      return newArr;
    },
  },
  created() {
    console.log("permission_routers", this.permission_routers);
    console.log("default_opened", this.default_opened);
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-menu {
  min-height: 100%;
}
</style>
