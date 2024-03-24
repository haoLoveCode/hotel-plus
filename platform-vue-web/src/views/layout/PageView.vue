<template>
  <div
    class="app-wrapper"
  >
    <div class="main-container">
      <app-main></app-main>
    </div>
  </div>
</template>

<script>
  import { AppMain } from "views/layout";
  export default {
    name: "PageView",
    components: {
      AppMain,
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
  .main-container {
    min-height: 100%;
    transition: all 0.28s ease-out;
    margin-left: 0px;
    margin-top: 0px;
  }
}
</style>
