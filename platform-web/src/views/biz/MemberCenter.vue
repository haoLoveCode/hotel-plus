<template>
  <div class="page-view">
    <div class="data-main-view">
      <div class="left-data-view">
        <el-image @click="handlePreView(userData.avatarUrl)"
                  class="left-img-view"
                  fit="fill" :src="userData.avatarUrl" alt=""/>
      </div>
      <div class="right-view">
        <div class="right-horizontal-data">
          <div class="horizontal-item-view">
            <div class="horizontal-title">
              用户名:
            </div>
            <div class="horizontal-value">
              {{userData.userName}}
            </div>
          </div>
          <div class="horizontal-item-view">
            <div class="horizontal-title">
              性别:
            </div>
            <div class="horizontal-value">
              {{ handleTypeByValue(userData.gender, genderOptions) }}
            </div>
          </div>
          <div class="horizontal-item-view">
            <div class="horizontal-title">
              用户名:
            </div>
            <div class="horizontal-value">
              {{userData.userName}}
            </div>
          </div>
          <div class="horizontal-item-view">
            <div class="horizontal-title">
              电话号码:
            </div>
            <div class="horizontal-value">
              {{userData.phoneNumber}}
            </div>
          </div>
        </div>
        <div class="right-data-view">
          <div class="right-item-view">
            <div class="right-item-title">
              昵称:
            </div>
            <div class="right-item-value">
              {{userData.nickName}}
            </div>
          </div>
          <div class="right-item-view">
            <div class="right-item-title">
              用户编号:
            </div>
            <div class="right-item-value">
              {{userData.userNumber}}
            </div>
          </div>
          <div class="right-item-view">
            <div class="right-item-title">
              注册时间:
            </div>
            <div class="right-item-value">
              {{userData.createTime}}
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="item-set-view">

    </div>
  </div>
</template>
<script>
import {mapGetters} from "vuex";
import moment from 'moment';
import Api from "@/services";

export default {
  components: {

  },
  data() {
    return {
      previewImageUrl: '',
      previewVisible: false,
      editorContent: '',
      currentTime: '',
      userData:{},
      genderOptions: this.$bizConstants.genderOptions,
    };
  },
  computed: {
    ...mapGetters(["permission_routers"]),
  },
  methods: {
    //查询当前用户信息
    async currentUserMeta() {
      await Api.currentUserMeta({

      }).then(async (res) => {
        if (!res.success) {
          return;
        }
        if (this.$isNull(res)) {
          return;
        }
        let data = res.data
        if (this.$isNull(data)) {
          return;
        }
        data.avatarUrl = await this.handleImageUrl(data.avatarUrl);
        this.userData = {...data};
        console.log('当前用户信息:' + JSON.stringify(this.userData))
      });
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
      //验证Token
      await this.$baseUtils.verifyToken();
      await this.currentUserMeta();
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
  position: relative;
  .item-set-view{
    width: 90%;
    margin: 10px 0px;
  }
  .data-main-view{
    width: 90%;
    margin: 10px 0px;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: flex-start;
    box-shadow: 0 0 8px #dedede;
    background: linear-gradient(220.55deg, #565656 0%, #181818 100%);
    border-radius: 10px;
    .left-data-view{
      .left-img-view{
        width: 150px;
        height: 150px;
        border-radius: 20px;
        margin: 10px 10px;
      }
    }
    .right-view{
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: flex-start;
      align-content: center;
      .right-horizontal-data{
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: flex-end;
        margin-left: 50px;
        margin-top: 10px;
        border-bottom: 1px solid #bfcbd9;
        .horizontal-item-view{
          display: flex;
          flex-direction: row;
          justify-content: flex-start;
          align-items: center;
          .horizontal-title{
            font-size: 15px;
            font-weight: bold;
            color:#FFFFFF;
          }
          .horizontal-value{
            margin: 0px 10px;
            font-size: 15px;
            color:#FFFFFF;
          }
        }
      }
      .right-data-view{
        display: flex;
        flex-direction: column;
        justify-content: flex-end;
        align-items: flex-end;
        margin-left: 50px;
        margin-top: 10px;
        .right-item-view{
          display: flex;
          flex-direction: row;
          justify-content: flex-start;
          align-items: center;
          margin-left: 50px;
          margin: 10px 0px;
          width: 100%;
          .right-item-title{
            font-size: 15px;
            font-weight: bold;
            color:#FFFFFF;
          }
          .right-item-value{
            margin: 0px 10px;
            font-size: 15px;
            color:#FFFFFF;
          }
        }
      }
    }
  }
}
</style>
