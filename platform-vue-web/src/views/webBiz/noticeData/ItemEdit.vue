<template>
  <el-dialog title="编辑公告信息"
             :center="true"
             @close="handleCancel"
             :visible.sync="editVisible">
    <div class="update-body">
      <el-form
          ref="submitForm"
          :model="submitData"
          :rules="validatorRules"
          label-width="130px"
          class="update-body-form"
      >
        <div class="text-area-view">
          <div class="update-item-view">
            <el-form-item label="标题" prop="dataTitle">
              <el-input
                  type="textarea"
                  :rows="5"
                  v-model="submitData.dataTitle"
                  placeholder="请填写-标题"
                  maxlength="200"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="text-area-view">
          <div class="update-item-view">
            <el-form-item label="备注信息" prop="remarkData">
              <el-input
                  type="textarea"
                  :rows="5"
                  v-model="submitData.remarkData"
                  placeholder="请填写-备注信息"
                  maxlength="200"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="update-body-div">
          <div class="update-item-view">
            <el-form-item label="是否上架" prop="dataStatus">
              <el-select
                  v-model="submitData.dataStatus"
                  :clearable="true"
                  placeholder="请选择是否上架">
                <el-option
                    v-for="(item,index) in yesOrNoOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <el-divider content-position="center">请填写详情介绍</el-divider>
      <Editor @change="getEditorContent" :value="editorContent" :isClear="true"></Editor>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button
          @click="handleCancel"
          size="mini">
        取 消
      </el-button>
      <el-button
          type="primary"
          @click="handleSubmit"
          size="mini">
        提 交
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import Api from "@/services";
import baseUtils from '@/utils/baseUtils'
import UploadImg from "@/components/UploadImg";
import Editor from '@/components/editor/Editor';

export default {
  components: {
    UploadImg: UploadImg,
    Editor: Editor,
  },
  name: "ItemEdit",
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
      title: "编辑",
      editVisible: false,
      submitData: {
        dataTitle: '',
        dataValue: '',
        remarkData: '',
        dataStatus: '',
      },
      validatorRules: {
        dataTitle: [
          {
            required: true,
            message: '请规范填写标题',
            trigger: 'blur'
          }
        ],
        dataValue: [
          {
            required: true,
            message: '请规范填写内容',
            trigger: 'blur'
          }
        ],
        remarkData: [
          {
            required: true,
            message: '请规范填写备注信息',
            trigger: 'blur'
          }
        ],
        dataStatus: [
          {
            required: true,
            message: '请选择是否上架',
            trigger: 'change'
          }
        ],
      }
    };
  },
  methods: {
    //拿到富文本内容
    getEditorContent(textValue) {
      this.editorContent = textValue
      console.log('textValue : ' + textValue)
      console.log('editorContent : ' + this.editorContent)
    },
    //处理展示
    showEdit(data) {
      console.log('data:' + JSON.stringify(data))
      if (data) {
        this.submitData = {
          ...data
        }
        this.editorContent = data.dataValue;
      }
      this.init();
      this.editVisible = true;
    },
    async setOtherData() {

    },
    //处理初始化
    async init() {
      await this.setOtherData();
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      loading.close();
    },
    clearAll() {
      console.log('触发清除所有')
      this.submitData = {
        dataTitle: '',
        dataValue: '',
        remarkData: '',
        dataStatus: '',
      }
      this.editorContent = ``;
    },
    handleCancel() {
      this.editVisible = false
      this.clearAll();
    },
    //处理提交
    handleSubmit() {
      this.$refs.submitForm.validate((valid) => {
        const params = {};
        if (valid) {
          if (!this.$isNull(this.editorContent)) {
            this.submitData.dataValue = this.editorContent
          } else {
            this.$message.error('请填写详情内容');
          }
          Object.assign(params, this.submitData);
          console.log("params", params);
        } else {
          this.$message.error('验证异常');
          return;
        }
        const loading = this.$loading({
          lock: true,
          text: "正在提交...",
        });
        try {
          Api.editNoticeDataItem(params).then((res) => {
            if (res.success) {
              this.editVisible = false;
              this.$emit("handleSuccess");
              this.$message({
                message: "操作成功!",
                type: "success",
              });
              this.clearAll()
            } else {
              this.$message.error('服务器异常');
            }
          });
          loading.close();
        } catch (error) {
          this.clearAll()
          loading.close();
          this.$message.error(error.message || error.msg || "服务器异常");
        }
      });
    },
  },
};
</script>
<style scoped lang="scss">
.update-body {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-content: center;
  align-items: center;

  .update-body-form {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    align-items: flex-start;
    width: 100%;

    .update-body-div {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-content: center;
      align-items: center;
      width: auto;

      .update-item-view {

      }

      .upload-item-view {
        .upload-item {

        }
      }
    }

    .text-area-view {
      width: 100%;

      .update-item-view {

      }
    }

    .dynamic-body-div {
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: flex-start;
      align-content: center;
      width: auto;
      border: 1px solid #575e57;
      margin: 2px 0px;
      width: 100%;

      .dynamic-row-view {
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: flex-start;
        align-content: center;
        width: auto;

        .dynamic-item-view {

        }

        .remove-button-view {
          margin: 10px 10px;
        }
      }
    }
  }
}
</style>
