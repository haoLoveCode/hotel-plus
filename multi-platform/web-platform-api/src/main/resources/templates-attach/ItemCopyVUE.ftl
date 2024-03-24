<template>
  <el-dialog
          title="新增${entityDesc}"
          @close="handleCancel"
          :center="true"
          :visible.sync="copyVisible">
    <div class="copy-body">
      <el-form
        ref="submitForm"
        :model="submitData"
        :rules="validatorRules"
        label-width="130px"
        class="copy-body-form"
      >
      <#list columnList as column>
        <div class="copy-body-div">
          <div class="copy-item-view">
            <el-form-item label="${column.columnNameDesc}" prop="${column.camelCaseName}">
              <el-input
                  v-model="submitData.${column.camelCaseName}"
                  placeholder="请填写-${column.columnNameDesc}"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
      </#list>
      </el-form>
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
export default {
  components: {
    UploadImg: UploadImg
  },
  name: "ItemCopy",
  data() {
    return {
      //-----------------

      //-----------------
      title: "新增",
      copyVisible: false,
      submitData: {
        <#list columnList as column>
        ${column.camelCaseName}: '',
        </#list>
      },
      validatorRules: {
        <#list columnList as column>
        ${column.camelCaseName}: [
          {
            required: true,
            message: '请规范填写${column.columnNameDesc}',
            trigger: 'blur'
          }
        ],
        </#list>
      }
    };
  },
  methods: {
    //处理展示
    async showCopy(data) {
      console.log('data:' + JSON.stringify(data))
      if (data) {
        this.submitData = {
          ...data
        }
      }
      await this.init(data);
      this.copyVisible = true;
    },
    async setOtherData(data) {

    },
    //处理初始化
    async init(data) {
      await this.setOtherData(data);
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
        <#list columnList as column>
        ${column.camelCaseName}: '',
        </#list>
      }
    },
    handleCancel() {
      this.copyVisible = false
      this.clearAll();
    },
    //处理提交
    handleSubmit() {
      this.$refs.submitForm.validate((valid) => {
        const params = {};
        if (valid) {
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
          Api.add${className}Item(params).then((res) => {
            if (res.success) {
              this.copyVisible = false;
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
<#noparse>
<style scoped lang="scss">
.copy-body {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-content: center;
  align-items: center;
  .copy-body-form {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    align-items: flex-start;
    width: 100%;
    .copy-body-div {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-content: center;
      align-items: center;
      width: auto;
      .copy-item-view {

      }
      .upload-item-view {
        .upload-item {

        }
      }
    }
    .text-area-view {
      width: 100%;
      .copy-item-view {
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
</#noparse>
