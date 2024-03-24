<template>
  <el-dialog title="新增系统用户" :center="true" :visible.sync="addVisible">
    <div class="add-body">
      <el-form
          ref="submitForm"
          :model="submitData"
          :rules="validatorRules"
          label-width="100px"
          class="add-body-form"
      >
        <div class="add-body-div">
          <div class="add-item-view">
            <el-form-item label="用户名" prop="userName">
              <el-input
                  v-model="submitData.userName"
                  placeholder="请填写-用户名">
              </el-input>
            </el-form-item>
          </div>
          <div class="add-item-view">
            <el-form-item label="用户编号" prop="userNumber">
              <el-input
                  v-model="submitData.userNumber"
                  placeholder="请填写-用户编号">
              </el-input>
            </el-form-item>
          </div>
          <div class="add-item-view">
            <el-form-item label="手机号码" prop="phoneNumber">
              <el-input
                  v-model="submitData.phoneNumber"
                  placeholder="请填写-手机号码">
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="add-body-div">
          <div class="add-body-div">
            <div class="add-item-view">
              <el-form-item label="密码" prop="password">
                <el-input
                    v-model="submitData.password"
                    placeholder="请填写">
                </el-input>
              </el-form-item>
            </div>
          </div>
          <div class="add-item-view">
            <el-form-item label="真实姓名" prop="realName">
              <el-input
                  v-model="submitData.realName"
                  placeholder="请填写-真实姓名">
              </el-input>
            </el-form-item>
          </div>
          <div class="add-item-view">
            <el-form-item label="性别" prop="gender">
              <el-select
                  v-model="submitData.gender"
                  :clearable="true"
                  @change="handleGendersChange"
                  placeholder="请选择性别">
                <el-option
                    v-for="item in authUserGenderOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        <div class="add-body-div">
          <div class="add-item-view">
            <el-form-item label="用户状态" prop="authStatus">
              <el-select
                  v-model="submitData.authStatus"
                  :clearable="true"
                  @change="handleAuthStatusChange"
                  placeholder="请选择用户状态">
                <el-option
                    v-for="item in authStatusOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="add-item-view">
            <el-form-item label="昵称" prop="nickName">
              <el-input
                  v-model="submitData.nickName"
                  placeholder="请填写-昵称">
              </el-input>
            </el-form-item>
          </div>
          <div class="add-item-view">
            <el-form-item label="所属角色" prop="authRoleIdList">
              <el-select
                  v-model="submitData.authRoleIdList"
                  :clearable="true"
                  multiple
                  @change="handleAuthRoleChange"
                  placeholder="请选择所属角色">
                <el-option
                    v-for="item in allRoleOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        <el-divider content-position="center">上传头像</el-divider>
        <div class="add-body-div">
          <div class="add-item-view">
            <el-form-item label="用户头像图:">
              <div class="upload-item-view">
                <UploadImg
                    class="upload-item"
                    ref="uploadAvatarUrl"
                    :uploadApi="uploadUrl"
                    :fileList="submitData.avatarUrlList"
                    @subBack="uploadAvatarSuccess"
                    @handleFileDelete="handleAvatarDelete"
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
            size="mini"
            @click="handleCancel">
            取 消
        </el-button>
        <el-button
            type="primary"
            size="mini"
            @click="handleSubmit">
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
      title: "新增",
      addVisible: false,
      authUserGenderOptions: this.$bizConstants.authUserGenderOptions,
      booleanOptions: this.$bizConstants.booleanOptions,
      authStatusOptions: this.$bizConstants.authStatusOptions,
      allRoleOptions: [],
      submitData: {
        userName: '',
        userNumber: '',
        phoneNumber: '',
        password: '',
        realName: '',
        gender: '',
        authStatus: '',
        nickName: '',
        authRoleIdList: [],
        avatarUrl: '',
        avatarUrlList: [],
      },
      uploadUrl: process.env.BASE_API + `/api/platformFile/uploadFile`,
      deleteFileUrl: process.env.BASE_API + `/api/platformFile/deleteFileByFileName`,
      uploadHeaders: {
        token: localStorage.getItem('token') || ''
      },
      validatorRules: {
        userName: [
          {required: true, message: '请规范填写用户名', trigger: 'blur'}
        ],
        userNumber: [
          {required: true, message: '请规范填写用户编号', trigger: 'blur'}
        ],
        phoneNumber: [
          {required: true, message: '请规范填写手机号码', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请规范填写密码', trigger: 'blur'}
        ],
        realName: [
          {required: true, message: '请规范填写真实姓名', trigger: 'blur'}
        ],
        gender: [
          {required: true, message: '请规范选择性别', trigger: 'change'}
        ],
        authStatus: [
          {required: true, message: '请规范选择用户状态', trigger: 'change'}
        ],
        nickName: [
          {required: true, message: '请规范填写昵称', trigger: 'blur'}
        ],
      }
    };
  },
  methods: {
    handleGendersChange(value) {
      console.log('handleGendersChange:' + value)
      this.submitData.gender = value
    },
    handleAuthRoleChange(value) {
      console.log('handleAuthRoleChange:' + JSON.stringify(value))
      this.submitData.authRoleIdList = value
    },
    handleAuthStatusChange(value) {
      console.log('handleAuthStatusChange:' + value)
      this.submitData.authStatus = value
    },
    // 上传图片回调
    uploadAvatarSuccess(fileName, file) {
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
      this.submitData.avatarUrlList.push(fileNameItem);
      this.submitData.avatarUrl = fileName
      console.log('上传成功后的文件集合:' + JSON.stringify(this.submitData.avatarUrlList))
    },
    async handleAvatarDelete(fileName) {
      console.log('删除文件fileName:' + fileName);
      if (this.$isNull(fileName)) {
        return
      }
      const index = this.submitData.avatarUrlList.findIndex((item) => {
        return item.name == fileName
      })
      this.submitData.avatarUrlList.splice(index, 1);
      const res = await Api.deleteFileApi({
        name: fileName
      });
      console.log('删除文件结果:' + JSON.stringify(res))
      if (res.success) {
        console.log('删除成功')
      }
      console.log('删除后的文件集合:' + this.submitData.avatarUrlList);
      this.submitData.avatarUrl = '';
    },
    //处理展示
    showAdd(data) {
      console.log('data:' + JSON.stringify(data))
      if (data) {

      }
      this.init();
      this.addVisible = true;
    },
    async setOtherData() {
      this.allRoleOptions = await this.$bizConstants.allRoleOptions();
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
        userName: '',
        userNumber: '',
        phoneNumber: '',
        password: '',
        realName: '',
        gender: '',
        authStatus: '',
        avatarUrl: '',
        avatarUrlList: [],
        nickName: '',
        authRoleIdList: [],
      }
      //清除图片
      if (this.$baseUtils.isNull(this.$refs.uploadAvatarUrl)) {
        this.$refs.uploadAvatarUrl.clearFiles()
      }
    },
    handleCancel() {
      this.addVisible = false
      this.clearAll();
    },
    //处理提交
    handleSubmit() {
      this.$refs.submitForm.validate((valid) => {
        //处理明文密码
        this.submitData.decryptionPassword = this.submitData.password
        const params = {};
        if (valid) {
          Object.assign(params, this.submitData);
        } else {
          this.$message.error('验证异常');
          return;
        }
        const loading = this.$loading({
          lock: true,
          text: "正在提交...",
        });
        try {
          Api.addAuthUserItem(params).then((res) => {
            if (res.success) {
              this.addVisible = false;
              this.$emit("handleSuccess");
              this.$message({
                message: "操作成功!",
                type: "success",
              });
            } else {
              this.$message.error('服务器异常');
            }
            this.clearAll()
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
.add-body {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-content: center;
  align-items: center;

  .add-body-form {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    align-items: flex-start;

    .add-body-div {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-content: center;
      align-items: center;

      .add-item-view {

      }

      .upload-item-view {
        .upload-item {
        }
      }
    }
  }
}
</style>
