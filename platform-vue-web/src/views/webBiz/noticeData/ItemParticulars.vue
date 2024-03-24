<template>
  <el-dialog
      title=""
      width="45%"
      :center="true"
      :visible.sync="particularsVisible">
    <div class="particulars-view">
      <div class="descriptions-title-view">
        公告信息详情
      </div>
      <div class="descriptions-view">
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">标题:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ particularsData.dataTitle }}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">内容:</el-tag>
            </div>
            <div class="descriptions-value">
              <el-tag size="medium" @click="showRichText(particularsData.dataValue)">点击查看</el-tag>
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">备注信息:</el-tag>
            </div>
            <div class="descriptions-value">
              {{ particularsData.remarkData }}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">是否上架:</el-tag>
            </div>
            <div class="descriptions-value">
              {{handleTypeByValue(particularsData.operatorId,authUserOptions)}}
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog append-to-body :visible.sync="previewVisible" title="图片预览">
      <img width="100%" :src="previewImageUrl" alt=""/>
    </el-dialog>
    <!--富文本查看弹窗-->
    <el-dialog
        append-to-body
        title="富文本内容"
        :visible.sync="richTextVisible"
        width="50%">
      <div v-html="richText"></div>
    </el-dialog>
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
      //-----------------
      editorContent: '',
      yesOrNoOptions: [
        {
          'text': '上架',
          'value': 1
        },
        {
          'text': '下架',
          'value': 2
        },
      ],
      //-----------------
      noticeDataOptions: [],
      richTextVisible: false,
      richText: ``,
      authUserOptions: [],
      particularsVisible: false,
      particularsData: {},
      size: '',
      previewImageUrl: '',
      previewVisible: false,
    }
  },
  methods: {
    async showRichText(richText) {
      console.log(richText)
      this.richText = richText
      this.richTextVisible = true
    },
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
    width: 100%;

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

    .table-view {
      width: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: flex-start;
      flex-wrap: wrap;

      .table-data-view {
        width: 100%;
      }

      .table-footer-view {

      }
    }
  }
}
</style>
