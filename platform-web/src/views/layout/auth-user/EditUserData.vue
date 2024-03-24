<template>
  <el-dialog title="编辑系统用户" :center="true" :visible.sync="editVisible" append-to-body>
    <div class="update-body">
      <el-form
          ref="submitForm"
          :model="submitData"
          :rules="validatorRules"
          label-width="100px"
          class="update-body-form"
      >
        <div class="update-body-div">
          <div class="update-item-view">
            <el-form-item label="用户名" prop="userName" >
              <el-input
                  v-model="submitData.userName" placeholder="请填写-用户名">
              </el-input>
            </el-form-item>
          </div>
          <div class="update-item-view">
            <el-form-item label="用户编号" prop="userNumber" >
              <el-input
                  v-model="submitData.userNumber" placeholder="请填写-用户编号">
              </el-input>
            </el-form-item>
          </div>
          <div class="update-item-view">
            <el-form-item label="手机号码" prop="phoneNumber" >
              <el-input
                  v-model="submitData.phoneNumber" placeholder="请填写-手机号码">
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="update-body-div">
          <div class="update-body-div">
            <div class="update-item-view">
              <el-form-item label="密码" prop="password" >
                <el-input
                    v-model="submitData.password" placeholder="请填写">
                </el-input>
              </el-form-item>
            </div>
          </div>
          <div class="update-item-view">
            <el-form-item label="真实姓名" prop="realName" >
              <el-input
                  v-model="submitData.realName" placeholder="请填写-真实姓名">
              </el-input>
            </el-form-item>
          </div>
          <div class="update-item-view">
            <el-form-item label="性别" prop="gender" >
              <el-select
                  v-model="submitData.gender"
                  :clearable="true"
                  @change="handleGenderChange"
                  placeholder="请选择性别">
                <el-option
                    v-for="item in genderOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        <div class="update-body-div">
          <div class="update-item-view">
            <el-form-item label="用户状态" prop="authStatus" >
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
          <div class="update-item-view">
            <el-form-item label="昵称" prop="nickName" >
              <el-input
                  v-model="submitData.nickName" placeholder="请填写-昵称">
              </el-input>
            </el-form-item>
          </div>
        </div>
        <el-divider content-position="center">上传头像</el-divider>
        <div class="update-body-div">
          <div class="update-item-view">
            <el-form-item label="封面图:" >
              <div class="upload-item-view">
                <UploadImg
                    class="upload-item"
                    ref="uploadAvatarUrl"
                    :uploadApi="uploadUrl"
                    :fileList="submitData.avatarList"
                    @subBack="uploadAvatarSuccess"
                    @handleFileDelete="uploadAvatarDelete"
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
    <el-button @click="handleCancel">取 消</el-button>
    <el-button type="primary" @click="handleSubmit">提 交</el-button>
  </span>
  </el-dialog>
</template>

