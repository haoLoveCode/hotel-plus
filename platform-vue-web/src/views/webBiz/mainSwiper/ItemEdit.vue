<template>
  <el-dialog title="编辑首页轮播图" :center="true" :visible.sync="editVisible">
    <div class="update-body">
      <el-form
          ref="submitForm"
          :model="submitData"
          :rules="validatorRules"
          label-width="200px"
          class="update-body-form"
      >
        <div class="update-body-div">
          <div class="update-item-view">
            <el-form-item label="标题" prop="mainTitle">
              <el-input
                  v-model="submitData.mainTitle"
                  placeholder="请填写-标题"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
          <div class="update-item-view">
            <el-form-item label="跳转地址" prop="routerUrl">
              <el-input
                  v-model="submitData.routerUrl"
                  placeholder="请填写-跳转地址"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="update-body-div">
          <div class="update-item-view">
            <el-form-item label="关联项" prop="itemId">
              <el-input
                  v-model="submitData.itemId"
                  placeholder="请填写-关联项"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <el-divider content-position="center">上传图片</el-divider>
        <div class="add-body-div">
          <div class="add-item-view">
            <el-form-item label="上传轮播图:">
              <div class="upload-item-view">
                <UploadImg
                    class="upload-item"
                    ref="uploadImage"
                    :uploadApi="uploadUrl"
                    :fileList="submitData.mainUrlList"
                    @subBack="uploadImageSuccess"
                    @handleFileDelete="uploadImageDelete"
                    :limit="1"
                    :headers="uploadHeaders"
                />
              </div>
            </el-form-item>
          </div>
        </div>
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
  name: "ItemEdit",
  data() {
    return {
      title: "编辑",
      editVisible: false,
      submitData: {
        mainUrl: '',
        mainTitle: '',
        routerUrl: '',
        itemId: '',
        mainUrlList: [],
      },
      uploadUrl: process.env.BASE_API + `/api/platformFile/uploadFile`,
      deleteFileUrl: process.env.BASE_API + `/api/platformFile/deleteFileByFileName`,
      uploadHeaders: {
        token: localStorage.getItem('token') || ''
      },
      validatorRules: {
        mainUrl: [
          {required: true, message: '请规范填写图片地址', trigger: 'blur'}
        ],
        mainTitle: [
          {required: false, message: '请规范填写标题', trigger: 'blur'}
        ],
        routerUrl: [
          {required: false, message: '请规范填写跳转地址', trigger: 'blur'}
        ],
        itemId: [
          {required: false, message: '请规范填写关联项', trigger: 'blur'}
        ],
      }
    };
  },
  methods: {
    // 上传图片回调
    uploadImageSuccess(fileName, file) {
      if (this.$isNull(fileName)) {
        return
      }
      if (fileName.length === 0) {
        return;
      }
      if (this.$isNull(file)) {
        return
      }
      //随机数
      let uuId = baseUtils.randomString();
      //文件名称
      let fileNameItem = {
        status: 'success',
        name: fileName,
        percentage: 100,
        uid: uuId,
        raw: {
          uid: uuId
        },
        url: this.handleImageUrl(fileName)
      }
      this.submitData.mainUrlList.push(fileNameItem);
      this.submitData.mainUrl = fileName
      console.log('上传成功后的文件集合:' + JSON.stringify(this.submitData.mainUrlList))
    },
    async uploadImageDelete(fileName) {
      console.log('删除文件fileName:' + fileName);
      if (this.$isNull(fileName)) {
        return
      }
      const index = this.submitData.mainUrlList.findIndex((item) => {
        return item.name == fileName
      })
      this.submitData.mainUrlList.splice(index, 1);
      const res = await Api.deleteFileApi({
        name: fileName
      });
      console.log('删除文件结果:' + JSON.stringify(res))
      if (res.success) {
        console.log('删除成功')
      }
      console.log('删除后的文件集合:' + this.submitData.mainUrlList);
      this.submitData.mainUrl = '';
    },
    //处理展示
    showEdit(data) {
      console.log('data:' + JSON.stringify(data))
      if (data) {
        this.submitData = {
          ...data
        }
      }
      let mainUrl = data.mainUrl
      let mainUrlUid = baseUtils.randomString()
      this.submitData.mainUrlList = this.$isNull(this.submitData.mainUrlList) ?
          new Array() : this.submitData.mainUrlList
      if (!this.$isNull(mainUrl)) {
        let item = {
          status: 'success',
          name: mainUrl,
          percentage: 100,
          uid: mainUrlUid,
          raw: {
            uid: mainUrlUid
          },
          url: this.handleImageUrl(mainUrl)
        }
        this.submitData.mainUrlList.push(item)
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
        mainUrl: '',
        mainTitle: '',
        routerUrl: '',
        itemId: '',
        mainUrlList: [],
      }
      //清除图片
      if (this.$baseUtils.isNull(this.$refs.uploadImage)) {
        this.$refs.uploadImage.clearFiles()
      }
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
          Api.editMainSwiperItem(params).then((res) => {
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
  }
}
</style>
