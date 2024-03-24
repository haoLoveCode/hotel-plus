<template>
  <el-dialog title="" :center="true" :visible.sync="particularsVisible">
    <div class="particulars-view">
      <div class="descriptions-title-view">
        系统用户详情
      </div>
      <div class="descriptions-view">
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">用户名:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ particularsData.userName }}
            </div>
          </div>
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">用户编号:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ particularsData.userNumber }}
            </div>
          </div>
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">所属角色:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ particularsData.roleName }}
            </div>
          </div>
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">真实姓名:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ particularsData.realName }}
            </div>
          </div>
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">手机号码:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ particularsData.phoneNumber }}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">性别:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ handleTypeByValue(particularsData.gender, authUserGenderOptions) }}
            </div>
          </div>
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">用户状态:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ handleTypeByValue(particularsData.authStatus, authStatusOptions) }}
            </div>
          </div>
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">创建人:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ handleTypeByValue(particularsData.operatorId, authUserOptions) }}
            </div>
          </div>
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">昵称:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ particularsData.nickName }}
            </div>
          </div>
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">头像:</el-tag>
            </div>
            <div class="descriptions-value">
              <img @click="handlePreView(handleImageUrl(particularsData.avatarUrl))"
                   :src="handleImageUrl(particularsData.avatarUrl)"
                   style="width: 30px;height: 30px;border-radius: 5px;"/>
            </div>
          </div>
        </div>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
        <el-button
            size="mini"
            @click="handleCancel">
            关闭
        </el-button>
    </span>
    <el-dialog append-to-body :visible.sync="previewVisible" title="图片预览">
      <img width="100%" :src="previewImageUrl" alt=""/>
    </el-dialog>
  </el-dialog>
</template>
<script>
import Api from "@/services";
import baseUtils from '@/utils/baseUtils'

export default {
  name: "ItemParticulars",
  components: {
    Api: Api,
    baseUtils: baseUtils,
  },
  data() {
    return {
      previewImageUrl: '',
      previewVisible: false,
      authStatusOptions: this.$bizConstants.authStatusOptions,
      authUserGenderOptions: this.$bizConstants.authUserGenderOptions,
      authUserOptions: [],
      particularsVisible: false,
      particularsData: {},
      size: '',
    }
  },
  methods: {
    handlePreView(url) {
      if (this.$isNull(url)) {
        return;
      }
      this.previewImageUrl = url
      this.previewVisible = true
    },
    handleTypeByValue(value, optionList) {
      //console.log('optionList:'+JSON.stringify(optionList))
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
      //console.log('result:'+JSON.stringify(result))
      if (!this.$isNull(result)) {
        return result.text
      } else {
        return '无'
      }
    },
    showParticulars(data) {
      console.log('data:' + JSON.stringify(data))
      if (!this.$isNull(data)) {
        this.particularsData = {
          ...data
        }
      }
      this.particularsVisible = true;
    },
    async init(data) {
      this.authUserOptions = await this.$bizConstants.authUserOptions()
      this.showParticulars(data);
    },
    handleCancel() {
      this.particularsVisible = false
      this.clearAll();
    },
    clearAll() {
      this.particularsVisible = false
    },
  },
  created() {

  }
}
</script>

<style scoped lang="scss">
.particulars-view {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  align-content: center;

  .descriptions-title-view {
    font-weight: bold;
    font-size: 20px;
    margin-bottom: 10px;
  }

  .descriptions-view {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
    align-content: center;

    .descriptions-item {
      display: flex;
      flex-direction: row;
      align-items: flex-start;
      justify-content: space-around;

      .descriptions-item-view {
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: center;
        font-size: 15px;

        .descriptions-title {
          font-weight: bold;
          padding: 10px 5px;
        }

        .descriptions-value {
          font-weight: bold;
          padding: 10px 5px;
        }
      }
    }
  }
}
</style>
