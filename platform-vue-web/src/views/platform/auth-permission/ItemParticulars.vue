<template>
  <el-dialog title="" :center="true" :visible.sync="particularsVisible">
    <div class="particulars-view">
      <div class="descriptions-title-view">
        系统权限详情
      </div>
      <div class="descriptions-view">
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">上级权限:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ handleTypeByValue(particularsData.parentId, permissionNodeOptions) }}
            </div>
          </div>
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">权限类型:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ handleTypeByValue(particularsData.permissionType, permissionTypeOptions) }}
            </div>
          </div>
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">排序:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ particularsData.sortIndex }}
            </div>
          </div>
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">权限路径:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ particularsData.permissionPath }}
            </div>
          </div>
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">权限编码:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ particularsData.permissionCode }}
            </div>
          </div>
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">备注:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ particularsData.permissionRemark }}
            </div>
          </div>
        </div>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
        <el-button
            @click="handleCancel"
            size="mini">
            关闭
        </el-button>
    </span>
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
      permissionTypeOptions: this.$bizConstants.permissionTypeOptions,
      permissionNodeOptions: [],
      authUserOptions: [],
      particularsVisible: false,
      particularsData: {},
      size: '',
    }
  },
  methods: {
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
    async queryPermNodeList() {
      const res = await Api.queryAllPermissionNodeList({});
      if (res.success) {
        let data = res.data;
        if (data) {
          let that = this
          data.map(item => {
            let option = {
              'text': item.permissionName,
              'value': item.authPermissionId
            }
            that.permissionNodeOptions.push(option);
          })
        }
      }
    },
    async showParticulars(data) {
      console.log('data:' + JSON.stringify(data))
      if (!this.$isNull(data)) {
        this.particularsData = {
          ...data
        }
      }
      await this.queryPermNodeList();
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
      this.permissionNodeOptions.splice(0, this.permissionNodeOptions.length)
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
