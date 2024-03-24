<template>
  <div class="page-view">
    <el-divider content-position="center">系统信息</el-divider>
    <div class="app-title-view">欢迎来到通用ElementUI,当前登录用户:{{userData.userName}},当前登陆IP地址:{{userData.currentIp}}</div>
    <div class="platform-text-view" v-if="$showMoreStatus">
      {{platformItemText}}
    </div>
    <div class="platform-view" v-if="$showMoreStatus">
      <div class="platform-item-view" v-for="(item,index) in platformItemList" :key="index">
        <div class="image-view">
          <el-image
              style="width: 200px; height: 200px"
              :src="item.imageUrl"
              fit="cover"></el-image>
        </div>
        <div class="text-view">
          {{item.itemText}}
        </div>
      </div>
    </div>
    <div class="main-chart-view">
      <div class="chart-view">
        <VisitCountChart></VisitCountChart>
      </div>
      <div class="chart-view">
        <UserRoleChart></UserRoleChart>
      </div>
    </div>
    <el-dialog append-to-body :visible.sync="previewVisible"  title="图片预览">
      <img width="100%" :src="previewImageUrl" alt="" />
    </el-dialog>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import UserRoleChart from './UserRoleChart'
import VisitCountChart from './VisitCountChart'
export default {
  components:{
    UserRoleChart:UserRoleChart,
    VisitCountChart:VisitCountChart,
  },
  data() {
    return {
      authUserGenderOptions:this.$bizConstants.authUserGenderOptions,
      userData:{},
      platformItemText:'该项目采用市面上比较流程的前后端分离架构，以SpringBoot技术栈为后端，以VUE为前端，采用优雅简洁漂亮的UI框架。' +
          '系统采用前端发起请求，后端处理业务的方式进行交互，相对于传统的JSP，freemarker等技术有较大区别以及先进性。' +
          '同时在权限控制方面有独到的创新，实现了VUE自定义指令，以控制系统权限到每一个系统按钮。是非常适合作为毕业设计以及学习的系统。',
      platformItemList:[
        {
          imageUrl:'https://www.skywalking.pro/download/platform/WxQrCode.png',
          itemText:'如需获取源码，共同交流，请微信扫码联系我们。只要共同交流，才会有所进步。只有阅读源码，才能了解项目。'
        },
        {
          imageUrl:'https://www.skywalking.pro/download/platform/AliPay.png',
          itemText:'如果您想捐赠此项目，帮我们以更多动力优化此项目，请使用支付宝扫码捐赠。'
        },
        {
          imageUrl:'https://www.skywalking.pro/download/platform/WxPay.png',
          itemText:'如果您想捐赠此项目，帮我们以更多动力优化此项目，请使用微信扫码捐赠。'
        },
      ],
      paginationData: {
        itemsPerPage: 5,
        totalCount: 0,
        currentPage:1
      },
      previewImageUrl:'',
      previewVisible:false,
      editorContent:'',
    };
  },
  computed: {
    ...mapGetters(["permission_routers"]),
  },
  methods: {
    handlePreView(url){
      if(this.$isNull(url)){
        return;
      }
      this.previewImageUrl = url
      this.previewVisible = true
    },
    handleTypeByValue(value,optionList){
      if(this.$isNull(value)){
        console.log('返回空')
        return '无'
      }
      if(this.$isNull(optionList)){
        console.log('optionList返回空')
        return '';
      }
      let result =
          optionList.find(item => item.value == value);
      if(!this.$isNull(result)){
        return result.text
      }else{
        return '无'
      }
    },
    async init(){

    },
  },
  created() {
    this.init();
  },
  mounted() {
    this.userData = JSON.parse(localStorage.getItem('userData'));
  },
};
</script>

<style scoped lang="scss">
  @import "./style.scss";
  .page-view{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    align-content: center;
    margin-top: 20px;
    .platform-text-view{
      margin: 10px 10px;
      font-size: 15px;
      font-weight: bold;
      color: #087fe7;
    }
    .app-title-view{
      font-size: 20px;
      font-weight: bold;
      color: #ec0621;
    }
    .platform-view{
      width:100%;
      display: flex;
      flex-direction: row;
      justify-content: space-around;
      align-items: flex-start;
      align-content: center;
      flex-wrap: wrap;
      .platform-item-view{
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        align-content: center;
        margin: 10px 10px;
        box-shadow: 5px 5px 5px #7D7D7D;
        border-radius: 10px;
        border: 2px solid #888888;
        .image-view{
          margin: 10px 10px;
          border-radius: 10px;
        }
        .text-view{
          width: 200px;
          font-size: 18px;
          //此处是换行的关键
          word-wrap: break-word;
          line-break: anywhere;
          color: #0d94d0;
          margin: 10px 10px;
        }
      }
    }
    .main-chart-view{
      width:100%;
      //border: 2px solid #dc2828;
      display: flex;
      flex-direction: row;
      justify-content: center;
      align-items: center;
      flex-wrap:wrap;
      .chart-view{
        //border: 5px solid #e01f5d;
        width:50%;
      }
    }
    .table-title{
      font-size: 30px;
      font-weight: bold;
      color: #1897ec;
    }
    .table-view{
      width: 100%;
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-items: flex-start;
      flex-wrap:wrap;
      margin-left: 60px;
      .table-item{
        .card-item{
          display: flex;
          flex-direction: column;
          justify-content: flex-start;
          align-items: center;
          border: 2px solid #08bfec;
          margin: 20px 20px;
          .card-image{
            //border: 2px solid #15d20e;
            width: 150px;
            height: 150px;
            border-radius: 10px;
          }
          .card-desc-view{
            //border: 2px solid #d20e3b;
            display: flex;
            flex-direction: column;
            justify-content: flex-start;
            align-items: center;
            margin-top: 10px;
            .desc-item-view{
              display: flex;
              flex-direction: row;
              justify-content: space-between;
              align-items: flex-start;
              font-weight: bold;
              font-size: 12px;
              width: 100%;
              .desc-title{

              }
              .desc-value{
                font-size: 12px;
              }
            }
            .card-desc-bottom{
              .button-view{

              }
            }
          }
        }
      }
      .pagination-view{
        margin-top: 20px;
        margin-bottom: 20px;
        margin-left: 20px;
      }
    }
  }

</style>
