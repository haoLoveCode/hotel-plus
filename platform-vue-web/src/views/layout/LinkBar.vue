<template>
  <div class="link-bar-wrapper">
    <el-menu
      mode="horizontal" theme="dark"
      :default-active="activeId"
      :background-color="$mainThemeColor"
      text-color="#FFFFFF"
      active-text-color="#409EFF"
    >
      <img src="~@static/icons/main-logo.svg"/>
      <span class="platform-title">通用ElementUI</span>
    </el-menu>
  </div>
</template>

<script>
import MixinWx from '@/mixin/authUser'
export default {
  name: 'LinkBar',
  mixins: [ MixinWx ],
  data() {
    return{
      activeId:'',
      num:''
    }
  },
  created() {
  },
  methods: {
    goto() {
      this.$router.push({path: '/car/8'});
    },
    isTest() {
      const hostname = window.location.hostname
      return hostname.indexOf('.cn') !== -1
    }
  }
}
</script>

<style rel="stylesheet/css" scoped>
  .link-bar-wrapper {
    position: fixed;
    top: 0;
    width: 100%;
    z-index: 100;
  }
  .el-menu-item > a {
    text-decoration: none;
    margin-left: -15px;
    font-size: 24px;
    /* font-family: '华文行楷'; */
  }
  .el-menu >img {
    /* margin: 18px auto; */
    /* width: 50px; */
    height: 36px;
    float: left;
    margin: 10px 10px;
    margin-left: 20px;
  }
  .platform-title{
    line-height: 60px;
    font-size: 18px;
    color: #ffffff;
    margin-left: 30px;
  }
</style>
