<template>
  <div
    class="app-wrapper"
    :class="{hideSidebar:!sidebar.opened}"
  >
    <LinkBar></LinkBar>
    <div class="sidebar-wrapper">
      <Sidebar class="sidebar-container"></Sidebar>
    </div>
    <div class="main-container">
      <navbar></navbar>
      <app-main></app-main>
    </div>
  </div>
</template>

<script>
  import LinkBar from "./LinkBar";
  import { Navbar, Sidebar, AppMain } from "views/layout";
  export default {
    name: "layout",
    components: {
      Navbar,
      Sidebar,
      AppMain,
      LinkBar,
    },
    computed: {
      sidebar() {
        return this.$store.state.app.sidebar;
      },
    },
  };
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
@import "src/styles/mixin.scss";
.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;
  &.hideSidebar {
    .sidebar-wrapper {
      transform: translate(-200px, 0);
      .sidebar-container {
        transform: translate(132px, 0);
      }
      &:hover {
        transform: translate(0, 0);
        .sidebar-container {
          transform: translate(0, 0);
        }
      }
    }
    .main-container {
      margin-left: 40px;
    }
  }
  .sidebar-wrapper {
    width: 200px;
    position: fixed;
    top: 60px;
    bottom: 0;
    left: 0;
    z-index: 1001;
    overflow: hidden;
    transition: all 0.28s ease-out;
  }
  .sidebar-container {
    transition: all 0.28s ease-out;
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: -17px;
    overflow-y: scroll;
  }
  .main-container {
    min-height: 100%;
    transition: all 0.28s ease-out;
    margin-left: 200px;
    margin-top: 60px;
  }
}
</style>
