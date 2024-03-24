<template>
  <el-dialog
      title="新增${entityDesc}"
      :center="true"
      @close="handleCancel"
      :visible.sync="addVisible">
    <div class="data-body">
      <el-form
          ref="submitForm"
          :model="submitData"
          :rules="validatorRules"
          label-width="130px"
          class="data-body-form"
      >
        <#if idOptionsList?? && (idOptionsList?size > 0) >
        <#list idOptionsList as idOptionsItem>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="${idOptionsItem.columnNameDesc?replace('ID', '')}" prop="${idOptionsItem.camelCaseName}">
              <el-select
                  v-model="submitData.${idOptionsItem.camelCaseName}"
                  :clearable="true"
                  placeholder="请选择-${idOptionsItem.columnNameDesc?replace('ID', '')}信息">
                <el-option
                    v-for="(item,index) in ${idOptionsItem.camelCaseName?replace('Id', '')}Options"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        </#list>
        </#if>
        <#if columnList?? && (columnList?size > 0) >
        <#list columnList as column>
        <div class="data-body-div">
          <div class="data-item-view">
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
        </#if>
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
  name: "ItemAdd",
  data() {
    return {
      //-----------------
      <#if idOptionsList?? && (idOptionsList?size > 0) >
      <#list idOptionsList as idOptionsItem>
      ${idOptionsItem.camelCaseName?replace('Id', '')}Options: [],
      </#list>
      <#else>
      </#if>
      //-----------------
      title: "新增",
      addVisible: false,
      submitData: {
        <#if idOptionsList?? && (idOptionsList?size > 0) >
        <#list idOptionsList as idOptionsItem>
        ${idOptionsItem.camelCaseName}: '',
        </#list>
        <#else>
        </#if>
        <#if columnList?? && (columnList?size > 0) >
        <#list columnList as column>
        ${column.camelCaseName}: '',
        </#list>
        <#else>
        </#if>
      },
      validatorRules: {
        <#if idOptionsList?? && (idOptionsList?size > 0) >
        <#list idOptionsList as idOptionsItem>
        ${idOptionsItem.camelCaseName}: [
          {
            required: true,
            message: '请选择-${idOptionsItem.columnNameDesc?replace('ID', '')}',
            trigger: 'change'
          }
        ],
        </#list>
        <#else>
        </#if>
        <#if columnList?? && (columnList?size > 0) >
        <#list columnList as column>
        ${column.camelCaseName}: [
          {
            required: true,
            message: '请规范填写-${column.columnNameDesc}',
            trigger: 'blur'
          }
        ],
        </#list>
        <#else>
        </#if>
      }
    };
  },
  methods: {
    //处理展示
    async showAdd(data) {
      console.log('data:' + JSON.stringify(data))
      if (data) {

      }
      await this.init(data);
      this.addVisible = true;
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
      this.addVisible = false
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
              this.addVisible = false;
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
.data-body {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-content: center;
  align-items: center;
  .data-body-form {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    align-items: flex-start;
    width: 100%;
    .data-body-div {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-content: center;
      align-items: center;
      width: auto;
      .data-item-view {

      }
      .upload-item-view {
        .upload-item {

        }
      }
    }
    .text-area-view {
      width: 100%;
      .data-item-view {
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