<script>
import Api from "@/services";
import baseUtils from '@/utils/baseUtils'
import UploadImg from "@/components/UploadImg";
import bizConstants from '@/utils/bizConstants'
export default {
  components: {
    UploadImg:UploadImg
  },
  name:"UpdateUserProfile",
  data() {
    return {
      title: "编辑",
      editVisible: false,
      genderOptions:this.$bizConstants.genderOptions,
      booleanOptions:this.$bizConstants.booleanOptions,
      authStatusOptions:this.$bizConstants.authStatusOptions,
      allRoleOptions:[],
      submitData: {
        authUserId:'',
        userName:'',
        userNumber:'',
        phoneNumber:'',
        password:'',
        decryptionPassword:'',
        realName:'',
        gender:'',
        authStatus:'',
        avatarUrl:'',
        avatarList:[],
        nickName:'',
        authRoleIdList:[],
        age:'',
        idCardNum:'',
        userRegion:'',
        userAddress:'',
      },
      uploadUrl: process.env.BASE_API+`/api/platformFile/uploadFile`,
      deleteFileUrl: process.env.BASE_API+`/api/platformFile/deleteFileByFileName`,
      uploadHeaders:{
        token: localStorage.getItem('token') || ''
      },
      validatorRules:{
        userName: [
          { required: true, message: '请规范填写用户名', trigger: 'blur' }
        ],
        userNumber: [
          { required: true, message: '请规范填写用户编号', trigger: 'blur' }
        ],
        phoneNumber: [
          { required: true, message: '请规范填写手机号码',pattern: new RegExp(baseUtils.phoneNumberPattern(), "g"), trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请规范填写密码', trigger: 'blur' }
        ],
        realName: [
          { required: true, message: '请规范填写真实姓名', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '请规范选择性别', trigger: 'change' }
        ],
        age: [
          { required: true, pattern: new RegExp(baseUtils.numberPattern(), "g") ,
            message: '请规范填写年龄', trigger: 'blur' }
        ],
        idCardNum: [
          { required: true, message: '请规范填写身份证号', trigger: 'blur' }
        ],
        userRegion: [
          { required: true, message: '请规范填写地区', trigger: 'blur' }
        ],
        userAddress: [
          { required: true, message: '请规范填写地址', trigger: 'blur' }
        ],
        authStatus: [
          { required: true, message: '请规范选择用户状态', trigger: 'change' }
        ],
        nickName: [
          { required: true, message: '请规范填写昵称', trigger: 'blur' }
        ],
      }
    };
  },
  methods: {
    handleGenderChange(value){
      console.log('handleGenderChange:'+value)
      this.submitData.gender = value
    },
    handleAuthRoleChange(value){
      console.log('handleAuthRoleChange:'+JSON.stringify(value))
      this.submitData.authRoleIdList = value
    },
    handleAuthStatusChange(value){
      console.log('handleAuthStatusChange:'+value)
      this.submitData.authStatus = value
    },
    //上传文件成功
    uploadAvatarSuccess(fileName,file) {
      //console.log('上传文件成功返回:file:'+JSON.stringify(file));
      if(this.$isNull(fileName)){
        return
      }
      if(fileName.length === 0){
        return;
      }
      if(this.$isNull(file)){
        return
      }
      //随机数
      let uuId = baseUtils.randomString();
      //文件名称
      let fileNameItem = {
        status:'success',
        name:fileName,
        percentage:100,
        uid:uuId,
        raw:{
          uid:uuId
        },
        url:this.handleImageUrl(fileName)
      }
      if(this.$isNull(this.submitData.avatarList)){
        this.submitData.avatarList = new Array();
      }
      this.submitData.avatarList.push(fileNameItem);
      this.submitData.avatarUrl = fileName
      console.log('上传成功后的文件集合:'+JSON.stringify(this.submitData.avatarList))
    },
    //删除文件
    async uploadAvatarDelete(fileName) {
      console.log('删除文件fileName:'+fileName);
      if(this.$isNull(fileName)){
        return
      }
      if(this.$isNull(this.submitData.avatarList)){
        this.submitData.avatarList = new Array();
      }
      const index = this.submitData.avatarList.findIndex((item) => {
        return item.name == fileName
      })
      this.submitData.avatarList.splice(index, 1);
      const res = await Api.deleteFileApi({
        name:fileName
      });
      console.log('删除文件结果:'+JSON.stringify(res))
      if (res.success) {
        console.log('删除成功')
      }
      this.submitData.avatarUrl = ''
      console.log('删除后的文件集合:'+this.submitData.avatarList);
    },
    logOut() {
      this.$store.dispatch('LogOut').then(() => {
        localStorage.clear();
        this.$router.push(bizConstants.LOGIN_PAGE);
        location.reload()
      });
    },
    //处理展示
    async showEdit(data) {
      if(data){
        this.submitData = {
          ...data
        }
        //处理明文密码
        this.submitData.password = this.submitData.decryptionPassword
        await Api.queryRoleByAuthUserId({
          authUserId:this.submitData.authUserId
        }).then((res) => {
          if (res.success) {
            let data = res.data
            if(!this.$isNull(data)){
              let authUserRoleIdList = new Array();
              data.map(item => {
                authUserRoleIdList.push(item.authRoleId)
              })
              this.submitData.authRoleIdList = authUserRoleIdList
            }
          }else{
            this.$message.error(res.message || res.msg || "服务器异常");
          }
        })
        let avatarUrl = data.avatarUrl
        let avatarUrlUid = baseUtils.randomString()
        this.submitData.avatarList = this.$isNull(this.submitData.avatarList) ?
            new Array() :this.submitData.avatarList
        if(!this.$isNull(avatarUrl)){
          let item = {
            status:'success',
            name:avatarUrl,
            percentage:100,
            uid:avatarUrlUid,
            raw:{
              uid:avatarUrlUid
            },
            url:this.handleImageUrl(avatarUrl)
          }
          this.submitData.avatarList.push(item)
        }
      }
      await this.init();
      this.editVisible = true;
    },
    /*async queryUserSketch(){

    },*/
    async setOtherData(){
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
    clearAll(){
      console.log('触发清除所有')
      this.submitData = {
        authUserId:'',
        userName:'',
        userNumber:'',
        phoneNumber:'',
        password:'',
        decryptionPassword:'',
        realName:'',
        gender:'',
        authStatus:'',
        avatarUrl:'',
        avatarList:[],
        nickName:'',
        authRoleIdList:[],
        age:'',
        idCardNum:'',
        userRegion:'',
        userAddress:'',
      }
      //清除图片
      if(this.$baseUtils.isNull(this.$refs.uploadAvatarUrl)){
        this.$refs.uploadAvatarUrl.clearFiles()
      }
    },
    handleCancel(){
      this.editVisible = false
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
          //console.log("params", params);
        }else{
          this.$message.error('验证异常');
          return;
        }
        const loading = this.$loading({
          lock: true,
          text: "正在提交...",
        });
        try {
          Api.editAuthUserItem(params).then((res) => {
            if (res.success) {
              this.editVisible = false;
              this.$emit("handleSuccess");
              this.$message({
                message: "操作成功!",
                type: "success",
              });
              this.clearAll()
              this.logOut();
            } else {
              this.$message.error('服务器异常');
              this.clearAll()
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
.update-body{
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-content: center;
  align-items: center;
  .update-body-form{
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    align-items: flex-start;
    .update-body-div{
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-content: center;
      align-items: center;
      .update-item-view{

      }
      .upload-item-view{
        .upload-item{

        }
      }
    }
  }
}
</style>
